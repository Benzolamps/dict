package com.benzolamps.dict.dao.impl;

import com.benzolamps.dict.bean.ShuffleSolution;
import com.benzolamps.dict.bean.ShuffleSolutions;
import com.benzolamps.dict.dao.base.ShuffleSolutionDao;
import com.benzolamps.dict.dao.core.Page;
import com.benzolamps.dict.dao.core.Pageable;
import com.benzolamps.dict.util.Constant;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringJoiner;

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

    @SuppressWarnings("unchecked")
    @Override
    public ShuffleSolution persist(ShuffleSolution shuffleSolution) {
        Assert.notNull(shuffleSolution, "shuffle solution不能为null");
        Assert.isNull(shuffleSolution.getId(), "shuffle solution必须为新建对象");

        Long id = 0L;
        for (val solution : solutions.getSolutions()) {
            id = Math.max(id, solution.getId());
        }
        shuffleSolution.setId(id + 1);
        StringJoiner sj = new StringJoiner(",", "{", "}");
        solutions.getSolutions().add(shuffleSolution);
        return shuffleSolution;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ShuffleSolution update(ShuffleSolution shuffleSolution) {
        Assert.notNull(shuffleSolution, "shuffle solution不能为null");
        Assert.notNull(shuffleSolution.getId(), "shuffle solution不能为新建对象");
        val ref = solutions.getSolutions().stream()
            .filter(solution -> solution.getId().equals(shuffleSolution.getId())).findFirst().get();
        ref.setName(shuffleSolution.getName());
        ref.setProperties(shuffleSolution.getProperties());
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
        solutions = tryFunc(() -> Constant.YAML.loadAs(resource.getInputStream(), ShuffleSolutions.class));
    }

    @Override
    public void flush() {
        tryAction(() -> Constant.YAML.dump(solutions, new FileWriter(resource.getFile())));
    }
}
