package com.sibertech.lib.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategory {
    
    protected int id = -1;
    protected String name = "not inited ProductCategory";
    protected String comment = "not inited comment of ProductCategory";
    
    public ProductCategory (int id) {
        this.id = id;
        this.name = "Категория продуктов #" + id;
        
        this.comment = "Комментарий для категории продукта #" + id;
    }
}
