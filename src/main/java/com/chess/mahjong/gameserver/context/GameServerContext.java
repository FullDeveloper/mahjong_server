package com.chess.mahjong.gameserver.context;

import com.chess.mahjong.gameserver.Avatar;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 周润斌
 * Date: 2018/9/5
 * Time: 13:00
 * Description:
 * 管理整个服上的玩家/创建房间数量
 * 1，查看当前在线人数，当天在线人数最高数记录，各个游戏在线人数。
 * 2，当天创建房间总数，当前在玩的创建的房间总数，各个游戏当前创建房间数，各个游戏当天创建的房间总数。
 * 4. 查看玩家的当前信息（是否在线，IP，所在游戏，房间号，房卡余额，总消耗房卡数）。
 */
public class GameServerContext {

    /**
     * 所有在线的玩家
     */
    private static Map<String, Avatar> ALL_ONLINE_PLAYER = new ConcurrentHashMap<>();
    /**
     * 所有掉线的玩家
     */
    private static Map<String, Avatar> ALL_OFFLINE_PLAYER = new ConcurrentHashMap<>();

    /**
     * 把用户添加到在线hashMap中
     * Character character
     *
     * @param avatar
     */
    public static void add_onLine_Character(Avatar avatar) {
        ALL_ONLINE_PLAYER.put(avatar.getUuId(), avatar);
    }

    /**
     * 把用户添加到掉线hashMap中,同时移除在线map中
     * Character character
     *
     * @param avatar
     */
    public static void add_offLine_Character(Avatar avatar) {
        ALL_OFFLINE_PLAYER.put(avatar.getUuId(), avatar);
        ALL_ONLINE_PLAYER.remove(avatar.getUuId());
    }

    /**
     * 把用户从在线hashmap中删除
     *
     * @param avatar
     */
    public static void remove_onLine_Character(Avatar avatar) {
        ALL_ONLINE_PLAYER.remove(avatar.getUuId());
    }

    /**
     * 把用户从掉线hashmap中删除
     *
     * @param avatar
     */
    public static void remove_offLine_Character(Avatar avatar) {
        ALL_OFFLINE_PLAYER.remove(avatar.getUuId());
    }

    /**
     * 从在线列表中得到用户
     *
     * @param uuid
     * @return avatar
     */
    public static Avatar getAvatarFromOnLine(String uuid) {
        return ALL_ONLINE_PLAYER.get(uuid);
    }

    /**
     *从离线列表中得到用户
     * @param uuid
     * @return avatar
     */
    public static  Avatar getAvatarFromOff(String uuid){
        return ALL_OFFLINE_PLAYER.get(uuid);
    }

}
