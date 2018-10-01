package com.benzolamps.dict.dao.impl;

import com.benzolamps.dict.bean.Student;
import com.benzolamps.dict.bean.Word;
import com.benzolamps.dict.bean.WordClazz;
import com.benzolamps.dict.dao.base.StudentDao;
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

    @Resource
    private StudentDao studentDao;

    @Override
    public Page<Word> findPage(Pageable pageable) {
        DictQuery<Word> dictQuery = new GeneratedDictQuery<Word>(Word.class) {

            private Boolean isMastered;

            private Integer studentId;

            @Override
            public void applySearch(Search search) {
                judge: {
                    if ("clazzes".equals(search.getField())) {
                        WordClazz wordClazz = wordClazzDao.find(DictObject.ofObject(search.getValue(), int.class));
                        getFilter().and(Filter.memberOf("clazzes", wordClazz));
                    } else if ("isMastered".equals(search.getField())) {
                        isMastered = DictObject.ofObject(search.getValue(), boolean.class);
                        if (studentId != null) break judge;

                    } else if ("studentId".equals(search.getField())) {
                        studentId = DictObject.ofObject(search.getValue(), int.class);
                        if (isMastered != null) break judge;
                    } else if (!"studentName".equals(search.getField())) {
                        super.applySearch(search);
                    }
                    return;
                }
                Student student = studentDao.find(studentId);
                getFilter().and(Filter.memberOf(isMastered ? "masteredStudents": "failedStudents", student));
            }

            @Override
            public void applyOrder(Order order) {
                order = order.convertIf(Order.IgnoreCaseOrder.class, "prototype", "americanPronunciation", "britishPronunciation");
                order = order.convertIf(Order.SizeOrder.class, "masteredStudents", "failedStudents");
                super.applyOrder(order);
            }
        };
        return super.findPage(dictQuery, pageable);
    }
}
