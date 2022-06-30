package io.github.aliothliu.pebble.infrastructure.fm;

import java.io.InputStream;

public interface FileStub {

    String getKey();

    String getName();

    String getContentType();

    boolean isPrivate();

    long getSize();

    boolean isEmpty();

    InputStream getInputStream();
}
