package com.benzolamps.dict.dao.impl;

import com.benzolamps.dict.bean.ShuffleSolution;
import com.benzolamps.dict.bean.ShuffleSolutions;
import com.benzolamps.dict.dao.base.ShuffleSolutionDao;
import com.benzolamps.dict.dao.core.Page;
import com.benzolamps.dict.dao.core.Pageable;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.yaml.snakeyaml.Yaml;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;

import static com.benzolamps.dict.util.DictLambda.tryAction;
import static com.benzolamps.dict.util.DictLambda.tryFunc;

/**
 * 乱序方案Dao接口实现类
 */
@SuppressWarnings("deprecation")
@Repository
public class ShuffleSolutionDaoImpl implements ShuffleSolutionDao {

    /* 乱序方案配置文件 */
    @Value("setting/shuffle-solutions.yml")
    private FileSystemResource resource;

    private ShuffleSolutions solutions;

    @Value("#{new org.yaml.snakeyaml.Yaml()}")
    private Yaml yaml;

    @Override
    public Page<ShuffleSolution> findAll() {
        Collection<ShuffleSolution> shuffleSolutions = solutions.getSolutions();
        Pageable pageable = new Pageable();
        pageable.setPageSize(10);
        pageable.setPageNumber(1);
        return new Page<>(new ArrayList<>(shuffleSolutions), (long) shuffleSolutions.size(), pageable);
    }

    @Override
    public ShuffleSolution find(Long id) {
        Assert.notNull(id, "id不能为null");
        return solutions.getSolutions().stream().filter(solution -> solution.getId().equals(id)).findFirst().get();
    }

    @Override
    public ShuffleSolution persist(ShuffleSolution shuffleSolution) {
        Assert.notNull(shuffleSolution, "shuffle solution不能为null");
        Assert.isNull(shuffleSolution.getId(), "shuffle solution必须为新建对象");

        Long id = 0L;
        for (val solution : solutions.getSolutions()) {
            id = Math.max(id, solution.getId());
        }
        shuffleSolution.setId(id + 1);
        for (String key : shuffleSolution.getProperties().keySet()) {
            Object value = shuffleSolution.getProperties().get(key);
            if (value == null) continue;
            if ("true".equalsIgnoreCase(value.toString())) {
                value = Boolean.TRUE;
            } else if ("false".equalsIgnoreCase(value.toString())) {
                value = Boolean.FALSE;
            } else {
                try {
                    if (value.toString().contains(".")) {
                        value = Double.valueOf(value.toString());
                    } else {
                        value = Long.valueOf(value.toString());
                    }
                } catch (NumberFormatException e) {
                }
            }
            shuffleSolution.getProperties().put(key, value);
        }
        solutions.getSolutions().add(shuffleSolution);
        return shuffleSolution;
    }

    @Override
    public ShuffleSolution update(ShuffleSolution shuffleSolution) {
        Assert.notNull(shuffleSolution, "shuffle solution不能为null");
        Assert.notNull(shuffleSolution.getId(), "shuffle solution不能为新建对象");
        val ref = solutions.getSolutions().stream()
            .filter(solution -> solution.getId().equals(shuffleSolution.getId())).findFirst().get();
        ref.setName(shuffleSolution.getName());
        ref.setProperties(shuffleSolution.getProperties());
        for (String key : ref.getProperties().keySet()) {
            Object value = ref.getProperties().get(key);
            if (value == null) continue;
            if ("true".equalsIgnoreCase(value.toString())) {
                value = Boolean.TRUE;
            } else if ("false".equalsIgnoreCase(value.toString())) {
                value = Boolean.FALSE;
            } else {
                try {
                    if (value.toString().contains(".")) {
                        value = Double.valueOf(value.toString());
                    } else {
                        value = Long.valueOf(value.toString());
                    }
                } catch (NumberFormatException e) {
                }
            }
            ref.getProperties().put(key, value);
        }
        ref.setRemark(shuffleSolution.getRemark());
        ref.setStrategyClass(shuffleSolution.getStrategyClass());
        return ref;
    }

    @Override
    public void remove(final Long shuffleSolutionId) {
        Assert.notNull(shuffleSolutionId, "shuffle solution id不能为null");
        val ref = solutions.getSolutions().stream()
            .filter(solution -> solution.getId().equals(shuffleSolutionId)).findFirst().get();
        solutions.getSolutions().remove(ref);
    }

    @Override
    public void reload() {
        solutions = tryFunc(() -> yaml.loadAs(resource.getInputStream(), ShuffleSolutions.class));
    }

    @Override
    public void flush() {
        tryAction(() -> yaml.dump(solutions, new FileWriter(resource.getFile())));
    }
}
