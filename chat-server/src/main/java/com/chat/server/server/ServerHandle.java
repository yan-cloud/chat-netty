package com.chat.server.server;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author y19477
 */
public class ServerHandle extends SimpleChannelInboundHandler<Object> {
    public static final Map<String, Channel> CLIENTS = new ConcurrentHashMap<>();
    private Logger log = LoggerFactory.getLogger(ServerHandle.class);
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) {
        JSONObject message = (JSONObject)JSONObject.parse((String)o);
        String type = message.getString("type");
        if("0".equals(type)){
            //auth  鉴权
            log.info("{},鉴权成功",message.getString("userId"));
            CLIENTS.put(message.getString("userId"),channelHandlerContext.channel());
        }else if("1".equals(type)){
            Channel sendTo = CLIENTS.get(message.getString("to"));
            if(sendTo.isActive()){
                sendTo.writeAndFlush(message.getString("message"));
            }
        }
        channelHandlerContext.flush();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelActive();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            ctx.write("heartbeat zzzZZZZ");
            ctx.flush();
        }
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端接入："+ctx.channel().remoteAddress().toString());
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端断开："+ctx.channel().remoteAddress().toString());
        CLIENTS.remove(ctx.channel().remoteAddress().toString());
        super.channelUnregistered(ctx);
    }
}
