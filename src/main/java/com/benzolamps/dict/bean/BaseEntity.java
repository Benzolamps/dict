package com.benzolamps.dict.bean;

import com.benzolamps.dict.component.DictIgnore;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.Date;

/**
 * 实体类的基类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-6-30 22:58:35
 */
@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity extends BaseLombok {

    private static final long serialVersionUID = -1589883392937692838L;

    /** 主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 版本 */
    @Version
    @Column(nullable = false)
    @DictIgnore
    @JsonIgnore
    private Integer version;

    /** 创建时间 */
    @Column(nullable = false, updatable = false)
    @DictIgnore
    @JsonIgnore
    @CreationTimestamp
    private Date createDate;

    /** 修改时间 */
    @Column(nullable = false)
    @DictIgnore
    @JsonIgnore
    @UpdateTimestamp
    private Date modifyDate;

    /** 备注 */
    @Column
    @Length(max = 255)
    @DictIgnore
    @JsonIgnore
    private String remark;

    /** @return 判断是否为新建对象 */
    @Transient
    @JsonIgnore
    public boolean isNew() {
        return getId() == null;
    }
}
