package com.codetree.CodeTreeHRM.util;

public class PasswordValidator {

    private static final int MIN_LENGTH = 8;

    public static void validate(String password) {
        if (password == null || password.length() < MIN_LENGTH) {
            throw new IllegalArgumentException("비밀번호는 8자 이상이어야 합니다.");
        }

        int categoryCount = 0;
        if (password.chars().anyMatch(Character::isUpperCase)) categoryCount++;
        if (password.chars().anyMatch(Character::isLowerCase)) categoryCount++;
        if (password.chars().anyMatch(Character::isDigit)) categoryCount++;
        if (password.chars().anyMatch(c -> !Character.isLetterOrDigit(c))) categoryCount++;

        if (categoryCount < 3) {
            throw new IllegalArgumentException("비밀번호는 영어 대문자, 영어 소문자, 숫자, 기호 중 3가지 이상을 포함해야 합니다.");
        }
    }
}