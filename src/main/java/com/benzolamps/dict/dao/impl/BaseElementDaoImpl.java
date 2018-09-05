package com.benzolamps.dict.dao.impl;

import com.benzolamps.dict.bean.BaseElement;
import com.benzolamps.dict.bean.Library;
import com.benzolamps.dict.dao.base.BaseElementDao;
import com.benzolamps.dict.util.Constant;
import com.benzolamps.dict.util.DictMath;
import org.springframework.core.ResolvableType;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unchecked")
public class BaseElementDaoImpl<T extends BaseElement> extends BaseDaoImpl<T> implements BaseElementDao<T> {

    /** 实体类类型 */
    private Class<T> entityClass;


    /** 构造方法 */
    public BaseElementDaoImpl() {
        ResolvableType resolvableType = ResolvableType.forClass(getClass());
        entityClass = (Class<T>) resolvableType.getSuperType().getGeneric().resolve();
    }

    @Override
    public Set<String> findNotContainsPrototype(Set<String> prototypes, Library library) {
        if (library == null || CollectionUtils.isEmpty(prototypes)) {
            return Constant.EMPTY_SET;
        } else {
            List prototypeList = new LinkedList(prototypes);
            for (int i = 0; i < prototypeList.size(); i += 100) {
                List subList = prototypeList.subList(i, DictMath.limit(i + 100, i, prototypeList.size()));
                CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
                CriteriaQuery<String> criteriaQuery = criteriaBuilder.createQuery(String.class);
                Root<T> root = criteriaQuery.from(entityClass);
                Predicate restrictions = criteriaBuilder.conjunction();
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("library"), library));
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.in(root.get("prototype")).value(subList));
                criteriaQuery.where(restrictions);
                criteriaQuery.select(root.get("prototype"));
                List<String> existsPrototypes = entityManager.createQuery(criteriaQuery).getResultList();
                prototypes.removeAll(existsPrototypes);
            }
            return prototypes;
        }
    }
}
