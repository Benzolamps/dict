package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.dao.base.MiscellaneousDao;
import com.benzolamps.dict.service.base.MiscellaneousService;
import com.benzolamps.dict.util.DictFile;
import com.benzolamps.dict.util.DictLambda;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 杂项Service接口实现类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-1 11:26:51
 */
@Service("miscellaneousService")
public class MiscellaneousServiceImpl implements MiscellaneousService {

    @Resource
    private MiscellaneousDao miscellaneousDao;

    @Value("file:${jdbc.file}")
    private org.springframework.core.io.Resource resource;

    @Override
    public String getSQLiteVersion() {
        return miscellaneousDao.getSQLiteVersion();
    }

    @Override
    public String databaseFileSize() {
       return DictFile.fileSizeStr(DictLambda.tryFunc(resource::contentLength));
    }

    @Override
    public void vacuum() {
        miscellaneousDao.vacuum();
    }
}
