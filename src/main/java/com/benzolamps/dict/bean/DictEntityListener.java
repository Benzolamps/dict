package com.benzolamps.dict.bean;

import com.benzolamps.dict.util.DictBean;
import com.benzolamps.dict.util.DictLambda.Action2;
import com.benzolamps.dict.util.DictProperty;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

/**
 * 实体类监听器
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-1 00:08:04
 */
public final class DictEntityListener {

    /* 监听实体类数据格式 */
    @SuppressWarnings("unchecked")
    private void format(BaseEntity bean) {
        DictBean<? extends BaseEntity> dictBean = new DictBean<>(bean.getClass());
        dictBean.forEachAnnotatedProperty(Format.class, (Action2<DictProperty, Format>) (property, format) -> {
            Object value = dictBean.getMethod(format.value(), property.get(dictBean)).invoke(dictBean);
            property.set(dictBean, value);
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
}
