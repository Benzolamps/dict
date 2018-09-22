package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.bean.Group;
import com.benzolamps.dict.bean.Group.Type;
import com.benzolamps.dict.dao.base.GroupDao;
import com.benzolamps.dict.dao.core.Filter;
import com.benzolamps.dict.service.base.GroupService;
import com.benzolamps.dict.util.DictArray;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collection;

/**
 * 单词短语分组Service接口实现类
 * @author Benzolamps
 * @version 2.1.4
 * @datetime 2018-9-21 22:09:55
 */
public abstract class GroupServiceImpl extends BaseServiceImpl<Group> implements GroupService {

    private final Type type;

    @Resource
    private GroupDao groupDao;

    protected GroupServiceImpl(Type type) {
        Assert.notNull(type, "type不能为null");
        this.type = type;
    }

    @Override
    public Group find(Integer id) {
        Group group = super.find(id);
        return type.equals(group.getType()) ? group : null;
    }

    @Override
    public void persist(Group... groups) {
        Arrays.stream(groups).forEach(group -> {
            group.setType(type);
            group.setStatus(Group.Status.NORMAL);
        });
        super.persist(groups);
    }

    @Override
    public Group persist(Group group) {
        group.setType(type);
        group.setStatus(Group.Status.NORMAL);
        return super.persist(group);
    }

    @Override
    public void persist(Collection<Group> groups) {
        groups.forEach(group -> {
            group.setType(type);
            group.setStatus(Group.Status.NORMAL);
        });
        super.persist(groups);
    }

    @Override
    public Group update(Group group, String... ignoreProperties) {
        return super.update(group, DictArray.concat(ignoreProperties, new String[] {"type", "status"}));
    }

    @Override
    public void update(Collection<Group> groups, String... ignoreProperties) {
        super.update(groups, DictArray.concat(ignoreProperties, new String[] {"type", "status"}));
    }

    @Override
    protected void handlerFilter(Filter filter) {
        filter.and(Filter.eq("type", type));
    }

    @Override
    public boolean nameExists(String name) {
        return groupDao.count(Filter.eq("name", name)) > 0;
    }
}
