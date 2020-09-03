package com.saim.demo.websocket.service.impl;

import com.saim.demo.websocket.Web;
import com.saim.demo.websocket.service.UserChatListService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description: UserChatListServiceImpl
 * date: 2020/7/25 17:26
 *
 * @author jh
 */
@Service
public class UserChatListServiceImpl implements UserChatListService {

    @Override
    public List<Object> findUserChatList() {

        List<Object> list = userChatList();

        for (int i = 0; i < list.size(); i++) {

            // 配置用户信息
            // User user = userService.findById(((Map) list.get(i)).get("fid"));
            // 用户昵称信息
            ((Map) list.get(i)).put("name", "昵称");
            // 用户头像信息
            ((Map) list.get(i)).put("img", "https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1596698839&di=e861f339c417ee75c415e4b5014f705a&src=http://a1.att.hudong.com/05/00/01300000194285122188000535877.jpg");
        }
        return list;
    }

    @Override
    public void updateUserChatList(String uid, Map<String, Object> map) {
        List<Object> list = userChatList();
        Map<String ,Object> newMap = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            if(uid.equals(((Map)list.get(i)).get("id"))){
                newMap.put("unread",(Integer) (((Map)list.get(i)).get("unread")) + 1);
                list.remove(i);
                break;
            }
        }
        newMap.put("id",uid);
        newMap.put("text",map.get("text"));
        newMap.put("date",map.get("date"));
        newMap.put("genre",map.get("genre"));
        newMap.putIfAbsent("unread",1);
        list.add(0, newMap);

        Web.redisUtil.lCover("IM:SERVICE:CHATLIST", list);

    }

    @Override
    public void readUserChatList(String uid) {
        List<Object> list = userChatList();
        for (int i = 0; i < list.size(); i++) {
            if(uid.equals(((Map)list.get(i)).get("id"))){
                ((Map)list.get(i)).put("unread",0);
                break;
            }
        }

        Web.redisUtil.lCover("IM:SERVICE:CHATLIST", list);

    }

    /**
     * 查询客服聊天列表
     * @return list
     */
    private List<Object> userChatList() {

        return Web.redisUtil.lGetAll("IM:SERVICE:CHATLIST");
    }
}
