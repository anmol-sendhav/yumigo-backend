package com.yumigo.constant;

public final class ValidationPatterns {

    private ValidationPatterns() {}

    public static final String PHONE = "^[6-9]\\d{9}$";

    public static final String PASSWORD =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,64}$";

    public static final String PASSWORD_MESSAGE =
            "Password must be 8-64 characters with an uppercase letter, a lowercase letter, a digit and a special character";
}