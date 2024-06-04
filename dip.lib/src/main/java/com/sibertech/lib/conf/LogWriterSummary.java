package com.sibertech.lib.conf;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogWriterSummary {

    protected Logger logback = LoggerFactory.getLogger(LogWriterSummary.class);
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

    public LogWriterSummary (
                    String sLogDir,
                    String sLogFileName,
                    String sFirstLofString) throws IOException
    {
        this.pLogDir      = Files.createDirectories(Paths.get(sLogDir));
        this.pLogFilePath = Paths.get(sLogDir, sLogFileName);
        Files.write(this.pLogFilePath, sFirstLofString.getBytes(),  StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
    // ------------------------------------------------------------------------------------------------

    public void _logInsTime (
            String salesTableName,
            int insertedRows,
            int rowsInTable,
            int insTime) throws IOException {

        String msg_file = String.format("Вставка в таблицу %s. Формат: [Всего строк в таблице; время на вставку %5d строк]: %4d  %12d",
                salesTableName,
                insertedRows,
                rowsInTable,
                insTime);

        Files.write (this.pLogFilePath, (msg_file + "\n").getBytes(), StandardOpenOption.WRITE, StandardOpenOption.APPEND);
    }

    public void _logTimeouts (
            String salesTableName,
            int insertedRows,
            int rowsInTable,
            int timeoutsCount) throws IOException {

        String msg_file = String.format("Вставка в таблицу %s. Формат: [Всего строк в таблице; количество таймаутов при вставке %5d строк]: %4d  %-12d",
                salesTableName,
                insertedRows,
                rowsInTable,
                timeoutsCount);

        Files.write (this.pLogFilePath, (msg_file + "\n").getBytes(), StandardOpenOption.WRITE, StandardOpenOption.APPEND);
    }
    // ------------------------------------------------------------------------------------------------
}
