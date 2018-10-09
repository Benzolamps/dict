package com.benzolamps.dict.util;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * 用于操作文件的类
 * @author Benzolamps
 * @version 1.1.1
 * @datetime 2018-6-12 13:15:21
 */
@SuppressWarnings("unused")
@Component
public interface DictFile {

    /**
     * 深度列出目录所有文件
     * @param file 目录
     * @param filter 过滤器
     * @return 文件集合
     */
    @SuppressWarnings("ConstantConditions")
    static Set<File> deepListFiles(File file, Predicate<File> filter) {
        Assert.state(file != null && file.isDirectory(), "file不是目录");
        Stack<File> stack = new Stack<>();
        Set<File> files = new HashSet<>();
        stack.push(file);
        while (!stack.isEmpty()) {
            File path = stack.pop();
            for (File subFile : path.listFiles(f -> f.isDirectory() || filter == null || filter.test(f))) {
                if (subFile.isDirectory()) {
                    stack.push(subFile);
                } else {
                    files.add(subFile);
                }
            }
        }

        return files;
    }

    /**
     * 将long型转换为文件大小字符串
     * @param size 大小
     * @return 字符串
     */
    static String fileSizeStr(long size) {
        String[] unit = {"B", "KB", "MB", "GB", "TB", "PB"};
        int index = 0;
        double doubleSize = size;
        while (index < 5 && doubleSize >= 1024) {
            ++index;
            doubleSize /= 1024;
        }
        return String.format("%.2f ", doubleSize) + unit[index];
    }

    static List<File> unzip(File file, String path) throws IOException {
        List<File> files = new ArrayList<>();
        try (ZipFile zipFile = new ZipFile(file)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry zipEntry = entries.nextElement();
                File entryFile = new File(path + zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    if (entryFile.isFile()) {
                        Assert.isTrue(entryFile.delete(), "删除entry file失败");
                    }
                    Assert.isTrue(entryFile.mkdirs(), "创建entry file失败");
                } else {
                    if (entryFile.isDirectory()) {
                        Assert.isTrue(entryFile.delete(), "删除entry file失败");
                    }
                    Assert.isTrue(entryFile.createNewFile(), "创建entry file失败");
                    try (InputStream is = zipFile.getInputStream(zipEntry); OutputStream os = new FileOutputStream(entryFile)) {
                        StreamUtils.copy(is, os);
                    }
                }
                files.add(entryFile);
            }
        }
        return files;
    }

    @SuppressWarnings("ConstantConditions")
    static void zip(File file, OutputStream outputStream) throws IOException {
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        Assert.isTrue(file != null && file.exists(), "file不能为null或不存在");
        if (file.isFile()) {
            zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
            StreamUtils.copy(new FileInputStream(file), zipOutputStream);
            zipOutputStream.closeEntry();
            zipOutputStream.finish();
            zipOutputStream.close();
            return;
        }
        Stack<File> stack = new Stack<>();
        stack.push(file);
        while (!stack.isEmpty()) {
            File path = stack.pop();
            for (File subFile : path.listFiles()) {
                if (subFile.isDirectory()) {
                    stack.push(subFile);
                } else {
                    zipOutputStream.putNextEntry(new ZipEntry(subFile.toPath().getName(file.getParentFile().toPath().getNameCount()).toString()));
                    StreamUtils.copy(new FileInputStream(subFile), zipOutputStream);
                }
            }
        }
        zipOutputStream.finish();
        zipOutputStream.close();
    }

}
