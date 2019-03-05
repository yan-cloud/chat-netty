package com.chat.client.client;

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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @author ywd
 */
public class NettyClient {
    private static final Logger log = LoggerFactory.getLogger(NettyClient.class);

    private static Bootstrap bootstrap;
    private static ChannelFuture channelFuture;
    private static final EventLoopGroup WORKER_GROUP = new NioEventLoopGroup();
    public static Channel channel = null;
    public static String host;
    public static int port;

    static {
        log.info("init netty client...");

        Properties properties = new Properties();
        try {
            properties.load(NettyClient.class.getClassLoader().getResourceAsStream("netty.properties"));
            host = properties.getProperty("netty.server.host");
            port = Integer.valueOf(properties.getProperty("netty.server.port"));
        } catch (IOException e) {
            log.info(" init netty.properties error", e);
        }

        bootstrap = new Bootstrap();
        bootstrap.group(WORKER_GROUP)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
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
        channelFuture = bootstrap.connect(address);
        channelFuture.addListener((ChannelFutureListener) channelFuture -> {
            if (NettyClient.channelFuture.isSuccess()) {
                log.info("Connect to server successfully!");
            } else {
                log.info("Failed to connect to server, try connect after 5s");
                NettyClient.channelFuture.channel().eventLoop().schedule(() -> start(new InetSocketAddress(host, port)), 5, TimeUnit.SECONDS);
            }
        });
    }
}
