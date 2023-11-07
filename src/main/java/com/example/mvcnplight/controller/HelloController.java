package com.example.mvcnplight.controller;

import com.example.mvcnplight.model.Device;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class HelloController {

    @GetMapping("/hello")
    public String sayHello(@RequestParam(value = "name",required = false) String name,
                           @RequestParam (value = "surname",required = false ) String surname,
                           Model model){

        model.addAttribute("message","Hello "+name+" "+surname);
        log.info("Hello {} {}",name,surname);
        List<Device> devices = new ArrayList<>();
        devices.add(new Device("860906042972288","Hulk_hv1","B.09.23"));
        devices.add(new Device("866897051076364","Hulk_hv1","B.09.23"));
        devices.add(new Device("866897050105321","Hulk_hv1","Q.11.23"));
        devices.add(new Device("860906042972288","Hulk_hv1","B.09.23"));
        devices.add(new Device("860906042972288","Hulk_hv1","B.09.23"));
        devices.add(new Device("860906042972288","Hulk_hv1","B.09.23"));
        model.addAttribute("devices",devices);
        return "hello_world";
    }

    @GetMapping("/goodbye")
    public String goodBye(){
        return "good_bye";
    }

}
