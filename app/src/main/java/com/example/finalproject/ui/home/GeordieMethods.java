package com.example.finalproject.ui.home;

import static com.example.finalproject.ui.home.EventAdapter.getMonth;

import java.text.DateFormatSymbols;
import java.time.LocalTime;

public class GeordieMethods {
    public static String timeTostring(LocalTime time) {

        boolean am = true;
        int hour = time.getHour();
        if (hour > 12) {
            hour -= 12;
            am = false;
        }
        return (hour + ":" + String.format("%02d", time.getMinute()) + ((am) ? " AM" : " PM"));
    }

    public static String modalTime(EventModal eventModal)
    {
        return String.format("%s %s %d @%s", new DateFormatSymbols().getWeekdays()[eventModal.getDate().getDayOfWeek().getValue()], getMonth(eventModal.getDate().getMonthValue()), eventModal.getDate().getDayOfMonth(), GeordieMethods.timeTostring(eventModal.getTime()));
    }
}