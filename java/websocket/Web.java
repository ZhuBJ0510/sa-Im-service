package com.saim.demo.websocket;


import com.saim.demo.websocket.service.*;
import com.saim.demo.websocket.utils.ImRedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * description: Web
 * date: 2020/7/24 18:02
 *
 * @author jh
 */
@Component
public class Web {

    public static ImRedisUtil redisUtil;
    public static UserChatService userChatService;
    public static UserChatListService userChatListService;

    @Autowired
    public void setBean(
            ImRedisUtil redisUtil,
            UserChatService userChatService,
            UserChatListService userChatListService
    ) {
        Web.redisUtil = redisUtil;
        Web.userChatService = userChatService;
        Web.userChatListService = userChatListService;
    }
}
