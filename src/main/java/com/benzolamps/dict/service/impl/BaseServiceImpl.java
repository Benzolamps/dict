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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
        this.handleFilter(filter);
        return baseDao.findSingle(filter);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findList(Filter filter, Order... orders) {
        this.handleFilter(filter);
        List<Order> orderList = new ArrayList<>(Arrays.asList(orders));
        this.handleOrder(orderList);
        return baseDao.findList(filter, orderList.toArray(orders));
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findList(Filter filter, List<Order> orders) {
        this.handleFilter(filter);
        Order[] orderArray = orders.toArray(new Order[0]);
        this.handleOrder(orders);
        return baseDao.findList(filter, new ArrayList<>(Arrays.asList(orderArray)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll() {
        List<Order> orders = new ArrayList<>();
        Filter filter = new Filter();
        this.handleFilter(filter);
        this.handleOrder(orders);
        return this.findList(filter, orders);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<T> findPage(Pageable pageable) {
        handleFilter(pageable.getFilter());
        handleOrder(pageable.getOrders());
        pageable.getOrders().add(Order.desc("id"));
        return baseDao.findPage(pageable);
    }

    @Override
    public List<T> findCount(int count, Filter filter, Order... orders) {
        return baseDao.findCount(count, filter, orders);
    }

    @Override
    public List<T> findCount(int count, Filter filter, Collection<Order> orders) {
        return baseDao.findCount(count, filter, orders);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer count(Filter filter) {
        this.handleFilter(filter);
        return baseDao.count(filter);
    }

    @Override
    @Transactional
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
    @Transactional
    public void remove(Integer... ids) {
        this.remove(findList(Filter.in("id", Arrays.asList(ids))));
    }

    @Override
    @Transactional
    public void remove(Collection<T> entities) {
        baseDao.remove(entities);
    }

    @Override
    @Transactional
    public void remove(Filter filter) {
        handleFilter(filter);
        baseDao.remove(filter);
    }

    /**
     * 处理筛选
     * @param filter 筛选
     */
    protected void handleFilter(final Filter filter) {
    }

    /**
     * 处理排序
     * @param orders 排序
     */
    protected void handleOrder(List<Order> orders) {
    }
}