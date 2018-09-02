package com.benzolamps.dict.service.base;

import com.benzolamps.dict.bean.User;

/**
 * 用户Service接口
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-8-30 21:35:35
 */
public interface UserService extends BaseService<User> {

    /**
     * 判断用户名是否存在
     * @param username 用户名
     * @return 判断结果
     */
    boolean usernameExists(String username);

    /**
     * 通过用户名获取用户
     * @param username 用户名
     * @return 用户
     */
    User findByUsername(String username);

    /**
     * 验证用户名跟密码
     * @param user 用户
     * @return 验证结果
     */
    boolean verifyUser(User user);

    /**
     * 设置当前登录用户
     * @param user 用户
     */
    void setCurrent(User user);

    /**
     * 获取当前登录用户
     * @return 用户
     */
    User getCurrent();

    /**
     * 修改密码
     * @param password 密码
     */
    void savePassword(User user, String password);

}
