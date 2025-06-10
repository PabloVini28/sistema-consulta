package com.scnuvem.sc.auth.exceptions;

public class InvalidVerificationCodeException extends RuntimeException {
    public InvalidVerificationCodeException(String message) {
        super(message);
    }

    public InvalidVerificationCodeException() {
        super("Código de verificação inválido!");
    }

}
