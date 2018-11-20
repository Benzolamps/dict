package com.benzolamps.dict.dao.impl;

import com.benzolamps.dict.bean.BaseEntity;
import com.benzolamps.dict.dao.base.BaseDao;
import com.benzolamps.dict.dao.core.*;
import com.benzolamps.dict.util.Constant;
import com.benzolamps.dict.util.DictArray;
import com.benzolamps.dict.util.DictBean;
import com.benzolamps.dict.util.DictProperty;
import org.springframework.core.ResolvableType;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaQuery;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static com.benzolamps.dict.dao.core.DictJpa.*;

/**
 * Dao基类接口实现类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-1 18:30:10
 */
@SuppressWarnings("unchecked")
public abstract class BaseDaoImpl<T extends BaseEntity> implements BaseDao<T> {

	/** 实体类类型 */
	private Class<T> entityClass;

	@PersistenceContext
	protected EntityManager entityManager;

    protected BaseDaoImpl() {
        ResolvableType resolvableType = ResolvableType.forClass(getClass());
        entityClass = (Class<T>) resolvableType.getSuperType().getGeneric().resolve();
    }

    /**
     * 生成一个GeneratedDictQuery
     * @return GeneratedDictQuery
     */
    protected GeneratedDictQuery<T> generateDictQuery() {
        GeneratedDictQuery<T> query = new GeneratedDictQuery<>(entityClass);
        query.setEntityManager(entityManager);
        return query;
    }

	@Override
	public T find(Integer id) {
        if (id == null) return null;
		return entityManager.find(entityClass, id);
	}

	@Override
	public T findSingle(Filter filter) {
		GeneratedDictQuery<T> query = generateDictQuery();
        query.getFilter().and(filter);
        try {
            return query.getTypedQuery().getSingleResult();
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
        Assert.hasText(jpql, "jpql不能为null或空");
        TypedQuery<T> query = createJpqlQuery(entityManager, jpql, entityClass, parameters, positionParameters);
        try {
            return query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
            return null;
        }
    }

    @Override
    public T findSingleNative(String sql, Map<String, Object> parameters, Object... positionParameters) {
        Assert.hasText(sql, "jpql不能为null或空");
        List<T> results = createNativeQuery(entityManager, sql, entityClass, parameters, positionParameters).list();
        return results.size() != 1 ? null : results.iterator().next();
    }

    @Override
    public Integer count(Filter filter) {
        GeneratedDictQuery<T> query = generateDictQuery();
        query.getFilter().and(filter);
        return query.getCountQuery().getSingleResult().intValue();
    }

    @Override
	public List<T> findList(Filter filter, Order... orders) {
        GeneratedDictQuery<T> query = generateDictQuery();
        query.getFilter().and(filter);
        Stream.of(orders).forEach(query::applyOrder);
        return query.getTypedQuery().getResultList();
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
        Assert.hasText(jpql, "jpql不能为null或空");
        return createJpqlQuery(entityManager, jpql, entityClass, parameters, positionParameters).getResultList();
    }

    @Override
    public List<T> findListNative(String sql, Map<String, Object> parameters, Object... positionParameters) {
        Assert.hasText(sql, "sql不能为null或空");
        return createNativeQuery(entityManager, sql, entityClass, parameters, positionParameters).list();
    }

    @Override
	public Page<T> findPage(DictQuery<T> dictQuery, Pageable pageable) {
        Assert.notNull(dictQuery, " dict query不能为null");
        dictQuery.setEntityManager(entityManager);

        if (pageable != null) {
            dictQuery.applySearches(pageable.getSearches());
            dictQuery.applyOrders(pageable.getOrders());
            dictQuery.getFilter().and(pageable.getFilter());
        }

        int total = dictQuery.getCountQuery().getSingleResult().intValue();
        TypedQuery<T> typedQuery = dictQuery.getTypedQuery();

        if (pageable != null) {
            if (!pageable.getPageDisabled()) {

                int totalPages = (total - 1) / pageable.getPageSize() + 1;
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
    public List<T> findCount(int count, Filter filter, Order... orders) {
        GeneratedDictQuery<T> query = generateDictQuery();
        query.getFilter().and(filter);
        Stream.of(orders).forEach(query::applyOrder);
        return query.getTypedQuery().setMaxResults(count).getResultList();
    }

    @Override
    public List<T> findCount(int count, Filter filter, Collection<Order> orders) {
        Order[] orderArray = orders == null ? new Order[0] : orders.toArray(new Order[0]);
        return findCount(count, filter, orderArray);
    }

    @Override
    public List<T> findCount(int count, CriteriaQuery<T> criteriaQuery) {
        Assert.notNull(criteriaQuery, "criteria query不能为null");
        return entityManager.createQuery(criteriaQuery).setMaxResults(count).getResultList();
    }

    @Override
    public List<T> findCount(int count, String jpql, Map<String, Object> parameters, Object... positionParameters) {
        Assert.hasText(jpql, "jpql不能为null或空");
        return createJpqlQuery(entityManager, jpql, entityClass, parameters, positionParameters)
            .setMaxResults(count)
            .getResultList();
    }

    @Override
	public T persist(T entity) {
		Assert.notNull(entity, "entity不能为null");
		Assert.isTrue(entity.isNew(), "entity必须为新建对象");
		entityManager.persist(entity);
        return entity;
	}

    @Override
    public T update(T entity, String... ignoreProperties) {
        Assert.notNull(entity, "entity不能为null");
        Assert.isTrue(!entity.isNew(), "entity不能为新建对象");
        ignoreProperties = Optional.ofNullable(ignoreProperties).orElse(Constant.EMPTY_STRING_ARRAY);
        String[] defaultIgnore = {"id", "version", "createDate", "updateDate"};
        T ref = find(entity.getId());
        DictBean<T> dictBean = new DictBean<>(entityClass);
        for (DictProperty property : dictBean.getProperties()) {
            if (DictArray.contains(ignoreProperties, property.getName()) || DictArray.contains(defaultIgnore, property.getName())) {
                continue;
            }
            property.set(ref, property.get(entity));
        }
        return entity;
    }

    @Override
	public void remove(Collection<T> entities) {
        Assert.notNull(entities, "entities不能为null");
        entities.forEach(entity -> entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity)));
	}

    @Override
    public void remove(Filter filter) {
        Collection<T> entities = findList(filter);
        this.remove(entities);
    }

	@Override
	public T detach(T entity) {
        Assert.notNull(entity, "entity不能为null");
        Assert.isTrue(!entity.isNew(), "entity不能为新建对象");
        entityManager.detach(entity);
		return entity;
	}

    @Override
    public void flush() {
        entityManager.flush();
    }

    @Override
    public boolean contains(T entity) {
        Assert.notNull(entity, "entity不能为null");
        Assert.isTrue(!entity.isNew(), "entity不能为新建对象");
        return find(entity.getId()) != null;
    }

    @Override
    public void execute(String jpql, Map<String, Object> parameters, Object... positionParameters) {
        Assert.hasText(jpql, "jpql不能为null或空");
        executeJpqlQuery(entityManager, jpql, parameters, positionParameters);
    }

    @Override
    public void executeNative(String sql, Map<String, Object> parameters, Object... positionParameters) {
        Assert.hasText(sql, "sql不能为null或空");
        executeNativeQuery(entityManager, sql, parameters, positionParameters);
    }
}

