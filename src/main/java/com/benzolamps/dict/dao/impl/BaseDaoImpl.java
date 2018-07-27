package com.benzolamps.dict.dao.impl;

import com.benzolamps.dict.bean.BaseEntity;
import com.benzolamps.dict.dao.base.BaseDao;
import com.benzolamps.dict.dao.core.*;
import com.benzolamps.dict.util.KeyValuePairs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ResolvableType;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaQuery;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Dao基类接口实现类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-1 18:30:10
 */
@Slf4j
@SuppressWarnings("unchecked")
public abstract class BaseDaoImpl<T extends BaseEntity> implements BaseDao<T> {

	/** 实体类类型 */
	private Class<T> entityClass;

	@PersistenceContext
	protected EntityManager entityManager;

    /**
     * 生成一个GeneratedDictQuery
     * @return GeneratedDictQuery
     */
	protected GeneratedDictQuery<T> generateDictQuery() {
	    return new GeneratedDictQuery<T>() { };
    }

	/** 构造方法 */
	public BaseDaoImpl() {
		ResolvableType resolvableType = ResolvableType.forClass(getClass());
		entityClass = (Class<T>) resolvableType.getSuperType().getGeneric().resolve();
	}

	@Override
	public T find(Integer id) {
        Assert.notNull(id, "id不能为空");
		return entityManager.find(entityClass, id);
	}

	@Override
	public T findSingle(Filter filter) {
		GeneratedDictQuery<T> query = generateDictQuery();
		query.setFilter(filter);
		return query.getTypedQuery(entityManager).getSingleResult();
	}

    @Override
    public T findSingle(CriteriaQuery<T> criteriaQuery) {
        Assert.notNull(criteriaQuery, "criteria query不能为空");
	    try {
            return entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
	        return null;
        }
    }

    @Override
    public Long count(Filter filter) {
        GeneratedDictQuery<T> query = generateDictQuery();
        query.setFilter(filter);
        return query.getCountQuery(entityManager).getSingleResult();
    }

    @Override
	public List<T> findList(Filter filter, Order... orders) {
        GeneratedDictQuery<T> query = generateDictQuery();
        query.setFilter(filter);
        query.applyOrders(orders);
        return query.getTypedQuery(entityManager).getResultList();
	}

    @Override
    public List<T> findList(Filter filter, Collection<Order> orders) {
	    Order[] orderArray = orders == null ? new Order[0] : orders.toArray(new Order[0]);
        return findList(filter, orderArray);
    }

    @Override
    public List<T> findList(String jpql, Map<String, Object> parameters) {
	    Assert.hasLength(jpql, "jpql不能为空或null");
	    logger.info("jpql: " + jpql);
        TypedQuery<T> query = entityManager.createQuery(jpql, entityClass);
        if (parameters != null) parameters.forEach(query::setParameter);
        return query.getResultList();
    }

    @Override
    public List<T> findList(String jpql, KeyValuePairs<String, Object>... parameters) {
        Assert.hasLength(jpql, "jpql不能为空或null");
        logger.info("jpql: " + jpql);
        TypedQuery<T> query = entityManager.createQuery(jpql, entityClass);
        Arrays.stream(parameters).forEach(pairs -> query.setParameter(pairs.getKey(), pairs.getValue()));
        return query.getResultList();
    }

    @Override
    public List<T> findList(CriteriaQuery<T> criteriaQuery) {
	    Assert.notNull(criteriaQuery, "criteria query不能为空");
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
	public Page<T> findPage(DictQuery<T> dictQuery, Pageable pageable) {
        Assert.notNull(dictQuery, " dict query不能为空");
        dictQuery.applyOrders(pageable.getOrders().toArray(new Order[0]));
        long total = dictQuery.getCountQuery(entityManager).getSingleResult();
        if (pageable != null) pageable.getSearches().forEach(dictQuery::applySearch);
        TypedQuery<T> typedQuery = dictQuery.getTypedQuery(entityManager);
        if (pageable != null) {
            if (pageable.getPageNumber() != -1) {
                int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
                if (totalPages < pageable.getPageNumber()) {
                    pageable.setPageNumber(totalPages);
                }
                typedQuery.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
                typedQuery.setMaxResults(pageable.getPageSize());

            }
        }
        List<T> list = typedQuery.getResultList();
        return new Page<>(list, total, pageable);
	}

    @Override
	public T persist(T entity) {
		Assert.notNull(entity, "entity不能为空");
		Assert.isTrue(entity.isNew(), "entity必须为新建对象");
		entityManager.persist(entity);
        return entity;
	}

    @Override
    public T update(T entity) {
        Assert.notNull(entity, "entity不能为空");
        Assert.isTrue(!entity.isNew(), "entity不能为新建对象");
        entityManager.merge(entity);
        return entity;
    }

    @Override
	public void remove(T entity) {
        Assert.notNull(entity, "entity不能为空");
        Assert.isTrue(!entity.isNew(), "entity不能为新建对象");
	    entityManager.remove(entity);
	}

	@Override
	public T detach(T entity) {
        Assert.notNull(entity, "entity不能为空");
        Assert.isTrue(!entity.isNew(), "entity不能为新建对象");
        entityManager.detach(entity);
		return entity;
	}
}

