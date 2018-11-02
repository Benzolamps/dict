package com.benzolamps.dict.bean;

import java.io.Serializable;
import java.util.Objects;

/**
 * 实体类的基类
 * @author Benzolamps
 * @version 2.2.3
 * @datetime 2018-10-23 09:32:04
 */
public abstract class BaseBean implements Serializable {

    private static final long serialVersionUID = -6202558809616015461L;

    /** @return 获取id */
    public abstract Integer getId();

    @Override
    public String toString() {
        return String.format("Entity of type %s with id: %s", this.getClass().getSimpleName(), this.getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!getClass().isAssignableFrom(obj.getClass())) {
            return false;
        }
        return Objects.equals(getId(), ((BaseEntity) obj).getId());
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = result * 59 + this.getClass().hashCode();
        result = result * 59 + (this.getId() == null ? 43 : this.getId().hashCode());
        return result;
    }
}
