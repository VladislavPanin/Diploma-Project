package com.sibertech.lib.Gen;

public class NextID {
    private int ID;
    
    public NextID (int startIdx) {
        ID = startIdx;
    }
    
    public int next () {
        return ID++;
    }
}
