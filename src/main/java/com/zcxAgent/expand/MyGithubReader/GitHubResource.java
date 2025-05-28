//package com.zcxAgent.expand.MyGithubReader;
//
//import org.kohsuke.github.GHContent;
//import org.kohsuke.github.GHRepository;
//import org.kohsuke.github.GitHub;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.core.io.Resource;
//import org.springframework.util.Assert;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.URI;
//import java.net.URL;
//
///**
// * 自定义GithubResource资源
// */
//public class GitHubResource implements Resource {
//    // 日志
//    private static final Logger logger = LoggerFactory.getLogger(GitHubResource.class);
//    // 输入流
//    private final InputStream inputStream;
//    // Github内容
//    private final GHContent content;
//
//    GitHubResource(GitHub gitHub, String branch, String path, String owner, String repo){
//        if (branch==null){
//            branch="main";
//        }
//        try {
//            GHRepository repository = gitHub.getRepository(String.format("%s/%s", owner, repo));
//            content = repository.getFileContent(path, branch);
//            Assert.isTrue(content.isFile(),"Github内容不是文件");
//            inputStream = content.read();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public GHContent getContent() {
//        return content;
//    }
//
//    /**
//     * 判断资源是否存在
//     */
//    @Override
//    public boolean exists() {
//        return false;
//    }
//
//    /**
//     * 获取资源的URL
//     */
//    @Override
//    public URL getURL() throws IOException {
//        return null;
//    }
//    /**
//     * 获取资源的URI
//     */
//    @Override
//    public URI getURI() throws IOException {
//        return null;
//    }
//    /**
//     * 获取资源的文件
//     */
//    @Override
//    public File getFile() throws IOException {
//        return null;
//    }
//    /**
//     * 获取资源的内容长度
//     */
//    @Override
//    public long contentLength() throws IOException {
//        return 0;
//    }
//    /**
//     * 获取资源的最后修改时间
//     */
//    @Override
//    public long lastModified() throws IOException {
//        return 0;
//    }
//    /**
//     * 获取资源的相对路径
//     */
//    @Override
//    public Resource createRelative(String relativePath) throws IOException {
//        return null;
//    }
//    /**
//     * 获取资源的文件名
//     */
//    @Override
//    public String getFilename() {
//        return "";
//    }
//    /**
//     * 获取资源的描述信息
//     */
//    @Override
//    public String getDescription() {
//        return "";
//    }
//    /**
//     * 获取资源的输入流
//     */
//    @Override
//    public InputStream getInputStream() throws IOException {
//        return null;
//    }
//}
