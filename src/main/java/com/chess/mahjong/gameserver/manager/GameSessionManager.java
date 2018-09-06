package com.chess.mahjong.gameserver.manager;

import com.chess.mahjong.gameserver.Avatar;
import com.chess.mahjong.gameserver.commons.code.ErrorCode;
import com.chess.mahjong.gameserver.commons.constant.GameConstants;
import com.chess.mahjong.gameserver.commons.session.GameSession;
import com.chess.mahjong.gameserver.context.GameServerContext;
import com.chess.mahjong.gameserver.logic.RoomLogic;
import com.chess.mahjong.gameserver.msg.response.ErrorResponse;
import com.chess.mahjong.gameserver.msg.response.login.BreakLineResponse;
import com.chess.mahjong.gameserver.msg.response.login.OtherBackLoginResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 周润斌
 * Date: 2018/9/5
 * Time: 13:06
 * Description:
 */
public class GameSessionManager {

    private static GameSessionManager gameSessionManager;

    /**
     * 总的在线人数
     */
    public static int topOnlineAccountCount = 0;

    public Map<String, GameSession> sessionMap = new HashMap<>();

    private GameSessionManager() {
    }

    /**
     * @return
     */
    public static GameSessionManager getInstance() {
        if (gameSessionManager == null) {
            gameSessionManager = new GameSessionManager();
        }
        return gameSessionManager;
    }

    /**
     * @param
     * @return
     */
    public GameSession getAvatarByUuid(String uuid) {
        return sessionMap.get(uuid);
    }


    public boolean putGameSessionInHashMap(GameSession gameSession, String uuId) {
        // 检查用户session是否存在
        boolean exists = checkSessionIsExists(uuId);
        // 存在
        if (exists) {
            // 找到这个会话
            GameSession preSession = sessionMap.get(GameConstants.SESSION_PREFIX + uuId);
            try {
                // 发送用户其他设备登陆通知。
                preSession.sendMsg(new ErrorResponse(ErrorCode.Error_000022));
                // 发送下线通知
                preSession.sendMsg(new BreakLineResponse(ErrorCode.SUCCESS_CODE));

                // 线程睡眠一秒 等待发送通知完成
                Thread.sleep(1000);

                // 关闭资源
                sessionMap.get(GameConstants.SESSION_PREFIX + uuId).close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 等待3秒
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 获取用户信息
            Avatar avatar = gameSession.getRole(Avatar.class);
            // 把用户加入到在线的列表中
            GameServerContext.add_onLine_Character(avatar);
            // 把用户从离线的集合中删除
            GameServerContext.remove_offLine_Character(avatar);

            // 停止销毁对象 TODO

            // 重新建立会话关联
            sessionMap.put(GameConstants.SESSION_PREFIX + uuId, gameSession);

            // 如果玩家在房间中 则需要给其他玩家发送在线消息

            if (avatar.getRoomVO() != null) {
                RoomLogic roomLogic = RoomManager.getInstance().getRoom(avatar.getRoomVO().getRoomId());
                if (roomLogic != null) {
                    List<Avatar> playerList = roomLogic.getPlayerList();
                    for (int i = 0; i < playerList.size(); i++) {
                        if (!playerList.get(i).getUuId().equals(avatar.getUuId())) {
                            //给其他三个玩家返回重连用户信息
                            playerList.get(i).getSession().sendMsg(new OtherBackLoginResponse(ErrorCode.SUCCESS_CODE, avatar.getUuId() + ""));
                        }
                    }
                }
            }
        } else {
            // 用户之前没有登陆过. 建立会话关联
            sessionMap.put(GameConstants.SESSION_PREFIX + uuId, gameSession);
            if (sessionMap.size() > topOnlineAccountCount) {
                topOnlineAccountCount = sessionMap.size();
            }
        }

        return !exists;
    }

    /**
     * 检测用户session是否存在
     *
     * @param uuId
     * @return
     */
    private boolean checkSessionIsExists(String uuId) {
        //可以用来判断是否在线****等功能
        GameSession gameSession = sessionMap.get(GameConstants.SESSION_PREFIX + uuId);
        if (gameSession != null) {
            return true;
        }
        return false;
    }
}
