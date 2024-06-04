package com.sibertech.lib.conf;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogWriter {

    protected Logger logback = LoggerFactory.getLogger(LogWriter.class);
    protected Path   pLogDir = null;
    protected Path   pLogFilePath = null;
    protected String sConsolePrefix = null;

    protected long startTime  = System.currentTimeMillis(); // начало замера времени на всю вставку
    protected long spent_time = 0L;

    public void start() {
        startTime  = System.currentTimeMillis();
    }
    public int getSpentTime() {
        return (int)spent_time;
    }
    // ================================================================================================

    public LogWriter (String sLogDir,
                      String sLogFileName,
                      String sFirstLofString,
                      String sConsolePrefix) throws IOException
    {
        this.sConsolePrefix = sConsolePrefix;

        this.pLogDir      = Files.createDirectories(Paths.get(sLogDir));
        this.pLogFilePath = Paths.get(sLogDir, sLogFileName);
        Files.write(this.pLogFilePath, sFirstLofString.getBytes(),  StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
    // ------------------------------------------------------------------------------------------------

    public void _log (long X_countInserted, long Y) throws IOException {
        String spentTime = getSpentTimeStr ();

        String msg_file = String.format("%-14d\t%d",   X_countInserted, Y);
        String msg_cons = String.format("%,14d\t%,9d", X_countInserted, Y);

        Files.write (this.pLogFilePath, (msg_file + "\n").getBytes(), StandardOpenOption.WRITE, StandardOpenOption.APPEND);
        logback.info(String.format("%s: %s \t\t %-24s", this.sConsolePrefix, msg_cons, spentTime));
    }
    // ------------------------------------------------------------------------------------------------

    protected String getSpentTimeStr () {

        spent_time = System.currentTimeMillis() - startTime;

        int  hours = (int)spent_time / (3600 * 1000);
        int  remainder = (int)spent_time - hours*(3600 * 1000);
        int  minuts = remainder / (60 * 1000);
             remainder = remainder - minuts*(60 * 1000);
        int  seconds = remainder / 1000;
             remainder = remainder - seconds*1000;
        int  mls = remainder;

        String msg = String.format("%02d часов,  %02d минут,  %02d секунд,  %4d микросекунд", hours, minuts, seconds, mls);
        return msg;
    }
}
