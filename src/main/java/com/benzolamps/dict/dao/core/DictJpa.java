package com.benzolamps.dict.dao.core;

import com.benzolamps.dict.bean.BaseEntity;
import com.benzolamps.dict.exception.DictException;
import com.benzolamps.dict.util.Constant;
import com.benzolamps.dict.util.DictSpring;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import static com.benzolamps.dict.util.DictLambda.tryAction;

/**
 * Jpql工具类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-1 11:07:23
 */
@Slf4j
public class DictJpa {

    /**
     * 创建一个JPQL查询
     * @param entityManager EntityManager
     * @param jpql JPQL
     * @param tClass 类型
     * @param parameters 参数
     * @param positionParameters 位置参数
     * @param <T> 类型
     * @return TypedQuery
     */
    @SuppressWarnings("unchecked")
    public static <T> TypedQuery<T> createJpqlQuery(
            EntityManager entityManager,
            String jpql,
            Class<T> tClass,
            Map<String, Object> parameters,
            Object... positionParameters) {
        Assert.notNull(entityManager, "entity manager不能为null");
        Assert.hasText(jpql, "sql不能为null或空");
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

    /**
     * 执行一个JPQL语句
     * @param entityManager EntityManager
     * @param jpql JPQL
     * @param parameters 参数
     * @param positionParameters 位置参数
     */
    @SuppressWarnings("unchecked")
    public static void executeJpqlQuery(
            EntityManager entityManager,
            String jpql,
            Map<String, Object> parameters,
            Object... positionParameters) {
        Assert.notNull(entityManager, "entity manager不能为null");
        Assert.hasText(jpql, "sql不能为null或空");
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

    /**
     * 创建一个原生SQL查询
     * @param entityManager EntityManager
     * @param sql SQL
     * @param tClass 类型
     * @param parameters 参数
     * @param positionParameters 位置参数
     * @return org.hibernate.Query
     */
    @SuppressWarnings("unchecked")
    public static org.hibernate.Query createNativeQuery(
            EntityManager entityManager,
            String sql,
            Class<?> tClass,
            Map<String, Object> parameters,
            Object... positionParameters) {
        Assert.notNull(entityManager, "entity manager不能为null");
        Assert.hasText(sql, "sql不能为null或空");
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

    /**
     * 执行一个原生SQL语句
     * @param entityManager EntityManager
     * @param sql SQL
     * @param parameters 参数
     * @param positionParameters 位置参数
     */
    @SuppressWarnings("unchecked")
    public static void executeNativeQuery(
            EntityManager entityManager,
            String sql,
            Map<String, Object> parameters,
            Object... positionParameters) {
        Assert.notNull(entityManager, "entity manager不能为null");
        Assert.hasText(sql, "sql不能为null或空");
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

    /**
     * 执行一个无事务的原生SQL语句
     * @param sql SQL
     * @param positionParameters 位置参数
     */
    public static void executeNonTransactionNativeQuery(String sql, Object... positionParameters) {
        Assert.hasText(sql, "sql不能为null或空");
        logger.info("sql: " + sql);
        DataSourceProperties dataSourceProperties = DictSpring.getBean(DataSourceProperties.class);
        try (var connection = DriverManager.getConnection(dataSourceProperties.getUrl())) {
            try (var statement = connection.prepareStatement(sql)) {
                tryAction(() -> ClassUtils.forName(dataSourceProperties.getDriverClassName(), DictSpring.getClassLoader()));
                for (int index = 0; index < positionParameters.length; index++) {
                    statement.setObject(index + 1, positionParameters[index]);
                }
                statement.execute();
            }
        } catch (SQLException e) {
            throw new DictException(e);
        }
    }
}
