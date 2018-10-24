package com.benzolamps.dict.util;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import static java.math.BigDecimal.ROUND_HALF_UP;

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
        BigDecimal layer = new BigDecimal(1 << 10);
        BigDecimal decimal = new BigDecimal(size);
        while (index < 5 && decimal.compareTo(layer) > 0) {
            ++index;
            decimal = decimal.divide(layer, 2, ROUND_HALF_UP);
        }
        return String.format("%.2f ", decimal) + unit[index];
    }

    /**
     * 解压缩
     * @param file 文件
     * @param path 解压路径
     * @return 文件列表
     * @throws IOException IOException
     */
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

    /**
     * 压缩文件
     * @param file 文件
     * @param outputStream 输出流
     * @param rootName 根名称
     * @return ZipOutputStream
     * @throws IOException IOException
     */
    @SuppressWarnings("ConstantConditions")
    static ZipOutputStream zip(File file, OutputStream outputStream, String rootName) throws IOException {
        Assert.isTrue(file != null && file.exists(), "file不能为null或不存在");
        Path basePath = file.getParentFile().getAbsoluteFile().toPath();
        ZipOutputStream zos = new ZipOutputStream(outputStream);
        if (file.isFile()) {
            zos.putNextEntry(new ZipEntry(rootName + '/' + file.getName()));
            try (InputStream is = new FileInputStream(file)) {
                StreamUtils.copy(is, zos);
            }
            zos.closeEntry();
            return zos;
        }
        Stack<File> stack = new Stack<>();
        stack.push(file);
        while (!stack.isEmpty()) {
            File path = stack.pop();
            for (File subFile : path.listFiles()) {
                if (subFile.isDirectory()) {
                    stack.push(subFile);
                } else {
                    Path entryPath = subFile.getAbsoluteFile().toPath();
                    zos.putNextEntry(new ZipEntry(rootName + '/' + entryPath.subpath(basePath.getNameCount(), entryPath.getNameCount()).toString()));
                    try (InputStream is = new FileInputStream(subFile)) {
                        StreamUtils.copy(is, zos);
                    }
                }
            }
        }
        return zos;
    }

}
