package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.bean.ShuffleSolution;
import com.benzolamps.dict.component.IShuffleStrategySetup;
import com.benzolamps.dict.controller.vo.DictPropertyInfoVo;
import com.benzolamps.dict.dao.base.ShuffleSolutionDao;
import com.benzolamps.dict.dao.core.Page;
import com.benzolamps.dict.service.base.ShuffleSolutionService;
import com.benzolamps.dict.util.DictBean;
import com.benzolamps.dict.util.DictMap;
import com.benzolamps.dict.util.DynamicClass;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 乱序策略Service接口实现类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-19 21:16:36
 */
@SuppressWarnings("unchecked")
@Service
public class ShuffleSolutionServiceImpl implements ShuffleSolutionService {

    private static final String CACHE_NAME = "shuffle_solution";

    /* 动态类加载器 */
    @Value("#{new com.benzolamps.dict.util.DynamicClass('${system.universe_path}')}")
    private DynamicClass dictDynamicClass;

    /* 可用的乱序策略Class */
    @Value("#{new java.util.HashSet()}")
    private Set<Class<IShuffleStrategySetup>> availableStrategySetups;

    @javax.annotation.Resource
    private ConfigurableApplicationContext context;

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
    public Set<String> getAvailableCopyStrategyNames() {
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
        val strategyClass = dictDynamicClass.loadDynamicClass(shuffleSolution.getStrategyClass());
        Properties properties = DictMap.convertToProperties(shuffleSolution.getProperties());
        return (IShuffleStrategySetup) new DictBean<>(strategyClass).createSpringBean(context, properties);
    }

    @Override
    @Transactional(readOnly = true)
    public IShuffleStrategySetup getSolutionInstanceAt(Long id) {
        return apply(shuffleSolutionDao.find(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ShuffleSolution> findAll() {
        return shuffleSolutionDao.findAll();
    }

    @Override
    public ShuffleSolution find(Long id) {
        return shuffleSolutionDao.find(id);
    }

    @Override
    public ShuffleSolution persist(ShuffleSolution shuffleSolution) {
        return shuffleSolutionDao.persist(shuffleSolution);
    }

    @Override
    public ShuffleSolution update(ShuffleSolution shuffleSolution) {
        return shuffleSolutionDao.update(shuffleSolution);
    }

    @Override
    public void remove(ShuffleSolution shuffleSolution) {
        shuffleSolutionDao.remove(shuffleSolution);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = CACHE_NAME, key = "#root.method + #className")
    public Collection<DictPropertyInfoVo> getShuffleSolutionPropertyInfo(String className) {
        Assert.hasLength(className, "class name不能为null或空");
        Assert.isTrue(getAvailableCopyStrategyNames().contains(className), "class name不存在");
        val strategyClass = dictDynamicClass.loadDynamicClass(className);
        return DictPropertyInfoVo.applyDictPropertyInfo(strategyClass);
    }
}
