package com.benzolamps.dict.bean;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * 乱序方案实体类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-20 23:26:47
 */
@Getter
@Setter
@ToString
public class ShuffleSolution implements Serializable {

    private static final long serialVersionUID = 4614817880825423160L;

    /** id */
    private Long id;

    /** 名称 */
    @NotBlank
    @Length(max = 20)
    private String name;

    /** 乱序策略类 */
    @NotBlank
    private String strategyClass;

    /** 属性 */
    private Map<String, Object> properties;

    /** 备注 */
    @Length(max = 50)
    private String remark;

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
        return Objects.equals(getId(), ((ShuffleSolution) obj).getId());
    }

    @Override
    public int hashCode() {
        int hashCode = 17;
        hashCode += getId() != null ? getId().hashCode() * 31 : 0;
        return hashCode;
    }
}
