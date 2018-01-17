package basil.sanity.Util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Andre Takhmazyan on 10/27/2017.
 */

public class RandomDate {

    public static Date nextDateInLastMonth(){

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);

        long random = ThreadLocalRandom.current().nextLong(cal.getTimeInMillis(), System.currentTimeMillis());
        return new Date(random);
    }

    public static Date nextDateInMonth(){

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);

        long random = ThreadLocalRandom.current().nextLong( System.currentTimeMillis(), cal.getTimeInMillis());
        return new Date(random);
    }

    public static Date nextDate(Date end){
        long random = ThreadLocalRandom.current().nextLong( System.currentTimeMillis(), end.getTime());
        return new Date(random);
    }

    public static GregorianCalendar nextDate(int start, int end){
        GregorianCalendar gc = new GregorianCalendar();
        int year = randBetween(start, end);
        //System.out.println("Start: " + start);
        //System.out.println("End: " + end);
        //System.out.println("The year is: " + year);
        gc.set(gc.YEAR, year);
        int dayOfYear = randBetween(1, gc.getActualMaximum(gc.DAY_OF_YEAR));
        gc.set(gc.DAY_OF_YEAR, dayOfYear);
        return gc;
    }

    private static int randBetween(int start, int end) {
        return start + (int)Math.round(Math.random() * (end - start));
    }
}