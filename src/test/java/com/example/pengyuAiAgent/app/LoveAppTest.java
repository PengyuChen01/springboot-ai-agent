package com.example.pengyuAiAgent.app;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;

import java.util.UUID;

@Slf4j
@SpringBootTest
class LoveAppTest {
    @Resource
    private LoveApp loveApp;

    @Test
    void testChat() {
        String chatId = UUID.randomUUID().toString();
        // first round
        String message = "您好，我是pengyu";
        String answer = loveApp.doChat(chatId, message);
        // second round
        message = "您好，我想让xxx 更爱我";
        answer = loveApp.doChat(chatId, message);
        Assertions.assertNotNull(answer);
        // third round
        message = "我的另一半叫什么？刚刚跟你说过 帮我回忆一下";
        answer = loveApp.doChat(chatId, message);
        Assertions.assertNotNull(answer);
    }
}