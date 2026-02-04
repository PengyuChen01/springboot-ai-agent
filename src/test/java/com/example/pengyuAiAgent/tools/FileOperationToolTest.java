package com.example.pengyuAiAgent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class FileOperationToolTest {

    @Test
    void readFile() {
        FileOperationTool fileOperationTool = new FileOperationTool();
        String filename = "xxx.txt";
        String result = fileOperationTool.readFile(filename);
        Assertions.assertNotNull(result);
    }

    @Test
    void writeFile() {
        FileOperationTool fileOperationTool = new FileOperationTool();
        String filename = "yyy.txt";
        String content =  "..wewewe.we.we.we.we";
        String result = fileOperationTool.writeFile(filename, content);
        Assertions.assertNotNull(result);
    }
}