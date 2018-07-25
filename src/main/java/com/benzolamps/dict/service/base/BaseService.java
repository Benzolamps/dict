/*
 * Copyright 2005-2016 yunnonggongshe.com All rights reserved.
 * 

 */
package com.benzolamps.dict.service.base;

import com.benzolamps.dict.bean.BaseEntity;

import java.util.List;

/**
 * Service基类接口
 * @param <T> 实体类型
 * @version 2.1.1
 * @datetime 2018-7-1 21:51:17
 */
public interface BaseService<T extends BaseEntity> {

//	/**
//	 * 查找实体对象
//	 * @param id Integer
//	 * @return 实体对象，若不存在则返回null
//	 */
//	T find(Integer id);
//
//	/**
//	 * 查找所有实体对象集合
//	 * @return 所有实体对象集合
//	 */
//	List<T> findAll();
//
//	/**
//	 * 查找实体对象集合
//	 *
//	 * @param ids Integer
//	 * @return 实体对象集合
//	 */
//	List<T> findList(Integer... ids);
//
//	/**
//	 * 查找实体对象集合
//	 * @param count 数量
//	 * @param filters 筛选
//	 * @param orders 排序
//	 * @return 实体对象集合
//	 */
//	List<T> findList(Integer count, List<Filter> filters, List<Order> orders);
//
//	/**
//	 * 查找实体对象集合
//	 * @param first 起始记录
//	 * @param count 数量
//	 * @param filters 筛选
//	 * @param orders 排序
//	 * @return 实体对象集合
//	 */
//	List<T> findList(Integer first, Integer count, List<Filter> filters, List<Order> orders);
//
//	/**
//	 *
//	 * 查找实体对象分页
//	 *
//	 * @param pageable 分页信息
//	 * @return 实体对象分页
//	 */
//	Page<T> findPage(Pageable pageable);
//
//	/**
//	 * 查询实体对象数量
//	 * @param filters 筛选
//	 * @return 实体对象数量
//	 */
//	long count(Filter... filters);
//
//	/**
//	 * 判断实体对象是否存在
//	 * @param id Integer
//	 * @return 实体对象是否存在
//	 */
//	boolean exists(Integer id);
//
//	/**
//	 * 判断实体对象是否存在
//	 * @param filters 筛选
//	 * @return 实体对象是否存在
//	 */
//	boolean exists(Filter... filters);
//
//	/**
//	 * 保存实体对象
//	 * @param entity 实体对象
//	 * @return 实体对象
//	 */
//	T save(T entity);
//
//	/**
//	 * 更新实体对象
//	 * @param entity 实体对象
//	 * @return 实体对象
//	 */
//	T update(T entity);
//
//	/**
//	 * 更新实体对象
//	 * @param entity 实体对象
//	 * @param ignoreProperties 忽略属性
//	 * @return 实体对象
//	 */
//	T update(T entity, String... ignoreProperties);
//
//	/**
//	 * 删除实体对象
//	 * @param id Integer
//	 */
//	void delete(Integer id);
//
//	/**
//	 * 删除实体对象
//	 * @param ids Integer
//	 */
//	void delete(Integer... ids);
//
//	/**
//	 * 删除实体对象
//	 * @param entity 实体对象
//	 */
//	void delete(T entity);

}