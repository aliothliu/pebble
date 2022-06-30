package io.github.aliothliu.pebble.infrastructure.fm;

import org.springframework.lang.NonNull;

/**
 * 文件存储，提供统一的文件管理接口
 *
 * @author liubin
 */
public interface FileManager {

    /**
     * 存储一个私有文件对象
     *
     * @param meta 文件元数据
     * @throws FileStoreException 文件保存异常
     */
    @NonNull
    FileStub storeInPrivate(FileMeta meta);

    /**
     * 存储一个有文件对象
     *
     * @param meta 文件元数据
     * @throws FileStoreException 文件保存异常
     */
    @NonNull
    FileStub storeInPublic(FileMeta meta);

    /**
     * 通过文件key读取文件流
     *
     * @param key 文件key
     * @return 文件流
     * @throws FileLoadException 文件读取异常
     */
    @NonNull
    FileStub load(String key);

    /**
     * 通过文件key删除文件
     *
     * @param key 文件key
     * @throws FileRemoveException 文件删除异常
     */
    void remove(String key);

    /**
     * 通过文件key获取可以下载的文件URL
     *
     * @return 文件URL
     */
    String presigned(String key);
}
