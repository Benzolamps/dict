package com.benzolamps.dict.dao.impl;

import com.benzolamps.dict.bean.Clazz;
import com.benzolamps.dict.controller.vo.StudyProcessVo;
import com.benzolamps.dict.dao.base.ClazzDao;
import com.benzolamps.dict.dao.core.*;
import com.benzolamps.dict.util.DictMap;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.persistence.TypedQuery;

/**
 * 班级Dao接口实现类
 * @author Benzolamps
 * @version 2.1.4
 * @datetime 2018-9-19 21:59:45
 */
@Repository("clazzDao")
public class ClazzDaoImpl extends BaseDaoImpl<Clazz> implements ClazzDao {

    @Override
    public Page<Clazz> findPage(Pageable pageable) {
        DictQuery<Clazz> dictQuery = new GeneratedDictQuery<Clazz>() {
            @Override
            public void applyOrder(Order order) {
                super.applyOrder(order.convertIf(Order.SizeOrder.class, "students"));
            }
        };
        return super.findPage(dictQuery, pageable);
    }

    @Override
    @SuppressWarnings("unchecked")
    public StudyProcessVo getWordStudyProcess(Clazz clazz) {
        Assert.notNull(clazz, "student不能为null");
        String jpql = "select new com.benzolamps.dict.controller.vo.StudyProcessVo(" +
            "avg(student.masteredWords.size), " +
            "avg(student.failedWords.size), " +
            "((select count(word) from Word as word) - avg(student.masteredWords.size) - avg(student.failedWords.size)), " +
            "(select count(word) from Word as word)) " +
            "from Student as student where student.clazz = :clazz";
        TypedQuery<StudyProcessVo> query = DictJpa.createJpqlQuery(
            entityManager, jpql, StudyProcessVo.class, DictMap.parse("clazz", clazz));
        return query.getSingleResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public StudyProcessVo getPhraseStudyProcess(Clazz clazz) {
        Assert.notNull(clazz, "student不能为null");
        String jpql = "select new com.benzolamps.dict.controller.vo.StudyProcessVo(" +
            "avg(student.masteredPhrases.size), " +
            "avg(student.failedPhrases.size), " +
            "((select count(phrase) from Phrase as phrase) - avg(student.masteredPhrases.size) - avg(student.failedPhrases.size)), " +
            "(select count(phrase) from Phrase as phrase)) " +
            "from Student as student where student.clazz = :clazz";
        TypedQuery<StudyProcessVo> query = DictJpa.createJpqlQuery(
            entityManager, jpql, StudyProcessVo.class, DictMap.parse("clazz", clazz));
        return query.getSingleResult();
    }

}
