package com.benzolamps.dict.dao.core;

import com.benzolamps.dict.bean.BaseEntity;
import com.benzolamps.dict.util.DictSpring;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.intellij.lang.annotations.Language;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.IntStream;

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
            @Language("JPAQL") String jpql,
            Class<T> tClass,
            Map<String, Object> parameters,
            Object... positionParameters) {
        Assert.notNull(entityManager, "entity manager不能为null");
        Assert.hasText(jpql, "sql不能为null或空");
        Assert.notNull(tClass, "t class不能为null");
        logger.info("jpql: " + jpql);

        TypedQuery<T> query = entityManager.createQuery(jpql, tClass);
        if (!CollectionUtils.isEmpty(parameters)) parameters.forEach(query::setParameter);
        if (!ObjectUtils.isEmpty(positionParameters)) {
            IntStream.range(0, positionParameters.length).forEach(i -> query.setParameter(i, positionParameters[i]));
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
            @Language("JPAQL") String jpql,
            Map<String, Object> parameters,
            Object... positionParameters) {
        Assert.notNull(entityManager, "entity manager不能为null");
        Assert.hasText(jpql, "sql不能为null或空");
        logger.info("jpql: " + jpql);

        Query query = entityManager.createQuery(jpql);
        if (!CollectionUtils.isEmpty(parameters)) parameters.forEach(query::setParameter);
        if (!ObjectUtils.isEmpty(positionParameters)) {
            IntStream.range(0, positionParameters.length).forEach(i -> query.setParameter(i, positionParameters[i]));
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
            @Language("MySQL") String sql,
            Class<?> tClass,
            Map<String, Object> parameters,
            Object... positionParameters) {

        Assert.notNull(entityManager, "entity manager不能为null");
        Assert.hasText(sql, "sql不能为null或空");
        Assert.notNull(tClass, "t class不能为null");
        logger.info("sql: " + sql);

        Query query = entityManager.createNativeQuery(sql);
        if (!CollectionUtils.isEmpty(parameters)) parameters.forEach(query::setParameter);
        if (!ObjectUtils.isEmpty(positionParameters)) {
            IntStream.range(0, positionParameters.length).forEach(i -> query.setParameter(i, positionParameters[i]));
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
            @Language("MySQL") String sql,
            Map<String, Object> parameters,
            Object... positionParameters) {

        Assert.notNull(entityManager, "entity manager不能为null");
        Assert.hasText(sql, "sql不能为null或空");
        logger.info("sql: " + sql);

        Query query = entityManager.createNativeQuery(sql);
        if (!CollectionUtils.isEmpty(parameters)) parameters.forEach(query::setParameter);
        if (!ObjectUtils.isEmpty(positionParameters)) {
            IntStream.range(0, positionParameters.length).forEach(i -> query.setParameter(i, positionParameters[i]));
        }
        query.executeUpdate();
    }

    /**
     * 执行一个原生SQL语句
     * @param sql SQL
     */
    @SneakyThrows(IOException.class)
    public static void executeSqlScript(@Language("MySQL") String sql) {
        Assert.hasText(sql, "sql不能为null或空");
        logger.info("sql: " + sql);
        executeSqlScript(new ByteArrayResource(sql.getBytes("UTF-8")));
    }

    /**
     * 执行一个原生SQL语句
     * @param sqlResource SQL资源
     */
    @SneakyThrows(SQLException.class)
    public static void executeSqlScript(Resource sqlResource) {
        Assert.notNull(sqlResource, "sql resource不能为null");
        DataSource dataSource = DictSpring.getBean(DataSource.class);
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, sqlResource);
        }
    }

    /**
     * 执行一个无事务的原生SQL语句
     * @param sql SQL
     * @param positionParameters 位置参数
     */
    @SuppressWarnings("unused")
    @SneakyThrows(SQLException.class)
    public static void executeNonTransactionNativeQuery(@Language("MySQL") String sql, Object... positionParameters) {
        Assert.hasText(sql, "sql不能为null或空");
        logger.info("sql: " + sql);
        DataSource dataSource = DictSpring.getBean(DataSource.class);

        try (var connection = dataSource.getConnection(); var statement = connection.prepareStatement(sql)) {
            for (int index = 0; index < positionParameters.length; index++) {
                statement.setObject(index + 1, positionParameters[index]);
            }
            statement.execute();
        }
    }
}
