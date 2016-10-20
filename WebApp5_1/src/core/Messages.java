package core;

import java.util.ResourceBundle;

import static java.util.ResourceBundle.getBundle;

public class Messages {
    public static String getMessage(String lang, String key) {
        String path = "core/messages/";
        ResourceBundle bundle = ("ru".equals(lang)) ? getBundle(path + "msg_ru") :
                getBundle(path + "msg_en");
        return bundle.getString(key);
    }
}