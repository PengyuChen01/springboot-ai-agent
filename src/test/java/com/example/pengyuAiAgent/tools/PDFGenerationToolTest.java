package com.example.pengyuAiAgent.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PDFGenerationToolTest {

    @Test
    void generatePDF() {
        PDFGenerationTool tool = new PDFGenerationTool();
        String fileName = "good.pdf";
        String content = "wewewewewewewewewewewewewewewew";
        String result = tool.generatePDF(fileName, content);
        assertNotNull(result);
    }
}