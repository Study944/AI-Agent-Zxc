package com.zcxAgent.tool;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileOperationToolTest {

    @Test
    void readFile() {
        String  fileName = "test.txt";
        String result = new FileOperationTool().ReadFile(fileName);
        System.out.println(result);
    }

    @Test
    void writeFile() {
        String fileName = "test.txt";
        String content = "hello world";
        String result = new FileOperationTool().WriteFile(fileName,content);
        System.out.println(result);
    }
}