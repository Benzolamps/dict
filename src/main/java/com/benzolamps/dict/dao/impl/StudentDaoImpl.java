package com.benzolamps.dict.dao.impl;

import com.benzolamps.dict.bean.Clazz;
import com.benzolamps.dict.bean.Student;
import com.benzolamps.dict.bean.StudyProcess;
import com.benzolamps.dict.cfg.processor.annotation.ResourceContent;
import com.benzolamps.dict.dao.base.ClazzDao;
import com.benzolamps.dict.dao.base.StudentDao;
import com.benzolamps.dict.dao.core.*;
import com.benzolamps.dict.util.DictObject;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonMap;

/**
 * 学生Dao接口实现类
 * @author Benzolamps
 * @version 2.1.4
 * @datetime 2018-9-19 23:09:29
 */
@Repository("studentDao")
public class StudentDaoImpl extends BaseDaoImpl<Student> implements StudentDao {

    @Resource
    private ClazzDao clazzDao;

    @ResourceContent("classpath:sql/study_process.sql")
    private String studyProcessSql;

    @Override
    @SuppressWarnings({"IfCanBeSwitch", "serial"})
    public Page<Student> findPage(Pageable pageable) {
        DictQuery<Student> dictQuery = new GeneratedDictQuery<Student>() {
            @Override
            public void applySearch(Search search) {
                if (search.getField().equals("clazz")) {
                    Clazz clazz = clazzDao.find(DictObject.ofObject(search.getValue(), int.class));
                    getFilter().and(Filter.eq("clazz", clazz));
                } else if ("order".equals(search.getField())) {
                    switch (search.getValue().toString()) {
                        case "masteredWords asc": this.applyOrder(Order.asc("masteredWords")); return;
                        case "masteredWords desc": this.applyOrder(Order.desc("masteredWords")); return;
                        case "failedWords asc": this.applyOrder(Order.asc("failedWords")); return;
                        case "failedWords desc": this.applyOrder(Order.desc("failedWords")); return;
                        case "masteredPhrases asc": this.applyOrder(Order.asc("masteredPhrases")); return;
                        case "masteredPhrases desc": this.applyOrder(Order.desc("masteredPhrases")); return;
                        case "failedPhrases asc": this.applyOrder(Order.asc("failedPhrases")); return;
                        case "failedPhrases desc": this.applyOrder(Order.desc("failedPhrases"));
                    }

                } else if ("masteredWords".equals(search.getField())) {
                    applyRangeFilter(new Search("masteredWordsCount", search.getValue()));
                } else if ("failedWords".equals(search.getField())) {
                    applyRangeFilter(new Search("failedWordsCount", search.getValue()));
                } else if ("masteredPhrases".equals(search.getField())) {
                    applyRangeFilter(new Search("masteredPhrasesCount", search.getValue()));
                } else if ("failedPhrases".equals(search.getField())) {
                    applyRangeFilter(new Search("failedPhrasesCount", search.getValue()));
                } else {
                    super.applySearch(search);
                }
            }

            @Override
            public void applyOrder(Order order) {
                if (order.getField().equals("clazz")) {
                    order = new Order("clazz.name", order.getDirection());
                } else if (order.getField().equals("progress")) {
                    order = new Order.SizeOrder(null, order.getDirection()) {
                        @Override
                        protected void applyField(String field) {
                            super.applyField("masteredWords");
                            addSnippet(new OperatorSnippet("+"));
                            super.applyField("masteredPhrases");
                        }
                    };
                } else {
                    order = order.convertIf(Order.SizeOrder.class, "masteredWords", "masteredPhrases", "failedWords", "failedPhrases");
                }
                super.applyOrder(order);
            }
        };
        return super.findPage(dictQuery, pageable);
    }

    @Override
    @SuppressWarnings("unchecked")
    public StudyProcess[] getStudyProcess(Student student) {
        Assert.notNull(student, "student不能为null");
        Map<String, Object> parameters = singletonMap("student_id", student.getId());
        Query query = DictJpa.createNativeQuery(entityManager, studyProcessSql, StudyProcess.class, parameters);
        List list = query.list();
        return ((List<StudyProcess>) list).toArray(new StudyProcess[0]);
    }

    @Override
    public Student findByNumber(Integer number) {
        Assert.notNull(number, "student number不能为null");
        return findSingle(Filter.eq("number", number));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Number> findMaxInfo() {
        String jpql =
            "select " +
            "coalesce(max(student.masteredWordsCount), 0) as maxMasteredWordsCount, " +
            "coalesce(max(student.failedWordsCount), 0) as maxFailedWordsCount, " +
            "coalesce(max(student.masteredPhrasesCount), 0) as maxMasteredPhrasesCount, " +
            "coalesce(max(student.failedPhrasesCount), 0) as maxFailedPhrasesCount " +
            "from Student as student ";
        return (Map<String, Number>) DictJpa.createJpqlQuery(entityManager, jpql, null).uniqueResult();
    }
}
