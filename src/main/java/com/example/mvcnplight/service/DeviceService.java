package com.example.mvcnplight.service;

import com.example.mvcnplight.command.Command;
import com.example.mvcnplight.command.CustomCommand;
import com.example.mvcnplight.command.SetColor;
import com.example.mvcnplight.entity.DeviceEntity;
import com.example.mvcnplight.model.CellModel;
import com.example.mvcnplight.model.WareHouseModel;
import com.example.mvcnplight.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeviceService {
    private final DeviceRepository deviceRepository;
    private final CommandRepository commandRepository;
    private final MockDeviceRepository mockDeviceRepository;
    private final ChannelRepository channelRepository;
    private final UpLedRepository upLedRepository;
    private final ShelvingRepository shelvingRepository;
    private List<DeviceEntity> devices = new ArrayList<>();

    //    @PostConstruct
    public void init() {
//        devices.add(new DeviceEntity("860906042972288","Hulk_hv1","B.09.23"));
//        devices.add(new DeviceEntity("866897051076364","Hulk_hv1","B.09.23"));
//        devices.add(new DeviceEntity("866897050105321","Hulk_hv1","Q.11.23"));
//        devices.add(new DeviceEntity("860906042972281","Hulk_hv1","B.09.23"));
//        devices.add(new DeviceEntity("860906042972282","Hulk_hv1","B.09.23"));
//        devices.add(new DeviceEntity("860906042972283","Hulk_hv1","B.09.23"));
//        deviceRepository.save(new DeviceEntity("860906042972288", "Hulk_hv1", "B.09.23"));
//        deviceRepository.save(new DeviceEntity("866897051076364", "Hulk_hv1", "B.09.23"));
//        deviceRepository.save(new DeviceEntity("866897050105321", "Hulk_hv1", "Q.11.23"));
//        deviceRepository.save(new DeviceEntity("860906042972281", "Hulk_hv1", "B.09.23"));
//        deviceRepository.save(new DeviceEntity("860906042972282", "Hulk_hv1", "B.09.23"));
//        deviceRepository.save(new DeviceEntity("860906042972283", "Hulk_hv1", "B.09.23"));
    }

    public List<DeviceEntity> index() {
        return deviceRepository.findAll();
//        return devices;
    }

    public DeviceEntity show(String id) {
        return deviceRepository.findById(id).orElse(null);
//        return devices.stream().filter(x->x.getImei().equals(id)).findAny().orElse(null);
    }

    public DeviceEntity create(String imei, String hw, String sw) {
        return deviceRepository.save(new DeviceEntity(imei, hw, sw));
    }

    @SneakyThrows
    public List<String> setColor(CellModel cell, String imei) {

        log.info("setColor {}", cell);
        String color = mockDeviceRepository.getColor(cell.getColor(), cell.getState());
        String adr = mockDeviceRepository.getCellNum(cell.getCell());
        String curCell = String.valueOf(cell.getColor());

        List<Command> commands = List.of(new SetColor(adr, color, curCell));

        var channel = channelRepository.getChannelCache().get(imei);

        return DeviceReqest.of(imei, "00")
                .commandRepository(commandRepository)
                .channel(channel)
//                .suffix("saveparam\r\n")
                .timeout(5)
                .commands(commands)
                .send();

    }

    @SneakyThrows
    public List<String> setColor(List<CellModel> cells, String host) {

        Map<String, List<CellModel>> sortedCells = sortCells(cells, host);
        List<String> result = new ArrayList<>();

        sortedCells.entrySet().stream().forEach(x -> log.info("sortedCells {}", x.getKey()));

        sortedCells.entrySet().parallelStream().forEach(lc -> {

            List<Command> commands = lc.getValue().stream()
                    .map(c -> {
                        List<SetColor> temp = new ArrayList<>();
                        String color = mockDeviceRepository.getColor(c.getColor(), c.getState());
                        String adr = mockDeviceRepository.getCellNum(host + c.getCell());
                        String curCell = String.valueOf(c.getColor());
                        if (adr != null && color != null){
                            return new SetColor(adr, color, curCell);
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            try {
                var channel = channelRepository.getChannelCache().get(lc.getKey());
                if (channel != null) {
                    log.info("[Write to Channel ] {} : {}", lc.getKey(), channel.id().asShortText());
                    result.addAll(DeviceReqest.of(lc.getKey(), "00")
                            .commandRepository(commandRepository)
                            .channel(channel)
                            //                .suffix("saveparam\r\n")
                            .timeout(5)
                            .commands(commands)
                            .send());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return result;

//        var channel = channelRepository.getChannelCache().get("429248");
//
//        return DeviceReqest.of("429248", "00")
//                .commandRepository(commandRepository)
//                .channel(channel)
////                .suffix("saveparam\r\n")
//                .timeout(5)
//                .commands(commands)
//                .send();

    }


    public void control(WareHouseModel wareHouse) {


        wareHouse.getData()
                .stream()
                .forEach(hostModel -> setColor(hostModel.getCells(), hostModel.getHost()));

        wareHouse.getData()
                .stream()
                .forEach(hostModel -> {

                    shelvingRepository.updateShelving(hostModel).forEach(x->{
                        log.info("[SET UP LED ] {}", x.toString());
                        var imei = getImeiByCell(hostModel.getHost());
                        setColor(x, imei);
                    });

//                    try {
//                        var imei = getImeiByCell(hostModel.getHost());
//                        var temp= upLedRepository.updateUpLed(hostModel);
//                        if(temp!=null){
//
//                            log.info("[SET UP LED ] {}", temp.toString());
//    //                        setColor(temp, imei);
//                        }
//                    } catch (Exception exception) {
//                        log.info("[SET UP LED ] ERROR");
//
//                    }
                });
    }

    @SneakyThrows
    public List<String> getCustom(String imei, String command, String stopMessage, Integer timeout) {
//        var channel = channelRepository.getChannelCache().get(imei);

        var curCommand = new CustomCommand();
        curCommand.setCommand(command);
        curCommand.setStopMessage(stopMessage);

        List<Command> commands = List.of(curCommand);
        var channel = channelRepository.getChannelCache().get(imei);

        return DeviceReqest.of(imei, "00")
                .commandRepository(commandRepository)
                .channel(channel)
                .timeout(timeout)
                .commands(commands)
                .send()
                .stream()
                .filter(s -> !s.contains("#M#"))
                .collect(Collectors.toList());
    }

    @SneakyThrows
    public void bingo(Integer color) {
//        for (int i = 1; i < 128 ; i++) {
//            setColor(new CellModel("00",1,0));
//            setColor(new CellModel(String.format("011A%02d",i),color,1));
//            Thread.sleep(5);
//
//        }

    }

    public Map<String, String> getCellMap() {
        return mockDeviceRepository.getCellMap();
    }

    public Map<String, String> addCell(String name, String address, String imei) {
        return mockDeviceRepository.addCell(name, address, imei);
    }

    public Map<String, String> addCellMap(String imei, Map<String, String> cells) {
        return mockDeviceRepository.addCells(imei, cells);
    }


    public Map<String, String> dellCell(String name, String address, String imei) {
        return mockDeviceRepository.dellCell(name, address, imei);
    }


    @Async
    @SneakyThrows
    public void testCell(String name) {
        var imei = getImeiByCell(name);

        setColor(new CellModel(name, 1, 1), imei);
        Thread.sleep(2000);
        setColor(new CellModel(name, 1, 0), imei);
//
//
//        for (int i = 0; i <4; i++) {
//            setColor(new CellModel("00",1,0));
//            Thread.sleep(50);
//            setColor(new CellModel(name,i,1));
//            Thread.sleep(1000);
//        }

    }

    @SneakyThrows
    public void reboot(String id) {
        var curCommand = new CustomCommand();
        curCommand.setCommand("S 100");
        curCommand.setStopMessage("S");
        List<Command> commands = List.of(curCommand);
        var channel = channelRepository.getChannelCache().get(id);
        DeviceReqest.of(id, "00")
                .commandRepository(commandRepository)
                .channel(channel)
                .timeout(1)
                .commands(commands)
                .send();
    }

    private Map<String, List<CellModel>> sortCells(List<CellModel> cells, String host) {
        var temp = cells.stream()
                .collect(Collectors.groupingBy(k -> getImeiByCell(host + k.getCell())));
        return temp;
    }

    private String getImeiByCell(String name) {
        return mockDeviceRepository.findImeiByCellName(name);
    }

    public List<String> getControllers(){
        return mockDeviceRepository.findAllDev();
    }


}
