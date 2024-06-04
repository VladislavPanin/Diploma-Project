package com.sibertech.fast.buff.proc_srv.services;

import lombok.Data;

@Data
public class BufferInfo {
    protected int minID = -1;
    protected int maxID = -1;
    protected int count = -1; // количество записей в таблице от minID до
}
