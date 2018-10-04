package com.benzolamps.dict.bean;

import com.benzolamps.dict.component.DictPropertyInfo;
import com.benzolamps.dict.component.DictTextArea;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Id;
import javax.validation.constraints.Pattern;
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
    @Id
    private Integer id;

    /** 名称 */
    @NotEmpty
    @Length(min = 2, max = 20)
    @Pattern(regexp = "^[0-9a-zA-Z\\u4e00-\\u9fa5]+$")
    @DictPropertyInfo(display = "名称")
    private String name;

    /** 备注 */
    @Length(max = 50)
    @DictTextArea
    @DictPropertyInfo(display = "备注")
    private String remark;

    /** 乱序策略类 */
    @NotEmpty
    @DictPropertyInfo(display = "乱序策略")
    private String strategyClass;

    /** 属性 */
    private transient Map<String, Object> properties;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!ShuffleSolution.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        return Objects.equals(getId(), ((ShuffleSolution) obj).getId());
    }

    @Override
    public int hashCode() {
        int hashCode = 17;
        hashCode += getId() != null ? getId().hashCode() * 31 : 0;
        hashCode += getClass().hashCode() * 31;
        return hashCode;
    }
}
