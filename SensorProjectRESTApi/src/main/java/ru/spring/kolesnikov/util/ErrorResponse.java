package ru.spring.kolesnikov.util;

import javax.swing.text.DateFormatter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ErrorResponse {
    private String message;
    //private LocalDateTime localDateTime;
    public String dateTime;
    private DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public ErrorResponse(String message, Date date) {
        this.message = message;
        //this.localDateTime = localDateTime;

        this.dateTime = formatter.format(date);

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

//    public LocalDateTime getLocalDateTime() {
//        return localDateTime;
//    }
//
//    public void setLocalDateTime(LocalDateTime localDateTime) {
//        this.localDateTime = localDateTime;
//    }


    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
