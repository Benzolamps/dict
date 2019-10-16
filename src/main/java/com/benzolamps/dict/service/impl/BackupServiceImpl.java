package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.exception.DictException;
import com.benzolamps.dict.service.base.BackupService;
import com.benzolamps.dict.service.base.MiscellaneousService;
import com.benzolamps.dict.util.DictCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.UnaryOperator;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 数据库备份恢复Service接口实现类
 * @author Benzolamps
 * @version 2.2.1
 * @datetime 2018-10-9 18:49:09
 */
@Service("backupService")
@Slf4j
public class BackupServiceImpl implements BackupService {

    @Value("#{miscellaneousService.mysqlBaseDir}\\bin\\mysqldump -u${spring.datasource.username} -p${spring.datasource.password} ${spring.datasource.name} --hex-blob --default-character-set gbk")
    private String mysqlDumpCmd;

    @Resource
    private MiscellaneousService miscellaneousService;

    @Resource
    private UnaryOperator<String> compress;

    @SuppressWarnings("SpellCheckingInspection")
    @Override
    public void backup(OutputStream outputStream) {
        DictCommand.exec(mysqlDumpCmd, (istr, estr) -> {
            if (StringUtils.hasText(istr)) {
                outputStream.write(istr.getBytes(UTF_8));
            }
            if (StringUtils.hasText(estr)) {
                estr = compress.apply(estr);
                if (estr.toLowerCase().contains("error")) {
                    throw new DictException(estr);
                } else {
                    logger.info(estr);
                }
            }
        });
    }

    @Override
    public void restore(InputStream inputStream) {
        miscellaneousService.executeSqlScript("set foreign_key_checks = 0;");
        miscellaneousService.executeSqlScript(inputStream);
        miscellaneousService.executeSqlScript("set foreign_key_checks = 1;");
    }
}
