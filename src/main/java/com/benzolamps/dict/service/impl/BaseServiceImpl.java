package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.bean.BaseEntity;
import com.benzolamps.dict.dao.base.BaseDao;
import com.benzolamps.dict.dao.core.Filter;
import com.benzolamps.dict.dao.core.Order;
import com.benzolamps.dict.dao.core.Page;
import com.benzolamps.dict.dao.core.Pageable;
import com.benzolamps.dict.service.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service基类接口实现类
 * @param <T> 实体类型
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-1 21:52:46
 */
@SuppressWarnings("deprecation")
@Transactional
public abstract class BaseServiceImpl<T extends BaseEntity> implements BaseService<T> {

    private BaseDao<T> baseDao;

    @Autowired
    @Transactional(readOnly = true)
    protected void setBaseDao(BaseDao<T> baseDao) {
        this.baseDao = baseDao;
    }

    @Override
    @Transactional(readOnly = true)
    public T find(Integer id) {
        return baseDao.find(id);
    }

    @Override
    @Transactional(readOnly = true)
    public T findSingle(Filter filter) {
        return baseDao.findSingle(filter);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findList(Filter filter, Order... orders) {
        return baseDao.findList(filter, orders);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findList(Filter filter, List<Order> orders) {
        return baseDao.findList(filter, orders);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll() {
        return baseDao.findList((Filter) null);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<T> findPage(Pageable pageable) {
        return baseDao.findPage(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Long count(Filter filter) {
        return baseDao.count(filter);
    }

    @Override
    @Transactional
    public T persist(T entity) {
        return baseDao.persist(entity);
    }

    @Override
    @Transactional
    public T update(T entity, String... ignoreProperties) {
        return baseDao.update(entity, ignoreProperties);
    }

    @Override
    @Transactional
    public void remove(T entity) {
      baseDao.remove(entity);
    }

    @Override
    public void remove(Integer id) {
        baseDao.remove(find(id));
    }
}