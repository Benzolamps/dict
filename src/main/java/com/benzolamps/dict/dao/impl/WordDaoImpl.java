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
import java.util.Collection;

/**
 * 单词Dao接口实现类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-1 22:13:15
 */
@Repository("wordDao")
public class WordDaoImpl extends BaseElementDaoImpl<Word> implements WordDao {

    @Resource
    private WordClazzDao wordClazzDao;

    @Resource
    private StudentDao studentDao;

    @Override
    public Page<Word> findPage(Pageable pageable) {
        DictQuery<Word> dictQuery = new GeneratedDictQuery<Word>() {

            private Boolean isMastered;

            private Integer studentNumber;

            @Override
            public void applySearch(Search search) {
                if ("clazzes".equals(search.getField())) {
                    WordClazz wordClazz = wordClazzDao.find(DictObject.ofObject(search.getValue(), int.class));
                    getFilter().and(Filter.memberOf("clazzes", wordClazz));
                } else if ("order".equals(search.getField())) {
                    switch (search.getValue().toString()) {
                        case "masteredStudents asc": this.applyOrder(Order.asc("masteredStudents")); return;
                        case "masteredStudents desc": this.applyOrder(Order.desc("masteredStudents")); return;
                        case "failedStudents asc": this.applyOrder(Order.asc("failedStudents")); return;
                        case "failedStudents desc": this.applyOrder(Order.desc("failedStudents"));
                    }
                } else if ("masteredStudents".equals(search.getField())) {
                    applyRangeFilter(new Search("masteredStudentsCount", search.getValue()));
                } else if ("failedStudents".equals(search.getField())) {
                    applyRangeFilter(new Search("failedStudentsCount", search.getValue()));
                } else if ("frequency".equals(search.getField())) {
                    applyRangeFilter(search);
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
                searches.removeIf(search -> search.getField().equals("studentName"));
                super.applySearches(searches);
                if (studentNumber != null) {
                    Student student = studentDao.findByNumber(studentNumber);
                    if (isMastered != null) {
                        getFilter().and(Filter.memberOf(isMastered ? "masteredStudents" : "failedStudents", student));
                    }
                    if (student != null) {
                        searches.add(new Search("studentName", student.getName()));
                        searches.add(new Search("student", student));
                    }
                }
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
