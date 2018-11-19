package com.benzolamps.dict.dao.impl;

import com.benzolamps.dict.dao.base.MiscellaneousDao;
import com.benzolamps.dict.dao.core.DictJpa;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 杂项Dao接口实现类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-1 11:24:01
 */
@Transactional
@Repository("miscellaneousDao")
public class MiscellaneousDaoImpl implements MiscellaneousDao {

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public String getMysqlVersion() {
        String sql = "select version();";
        return DictJpa.createNativeQuery(entityManager, sql, String.class, null).uniqueResult().toString();
    }

    @SuppressWarnings("unchecked")
    @Override
    public String getMysqlBaseDir() {
        String sql = "show variables where variable_name = 'basedir';";
        Map<String, String> variable = (Map<String, String>) DictJpa.createNativeQuery(entityManager, sql, Map.class, null).uniqueResult();
        return variable.get("Value").replaceAll("[\\\\/]+$", "");
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public void clear() {
        String sql = "show tables;";
        List<String> tables = DictJpa.createNativeQuery(entityManager, sql, String.class, null).list();
        String sqlClean = "optimize table " + String.join(", ", tables) + ";";
        DictJpa.executeNativeQuery(entityManager, sqlClean, null);
        DictJpa.executeSqlScript(tables.stream()
            .map(table -> /* language=MySQL */ "alter table " + table + " auto_increment = 1;")
            .collect(Collectors.joining("\n")));
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public long dataSize() {
        String sql = "select sum(t.data_length + t.index_length) from information_schema.tables as t;";
        Number result = (Number) DictJpa.createNativeQuery(entityManager, sql, Number.class, null).uniqueResult();
        return result.longValue();
    }

    @Override
    public void executeSqlScript(String sql) {
        DictJpa.executeSqlScript(sql);
    }

    @Override
    public void executeSqlScript(InputStream sqlStream) {
        DictJpa.executeSqlScript(new InputStreamResource(sqlStream));
    }
}
