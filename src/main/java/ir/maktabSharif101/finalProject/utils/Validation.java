package ir.maktabSharif101.finalProject.utils;

import java.util.regex.Pattern;

public class Validation {

    public static boolean isValidPassword(String password) {
        return validateByPattern(password, "^(?=.*[a-z])(?=.*[A-Z])(?=.*[&%$#@])(?=.*[0-9]).{8,}$");
    }

    public static boolean isValidEmail(String email){
        return validateByPattern(email, "^[A-Za-z0-9+_.-]+@gmail.com$");
    }

    public static boolean isValidName(String name){
        return validateByPattern(name,"^[a-zA-Z]*$");
    }

    private static boolean validateByPattern(String input, String pattern) {
        return Pattern.compile(pattern).matcher(input).matches();
    }


}
