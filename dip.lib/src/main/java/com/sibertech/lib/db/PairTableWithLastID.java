package com.sibertech.lib.db;

public class PairTableWithLastID {
    
    public String tableName = null;
    public int lastID = -1;

    public PairTableWithLastID(String tableName, int lastID) {
        this.tableName = tableName;
        this.lastID = lastID;
    }
}
