package io.github.aliothliu.pebble.infrastructure.fm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Slf4j
public class MultipartFileMeta implements FileMeta {

    private final MultipartFile multipartFile;

    private final String dictionary;

    private final String key;

    public MultipartFileMeta(MultipartFile multipartFile) {
        this(multipartFile, null);
    }

    /**
     * 基于MultipartFile构建文件上传基本参数对象
     * <p>
     * 注意：会采取时间戳重命名文件key
     *
     * @param multipartFile 文件对象
     * @param dictionary    文件目录
     */
    public MultipartFileMeta(MultipartFile multipartFile, String dictionary) {
        Assert.notNull(multipartFile, "MultipartFile should not be null");
        Assert.isTrue(!multipartFile.isEmpty(), "MultipartFile should not be null");
        this.multipartFile = multipartFile;
        this.dictionary = dictionary;
        this.key = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")) + this.getFileExtension();
    }

    private String getFileExtension() {
        String filename = this.getName();
        if (Objects.isNull(filename)) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public String getName() {
        String fileName = multipartFile.getOriginalFilename();
        if (Objects.isNull(fileName)) {
            return null;
        }
        return new String(multipartFile.getOriginalFilename().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    @Override
    public String getDictionary() {
        return this.dictionary;
    }

    @Override
    public String getContentType() {
        return multipartFile.getContentType();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return this.multipartFile.getInputStream();
    }

    @Override
    public long size() {
        try {
            return multipartFile.getBytes().length;
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return 0;
    }
}
