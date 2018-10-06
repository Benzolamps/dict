package com.benzolamps.dict.dao.impl;

import com.benzolamps.dict.bean.ShuffleSolution;
import com.benzolamps.dict.bean.ShuffleSolutions;
import com.benzolamps.dict.dao.base.ShuffleSolutionDao;
import com.benzolamps.dict.dao.core.Order;
import com.benzolamps.dict.dao.core.Page;
import com.benzolamps.dict.dao.core.Pageable;
import com.benzolamps.dict.exception.DictException;
import com.benzolamps.dict.util.Constant;
import lombok.val;
import lombok.var;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.benzolamps.dict.util.DictLambda.tryFunc;

/**
 * 乱序方案Dao接口实现类
 */
@Repository("shuffleSolutionDao")
public class ShuffleSolutionDaoImpl implements ShuffleSolutionDao {

    /* 乱序方案配置文件 */
    @Value("setting/shuffle-solutions.yml")
    private FileSystemResource resource;

    /* 默认乱序方案配置文件 */
    // language=SpEL
    @Value(
        "#{" +
        "T(com.benzolamps.dict.util.Constant).YAML.loadAs(" +
        "new org.springframework.core.io.FileSystemResource(dictProperties.universePath + '/default.yml').inputStream, " +
        "T(com.benzolamps.dict.bean.ShuffleSolution)" +
        ")" +
        "}"
    )
    private ShuffleSolution defaultSolution;

    private ShuffleSolutions solutions;

    @Override
    public List<ShuffleSolution> findAll() {
        return new ArrayList<>(solutions.getSolutions());
    }

    @Override
    public Page<ShuffleSolution> findPage(Pageable pageable) {
        List<ShuffleSolution> shuffleSolutions = this.findAll();
        pageable.setPageSize(10);
        pageable.setPageNumber(1);
        pageable.getOrders().add(Order.desc("id"));
        return new Page<>(new ArrayList<>(shuffleSolutions), shuffleSolutions.size(), pageable);
    }

    @Override
    public ShuffleSolution find(Integer id) {
        Assert.notNull(id, "id不能为null");
        return solutions.getSolutions().stream().filter(solution -> solution.getId().equals(id)).findFirst().orElse(null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public ShuffleSolution persist(ShuffleSolution shuffleSolution) {
        Assert.notNull(shuffleSolution, "shuffle solution不能为null");
        Assert.isNull(shuffleSolution.getId(), "shuffle solution必须为新建对象");

        Integer id = solutions.getSolutions().stream().mapToInt(ShuffleSolution::getId).max().orElse(-1);
        shuffleSolution.setId(id + 1);
        solutions.getSolutions().add(shuffleSolution);
        return shuffleSolution;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ShuffleSolution update(ShuffleSolution shuffleSolution) {
        Assert.notNull(shuffleSolution, "shuffle solution不能为null");
        Assert.notNull(shuffleSolution.getId(), "shuffle solution不能为新建对象");
        val ref = solutions.getSolutions().stream()
            .filter(solution -> solution.getId().equals(shuffleSolution.getId()))
            .findFirst().orElseThrow(() -> new IllegalArgumentException("shuffle solution不存在"));
        Assert.notNull(ref, "shuffle solution不存在");
        ref.setName(shuffleSolution.getName());
        ref.setProperties(shuffleSolution.getProperties());
        ref.setRemark(shuffleSolution.getRemark());
        ref.setStrategyClass(shuffleSolution.getStrategyClass());
        return ref;
    }

    @Override
    public void remove(final Integer shuffleSolutionId) {
        Assert.notNull(shuffleSolutionId, "shuffle solution id不能为null");
        val ref = solutions.getSolutions().stream()
            .filter(solution -> solution.getId().equals(shuffleSolutionId))
            .findFirst().orElseThrow(() -> new IllegalArgumentException("shuffle solution不存在"));
        solutions.getSolutions().remove(ref);
    }

    @Override
    public void reload() {
        if (resource.exists()) {
            try (InputStream inputStream = resource.getInputStream()) {
                solutions = Constant.YAML.loadAs(inputStream, ShuffleSolutions.class);
            } catch (IOException e) {
                throw new DictException(e);
            }
        }

        if (solutions == null) {
            solutions = new ShuffleSolutions();
        }

        Set<ShuffleSolution> shuffleSolutions = solutions.getSolutions();

        if (shuffleSolutions == null) {
            solutions.setSolutions(shuffleSolutions = new HashSet<>());
        }

        if (shuffleSolutions.isEmpty()) {
            shuffleSolutions.add(defaultSolution);
        }
    }

    @Override
    public void flush() {
        File file = resource.getFile();
        if (file.exists() || file.getParentFile().mkdirs() && tryFunc(file::createNewFile)) {
            try (var os = new FileOutputStream(file); var osw = new OutputStreamWriter(os, "UTF-8") ) {
                Constant.YAML.dump(solutions, osw);
            }  catch (IOException e) {
                throw new DictException(e);
            }
        }
    }
}
