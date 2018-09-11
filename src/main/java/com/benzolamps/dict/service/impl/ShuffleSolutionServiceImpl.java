package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.bean.ShuffleSolution;
import com.benzolamps.dict.component.IShuffleStrategySetup;
import com.benzolamps.dict.controller.vo.DictPropertyInfoVo;
import com.benzolamps.dict.dao.base.ShuffleSolutionDao;
import com.benzolamps.dict.dao.core.Page;
import com.benzolamps.dict.dao.core.Pageable;
import com.benzolamps.dict.service.base.ShuffleSolutionService;
import com.benzolamps.dict.util.*;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

import static com.benzolamps.dict.util.DictLambda.tryFunc;

/**
 * 乱序方案Service接口实现类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-19 21:16:36
 */
@SuppressWarnings("unchecked")
@Service("shuffleSolutionService")
public class ShuffleSolutionServiceImpl implements ShuffleSolutionService {

    private static final String CACHE_NAME = "shuffle_solution";

    /* 动态类加载器 */
    @Value("#{new com.benzolamps.dict.util.DynamicClass(dictProperties.universePath)}")
    private DynamicClass dictDynamicClass;

    /* 可用的乱序策略Class */
    @Value("#{new java.util.HashSet()}")
    private Set<Class<IShuffleStrategySetup>> availableStrategySetups;

    @javax.annotation.Resource
    private ShuffleSolutionDao shuffleSolutionDao;

    @PostConstruct
    private void postConstruct() {
        /* 将检测到的IShuffleStrategySetup实体类加入 */
        for (val clazz : dictDynamicClass.getDynamicClassSet()) {
            if (new DictBean<>(clazz).instantiable() && IShuffleStrategySetup.class.isAssignableFrom(clazz)) {
                availableStrategySetups.add((Class<IShuffleStrategySetup>) clazz);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = CACHE_NAME, key = "#root.method")
    public Set<String> getAvailableStrategyNames() {
        return availableStrategySetups.stream().map(Class::getName).collect(Collectors.toSet());
    }

    @Override
    @Transactional(readOnly = true)
    public IShuffleStrategySetup getDefaultSolutionInstance() {
        ShuffleSolution shuffleSolution = new ShuffleSolution();
        shuffleSolution.setStrategyClass("rose.RoseShuffleStrategySetup");
        shuffleSolution.setProperties(new HashMap<>());
        return apply(shuffleSolution);
    }

    private IShuffleStrategySetup apply(ShuffleSolution shuffleSolution) {
        ClassLoader classLoader = DictSpring.getBean("classLoader");
        val strategyClass = tryFunc(() -> ClassUtils.forName(shuffleSolution.getStrategyClass(), classLoader));
        Properties properties = DictMap.convertToProperties(shuffleSolution.getProperties());
        return (IShuffleStrategySetup) new DictBean<>(strategyClass).createSpringBean(properties);
    }

    @Override
    @Transactional(readOnly = true)
    public IShuffleStrategySetup getSolutionInstanceAt(Integer id) {
        return apply(shuffleSolutionDao.find(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShuffleSolution> findAll() {
        List<ShuffleSolution> shuffleSolutions = shuffleSolutionDao.findAll();
        shuffleSolutions.sort((o1, o2) -> {
            if (Objects.equals(o1, o2)) {
                return 0;
            }
            if (o1 == null ^ o2 == null) {
                return o1 == null ? -1 : 1;
            }
            return o1.getOrder() - o2.getOrder();
        });
        return shuffleSolutions;
    }

    @Override
    public Page<ShuffleSolution> findPage(Pageable pageable) {
        return shuffleSolutionDao.findPage(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public ShuffleSolution find(Integer id) {
        return shuffleSolutionDao.find(id);
    }

    @Override
    @Transactional
    public ShuffleSolution persist(ShuffleSolution shuffleSolution) {
        Class<IShuffleStrategySetup> strategyClass = DynamicClass.loadClass(shuffleSolution.getStrategyClass());
        DictBean<IShuffleStrategySetup> dictBean = new DictBean<>(strategyClass);
        Collection<DictProperty> dictProperties = dictBean.getProperties();
        Map<String, Object> properties = shuffleSolution.getProperties();
        for (DictProperty dictProperty : dictProperties) {
            String key = dictProperty.getName();
            Class<?> type = dictProperty.getType();
            properties.put(key, DictObject.ofObject(properties.get(key), type));
        }
        return shuffleSolutionDao.persist(shuffleSolution);
    }

    @Override
    @Transactional
    public ShuffleSolution update(ShuffleSolution shuffleSolution) throws ClassNotFoundException {
        Class<IShuffleStrategySetup> strategyClass = (Class<IShuffleStrategySetup>) ClassUtils.forName(shuffleSolution.getStrategyClass(), DictSpring.getBean(ClassLoader.class));
        DictBean<IShuffleStrategySetup> dictBean = new DictBean<>(strategyClass);
        Collection<DictProperty> dictProperties = dictBean.getProperties();
        Map<String, Object> properties = shuffleSolution.getProperties();
        for (DictProperty dictProperty : dictProperties) {
            String key = dictProperty.getName();
            Class<?> type = dictProperty.getType();
            properties.put(key, DictObject.ofObject(properties.get(key), type));
        }
        return shuffleSolutionDao.update(shuffleSolution);
    }

    @Override
    @Transactional
    public void remove(Integer shuffleSolutionId) {
        shuffleSolutionDao.remove(shuffleSolutionId);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = CACHE_NAME, key = "#root.method + #className")
    public Collection<DictPropertyInfoVo> getShuffleSolutionPropertyInfo(String className) {
        Assert.hasLength(className, "class name不能为null或空");
        Assert.isTrue(getAvailableStrategyNames().contains(className), "class name不存在");
        Class strategyClass = DynamicClass.loadClass(className);
        return DictPropertyInfoVo.applyDictPropertyInfo(strategyClass);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isSpare() {
        return shuffleSolutionDao.findAll().size() < 10;
    }

    @Override
    @Transactional
    public void use(Integer id) {
        ShuffleSolution solution = find(id);
        Integer order = findAll().stream().mapToInt(ShuffleSolution::getOrder).max().orElse(0);
        solution.setOrder(order + 1);
    }
}
