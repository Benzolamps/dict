package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.bean.Library;
import com.benzolamps.dict.bean.User;
import com.benzolamps.dict.dao.base.LibraryDao;
import com.benzolamps.dict.dao.core.Filter;
import com.benzolamps.dict.service.base.LibraryService;
import com.benzolamps.dict.service.base.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 词库Service接口实现类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-1 22:26:11
 */
@Service("libraryService")
@Transactional
public class LibraryServiceImpl extends BaseServiceImpl<Library> implements LibraryService {

    @Resource
    private UserService userService;

    @Resource
    private LibraryDao libraryDao;

    @Override
    @Transactional
    public Library getCurrent() {
        User user = userService.getCurrent();
        if (user == null) {
            return null;
        }
        if (user.getLibrary() == null && count() > 0) {
            user.setLibrary(libraryDao.findCount(1, (Filter) null).get(0));
        }
        return user.getLibrary();
    }

    @Override
    @Transactional
    public void setCurrent(Library library) {
        User user = userService.getCurrent();
        Assert.isTrue(library == null || !library.isNew(), "library不能为新建对象");
        user.setLibrary(library);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer count() {
        return userService.getCurrent() == null ? 0 : count(null);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean nameExists(String name) {
        return count(Filter.eq("name", name)) > 0;
    }

    @Override
    public void remove(Collection<Library> libraries) {
        Assert.isTrue(!libraries.contains(getCurrent()), "不能删除当前词库");
        /* 将使用此词库的用户词库设为空 */
        List<User> users = userService.findList(Filter.in("library", libraries));
        users.forEach(user -> user.setLibrary(null));
        libraryDao.flush();
        super.remove(libraries);
    }
}
