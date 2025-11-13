package com.example.pengyuAiAgent.demo.invoke;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class HttpAiInvoke {
    
    private static final String API_URL = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation";
    
    @Value("${spring.ai.dashscope.api-key}")
    private String apiKey;
    
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
     * 从 application-local.yml 配置读取 API_KEY 进行调用
     */
    public String invoke() {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new RuntimeException("未找到 spring.ai.dashscope.api-key，请检查 application-local.yml 配置");
        }
        return invoke(apiKey);
    }
    
    /**
     * 测试方法（需要在 Spring 上下文中运行）
     */
    public static void main(String[] args) {
        // 使用 SpringApplication 来启动，以便正确加载配置文件
        org.springframework.boot.SpringApplication app = 
            new org.springframework.boot.SpringApplication(com.example.pengyuAiAgent.AiGentApplication.class);
        app.setWebApplicationType(org.springframework.boot.WebApplicationType.NONE);
        org.springframework.context.ConfigurableApplicationContext context = app.run(args);
        
        try {
            HttpAiInvoke httpAiInvoke = context.getBean(HttpAiInvoke.class);
            String response = httpAiInvoke.invoke();
            System.out.println("响应结果：");
            System.out.println(response);
        } finally {
            context.close();
        }
    }
}
