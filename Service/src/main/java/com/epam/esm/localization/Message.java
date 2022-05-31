package com.epam.esm.localization;

import java.util.Locale;
import java.util.ResourceBundle;

public class Message {
    public static String getMessageForLocale(String messageKey, Locale locale) {
        return ResourceBundle.getBundle("messages", locale)
                .getString(messageKey);
    }
}
