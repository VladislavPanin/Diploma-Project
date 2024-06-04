package com.sibertech.lib.DTO;

import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    
    protected int client_id;
    protected int region_id;    
    protected int countOfBonuses;
    
    protected String name;
    protected String surname;
    protected String secondName;
    protected Date   birthdate;
    
    protected String comment;

    public Client(int client_id, int region_id, Date birthdate)
    {
        this.client_id = client_id;
        this.region_id = region_id;
        this.birthdate = birthdate;
        
        this.name       = "name_"       + client_id;
        this. surname   = "surname_"    + client_id;
        this.secondName = "secondName_" + client_id;        
        this.birthdate  = birthdate;
        
        comment = String.format("Comment for client #%d", client_id);
    }
}
