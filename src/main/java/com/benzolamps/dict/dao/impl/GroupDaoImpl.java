package com.benzolamps.dict.dao.impl;

import com.benzolamps.dict.bean.Group;
import com.benzolamps.dict.dao.base.GroupDao;
import org.springframework.stereotype.Repository;

/**
 * 单词短语分组Dao接口实现类
 * @author Benzolamps
 * @version 2.1.4
 * @datetime 2018-9-21 22:08:37
 */
@Repository("groupDao")
public class GroupDaoImpl extends BaseDaoImpl<Group> implements GroupDao {
}
