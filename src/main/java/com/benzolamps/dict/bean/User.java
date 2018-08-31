package com.benzolamps.dict.bean;

import com.benzolamps.dict.util.Constant;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

/**
 * 用户实体类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-8-30 20:38:45
 */
@Entity
@Table(name = "dict_user")
@Getter
@Setter
public class User extends BaseEntity {

    private static final long serialVersionUID = -5309444370236159463L;

    /** 用户名 */
    @NotEmpty
    @Length(min = 6, max = 15)
    @Pattern(regexp = Constant.CHINESE_TITLE_PATTERN)
    @Column(nullable = false, updatable = false, unique = true)
    private String username;

    /** 登录密码 */
    @NotEmpty
    @Length(min = 6, max = 15)
    @Pattern(regexp = "^[_0-9A-Za-z]+$")
    @Column(nullable = false)
    private String password;

    /** 解锁密码 */
    @Column
    private String unlockPassword;

    /** 锁定时长 */
    @Column(nullable = false, columnDefinition = "INTEGER NOT NULL DEFAULT(60 * 60 * 2)")
    private Long lockTime;

    /** 昵称 */
    @Column
    private String nickName;
}
