package com.scnuvem.sc.auth.exceptions;

public class ExpiredVerificationCodeException extends RuntimeException {

    public ExpiredVerificationCodeException() {
        super("O código de verificação expirou. Por favor, solicite um novo código.");
    }

    public ExpiredVerificationCodeException(String message) {
        super(message);
    }

}
