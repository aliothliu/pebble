package io.github.aliothliu.pebble.domain;

import java.time.LocalDateTime;

/**
 * AuditableEntity
 *
 * @author liubin
 **/
public interface Auditable {

    String getCreatedBy();

    LocalDateTime getCreatedDate();

    String getLastModifiedBy();

    LocalDateTime getLastModifiedDate();
}
