package com.sibertech.lib.utils;

import static java.lang.Math.floor;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Util {

    protected static Logger logback = LoggerFactory.getLogger(Util.class);

    public static String _to_s (java.sql.Date sqlDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String str = sdf.format(sqlDate);
        return str;
    }

    public static String _to_s (java.sql.Time sqlTime) {

        String str  = sqlTime.toString();
        return str;
    }

    public static String _to_s (java.sql.Timestamp sqlTimestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str  = sdf.format(sqlTimestamp);
        return str;
    }

    public static String _to_s(LocalDateTime ldt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String str = ldt.format(formatter);
        return str;
    }

    public static java.sql.Date _to_date (String yyyy_MM_dd) throws ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd");
        java.util.Date date = sdf. parse (yyyy_MM_dd);
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        return sqlDate;
    }

    public static java.sql.Date _localDate_to_sqlDate (LocalDate ld)
    {
        java.sql.Date sqlDate = java.sql.Date.valueOf(ld);
        return sqlDate;
    }

    public static java.sql.Time _now_sqlTime ()
    {
        java.util.Date date = new java.util.Date();
        java.sql.Time time = new java.sql.Time(date.getTime());

        return time;
    }

    public static java.sql.Date _now_sqlDate ()
    {
        java.util.Date date = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        return sqlDate;
    }

    public static java.sql.Date _getRandomYear (int startYear, int uptoYear) throws ParseException, Exception {
        int year  = Util._getRandFromRange (startYear, uptoYear);
        int month = Util._getRandFromRange (1, 12);
        int day   = Util._getRandFromRange (1, 28);

        String yyyy_MM_dd = String.format("%04d-%02d-%02d", year, month, day);
        java.sql.Date date = Util._to_date(yyyy_MM_dd);
        return date;
    }

    public static int _getRandFromRange (int fromStartRange, int toEndRange) throws Exception {

        if (toEndRange <= fromStartRange)
            throw new Exception("Значание начала диапазона должно быть меньше значения конца диапазона");
        double rand = Math.random();
        rand = rand * (double)((toEndRange+1) - fromStartRange);
        int
        idx = (int)floor (rand);
        idx += fromStartRange;
        if (idx > toEndRange){String msg = "getRandomId(): idx > toEndRange";throw new Exception(msg);}
        if (idx < fromStartRange){String msg = "getRandomId(): idx < fromStartRange";throw new Exception(msg);}

        return idx;
    }

    /**
     * @param hh_mm_ss - "08:39:00"
     * @return java.sql.Time
     */
    public static java.sql.Time _from(String hh_mm_ss)
    {
        java.sql.Time time = java.sql.Time. valueOf(hh_mm_ss);
        return time;
    }

    public static java.sql.Timestamp _Timestamp_from (String yyyy_MM_dd)
    {
        try
        {
            SimpleDateFormat df = new SimpleDateFormat ("yyyy-MM-dd");
            java.util.Date date = df. parse (yyyy_MM_dd);
            java.sql.Timestamp timestamp = new java.sql.Timestamp (date. getTime ());
            return timestamp;
        }
        catch(Exception ignore)
        {}
        return null;
    }

    public static LocalDateTime _now_date() {
        LocalDateTime ldt = LocalDateTime.now();
        return ldt;
    }

    public static LocalDateTime _now_date_without_mls() {

        LocalDateTime
        ldt = Util._now_date();
        ldt = ldt.truncatedTo(ChronoUnit.SECONDS);

        return ldt;
    }

    public static java.sql.Date _now_dateSQL() {
        LocalDateTime ldt = _now_date();
        java.sql.Date sqlDate = java.sql.Date.valueOf(ldt.toLocalDate());
        return sqlDate;
    }

    public static java.sql.Timestamp _nowTimestamp ()
    {
        ZonedDateTime zDT = ZonedDateTime. now();
        LocalDateTime lDT = LocalDateTime. ofInstant (zDT. toInstant (), ZoneId. of ("UTC"));
        java.sql.Timestamp     tst = java.sql.Timestamp. valueOf (lDT);

        return tst;
    }
    //----------------------------------------------------------
    public static String _nowStr_dateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String str = _nowStr (formatter);
        return str;
    }
    public static String _nowStr_dateTime_forDirPath() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm");
        String str = _nowStr (formatter);
        return str;
    }
    public static String _nowStr_dateOnly() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String str = _nowStr (formatter);
        return str;
    }

    public static String _nowStr_timeOnly() {

        java.util.Date date = new java.util.Date();
        java.sql.Time time = new java.sql.Time(date.getTime());
        String str = time.toString();
        return str;
    }

    /**
     *
     * @param pattern - шаблон, в которм будет возвращена текущее дата/время
     * "yyyy-MM-dd HH:mm::ss", "yyyy-MM-dd HH:mm", "yyyy-MM-dd", "yyyy_MM_dd- HH_mm" и т.д
     * @return
     */
    public static String _nowStr (String pattern) {
        DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = _now_date();
        String str = dateTime.format(formatter);
        return str;
    }

    public static String _nowStr (DateTimeFormatter formatter) {
        LocalDateTime dateTime = _now_date();
        String str = dateTime.format(formatter);
        return str;
    }
    //----------------------------------------------------------

    public static String _dateSQL_toStrDateTime(java.sql.Date sqlDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm");
        String str = sdf.format(sqlDate);
        return str;
    }

    public static String _date_toStrDate(LocalDateTime ldt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String str = ldt.format(formatter);
        return str;
    }

    public static String _date_toStrDateTime(LocalDateTime ldt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String str = ldt.format(formatter);
        return str;
    }
    //----------------------------------------------------------
}
