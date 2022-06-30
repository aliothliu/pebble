package io.github.aliothliu.pebble.infrastructure.errors;

import io.github.aliothliu.marble.domain.ValidationException;

public class NonUniquenessException extends ValidationException {

    public NonUniquenessException(String message) {
        super(message);
    }
}
