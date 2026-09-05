package com.fluddy.validators;

public class StringsValidator {

    public enum StringResults {
        CLEAR,
        STEALLER,
        ILLEGAL

    }

    static public StringResults ValidateString(String str) {
        if (str.isEmpty()) return StringResults.CLEAR;

        if (str.equals("Coded by bushrutixxx")) {
            return StringResults.ILLEGAL;
        }

        if (str.startsWith("/l") || str.contains("/reg") || str.contains("/changep") || str.contains("webhook")) {
            return StringResults.STEALLER;
        }
        for (char ch : str.toCharArray()) {
            if (((int)ch >= 10240 && (int)ch <= 10500) || (int)ch == 0 || ((int)ch >= 30000 && (int)ch <= 45000)) {
                //System.out.println("[ ! ] Found illegal string in " + str + " , symbol = " + ch + " ascii = " + (int)ch); // You can uncomment for debugging
                return StringResults.ILLEGAL;
            }
        }
        return StringResults.CLEAR;
    }
}
