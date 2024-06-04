package com.sibertech.lib.Gen.db.ins.core;

import com.sibertech.lib.DTO.Client;
import com.sibertech.lib.DTO.Region;
import com.sibertech.lib.conf.DSet;
import com.sibertech.lib.conf.ConfSQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class InsClients extends Ins {
    
    @Override
    protected void insert_internal(DSet dSet, Connection conn)  throws SQLException {     
        
        try (PreparedStatement preparedStatement = conn.prepareStatement(ConfSQL.INSERT_CLIENTS);) {

            for(Map.Entry<Integer, Region> entryReg : dSet.getRegionsMap().entrySet()) {
                Region region = entryReg.getValue();
                Map<Integer, Client>  clientsMap = region.getClientsMap();
                
                for(Map.Entry<Integer, Client> entryCli : clientsMap.entrySet()) {
                    Client cli = entryCli.getValue();                

                    preparedStatement.setInt      (1, cli.getClient_id());
                    preparedStatement.setInt      (2, cli.getRegion_id());
                    preparedStatement.setString   (3, cli.getName());
                    preparedStatement.setString   (4, cli.getSurname());
                    preparedStatement.setString   (5, cli.getSecondName());
                    preparedStatement.setDate     (6, cli.getBirthdate());
                    preparedStatement.setInt      (7, cli.getCountOfBonuses());                
                    preparedStatement.setString   (8, cli.getComment());

                    preparedStatement.execute();
                    this.incLineCount();
                }
            }
        }       
    }
}
