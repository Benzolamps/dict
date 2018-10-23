package com.benzolamps.dict.bean;

import com.benzolamps.dict.component.DictIgnore;
import com.benzolamps.dict.component.DictPropertyInfo;
import com.benzolamps.dict.component.DictTextArea;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Id;
import javax.validation.constraints.Pattern;
import java.util.Map;

/**
 * 乱序方案实体类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-20 23:26:47
 */
@Getter
@Setter
public class ShuffleSolution extends BaseBean {

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
    @DictIgnore
    private Map<String, Object> properties;
}
