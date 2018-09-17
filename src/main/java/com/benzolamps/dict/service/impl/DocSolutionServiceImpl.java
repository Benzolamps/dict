package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.bean.DocSolution;
import com.benzolamps.dict.dao.base.DocSolutionDao;
import com.benzolamps.dict.service.base.DocSolutionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

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
    @Transactional(readOnly = true)
    public List<DocSolution> findAll() {
        List<DocSolution> docSolutions = docSolutionDao.findAll();
        docSolutions.sort(Comparator.comparing(DocSolution::getOrder));
        return docSolutions;
    }

    @Override
    @Transactional(readOnly = true)
    public DocSolution find(Integer id) {
        return docSolutionDao.find(id);
    }

    @Override
    @Transactional
    public void use(Integer id) {
        DocSolution solution = find(id);
        Integer order = findAll().stream().mapToInt(DocSolution::getOrder).max().orElse(0);
        solution.setOrder(order + 1);
    }

    @Override
    public Map<String, Object> getBaseProperties() {
        return docSolutionDao.getBaseProperties();
    }
}
