package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.bean.WordClazz;
import com.benzolamps.dict.dao.base.WordClazzDao;
import com.benzolamps.dict.dao.core.Filter;
import com.benzolamps.dict.dao.core.Order;
import com.benzolamps.dict.service.base.WordClazzService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

/**
 * 词性Service接口实现类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-1 22:24:02
 */
@Service("wordClazzService")
@Transactional
public class WordClazzServiceImpl extends BaseServiceImpl<WordClazz> implements WordClazzService {

    @Resource
    private WordClazzDao wordClazzDao;

    @Override
    public List<WordClazz> findAll() {
        return findList(null, Order.desc("words.size"));
    }

    @Override
    @Transactional(readOnly = true)
    public WordClazz findByName(String name) {
        Assert.hasLength(name, "name不能为null或空");
        return wordClazzDao.findSingle(Filter.eq("name", name));
    }

    @Override
    @Transactional(readOnly = true)
    public WordClazz findByIdOrName(String name) {
        Assert.hasLength(name, "name不能为null或空");
        try {
            return wordClazzDao.find(Integer.valueOf(name));
        } catch (NumberFormatException e) {
            return findByName(name);
        }
    }
}
