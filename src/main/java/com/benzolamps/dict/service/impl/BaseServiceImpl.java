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

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service基类接口实现类
 * @param <T> 实体类型
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-1 21:52:46
 */
@Transactional
public abstract class BaseServiceImpl<T extends BaseEntity> implements BaseService<T> {

    @Autowired
    private BaseDao<T> baseDao;

    @Override
    @Transactional(readOnly = true)
    public T find(Integer id) {
        return baseDao.find(id);
    }

    @Override
    @Transactional(readOnly = true)
    public T findSingle(Filter filter) {
        this.handlerFilter(filter);
        return baseDao.findSingle(filter);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findList(Filter filter, Order... orders) {
        this.handlerFilter(filter);
        return baseDao.findList(filter, orders);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findList(Filter filter, List<Order> orders) {
        this.handlerFilter(filter);
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
        pageable.getOrders().add(Order.desc("id"));
        handlerFilter(pageable.getFilter());
        return baseDao.findPage(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer count(Filter filter) {
        this.handlerFilter(filter);
        return baseDao.count(filter);
    }

    @Override
    public T persist(T entity) {
        return baseDao.persist(entity);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public void persist(T... entities) {
        Arrays.stream(entities).forEach(baseDao::persist);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public void persist(Collection<T> entities) {
        entities.forEach(baseDao::persist);
    }

    @Override
    @Transactional
    public T update(T entity, String... ignoreProperties) {
        return baseDao.update(entity, ignoreProperties);
    }

    @Override
    public void update(Collection<T> entities, String... ignoreProperties) {
        entities.forEach(entity -> baseDao.update(entity, ignoreProperties));
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public void remove(T... entities) {
        this.remove(Arrays.asList(entities));
    }

    @Override
    public void remove(Integer... ids) {
        this.remove(Arrays.stream(ids).map(this::find).collect(Collectors.toList()));
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public void remove(Collection<T> entities) {
        baseDao.remove(entities);
    }

    @Override
    public void remove(Filter filter) {
        handlerFilter(filter);
        baseDao.remove(filter);
    }

    /**
     * 处理筛选
     * @param filter 筛选
     */
    protected void handlerFilter(Filter filter) {
    }
}