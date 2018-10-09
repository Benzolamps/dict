package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.service.base.BackupService;
import com.benzolamps.dict.util.DictFile;
import lombok.var;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;

/**
 * 数据库备份恢复Service接口实现类
 * @author Benzolamps
 * @version 2.2.1
 * @datetime 2018-10-9 18:49:09
 */
@Service("backupService")
public class BackupServiceImpl implements BackupService {

    @Value("mysql\\data")
    private File path;

    @Override
    public void backup(OutputStream outputStream) throws IOException {
        try (var zos = DictFile.zip(path, outputStream, "mysql")) {
            zos.putNextEntry(new ZipEntry("start.txt"));
            try (var pw = new PrintWriter(zos)) {
                pw.println("delete mysql");
                Path root = new File("").getAbsoluteFile().toPath();
                DictFile.deepListFiles(path, null).forEach(file -> {
                    Path subPath = file.getAbsoluteFile().toPath();
                    pw.println("save " + subPath.subpath(root.getNameCount(), subPath.getNameCount()));
                });
                pw.flush();
            }
        }
    }

    @Override
    @SuppressWarnings("SpellCheckingInspection")
    public void restore(InputStream inputStream) throws IOException {
        File file = new File("tmp/dict/restore" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".zip");

        /* 创建父路径 */
        if (file.getParentFile() != null && !file.getParentFile().exists()) file.getParentFile().mkdirs();

        /* 创建新文件 */
        file.createNewFile();

        /* 流复制 */
        try (var is = inputStream; var os = new FileOutputStream(file)) {
            StreamUtils.copy(is, os);
        }
    }
}
