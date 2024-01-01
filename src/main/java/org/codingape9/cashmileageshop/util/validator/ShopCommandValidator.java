package org.codingape9.cashmileageshop.util.validator;

public class ShopCommandValidator {
    public static boolean hasValidSubCommandLength(String[] subCommand, int expectedLength) {
        return subCommand.length == expectedLength;
    }
}
