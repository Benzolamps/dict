package com.benzolamps.dict.dao.impl;

import com.benzolamps.dict.dao.base.MiscellaneousDao;
import com.benzolamps.dict.dao.core.DictJpa;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

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
        List<String> results = DictJpa.createNativeQuery(entityManager, sql, String.class, null).list();
        return results.size() != 1 ? null : results.iterator().next();
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public void clear() {
        String sql = "show tables;";
        List<String> tables = DictJpa.createNativeQuery(entityManager, sql, String.class, null).list();
        String sqlClean = "optimize table " + String.join(", ", tables) + ";";
        DictJpa.executeNativeQuery(entityManager, sqlClean, null);
        DictJpa.executeNativeQueryBatch(tables.stream()
            .map(table -> /* language=MySQL */ "alter table " + table + " auto_increment = 1;").toArray(String[]::new));
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public long dataSize() {
        String sql = "select sum(t.data_length + t.index_length) from information_schema.tables as t;";
        List<Number> results = DictJpa.createNativeQuery(entityManager, sql, Number.class, null).list();
        return results.get(0).longValue();
    }
}
