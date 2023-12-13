package mao.uniappnetworkrequestserver.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.annotation.PostConstruct;

/**
 * Project name(项目名称)：uniapp-network-request-server
 * Package(包名): mao.uniappnetworkrequestserver.config
 * Class(类名): WebSocketConfig
 * Author(作者）: mao
 * Author QQ：1296193245
 * GitHub：https://github.com/maomao124/
 * Date(创建日期)： 2023/12/13
 * Time(创建时间)： 11:47
 * Version(版本): 1.0
 * Description(描述)： websocket配置
 */

@Slf4j
@Configuration
@EnableWebSocket
public class WebSocketConfig
{


    /**
     * ServerEndpointExporter
     *
     * @return {@link ServerEndpointExporter}
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter()
    {
        return new ServerEndpointExporter();
    }

    @PostConstruct
    public void init()
    {
        log.info("初始化 WebSocketConfig");
    }
}
