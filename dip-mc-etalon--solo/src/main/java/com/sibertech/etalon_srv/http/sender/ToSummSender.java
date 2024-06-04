package com.sibertech.etalon_srv.http.sender;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sibertech.lib.conf.Conf;
import com.sibertech.lib.conf.ConfApp;
import com.sibertech.lib.params.http_client.MyObjResponse;
import com.sibertech.lib.params.http_client.Point;
import com.sibertech.lib.params.http_client.ResponseHandler_forMyObjResponse;
import java.io.IOException;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.ByteArrayEntity;

public class ToSummSender {

    protected String summSeviceUrl_inserted = Conf.inst().app().getMc_summ_etal_inserted();
    protected String summSeviceUrl_timeouts = Conf.inst().app().getMc_summ_etal_timeouts();

    protected ObjectMapper objectMapper = ConfApp.objectMapperInst();

    public void send_inserted(int countOfInsertedLines, int interval, int thread_num, String serv_IP) throws IOException {
        Point point = new Point(countOfInsertedLines, interval, thread_num, serv_IP, "send_inserted");
        sendPoint (summSeviceUrl_inserted, point);

       // String msg = String.format(" ------------- поток #%-2d  (INSERTED)    X=%d    Y=%d", thread_num, point.getX(), point.getY());
       // System.out.println(msg);
    }

    public void send_timeouts(int countOfInsertedLines, int timeoutCounter, int thread_num, String serv_IP) throws IOException {
        Point point = new Point(countOfInsertedLines, timeoutCounter, thread_num, serv_IP, "send_timeouts");
        sendPoint (summSeviceUrl_timeouts, point);

       // String msg = String.format(" ------------- поток #%-2d  (TIMEOUTS)    X=%d    Y=%d", thread_num, point.getX(), point.getY());
       // System.out.println(msg);
    }

    public MyObjResponse sendPoint (String url, Point point) throws IOException {

        MyObjResponse myObjResponse;
        CloseableHttpClient httpClient = HttpClients.createDefault();

         try (httpClient) {
            HttpPost httpPost = new HttpPost(url);
            String jStr = objectMapper.writeValueAsString(point);
             //HttpEntity entity = new StringEntity(jStr, "UTF-8"); - так не получится. Русские буквы сприн так не поймет.
            // Нужно вот так:
            HttpEntity httpEntity = new ByteArrayEntity(jStr.getBytes("UTF-8"), ContentType.APPLICATION_JSON, "UTF-8");
            httpPost.setEntity(httpEntity);

            HttpClientResponseHandler<MyObjResponse> responseHandler = new ResponseHandler_forMyObjResponse();
            myObjResponse = httpClient.execute(httpPost, responseHandler);
        }
        //System.out.println("Сервис суммирования ответил: "+ myObjResponse.getMsg());
        return myObjResponse;
    }

    public static MyObjResponse sendOver (MyObjResponse msgResp) throws IOException {

        String url = Conf.inst().app().getMc_summ_etal_over();

        MyObjResponse myObjResponse;
        CloseableHttpClient httpClient = HttpClients.createDefault();

         try (httpClient) {
            HttpPost httpPost = new HttpPost(url);
            ObjectMapper objMapper = ConfApp.objectMapperInst();
            String jStr = objMapper.writeValueAsString(msgResp);
             //HttpEntity entity = new StringEntity(jStr, "UTF-8"); - так не получится. Русские буквы сприн так не поймет.
            // Нужно вот так:
            HttpEntity httpEntity = new ByteArrayEntity(jStr.getBytes("UTF-8"), ContentType.APPLICATION_JSON, "UTF-8");
            httpPost.setEntity(httpEntity);

            HttpClientResponseHandler<MyObjResponse> responseHandler = new ResponseHandler_forMyObjResponse();
            myObjResponse = httpClient.execute(httpPost, responseHandler);
        }
        //System.out.println("Сервис суммирования ответил: "+ myObjResponse.getMsg());
        return myObjResponse;
    }
}
