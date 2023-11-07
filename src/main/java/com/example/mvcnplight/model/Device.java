package com.example.mvcnplight.model;

import com.example.mvcnplight.exception.DeviceLoggedOutException;
import com.example.mvcnplight.repository.ChannelRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Device implements Serializable {
    @JsonIgnore
    public static final AttributeKey<Device> DEVICE_ATTRIBUTE_KEY = AttributeKey.newInstance("DEVICE");

    private String imei;
    private String password;
    private String sw= "";
    private String hw = "";
    private String mcu = "";

    public Device(String imei, String password, String sw, String hw, String mcu) {
        this.imei = imei;
        this.password = password;
        this.sw = sw;
        this.hw = hw;
        this.mcu = mcu;
    }
    public Device(String imei, String hw, String sw) {
        this.imei = imei;
        this.sw = sw;
        this.hw = hw;

    }

    public static Device of(@NonNull String loginCommand, @NonNull Channel channel) throws IllegalArgumentException{
        try {
            loginCommand = loginCommand.replaceAll("#L#","");
            String[] decodStr =  loginCommand.split(";");
            String imei = decodStr[0];
//            String hw = decodStr[1];
//            String sw = decodStr[2];
            String password = decodStr[1];
//            String mcu = decodStr[5];
            return new Device(imei,password,"sw","hw","mcu");
        } catch (Exception exception) {
            throw new IllegalArgumentException("loginCommand ["+loginCommand+"] can not be accepted");
        }
    }
    public void login(ChannelRepository channelRepository, Channel channel) {
        channel.attr(DEVICE_ATTRIBUTE_KEY).set(this);
        channelRepository.put(this.imei, channel);
    }

    public boolean logout(ChannelRepository channelRepository, Channel channel) {
        channel.attr(DEVICE_ATTRIBUTE_KEY).getAndSet(null);
       return channelRepository.remove(this.imei, channel);
    }

    public static Device current (Channel channel) throws DeviceLoggedOutException {
        Device device =channel.attr(DEVICE_ATTRIBUTE_KEY).get();
        if(device == null) {
            throw new DeviceLoggedOutException();
        }
        return device;
    }

    @Override
    public String toString() {
        return "Device{" +
                "imei='" + imei + '\'' +
                ", password='" + password + '\'' +
                ", sw='" + sw + '\'' +
                ", hw='" + hw + '\'' +
                ", mcu='" + mcu + '\'' +
                '}';
    }
}
