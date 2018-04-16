package com.example.android.medmanagerapplication.helperUtilitiesClasses;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class CalculateDays {


    private static final String DATE_FORMAT = "d/M/yyyy";  //or use "M/d/yyyy"

    public static long getDaysBetweenDates(String start, String end) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        Date startDate, endDate;
        long numberOfDays = 0;
        try {
            startDate = dateFormat.parse(start);
            endDate = dateFormat.parse(end);
            numberOfDays = getUnitBetweenDates(startDate, endDate, TimeUnit.DAYS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return numberOfDays;
    }

    private static long getUnitBetweenDates(Date startDate, Date endDate, TimeUnit unit) {
        long timeDiff = endDate.getTime() - startDate.getTime();
        return unit.convert(timeDiff, TimeUnit.MILLISECONDS);
    }

    private static long getUnitBetweenDates(Date startDate, Date endDate) {
        long timeDiff = endDate.getTime() - startDate.getTime();
        return timeDiff;
    }

    public static boolean compareDate(String d1, String d2) throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

        Date date1 = format.parse(d1);
        Date date2 = format.parse(d2);

        if (date2.before(date1)) {
            return true;
        } else {
            return false;
        }



    }

    public static long dateInMillisconds(String date) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Date mDate = sdf.parse(date);
        long timeInMilliseconds = mDate.getTime();

        return timeInMilliseconds;
    }

    public static String timeInStringFormat(long time) {

        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(time);

        return date;

    }

    public static long dailyInterval(long drugInterval) {

        int result = (int) Math.ceil(24 / drugInterval);

        return TimeUnit.HOURS.toMillis(result);
    }
}
