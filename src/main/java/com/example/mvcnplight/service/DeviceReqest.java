package com.example.mvcnplight.service;

import com.example.mvcnplight.command.Command;
import com.example.mvcnplight.repository.CommandRepository;
import com.google.common.base.Splitter;
import io.netty.channel.Channel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Data
@Slf4j
public class DeviceReqest {

//    @Autowired
//    private ChannelRepository channelRepository;


    private String address,imei;
    private LinkedBlockingQueue<String> messagesQueue = new LinkedBlockingQueue<>();
    private List<Command> commands;
    private Channel channel;
    private String commandString;
    private int timeout = 10;
    private String suffix = "\r\n";
    private CommandRepository commandRepository;
    private TimeUnit timeUnit = TimeUnit.SECONDS;


    public DeviceReqest(String address, String imei) {
        this.address = address;
        this.imei = imei;
    }

    public static DeviceReqest of(String imei, String address){
        return new DeviceReqest(address,imei);
    }

    public DeviceReqest channel(Channel channel){
        this.channel=channel;
        return this;
    }

    public DeviceReqest commands(List<Command> commands){
        this.commands=commands;
        commandString= commands.stream()
                .map(x-> x.getCommand())
                .collect(Collectors.joining("","#M#",suffix));
        return this;
    }
    public DeviceReqest commands(Command command){
        List<Command> temp = new ArrayList<>();
        temp.add(command);
        return commands(temp);
    }

    public DeviceReqest commandRepository(CommandRepository commandRepository){
        this.commandRepository=commandRepository;
        return this;
    }

    public DeviceReqest timeout(int timeout){
        this.timeout = timeout;
        return this;
    }
    public DeviceReqest timeout(int timeout,TimeUnit timeUnit){
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        return this;
    }
    public DeviceReqest suffix(String suffix){
        this.suffix =suffix;
        return this;
    }

    public List<String> send() throws TimeoutException {
//        channel = channelRepository.getChannelCache().get(imei);
//        if (channel ==null) throw new TimeoutException();

        List<String> result = new ArrayList<>();
        boolean semFlag = false;
        try {
            commandRepository.addQueue(imei, messagesQueue);
            semFlag =true;
            channel.writeAndFlush(commandString);
            log.info("[Write] "+commandString);
            while (commands.size() > 0) {

                var respone = messagesQueue.poll(timeout, timeUnit);
                if (respone == null) throw new Exception();
                result.add(respone);
                log.info("[Recive] "+respone);
                commands = commands.stream()
                        .filter(x -> !x.review(respone))
                        .collect(Collectors.toList());

            }
        } catch (Exception exception) {
            log.debug("Exception");
        } finally {
           if(semFlag) commandRepository.removeQueue(imei,messagesQueue);
        }
        return result.stream()
                .flatMap(x-> Splitter.on(";").trimResults().splitToStream(x))
                .filter(s->!s.isEmpty())
                .collect(Collectors.toList());

    }





}
