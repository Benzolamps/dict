package com.benzolamps.dict.dao.base;

import com.benzolamps.dict.bean.BaseEntity;
import com.benzolamps.dict.dao.core.*;
import com.benzolamps.dict.util.KeyValuePairs;

import javax.persistence.criteria.CriteriaQuery;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Dao基类接口
 * @param <T> 实体类型
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-1 18:15:07
 */
@SuppressWarnings("unused")
public interface BaseDao<T extends BaseEntity> {

	GeneratedDictQuery<T> generateDictQuery();

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
     * 根据CriteriaQuery查找单个实体对象
     * @param criteriaQuery CriteriaQuery
     * @return 实体对象
     */
	T findSingle(CriteriaQuery<T> criteriaQuery);

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
	List<T> findList(Filter filter, Collection<Order> orders);

	/**
	 * 根据jpql和参数获取实体对象集合
	 * @param jpql jpql
	 * @param parameters 参数
	 * @return 实体对象集合
	 */
	List<T> findList(String jpql, Map<String, Object> parameters);

	/**
	 * 根据jpql和参数获取实体对象集合
	 * @param jpql jpql
	 * @param parameters 参数
	 * @return 实体对象集合
	 */
	@SuppressWarnings("unchecked")
	List<T> findList(String jpql, KeyValuePairs<String, Object>... parameters);

    /**
     * 根据CriteriaQuery查找实体对象集合
     * @param criteriaQuery CriteriaQuery
     * @return 实体对象集合
     */
	List<T> findList(CriteriaQuery<T> criteriaQuery);

	/**
	 * 根据DictQuery和分页获取实体对象分页集合
	 * @param query DictQuery
	 * @param pageable 分页
	 * @return 实体对象分页集合
	 */
	Page<T> findPage(DictQuery<T> query, Pageable pageable);

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
	 * 将实体对象转换为游离状态
	 * @param entity 实体对象
	 * @return 转换后的实体对象
	 */
	T detach(T entity);
}