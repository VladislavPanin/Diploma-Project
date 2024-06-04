package com.sibertech.lib.Gen;

import com.sibertech.lib.DTO.Client;
import com.sibertech.lib.DTO.Region;
import com.sibertech.lib.conf.DSet;
import com.sibertech.lib.conf.ConfDb;
import com.sibertech.lib.db.actuator.DtoActuator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenAuxClients {

    protected DtoActuator actuator = new DtoActuator();
    protected Logger logback = LoggerFactory.getLogger(GenAuxClients.class);
    
    public void generate (DSet dSet) throws Exception {  
        
        Map<Integer, Region> regionsMap = dSet.getRegionsMap();
        int regionCount = regionsMap.size();
        int idxRegion;
        
        for(int i=1; i <= ConfDb.COUNT_OF_CLIENTS; i++) {            
            
            idxRegion = i % regionCount;
            idxRegion = (0 == idxRegion) ? regionCount : idxRegion;
            
            Client clnt = new Client(i, idxRegion, actuator.getRandomBirstday(1960, 2000));
            
            Region region = regionsMap.get(idxRegion);
            region.addClient(clnt);
            
            //logback.info("idxRegion = " + idxRegion);
        }
    }
}
