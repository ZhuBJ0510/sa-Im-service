package com.saim.demo.websocket;

import com.alibaba.fastjson.JSON;

import com.saim.demo.websocket.pojo.Websocket;
import org.springframework.stereotype.Service;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * description: UserWebsock
 * date: 2020/7/24 11:22
 *
 * @author jh
 */
@Service
@ServerEndpoint("/websocket/{uid}/{type}")
public class UserWebsocket {

    private Session session;

    /**
     * 连接的用户id
     */
    private String uId;
    /**
     * 连接用户类型 1 客服 2 用户
     */
    private String  type;
    /**
     * 是否是非法登录
     */
    private boolean boo = true;

    /**
     * 打开链接
     */
    @OnOpen
    public void onOpen(@PathParam(value = "uid") String uid,@PathParam(value = "type") String type, Session session) throws IOException {

        System.out.println(uid + "连接websocket"+type);

        if (uid != null) {
            this.session = session;
            this.uId = uid;
            this.type = type;

            if("1".equals(type)){
                // 客服登录
                // 可以在这与管理员账号绑定
                UserWebsocket userWebsocket = Websocket.serviceWebsocketMap.get(uid);
                if (userWebsocket == null) {
                    Websocket.serviceWebsocketMap.put(uid, this);

                    Map<String, Object> map = new HashMap<>();
                    map.put("type", "chatList");
                    onMessage(JSON.toJSONString(map));

                    boo = false;
                }
            }else{
                // 用户登录
                // 可以在这与用户账号绑定
                UserWebsocket userWebsocket = Websocket.userWebsocketMap.get(uid);
                if (userWebsocket == null) {
                    Websocket.userWebsocketMap.put(uid, this);

                    // Map<String, Object> map = new HashMap<>();
                    // map.put("type", "joinChat");
                    // map.put("pageNo",1);
                    // map.put("pageSize",10);
                    // onMessage(JSON.toJSONString(map));

                    boo = false;
                }
            }
        }

        if (boo) {
            Map<String, Object> returnMap = new HashMap<>();
            returnMap.put("type", "close");
            returnMap.put("text", "账号在线,请先退出");
            sendMessageTo(returnMap);
            session.close();
            System.out.println("非法连接");
        }

        session.setMaxIdleTimeout(20000);
    }

    /**
     * 关闭连接
     */
    @OnClose
    public void onClose() {

        if (!boo) {
            if ("1".equals(this.type)){
                Websocket.serviceWebsocketMap.remove(uId);
            }else{
                Websocket.userWebsocketMap.remove(uId);
            }
        }

        System.out.println("退出" + uId);
    }

    /**
     * 异常连接
     */
    @OnError
    public void onError(Throwable throwable, Session session) {
        System.err.println("异常！！！！！");
        throwable.printStackTrace();
    }

    /**
     * 通信
     */
    @OnMessage
    public void onMessage(String msg) {
        System.out.println(uId + "发送的" + msg);

        //接收的消息
        Map<String, Object> map = JSON.parseObject(msg);
        //返回消息
        Map<String, Object> returnMap = new HashMap<>();


        // 心跳
        if ("setdate".equals(map.get("type"))) {
            returnMap.put("type", "setdate");
            sendMessageTo(returnMap);
        }
        //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓聊    天↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
        // 发送消息
        if ("sendText".equals(map.get("type"))) {
            returnMap.put("type", "sendText");
            returnMap.put("sid", uId);
            returnMap.put("text", map.get("text"));
            returnMap.put("date", System.currentTimeMillis());
            returnMap.put("genre", (String) map.get("genre"));

            Web.userChatService.sendChat("1".equals(this.type) ? (String) map.get("fId") : uId , returnMap);
            returnMap.put("unread",map.get("unread"));
            // 自己发信息没有未读信息
            if ("1".equals(this.type)){
                Web.userChatListService.readUserChatList((String) map.get("fId"));
            }else{
                returnMap.put("unread",0);
                Web.userChatService.readChat(uId);
            }
            sendMessageTo(returnMap);
            returnMap.put("type", "receiveText");
            if ("1".equals(this.type)){
                sendMessageToUser((String) map.get("fId"),returnMap);
            }else{
                sendMessageToService(returnMap);
            }
            map.put("type", "chatList");
        }

        // 进入单聊室
        if ("joinChat".equals(map.get("type"))) {
            returnMap.put("type", "joinChat");


            // 查询 用户聊天记录
            returnMap.put("data", Web.userChatService.findChatRecord(
                    "1".equals(this.type) ? (String) map.get("fId") : uId ,
                    (Integer) map.get("pageNo"),
                    (Integer) map.get("pageSize")));

            sendMessageTo(returnMap);


            map.put("type","readChat");

        }

        // 读取信息
        if ("readChat".equals(map.get("type"))) {

            if ("1".equals(this.type)) {
                returnMap.put("fid", map.get("fId"));
                // 清空聊天列表未读消息
                Web.userChatListService.readUserChatList((String) map.get("fId"));

                map.put("type", "chatList");
            }else{
                // 清空用户未读信息
                Web.userChatService.readChat(uId);
            }
            if ("2".equals(this.type)) {
                map.put("type", "unread");
            }
        }

        // 查询列表
        if ("chatList".equals(map.get("type"))) {

            // 发送自己聊天列表
            returnMap.clear();

            returnMap.put("type", "chatList");
            returnMap.put("data", Web.userChatListService.findUserChatList());
            sendMessageToService(returnMap);
        }

        // 用户查询未读消息
        if("unread".equals(map.get("type"))){

            returnMap.clear();
            returnMap.put("type","unread");
            returnMap.put("data",Web.userChatService.findChatRecord(uId,1,1).get(0));
            sendMessageTo(returnMap);
        }
        //↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    }

    /**
     * 给自己返回消息
     *
     * @param returnMap 发送信息
     */
    private synchronized void sendMessageTo(Map<String, Object> returnMap) {
        if (session.isOpen()) {
            String returnJson = JSON.toJSONString(returnMap);

            try {
                session.getBasicRemote().sendText(returnJson);
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println(uId + "返回信息" + returnJson);
        }
    }

    private synchronized void sendMessageToUser(String uid,Map<String ,Object> returnMap){
        UserWebsocket userWebsocket = Websocket.userWebsocketMap.get(uid);
        if (userWebsocket != null&&userWebsocket.session.isOpen()){
            String returnJson = JSON.toJSONString(returnMap);
            try {
                userWebsocket.session.getBasicRemote().sendText(returnJson);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("发给" + uid + "信息" + returnJson);
        }
    }

    /**
     * 发给客服信息
     *
     * @param returnMap 发送信息
     */
    private synchronized void sendMessageToService( Map<String, Object> returnMap) {

        for (Map.Entry<String ,UserWebsocket> m : Websocket.serviceWebsocketMap.entrySet()) {
            if (m.getValue() != null && m.getValue().session.isOpen()) {
                String returnJson = JSON.toJSONString(returnMap);
                try {
                    m.getValue().session.getBasicRemote().sendText(returnJson);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println("发给" + m.getKey() + "信息" + returnJson);
            }
        }
    }
}
