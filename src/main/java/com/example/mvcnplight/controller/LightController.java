package com.example.mvcnplight.controller;

import com.example.mvcnplight.model.Device;
import com.example.mvcnplight.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/device")
@RequiredArgsConstructor
public class LightController {
    private final DeviceService deviceService;

    @GetMapping()
    public String index(Model model){
        model.addAttribute("devices", deviceService.index());
        model.addAttribute("device", new Device());
        return "device/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") String id,
                       Model model){
        model.addAttribute("device",deviceService.show(id));

        return "device/show";
    }

    @PostMapping()
    public String create(@ModelAttribute("device") Device device,
                         Model model){
        model.addAttribute("device", deviceService.create(device.getImei(),device.getHw(),device.getSw()));
        model.addAttribute("devices", deviceService.index());

        return "device/index";
    }

//    @PostMapping()
//    public String create(@RequestParam("imei") String imei,
//                         @RequestParam("hw") String hw,
//                         @RequestParam("sw") String sw,
//                         Model model){
//        model.addAttribute("device", deviceService.create(imei,hw,sw));
//
//        return "successPage";
//    }

}
