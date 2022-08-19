package com.busra.connecting.config.web;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.embedded.netty.NettyServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorResourceFactory;
import reactor.netty.http.server.HttpServer;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebServerFactory {
    @Bean
    public NioEventLoopGroup eventLoopGroup() {
        ThreadFactory threadFactory = runnable -> new Thread(runnable, "Netty Thread");
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                12,
                12,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                threadFactory,
                new ThreadPoolExecutor.AbortPolicy());
        NioEventLoopGroup eventLoopGroup= new NioEventLoopGroup(12, executor);
        eventLoopGroup.setIoRatio(100);
        return eventLoopGroup;
    }
    @Bean
    public ReactorResourceFactory resourceFactory(NioEventLoopGroup eventLoopGroup) {
        ReactorResourceFactory reactorResourceFactory = new ReactorResourceFactory();
        reactorResourceFactory.setLoopResources(useNative -> eventLoopGroup);
        reactorResourceFactory.setUseGlobalResources(false);
        return reactorResourceFactory;
    }
    @Bean
    public NettyReactiveWebServerFactory nettyReactiveWebServerFactory(NioEventLoopGroup eventLoopGroup) {
        NettyReactiveWebServerFactory webServerFactory = new NettyReactiveWebServerFactory();
        webServerFactory.addServerCustomizers(new EventLoopNettyCustomizer(eventLoopGroup));
        return webServerFactory;
    }

    private static class EventLoopNettyCustomizer implements NettyServerCustomizer {
        final NioEventLoopGroup eventLoopGroup;
        public EventLoopNettyCustomizer(NioEventLoopGroup eventLoopGroup) {
            this.eventLoopGroup = eventLoopGroup;
        }

        @Override
        public HttpServer apply(HttpServer httpServer) {

            EventLoopGroup parentGroup = new NioEventLoopGroup();
//            EventLoopGroup childGroup = new NioEventLoopGroup();
            this.eventLoopGroup.register(new NioServerSocketChannel());
//            ServerBootstrap serverBootstrap = new ServerBootstrap();
//            serverBootstrap.group(parentGroup, childGroup)
//                    .channel(NioServerSocketChannel.class);
            return httpServer.runOn(parentGroup);
//                    .runOn(LoopResources.create("soul-netty", 1, 8, true), false)
//                    .option(ChannelOption.SO_BACKLOG, 1024)
//                    .option(ChannelOption.SO_REUSEADDR, true)
//                    .option(ChannelOption.TCP_NODELAY, true)
//                    .option(ChannelOption.SO_KEEPALIVE, true)
//                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

//                    .tcpConfiguration(tcpServer -> tcpServer.bootstrap(
//                            serverBootstrap -> serverBootstrap.group(parentGroup, childGroup).channel(NioServerSocketChannel.class)
//                    ));
        }
    }
}
