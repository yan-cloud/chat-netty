package com.chat.chatnetty.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author y19477
 */
public class ServerHandle extends SimpleChannelInboundHandler<Object> {
    public static final Map<String, Channel> CLIENTS = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        CLIENTS.keySet().stream().filter(key->!channelHandlerContext.channel().remoteAddress().toString().equals(key)).forEach(address->{
            Channel channel = CLIENTS.get(address);
            if(channel.isActive()){
                channel.writeAndFlush(now+ "  "+channelHandlerContext.channel().remoteAddress().toString()+" say "+ o);
            }
        });
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
        System.out.println("客户端接入："+ctx.channel().remoteAddress().toString());
        CLIENTS.put(ctx.channel().remoteAddress().toString(),ctx.channel());
        CLIENTS.keySet().stream().forEach(address->{
            Channel channel = CLIENTS.get(address);
            if(channel.isActive()){
                channel.writeAndFlush("欢迎："+ctx.channel().remoteAddress().toString()+"加入聊天");
            }
        });
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端断开："+ctx.channel().remoteAddress().toString());
        CLIENTS.remove(ctx.channel().remoteAddress().toString());
        super.channelUnregistered(ctx);
    }
}
