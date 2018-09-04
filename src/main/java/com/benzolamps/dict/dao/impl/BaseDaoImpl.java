package com.benzolamps.dict.dao.impl;

import com.benzolamps.dict.bean.BaseEntity;
import com.benzolamps.dict.dao.base.BaseDao;
import com.benzolamps.dict.dao.core.*;
import com.benzolamps.dict.util.Constant;
import com.benzolamps.dict.util.DictBean;
import com.benzolamps.dict.util.DictProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.core.ResolvableType;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaQuery;
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

	/** 构造方法 */
	public BaseDaoImpl() {
		ResolvableType resolvableType = ResolvableType.forClass(getClass());
		entityClass = (Class<T>) resolvableType.getSuperType().getGeneric().resolve();
	}

    /**
     * 生成一个GeneratedDictQuery
     * @return GeneratedDictQuery
     */
    protected GeneratedDictQuery<T> generateDictQuery() {
        return new GeneratedDictQuery<>(entityClass);
    }

	@Override
	public T find(Integer id) {
        if (id == null) return null;
		return entityManager.find(entityClass, id);
	}

	@Override
	public T findSingle(Filter filter) {
		GeneratedDictQuery<T> query = generateDictQuery();
		query.setFilter(filter);
        try {
            return query.getTypedQuery(entityManager).getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
            return null;
        }
	}

    @Override
    public T findSingle(CriteriaQuery<T> criteriaQuery) {
        Assert.notNull(criteriaQuery, "criteria query不能为null");
	    try {
            return entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
	        return null;
        }
    }

    @Override
    public T findSingle(String jpql, Map<String, Object> parameters, Object... positionParameters) {
        Assert.hasLength(jpql, "jpql不能为null或空");
        TypedQuery<T> query = DictJpa.createJpqlQuery(entityManager, jpql, entityClass, parameters, positionParameters);
        try {
            return query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
            return null;
        }
    }

    @Override
    public T findSingleNative(String sql, Map<String, Object> parameters, Object... positionParameters) {
        Assert.hasLength(sql, "jpql不能为null或空");
        List<T> results = DictJpa.createNativeQuery(entityManager, sql, entityClass, parameters, positionParameters).list();
        return results.size() != 1 ? null : results.iterator().next();
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
    public List<T> findList(CriteriaQuery<T> criteriaQuery) {
	    Assert.notNull(criteriaQuery, "criteria query不能为null");
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<T> findList(String jpql, Map<String, Object> parameters, Object... positionParameters) {
        Assert.hasLength(jpql, "jpql不能为null或空");
        return DictJpa.createJpqlQuery(entityManager, jpql, entityClass, parameters, positionParameters).getResultList();
    }

    @Override
    public List<T> findListNative(String sql, Map<String, Object> parameters, Object... positionParameters) {
        Assert.hasLength(sql, "sql不能为null或空");
        return DictJpa.createNativeQuery(entityManager, sql, entityClass, parameters, positionParameters).list();
    }

    @Override
	public Page<T> findPage(DictQuery<T> dictQuery, Pageable pageable) {
        Assert.notNull(dictQuery, " dict query不能为null");
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
    public Page<T> findPage(Pageable pageable) {
        return findPage(generateDictQuery(), pageable);
    }

    @Override
	public T persist(T entity) {
		Assert.notNull(entity, "entity不能为null");
		Assert.isTrue(entity.isNew(), "entity必须为新建对象");
		entityManager.persist(entity);
        return entity;
	}

    @Override
    public T update(T entity, String[] ignoreProperties) {
        Assert.notNull(entity, "entity不能为null");
        Assert.isTrue(!entity.isNew(), "entity不能为新建对象");
        if (ignoreProperties == null) ignoreProperties = Constant.EMPTY_STRING_ARRAY;
        String[] defaultIgnore = {"id", "version", "createDate", "updateDate"};
        T ref = find(entity.getId());
        DictBean<T> dictBean = new DictBean<>(entityClass);
        for (DictProperty property : dictBean.getProperties()) {
            if (ArrayUtils.contains(ignoreProperties, property.getName()) || ArrayUtils.contains(defaultIgnore, property.getName())) {
                continue;
            }
            property.set(ref, property.get(entity));
        }
        return entity;
    }

    @Override
	public void remove(T entity) {
        Assert.notNull(entity, "entity不能为null");
        Assert.isTrue(!entity.isNew(), "entity不能为新建对象");
        if (entityManager.contains(entity)) {
            entityManager.remove(entityManager.merge(entity));
        }
	}

	@Override
	public T detach(T entity) {
        Assert.notNull(entity, "entity不能为null");
        Assert.isTrue(!entity.isNew(), "entity不能为新建对象");
        entityManager.detach(entity);
		return entity;
	}

    @Override
    public boolean contains(T entity) {
        Assert.notNull(entity, "entity不能为null");
        Assert.isTrue(!entity.isNew(), "entity不能为新建对象");
        return find(entity.getId()) != null;
    }

    @Override
    public void execute(String jpql, Map<String, Object> parameters, Object... positionParameters) {
        Assert.hasLength(jpql, "jpql不能为null或空");
        DictJpa.createJpqlQuery(entityManager, jpql, parameters, positionParameters);
    }

    @Override
    public void executeNative(String sql, Map<String, Object> parameters, Object... positionParameters) {
        Assert.hasLength(sql, "sql不能为null或空");
        DictJpa.executeNativeQuery(entityManager, sql, parameters, positionParameters);
    }
}

