package com.example.pengyuAiAgent.demo.invoke;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 环境变量工具类，用于读取 .env 文件
 */
public class EnvUtil {
    
    private static final String ENV_FILE = ".env";
    private static Map<String, String> envCache = null;
    
    /**
     * 读取 .env 文件并缓存结果
     */
    private static Map<String, String> loadEnv() {
        if (envCache != null) {
            return envCache;
        }
        
        envCache = new HashMap<>();
        
        // 尝试从多个位置查找 .env 文件
        File envFile = findEnvFile();
        
        if (envFile == null || !envFile.exists()) {
            System.err.println("警告: .env 文件不存在，请创建 .env 文件并配置 DASHSCOPE_API_KEY");
            System.err.println("提示: 可以在项目根目录创建 .env 文件，格式: DASHSCOPE_API_KEY=your-api-key");
            // 尝试从系统环境变量读取
            String systemEnvKey = System.getenv("DASHSCOPE_API_KEY");
            if (systemEnvKey != null && !systemEnvKey.isEmpty()) {
                envCache.put("DASHSCOPE_API_KEY", systemEnvKey);
                System.out.println("已从系统环境变量读取 DASHSCOPE_API_KEY");
            }
            return envCache;
        }
        
        try {
            List<String> lines = FileUtil.readUtf8Lines(envFile);
            for (String line : lines) {
                // 跳过空行和注释
                if (StrUtil.isBlank(line) || line.trim().startsWith("#")) {
                    continue;
                }
                
                // 解析 KEY=VALUE 格式
                int equalsIndex = line.indexOf('=');
                if (equalsIndex > 0) {
                    String key = line.substring(0, equalsIndex).trim();
                    String value = line.substring(equalsIndex + 1).trim();
                    // 移除引号（如果有）
                    if ((value.startsWith("\"") && value.endsWith("\"")) ||
                        (value.startsWith("'") && value.endsWith("'"))) {
                        value = value.substring(1, value.length() - 1);
                    }
                    envCache.put(key, value);
                }
            }
        } catch (Exception e) {
            System.err.println("读取 .env 文件失败: " + e.getMessage());
        }
        
        return envCache;
    }
    
    /**
     * 获取环境变量值
     * @param key 环境变量键名
     * @return 环境变量值，如果不存在返回 null
     */
    public static String get(String key) {
        Map<String, String> env = loadEnv();
        return env.get(key);
    }
    
    /**
     * 获取环境变量值，如果不存在则返回默认值
     * @param key 环境变量键名
     * @param defaultValue 默认值
     * @return 环境变量值或默认值
     */
    public static String get(String key, String defaultValue) {
        String value = get(key);
        return value != null ? value : defaultValue;
    }
    
    /**
     * 查找 .env 文件，按优先级顺序查找：
     * 1. 当前工作目录
     * 2. 项目根目录（通过类路径推断）
     * 3. 用户主目录
     */
    private static File findEnvFile() {
        // 1. 尝试当前工作目录
        File envFile = new File(ENV_FILE);
        String currentDir = System.getProperty("user.dir");
        System.out.println("当前工作目录: " + currentDir);
        if (envFile.exists()) {
            System.out.println("找到 .env 文件: " + envFile.getAbsolutePath());
            return envFile;
        }
        
        // 2. 尝试项目根目录（通过类路径推断）
        File projectRootEnvFile = null;
        try {
            // 获取类路径中的资源路径
            java.net.URL resourceUrl = EnvUtil.class.getResource("/");
            if (resourceUrl != null) {
                String classPath = resourceUrl.getPath();
                // 处理 Windows 路径和 URL 编码
                if (classPath.startsWith("/") && classPath.contains(":")) {
                    classPath = classPath.substring(1);
                }
                // 处理 URL 编码（如 %20 -> 空格）
                try {
                    classPath = java.net.URLDecoder.decode(classPath, "UTF-8");
                } catch (Exception e) {
                    // 忽略解码异常
                }
                
                System.out.println("类路径: " + classPath);
                
                // 从 target/classes 或 out/production 向上找到项目根目录
                File projectRoot = new File(classPath);
                while (projectRoot != null && projectRoot.exists()) {
                    String name = projectRoot.getName();
                    if (name.equals("target") || name.equals("out") || name.equals("build") || 
                        name.equals("classes") || name.equals("production")) {
                        projectRoot = projectRoot.getParentFile();
                        break;
                    }
                    File parent = projectRoot.getParentFile();
                    if (parent == null || !parent.exists()) {
                        break;
                    }
                    projectRoot = parent;
                }
                
                if (projectRoot != null && projectRoot.exists()) {
                    System.out.println("推断的项目根目录: " + projectRoot.getAbsolutePath());
                    projectRootEnvFile = new File(projectRoot, ENV_FILE);
                    if (projectRootEnvFile.exists()) {
                        System.out.println("找到 .env 文件: " + projectRootEnvFile.getAbsolutePath());
                        return projectRootEnvFile;
                    } else {
                        System.out.println(".env 文件不存在于: " + projectRootEnvFile.getAbsolutePath());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("查找项目根目录时出错: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 3. 尝试用户主目录
        String userHome = System.getProperty("user.home");
        if (userHome != null) {
            File userHomeEnvFile = new File(userHome, ENV_FILE);
            if (userHomeEnvFile.exists()) {
                System.out.println("找到 .env 文件: " + userHomeEnvFile.getAbsolutePath());
                return userHomeEnvFile;
            }
        }
        
        System.err.println("未找到 .env 文件，已尝试的位置:");
        System.err.println("  1. 当前工作目录: " + envFile.getAbsolutePath());
        if (projectRootEnvFile != null) {
            System.err.println("  2. 项目根目录: " + projectRootEnvFile.getAbsolutePath());
        } else {
            System.err.println("  2. 项目根目录: 无法确定");
        }
        if (userHome != null) {
            System.err.println("  3. 用户主目录: " + new File(userHome, ENV_FILE).getAbsolutePath());
        }
        
        return null;
    }
    
    /**
     * 获取 DASHSCOPE_API_KEY
     * @return API Key
     */
    public static String getDashScopeApiKey() {
        // 优先从 .env 文件读取
        String apiKey = get("DASHSCOPE_API_KEY");
        // 如果 .env 中没有，尝试从系统环境变量读取
        if (apiKey == null || apiKey.isEmpty()) {
            apiKey = System.getenv("DASHSCOPE_API_KEY");
        }
        return apiKey;
    }
}


