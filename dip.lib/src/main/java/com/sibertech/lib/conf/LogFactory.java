package com.sibertech.lib.conf;

import java.io.IOException;

/**
 * LogFactory - Класс, отдающий по запросу get() экземпляр логгера LogWriter для каждого запускаемого потока требующего логгирования.
 * LogWriter реализует вывод в файл величин замера параметров работы сервиса.
 * Каждый микросервис выполняет только функцию вывода графика - поэтому есть только один тип LogWriter на сервис
 * При этом, параллельно запускается несколько потоков с одной и той же функцией.
 * Номер потока - суффикс имени файла, в который выводится график.
 *
 * При смене параметров сервиса, потоки должны быть остановлены, далее должен быть вызван метод Log.update(), который сбросит на 0 счетчик потоков.
 * После этого, потоки можно снова запустить на выполнение по прежнему алгоритму  самого начала, и для каждого из них нужно вызвать заново LogFactory.get().
 */

public class LogFactory {

    protected static String sLogRootDir = "__LOG/";
    protected static String sLogDir = null;

    public static final String LOG_MISSION_GEN_CORE = "core";
    public static final String LOG_MISSION_GEN_SALE = "sale";
    public static final String LOG_MISSION_ETAL = "etal";
    public static final String LOG_MISSION_FAST = "fast";
    // =========================================================================

    public    static LogWriter get_for_gen_core () throws IOException  {

        LogWriter log = get_for_gen (LOG_MISSION_GEN_CORE);
        return log;
    }
    public    static LogWriter get_for_gen_sales () throws IOException  {

        LogWriter log = get_for_gen (LOG_MISSION_GEN_SALE);
        return log;
    }
    protected static LogWriter get_for_gen (String mission) throws IOException  {

        String dateTime_forDirPath = Conf.inst().app().getDateTime_forDirPath();
        String ipThisHost          = Conf.inst().app().getIpHostThis();

        LogFactory. sLogDir = LogFactory.sLogRootDir + dateTime_forDirPath + "/";
        String sLogFileName = "log-" +
                               Conf.mcServName() +
                               "-" +
                               ipThisHost +
                               "-" +
                               Conf.inst().app().getDbName() +
                               "--" +
                               mission +
                               ".log";

        int inserStep = Conf.inst().app().getInsertStepCounter();

        String sFistLogString = String.format("""
        Микросервис %s
        IP %s
        Каталог логгирования %s
        Файл %s
        Вставка в БД %s,
        миссия %s
        Шаг вставки %,d
        Время начала логгирования %s\n\n""", Conf.mcServName(),
                                         ipThisHost,
                                         sLogDir,
                                         sLogFileName,
                                         Conf.inst().app().getDbName(),
                                         mission,
                                         inserStep,
                                         dateTime_forDirPath);

        String sConsolePrefix = String.format("%30s.%-10s: ", Conf.inst().app().getDbName(), mission);
        LogWriter log = new LogWriter (sLogDir, sLogFileName, sFistLogString, sConsolePrefix);

        return log;
    }

    public static LogWriterSummary get_for_summary_fast_insTime () throws IOException  {

        String dateTime_forDirPath = Conf.inst().app().getDateTime_forDirPath();
        String ipThisHost          = Conf.inst().app().getIpHostThis();

        LogFactory. sLogDir = LogFactory.sLogRootDir + dateTime_forDirPath + "/summary/";
        String sLogFileName = "log-" +
                               Conf.mcServName() +
                               "-" +
                               ipThisHost +
                               "-" +
                               Conf.inst().app().getDbName() +
                               "--summary_insTime-fast" +
                               ".log";

        int inserStep = Conf.inst().app().getInsertStepCounter();

        String sFistLogString = String.format("""
        -- Суммарная  информация быстрой вставки (буферизация) --
        Микросервис %s
        IP %s
        Каталог логгирования %s
        Файл %s
        Вставка в БД %s,
        Шаг вставки %,d
        Время начала логгирования %s\n\n""", Conf.mcServName(),
                                         ipThisHost,
                                         sLogDir,
                                         sLogFileName,
                                         Conf.inst().app().getDbName(),
                                         inserStep,
                                         dateTime_forDirPath);

        LogWriterSummary log = new LogWriterSummary (sLogDir, sLogFileName, sFistLogString);

        return log;
    }

    public static LogWriterSummary get_for_summary_etalon_insTime () throws IOException  {

        String dateTime_forDirPath = Conf.inst().app().getDateTime_forDirPath();
        String ipThisHost          = Conf.inst().app().getIpHostThis();

        LogFactory. sLogDir = LogFactory.sLogRootDir + dateTime_forDirPath + "/summary/";
        String sLogFileName = "log-" +
                               Conf.mcServName() +
                               "-" +
                               ipThisHost +
                               "-" +
                               Conf.inst().app().getDbName() +
                               "--summary_insTime-etalon" +
                               ".log";

        int inserStep = Conf.inst().app().getInsertStepCounter();

        String sFistLogString = String.format("""
        -- Суммарная  информация эталонной вставки (ВРЕМЯ ВСТАВКИ) --
        Микросервис %s
        IP %s
        Каталог логгирования %s
        Файл %s
        Вставка в БД %s,
        Шаг вставки %,d
        Время начала логгирования %s\n\n""", Conf.mcServName(),
                                         ipThisHost,
                                         sLogDir,
                                         sLogFileName,
                                         Conf.inst().app().getDbName(),
                                         inserStep,
                                         dateTime_forDirPath);

        LogWriterSummary log = new LogWriterSummary (sLogDir, sLogFileName, sFistLogString);

        return log;
    }

    public static LogWriterSummary get_for_summary_etalon_timeouts () throws IOException  {

        String dateTime_forDirPath = Conf.inst().app().getDateTime_forDirPath();
        String ipThisHost          = Conf.inst().app().getIpHostThis();

        LogFactory. sLogDir = LogFactory.sLogRootDir + dateTime_forDirPath + "/summary/";
        String sLogFileName = "log-" +
                               Conf.mcServName() +
                               "-" +
                               ipThisHost +
                               "-" +
                               Conf.inst().app().getDbName() +
                               "--summary_timeouts-etalon" +
                               ".log";

        int inserStep = Conf.inst().app().getInsertStepCounter();

        String sFistLogString = String.format("""
        -- Суммарная  информация эталонной вставки (КОЛИЧЕСТВО ТАЙМАУТОВ) --
        Микросервис %s
        IP %s
        Каталог логгирования %s
        Файл %s
        Вставка в БД %s,
        Шаг вставки %,d
        Время начала логгирования %s\n\n""", Conf.mcServName(),
                                         ipThisHost,
                                         sLogDir,
                                         sLogFileName,
                                         Conf.inst().app().getDbName(),
                                         inserStep,
                                         dateTime_forDirPath);

        LogWriterSummary log = new LogWriterSummary (sLogDir, sLogFileName, sFistLogString);

        return log;
    }

    public static LogWriter get_for_dip_inserted_rows (String mission, int iThreadNum) throws IOException  {
        // String suffix_logMission - "etal", "fast"
        mission = String.format("%s; thread# %02d -- (for inserted-rows)", mission, iThreadNum);
        LogWriter log = get_for_dip (mission);
        return log;
    }
    public static LogWriter get_for_dip_timeout_count (String mission, int iThreadNum) throws IOException  {
        // String suffix_logMission - "etal", "fast"
        mission = String.format("%s; thread# %02d -- (for timeout-count)", mission, iThreadNum);
        LogWriter log = get_for_dip (mission);
        return log;
    }

    public static LogWriter get_for_dip_inserted_rows_summ (String mission) throws IOException  {
         // String suffix_logMission - "etal", "fast"
        mission = String.format("%s  -- (for inserted-rows) summ", mission);
        LogWriter log = get_for_dip (mission);
        return log;
    }
    public static LogWriter get_for_dip_timeout_count_summ (String mission) throws IOException  {
        // String suffix_logMission - "etal", "fast"
        mission = String.format("%s  -- (for timeout_count) summ", mission);
        LogWriter log = get_for_dip (mission);
        return log;
    }
    public static LogWriter get_for_dip_inserted_rows_collector () throws IOException  {

        LogWriter log = get_for_dip ("FAST COLLECTOR -- (for inserted-rows)");
        return log;
    }
    public static LogWriter get_for_dip_buff_proc () throws IOException  {

        LogWriter log = get_for_dip ("FAST BUFF PROC -- (for inserted-rows)");
        return log;
    }

    protected static LogWriter get_for_dip (String mission) throws IOException  {

        String dateTime_forDirPath = Conf.inst().app().getDateTime_forDirPath();
        String ipThisHost          = Conf.inst().app().getIpHostThis();
        String currTableToInsert   = Conf.inst().app().getSalesTableName();

        LogFactory. sLogDir = LogFactory.sLogRootDir + dateTime_forDirPath + "/";
        String sLogFileName = "log-" +
                               Conf.mcServName() +
                               "-" +
                               ipThisHost +
                               "-" +
                               Conf.inst().app().getDbName() +
                               "--" +
                               mission +
                               "--" +
                               currTableToInsert +
                               ".log";

        int inserStep = Conf.inst().app().getInsertStepCounter();
        int onThread = Conf.inst().app().getBreakDip_afterCount();
        int inTotal  = onThread * Conf.inst().app().getDB_INSERTER_THREADS_COUNT_AT_ALL();

        String sFistLogString = String.format("""
        Микросервис %s
        IP %s
        Каталог логгирования %s
        Файл %s
        Вставка в БД %s
        Таблица %s
        Миссия %s
        Шаг вставки %,d
        Cтрок для вставки на каждый поток %,d
        Всего строк для вставки %,d
        Время обновления конфигурации логгирования %s\n\n""", Conf.mcServName(),
                                         ipThisHost,
                                         sLogDir,
                                         sLogFileName,
                                         Conf.inst().app().getDbName(),
                                         Conf.inst().app().getSalesTableName(),
                                         mission,
                                         inserStep,
                                         onThread,
                                         inTotal,
                                         dateTime_forDirPath);

        String sConsolePrefix = String.format("%30s.%-10s: ", Conf.inst().app().getDbName(), mission);
        LogWriter log = new LogWriter (sLogDir, sLogFileName, sFistLogString, sConsolePrefix);

        return log;
    }
}
