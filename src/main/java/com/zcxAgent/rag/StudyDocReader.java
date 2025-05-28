package com.zcxAgent.rag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * md文档读取
 */
@Component
@Slf4j
public class StudyDocReader {

    private List<Resource> resourceList;

    public StudyDocReader(@Value("classpath:studydoc/*.md") List<Resource> resources) {
        this.resourceList = resources;
    }

    List<Document> loadMarkdown(){

        List<Document> documents = new ArrayList<>();
        for (Resource resource : resourceList) {
            String filename = resource.getFilename();
            String status = filename.substring(0, filename.indexOf(".")-1);
            MarkdownDocumentReaderConfig readerConfig = MarkdownDocumentReaderConfig.builder()
                    .withHorizontalRuleCreateDocument(true)
                    .withIncludeCodeBlock(false)
                    .withIncludeBlockquote(false)
                    .withAdditionalMetadata("filename", filename)
                    .withAdditionalMetadata("status", status)
                    .build();
            MarkdownDocumentReader reader = new MarkdownDocumentReader(resource,readerConfig);
            documents.addAll(reader.get());
        }
        return documents;
    }

}
