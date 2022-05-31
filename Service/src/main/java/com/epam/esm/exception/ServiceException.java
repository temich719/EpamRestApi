package com.epam.esm.exception;

import com.epam.esm.localization.Message;

import java.util.Locale;

public class ServiceException extends Exception{

    private final String messageKey;
    private final Locale locale;

    public ServiceException(String messageKey, Locale locale) {
        this.messageKey = messageKey;
        this.locale = locale;
    }

    public ServiceException(String messageKey){
        this(messageKey, Locale.getDefault());
    }

    public String getLocalizedMessage(){
        return Message.getMessageForLocale(messageKey, locale);
    }

}
