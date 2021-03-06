package com.benzolamps.dict.bean;

import com.benzolamps.dict.component.DictIgnore;
import com.benzolamps.dict.component.DictPropertyInfo;
import com.benzolamps.dict.component.DictReadonly;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

import static com.benzolamps.dict.util.Constant.CHINESE_TITLE_PATTERN;

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
@NoArgsConstructor
@JsonIgnoreProperties({"createDate", "modifyDate", "version", "remark"})
public class User extends BaseEntity {

    private static final long serialVersionUID = -5309444370236159463L;

    /**
     * 构造器
     * @param username 用户名
     * @param password 密码
     */
    public User(String username, String password) {
        setUsername(username);
        setPassword(password);
    }

    /** 登录、注册验证组 */
    public interface LoginGroup {
    }

    /** 用户名 */
    @NotEmpty(groups = LoginGroup.class)
    @Length(min = 6, max = 15, groups = LoginGroup.class)
    @Pattern(regexp = CHINESE_TITLE_PATTERN, groups = LoginGroup.class)
    @DictReadonly
    @Column(nullable = false, updatable = false, unique = true)
    @DictPropertyInfo(display = "用户名")
    private String username;

    /** 密码 */
    @NotEmpty(groups = LoginGroup.class)
    @Length(min = 6, max = 15, groups = LoginGroup.class)
    @Pattern(regexp = "^[_0-9A-Za-z]+$", groups = LoginGroup.class)
    @Column(nullable = false)
    @DictIgnore
    @JsonIgnore
    private String password;

    /** 昵称 */
    @NotEmpty
    @Length(min = 1, max = 15)
    @Pattern(regexp = CHINESE_TITLE_PATTERN)
    @Column(nullable = false)
    @DictPropertyInfo(display = "昵称", description = "昵称必须是汉字、数字、字母、下划线、短横线的组合")
    private String nickname;

    /** 词库 */
    @ManyToOne
    @JoinColumn(name = "library")
    @DictIgnore
    private Library library;
}
