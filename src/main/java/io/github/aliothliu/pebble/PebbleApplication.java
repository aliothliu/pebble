package io.github.aliothliu.pebble;

import io.github.aliothliu.gabbro.EnableGabbro;
import io.github.aliothliu.marble.MarbleConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableGabbro
@Import(value = {
        MarbleConfiguration.class
})
public class PebbleApplication {

    public static void main(String[] args) {
        SpringApplication.run(PebbleApplication.class, args);
    }

}
