package com.sibertech.lib.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Loyality {
    
    protected int id = -1;    
    protected int region_id = -1;
    protected int category_id = -1;
    protected int loyality_params_id = -1;
    
    protected double coefficient_by_region = -1.0;    
    protected String comment = "not inited comment of Loyality";    
    
    public Loyality (int id, int region_id, int category_id, int loyality_params_id, double coefficient_by_region) {
        
        this.coefficient_by_region = coefficient_by_region;
        
        this.id = id;
        this.region_id = region_id;
        this.category_id = category_id;        
        this.loyality_params_id = loyality_params_id;
        
        this.comment = "Комментарий для программы лояльности #" + id;
    }    
}
