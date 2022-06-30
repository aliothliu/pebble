package io.github.aliothliu.pebble.infrastructure.fm;

import java.io.IOException;
import java.io.InputStream;

public interface FileMeta {

    String getKey();

    String getName();

    String getDictionary();

    String getContentType();

    InputStream getInputStream() throws IOException;

    long size();
}