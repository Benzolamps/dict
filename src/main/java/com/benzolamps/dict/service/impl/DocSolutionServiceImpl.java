package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.bean.DocSolution;
import com.benzolamps.dict.dao.base.DocSolutionDao;
import com.benzolamps.dict.service.base.DocSolutionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Word文档方案Service接口实现类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-19 21:16:36
 */
@SuppressWarnings("unchecked")
@Service("docSolutionService")
public class DocSolutionServiceImpl implements DocSolutionService {

    @Resource
    private DocSolutionDao docSolutionDao;

    @Override
    public List<DocSolution> findAll() {
        return docSolutionDao.findAll();
    }

    @Override
    public DocSolution find(Integer id) {
        return docSolutionDao.find(id);
    }

    @Override
    public Map<String, Object> getBaseProperties() {
        return docSolutionDao.getBaseProperties();
    }
}
