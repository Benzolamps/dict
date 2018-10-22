package com.benzolamps.dict.bean;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 实体类的基类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-6-30 22:58:35
 */
@Getter
@Setter
@EqualsAndHashCode(of = {"id", "entityClass"})
public abstract class BaseLombok implements Serializable {

    @Getter(AccessLevel.PRIVATE)
    private final Class<? extends BaseLombok> entityClass = this.getClass();

    private Integer id;

    @Override
    public String toString() {
        return String.format("Entity of type %s with id: %s", entityClass.getSimpleName(), this.getId());
    }
}
