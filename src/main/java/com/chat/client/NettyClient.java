package com.chat.client;

import com.alibaba.fastjson.JSONObject;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * @author y19477
 */
public class NettyClient {
    private static final Logger log = LoggerFactory.getLogger(NettyClient.class);

    private static Bootstrap b;
    private static ChannelFuture f;
    private static final EventLoopGroup WORKER_GROUP = new NioEventLoopGroup();
    public static Channel channel = null;

    static {
        log.info("init...");
        b = new Bootstrap();
        b.group(WORKER_GROUP);
        b.channel(NioSocketChannel.class);
        b.option(ChannelOption.SO_KEEPALIVE, true);
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) {
                // 解码编码
                socketChannel.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));
                socketChannel.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
                socketChannel.pipeline().addLast(new ClientHandler());
            }
        });
    }

    /**
     * 启动客户端
     *
     * @param address
     */
    public static void start(InetSocketAddress address) {
        f = b.connect(address);
        f.addListener((ChannelFutureListener) channelFuture -> {
            if (f.isSuccess()) {
                channel = f.channel();
                //auth 发送个人信息
                JSONObject auth = new JSONObject();
                auth.put("userId", "");
                auth.put("type", "0");
                channel.writeAndFlush(auth.toJSONString());
                log.info("Connect to server successfully!");
                return;
            } else {
                log.info("Failed to connect to server, try connect after 5s");
                f.channel().eventLoop().schedule(() -> start(new InetSocketAddress("127.0.0.1", 9527)), 5, TimeUnit.SECONDS);
            }
        });
    }

    public static void main(String[] args) {

        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 9527);
        try {
            start(address);
            while (true) {
                System.out.print("say something:");
                Scanner scanner = new Scanner(System.in);
                String s = scanner.nextLine();
                if (!StringUtils.isEmpty(s)) {
                    if (channel != null) {
                        channel.writeAndFlush(s).sync();
                    }
                }
            }

        } catch (Exception e) {
            log.error("", e);

        } finally {
            if (null != f) {
                f.channel().close();
            }
        }
    }
}
