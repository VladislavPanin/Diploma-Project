package com.sibertech.lib.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoyalityParam {

    protected int id = -1;
    protected String name = "not inited LoyalityParam";
    protected String comment = "not inited comment of LoyalityParams";    
    
    protected double precission  = 100.0;
    protected double coefficient = -1.0;
    
    
    
    public LoyalityParam (int id, double coefficient) {
        
        this.coefficient = coefficient;
        
        this.id = id;
        this.name = "Параметры #" + id + " программы лояльности ";
        this.comment = "Комментарий для параметров программы лояльности #" + id;  
    }    
}
