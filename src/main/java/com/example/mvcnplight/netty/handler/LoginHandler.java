package com.example.mvcnplight.netty.handler;

import com.example.mvcnplight.model.Device;
import com.example.mvcnplight.repository.ChannelRepository;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;



@Component
@Slf4j
@RequiredArgsConstructor
@ChannelHandler.Sharable
public class LoginHandler extends ChannelInboundHandlerAdapter {
    private final ChannelRepository channelRepository;
//    private final DeviceService deviceService;
//    private final EventMessageSender eventMessageSender;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof String) || !((String) msg).startsWith("#L#")) {
            ctx.fireChannelRead(msg);
            return;
        }
        String stringMessage = (String) msg;
        if (log.isDebugEnabled()) {
            log.debug(stringMessage);
        }
        try {
            Device device = Device.of(stringMessage,ctx.channel());
            device.login(channelRepository,ctx.channel());
            ctx.writeAndFlush("#AL#1\r\n");
            log.debug("Successfully logged in as {} : {} : {}", device.getImei(),device.getSw(),device.getMcu());
//            deviceService.saveDevice(device,"LOGIN");
//            deviceService.loginDev(device,"LOGIN");
        } catch (IllegalArgumentException e) {
            ctx.writeAndFlush("#AL#0\r\n");
        }
//       eventMessageSender.sendEvent(new EventMessage("ADD",device));

    }
}
