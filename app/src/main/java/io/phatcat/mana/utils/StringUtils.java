package io.phatcat.mana.utils;

public class StringUtils {

    // Disallow instantiation
    private StringUtils() {}

    public static CharSequence getFirstLetterOrBlank(String input) {
        if (isNotBlank(input)) {
            return input.substring(0,1);
        }
        else {
            return "";
        }
    }

    public static boolean isNotBlank(CharSequence string) {
        if (string == null) {
            return false;
        }
        for (int i = 0, n = string.length(); i < n; i++) {
            if (!Character.isWhitespace(string.charAt(i))) {
                return true;
            }
        }
        return false;
    }
}
