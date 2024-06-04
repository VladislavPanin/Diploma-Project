package com.sibertech.lib.conf;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sibertech.lib.utils.IP;
import com.sibertech.lib.utils.Util;
import java.time.LocalDateTime;
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConfApp {

    @JsonIgnore public static String LOG_PEFIX        = " ============";
    @JsonIgnore public static String LOG_SPLIT        = LOG_PEFIX + " ------------------------------------------------------------------------------------------------------------------------------------------------------------";
    @JsonIgnore public static String LOG_PEFIX_ERR    = " ============ --- !!    <ERROR>:      ";
    @JsonIgnore public static String LOG_PEFIX_EXCEPT = " ============ --- !!    <EXCEPTION>:  ";

    @JsonIgnore protected String  rootSalesTableName      = "sales"; // нужно для работы вставки в генераторе
    @JsonIgnore protected String  rootDictionaryTableName = "order_numbers_dictionary"; // нужно для работы вставки в генераторе

    @JsonIgnore protected String  ipHostThis = ""; // для каждого хоста определяется индивидуально

    protected String dateTime_forDirPath = ""; // часть пути каталога, куда будет выводится лог

    // время начала нового замера (старта сервисов), устанавливается на конфигурационном сервисе, во время получения новой конфигурации
    // остальные сервисы получают его во время запроса конфигурации.
    protected int    delayMins = 3;     // задержка старта сервисов от времени обновления конфигурации на конф.сервисе в минутах
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime startInsertTime;  // вычисляется при установке delayMins (http://192.168.0.13:8070/dip-conf/set-params) на сервере
                                                 // конфигурации. Послле этого, микросервисы должны получить новую конфигурацию, где будет
                                                 // указан startInsertTime - время в которое они должны начать вставку

    protected int breakGen_afterCount   = 4000000; // для sales. генератор остановится после указанного количества вставок, если раньше не исчерпается набор корзин для региона (AllData::regionsMapMap)
    protected int breakDip_afterCount   =   3000; // каждый поток выполнит столько вставок - для замера точек в диплом
    protected int insertStepCounter     =   1000; //Количество шагов цикла, после которого будем выводить в лог точку {количество вставок в sales на этом шаге; время потраченное на этот шаг}

    protected String  dbName     = "db_diplom";

    protected int countLines_in_sales__xxxx = 0; // количество строк в таблице, над которой проводится эксперимент
    protected String  salesTableName        = "sales__0000";                    // таблица, на которой делаем замеры.
    protected String  dictionaryTableName   = "order_numbers_dictionary__0000"; // таблица, на которой делаем замеры.

    protected String  ipHostDb        = "192.168.0.13"; // IP на котором расположен Postgres
    protected String  ipHostConf      = "192.168.0.13"; // IP на котором расположен сервер конфигурации
    protected String  ipHostSummEtal  = "192.168.0.13"; // IP на котором расположен суммирующий сервер эталона
    protected String  ipHostCollector = "192.168.0.13"; // IP на котором расположен сервер-fast-приемник данных с серверов, провайдеров данных


    protected String mc_conf_url  = "http://" + ipHostConf + ":8070/dip-conf/get"; // откуда запрашиваем параметры

    protected String mc_summ_etal_timeouts = "http://" + ipHostSummEtal + ":8060/dip-etal-summ/timeouts"; // куда отсылаем данные замеров для суммирования
    protected String mc_summ_etal_inserted = "http://" + ipHostSummEtal + ":8060/dip-etal-summ/inserted"; // куда отсылаем данные замеров для суммирования
    protected String mc_summ_etal_over     = "http://" + ipHostSummEtal + ":8060/dip-etal-summ/over";     // куда отсылаем сообщение о завершении вставки

    protected String mc_fast_collector = "http://" + ipHostCollector + ":8090/dip-fast-collector/listen-for-basket";

    // Число потоков вставки на одной машине должно быть равно числу регионов
    protected int  DB_INSERTER_THREADS_COUNT = 10; // сколько потоков на вставку будет запускаться на одной машине
    protected int  HOST_INSERTERS_COUNT = 1;       // сколько машин будет учавствовать во вставке

    protected int  DB_INSERTER_THREADS_COUNT_AT_ALL  = DB_INSERTER_THREADS_COUNT * HOST_INSERTERS_COUNT;
    protected static ObjectMapper objectMapper = null;
    // =======================================================================

    public ConfApp ()
    {
        ArrayList<String> ipList = IP.getIps();
        if (ipList.isEmpty())
            throw new RuntimeException ("Станция не имеет ни одного сетевого интерфейса");

        this.ipHostThis = ipList.get(0);
        this.dateTime_forDirPath = Util._nowStr_dateTime_forDirPath();
        this.startInsertTime = LocalDateTime.now().plusMinutes(delayMins);
    }

    public static ObjectMapper objectMapperInst() {
        if (objectMapper == null)
          objectMapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();

        return objectMapper;
    }

    @JsonIgnore protected boolean isRootSetConfig = false;
    // ! POST-запрос. Только на микросервисе провайдере конфигурации !
    public void setConfRoot (ConfApp prms) throws Exception {

        isRootSetConfig = true;
            setConfForeign (prms);
            this.startInsertTime = LocalDateTime.now().plusMinutes(delayMins);
        isRootSetConfig = false;
    }

    public void setConfForeign (ConfApp prms) throws Exception {

        if(!isRootSetConfig)
            this.dateTime_forDirPath = prms.dateTime_forDirPath;

        this.breakGen_afterCount   = prms.breakGen_afterCount;
        this.breakDip_afterCount   = prms.breakDip_afterCount;
        this.insertStepCounter = prms.insertStepCounter;

        this.dbName = prms.dbName;

        this.salesTableName      = prms.salesTableName;
        this.dictionaryTableName = prms.dictionaryTableName;

        this.mc_conf_url = prms.mc_conf_url;

        this.ipHostDb = prms.ipHostDb;
        this.ipHostConf = prms.ipHostConf;
        this.ipHostSummEtal = prms.ipHostSummEtal;
        this.ipHostCollector = prms.ipHostCollector;
        this.mc_conf_url = prms.mc_conf_url;
        this.mc_summ_etal_timeouts = prms.mc_summ_etal_timeouts;
        this.mc_summ_etal_inserted = prms.mc_summ_etal_inserted;
        this.DB_INSERTER_THREADS_COUNT_AT_ALL = prms.DB_INSERTER_THREADS_COUNT_AT_ALL;
        this.HOST_INSERTERS_COUNT = prms.HOST_INSERTERS_COUNT;
        this.DB_INSERTER_THREADS_COUNT = prms.DB_INSERTER_THREADS_COUNT;

        this.delayMins = prms.delayMins;  //- для микросервисов (не конфиг) - запуск устанвливается prms.startInsertTime, взятым с конфига. А конфиг при установке времени запуска сам вычислит время ()по  delayMins
        this.startInsertTime = prms.startInsertTime;

        this.countLines_in_sales__xxxx = prms.countLines_in_sales__xxxx;
    }
}
