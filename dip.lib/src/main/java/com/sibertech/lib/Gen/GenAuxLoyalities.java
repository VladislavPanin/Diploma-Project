package com.sibertech.lib.Gen;

import com.sibertech.lib.DTO.Loyality;
import com.sibertech.lib.DTO.LoyalityParam;
import com.sibertech.lib.db.actuator.DtoActuator;
import com.sibertech.lib.conf.DSet;
import com.sibertech.lib.conf.ConfDb;
import java.util.HashMap;
import java.util.Map;

public class GenAuxLoyalities {
    
     protected DtoActuator actuator = new DtoActuator();
    
    public void generate (DSet dSet) throws Exception {
        dSet.setLoyalityParamsMap(this.genLoyalityParams());
        dSet.setLoyalitisMap(genLoyalities(dSet));  
    }

    protected Map<Integer, LoyalityParam> genLoyalityParams () {
        
        Map<Integer, LoyalityParam> loyParamsMap = new HashMap<>();        
        for(int iLoyParam=1; iLoyParam <= ConfDb.COUNT_OF_LOYALITY_PARAMS; iLoyParam++) {            
            LoyalityParam loyParam = new LoyalityParam(iLoyParam, actuator.getCoeff(Math.random()));
            loyParamsMap.put(iLoyParam, loyParam);
        }        
        return loyParamsMap;
    }
    
    protected Map<Integer, Loyality> genLoyalities (DSet dSet) throws Exception {
        
        Map<Integer, Loyality> loyMap = new HashMap<>();
        int loyMapRunner = 1;        
        /**
         * элементов в LoyalityParam в 10 раз больше, чем нужно для заполнения Loyality по всем регионам.
         * Loyality будем присваивать ссылку на LoyalityParam, шагая по LoyalityParam с шагом 3.*/
        int loyality_params_id = 1;
        int category_id = 1;
        
        for(int iReg=1; iReg <= ConfDb.COUNT_OF_REGIONS; iReg++) {            
            for(int iLoy=1; iLoy <= ConfDb.COUNT_OF_LOYALITY_IN_REGION; iLoy++) {
            
                category_id = actuator.getRandomProductCategryID();
                loyality_params_id += 3; // шагаем с шагом 3            
                Loyality loy = new Loyality(loyMapRunner, iReg, category_id, loyality_params_id, actuator.getCoeff(Math.random()));

                loyMap.put(loyMapRunner++, loy);
            }
        }        
        return loyMap;
    }
}
