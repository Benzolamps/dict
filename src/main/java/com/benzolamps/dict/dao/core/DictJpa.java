package com.benzolamps.dict.dao.core;

import com.benzolamps.dict.bean.BaseEntity;
import com.benzolamps.dict.util.DictSpring;
import com.benzolamps.dict.util.lambda.IntConsumer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.intellij.lang.annotations.Language;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.IntStream;

import static java.nio.charset.StandardCharsets.UTF_8;

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
     * @param <T> 类型
     * @param entityManager EntityManager
     * @param jpql JPQL
     * @param tClass 类型
     * @param parameters 参数
     * @param positionParameters 位置参数
     * @return TypedQuery
     */
    public static <T> TypedQuery<T> createJpqlQuery(
        EntityManager entityManager,
        String jpql,
        Class<T> tClass,
        Map<String, Object> parameters,
        Object... positionParameters) {

        Assert.notNull(entityManager, "entity manager不能为null");
        Assert.hasText(jpql, "sql不能为null或空");
        Assert.notNull(tClass, "t class不能为null");
        logger.info("jpql: " + jpql);
        TypedQuery<T> typedQuery = entityManager.createQuery(jpql, tClass);
        if (!CollectionUtils.isEmpty(parameters)) parameters.forEach(typedQuery::setParameter);
        if (!ObjectUtils.isEmpty(positionParameters)) {
            IntStream.range(0, positionParameters.length).forEach(i -> typedQuery.setParameter(i, positionParameters[i]));
        }
        return typedQuery;
    }

    /**
     * 创建一个JPQL查询
     * @param entityManager EntityManager
     * @param jpql JPQL
     * @param parameters 参数
     * @param positionParameters 位置参数
     * @return org.hibernate.Query
     */
    public static org.hibernate.Query createJpqlQuery(
            EntityManager entityManager,
            String jpql,
            Map<String, Object> parameters,
            Object... positionParameters) {

        Assert.notNull(entityManager, "entity manager不能为null");
        Assert.hasText(jpql, "sql不能为null或空");
        logger.info("jpql: " + jpql);
        Query query = entityManager.createQuery(jpql);
        org.hibernate.Query ohQuery = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        if (!CollectionUtils.isEmpty(parameters)) parameters.forEach(ohQuery::setParameter);
        if (!ObjectUtils.isEmpty(positionParameters)) {
            IntStream.range(0, positionParameters.length).forEach(i -> ohQuery.setParameter(i, positionParameters[i]));
        }
        return ohQuery;
    }

    /**
     * 执行一个JPQL语句
     * @param entityManager EntityManager
     * @param jpql JPQL
     * @param parameters 参数
     * @param positionParameters 位置参数
     */
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
        sql = sql.replace(":=", "\\:=");
        Query query = entityManager.createNativeQuery(sql);
        if (!CollectionUtils.isEmpty(parameters)) parameters.forEach(query::setParameter);
        if (!ObjectUtils.isEmpty(positionParameters)) {
            IntStream.range(0, positionParameters.length).forEach(i -> query.setParameter(i, positionParameters[i]));
        }

        SQLQuery sqlQuery = query.unwrap(SQLQuery.class);
        if (tClass.getPackage().equals(BaseEntity.class.getPackage())) {
            return sqlQuery.setResultTransformer(Transformers.aliasToBean(tClass));
        } else if (Map.class.isAssignableFrom(tClass) || Object.class.equals(tClass)) {
            return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        } else {
            return sqlQuery;
        }
    }

    /**
     * 执行一个原生SQL语句
     * @param entityManager EntityManager
     * @param sql SQL
     * @param parameters 参数
     * @param positionParameters 位置参数
     */
    public static void executeNativeQuery(
            EntityManager entityManager,
            @Language("MySQL") String sql,
            Map<String, Object> parameters,
            Object... positionParameters) {

        Assert.notNull(entityManager, "entity manager不能为null");
        Assert.hasText(sql, "sql不能为null或空");
        logger.info("sql: " + sql);
        sql = sql.replace(":=", "\\:=");
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
    public static void executeSqlScript(@Language("MySQL") String sql) {
        Assert.hasText(sql, "sql不能为null或空");
        logger.info("sql: " + sql);
        executeSqlScript(new ByteArrayResource(sql.getBytes(UTF_8)));
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
            ScriptUtils.executeSqlScript(connection, new EncodedResource(sqlResource, UTF_8));
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

        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            if (!ObjectUtils.isEmpty(positionParameters)) {
                IntStream.range(0, positionParameters.length).forEach((IntConsumer) i -> statement.setObject(i + 1, positionParameters[i]));
            }
            statement.execute();
        }
    }
}
