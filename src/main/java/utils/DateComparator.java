package utils;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;

public class DateComparator implements Comparator<Date> {
    public int compare(Date p, Date q) {
        if (p.before(q)) {
            return -1;
        } else if (p.after(q)) {
            return 1;
        } else {
            return 0;
        }
    }
}