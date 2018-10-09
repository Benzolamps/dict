package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.dao.base.MiscellaneousDao;
import com.benzolamps.dict.service.base.MiscellaneousService;
import com.benzolamps.dict.service.base.WordClazzService;
import com.benzolamps.dict.util.DictFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

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

    @Resource
    private WordClazzService wordClazzService;

    @Value("file:E:\\test")
    private File path;

    @Override
    public String getMysqlVersion() {
        return miscellaneousDao.getMysqlVersion();
    }

    @Override
    public String databaseFileSize() {
       return DictFile.fileSizeStr(miscellaneousDao.dataSize());
    }

    @Override
    public void clean() {
        wordClazzService.clearUseless();
        miscellaneousDao.clear();
    }

    @Override
    public void backup(OutputStream outputStream) throws IOException {
        DictFile.zip(path, outputStream);
    }
}
