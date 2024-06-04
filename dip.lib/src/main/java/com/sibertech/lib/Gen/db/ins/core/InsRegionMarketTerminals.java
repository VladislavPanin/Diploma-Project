package com.sibertech.lib.Gen.db.ins.core;

import com.sibertech.lib.DTO.Market;
import com.sibertech.lib.DTO.Region;
import com.sibertech.lib.DTO.Terminal;
import com.sibertech.lib.conf.DSet;
import com.sibertech.lib.conf.ConfSQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class InsRegionMarketTerminals extends Ins {
    
    @Override
    protected void insert_internal(DSet dSet, Connection conn)  throws SQLException {  
        
        try (PreparedStatement preparedStatement = conn.prepareStatement(ConfSQL.INSERT_TERMINALS);) {

            for(Map.Entry<Integer, Region> entryReg : dSet.getRegionsMap().entrySet()) {
                Region region = entryReg.getValue();   
                Map<Integer, Market> marketsMap = region.getMarketsMap();
                
                for(Map.Entry<Integer, Market> entryMark : marketsMap.entrySet()) {
                    Market mark = entryMark.getValue(); 
                    Map<Integer,Terminal> terminalsMap = mark.getTerminals_in_market();

                    for(Map.Entry<Integer, Terminal> entryTerm : terminalsMap.entrySet()) {
                        Terminal term = entryTerm.getValue(); 

                        // id, region_id, market_id, terminal_id_on_market, comment
                        preparedStatement.setInt      (1, countOfInsertedLines + 1);
                        preparedStatement.setInt      (2, term.getRegion_id());
                        preparedStatement.setInt      (3, term.getMarket_id());
                        preparedStatement.setInt      (4, term.getTerminal_id_on_market());
                        preparedStatement.setString   (5, term.getComment());

                        preparedStatement.execute();
                        this.incLineCount();
                    }
                
                }
            }
        }    
    }
}
