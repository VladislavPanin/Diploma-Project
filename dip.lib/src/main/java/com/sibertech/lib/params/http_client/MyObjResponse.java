package com.sibertech.lib.params.http_client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyObjResponse {

    protected int num = 0;
    protected boolean success = true;
    protected String  msg     = "No message";
    protected int spentTimeMillisec = -1;
    protected int timeoutsCount = -1;
}
