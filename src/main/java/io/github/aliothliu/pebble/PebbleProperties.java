package io.github.aliothliu.pebble;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Alioth Liu
 **/
@Configuration
@ConfigurationProperties(prefix = "pebble")
@Data
public class PebbleProperties {

}
