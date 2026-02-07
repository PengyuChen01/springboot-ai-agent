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


    @Test
    void doChatWithReport() {
        String chatId = UUID.randomUUID().toString();
        String message = "您好，我是pengyu, 我想让另一半更爱我，但我不知道怎么做";
        LoveApp.LoveReport loveReport = loveApp.doChatWithReport(message, chatId);
        Assertions.assertNotNull(loveReport);
    }

    @Test
    void doChatWithRag() {
        String chatId = UUID.randomUUID().toString();
        String message = "我已经结婚了，但是婚后关系不太亲密，怎么办？";
        String answer = loveApp.doChatWithRag(message, chatId);
        Assertions.assertNotNull(answer);
    }
    /**
     * 测试AI调用WebSearchTool
     * message故意引导AI去搜索
     */
    @Test
    void testChatWithWebSearch() {
        String chatId = UUID.randomUUID().toString();
        String message = "帮我搜索一下2024年最新的恋爱沟通技巧有哪些";
        String answer = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        log.info("WebSearch answer: {}", answer);
    }

    /**
     * 测试AI调用FileOperationTool（写文件）
     */
    @Test
    void testChatWithFileWrite() {
        String chatId = UUID.randomUUID().toString();
        String message = "帮我把以下内容写到一个叫love_tips.txt的文件里：恋爱三大原则：尊重、沟通、信任";
        String answer = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        log.info("FileWrite answer: {}", answer);
    }

    /**
     * 测试AI调用FileOperationTool（读文件）
     */
    @Test
    void testChatWithFileRead() {
        String chatId = UUID.randomUUID().toString();
        String message = "帮我读取love_tips.txt文件的内容";
        String answer = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        log.info("FileRead answer: {}", answer);
    }

    /**
     * 测试AI调用WebScrapingTool
     */
    @Test
    void testChatWithWebScraping() {
        String chatId = UUID.randomUUID().toString();
        String message = "帮我抓取这个网页的内容：https://www.zhihu.com/question/275359100";
        String answer = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        log.info("WebScraping answer: {}", answer);
    }

    /**
     * 测试AI调用ResourceDownloadTool
     */
    @Test
    void testChatWithResourceDownload() {
        String chatId = UUID.randomUUID().toString();
        String message = "帮我下载这张图片并保存为test.png：https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png";
        String answer = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        log.info("ResourceDownload answer: {}", answer);
    }

    /**
     * 测试AI调用多个Tool的组合场景
     * AI可能先搜索，再写文件
     */
    @Test
    void testChatWithMultipleTools() {
        String chatId = UUID.randomUUID().toString();
        String message = "帮我搜索一下恋爱中如何处理冷战，然后把搜索到的建议写到一个叫cold_war_tips.txt的文件里";
        String answer = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        log.info("MultipleTools answer: {}", answer);
    }

    @Test
    void testChatWithMcp() {
        String chatId = UUID.randomUUID().toString();
        // 地图/地理相关的问题，触发AI调用高德MCP工具
        String message = "帮我查一下从北京天安门到上海外滩的驾车路线";
        String answer = loveApp.doChatWithMcp(message, chatId);
        Assertions.assertNotNull(answer);
        log.info("MCP answer: {}", answer);
    }
}