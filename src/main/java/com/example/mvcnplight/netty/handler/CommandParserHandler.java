package com.example.mvcnplight.netty.handler;

import com.example.mvcnplight.exception.DeviceLoggedOutException;
import com.example.mvcnplight.model.Device;
import com.example.mvcnplight.repository.ChannelRepository;
import com.example.mvcnplight.repository.CommandRepository;
import com.example.mvcnplight.service.DeviceService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;



@Component
@Slf4j
@RequiredArgsConstructor
@ChannelHandler.Sharable
public class CommandParserHandler extends ChannelInboundHandlerAdapter {

    private final ChannelRepository channelRepository;
    private final CommandRepository commandRepository;
    private final DeviceService deviceService;


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Assert.notNull(this.channelRepository, "[Assertion failed] - ChannelRepository is required; it must not be null");
        ctx.fireChannelActive();
        if (log.isDebugEnabled()) {
            log.debug(ctx.channel().remoteAddress() + "");
        }
        if (log.isDebugEnabled()) {
            log.debug("Bound Channel Count is {}", this.channelRepository.size());
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String stringMessage = (String) msg;
        if (log.isDebugEnabled()) {
            log.debug(stringMessage);
        }
        if (stringMessage.equals("#P#")) {
            ctx.channel().writeAndFlush("#AP#\r\n");
            return;
        }
//        if (stringMessage.equals("#AM#1")) {
//            return;
//        }

        if (stringMessage.startsWith("#L#")) {
            ctx.fireChannelRead(msg);
            return;
        }

        try {
            Device device = Device.current(ctx.channel());
            log.info(device.getImei() + ":" + stringMessage);
            commandRepository.addMessage(device.getImei(),stringMessage);

        } catch (DeviceLoggedOutException e) {
            ctx.disconnect();
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error(cause.getMessage(), cause);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Assert.notNull(this.channelRepository, "[Assertion failed] - ChannelRepository is required; it must not be null");
        Assert.notNull(ctx, "[Assertion failed] - ChannelHandlerContext is required; it must not be null");
        try {
            Device device = Device.current(ctx.channel());

            if (device.logout(this.channelRepository, ctx.channel())) {
//                deviceService.saveDevice(device,"LOGOUT");
//                deviceService.saveEvent(device,"LOGOUT");
                log.info("channelInactive "+device.getImei());
//                eventMessageSender.sendEvent(new EventMessage("DELETE",device));
            }

            if (log.isDebugEnabled()) {
                log.debug("Channel Count is " + this.channelRepository.size());
            }
        } catch (Exception exception) {
            log.debug("[Unknown client] channelInactive");
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.READER_IDLE) {
                ctx.close();
            } else if (e.state() == IdleState.WRITER_IDLE) {
                ctx.writeAndFlush("PING");
            }
        }
    }


}

