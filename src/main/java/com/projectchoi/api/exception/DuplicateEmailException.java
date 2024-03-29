package com.projectchoi.api.exception;

public class DuplicateEmailException extends ProjectChoiException {

    private static final String MESSAGE = "이미 등록된 이메일입니다.";

    public DuplicateEmailException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
