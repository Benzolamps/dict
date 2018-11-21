package com.benzolamps.dict.dao.impl;

import com.benzolamps.dict.bean.User;
import com.benzolamps.dict.dao.base.UserDao;
import org.springframework.stereotype.Repository;

/**
 * 用户Dao接口实现类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-8-30 20:49:58
 */
@Repository("userDao")
public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao {
}
