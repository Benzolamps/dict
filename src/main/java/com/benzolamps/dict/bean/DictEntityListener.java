package com.benzolamps.dict.bean;

import com.benzolamps.dict.util.DictBean;
import lombok.val;

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
    private void format(BaseEntity bean) {
        val dictBean = new DictBean<BaseEntity>(bean);
        dictBean.forEachAnnotatedProperty(Format.class, (property, format) -> {
            val value = dictBean.invokeMethod(dictBean.getMethod(format.value(), property.getType()), property.getValue());
            property.setValue(value);
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
