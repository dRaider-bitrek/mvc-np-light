package com.example.mvcnplight.repository;

import com.example.mvcnplight.entity.ShelvingEntity;
import com.example.mvcnplight.model.CellModel;
import com.example.mvcnplight.model.HostModel;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ShelvingRepository {
    Map<String, ShelvingEntity> moc = new ConcurrentHashMap<>();

    @PostConstruct
    public void init(){
        ShelvingEntity shelvingEntity1 = new ShelvingEntity("025");
        shelvingEntity1.addShelfColor("A01");
        shelvingEntity1.addShelfColor("A02");
        shelvingEntity1.addShelfColor("A03");
        shelvingEntity1.addShelfColor("A04");
        shelvingEntity1.addShelfColor("A05");

        shelvingEntity1.addShelfColor("A06");
        shelvingEntity1.addShelfColor("A07");
        shelvingEntity1.addShelfColor("A08");
        shelvingEntity1.addShelfColor("A09");
        shelvingEntity1.addShelfColor("A10");
        shelvingEntity1.addShelfColor("A11");
        shelvingEntity1.addShelfColor("B07");
        shelvingEntity1.addShelfColor("B06");
        shelvingEntity1.addShelfColor("B05");
        shelvingEntity1.addShelfColor("B04");
        shelvingEntity1.addShelfColor("B03");
        shelvingEntity1.addShelfColor("B02");

        moc.put("025",shelvingEntity1);

        ShelvingEntity shelvingEntity2 = new ShelvingEntity("026");
        shelvingEntity2.addShelfColor("A01");
        shelvingEntity2.addShelfColor("A02");
        shelvingEntity2.addShelfColor("A03");
        shelvingEntity2.addShelfColor("A04");
        shelvingEntity2.addShelfColor("A05");

        shelvingEntity2.addShelfColor("A06");
        shelvingEntity2.addShelfColor("A07");
        shelvingEntity2.addShelfColor("A08");
        shelvingEntity2.addShelfColor("A09");
        shelvingEntity2.addShelfColor("A10");
        shelvingEntity2.addShelfColor("A11");
        shelvingEntity2.addShelfColor("A12");
        shelvingEntity2.addShelfColor("A13");
        shelvingEntity2.addShelfColor("A14");
        shelvingEntity2.addShelfColor("B10");
        shelvingEntity2.addShelfColor("B09");
        shelvingEntity2.addShelfColor("B08");
        shelvingEntity2.addShelfColor("B07");
        shelvingEntity2.addShelfColor("B06");
        shelvingEntity2.addShelfColor("B05");
        shelvingEntity2.addShelfColor("B04");
        shelvingEntity2.addShelfColor("B03");
        shelvingEntity2.addShelfColor("B02");

        moc.put("026",shelvingEntity2);
    }


    public List<CellModel> updateShelving(HostModel hostModel) {
        List result = new ArrayList();
        if (moc.containsKey(hostModel.getHost())) {
            var shelving = moc.get(hostModel.getHost());
            shelving.updateShelf(hostModel);
            result.add(shelving.getState(1));
            result.add(shelving.getState(2));
            result.add(shelving.getState(3));
            result.add(shelving.getState(4));
        }
//        if(hostModel.getHost().equals("025")|| hostModel.getHost().equals("026")){
//            var shelving = moc.get("STR");
//            shelving.updateShelf(hostModel);
//
//
//        }


        return result;
    }

}
