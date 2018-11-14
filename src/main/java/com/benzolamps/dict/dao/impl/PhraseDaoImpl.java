package com.benzolamps.dict.dao.impl;

import com.benzolamps.dict.bean.Phrase;
import com.benzolamps.dict.bean.Student;
import com.benzolamps.dict.dao.base.PhraseDao;
import com.benzolamps.dict.dao.base.StudentDao;
import com.benzolamps.dict.dao.core.*;
import com.benzolamps.dict.util.DictObject;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Collection;

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

            private Integer studentNumber;

            @Override
            public void applySearch(Search search) {
                if ("frequency".equals(search.getField())) {
                    int flag = DictObject.ofObject(search.getValue(), int.class);
                    switch (flag) {
                        case 0: getFilter().and(Filter.le("frequency", 0)); return;
                        case 1: getFilter().and(Filter.betweenAnd("frequency", 1, 10)); return;
                        case 2: getFilter().and(Filter.betweenAnd("frequency", 11, 20)); return;
                        case 3: getFilter().and(Filter.betweenAnd("frequency", 21, 30)); return;
                        case 4: getFilter().and(Filter.betweenAnd("frequency", 31, 40)); return;
                        case 5: getFilter().and(Filter.betweenAnd("frequency", 41, 50)); return;
                        case 6: getFilter().and(Filter.betweenAnd("frequency", 51, 60)); return;
                        case 7: getFilter().and(Filter.betweenAnd("frequency", 61, 70)); return;
                        case 8: getFilter().and(Filter.betweenAnd("frequency", 71, 80)); return;
                        case 9: getFilter().and(Filter.betweenAnd("frequency", 81, 90)); return;
                        case 10: getFilter().and(Filter.betweenAnd("frequency", 91, 100)); return;
                        case 11: getFilter().and(Filter.betweenAnd("frequency", 101, 200)); return;
                        case 12: getFilter().and(Filter.betweenAnd("frequency", 201, 500)); return;
                        case 13: getFilter().and(Filter.betweenAnd("frequency", 501, 1000)); return;
                        case 14: getFilter().and(Filter.gt("frequency", 1000)); return;
                        case 15: getFilter().and(Filter.gt("frequency", 0));
                    }
                } else if ("isMastered".equals(search.getField())) {
                    isMastered = DictObject.ofObject(search.getValue(), boolean.class);
                } else if ("studentNumber".equals(search.getField())) {
                    studentNumber = DictObject.ofObject(search.getValue(), int.class);
                } else if (!"studentName".equals(search.getField())) {
                    super.applySearch(search);
                }
            }

            @Override
            public void applySearches(Collection<Search> searches) {
                super.applySearches(searches);
                if (studentNumber != null) {
                    Student student = studentDao.findByNumber(studentNumber);
                    if (isMastered != null) {
                        getFilter().and(Filter.memberOf(isMastered ? "masteredStudents" : "failedStudents", student));
                    }
                    searches.add(new Search("studentName", student.getName()));
                    searches.add(new Search("student", student));
                }
            }

            @Override
            public void applyOrder(Order order) {
                order = order.convertIf(Order.IgnoreCaseOrder.class, "prototype");
                order = order.convertIf(Order.SizeOrder.class, "masteredStudents", "failedStudents");
                super.applyOrder(order);
            }
        };
        return super.findPage(dictQuery, pageable);
    }
}
