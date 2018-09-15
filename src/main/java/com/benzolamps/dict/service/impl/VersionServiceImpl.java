package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.bean.DictProperties;
import com.benzolamps.dict.service.base.VersionService;
import com.benzolamps.dict.util.Constant;
import com.benzolamps.dict.util.DictFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StreamUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
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

    private long size;

    private long lastDelta;

    private Map versionInfo;

    @Value("#{dictProperties.remoteBaseUrl}")
    private String baseUrl;

    private Status status;

    @Resource
    private DictProperties dictProperties;

    @Value("tmp")
    private String tempPath;

    private Thread thread;

    private Consumer<Status> callback = status -> {};

    @Override
    public void update() {
        size = 0;
        lastDelta = 0;
        if (thread == null) {
            thread = new Thread(() -> {
                try {
                    updateProcess();
                } catch (Throwable e) {
                    logger.error(e.getMessage(), e);
                    status = Status.FAILED;
                    callback.accept(status);
                } finally {
                    thread = null;
                }
            });
            thread.start();
        }
    }

    private Map getVersionInfo() {
        try {
            UrlResource resource = new UrlResource(baseUrl + "/yml/version.yml");
            return Constant.YAML.loadAs(resource.getInputStream(), Map.class);
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Status getStatus() {
        if (status == Status.ALREADY_NEW || status == Status.HAS_NEW || status == Status.FAILED || status == null) {
            versionInfo = getVersionInfo();
            if (versionInfo == null || dictProperties.getVersion().equals(versionInfo.get("name"))) {
                return status = Status.ALREADY_NEW;
            } else {
                return status = Status.HAS_NEW;
            }
        }
        return status;
    }

    @Override
    public String getTotalSize() {
        return DictFile.fileSizeStr(size);
    }

    @Override
    public int getTotal() {
        if (versionInfo != null) {
            return ((List)versionInfo.get("files")).size();
        }
        return 0;
    }

    @Override
    public long getDeltaTime() {
        return lastDelta;
    }

    @Override
    public String getNewVersionName() {
        if (versionInfo != null) {
            return (String) versionInfo.get("name");
        }
        return null;
    }

    private void updateProcess() throws Throwable {
        long start = System.currentTimeMillis();
        versionInfo = getVersionInfo();
        if (versionInfo != null) {
            List<String> fileNames = (List<String>) versionInfo.get("files");
            File tempFile = new File(tempPath);
            tempFile.mkdirs();
            status = Status.DOWNLOADING;
            callback.accept(status);
            for (String fileName1 : fileNames) {
                String url = baseUrl + "/" + fileName1;
                UrlResource resource = new UrlResource(url);
                InputStream inputStream = resource.getInputStream();
                File file = new File(tempPath + "/" + fileName1);
                if (file.getParentFile() != null) file.getParentFile().mkdirs();
                OutputStream outputStream = new FileOutputStream(file);
                StreamUtils.copy(inputStream, outputStream);
                inputStream.close();
                outputStream.close();
            }
            status = Status.COPYING;
            callback.accept(status);
            for (String fileName : fileNames) {
                File from = new File(tempPath + "/" + fileName);
                File to = new File(fileName);
                if (to.getParentFile() != null) to.getParentFile().mkdirs();
                to.createNewFile();
                FileCopyUtils.copy(from, to);
                size += to.length();
            }
            status = Status.DELETING;
            callback.accept(status);
            FileSystemUtils.deleteRecursively(tempFile);
            long end = System.currentTimeMillis();
            lastDelta = end - start;
            status = Status.FINISHED;
            dictProperties.setVersion(getNewVersionName());
            callback.accept(status);
        }
    }

    @Override
    public void setStatusCallback(Consumer<Status> callback) {
        this.callback = callback;
    }

    @Override
    public void resetStatus() {
        status = Status.ALREADY_NEW;
    }
}
