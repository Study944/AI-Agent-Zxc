package com.zcxAgent.tool;

import cn.hutool.core.io.FileUtil;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

/**
 * PDF生成工具
 */
public class PDFGenerationTool {

    static final String FILE_PATH = System.getProperty("user.dir")+"/tmp";

    @Tool(description = "PDF生成工具")
    public String generatePDF(@ToolParam(description = "生成PDF文件的名称") String fileName,
                              @ToolParam(description = "PDF的内容") String content) {
        String fileDir = FILE_PATH + "/pdf";
        String filePath = fileDir + "/" + fileName;

        try {
            FileUtil.mkdir(fileDir);
            System.out.println("文件将被保存至：" + filePath);

            // 创建 PDF 文档
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // 设置默认字体测试
            PdfFont font = PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H");
            document.setFont(font);

            // 添加内容
            Paragraph paragraph = new Paragraph(content);
            document.add(paragraph);

            // 关闭文档，确保内容写入
            document.close();

            return "PDF文件生成成功：" + filePath;
        } catch (Exception e) {
            return "PDF文件生成失败：" + e.getMessage();
        }
    }


}
