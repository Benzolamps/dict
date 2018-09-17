package com.benzolamps.dict.util;

import com.benzolamps.dict.exception.DictException;
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
import static com.benzolamps.dict.util.DictLambda.tryFunc;

/**
 * 动态编译并加载目录下的全部.java文件
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-18 22:04:21
 */
@SuppressWarnings({"unused", "unchecked"})
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
    @SuppressWarnings({"SpellCheckingInspection", "ConstantConditions"})
    public void compile() {
        if (!compiled && (compiled = true)) {
            StandardJavaFileManager manager = compiler.getStandardFileManager(diagnosticListener, locale, charset);
            Set<File> files = DictFile.deepListFiles(file, f -> f.getName().toLowerCase().endsWith(".java"));
            Iterable<? extends JavaFileObject> iterable = manager.getJavaFileObjectsFromFiles(files);
            List<String> options = Arrays.asList("-Xlint:unchecked", "-classpath", System.getProperty("java.class.path"));
            JavaCompiler.CompilationTask task = compiler.getTask(null, produceJavaFileManager(manager), null, options, null, iterable);
            Boolean result = task.call();
            if (result == null || !result) {
                throw new DictException("编译失败");
            }
            classBytes.keySet().forEach((DictLambda.Action1<String>) key -> dynamicClassSet.add(classLoader.loadClass(key)));
            DictSpring.removeBean("classLoader");
            DictSpring.setBean("classLoader", classLoader);
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

    /**
     * 构造器
     * @param classPath 类加载路径
     */
    public DynamicClass(String classPath) {
        Assert.notNull(classPath, "class path不能为null");
        if (compiler == null) {
            throw new DictException("需要tools.jar");
        }

        classLoader = new URLClassLoader(new URL[0], DictSpring.getBean("classLoader")) {
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

        file = new File(classPath);
        classBytes = new HashMap<>();
        dynamicClassSet = new HashSet<>();
    }

    public static <T> Class<T> loadClass(String className) {
        return (Class<T>) tryFunc(() -> ClassUtils.forName(className, DictSpring.getBean("classLoader")));
    }
}
