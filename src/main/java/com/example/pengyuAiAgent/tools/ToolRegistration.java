package com.example.pengyuAiAgent.tools;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolRegistration {

    @Bean
    public FileOperationTool fileOperationTool() {
        return new FileOperationTool();
    }
    @Bean
    public PDFGenerationTool pdfGenerationTool() {
        return new PDFGenerationTool();
    }
    @Bean
    public WebScrapingTool webScrapingTool() {
        return new WebScrapingTool();
    }

    @Bean
    public TerminalOperationTool terminalOperationTool() {
        return new TerminalOperationTool();
    }

    @Bean
    public ResourceDownloadTool resourceDownloadTool() {
        return new ResourceDownloadTool();
    }

}
