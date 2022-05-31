package com.epam.esm.dateiniso;

import com.epam.esm.stringsstorage.RepositoryStringsStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;

@Component
public class DateGenerator {

    private final ApplicationContext applicationContext;
    private final TimeZone timeZone;
    private final DateFormat dateFormat;

    @Autowired
    public DateGenerator(ApplicationContext applicationContext, TimeZone timeZone, DateFormat dateFormat) {
        this.applicationContext = applicationContext;
        this.timeZone = timeZone;
        this.dateFormat = dateFormat;
    }

    public String getCurrentDateAsISO() {
        Date date = (Date) applicationContext.getBean(RepositoryStringsStorage.DATE);
        dateFormat.setTimeZone(timeZone);
        return dateFormat.format(date);
    }

}
