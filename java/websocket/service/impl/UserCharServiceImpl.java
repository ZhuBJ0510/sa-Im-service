package com.saim.demo.websocket.service.impl;

import com.saim.demo.websocket.Web;
import com.saim.demo.websocket.service.UserChatService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description: UserServiceImpl
 * date: 2020/7/24 18:56
 *
 * @author jh
 */
@Service
public class UserCharServiceImpl implements UserChatService {

    @Override
    public List<Object> findChatRecord(String uid,Integer pageNo, Integer pageSize) {

        return Web.redisUtil.lGet("IM:SERVICE:MESSAGE:" + uid, (pageNo - 1) * pageSize, pageSize * pageNo - 1);
    }

    @Override
    public void sendChat(String uid, Map returnMap) {

        Map m = (Map) Web.redisUtil.lGetIndex("IM:SERVICE:MESSAGE:" + uid, 0);

        returnMap.put("id", Web.redisUtil.lGetListSize("IM:SERVICE:MESSAGE:" + uid));
        // 添加用户的未读信息
        returnMap.put("unread",m == null ? 1 : (Integer) m.get("unread") + 1);
        Web.redisUtil.lSet("IM:SERVICE:MESSAGE:" + uid, returnMap);

        Web.userChatListService.updateUserChatList(uid, returnMap);
    }

    @Override
    public void readChat(String uid) {
        Map m = (Map) Web.redisUtil.lGetIndex("IM:SERVICE:MESSAGE:" + uid, 0);
        m.put("unread",0);
        Web.redisUtil.lSet("IM:SERVICE:MESSAGE:" + uid, 0 ,m);
    }
}
