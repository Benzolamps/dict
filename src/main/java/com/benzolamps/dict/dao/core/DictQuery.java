package com.benzolamps.dict.dao.core;

import com.benzolamps.dict.bean.BaseEntity;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Collection;

/**
 * 自定义查询, 可以指定搜索和排序
 * @param <B> 实体类型
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-25 19:18:04
 */
public interface DictQuery<B extends BaseEntity> {

    /**
     * 应用排序
     * @param order 排序
     */
    void applyOrder(Order order);

    /**
     * 应用搜索
     * @param search 搜索
     */
    void applySearch(Search search);

    /**
     * 应用排序
     * @param orders 排序
     */
    default void applyOrders(Collection<Order> orders) {
        if (!CollectionUtils.isEmpty(orders)) {
            orders.forEach(this::applyOrder);
        }
    }

    /**
     * 应用搜索
     * @param searches 搜索
     */
    default void applySearches(Collection<Search> searches) {
        if (!CollectionUtils.isEmpty(searches)) {
            searches.forEach(this::applySearch);
        }
    }

    /**
     * 获取结果集查询
     * @return 结果集查询
     */
    TypedQuery<B> getTypedQuery();

    /**
     * 获取总条数查询
     * @return 总条数查询
     */
    TypedQuery<? extends Number> getCountQuery();

    /**
     * 设置EntityManager
     * @param entityManager EntityManager
     */
    void setEntityManager(EntityManager entityManager);

    /**
     * 获取筛选
     * @return 筛选
     */
    Filter getFilter();
}
