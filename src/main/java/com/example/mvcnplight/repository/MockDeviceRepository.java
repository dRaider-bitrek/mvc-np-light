package com.example.mvcnplight.repository;

import com.example.mvcnplight.entity.CellAddressEntity;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class MockDeviceRepository {
    private final CellAddressRepository cellAddressRepository;
    private final HostRepository hostRepository;

    Map<String,String> cellMap = new HashMap<>();
    Map<Integer,String> colorMap = new HashMap<>();

    @PostConstruct
    public void init(){



        cellMap.put("RESET","0");
        cellMap.put("011A01","1");
        cellMap.put("011A02","2");
        cellMap.put("011A03","3");
        cellMap.put("011A04","4");
        cellMap.put("011A05","5");
        cellMap.put("011A06","6");
        cellMap.put("011A07","7");
        cellMap.put("011A08","8");
        cellMap.put("011A09","9");

        for (int i = 10; i <300 ; i++) {
            cellMap.put("011A"+i,String.valueOf(i));
        }

        colorMap.put(0,"FF0000");
        colorMap.put(1,"00FFFF");
        colorMap.put(2,"00FF00");
        colorMap.put(3,"FFFF00");
        colorMap.put(4,"ADD8E6");//Lightblue
        colorMap.put(5,"FFA500");//Orange
        colorMap.put(6,"800080");//Purple
        colorMap.put(7,"FFFFFF");///White





//        colorMap.put(4,"FFFF00");

    }

    public String getColor(int code, int state){

        if(!colorMap.containsKey(code)) return null;
        if (state == 0) return "000000";
        return colorMap.get(code);
    }

    public String getCellNum(String address ){
      var optional=  cellAddressRepository.findById(address);

              if(optional.isPresent()) return optional.get().getAddress();
              return null;
//        return cellMap.getOrDefault(address,"");

    }

    public Map<String,String> getCellMap(){
       return cellAddressRepository.findAll().stream()
                .collect(Collectors.toMap(x->x.getCell(),x->x.getAddress()));
//        return cellMap;
    }
    public Map<String,String> addCell(String name,String address,String imei){
        cellAddressRepository.save(new CellAddressEntity(name,address,imei));
        return getCellMap();
//        cellMap.put(name,address);
//        return cellMap;
    }

    public Map<String,String> addCells(String imei, Map<String,String> cells){
       var entities= cells.entrySet().stream()
                .map(x->new CellAddressEntity(x.getKey(), x.getValue(),imei))
        .collect(Collectors.toList());
        cellAddressRepository.saveAll(entities);
        return getCellMap();

    }

    public  Map<String,String> dellCell(String name,String address,String imei){
        cellAddressRepository.delete(new CellAddressEntity(name,address,imei));
        return getCellMap();
//        cellMap.remove(name);
//        return cellMap;
    }

    public String findImeiByCellName(String name){
    return cellAddressRepository.findById(name).orElseThrow().getImei();
    }

    public List<String> findAllDev(){
       var result= cellAddressRepository.findAll().stream()
                .collect(Collectors.groupingBy(s->s.getImei()))
                        .entrySet()
                .stream()
                .map(x->x.getKey())
                .collect(Collectors.toList());
       return result;
    }

}
