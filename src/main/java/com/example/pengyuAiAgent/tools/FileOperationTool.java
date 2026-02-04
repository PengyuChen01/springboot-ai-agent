package com.example.pengyuAiAgent.tools;

import cn.hutool.core.io.FileUtil;
import com.example.pengyuAiAgent.constant.FileConstant;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

/**
 *  File operation class (support read and write)
 */
public class FileOperationTool {
    private final String FILE_DIR = FileConstant.FILE_SAVE_DIR + "/file";

    @Tool(description = "Read content from a file")
    public String readFile(@ToolParam(description =  "Name of a file to read") String fileName ) {
        String filePath = FileConstant.FILE_SAVE_DIR + "/" + fileName;
        try {
            return FileUtil.readUtf8String(filePath);
        }
        catch (Exception e) {
            return "Error reading file" + e.getMessage();
        }

    }
    @Tool(description = "Write content to a fileName")
    public String writeFile(@ToolParam(description =  "Name of a file to write") String fileName, @ToolParam(description =  "content write to a file")String content) {
        String filePath = FileConstant.FILE_SAVE_DIR + "/" + fileName;
        try {
            FileUtil.mkdir(filePath);
            FileUtil.writeUtf8String(content, filePath);
            return "File write successfully" +filePath;
        } catch (Exception e) {
            return "Error writing to file" + e.getMessage();
        }
    }
}
