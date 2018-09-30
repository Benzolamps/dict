package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.cfg.DictProperties;
import com.benzolamps.dict.service.base.VersionService;
import com.benzolamps.dict.util.Constant;
import com.benzolamps.dict.util.DictFile;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StreamUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.function.Consumer;

/**
 * 版本Service接口实现类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-14 21:28:51
 */
@SuppressWarnings({"ResultOfMethodCallIgnored", "unchecked"})
@Service("versionService")
@Slf4j
public class VersionServiceImpl implements VersionService {

    @Value("${install.total:0}")
    private int total;

    @Value("${install.totalSize:0}")
    private long totalSize;

    @Value("${install.lastDelta:0}")
    private long lastDelta;

    @Value("${install.succeed:}")
    private Boolean succeed;

    private boolean dead;

    private List<String> versionInfo = Constant.EMPTY_LIST;

    private String newVersionName;

    @Value("#{dictProperties.remoteBaseUrl}")
    private String baseUrl;

    private Status status = Status.ALREADY_NEW;

    @Resource
    private DictProperties dictProperties;

    @Value("tmp")
    private String tempPath;

    private Thread thread;

    private Consumer<Status> callback = status -> {};

    @Override
    public void update() {
        /* 下载完成或者更新完成的状态时不能再次更新 */
        if (status != Status.DOWNLOADED && status != Status.INSTALLED) {

            /* 开启一个更新线程 */
            if (thread == null) {
                (thread = new Thread(() -> {
                    try {
                        updateProcess();
                    } catch (Throwable e) {
                        logger.error(e.getMessage(), e);
                        status = Status.FAILED;
                        callback.accept(status);
                    } finally {
                        thread = null;
                    }
                })).start();
            }
        }
    }

    /* 重置版本信息 */
    @PostConstruct
    @Scheduled(fixedRate = 1000 * 60 * 15)
    private void resetVersionInfo() {

        if (succeed != null && status == Status.ALREADY_NEW) {
            status = succeed ? Status.INSTALLED : Status.FAILED;
            return;
        }

        try {
            /* 下载完成或者更新完成的状态时不能再次更新 */
            if (status != Status.DOWNLOADED && status != Status.INSTALLED) {
                UrlResource resource = new UrlResource(baseUrl + "/dict/version.yml");
                versionInfo = Constant.YAML.loadAs(resource.getInputStream(), List.class);

                /* 判断是否有新版本 */
                newVersionName = getVersion(versionInfo.stream().max(String::compareToIgnoreCase).orElse(null));
                if (dictProperties.getVersion().compareTo(newVersionName) < 0) {
                    if (status != Status.FAILED && status != Status.HAS_NEW) {
                        status = Status.HAS_NEW;
                    }
                }
            }
        } catch (Throwable e) {
            if (newVersionName == null) newVersionName = dictProperties.getVersion();
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public Status getStatus() {
       return status;
    }

    /* 从文件名中提取版本号 */
    private String getVersion(String str) {
        if (str == null || !(str = str.toLowerCase()).contains("-") || !str.endsWith(".zip")) {
            return null;
        }
        return str.substring(str.lastIndexOf("-") + 1, str.indexOf(".zip"));
    }

    @Override
    public String getTotalSize() {
        return DictFile.fileSizeStr(totalSize);
    }

    @Override
    public int getTotal() {
        return total;
    }

    @Override
    public long getDeltaTime() {
        return lastDelta;
    }

    @Override
    public String getNewVersionName() {
        return newVersionName;
    }

    private void updateProcess() throws Throwable {

        if (versionInfo != null) {

            /* 重置各种计数器 */
            totalSize = 0; lastDelta = 0; total = 0; dead = false;

            long start = System.currentTimeMillis();

            /* 更改状态 */
            status = Status.DOWNLOADING;
            callback.accept(status);

            /* 创建临时路径 */
            File tempFile = new File(tempPath);
            if (tempFile.exists()) {
                if (tempFile.isDirectory()) FileSystemUtils.deleteRecursively(tempFile);
                else tempFile.delete();
            }
            tempFile.mkdirs();

            /* 下载文件 */
            for (String fileName : versionInfo) {

                String version = getVersion(fileName);

                /* 判断是不是新版本 */
                if (version != null && dictProperties.getVersion().compareTo(version) < 0) {

                    /* 生成本地路径 */
                    String url = baseUrl + "/" + fileName;
                    UrlResource resource = new UrlResource(url);
                    File file = new File(tempPath + "/" + fileName);

                    /* 创建父路径 */
                    if (file.getParentFile() != null && !file.getParentFile().exists()) file.getParentFile().mkdirs();

                    /* 创建新文件 */
                    file.createNewFile();

                    /* 流复制 */
                    try (var is = resource.getInputStream(); var os = new FileOutputStream(file)) {
                        int size = StreamUtils.copy(is, os);
                        this.totalSize += size;
                        ++total;
                    }
                }
            }

            long end = System.currentTimeMillis();
            lastDelta = end - start;

            /* 更改状态 */
            status = Status.DOWNLOADED;
            callback.accept(status);
        }
    }

    @Override
    public void setStatusCallback(Consumer<Status> callback) {
        this.callback = callback;
    }

    @Override
    public void die() {
        dead = true;
    }

    @Override
    public boolean isDead() {
        return dead;
    }
}
