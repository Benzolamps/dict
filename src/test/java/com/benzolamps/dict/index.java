package com.benzolamps.dict;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Class index
 *
 * @author Benzolamps
 * @version 2.1.2
 * @datetime 2018/9/16 21:14
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public final class index {

    private static final int BUFFER_SIZE = 4096;

    private static long lastDelta = 0;

    private static long total = 0;

    private static long totalSize = 0;

    private static Process process = null;

    public static void main(String... args) {

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (process != null) process.destroy();
        }));

        new Thread(() -> {
            Boolean succeed = null;

            try {
                long start = System.currentTimeMillis();
                if (fileAccess(args[0])) succeed = true;
                long end = System.currentTimeMillis();
                lastDelta = end - start;

            } catch (Throwable e) {
                succeed = false;
                e.printStackTrace();
            }

            try {
                File var1 = new File("templates/static/login.html");
                Desktop.getDesktop().open(var1);
                StringJoiner cmd = new StringJoiner(" ");
                cmd.add("jre/bin/java.exe").add("-jar").add("dict-" + args[1] + ".jar").add("--spring.profiles.active=release");

                if (succeed != null && succeed) {
                    cmd.add("--install.succeed=true")
                            .add("--install.lastDelta=" + lastDelta)
                            .add("--install.total=" + total)
                            .add("--install.totalSize=" + totalSize);
                } else if (succeed != null && !succeed) {
                    cmd.add("--install.succeed=false");
                }
                process = Runtime.getRuntime().exec(cmd.toString());
                try (InputStream is = process.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr)) {
                    br.lines().forEach(System.out::println);
                }
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    private static boolean fileAccess(String path) throws IOException {

        File file = new File(path);

        if (!file.exists() || file.isFile()) return false;

        List<File> zipFiles = Arrays.stream(file.listFiles(index::validate)).sorted(Comparator.comparing(File::getName)).collect(Collectors.toList());

        if (zipFiles.isEmpty()) return false;

        for (File zipFile : zipFiles) {
            List<File> files = unzip(zipFile, path);
            File start = files.stream().filter(f -> f.isFile() && f.getName().toLowerCase().equals("start.txt")).findFirst().orElse(null);
            if (start == null) {
                throw new IllegalArgumentException("start不能为null");
            } else {
                try (Reader fr = new FileReader(start); BufferedReader br = new BufferedReader(fr)) {
                    br.lines().forEach((String str) -> {
                        if (str != null && str.trim().length() > 0) {

                            String[] content = str.trim().split("[ \\t]+");
                            if (content.length < 2) {
                                throw new RuntimeException();
                            }

                            String operation = content[0].toLowerCase();

                            String fileName = content[1];
                            switch (operation) {
                                case "delete": {
                                    File d = new File(fileName);
                                    if (d.exists()) {
                                        if (d.isDirectory()) {
                                            deleteRecursively(d);
                                        } else {
                                            d.delete();
                                        }
                                    }
                                    break;
                                }
                                case "save": {
                                    File d = new File(fileName);
                                    try {
                                        createFile(d);
                                        File s = new File(path + fileName);
                                        totalSize += s.length();
                                        ++total;
                                        try (InputStream is = new FileInputStream(s); OutputStream os = new FileOutputStream(d)) {
                                            copy(is, os);
                                        }
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    break;
                                }
                                default:
                                    throw new IllegalArgumentException(operation + "不存在");
                            }

                        }
                    });
                }
            }
        }

        deleteRecursively(file);

        return true;
    }

    private static boolean validate(File file) {
        if (file != null && file.exists() && !file.isDirectory()) {
            String fileName = file.getName();
            return fileName != null && fileName.contains("-") && fileName.toLowerCase().endsWith(".zip");
        }

        return false;
    }

    private static void deleteRecursively(File root) {
        if (root != null && root.exists()) {
            if (root.isDirectory()) {
                File[] children = root.listFiles();
                if (children != null) {
                    for (File child : children) {
                        deleteRecursively(child);
                    }
                }
            }
            root.delete();
        }
    }

    private static void copy(InputStream in, OutputStream out) throws IOException {
        int byteCount = 0;
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
            byteCount += bytesRead;
        }
        out.flush();
    }

    private static List<File> unzip(File file, String path) throws IOException {
        List<File> files = new ArrayList<>();
        ZipFile zipFile = new ZipFile(file);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry zipEntry = entries.nextElement();
            File entryFile = new File(path + zipEntry.getName());
            if (zipEntry.isDirectory()) {
                createDirectory(entryFile);
            } else {
                createFile(entryFile);
                try (InputStream is = zipFile.getInputStream(zipEntry); OutputStream os = new FileOutputStream(entryFile)) {
                    copy(is, os);
                }
            }

            files.add(entryFile);
        }
        return files;
    }

    private static void createDirectory(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                deleteRecursively(file);
            } else {
                file.delete();
            }
        }
        file.mkdirs();
    }

    private static void createFile(File file) throws IOException {
        if (file.exists() && file.isFile()) return;
        File parent = file.getParentFile();
        if (parent != null && parent.exists() && parent.isFile()) {
            parent.delete();
        }
        if (parent != null) parent.mkdirs();
        file.createNewFile();
    }
}
