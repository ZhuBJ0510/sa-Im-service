package com.saim.demo.websocket.pojo;


import com.saim.demo.websocket.UserWebsocket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * description: Websocket
 * date: 2020/7/24 17:55
 *
 * @author jh
 */
public class Websocket {
    /**
     * 在线用户
     */
    public static Map<String, UserWebsocket> userWebsocketMap = new ConcurrentHashMap<>();

    /**
     * 在线客服
     */
    public static Map<String, UserWebsocket> serviceWebsocketMap = new ConcurrentHashMap<>();
}
