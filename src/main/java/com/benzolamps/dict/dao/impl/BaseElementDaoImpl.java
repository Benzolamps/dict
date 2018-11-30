package com.benzolamps.dict.dao.impl;

import com.benzolamps.dict.bean.BaseElement;
import com.benzolamps.dict.bean.Library;
import com.benzolamps.dict.dao.base.BaseElementDao;
import com.benzolamps.dict.dao.core.DictJpa;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * 单词或短语类的基类Dao接口实现类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-5 22:45:47
 */
@SuppressWarnings("unchecked")
public class BaseElementDaoImpl<T extends BaseElement> extends BaseDaoImpl<T> implements BaseElementDao<T> {

    @Override
    public Set<String> findPrototypes(Library library) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> criteriaQuery = criteriaBuilder.createQuery(String.class);
        Root<T> root = criteriaQuery.from(entityClass);
        Predicate restrictions = criteriaBuilder.conjunction();
        criteriaQuery.where(restrictions, criteriaBuilder.equal(root.get("library"), library));
        criteriaQuery.select(root.get("prototype"));
        List<String> existsPrototypes = entityManager.createQuery(criteriaQuery).getResultList();
        return new HashSet<>(existsPrototypes);
    }

    @Override
    public int findMaxIndex(Library library) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Number> criteriaQuery = criteriaBuilder.createQuery(Number.class);
        Root<T> root = criteriaQuery.from(entityClass);
        criteriaQuery.where(criteriaBuilder.equal(root.get("library"), library));
        criteriaQuery.select(criteriaBuilder.max(root.get("index")));
        Number result = entityManager.createQuery(criteriaQuery).getSingleResult();
        return result != null ? result.intValue() : 0;
    }

    @Override
    public Map<String, Number> findMaxInfo(Library library) {
        String jpql =
            "select " +
            "coalesce(max(ele.masteredStudentsCount), 0) as maxMasteredStudentsCount, " +
            "coalesce(max(ele.failedStudentsCount), 0) as maxFailedStudentsCount, " +
            "coalesce(max(ele.frequency), 0) as maxFrequency " +
            "from " + entityClass.getSimpleName() + " as ele " +
            "where ele.library = :library";
        return (Map<String, Number>) DictJpa.createJpqlQuery(entityManager, jpql, Collections.singletonMap("library", library)).uniqueResult();
    }
}
