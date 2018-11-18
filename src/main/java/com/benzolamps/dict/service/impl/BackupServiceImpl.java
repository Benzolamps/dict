package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.dao.core.DictJpa;
import com.benzolamps.dict.exception.DictException;
import com.benzolamps.dict.service.base.BackupService;
import com.benzolamps.dict.util.DictCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.UnaryOperator;

/**
 * 数据库备份恢复Service接口实现类
 * @author Benzolamps
 * @version 2.2.1
 * @datetime 2018-10-9 18:49:09
 */
@Service("backupService")
@Slf4j
public class BackupServiceImpl implements BackupService {

    @SuppressWarnings("SpringElInspection")
    @Value("#{dictProperties.mysqlRoot}\\bin\\mysqldump -u${spring.datasource.username} -p${spring.datasource.password} ${spring.datasource.name} --default-character-set gbk")
    private String mysqlDumpCmd;

    @Resource
    private UnaryOperator<String> compress;

    @Override
    public void backup(OutputStream outputStream) {
        DictCommand.exec(mysqlDumpCmd, (istr, estr) -> {
            if (StringUtils.hasText(istr)) {
                istr = compress.apply(istr);
                outputStream.write(istr.getBytes("UTF-8"));
            }
            if (StringUtils.hasText(estr)) {
                estr = compress.apply(estr);
                if (estr.toLowerCase().contains("error")) {
                    throw new DictException(estr);
                } else {
                    logger.error(estr);
                }
            }
        });
    }

    @Override
    @SuppressWarnings("SpellCheckingInspection")
    public void restore(InputStream inputStream) {
        DictJpa.executeSqlScript("set foreign_key_checks = 0;");
        DictJpa.executeSqlScript(new InputStreamResource(inputStream));
        DictJpa.executeSqlScript("set foreign_key_checks = 1;");
    }
}
