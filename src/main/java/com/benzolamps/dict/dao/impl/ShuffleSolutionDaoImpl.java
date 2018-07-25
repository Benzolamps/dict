package com.benzolamps.dict.dao.impl;

import com.benzolamps.dict.bean.ShuffleSolution;
import com.benzolamps.dict.bean.ShuffleSolutions;
import com.benzolamps.dict.dao.base.ShuffleSolutionDao;
import com.benzolamps.dict.dao.core.Page;
import com.benzolamps.dict.dao.core.Pageable;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

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
    @Value("file:setting/shuffle-solutions.json")
    private Resource resource;

    private ShuffleSolutions solutions;

    @javax.annotation.Resource
    private ObjectMapper objectMapper;

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
        return solutions.getSolutions().stream().filter(solution -> solution.getId().equals(id)).findFirst().get();
    }

    @Override
    public ShuffleSolution persist(@NonNull ShuffleSolution shuffleSolution) {

        Assert.isNull(shuffleSolution.getId());

        Long id = 0L;
        for (val solution : solutions.getSolutions()) {
            id = Math.max(id, solution.getId());
        }

        shuffleSolution.setId(id + 1);
        solutions.getSolutions().add(shuffleSolution);
        return shuffleSolution;
    }

    @Override
    public ShuffleSolution update(ShuffleSolution shuffleSolution) {

        Assert.notNull(shuffleSolution);
        Assert.state(shuffleSolution.getId() != null);
        val ref = solutions.getSolutions().stream()
            .filter(solution -> solution.getId().equals(shuffleSolution.getId())).findFirst().get();
        ref.setName(shuffleSolution.getName());
        ref.setProperties(shuffleSolution.getProperties());
        ref.setRemark(shuffleSolution.getRemark());
        ref.setStrategyClass(shuffleSolution.getStrategyClass());
        return ref;
    }

    @Override
    public void remove(ShuffleSolution shuffleSolution) {
        Assert.notNull(shuffleSolution);
        Assert.state(shuffleSolution.getId() != null);
        val ref = solutions.getSolutions().stream()
            .filter(solution -> solution.getId().equals(shuffleSolution.getId())).findFirst().get();
        solutions.getSolutions().remove(ref);
    }

    @Override
    public void reload() {
        solutions = tryFunc(() -> objectMapper.readValue(resource.getFile(), ShuffleSolutions.class));
    }

    @Override
    public void flush() {
        tryAction(() -> objectMapper.writeValue(resource.getFile(), solutions));
    }
}
