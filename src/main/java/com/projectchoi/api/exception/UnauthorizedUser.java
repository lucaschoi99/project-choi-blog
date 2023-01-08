package com.projectchoi.api.exception;

/**
 * StatusCode = 401
 */
public class UnauthorizedUser extends ProjectChoiException {

    private static final String MESSAGE = "인증이 필요합니다.";

    public UnauthorizedUser() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
