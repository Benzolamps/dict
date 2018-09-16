package com.benzolamps.dict.dao.impl;

import com.benzolamps.dict.bean.DocSolution;
import com.benzolamps.dict.bean.DocSolutions;
import com.benzolamps.dict.dao.base.DocSolutionDao;
import com.benzolamps.dict.exception.DictException;
import com.benzolamps.dict.util.Constant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.benzolamps.dict.util.DictResource.closeCloseable;

/**
 * Word文档方案Dao接口实现类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-11 21:21:02
 */
@Repository("docSolutionDao")
public class DocSolutionDaoImpl implements DocSolutionDao {

    /* 乱序方案配置文件 */
    @Value("templates/doc/config.yml")
    private FileSystemResource resource;

    private DocSolutions solutions;

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
    public void reload() {
        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
            solutions = Constant.YAML.loadAs(inputStream, DocSolutions.class);
        }  catch (IOException e) {
            throw new DictException(e);
        } finally {
            closeCloseable(inputStream);
        }
    }

    @Override
    public void flush() {
        File file = resource.getFile();
        OutputStream outputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        try {
            outputStream = new FileOutputStream(file);
            outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
            Constant.YAML.dump(solutions, outputStreamWriter);
        } catch (IOException e) {
            throw new DictException(e);
        } finally {
            closeCloseable(outputStreamWriter);
            closeCloseable(outputStream);
        }
    }

    @Override
    public Map<String, Object> getBaseProperties() {
        return solutions.getBaseProperties();
    }
}
