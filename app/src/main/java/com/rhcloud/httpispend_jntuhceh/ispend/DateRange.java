package com.rhcloud.httpispend_jntuhceh.ispend;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Muneer on 26-05-2016.
 */
public class DateRange {

    Date startDateObject, endDateObject;
    DateTimeHelper dateTimeHelper;

    DateRange(String startDateInsertString, String endDateInsertString) {
        dateTimeHelper = new DateTimeHelper();
        this.startDateObject = dateTimeHelper.getStartDateObjectFromInsertString(startDateInsertString);
        this.endDateObject = dateTimeHelper.getEndDateObjectFromInsertString(endDateInsertString);
    }

    DateRange(String type) {
        dateTimeHelper = new DateTimeHelper();
        switch (type) {

            case "w":
                this.startDateObject = dateTimeHelper.getWeekStartDateObject();
                this.endDateObject = dateTimeHelper.getWeekEndDateObject();
                break;

            case "m":
                this.startDateObject = dateTimeHelper.getMonthBeginDateObject();
                this.endDateObject = dateTimeHelper.getMonthEndDateObject();
                break;

            case "y":
                this.startDateObject = dateTimeHelper.getYearBeginDateObject();
                this.endDateObject = dateTimeHelper.getYearEndDateObject();
                break;

        }
    }

    public void subtractWeek() {
        Calendar cal = Calendar.getInstance();

        cal.setTime(this.startDateObject);
        cal.add(Calendar.DATE, -7);
        this.startDateObject = cal.getTime();

        cal.setTime(this.endDateObject);
        cal.add(Calendar.DATE, -7);
        this.endDateObject = cal.getTime();
    }

    public void addWeek() {
        Calendar cal = Calendar.getInstance();

        cal.setTime(this.startDateObject);
        cal.add(Calendar.DATE, 7);
        this.startDateObject = cal.getTime();

        cal.setTime(this.endDateObject);
        cal.add(Calendar.DATE, 7);
        this.endDateObject = cal.getTime();
    }

    public void subtractMonth() {
        Calendar cal = Calendar.getInstance();

        cal.setTime(this.startDateObject);
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        this.startDateObject = cal.getTime();

        cal.setTime(this.endDateObject);
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        this.endDateObject = cal.getTime();
    }

    public void addMonth() {
        Calendar cal = Calendar.getInstance();

        cal.setTime(this.startDateObject);
        cal.add(Calendar.MONTH, 1);
        cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        this.startDateObject = cal.getTime();

        cal.setTime(endDateObject);
        cal.add(Calendar.MONTH, 1);
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        this.endDateObject = cal.getTime();
    }

    public void subtractYear() {
        Calendar cal = Calendar.getInstance();

        cal.setTime(this.startDateObject);
        cal.add(Calendar.YEAR, -1);
        this.startDateObject = cal.getTime();

        cal.setTime(this.endDateObject);
        cal.add(Calendar.YEAR, -1);
        this.endDateObject = cal.getTime();
    }

    public void addYear() {
        Calendar cal = Calendar.getInstance();

        cal.setTime(this.startDateObject);
        cal.add(Calendar.YEAR, 1);
        this.startDateObject = cal.getTime();

        cal.setTime(this.endDateObject);
        cal.add(Calendar.YEAR, 1);
        this.endDateObject = cal.getTime();
    }
}
