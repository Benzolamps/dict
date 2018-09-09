package com.benzolamps.dict.dao.impl;

import com.benzolamps.dict.bean.Word;
import com.benzolamps.dict.bean.WordClazz;
import com.benzolamps.dict.dao.base.WordClazzDao;
import com.benzolamps.dict.dao.base.WordDao;
import com.benzolamps.dict.dao.core.*;
import com.benzolamps.dict.util.DictObject;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * 单词Dao接口实现类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-1 22:13:15
 */
@SuppressWarnings("unchecked")
@Repository("wordDao")
public class WordDaoImpl extends BaseElementDaoImpl<Word> implements WordDao {

    @Resource
    private WordClazzDao wordClazzDao;

    @Override
    public Page<Word> findPage(Pageable pageable) {
        DictQuery<Word> dictQuery = new GeneratedDictQuery<Word>(Word.class) {
            @Override
            public void applySearch(Search search) {
                if (search.getField().equals("clazzes")) {
                    WordClazz wordClazz = wordClazzDao.find(DictObject.ofObject(search.getValue(), Integer.class));
                    getFilter().and(Filter.memberOf("clazzes", wordClazz));
                } else {
                    super.applySearch(search);
                }
            }
        };
        return super.findPage(dictQuery, pageable);
    }
}
