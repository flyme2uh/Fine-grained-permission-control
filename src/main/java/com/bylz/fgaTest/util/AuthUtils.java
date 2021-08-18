package com.bylz.fgaTest.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @ClassName: AuthUtils
 * @Description:
 * @Author chenzhipeng
 * @Date 2021/8/16
 * @Version 1.0
 */
public class AuthUtils {

    @Autowired
    private static ResourceLoader resourceLoader;

    private static final String VALUE = "value";

    public static void main(String[] args){
        //获取注解集合
        Set<Class<?>> clzFromPkg = getClzFromPkg("com.bylz.fgaTest.annotation");
        Map<String, ArrayList> authPathMap = new HashMap<>();
        for (Class<?> annotation : clzFromPkg) {
            Map<String, Map<String, Object>> allAddTagAnnotationUrl = null;
            try {
                //获取标记了注解的controller的url
                allAddTagAnnotationUrl = getAllAddTagAnnotationUrl("classpath:com/bylz/fgaTest/controller/*.class", annotation);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ArrayList pathList = new ArrayList();
            for (String s : allAddTagAnnotationUrl.keySet()) {
                pathList.add(s);
            }
            String annotationFullName = annotation.getName();
            String[] split = annotationFullName.split("\\.");
            String annotationName = split[split.length - 1];
            authPathMap.put(annotationName,pathList);
        }
        Yaml yml = new Yaml();
        try {
            FileWriter writer = new FileWriter(new File( "src/main/resources/authPath.yml"));
            writer.write(yml.dumpAsMap(authPathMap));
            writer.write("\n\n#本yml中的path为拒绝该权限访问的path");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取指定包下所有添加了执行注解的方法信息
     * @param classPath 包名
     * @param tagAnnotationClass 指定注解类型
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> Map<String, Map<String, Object>> getAllAddTagAnnotationUrl(String classPath, Class<T> tagAnnotationClass) throws Exception {
        Map<String, Map<String, Object>> resMap = new HashMap<>();
        ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        MetadataReaderFactory metaReader = new CachingMetadataReaderFactory(resourceLoader);
        Resource[] resources = resolver.getResources(classPath);

        for (org.springframework.core.io.Resource r : resources) {
            MetadataReader reader = metaReader.getMetadataReader(r);
            resMap = resolveClass(reader, resMap, tagAnnotationClass);
        }


        return resMap;
    }

    private static <T> Map<String, Map<String, Object>> resolveClass(
            MetadataReader reader, Map<String, Map<String, Object>> resMap, Class<T> tagAnnotationClass)
            throws Exception {
        String tagAnnotationClassCanonicalName = tagAnnotationClass.getCanonicalName();
        //获取注解元数据
        AnnotationMetadata annotationMetadata = reader.getAnnotationMetadata();
        //获取类中RequestMapping注解的属性
        Map<String, Object> annotationAttributes =
                annotationMetadata.getAnnotationAttributes(RequestMapping.class.getCanonicalName());

        //若类无RequestMapping注解
        if (annotationAttributes == null) return resMap;

        //获取RequestMapping注解的value
        String[] pathParents = (String[]) annotationAttributes.get(VALUE);
        if (0 == pathParents.length) return resMap;

        //获取RequestMapping注解的value
        String pathParent = pathParents[0];

        //获取当前类中已添加要扫描注解的方法
        Set<MethodMetadata> annotatedMethods = annotationMetadata.getAnnotatedMethods(tagAnnotationClassCanonicalName);

        for (MethodMetadata annotatedMethod : annotatedMethods) {
            //获取当前方法中要扫描注解的属性
            Map<String, Object> targetAttr = annotatedMethod.getAnnotationAttributes(tagAnnotationClassCanonicalName);
            //获取当前方法中要xxxMapping注解的属性
            Map<String, Object> mappingAttr = getPathByMethod(annotatedMethod);
            if (mappingAttr == null){
                continue;
            }

            String[] childPath = (String[]) mappingAttr.get(VALUE);
            if (targetAttr == null || childPath == null || childPath.length == 0) {
                continue;
            }

            String path = pathParent + childPath[0];

            boolean isHas = resMap.containsKey(path);
            if (isHas){
                throw new Exception("重复定义了相同的映射关系");
            }

            resMap.put(path, targetAttr);
        }

        return resMap;
    }


    private static Map<String, Object> getPathByMethod(MethodMetadata annotatedMethod) {
        Map<String, Object> annotationAttributes = annotatedMethod.getAnnotationAttributes(GetMapping.class.getCanonicalName());
        if (annotationAttributes != null && annotationAttributes.get(VALUE) != null) {
            return annotationAttributes;
        }
        annotationAttributes = annotatedMethod.getAnnotationAttributes(PostMapping.class.getCanonicalName());
        if (annotationAttributes != null && annotationAttributes.get(VALUE) != null) {
            return annotationAttributes;
        }

        annotationAttributes = annotatedMethod.getAnnotationAttributes(DeleteMapping.class.getCanonicalName());
        if (annotationAttributes != null && annotationAttributes.get(VALUE) != null) {
            return annotationAttributes;
        }

        annotationAttributes = annotatedMethod.getAnnotationAttributes(PutMapping.class.getCanonicalName());
        if (annotationAttributes != null && annotationAttributes.get(VALUE) != null) {
            return annotationAttributes;
        }
        annotationAttributes = annotatedMethod.getAnnotationAttributes(RequestMapping.class.getCanonicalName());
        return annotationAttributes;
    }

    /**
     * 从包package中获取所有的Class
     *
     * @param pkg
     * @return
     */
    public static Set<Class<?>> getClzFromPkg(String pkg) {
        //第一个class类的集合
        Set<Class<?>> classes = new LinkedHashSet<>();
        // 获取包的名字 并进行替换
        String pkgDirName = pkg.replace('.', '/');
        try {
            Enumeration<URL> urls = AuthUtils.class.getClassLoader().getResources(pkgDirName);
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                // 得到协议的名称
                String protocol = url.getProtocol();
                // 如果是以文件的形式保存在服务器上
                if ("file".equals(protocol)) {
                    // 获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    // 以文件的方式扫描整个包下的文件 并添加到集合中
                    findClassesByFile(pkg, filePath, classes);
                } else if ("jar".equals(protocol)) {
                    // 如果是jar包文件
                    // 获取jar
                    JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
                    //扫描jar包文件 并添加到集合中
                    findClassesByJar(pkg, jar, classes);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classes;
    }
    private static void findClassesByFile(String pkgName, String pkgPath, Set<Class<?>> classes) {
        // 获取此包的目录 建立一个File
        File dir = new File(pkgPath);
        // 如果不存在或者 也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        // 如果存在 就获取包下的所有文件 包括目录
//        File[] dirfiles = dir.listFiles(new FileFilter() {
//            // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
//            public boolean accept(File file) {
//                return file.isDirectory()|| file.getName().endsWith(".class");
//            }
//        });
        File[] dirfiles = dir.listFiles(pathname -> pathname.isDirectory() || pathname.getName().endsWith("class"));

        if (dirfiles == null || dirfiles.length == 0) {
            return;
        }

        String className;
        Class clz;
        // 循环所有文件
        for (File f : dirfiles) {
            // 如果是目录 则继续扫描
            if (f.isDirectory()) {
                findClassesByFile(pkgName + "." + f.getName(),pkgPath + "/" + f.getName(),classes);
                continue;
            }
            // 如果是java类文件 去掉后面的.class 只留下类名
            className = f.getName();
            className = className.substring(0, className.length() - 6);

            //加载类
            clz = loadClass(pkgName + "." + className);
            // 添加到集合中去
            if (clz != null) {
                classes.add(clz);
            }
        }
    }

    private static void findClassesByJar(String pkgName, JarFile jar, Set<Class<?>> classes) {
        String pkgDir = pkgName.replace(".", "/");
        // 从此jar包 得到一个枚举类
        Enumeration<JarEntry> entry = jar.entries();

        JarEntry jarEntry;
        String name, className;
        Class<?> claze;
        // 同样的进行循环迭代
        while (entry.hasMoreElements()) {
            // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文
            jarEntry = entry.nextElement();

            name = jarEntry.getName();
            // 如果是以/开头的
            if (name.charAt(0) == '/') {
                // 获取后面的字符串
                name = name.substring(1);
            }

            if (jarEntry.isDirectory() || !name.startsWith(pkgDir) || !name.endsWith(".class")) {
                continue;
            }
            //如果是一个.class文件 而且不是目录
            // 去掉后面的".class" 获取真正的类名
            className = name.substring(0, name.length() - 6);
            //加载类
            claze = loadClass(className.replace("/", "."));
            // 添加到集合中去
            if (claze != null) {
                classes.add(claze);
            }
        }
    }

    /**
     *    加载类
     * @param fullClzName 类全名
     * @return
     */
    private static Class<?> loadClass(String fullClzName) {
        try {
            return Thread.currentThread().getContextClassLoader().loadClass(fullClzName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
