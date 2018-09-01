package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.dao.base.MiscellaneousDao;
import com.benzolamps.dict.service.base.MiscellaneousService;
import org.springframework.stereotype.Service;

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

    @Override
    public String getSQLiteVersion() {
        return miscellaneousDao.getSQLiteVersion();
    }
}
