package com.benzolamps.dict.dao.impl;

import com.benzolamps.dict.bean.Clazz;
import com.benzolamps.dict.bean.Student;
import com.benzolamps.dict.controller.vo.StudyProcessVo;
import com.benzolamps.dict.dao.base.ClazzDao;
import com.benzolamps.dict.dao.base.StudentDao;
import com.benzolamps.dict.dao.core.*;
import com.benzolamps.dict.util.DictMap;
import com.benzolamps.dict.util.DictObject;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.persistence.TypedQuery;

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

    @Override
    public Page<Student> findPage(Pageable pageable) {
        DictQuery<Student> dictQuery = new GeneratedDictQuery<Student>() {
            @Override
            public void applySearch(Search search) {
                if (search.getField().equals("clazz")) {
                    Clazz clazz = clazzDao.find(DictObject.ofObject(search.getValue(), int.class));
                    getFilter().and(Filter.eq("clazz", clazz));
                } else {
                    super.applySearch(search);
                }
            }

            @SuppressWarnings({"IfCanBeSwitch", "serial"})
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
    public StudyProcessVo getWordStudyProcess(Student student) {
        Assert.notNull(student, "student不能为null");
        String jpql = "select new com.benzolamps.dict.controller.vo.StudyProcessVo(" +
            "student.masteredWords.size, " +
            "student.failedWords.size, " +
            "((select count(word) from Word as word) - student.masteredWords.size - student.failedWords.size), " +
            "(select count(word) from Word as word)) " +
            "from Student as student where student = :student";
        TypedQuery<StudyProcessVo> query = DictJpa.createJpqlQuery(
                entityManager, jpql, StudyProcessVo.class, DictMap.parse("student", student));
        return query.getSingleResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public StudyProcessVo getPhraseStudyProcess(Student student) {
        Assert.notNull(student, "student不能为null");
        String jpql = "select new com.benzolamps.dict.controller.vo.StudyProcessVo(" +
            "student.masteredPhrases.size, " +
            "student.failedPhrases.size, " +
            "((select count(phrase) from Phrase as phrase) - student.masteredPhrases.size - student.failedPhrases.size), " +
            "(select count(phrase) from Phrase as phrase)) " +
            "from Student as student where student = :student";
        TypedQuery<StudyProcessVo> query = DictJpa.createJpqlQuery(
            entityManager, jpql, StudyProcessVo.class, DictMap.parse("student", student));
        return query.getSingleResult();
    }
}
