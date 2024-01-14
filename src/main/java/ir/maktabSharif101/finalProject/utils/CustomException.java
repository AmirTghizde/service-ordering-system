package ir.maktabSharif101.finalProject.utils;

public class CustomException extends RuntimeException{
    public CustomException(String error,String description) {
        super("(×_×;）\n🏮ERROR: "+error+"\n📃DESC: "+description);
    }
}
