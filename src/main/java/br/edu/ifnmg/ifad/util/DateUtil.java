package br.edu.ifnmg.ifad.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author danilo
 */
public class DateUtil {

    public static Date truncate(Date data) {
        Calendar cal = Calendar.getInstance(); // locale-specific
        cal.setTime(data);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 1);
        return cal.getTime();
    }
    
    public static Date truncateEndDay(Date data){
        Calendar cal = Calendar.getInstance(); // locale-specific
        cal.setTime(data);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }
}
