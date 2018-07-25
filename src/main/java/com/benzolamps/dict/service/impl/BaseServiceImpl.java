package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.bean.BaseEntity;
import com.benzolamps.dict.dao.base.BaseDao;
import com.benzolamps.dict.exception.DictException;
import com.benzolamps.dict.service.base.BaseService;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
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

//    /** 更新忽略属性 */
//    private static final String[] UPDATE_IGNORE_PROPERTIES = new String[] {"createDate", "modifyDate", "version"};
//
//    /* BaseDao */
//	private BaseDao<T> baseDao;
//
//	@Autowired
//	protected void setBaseDao(BaseDao<T> baseDao) {
//		this.baseDao = baseDao;
//	}
//
//	@Transactional(readOnly = true)
//	public T find(Integer id) {
//		return baseDao.find(id);
//	}
//
//	@Transactional(readOnly = true)
//	public List<T> findAll() {
//		return findList(null, null, null, null);
//	}
//
//	@Transactional(readOnly = true)
//	public List<T> findList(Integer... ids) {
//		List<T> result = new ArrayList<>();
//		if (ids != null) {
//			for (Integer id : ids) {
//				T entity = find(id);
//				if (entity != null) {
//					result.add(entity);
//				}
//			}
//		}
//		return result;
//	}
//
//	@Transactional(readOnly = true)
//	public List<T> findList(Integer count, List<Filter> filters, List<Order> orders) {
//		return findList(null, count, filters, orders);
//	}
//
//	@Transactional(readOnly = true)
//	public List<T> findList(Integer first, Integer count, List<Filter> filters, List<Order> orders) {
//		return baseDao.findList(first, count, filters, orders);
//	}
//
//	@Transactional(readOnly = true)
//	public Page<T> findPage(Pageable pageable) {
//		return baseDao.findPage(pageable);
//	}
//
//	@Transactional(readOnly = true)
//	public long count(Filter... filters) {
//		return baseDao.count(filters);
//	}
//
//	@Transactional(readOnly = true)
//	public boolean exists(Integer id) {
//		return baseDao.find(id) != null;
//	}
//
//	@Transactional(readOnly = true)
//	public boolean exists(Filter... filters) {
//		return baseDao.count(filters) > 0;
//	}
//
//	@Transactional
//	public T save(T entity) {
//		Assert.notNull(entity);
//		Assert.isTrue(entity.isNew());
//		baseDao.persist(entity);
//		return entity;
//	}
//
//	@Transactional
//	public T update(T entity) {
//		Assert.notNull(entity);
//		Assert.isTrue(!entity.isNew());
//		if (!baseDao.isManaged(entity)) {
//            T persistant = baseDao.find(baseDao.getIdentifier(entity));
//            if (persistant != null) {
//                copyProperties(entity, persistant, UPDATE_IGNORE_PROPERTIES);
//            }
//            return baseDao.find(baseDao.getIdentifier(entity));
//		}
//
//		return entity;
//	}
//
//	@Transactional
//	public T update(T entity, String... ignoreProperties) {
//		Assert.notNull(entity);
//		Assert.isTrue(!entity.isNew());
//		Assert.isTrue(!baseDao.isManaged(entity));
//
//        T persistant = baseDao.find(baseDao.getIdentifier(entity));
//        if (persistant != null) {
//            copyProperties(entity, persistant, (String[]) ArrayUtils.addAll(ignoreProperties, UPDATE_IGNORE_PROPERTIES));
//        }
//		return update(persistant);
//	}
//
//	@Transactional
//	public void delete(Integer id) {
//		delete(baseDao.find(id));
//	}
//
//	@Transactional
//	public void delete(Integer... ids) {
//		if (ids != null) {
//			for (Integer id : ids) {
//				delete(baseDao.find(id));
//			}
//		}
//	}
//
//	@Transactional
//	public void delete(T entity) {
//		if (entity != null) {
//			baseDao.remove(baseDao.isManaged(entity) ? entity : baseDao.merge(entity));
//		}
//	}
//
//    /**
//     * 拷贝对象属性
//     * @param source 源
//     * @param target 目标
//     * @param ignoreProperties 忽略属性
//     */
//    protected void copyProperties(T source, T target, String... ignoreProperties) {
//        Assert.notNull(source);
//        Assert.notNull(target);
//
//        PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(target);
//        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
//            String propertyName = propertyDescriptor.getName();
//            Method readMethod = propertyDescriptor.getReadMethod();
//            Method writeMethod = propertyDescriptor.getWriteMethod();
//            if (ArrayUtils.contains(ignoreProperties, propertyName)
//                || readMethod == null || writeMethod == null || !baseDao.isLoaded(source, propertyName)) {
//                continue;
//            }
//            try {
//                Object sourceValue = readMethod.invoke(source);
//                writeMethod.invoke(target, sourceValue);
//            } catch (ReflectiveOperationException e) {
//                throw new DictException(e);
//            }
//        }
//    }
}