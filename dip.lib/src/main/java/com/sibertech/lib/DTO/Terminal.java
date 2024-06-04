package com.sibertech.lib.DTO;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Terminal {
    
    protected int id;
    protected int region_id;
    protected int market_id;
    protected int terminal_id_on_market;
    protected String comment;
    
    protected Map<Integer,ProductBasket> productBasketMap = new HashMap<>();
    
    public Terminal(int id_terminal, int terminal_id_on_market, int market_id, int region_id){
        
        this.id = id_terminal;
        this.terminal_id_on_market = terminal_id_on_market;
        this.region_id = region_id;
        this.market_id = market_id;
        
        comment = String.format("comment for terminal #%d on Market #%d in Region #%d", terminal_id_on_market,  region_id, market_id);
    }
    
    public void addProductBasket(ProductBasket basket){
        int basketIdx = productBasketMap.size()+1;
        this.productBasketMap.put(basketIdx, basket);
    }
    
     public int getCountOFBaskets(){
        int sz = productBasketMap.size();
        return sz;
    }
}
