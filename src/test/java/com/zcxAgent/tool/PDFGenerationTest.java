package com.zcxAgent.tool;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PDFGenerationTest {

    @Test
    void generatePDF() {
        PDFGenerationTool pdfGeneration = new PDFGenerationTool();
        String result = pdfGeneration.generatePDF("test.pdf", "Hello World!");
        System.out.println(result);
    }
}