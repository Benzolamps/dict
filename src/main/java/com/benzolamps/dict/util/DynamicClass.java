package com.benzolamps.dict.util;

import com.benzolamps.dict.exception.DictException;

import javax.tools.*;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.*;

import static javax.tools.JavaFileObject.Kind;

/**
 * 动态编译并加载目录下的全部.java文件
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-18 22:04:21
 */
@SuppressWarnings("unused")
public class DynamicClass {

    /* 目录 */
    private File file;

    /* 编译错误监听 */
    private DiagnosticListener<? super JavaFileObject> diagnosticListener = diagnostic -> {};

    /* locale */
    private Locale locale = Locale.SIMPLIFIED_CHINESE;

    /* charset */
    private Charset charset = Charset.forName("UTF-8");

    /* 类byte[] */
    private Map<String, byte[]> classBytes;

    /* 全部的动态类 */
    private Set<Class<?>> dynamicClassSet;

    /* 获取编译器 */
    private static JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

    private boolean compiled = false;

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
        return dynamicClassSet;
    }

    /** 编译 */
    @SuppressWarnings("SpellCheckingInspection")
    public void compile() {
        if (!compiled && (compiled = true)) {
            StandardJavaFileManager manager = compiler.getStandardFileManager(diagnosticListener, locale, charset);
            ClassLoader classLoader = produceClassLoader();
            Set<File> files = DictFile.deepListFiles(file, f -> f.getName().toLowerCase().endsWith(".java"));
            Iterable<? extends JavaFileObject> iterable = manager.getJavaFileObjectsFromFiles(files);
            List<String> options = Arrays.asList("-Xlint:unchecked", "-classpath", System.getProperty("java.class.path"));
            JavaCompiler.CompilationTask task = compiler.getTask(null, produceJavaFileManager(manager), null, options, null, iterable);
            Boolean result = task.call();
            if (result == null || !result) {
                throw new DictException("编译失败!");
            }
            classBytes.keySet().forEach((DictLambda.Action1<String>) key -> dynamicClassSet.add(classLoader.loadClass(key)));
        }
    }

    /* 生成JavaFileManager */
    private ForwardingJavaFileManager<JavaFileManager> produceJavaFileManager(JavaFileManager manager) {
        return new ForwardingJavaFileManager<JavaFileManager>(manager) {
            @Override
            public JavaFileObject getJavaFileForOutput(Location location, String className, Kind kind, FileObject sibling) throws IOException {

                if (kind != Kind.CLASS) {
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

    /* 生成ClassLoader */
    private URLClassLoader produceClassLoader() {
        return new URLClassLoader(new URL[0], getClass().getClassLoader()) {
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

    /**
     * 构造器
     * @param classPath 类加载路径
     */
    public DynamicClass(String classPath) {
        if (compiler == null) {
            throw new DictException("需要tools.jar");
        }

        file = new File(classPath);
        classBytes = new HashMap<>();
        dynamicClassSet = new HashSet<>();
    }

    /**
     * 加载一个动态的类
     * @param className 类名
     * @return 类对象
     */
    public Class<?> loadDynamicClass(String className) {
        this.compile();
        return dynamicClassSet.stream().filter(clazz -> className.equals(clazz.getName())).findFirst().get();
    }
}
