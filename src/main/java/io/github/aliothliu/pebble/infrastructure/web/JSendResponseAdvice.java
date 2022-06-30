package io.github.aliothliu.pebble.infrastructure.web;

import io.github.aliothliu.gabbro.GabbroResponseAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Alioth Liu
 **/
@RestControllerAdvice(basePackages = "io.github.aliothliu")
public class JSendResponseAdvice extends GabbroResponseAdvice {
}
