package ir.maktabSharif101.finalProject.utils;

import java.util.regex.Pattern;

public class Validation {

    public static boolean isValidPassword(String password) {
        return validateByPattern(password, "^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]+$");
    }
    public static boolean isValidEmail(String email){
        return validateByPattern(email, "^[A-Za-z0-9+_.-]+@(?:gmail\\\\.com|mail\\\\.com)$");
    }
    public static boolean isValidName(String name){
        return validateByPattern(name,"^[a-zA-Z]*$");
    }
    public static boolean isValidDate(String name){
        return validateByPattern(name,"\\d{4}-\\d{2}-\\d{2}");
    }
    public static boolean isValidTime(String name){
        return validateByPattern(name,"^([01]\\d|2[0-3]):([0-5]\\d)$");
    }

    private static boolean validateByPattern(String input, String pattern) {
        return Pattern.compile(pattern).matcher(input).matches();
    }


}
