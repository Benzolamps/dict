package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.bean.User;
import com.benzolamps.dict.dao.base.UserDao;
import com.benzolamps.dict.dao.core.Filter;
import com.benzolamps.dict.service.base.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.DigestUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.annotation.Resource;
import javax.persistence.NoResultException;
import java.nio.charset.Charset;

/**
 * 用户Service接口实现类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-8-30 21:40:22
 */
@Service("userService")
@Transactional
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

    private static final String CURRENT_USER_ATTRIBUTE = "currentUser";

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
        return ref.getPassword().equalsIgnoreCase(encryptPassword(user.getPassword()));
    }

    @Override
    @Transactional(readOnly = true)
    public void setCurrent(User user) {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        if (user == null || user.isNew()) {
            requestAttributes.removeAttribute("currentUser", RequestAttributes.SCOPE_SESSION);
        } else {
            requestAttributes.setAttribute(CURRENT_USER_ATTRIBUTE, user.getId(), RequestAttributes.SCOPE_SESSION);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public User getCurrent() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        Integer id = (Integer) requestAttributes.getAttribute(CURRENT_USER_ATTRIBUTE, RequestAttributes.SCOPE_SESSION);
        return id == null ? null : userDao.find(id);
    }

    @Override
    @Transactional
    public void savePassword(User user, String password) {
        user.setPassword(encryptPassword(password));
        setCurrent(null);
    }

    @Override
    @Transactional
    public User persist(User user) {
        Assert.notNull(user, "user不能为null");
        user.setPassword(encryptPassword(user.getPassword()));
        return super.persist(user);
    }

    @Override
    @Transactional
    public User update(User user, String[] ignoreProperties) {
        if (user.getPassword() != null) {
            user.setPassword(encryptPassword(user.getPassword()));
        }
        return super.update(user, ignoreProperties);
    }

    private String encryptPassword(String password) {
        Assert.hasLength(password, "password不能为null或空");
        Charset charset = Charset.forName("UTF-8");
        String one = DigestUtils.md5DigestAsHex(password.getBytes(charset));
        String two = DigestUtils.md5DigestAsHex(one.getBytes(charset));
        String three = DigestUtils.md5DigestAsHex(two.getBytes(charset));
        String four = DigestUtils.md5DigestAsHex(three.getBytes(charset));
        String five = DigestUtils.md5DigestAsHex(four.getBytes(charset));
        return DigestUtils.md5DigestAsHex(five.getBytes(charset));
    }
}
