package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.bean.User;
import com.benzolamps.dict.dao.base.UserDao;
import com.benzolamps.dict.dao.core.Filter;
import com.benzolamps.dict.service.base.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.persistence.NoResultException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * 用户Service接口实现类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-8-30 21:40:22
 */
@Service
@Transactional
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    @Transactional(readOnly = true)
    public boolean usernameExists(String username) {
        Assert.hasLength(username, "username不能为null或空");
        return count(Filter.eq("username", username)) > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        Assert.hasLength(username, "username不能为null或空");
        try {
            return userDao.findSingle(Filter.eq("username", username));
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean verifyUser(User user) {
        Assert.notNull(user, "user不能为null");
        Assert.hasLength(user.getUsername(), "username不能为null或空");
        Assert.hasLength(user.getPassword(), "password不能为null或空");
        User ref = findByUsername(user.getUsername());
        if (ref == null) return false;
        if (!ref.getPassword().equals(encryptPassword(user.getPassword()))) return false;
        return true;
    }

    @Override
    public User persist(User user) {
        Assert.notNull(user, "user不能为null");
        user.setPassword(encryptPassword(user.getPassword()));
        return super.persist(user);
    }

    @Override
    public User update(User user, String[] ignoreProperties) {
        Assert.notNull(user, "user不能为null");
        user.setPassword(encryptPassword(user.getPassword()));
        return super.update(user, ignoreProperties);
    }

    private String encryptPassword(String password) {
        Assert.hasLength(password, "password不能为null或空");
        String one = DigestUtils.md5DigestAsHex(password.getBytes(Charset.forName("UTF-8")));
        String two = DigestUtils.md5DigestAsHex(one.getBytes(Charset.forName("UTF-8")));
        String three = DigestUtils.md5DigestAsHex(two.getBytes(Charset.forName("UTF-8")));
        String four = DigestUtils.md5DigestAsHex(three.getBytes(Charset.forName("UTF-8")));
        String five = DigestUtils.md5DigestAsHex(four.getBytes(Charset.forName("UTF-8")));
        return DigestUtils.md5DigestAsHex(five.getBytes(Charset.forName("UTF-8")));
    }
}
