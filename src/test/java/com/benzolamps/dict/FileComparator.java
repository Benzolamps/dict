package com.benzolamps.dict;

import org.aspectj.lang.annotation.AfterThrowing;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.benzolamps.dict.util.Constant.TEXT_FILES;
import static com.benzolamps.dict.util.DictFile.deepListFiles;
import static com.benzolamps.dict.util.DictResource.closeCloseable;
import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;
import static org.springframework.util.FileCopyUtils.copy;
import static org.springframework.util.FileCopyUtils.copyToByteArray;
import static org.springframework.util.FileSystemUtils.deleteRecursively;

/**
 * Class FileComparator
 *
 * @author Benzolamps
 * @version 2.1.2
 * @datetime 2018/9/18 19:39
 */
@SuppressWarnings("FieldCanBeLocal")
public class FileComparator {

    private File src;
    private File dest;
    private File diff;
    private File start;
    private OutputStream os;
    private PrintStream ps;

    @Before
    public void before() throws IOException {
        src = new File("D:\\src");
        dest = new File("D:\\dest");
        diff = new File("D:\\diff");
        start = new File(diff, "start.txt");

        isTrue(src.isDirectory(), "src不是目录");
        isTrue(dest.isDirectory(), "dest不是目录");
        if (diff.isFile()) {
            isTrue(diff.delete(), "diff删除失败");
        } else if (diff.isDirectory()) {
            isTrue(deleteRecursively(diff), "diff删除失败");
        }
        isTrue(diff.mkdirs(), "创建diff目录失败");
        isTrue(start.createNewFile(), "创建start.txt失败");
        os = new FileOutputStream(start);
        ps = new PrintStream(os);
    }


    @SuppressWarnings("UnnecessaryContinue")
    @Test
    public void execute() throws IOException {
        Set<File> srcFiles = deepListFiles(src, null);
        Set<File> destFiles = deepListFiles(dest, null);
        List<String> srcPaths = srcFiles.stream().map(this::comparablePart).collect(Collectors.toList());
        List<String> destPaths = destFiles.stream().map(this::comparablePart).collect(Collectors.toList());
        List<String> srcOnly = srcPaths.stream()
            .filter(((Predicate<String>) destPaths::contains).negate())
            .collect(Collectors.toList());
        List<String> destOnly = destPaths.stream()
            .filter(((Predicate<String>) srcPaths::contains).negate())
            .collect(Collectors.toList());
        List<String> same = srcPaths.stream().filter(destPaths::contains).collect(Collectors.toList());
        srcOnly.sort(String::compareToIgnoreCase);
        destOnly.sort(String::compareToIgnoreCase);
        same.sort(String::compareToIgnoreCase);
        srcOnly.forEach(path -> ps.println("delete " + path));
        for (String path : destOnly) {
            ps.println("save " + path);
            File news = new File(dest, path);
            File diffs = new File(diff, path);
            if (news.isFile()) {
                File parent = diffs.getParentFile();
                if (!parent.isDirectory()) {
                    isTrue(parent.mkdirs(), "创建parent目录失败");
                }
                isTrue(diffs.createNewFile(), "创建diffs文件失败");
                copy(news, diffs);
            } else {
                isTrue(diffs.mkdirs(), "创建diffs目录失败");
            }
        }
        for (String path : same) {
            File old = new File(src, path);
            File news = new File(dest, path);
            File diffs = new File(diff, path);
            if (old.isDirectory() && news.isDirectory()) {
                continue;
            } else if (old.isFile() && news.isDirectory()) {
                isTrue(diffs.mkdirs(), "创建diffs目录失败");
            } else {
                if (!compare(old, news)) {
                    ps.println("save " + path);
                    File parent = diffs.getParentFile();
                    if (!parent.isDirectory()) {
                        isTrue(parent.mkdirs(), "创建parent目录失败");
                    }
                    isTrue(diffs.createNewFile(), "创建diffs文件失败");
                    copy(news, diffs);
                }
            }
        }
    }

    private boolean compare(File file1, File file2) throws IOException {
        isTrue(file1.isFile(), "file1不是文件");
        isTrue(file2.isFile(), "file2不是文件");
        if (Arrays.stream(TEXT_FILES).anyMatch(file1.getName()::endsWith)) {
            try (InputStream is1 = new FileInputStream(file1);
                 InputStreamReader isr1 = new InputStreamReader(is1);
                 BufferedReader br1 = new BufferedReader(isr1);
                 InputStream is2 = new FileInputStream(file2);
                 InputStreamReader isr2 = new InputStreamReader(is2);
                 BufferedReader br2 = new BufferedReader(isr2)
            ) {
                if (Arrays.stream(TEXT_FILES).anyMatch(file1.getName().toLowerCase()::endsWith)) {
                    String line1, line2;
                    while ((line1 = br1.readLine()) != null) {
                        line2 = br2.readLine();
                        if (!Objects.equals(line1, line2)) {
                            return false;
                        }
                    }
                    return br2.readLine() == null;
                }
            }
        } else {
            byte[] bytesOld = copyToByteArray(file1);
            byte[] bytesNew = copyToByteArray(file2);
            return Objects.deepEquals(bytesOld, bytesNew);
        }
        return false;
    }

    private String comparablePart(File file) {
        notNull(file, "file不能为null");
        Path path = file.toPath();
        return path.subpath(1, path.getNameCount()).toString();
    }

    @After
    @AfterThrowing
    public void after() {
        closeCloseable(ps);
        closeCloseable(os);
    }
}
