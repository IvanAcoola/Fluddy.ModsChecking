package com.fluddy.validators;

public class ClassNameValidator {
    static public Boolean ValidateClassName(String className) {
        // System.out.println(className); // You can uncomment for debugging
        long count_l = className.chars().filter(ch -> ch == 'l').count();
        long count_i = className.chars().filter(ch -> ch == 'I').count();

        if (className.length() / 4 < count_i && className.length() / 4 < count_l) {
            return true; // Found illegal name
        }
        if (className.equals("mod_d") || className.equals("qProtect")) {
            return true; // detected protection
        }

        for (char ch : className.toCharArray()) {
            if ((int)ch <= 32 || (int)ch >= 127) {
                // System.out.println("[ ! ] Found illegal classname in " + className + " , symbol = " + ch + " ascii = " + (int)ch); // You can uncomment for debugging
                return true; // Found illegal symbol
            }
        }
        return false;
    }
}
