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
    public void persist(Group... entities) {
        Arrays.stream(entities).forEach(group -> group.setType(type));
        super.persist(entities);
    }

    @Override
    public Group persist(Group entity) {
        entity.setType(type);
        return super.persist(entity);
    }

    @Override
    public void persist(Collection<Group> entities) {
        entities.forEach(group -> group.setType(type));
        super.persist(entities);
    }

    @Override
    public Group update(Group entity, String... ignoreProperties) {
        return super.update(entity, DictArray.add(ignoreProperties, "type"));
    }

    @Override
    public void update(Collection<Group> entities, String... ignoreProperties) {
        super.update(entities, DictArray.add(ignoreProperties, "type"));
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
