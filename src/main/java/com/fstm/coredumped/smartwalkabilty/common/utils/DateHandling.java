package com.fstm.coredumped.smartwalkabilty.common.utils;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateHandling {
    static SimpleDateFormat DateOnlyFormatter = new SimpleDateFormat("yyyy-MM-dd");
    static SimpleDateFormat SimpleFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    static  SimpleDateFormat TimeFormater = new SimpleDateFormat("hh:mm:ss");

    private  DateHandling(){

    }

    public static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static Time getCurrentTime()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, 0);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.YEAR, 1970);

        return new Time(calendar.getTimeInMillis());
    }

    public static boolean equalsByDateOnly(Date date1,Date date2)
    {
        Calendar calendar1 = getCalenderOfDateWithoutTime(date1);
        Calendar calendar2 = getCalenderOfDateWithoutTime(date2);

        return  calendar1.equals(calendar2);
    }

    public static boolean afterByDateOnly(Date date1,Date date2)
    {
        Calendar calendar1 = getCalenderOfDateWithoutTime(date1);
        Calendar calendar2 = getCalenderOfDateWithoutTime(date2);

        return  calendar1.after(calendar2);
    }

    public static Calendar getCalenderOfDateWithoutTime(Date date2) {
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        calendar2.set(Calendar.HOUR_OF_DAY, 0);
        calendar2.set(Calendar.MINUTE, 0);
        calendar2.set(Calendar.SECOND, 0);
        calendar2.set(Calendar.MILLISECOND, 0);

        return calendar2;
    }

    public static boolean timeIsAfterTime(Time time , Time time2){
        LocalTime timeOne = LocalTime.parse(time.toString(), DateTimeFormatter.ofPattern("HH:mm:ss"));
        LocalTime timeTwo = LocalTime.parse(time2.toString(), DateTimeFormatter.ofPattern("HH:mm:ss"));
        return timeOne.isAfter(timeTwo);
    }

    public static String formatToNormalDateOnly(Date date)
    {
        return DateOnlyFormatter.format(date);
    }

    public static String formatToNormalDate(Date date)
    {
        return SimpleFormatter.format(date);
    }

    public static String formatToNormalTimeOnly(Date date)
    {
        return TimeFormater.format(date);
    }
}
