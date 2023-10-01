package com.jarek4313.auth.entity;

public enum Code {
    SUCCESS("Operacja zakończona sukcesem"),
    PERMIT("Przyznano dostęp"),
    USER_IS_NOT_ACTIVATE("Podany użytkownik o danej nazwie nie istnieje lub nie aktywował konta"),
    USER_INVALID_CREDENTIAL("Podane dane są nieprawidłowe"),
    INVALID_TOKEN("Wskazany tokane jest pusty lub nie ważny"),
    USER_ALREADY_EXISTS_LOGIN("Użytkownik o podanym loginie już istnieje"),
    USER_ALREADY_EXISTS_MAIL("Użytkownik o podanym mailu już istnieje");

    public final String label;
    private Code(String label) {
        this.label = label;
    }
}
