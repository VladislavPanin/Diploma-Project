package com.sibertech.lib.Gen.db.ins.core;

import com.sibertech.lib.DTO.Market;
import com.sibertech.lib.DTO.Region;
import com.sibertech.lib.conf.DSet;
import com.sibertech.lib.conf.ConfSQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class InsRegionMarkets extends Ins {
    
    @Override
    protected void insert_internal(DSet dSet, Connection conn)  throws SQLException {      
        
        try (PreparedStatement preparedStatement = conn.prepareStatement(ConfSQL.INSERT_MARKETS);) {

            for(Map.Entry<Integer, Region> entryReg : dSet.getRegionsMap().entrySet()) {
                Region region = entryReg.getValue();   
                Map<Integer, Market> marketsMap = region.getMarketsMap();
                
                for(Map.Entry<Integer, Market> entryMark : marketsMap.entrySet()) {
                    Market mark = entryMark.getValue();            

                    preparedStatement.setInt      (1, mark.getId());
                    preparedStatement.setInt      (2, mark.getRegion_id());
                    preparedStatement.setInt      (3, mark.getMarket_id_on_region());
                    preparedStatement.setString   (4, mark.getMarket_name());
                    preparedStatement.setString   (5, "some comment for Market #" + mark.getMarket_id_on_region() + " at region #" + region.getId());

                    preparedStatement.execute();
                    this.incLineCount();
                }
            }
        }       
    }
}
