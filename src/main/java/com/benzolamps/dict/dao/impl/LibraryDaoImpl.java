package com.benzolamps.dict.dao.impl;

import com.benzolamps.dict.bean.Library;
import com.benzolamps.dict.dao.base.LibraryDao;
import org.springframework.stereotype.Repository;

/**
 * 词库Dao接口实现类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-1 22:16:40
 */
@Repository("libraryDao")
public class LibraryDaoImpl extends BaseDaoImpl<Library> implements LibraryDao {
}
