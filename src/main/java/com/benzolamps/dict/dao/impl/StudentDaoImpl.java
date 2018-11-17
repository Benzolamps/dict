package com.benzolamps.dict.dao.impl;

import com.benzolamps.dict.bean.Clazz;
import com.benzolamps.dict.bean.Student;
import com.benzolamps.dict.bean.StudyProcess;
import com.benzolamps.dict.dao.base.ClazzDao;
import com.benzolamps.dict.dao.base.StudentDao;
import com.benzolamps.dict.dao.core.*;
import com.benzolamps.dict.util.DictObject;
import org.hibernate.Query;
import org.intellij.lang.annotations.Language;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Collection;
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

    // language=SpEL
    @Value(
        "#{" +
        "T(org.springframework.util.StreamUtils)" +
        ".copyToString(new org.springframework.core.io.ClassPathResource('sql/study_process.sql').inputStream, 'UTF-8')" +
        "}"
    )
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
                    getFilter().and(SwitchOptions.switch1000("masteredWordsCount", DictObject.ofObject(search.getValue(), int.class), true, false, true, false));
                } else if ("failedWords".equals(search.getField())) {
                    getFilter().and(SwitchOptions.switch1000("failedWordsCount", DictObject.ofObject(search.getValue(), int.class), true, false, true, false));
                } else if ("masteredPhrases".equals(search.getField())) {
                    getFilter().and(SwitchOptions.switch1000("masteredPhrasesCount", DictObject.ofObject(search.getValue(), int.class), true, false, true, false));
                } else if ("failedPhrases".equals(search.getField())) {
                    getFilter().and(SwitchOptions.switch1000("failedPhrasesCount", DictObject.ofObject(search.getValue(), int.class), true, false, true, false));
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
    public Student findByNumber(Integer studentNumber) {
        Assert.notNull(studentNumber, "student number不能为null");
        return findSingle(Filter.eq("number", studentNumber));
    }

    @Override
    public void remove(Collection<Student> students) {
        for (Student student : students) {
            @Language("MySQL") String sqlGs = "delete from dict_gs where student = :id;";
            @Language("MySQL") String sqlGss = "delete from dict_gss where student = :id;";
            Map<String, Object> parameters = singletonMap("id", student.getId());
            executeNative(sqlGs, parameters);
            executeNative(sqlGss, parameters);
        }
        super.remove(students);
    }
}
