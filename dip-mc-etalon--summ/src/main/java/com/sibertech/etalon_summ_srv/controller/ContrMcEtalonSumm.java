package com.sibertech.etalon_summ_srv.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sibertech.etalon_summ_srv.services.ProcessPointInserts;
import com.sibertech.etalon_summ_srv.services.ProcessPointTimeouts;
import com.sibertech.lib.conf.Conf;
import com.sibertech.lib.conf.ConfApp;
import com.sibertech.lib.conf.LogFactory;
import com.sibertech.lib.conf.LogWriterSummary;
import com.sibertech.lib.params.http_client.GetParams;
import com.sibertech.lib.params.http_client.MyObjResponse;
import com.sibertech.lib.params.http_client.Point;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Микросервис эталонной вставки напрямую в БД
 * Слушает на порту 8060
 */
@RestController
@RequestMapping("/dip-etal-summ")
public class ContrMcEtalonSumm {

    protected Logger logback = LoggerFactory.getLogger(ContrMcEtalonSumm.class);
    protected ObjectMapper objectMapper = ConfApp.objectMapperInst();

    protected static Conf conf = Conf.inst("03.2-etalon-summ");
    protected static int  DB_INSERTER_THREAD_COUNT = 10;

    protected ProcessPointInserts procInserts = null;
    protected ProcessPointTimeouts procTimeouts = null;

    protected boolean isFirstGetConfig = true;

    LogWriterSummary logInsTime = null;
    LogWriterSummary logTimeouts = null;
// ============================================================================================================================
    //  команда обратится к микросервису конфигурации за конфигурацией
    // http://localhost:8060/dip-etal-summ/get-config
    @GetMapping("/get-config")
    public ResponseEntity<String> getConfig () throws Exception {

        String mcServName = Conf.mcServName();
        String url = conf.app().getMc_conf_url (); // откуда запрашиваем параметры
        url = url + "?servRequested=" +  URLEncoder.encode(mcServName, StandardCharsets.UTF_8);

        GetParams getter = new GetParams();
        ConfApp params = getter.requestParams(url);
        conf.app().setConfForeign (params);

        if (isFirstGetConfig) {
            logInsTime = LogFactory.get_for_summary_etalon_insTime();
            logTimeouts = LogFactory.get_for_summary_etalon_timeouts();
            isFirstGetConfig = false;
        }

        procInserts = new ProcessPointInserts();
        procTimeouts = new ProcessPointTimeouts();

        // String jConf = objectMapper.writeValueAsString(params);
        String jConfPretty = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(conf.app());

        String msg = String.format("%s (%s) конфигурация получена", ConfApp.LOG_PEFIX, Conf.mcServName());
        logback.info("");
        logback.info(msg);
        logback.info (jConfPretty);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(msg + "<br>" + jConfPretty);
    }
 // ===========================================================================================================================
    // загрузка таймаутов со всех потоков всех сервисов
    // http://localhost:8060/dip-etal-summ/timeouts
    @PostMapping("/timeouts")
    public ResponseEntity<MyObjResponse> collectTimeouts (@RequestBody Point point) throws Exception {

        procTimeouts.proc (point);

        MyObjResponse myObjResponse = new MyObjResponse();
        myObjResponse.setSuccess(true);

        String msg = "          поток #"+point.getThread_num() + " на интервале " + point.getX() + " отправил send_timeOut() со значением = " + point.getY();
        myObjResponse.setMsg(msg);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(myObjResponse);
    }
// ============================================================================================================================
    // загрузка времени вставки со всех потоков всех сервисов
    // http://localhost:8060/dip-etal-summ/inserted
    @PostMapping("/inserted")
    public ResponseEntity<MyObjResponse> collectInserts (@RequestBody Point point) throws Exception {

        procInserts.proc (point);
        MyObjResponse myObjResponse = new MyObjResponse();
        myObjResponse.setSuccess(true);

        String msg = "          поток #"+point.getThread_num() + " на интервале " + point.getX() + " отправил send_inserted() со значением = " + point.getY();
        myObjResponse.setMsg(msg);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(myObjResponse);
    }
// ============================================================================================================================
    // http://localhost:8060/dip-etal-summ/over
    @PostMapping("/over")
    public ResponseEntity<MyObjResponse> printOver (@RequestBody MyObjResponse res) throws Exception {

        if (res.isSuccess()) {
            String msg = res.getMsg() + "\n";
            msg += String.format("Время вставки %d записей в таблицу %s составило %,d  миллисекунд",
                    conf.app().getBreakDip_afterCount(),
                    conf.app().getSalesTableName(),
                    res.getSpentTimeMillisec());

            msg += String.format(". Количество timeout_lock за время вставки составило %,d", res.getTimeoutsCount());
            printSummary(res);

            logback.info (msg);
        }
        else
            logback.info ("Опреация вставки закончилась неудачей");

        MyObjResponse myObjResponse = new MyObjResponse();
        myObjResponse.setSuccess(true);
        myObjResponse.setMsg("Сообщение принято");

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(myObjResponse);
    }

    private void printSummary(MyObjResponse res) throws IOException {

        String salesTableName = conf.app().getSalesTableName();
        int rowsInTable = conf.app().getCountLines_in_sales__xxxx();
        int insertedRows = conf.app().getBreakDip_afterCount () * conf.app().getDB_INSERTER_THREADS_COUNT_AT_ALL();

        int time = res.getSpentTimeMillisec();
        int timeouts = res.getTimeoutsCount();

        logInsTime._logInsTime(salesTableName, insertedRows, rowsInTable, time);
        logTimeouts._logTimeouts(salesTableName, insertedRows, rowsInTable, timeouts);
    }
}