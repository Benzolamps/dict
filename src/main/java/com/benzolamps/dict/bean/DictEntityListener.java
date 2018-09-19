package com.benzolamps.dict.bean;

import com.benzolamps.dict.component.Format;
import com.benzolamps.dict.component.Size;
import com.benzolamps.dict.util.DictBean;
import com.benzolamps.dict.util.DictLambda.Action2;
import com.benzolamps.dict.util.DictProperty;
import com.benzolamps.dict.util.DictSpring;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Date;

/**
 * 实体类监听器
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-1 00:08:04
 */
public final class DictEntityListener {

    /* 监听实体类数据格式 */
    private void format(BaseEntity bean) {
        DictBean<? extends BaseEntity> dictBean = new DictBean<>(bean.getClass());
        dictBean.forEachAnnotatedProperty(Format.class, (Action2<DictProperty, Format>) (property, format) -> {
            Object value = dictBean.getMethod(format.value(), property.getType()).invoke(bean, (String) property.get(bean));
            property.set(bean, value);
        });
    }

    /* 监听实体类集合字段总数 */
    private void size(BaseEntity bean) {
        DictBean<? extends BaseEntity> dictBean = new DictBean<>(bean.getClass());
        dictBean.forEachAnnotatedProperty(Size.class, (Action2<DictProperty, Size>) (property, size) -> {
            Assert.isTrue(property.getType().equals(int.class) || property.getType().isAssignableFrom(Integer.class), "property只能是int类型");
            EntityManager entityManager = DictSpring.getBean(EntityManager.class);
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Integer> criteriaQuery = criteriaBuilder.createQuery(Integer.class);
            Root<? extends BaseEntity> root = criteriaQuery.from(bean.getClass());
            criteriaQuery.where(criteriaBuilder.equal(root, bean));
            criteriaQuery.select(criteriaBuilder.size(root.get(size.value())));
            property.set(bean, entityManager.createQuery(criteriaQuery).getSingleResult());
        });
    }

    /* 插入前处理 */
    @PrePersist
    private void prePersist(BaseEntity bean) {
        format(bean);
        Date now = new Date();
        bean.setCreateDate(now);
        bean.setModifyDate(now);
        bean.setVersion(null);
    }

    /* 更新前处理 */
    @PreUpdate
    private void preUpdate(BaseEntity bean) {
        format(bean);
        bean.setModifyDate(new Date());
    }

    /* 加载后处理 */
    @PostLoad
    private void postLoad(BaseEntity bean) {
        size(bean);
    }
}
