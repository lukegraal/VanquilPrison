package com.vanquil.prison.tools.util;

public class RomanNumeral {
    public static String toRomanNumeral(final int i) {
        StringBuilder builder = new StringBuilder();
        int j = i;
        while (j != 0) {
            if (j >= 1000) {
                builder.append("M"); j -= 1000;
            } else if (j >= 500) {
                builder.append("D"); j -= 500;
            } else if (j >= 400) {
                builder.append("CD"); j -= 400;
            } else if (j >= 100) {
                builder.append("C"); j -= 100;
            } else if (j >= 50) {
                builder.append("L"); j -= 50;
            } else if (j >= 40) {
                builder.append("XL"); j -= 40;
            } else if (j >= 10) {
                builder.append("X"); j -= 10;
            } else if (j >= 9) {
                builder.append("IX"); j -= 9;
            } else if (j >= 5) {
                builder.append("V"); j -= 5;
            } else if (j >= 4) {
                builder.append("IV"); j -= 4;
            } else if (j >= 1) {
                builder.append("I"); j -= 1;
            }
        }
        return builder.toString();
    }
}
