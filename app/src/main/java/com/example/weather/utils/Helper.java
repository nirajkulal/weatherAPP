package com.example.weather.utils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Helper {
    /**
     * Get coverted celsius with limited decimal
     * @param input
     * @return
     */
    public static String getCelsius(Double input) {
        return getLimitedDecimal((input - 273.15));
    }

    /**
     * get converted celsius in integer
     * @param input
     * @return
     */
    public static int getCelsiusInt(Double input) {
        return (int) (input - 273.15);
    }

    /**
     * Get Date with format dd MMM HH:mm"
     * @param lastUpdated
     * @return
     */
    public static String epochToDate(long lastUpdated) {
        Date date = new Date(lastUpdated);
        DateFormat format = new SimpleDateFormat("dd MMM HH:mm");
        return format.format(date);
    }

    /**
     * Get date with format "HH:mm"
     * @param lastUpdated
     * @return
     */
    public static String epochToDate2(long lastUpdated) {
        Date date = new Date(lastUpdated * 1000L);
        DateFormat format = new SimpleDateFormat("HH:mm");
       return format.format(date);
    }

    public static String getIcon(String icon) {
        return "http://openweathermap.org/img/w/" + icon + ".png";
    }

    /**
     * Limit decimal points
     * @param input
     * @return
     */
    public static String getLimitedDecimal(Double input) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(input);
    }
}
