package com.projectchoi.api.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * {
 *     "code": "400",
 *     "message": "Bad Request!",
 *     "validation": {
 *         "title": "must not be blank!"
 *     }
 * }
 */
@Getter
@RequiredArgsConstructor
public class ErrorResponse {

    private final String code;
    private final String message;
    private Map<String, String> validation = new HashMap<>();

    public void addValidation(String field, String defaultMessage) {
        this.validation.put(field, defaultMessage);
    }

}
