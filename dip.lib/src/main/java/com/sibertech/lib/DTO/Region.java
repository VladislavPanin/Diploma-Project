package com.sibertech.lib.DTO;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Region 
{
    protected boolean isComplited = false;
    
    protected int id = -1;
    protected String name = "not inited Region";
    protected String comment = "";
    
    protected Map<Integer, Client>   clientsMap = new HashMap<>();    
    protected Map<Integer, Market>   marketsMap = new HashMap<>();
    
    public Region(int id){
        this.id = id;
        this.name = String.format("Регион #%02d, НЕ ЗАПОЛНЕННЫЙ", id);
        this.comment = String.format("Comment for region #%02d", id);
    }

    public void addMarket(Market market){
        int market_id = marketsMap.size()+1;
        this.marketsMap.put(market_id, market);
    }
    
    public void addClient(Client client){
        int client_id = clientsMap.size()+1;
        this.clientsMap.put(client_id, client);
    }
    
    public void setFillCompliteded (){
        isComplited = true;
        this.name = String.format("Регион #%02d", id);        
    }
}
