package models;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Model for Custom Date
 */
public class CustomDate extends Date {


    public String toStringInHours() {
        String pattern = "E-H";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String date = simpleDateFormat.format(this);
        date += ":00";
        return date;
    }

    public String toStringInDate() {
        String pattern = "E-d-MMM";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String date = simpleDateFormat.format(this);
        return date;

    }
}
