package com.Maktab101.SpringProject.utils;

public class CustomException extends RuntimeException{
    public CustomException(String error,String description) {
        super("(×_×;）\n❗ERROR: "+error+"\n📃DESC:\n"+description);
    }
}
