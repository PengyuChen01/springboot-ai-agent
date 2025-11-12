package com.example.pengyuAiAgent.demo.invoke;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpAiInvoke {
    
    private static final String API_URL = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation";
    
    /**
     * 调用阿里云通义千问API
     * @param apiKey API密钥
     * @return 响应结果
     */
    public static String invoke(String apiKey) {
        // 构建请求体
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "qwen-plus");
        
        // 构建input部分
        Map<String, Object> input = new HashMap<>();
        List<Map<String, String>> messages = new ArrayList<>();
        
        // 添加system消息
        Map<String, String> systemMsg = new HashMap<>();
        systemMsg.put("role", "system");
        systemMsg.put("content", "You are a helpful assistant.");
        messages.add(systemMsg);
        
        // 添加user消息
        Map<String, String> userMsg = new HashMap<>();
        userMsg.put("role", "user");
        userMsg.put("content", "我是pengyu");
        messages.add(userMsg);
        
        input.put("messages", messages);
        requestBody.put("input", input);
        
        // 构建parameters部分
        Map<String, String> parameters = new HashMap<>();
        parameters.put("result_format", "message");
        requestBody.put("parameters", parameters);
        
        // 发送POST请求
        String response = HttpRequest.post(API_URL)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(JSONUtil.toJsonStr(requestBody))
                .execute()
                .body();
        
        return response;
    }
    
    /**
     * 从 .env 文件读取 API_KEY 进行调用
     */
    public static String invoke() {
        String apiKey = EnvUtil.getDashScopeApiKey();
        if (apiKey == null || apiKey.isEmpty()) {
            throw new RuntimeException("未找到 DASHSCOPE_API_KEY，请检查 .env 文件配置");
        }
        return invoke(apiKey);
    }
    
    /**
     * 使用TestApiKey中的API_KEY进行调用（备用方法）
     */
    public static String invokeWithTestKey() {
        return invoke(TestApiKey.API_KEY);
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        String response = invoke();
        System.out.println("响应结果：");
        System.out.println(response);
    }
}
