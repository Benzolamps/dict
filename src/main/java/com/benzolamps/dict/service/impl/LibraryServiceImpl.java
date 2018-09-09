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
        if (count() <= 0) {
            return null;
        } else {
            User user = userService.getCurrent();
            if (user.getLibrary() == null) {
                user.setLibrary(libraryDao.findCount(1, (Filter) null).get(0));
            }
            return user.getLibrary();
        }
    }

    @Override
    public void setCurrent(Library library) {
        User user = userService.getCurrent();
        Assert.notNull(library, "library不能为null");
        Assert.isTrue(!library.isNew(), "library不能为新建对象");
        user.setLibrary(library);
    }

    @Override
    public Long count() {
        return userService.getCurrent() == null ? 0 : count(null);
    }

    @Override
    public boolean nameExists(String name) {
        return count(Filter.eq("name", name)) > 0;
    }
}
