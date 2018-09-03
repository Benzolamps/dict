/*
 * Copyright 2005-2016 yunnonggongshe.com All rights reserved.
 * 

 */
package com.benzolamps.dict.service.base;

import com.benzolamps.dict.bean.BaseEntity;
import com.benzolamps.dict.dao.core.Filter;
import com.benzolamps.dict.dao.core.Order;
import com.benzolamps.dict.dao.core.Page;
import com.benzolamps.dict.dao.core.Pageable;

import java.util.List;

/**
 * Service基类接口
 * @param <T> 实体类型
 * @version 2.1.1
 * @datetime 2018-7-1 21:51:17
 */
@SuppressWarnings("unused")
public interface BaseService<T extends BaseEntity> {

    /**
     * 根据id获取实体对象
     * @param id id
     * @return 实体对象，若不存在则返回null
     */
    T find(Integer id);

    /**
     * 根据条件查找单个实体对象
     * @param filter 条件
     * @return 实体对象
     */
    T findSingle(Filter filter);

    /**
     * 根据条件和排序获取实体对象集合
     * @param filter 条件
     * @param orders 排序
     * @return 实体对象集合
     */
    List<T> findList(Filter filter, Order... orders);

    /**
     * 根据条件和排序获取实体对象集合
     * @param filter 条件
     * @param orders 排序
     * @return 实体对象集合
     */
    List<T> findList(Filter filter, List<Order> orders);

    /**
     * 查找所有对象集合
     * @return 实体对象集合
     */
    List<T> findAll();

    /**
     * 查找所有对象分页集合
     * @param pageable 分页
     * @return 实体对象分页集合
     */
    Page<T> findPage(Pageable pageable);

    /**
     * 根据条件获取查询结果条数
     * @param filter 条件
     * @return 查询结果条数
     */
    Long count(Filter filter);

    /**
     * 保存实体对象
     * @param entity 实体对象
     * @return 保存后的实体对象
     */
    T persist(T entity);

    /**
     * 更新实体对象
     * @param entity 实体对象
     * @return 更新后的实体对象
     */
    T update(T entity, String... ignoreProperties);

    /**
     * 删除实体对象
     * @param entity 实体对象
     */
    void remove(T entity);

    /**
     * 删除实体对象
     * @param id 实体对象id
     */
    void remove(Integer id);
}