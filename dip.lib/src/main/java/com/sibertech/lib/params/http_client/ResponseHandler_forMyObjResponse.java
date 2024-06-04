package com.sibertech.lib.params.http_client;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sibertech.lib.conf.ConfApp;
import java.io.IOException;
import java.io.InputStream;
import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;

public class ResponseHandler_forMyObjResponse  implements HttpClientResponseHandler<MyObjResponse> {

    protected ClassicHttpResponse response = null;
    ObjectMapper objectMapper = ConfApp.objectMapperInst();

    @Override
    public MyObjResponse handleResponse(ClassicHttpResponse response) throws HttpException, IOException {

        this.response = response;
        int status = response.getCode();

        if (status >= HttpStatus.SC_SUCCESS && status < HttpStatus.SC_REDIRECTION)  {

                JsonFactory jsonFactory = new JsonFactory();

                try (org.apache.hc.core5.http.HttpEntity httpEntity = response.getEntity();
                     InputStream inputStream = httpEntity.getContent())
                {
                        JsonParser jsonParser = jsonFactory.createParser(inputStream);
                        jsonParser.setCodec(objectMapper);
                        MyObjResponse myObjResponse = jsonParser.readValueAs(MyObjResponse.class);
                        return myObjResponse;
                }
        }
        else {
               throw new ClientProtocolException("Unexpected response status: " + status);
        }
    }

    public ClassicHttpResponse getResponse() {
        return response;
    }
}
