package com.sibertech.lib.utils;


import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

//**********************************************************************************************************************
public class Utils
{
    //******************************************************************************************************************
    public static String get_UUID ()
    {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static void rewriteCookieToHeader (HttpServletRequest request, HttpServletResponse response)
    {
        if (response.containsHeader("SET-COOKIE"))
        {
            String sessionid   = request. getSession (). getId ();
            String contextPath = request. getContextPath ();
            String secure = "";

            if (request. isSecure ())
                secure = "; Secure";

            response. setHeader ("SET-COOKIE", "JSESSIONID=" + sessionid + "; Path=" + contextPath + "; HttpOnly" + secure);
        }
    }
    //******************************************************************************************************************
    public static void printRequestParams (ServletRequest req)
    {
        Map<String, String[]> map = req.getParameterMap();

        map.keySet().forEach(
        (key) -> {
                    String[] aS = map.get (key);
                    //LOG._dbg("key: " + key + " val:" + Arrays.toString(aS));
                 });

    }
    //******************************************************************************************************************
    public static String NowStr_UtcDataTime ()
    {
        ZonedDateTime zDT = ZonedDateTime. now(ZoneId. of ("UTC"));
        String ret = DateTimeFormatter.ISO_INSTANT.format (zDT);
        return ret;
    }
    //******************************************************************************************************************
    public static String UTC_getNowStr ()
    {
        ZonedDateTime zDT = ZonedDateTime. now(ZoneId. of ("UTC"));
        String ret = DateTimeFormatter.ISO_INSTANT.format (zDT);
        return ret;
    }
    //******************************************************************************************************************
    public static ZonedDateTime UTC_get_1900 ()
    {
        ZonedDateTime dZT = ZonedDateTime.of(1900, 01, 01, 0, 0, 0, 0, ZoneId.of("UTC"));
        return dZT;
    }
    //******************************************************************************************************************
    public static boolean UTC_isInFeature (ZonedDateTime date)
    {
        try
        {
            ZonedDateTime now = ZonedDateTime. now(ZoneId. of ("UTC"));
            if (now. isBefore (date))
                return true;
        }
        catch (Exception e)
        {
            //LOG._err(e.getMessage());
        }

        return false;
    }
    public static boolean UTC_inFeature (String isoDateTime)
    {
        try
        {
            ZonedDateTime now = ZonedDateTime. now(ZoneId. of ("UTC"));
            ZonedDateTime zDT = ZonedDateTime. parse (isoDateTime, DateTimeFormatter.ISO_DATE_TIME);

            if (now. isBefore (zDT))
                return true;
        }
        catch (Exception e)
        {
            //LOG._err(e.getMessage());
        }

        return false;
    }
    //******************************************************************************************************************
    public static boolean UTC_firstBeforeSecond (String firstIsoDateTime, String secondIsoDateTime)
    {
        try
        {
            ZonedDateTime first  = ZonedDateTime. parse (firstIsoDateTime, DateTimeFormatter.ISO_DATE_TIME);
            ZonedDateTime second = ZonedDateTime. parse (secondIsoDateTime, DateTimeFormatter.ISO_DATE_TIME);

            if (first. isBefore (second))
                return true;
        }
        catch (Exception e)
        {
            //LOG._err(e.getMessage());
        }

        return false;
    }
    //******************************************************************************************************************
    public static Timestamp UTC_getNow ()
    {
        ZonedDateTime zDT = ZonedDateTime. now();
        LocalDateTime lDT = LocalDateTime. ofInstant (zDT. toInstant (), ZoneId. of ("UTC"));
        Timestamp     tst = Timestamp. valueOf (lDT);

        return tst;
    }
    //******************************************************************************************************************
    public static Timestamp UTC_timestampFromIsoStr (String isoDateTime)
    {
        if (isoDateTime == null)
            return null;

        LocalDateTime localOnUTC;
        Timestamp     tsOnUTC;

        try
        {
            ZonedDateTime zDT = ZonedDateTime. parse (isoDateTime, DateTimeFormatter.ISO_DATE_TIME);
            localOnUTC = LocalDateTime. ofInstant (zDT. toInstant (), ZoneId. of ("UTC"));
            tsOnUTC = Timestamp. valueOf (localOnUTC);
        }
        catch (Exception e)
        {
            //LOG._err("Exception " + e.getMessage());
            return null;
        }

        return tsOnUTC;
    }
    //******************************************************************************************************************
    public static ZonedDateTime UTC_fromIsoStr (String isoDateTime)
    {
        if (isoDateTime == null)
            return null;

        try
        {
            ZonedDateTime zDT = ZonedDateTime. parse (isoDateTime, DateTimeFormatter.ISO_DATE_TIME);
            return zDT;
        }
        catch (Exception e)
        {
            //LOG._err("Exception " + e.getMessage());
        }

        return null;
    }
    //******************************************************************************************************************
    public static ZonedDateTime UTC_ruFormat_fromDate (String sDate)
    {
        try
        {
                SimpleDateFormat df = new SimpleDateFormat ("dd.MM.yyyy");
                Date date = df. parse (sDate);

                Timestamp timestamp = new Timestamp (date. getTime ());
                ZonedDateTime zDT = ZonedDateTime.ofInstant (timestamp.toInstant(), ZoneId. of ("UTC"));

                return zDT;
        }
        catch(Exception ignore)
        {}
        return null;
    }
    //******************************************************************************************************************
    public static Timestamp UTC_Ts_fromIsoDate (String sIsoDate_only)
    {
        try
        {
            SimpleDateFormat df = new SimpleDateFormat ("yyyy-MM-dd");
            Date date = df. parse (sIsoDate_only);

            Timestamp timestamp = new Timestamp (date. getTime ());
            return timestamp;
        }
        catch(Exception ignore)
        {}
        return null;
    }
    //******************************************************************************************************************
    public static Timestamp UTC_timestampFromZDT (ZonedDateTime zDT)
    {
        if (zDT == null)
            return null;

        LocalDateTime localOnUTC;
        Timestamp     tsOnUTC;

        try
        {
            localOnUTC = LocalDateTime. ofInstant (zDT. toInstant (), ZoneId. of ("UTC"));
            tsOnUTC = Timestamp. valueOf (localOnUTC);
        }
        catch (Exception e)
        {
            //LOG._err("Exception " + e.getMessage());
            return null;
        }

        return tsOnUTC;
    }
    //******************************************************************************************************************
    public static String UTC_get_ddmmyy_hhmm ()
    {
        String iso = Utils.UTC_getNowStr();

        String[] big = iso.split("T");
        String[] left = big[0].split("-");
        String[] right = big[1].split(":");

        String ret = left[2] + left[1] + left[0].substring(2) + "_" + right[0] + right[1];

        return ret;
    }

    public static String UTC_get_ddmmyy_hhmm_prec ()
    {
        String iso = Utils.UTC_getNowStr();

        String[] big = iso.split("T");
        String[] left = big[0].split("-");
        String[] right = big[1].split(":");

        String ret = left[2] + left[1] + left[0].substring(2) + "_" + right[0] + right[1] + "-" + right[2];

        return ret;
    }
    //******************************************************************************************************************
}
