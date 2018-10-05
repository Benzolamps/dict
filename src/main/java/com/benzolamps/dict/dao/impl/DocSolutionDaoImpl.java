package com.benzolamps.dict.dao.impl;

import com.benzolamps.dict.bean.DocSolution;
import com.benzolamps.dict.bean.DocSolutions;
import com.benzolamps.dict.dao.base.DocSolutionDao;
import com.benzolamps.dict.util.Constant;
import com.benzolamps.dict.util.DictResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Word文档方案Dao接口实现类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-11 21:21:02
 */
@Repository("docSolutionDao")
public class DocSolutionDaoImpl implements DocSolutionDao {

    @Value("file:templates/doc/config.yml")
    private InputStream inputStream;

    private DocSolutions solutions;

    @PostConstruct
    private void postConstruct() {
        solutions = Constant.YAML.loadAs(inputStream, DocSolutions.class);
        DictResource.closeCloseable(inputStream);
    }

    @Override
    public List<DocSolution> findAll() {
        return new ArrayList<>(solutions.getSolutions());
    }

    @Override
    public DocSolution find(Integer id) {
        Assert.notNull(id, "id不能为null");
        return solutions.getSolutions().stream().filter(solution -> solution.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public Map<String, Object> getBaseProperties() {
        return solutions.getBaseProperties();
    }
}
