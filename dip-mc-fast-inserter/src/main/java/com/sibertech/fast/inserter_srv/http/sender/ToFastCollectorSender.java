package com.sibertech.fast.inserter_srv.http.sender;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sibertech.lib.DTO.ProductBasket;
import com.sibertech.lib.conf.Conf;
import com.sibertech.lib.conf.ConfApp;
import com.sibertech.lib.params.http_client.MyObjResponse;
import com.sibertech.lib.params.http_client.ResponseHandler_forMyObjResponse;
import java.io.IOException;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.entity.ByteArrayEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToFastCollectorSender {

    protected ConfApp app = Conf.inst().app();
            
    protected ObjectMapper objectMapper = ConfApp.objectMapperInst();
    protected Logger logback = LoggerFactory.getLogger(ToFastCollectorSender.class);
    
    public void send(int threadNumber, ProductBasket basket)  throws IOException {        
        String url = app.getMc_fast_collector();                
        
        CloseableHttpClient httpClient = HttpClients.createDefault();
         try (httpClient) {
            HttpPost httpPost = new HttpPost(url);
            String jStr = objectMapper.writeValueAsString(basket);             
            HttpEntity httpEntity = new ByteArrayEntity(jStr.getBytes("UTF-8"), ContentType.APPLICATION_JSON, "UTF-8");
            httpPost.setEntity(httpEntity);
            
            ClassicHttpResponse response = httpClient.execute(httpPost);
            int status = response.getCode();
            boolean success =  status >= HttpStatus.SC_SUCCESS && status < HttpStatus.SC_REDIRECTION;
            if (!success)
            {
                System.out.println("!!! HTTP STATUS = " + status);  
                System.out.println("basket = " + jStr);
            }
        }
    }

    /*
    public void send(int threadNumber, ProductBasket basket)  throws IOException {        
        String url = app.getMc_fast_collector();                
        
        CloseableHttpClient httpClient = HttpClients.createDefault();
         try (httpClient) {
            HttpPost httpPost = new HttpPost(url);
            String jStr = objectMapper.writeValueAsString(basket);             
            HttpEntity httpEntity = new ByteArrayEntity(jStr.getBytes("UTF-8"), ContentType.APPLICATION_JSON, "UTF-8");
            httpPost.setEntity(httpEntity);
            HttpClientResponseHandler<MyObjResponse> responseHandler = new ResponseHandler_forMyObjResponse();
            MyObjResponse resp = 
            httpClient.execute(httpPost, responseHandler);
            int num = resp.getNum();
            if (num % 100 == 0)
                 System.out.println("Обработана корзина  №"+num);
            
        }
    }
    
    public void send_dbg(int threadNumber, ProductBasket basket)  throws IOException {        
        String url = app.getMc_fast_collector();                
        
        CloseableHttpClient httpClient = HttpClients.createDefault();
         try (httpClient) {
            HttpPost httpPost = new HttpPost(url);
            String jStr = objectMapper.writeValueAsString(basket);             
            HttpEntity httpEntity = new ByteArrayEntity(jStr.getBytes("UTF-8"), ContentType.APPLICATION_JSON, "UTF-8");
            httpPost.setEntity(httpEntity);
            HttpClientResponseHandler<MyObjResponse> responseHandler = new ResponseHandler_forMyObjResponse();
            MyObjResponse resp = 
            httpClient.execute(httpPost, responseHandler);
            int num = resp.getNum();
            if (num % 100 == 0)
                 System.out.println("Обработана корзина  №"+num);
            
        }
    }*/
}
