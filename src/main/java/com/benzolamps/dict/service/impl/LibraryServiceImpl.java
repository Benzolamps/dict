package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.bean.Library;
import com.benzolamps.dict.bean.User;
import com.benzolamps.dict.dao.base.LibraryDao;
import com.benzolamps.dict.dao.core.Filter;
import com.benzolamps.dict.service.base.LibraryService;
import com.benzolamps.dict.service.base.PhraseService;
import com.benzolamps.dict.service.base.UserService;
import com.benzolamps.dict.service.base.WordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collection;

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

    @Resource
    private WordService wordService;

    @Resource
    private PhraseService phraseService;

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
    public Long count() {
        return userService.getCurrent() == null ? 0 : count(null);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean nameExists(String name) {
        return count(Filter.eq("name", name)) > 0;
    }

    @Override
    public void remove(Collection<Library> libraries) {
        for (Library library : libraries) {
            if (library != null) {
                if (!CollectionUtils.isEmpty(library.getWords())) {
                    wordService.remove(library.getWords());
                }
                if (!CollectionUtils.isEmpty(library.getPhrases())) {
                    phraseService.remove(library.getPhrases());
                }
            }
        }
        super.remove(libraries);
        setCurrent(null);
    }
}
