package com.example.mvcnplight.entity;

import com.example.mvcnplight.model.CellModel;
import com.example.mvcnplight.model.HostModel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Slf4j
public class ShelvingEntity {
    private String host;
    Map<String, Map<Integer, Integer>> onShelfs = new ConcurrentHashMap<>();

    public ShelvingEntity(String host) {
        this.host = host;
    }

    public CellModel getState(int color) {

        var temp = onShelfs.entrySet().stream()
                .map(x -> x.getValue())
                .map(y -> y.entrySet().stream()
                        .filter(s -> s.getKey().equals(color))
                        .map(d -> d.getValue())
                )
                .flatMap(h -> h)
                .reduce(0, (subtotal, element) -> subtotal + element);

        return new CellModel(host, color, temp>0?1:0);

    }

    public void addShelfColor(String shelf) {

        if (!onShelfs.containsKey(shelf)) {
            onShelfs.put(shelf, new ConcurrentHashMap<>());
        }
        var temp = onShelfs.get(shelf);
        temp.put(1, 0);
        temp.put(2, 0);
        temp.put(3, 0);
        temp.put(4, 0);
        onShelfs.put(shelf,temp);
    }

    public void updateShelf(HostModel hostModel) {
        hostModel.getCells().stream()
                .forEach(x -> {
                    if (onShelfs.containsKey(x.getCell())) {
                        onShelfs.get(x.getCell()).put(x.getColor()+1, x.getState());
                    }
                });
    }

}
