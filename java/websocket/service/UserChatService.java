package com.saim.demo.websocket.service;

import java.util.List;
import java.util.Map;

/**
 * description: UserChat
 * date: 2020/7/24 18:45
 * 聊天室操作
 *
 * @author jh
 */
public interface UserChatService {

    /**
     * 查询单聊历史记录
     *
     * @param uid      发送人id
     * @param pageNo   当前页
     * @param pageSize 页大小
     * @return list
     */
    List<Object> findChatRecord(String uid, Integer pageNo, Integer pageSize);

    /**
     * 发送消息
     *
     * @param uid       用户id
     * @param returnMap 发送的内容
     */
    void sendChat(String uid, Map returnMap);

    /**
     * 用户读取消息
     * @param uid       用户id
     */
    void readChat(String uid);
}
