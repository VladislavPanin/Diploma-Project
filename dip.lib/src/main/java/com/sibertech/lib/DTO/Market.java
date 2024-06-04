package com.sibertech.lib.DTO;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Market{
    
    protected Logger logback = LoggerFactory.getLogger(Market.class);
    
    protected int id;
    protected int region_id;
    protected int market_id_on_region;
    
    protected String  market_name;    
    protected String comment;
    
    protected Map<Integer,Terminal> terminals_in_market = new HashMap();
    
    public void addTerminal(Terminal terminal){
        int idx = terminals_in_market.size()+1;
        this.terminals_in_market.put(idx, terminal);
    }
    
    public Market(int id_market, int market_id_on_region, int region_id, String name){
        this.id = id_market;        
        this.market_id_on_region = market_id_on_region;
        this.region_id = region_id;
        this.market_name = name;
        
        this.comment = String.format("Comment for market #%d in region #%d", market_id_on_region, region_id);
    }
}
