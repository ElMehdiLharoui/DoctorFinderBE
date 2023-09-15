package com.fstm.coredumped.smartwalkabilty.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeCalcules {

    public static int stringHourToMinutes(String heureDebut) {
        String[] parts = heureDebut.split(":");
        return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
    }

    public static String minutesToStringHour(int minutes)
    {
        int hours = minutes / 60;
        minutes = minutes - hours * 60;
        return new StringBuilder().append(hours).append(":").append(minutes).toString();
    }
    public static int getCurrentTimeInMinutes(){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String formattedTime = dateFormat.format(date);
        return stringHourToMinutes(formattedTime);
    }
}
