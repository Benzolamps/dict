package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.bean.Library;
import com.benzolamps.dict.service.base.LibraryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 词库Service接口实现类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-1 22:26:11
 */
@Service("libraryService")
@Transactional
public class LibraryServiceImpl extends BaseServiceImpl<Library> implements LibraryService {

    @Override
    @Transactional(readOnly = true)
    public Library getCurrent() {
        return find(1);
    }
}
