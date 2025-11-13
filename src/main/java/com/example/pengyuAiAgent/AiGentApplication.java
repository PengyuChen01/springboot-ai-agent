package com.example.pengyuAiAgent;

import com.example.pengyuAiAgent.demo.invoke.TestApiKey;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AiGentApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiGentApplication.class, args);

    }

}
