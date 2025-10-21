package com.scnuvem.sc.auth.exceptions;

public class UserEmailNotFoundException extends RuntimeException {
    public UserEmailNotFoundException(String message) {
        super(message);
    }

    public UserEmailNotFoundException() {
        super("Usuário com o e-mail informado não encontrado!");
    }

}
