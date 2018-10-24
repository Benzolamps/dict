package com.benzolamps.dict.dao.impl;

import com.benzolamps.dict.bean.Phrase;
import com.benzolamps.dict.bean.Student;
import com.benzolamps.dict.dao.base.PhraseDao;
import com.benzolamps.dict.dao.base.StudentDao;
import com.benzolamps.dict.dao.core.*;
import com.benzolamps.dict.util.DictObject;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * 短语Dao接口实现类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-1 23:01:56
 */
@Repository("phraseDao")
public class PhraseDaoImpl extends BaseElementDaoImpl<Phrase> implements PhraseDao {

    @Resource
    private StudentDao studentDao;

    @Override
    public Page<Phrase> findPage(Pageable pageable) {
        DictQuery<Phrase> dictQuery = new GeneratedDictQuery<Phrase>() {

            private Boolean isMastered;

            private Integer studentId;

            @Override
            public void applyOrder(Order order) {
                order = order.convertIf(Order.IgnoreCaseOrder.class, "prototype");
                order = order.convertIf(Order.SizeOrder.class, "masteredStudents", "failedStudents");
                super.applyOrder(order);
            }

            @Override
            public void applySearch(Search search) {
                judge: {
                    if ("isMastered".equals(search.getField())) {
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
        };
        return super.findPage(dictQuery, pageable);
    }
}
