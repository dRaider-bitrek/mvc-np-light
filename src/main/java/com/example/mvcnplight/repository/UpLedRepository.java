package com.example.mvcnplight.repository;

import com.example.mvcnplight.model.CellModel;
import com.example.mvcnplight.model.HostModel;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Slf4j
public class UpLedRepository {
    Map<String,Long> upLedMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init(){
        upLedMap.put("025",0L);
        upLedMap.put("026",0L);

    }


    public CellModel updateUpLed (HostModel hostModel){
        if(upLedMap.containsKey(hostModel.getHost())){
           var stateOn= hostModel.getCells().stream()
                    .filter(c->c.getState()==1)
                    .count();
            var stateOff= hostModel.getCells().stream()
                    .filter(c->c.getState()==0)
                    .count();
           Long currState =  upLedMap.get(hostModel.getHost());

            log.info("Host: {}, currState: {}, stateOn: {}, stateOff: {}",
                    hostModel.getHost(),
                    currState,
                    stateOn,
                    stateOff);

           currState = currState+stateOn-stateOff>0?(currState+stateOn-stateOff):0L;


            upLedMap.put(hostModel.getHost(),currState);

            if (currState>0) return new CellModel(hostModel.getHost(), 1, 1);
            else return new CellModel(hostModel.getHost(), 1, 0);
        }
        return null;
    }


}
