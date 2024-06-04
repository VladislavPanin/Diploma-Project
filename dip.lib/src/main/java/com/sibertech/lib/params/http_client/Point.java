package com.sibertech.lib.params.http_client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Point {

    protected int x = -1;
    protected int y = -1;

    protected int thread_num = -1;
    protected String serv_IP = "0.0.0.0";

    protected String mission = "no mission";

    public Point (int x, int y, int thread_num, String serv_IP, String mission) {

        this.mission = mission;

        this.x = x;
        this.y = y;

        this.thread_num = thread_num;
        this.serv_IP = serv_IP;
    }
}
