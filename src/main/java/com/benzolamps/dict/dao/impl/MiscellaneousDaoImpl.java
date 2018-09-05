package com.benzolamps.dict.dao.impl;

import com.benzolamps.dict.dao.base.MiscellaneousDao;
import com.benzolamps.dict.dao.core.DictJpa;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * 杂项Dao接口实现类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-1 11:24:01
 */
@Repository("miscellaneousDao")
public class MiscellaneousDaoImpl implements MiscellaneousDao {

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    @Override
    public String getSQLiteVersion() {
        // language=SQLite
        String sql = "select sqlite_version();";
        List<String> results = DictJpa.createNativeQuery(entityManager, sql, String.class, null).list();
        return results.size() != 1 ? null : results.iterator().next();
    }
}
