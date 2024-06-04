package com.sibertech.fast.buff.proc_srv.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sibertech.fast.buff.proc_srv.services.InsFastBuffProc;
import com.sibertech.lib.conf.Conf;
import com.sibertech.lib.conf.ConfApp;
import com.sibertech.lib.conf.LogFactory;
import com.sibertech.lib.conf.LogWriterSummary;
import com.sibertech.lib.db.measure.DipTimer;
import com.sibertech.lib.params.http_client.GetParams;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Микросервис приемник данных от генератора отсылки данных
 * Слушает на порту 8085
 */
@RestController
@RequestMapping("/dip-fast-buff-proc")
public class ContrMcFastBuffProc {

    protected Logger logback = LoggerFactory.getLogger(ContrMcFastBuffProc.class);
    protected ObjectMapper objectMapper = ConfApp.objectMapperInst();

    protected static Conf conf = Conf.inst("04.3-fast-buff-proc");

    protected static ExecutorService threadPool = Executors.newFixedThreadPool(3);
    protected InsFastBuffProc ins;

    protected boolean isFirstGetConfig = true;

    protected LogWriterSummary logInsTime = null;
    protected DipTimer summaryTimer = null;

// ============================================================================================================================
    //  запрос конфигурации с микросервиса конфигурации
    // http://localhost:8085/dip-fast-buff-proc/get-config
    @GetMapping("/get-config")
    public ResponseEntity<String> getConfig () throws Exception {

       String mcServName = Conf.mcServName();
        String url = conf.app().getMc_conf_url (); // откуда запрашиваем параметры
        url = url + "?servRequested=" +  URLEncoder.encode(mcServName, StandardCharsets.UTF_8);

        GetParams getter = new GetParams();
        ConfApp params = getter.requestParams(url);
        conf.app().setConfForeign (params);

        if (isFirstGetConfig) {
            onFirstGetConfig();
            isFirstGetConfig = false;
        }

        // String jConf = objectMapper.writeValueAsString(params);
        String jConfPretty = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(conf.app());

        String msg = String.format("%s (%s) конфигурация получена", ConfApp.LOG_PEFIX, Conf.mcServName());
        logback.info("");
        logback.info(msg);
        logback.info (jConfPretty);

        this.ins = new InsFastBuffProc();
        this.ins.prepare(logInsTime, summaryTimer);
        threadPool.submit(ins);
        logback.info (String.format("%s (%s) Поток обработки буффера buffer_in отправлен на выполнение", ConfApp.LOG_PEFIX, Conf.mcServName()));

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(msg + "<br>" + jConfPretty);
    }

    private void onFirstGetConfig() throws IOException {
        logInsTime = LogFactory.get_for_summary_fast_insTime();

        summaryTimer = new DipTimer();
        LocalDateTime ldt = conf.app().getStartInsertTime();
        summaryTimer.startAt(ldt);
    }
}