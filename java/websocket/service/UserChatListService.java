package com.saim.demo.websocket.service;

import java.util.List;
import java.util.Map;

/**
 * description: UserChatListService
 * date: 2020/7/25 17:26
 * 用户聊天列表
 *
 * @author jh
 */
public interface UserChatListService {

    /**
     * 查询客服聊天列表
     *
     * @return list
     */
    List findUserChatList();

    /**
     * 修改聊天列表
     *
     * @param uid  用户id
     * @param map  最新聊天信息
     */
    void updateUserChatList(String uid, Map<String, Object> map);

    /**
     * 聊天列表已读
     *
     * @param uid 用户id
     */
    void readUserChatList(String uid);
}
