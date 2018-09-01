package com.benzolamps.dict.bean;

import com.benzolamps.dict.util.Constant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;

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
public class User extends BaseEntity {

    private static final long serialVersionUID = -5309444370236159463L;

    public User(String username, String password) {
        setUsername(username);
        setPassword(password);
    }

    public interface Password {
    }

    /** 用户名 */
    @NotEmpty
    @Length(min = 6, max = 15, groups = {Password.class, Default.class})
    @Pattern(regexp = Constant.CHINESE_TITLE_PATTERN, groups = {Password.class, Default.class})
    @Column(nullable = false, updatable = false, unique = true)
    private String username;

    /** 登录密码 */
    @NotEmpty
    @Length(min = 6, max = 15, groups = Password.class)
    @Pattern(regexp = "^[_0-9A-Za-z]+$", groups = Password.class)
    @Column(nullable = false)
    private String password;

    /** 昵称 */
    @Column
    private String nickname;
}
