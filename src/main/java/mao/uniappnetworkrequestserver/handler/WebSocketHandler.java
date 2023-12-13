package mao.uniappnetworkrequestserver.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.net.http.WebSocket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Project name(项目名称)：uniapp-network-request-server
 * Package(包名): mao.uniappnetworkrequestserver.handler
 * Class(类名): WebSocketHandler
 * Author(作者）: mao
 * Author QQ：1296193245
 * GitHub：https://github.com/maomao124/
 * Date(创建日期)： 2023/12/13
 * Time(创建时间)： 11:50
 * Version(版本): 1.0
 * Description(描述)： websocket接口
 * 接口路径 ws://localhost:9091/websocket/userId;
 */

@Component
@Slf4j
@ServerEndpoint("/websocket/{userId}")
public class WebSocketHandler
{
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     * 虽然@Component默认是单例模式的，但springboot还是会为每个websocket连接初始化一个bean，所以可以用一个静态set保存起来。
     */
    private static final CopyOnWriteArraySet<WebSocketHandler> webSockets = new CopyOnWriteArraySet<>();

    /**
     * 用来存在线连接用户信息
     */
    private static final ConcurrentHashMap<String, Session> sessionPool = new ConcurrentHashMap<>();

    /**
     * 链接成功调用的方法
     *
     * @param session {@link Session}
     * @param userId  用户id 路径变量
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "userId") String userId)
    {
        try
        {
            synchronized (("lock:WebSocketHandler:onOpen" + userId).intern())
            {
                this.session = session;
                this.userId = userId;
                webSockets.add(this);
                sessionPool.put(userId, session);
                log.info("【websocket消息】有新的连接，当前连接总数为:" + webSockets.size());
            }
        }
        catch (Exception e)
        {
            log.error("连接时发生错误：", e);
        }
    }

    /**
     * 链接关闭调用的方法
     */
    @OnClose
    public void onClose()
    {
        try
        {
            synchronized (("lock:WebSocketHandler:onClose" + userId).intern())
            {
                webSockets.remove(this);
                sessionPool.remove(this.userId);
                log.info("【websocket消息】连接断开，当前连接总数为:" + webSockets.size());
            }
        }
        catch (Exception e)
        {
            log.error("断开连接时发生错误：", e);
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message {@link Session}
     */
    @OnMessage
    public void onMessage(String message)
    {
        log.info("【websocket消息】收到客户端[" + this.userId + "]的消息:" + message);
    }

    /**
     * 发送错误时的处理
     *
     * @param session {@link Session}
     * @param error   {@link Throwable}
     */
    @OnError
    public void onError(Session session, Throwable error)
    {
        log.error("用户错误,原因:" + error.getMessage());
        error.printStackTrace();
    }


    /**
     * 发送广播消息
     *
     * @param message 消息内容
     */
    public void sendAllMessage(String message)
    {
        log.info("【websocket消息】广播消息:" + message);
        for (WebSocketHandler webSocketHandler : webSockets)
        {
            try
            {
                if (webSocketHandler.session.isOpen())
                {
                    webSocketHandler.session.getAsyncRemote().sendText(message);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }


    /**
     * 发送单点消息
     *
     * @param userId  要发送的用户id（发给谁？）
     * @param message 消息内容
     */
    public void sendOneMessage(String userId, String message)
    {
        Session session = sessionPool.get(userId);
        if (session != null && session.isOpen())
        {
            try
            {
                log.info("【websocket消息】 单点消息:" + message);
                session.getAsyncRemote().sendText(message);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }


    /**
     * 发送消息给多个人
     *
     * @param userIds 用户id列表
     * @param message 消息内容
     */
    public void sendMoreMessage(String[] userIds, String message)
    {
        for (String userId : userIds)
        {
            Session session = sessionPool.get(userId);
            if (session != null && session.isOpen())
            {
                try
                {
                    log.info("【websocket消息】 单点消息:" + message);
                    session.getAsyncRemote().sendText(message);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

    }
}
