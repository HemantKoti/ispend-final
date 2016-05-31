package com.rhcloud.httpispend_jntuhceh.ispend;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Muneer on 26-05-2016.
 */
public class DateTimeHelper {

    public String getDisplayString(Date date) {

        DateFormat displayFormat = new SimpleDateFormat("d MMM");
        String displayString = displayFormat.format(date);
        return displayString;
    }

    public String getFullDisplayString(Date date) {

        DateFormat displayFormat = new SimpleDateFormat("dd MMM yyyy");
        String displayString = displayFormat.format(date);
        return displayString;
    }

    public String getInsertString(Date date) {
        DateFormat insertFormat = new SimpleDateFormat("yyyy-MM-dd");
        String insertString = insertFormat.format(date);
        return insertString;
    }

    public Date getStartDateObjectFromInsertString(String insertString) {
        DateFormat insertFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = insertFormat.parse(insertString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        setTimeToBeginningOfDay(calendar);
        date = calendar.getTime();
        return date;
    }

    public Date getEndDateObjectFromInsertString(String insertString) {
        DateFormat insertFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = insertFormat.parse(insertString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        setTimeToEndofDay(calendar);
        date = calendar.getTime();
        return date;
    }

    public String getActionBarDisplayString(DateRange dateRange) {
        String actionBarDisplayString  = getDisplayString(dateRange.startDateObject) + " - " + getDisplayString(dateRange.endDateObject);
        return actionBarDisplayString;
    }

    public String getFullDisplayString(DateRange dateRange) {
        DateFormat displayFormat = new SimpleDateFormat("d MMM yyyy");
        String fullDisplayString = displayFormat.format(dateRange.startDateObject) + " - " + displayFormat.format(dateRange.endDateObject);
        return fullDisplayString;
    }

    public Date getWeekStartDateObject() {
        Calendar c = Calendar.getInstance();
        //c.setFirstDayOfWeek(Calendar.MONDAY);
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        setTimeToBeginningOfDay(c);
        return c.getTime();
    }

    public Date getWeekEndDateObject() {
        Calendar c = Calendar.getInstance();
        //c.setFirstDayOfWeek(Calendar.MONDAY);
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        setTimeToEndofDay(c);
        c.add(Calendar.DATE, 6);
        return c.getTime();
    }

    public Date getMonthBeginDateObject() {
        Date monthBeginDateObject;
        Calendar calendar = getCalendarForNow();
        calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        setTimeToBeginningOfDay(calendar);
        monthBeginDateObject = calendar.getTime();
        return monthBeginDateObject;
    }

    public Date getMonthEndDateObject() {
        Date monthEndDateObject;
        Calendar calendar = getCalendarForNow();
        calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        setTimeToEndofDay(calendar);
        monthEndDateObject = calendar.getTime();
        return monthEndDateObject;
    }

    public Date getYearBeginDateObject() {
        Date yearBeginDateObject = null;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        setTimeToBeginningOfDay(cal);
        yearBeginDateObject = cal.getTime();
        return yearBeginDateObject;
    }

    public Date getYearEndDateObject() {
        Date yearEndDateObject = null;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DAY_OF_MONTH, 31);
        setTimeToEndofDay(cal);
        yearEndDateObject = cal.getTime();
        return yearEndDateObject;
    }

    public Calendar getCalendarForNow() {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(new Date());
        return calendar;
    }

    public void setTimeToBeginningOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    public void setTimeToEndofDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
    }

    public String getDisplayDateStringFromInsertDateString(String insertDateString) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        try {
            date = df.parse(insertDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat displayFormat = new SimpleDateFormat("EEE MMM d yyyy");
        String dateInWords = displayFormat.format(date);
        return dateInWords;
    }

    public String getInsertDateStringFromTheseIntegers(Integer year_x, Integer month_x, Integer day_x) {
        DateFormat insertFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = insertFormat.parse(year_x.toString() + "-" + month_x.toString() + "-" + day_x.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String insertString = getInsertString(date);
        return insertString;
    }

    public String getDisplayStringFromDateObject(Date date) {
        DateFormat displayFormat = new SimpleDateFormat("EEE MMM d yyyy");
        String dateInWords = displayFormat.format(date);
        return dateInWords;
    }
}
