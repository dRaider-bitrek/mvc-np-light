package com.example.mvcnplight.controller;

import com.example.mvcnplight.model.CellModel;
import com.example.mvcnplight.model.WareHouseModel;
import com.example.mvcnplight.service.DeviceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class ApiController {

    private final DeviceService deviceService;

    @PostMapping("/control")
    public ResponseEntity control(@RequestBody WareHouseModel wareHouse){
        log.info(wareHouse.toString());
        deviceService.control(wareHouse);
        Map<String,String> result = new HashMap<>();
        result.put("status","0");
        result.put("msg","OK");
        return ResponseEntity.ok(result);
    }

    @PostMapping("/cell")
    public ResponseEntity control(@RequestBody CellModel cell){
        log.info(cell.toString());
        deviceService.setColor(cell,"429248");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/custom_command")
    public ResponseEntity<List<String>> cousomCommand(@RequestParam String imei,
                                                      @RequestParam String command,
                                                      @RequestParam String stop,
                                                      @RequestParam Integer timout){

        return new ResponseEntity<>(deviceService.getCustom( imei, command, stop, timout), HttpStatus.OK);
    }
    @GetMapping("/bingo")
    public ResponseEntity bingo(@RequestParam Integer color){
        deviceService.bingo(color);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/cells")
    public ResponseEntity<Map<String,String>> getCellMap(){
        return new ResponseEntity<>(deviceService.getCellMap(), HttpStatus.OK);
    }
    @GetMapping("/addcell")
    public ResponseEntity<Map<String,String>> addCell(@RequestParam(required = false) String imei,
                                                      @RequestParam String name,
                                                      @RequestParam String address){
        return new ResponseEntity<>(deviceService.addCell(name, address,imei!=null?imei:"429248"), HttpStatus.OK);
    }
    @PostMapping("/addcellmap")
    public ResponseEntity<Map<String,String>> addCellMap(@RequestParam(required = false) String imei,
                                                         @RequestBody Map<String,String> cells){
        return new ResponseEntity<>(deviceService.addCellMap(imei,cells), HttpStatus.OK);
    }


    @GetMapping("/delcell")
    public ResponseEntity<Map<String,String>> dellCell(@RequestParam(required = false) String imei,
                                                       @RequestParam String name,
                                                      @RequestParam String address){
        return new ResponseEntity<>(deviceService.dellCell(name, address,imei!=null?imei:"429248"), HttpStatus.OK);
    }

    @GetMapping("/testcell")
    public ResponseEntity testCell(@RequestParam String name){
       deviceService.testCell(name);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/reboot")
    public ResponseEntity reboot(@RequestParam(required = false) String id){
        deviceService.reboot(id!=null?id:"429248");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/controllers")
    public ResponseEntity controllers(){
        return ResponseEntity.ok(deviceService.getControllers());
    }



    @ExceptionHandler({Exception.class})
    public ResponseEntity<Void> timeoutExceptionHandle() {
        return ResponseEntity.status(404).build();
    }

}
