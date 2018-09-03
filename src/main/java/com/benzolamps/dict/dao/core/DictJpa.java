package com.benzolamps.dict.dao.core;

import com.benzolamps.dict.bean.BaseEntity;
import com.benzolamps.dict.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Map;

/**
 * Jpql工具类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-1 11:07:23
 */
@Slf4j
public class DictJpa {

    /**
     *
     * @param entityManager
     * @param jpql
     * @param tClass
     * @param parameters
     * @param positionParameters
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> TypedQuery<T> createJpqlQuery(
            EntityManager entityManager,
            String jpql,
            Class<T> tClass,
            Map<String, Object> parameters,
            Object... positionParameters) {
        Assert.notNull(entityManager, "entity manager不能为null");
        Assert.hasLength(jpql, "sql不能为null或空");
        Assert.notNull(tClass, "t class不能为null");
        if (parameters == null) parameters = Constant.EMPTY_MAP;
        if (positionParameters == null) positionParameters = Constant.EMPTY_OBJECT_ARRAY;
        logger.info("jpql: " + jpql);
        TypedQuery<T> query = entityManager.createQuery(jpql, tClass);
        parameters.forEach(query::setParameter);
        int bound = positionParameters.length;
        for (int i = 0; i < bound; i++) {
            query.setParameter(i, positionParameters[i]);
        }
        return query;
    }

    @SuppressWarnings("unchecked")
    public static void createJpqlQuery(
            EntityManager entityManager,
            String jpql,
            Map<String, Object> parameters,
            Object... positionParameters) {
        Assert.notNull(entityManager, "entity manager不能为null");
        Assert.hasLength(jpql, "sql不能为null或空");
        if (parameters == null) parameters = Constant.EMPTY_MAP;
        if (positionParameters == null) positionParameters = Constant.EMPTY_OBJECT_ARRAY;
        logger.info("jpql: " + jpql);
        Query query = entityManager.createQuery(jpql);
        parameters.forEach(query::setParameter);
        int bound = positionParameters.length;
        for (int i = 0; i < bound; i++) {
            query.setParameter(i, positionParameters[i]);
        }
        query.executeUpdate();
    }

    @SuppressWarnings("unchecked")
    public static org.hibernate.Query createNativeQuery(
            EntityManager entityManager,
            String sql,
            Class<?> tClass,
            Map<String, Object> parameters,
            Object... positionParameters) {
        Assert.notNull(entityManager, "entity manager不能为null");
        Assert.hasLength(sql, "sql不能为null或空");
        Assert.notNull(tClass, "t class不能为null");
        if (parameters == null) parameters = Constant.EMPTY_MAP;
        if (positionParameters == null) positionParameters = Constant.EMPTY_OBJECT_ARRAY;
        logger.info("sql: " + sql);
        Query query = entityManager.createNativeQuery(sql);
        parameters.forEach(query::setParameter);
        int bound = positionParameters.length;
        for (int i = 0; i < bound; i++) {
            query.setParameter(i, positionParameters[i]);
        }
        SQLQuery sqlQuery = query.unwrap(SQLQuery.class);
        if (!tClass.getPackage().equals(BaseEntity.class.getPackage())) {
            return sqlQuery;
        } else {
            return sqlQuery.setResultTransformer(Transformers.aliasToBean(tClass));
        }
    }

    @SuppressWarnings("unchecked")
    public static void executeNativeQuery(
            EntityManager entityManager,
            String sql,
            Map<String, Object> parameters,
            Object... positionParameters) {
        Assert.notNull(entityManager, "entity manager不能为null");
        Assert.hasLength(sql, "sql不能为null或空");
        if (parameters == null) parameters = Constant.EMPTY_MAP;
        if (positionParameters == null) positionParameters = Constant.EMPTY_OBJECT_ARRAY;
        logger.info("sql: " + sql);
        Query query = entityManager.createNativeQuery(sql);
        parameters.forEach(query::setParameter);
        int bound = positionParameters.length;
        for (int i = 0; i < bound; i++) {
            query.setParameter(i, positionParameters[i]);
        }
        query.executeUpdate();
    }
}