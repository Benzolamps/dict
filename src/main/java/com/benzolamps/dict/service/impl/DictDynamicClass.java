package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.util.Constant;
import com.benzolamps.dict.util.DictFile;
import com.benzolamps.dict.util.DictSpring;
import com.benzolamps.dict.util.lambda.Action1;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import javax.tools.*;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.*;

import static javax.tools.JavaFileObject.Kind;
import static javax.tools.JavaFileObject.Kind.CLASS;

/**
 * 动态编译并加载目录下的全部.java文件
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-18 22:04:21
 */
@Slf4j
@Component
@SuppressWarnings({"unused", "unchecked"})
public class DictDynamicClass {

    /* 目录 */
    @Value("#{dictProperties.universePath}")
    private File file;

    /* 编译错误监听 */
    private DiagnosticListener<? super JavaFileObject> diagnosticListener = diagnostic -> {};

    /* locale */
    private Locale locale = Locale.SIMPLIFIED_CHINESE;

    /* charset */
    private Charset charset = Constant.UTF8_CHARSET;

    /* 类byte[] */
    @Value("#{new java.util.HashMap()}")
    private Map<String, byte[]> classBytes;

    /* 全部的动态类 */
    @Value("#{new java.util.HashSet()}")
    private Set<Class<?>> dynamicClassSet;

    /* 获取编译器 */
    private static JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

    private boolean compiled = false;

    private final ClassLoader classLoader;

    /** @return 获取编译错误监听 */
    public DiagnosticListener<? super JavaFileObject> getDiagnosticListener() {
        return diagnosticListener;
    }

    /** @param diagnosticListener 设置编译错误监听 */
    public void setDiagnosticListener(DiagnosticListener<? super JavaFileObject> diagnosticListener) {
        this.diagnosticListener = diagnosticListener;
    }

    /** @return 获取Locale */
    public Locale getLocale() {
        return locale;
    }

    /** @param locale 设置Locale */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    /** @return 获取Charset */
    public Charset getCharset() {
        return charset;
    }

    /** @param charset 设置Charset */
    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    /** @return 获取全部的动态类 */
    public Set<Class<?>> getDynamicClassSet() {
        this.compile();
        return dynamicClassSet;
    }

    /** 编译 */
    @SuppressWarnings("ConstantConditions")
    public void compile() {
        if (!compiled && (compiled = true)) {
            StandardJavaFileManager manager = compiler.getStandardFileManager(diagnosticListener, locale, charset);
            Set<File> files = DictFile.deepListFiles(file, f -> f.getName().toLowerCase().endsWith(".java"));
            Iterable<? extends JavaFileObject> iterable = manager.getJavaFileObjectsFromFiles(files);
            List<String> options = Arrays.asList("-Xlint:unchecked", "-classpath", System.getProperty("java.class.path"));
            JavaCompiler.CompilationTask task = compiler.getTask(null, produceJavaFileManager(manager), null, options, null, iterable);
            Boolean result = task.call();
            Assert.isTrue(result != null && result, "编译失败");
            logger.info("compiled classes: " + classBytes.keySet());
            classBytes.keySet().forEach((Action1<String>) key -> dynamicClassSet.add(classLoader.loadClass(key)));
            DictSpring.setClassLoader(classLoader);
        }
    }

    /* 生成JavaFileManager */
    private ForwardingJavaFileManager<JavaFileManager> produceJavaFileManager(JavaFileManager manager) {
        return new ForwardingJavaFileManager<JavaFileManager>(manager) {
            @Override
            public JavaFileObject getJavaFileForOutput(Location location, String className, Kind kind, FileObject sibling) throws IOException {

                if (kind != CLASS) {
                    return super.getJavaFileForOutput(location, className, kind, sibling);
                }

                return new SimpleJavaFileObject(URI.create(""), kind) {
                    @Override
                    public OutputStream openOutputStream() {
                        return new FilterOutputStream(new ByteArrayOutputStream()) {
                            @Override
                            public void close() throws IOException {
                                classBytes.put(className, ((ByteArrayOutputStream) out).toByteArray());
                                out.close();
                            }
                        };
                    }
                };
            }
        };
    }

    /**
     * 构造器
     */
    public DictDynamicClass() {
        Assert.notNull(compiler, "需要tools.jar");
        classLoader = new URLClassLoader(new URL[0], DictSpring.getClassLoader()) {
            @Override
            protected Class<?> findClass(String name) throws ClassNotFoundException {
                byte[] buf = classBytes.get(name);
                if (buf == null) {
                    return super.findClass(name);
                }
                classBytes.put(name, null);
                return defineClass(name, buf, 0, buf.length);
            }
        };
    }

    @SneakyThrows(ClassNotFoundException.class)
    public static <T> Class<T> loadClass(String className) {
        return (Class<T>) ClassUtils.forName(className, DictSpring.getClassLoader());
    }
}
