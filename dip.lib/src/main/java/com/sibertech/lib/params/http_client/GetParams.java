package com.sibertech.lib.params.http_client;

import com.sibertech.lib.conf.ConfApp;
import java.io.IOException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;

public class GetParams {
    
    public ConfApp requestParams (String url) throws IOException {
        ConfApp appConf;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        
         try (httpClient) {             
            HttpGet httpGet = new HttpGet(url);

            HttpClientResponseHandler<ConfApp> responseHandler = new ResponseHandler_params();
            appConf = httpClient.execute(httpGet, responseHandler);
        }         
        return appConf;
    }
}
