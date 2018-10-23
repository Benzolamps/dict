package com.benzolamps.dict.bean;

import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 实体类的基类
 * @author Benzolamps
 * @version 2.2.3
 * @datetime 2018-10-23 09:32:04
 */
@EqualsAndHashCode
public abstract class BaseBean implements Serializable {

    private static final long serialVersionUID = -6202558809616015461L;

    @SuppressWarnings("unused")
    @EqualsAndHashCode.Include
    private Class<? extends BaseBean> getEntityClass() {
        return this.getClass();
    }

    @EqualsAndHashCode.Include
    public abstract Integer getId();

    @Override
    public String toString() {
        return String.format("Entity of type %s with id: %s", this.getEntityClass().getSimpleName(), this.getId());
    }
}
