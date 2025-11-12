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
        File envFile = new File(ENV_FILE);
        
        if (!envFile.exists()) {
            System.err.println("警告: .env 文件不存在，请创建 .env 文件并配置 DASHSCOPE_API_KEY");
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
     * 获取 DASHSCOPE_API_KEY
     * @return API Key
     */
    public static String getDashScopeApiKey() {
        return get("DASHSCOPE_API_KEY");
    }
}

