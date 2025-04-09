package application.utils;

import java.nio.charset.StandardCharsets;

public class TextUtils {

    public TextUtils(){}

    public String setUtf8(String text){
        return new String(text.getBytes(), StandardCharsets.UTF_8);
    }
}
