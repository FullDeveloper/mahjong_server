package com.chess.mahjong.gameserver.commons.session;

import com.chess.mahjong.gameserver.Avatar;
import com.chess.mahjong.gameserver.commons.code.ErrorCode;
import com.chess.mahjong.gameserver.commons.constant.GameConstants;
import com.chess.mahjong.gameserver.context.GameServerContext;
import com.chess.mahjong.gameserver.logic.RoomLogic;
import com.chess.mahjong.gameserver.manager.GameSessionManager;
import com.chess.mahjong.gameserver.manager.RoomManager;
import com.chess.mahjong.gameserver.msg.response.offline.OffLineResponse;
import com.chess.network.codec.message.ResponseMsg;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * Author: zrb
 * Date: 2018/9/5
 * Time: 10:03
 * Description: 游戏会话对象
 */
public class GameSession {

    /**
     * IoSession
     */
    private IoSession session;
    /**
     * 用户的服务器地址
     */
    private String address;
    /**
     * 记录心跳时间， 间隔，到达一定时间就表示前端断线了
     */
    private int time = 0;

    /**
     * 用户的角色
     */
    private Object role;

    /**
     * 是否登陆
     */
    private boolean isLogin = false;

    private static final AttributeKey KEY_PLAYER_SESSION = new AttributeKey(GameSession.class, "player.session");

    public GameSession(IoSession session) {
        this.session = session;
        this.session.setAttribute(KEY_PLAYER_SESSION, this);
        SocketAddress socketaddress = session.getRemoteAddress();
        InetSocketAddress s = (InetSocketAddress) socketaddress;
        address = s.getAddress().getHostAddress();
        //存当前用户相关的服务器地址
    }

    /**
     * 得到一个GameSession的实例化对象
     *
     * @param session
     * @return
     */
    public static GameSession getInstance(IoSession session) {
        Object playerObj = session.getAttribute(KEY_PLAYER_SESSION);
        session.getService().getManagedSessions();
        return (GameSession) playerObj;
    }

    /**
     * 发送消息给客户端
     *
     * @param msg
     * @return
     * @throws InterruptedException
     */
    public WriteFuture sendMsg(ResponseMsg msg) {
        if (session == null || !session.isConnected() || session.isClosing()) {
            return null;
        }
        return session.write(msg);
    }


    public IoSession getSession() {
        return session;
    }

    public void setSession(IoSession session) {
        this.session = session;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    /**
     * 得到角色信息
     */
    public <T> T getRole(Class<T> t) {
        return (T) this.role;
    }

    public void setRole(Object role) {
        this.role = role;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    /**
     * 关闭session
     */
    public void close() {

        if (session != null) {
            // 2016-8-29短线时不关闭用户session，等
            session.close(false);
            Avatar avatar = getRole(Avatar.class);
            if (avatar != null) {

                // 将该用户从在线的集合中删除。  放入的离线的集合中。
                GameServerContext.add_offLine_Character(avatar);
                // 将用户从在线的集合中删除
                GameServerContext.remove_onLine_Character(avatar);

                // 删除该用户的会话
                GameSessionManager.getInstance().sessionMap.remove(GameConstants.SESSION_PREFIX + avatar.getUuId());

                // 设置该用户的状态为离线
                avatar.avatarVO.setOnLine(false);

                // 延迟销毁用户数据 TODO


                // 如果用户处于玩牌房间 通知其他用户该用户离线
                if (avatar.avatarVO.getRoomId() != 0) {
                    RoomLogic roomLogic = RoomManager.getInstance().getRoom(avatar.avatarVO.getRoomId());
                    if (roomLogic != null) {
                        // 两个人以上才进行通知
                        if (avatar.getRoomVO().getPlayerList().size() >= 2) {
                            //房间还有其他玩家，则向其他玩家发送离线玩家消息
                            for (Avatar ava : roomLogic.getPlayerList()) {
                                if (!avatar.getUuId().equals(ava.getUuId())) {
                                    //发送离线通知
                                    ava.getSession().sendMsg(new OffLineResponse(ErrorCode.SUCCESS_CODE, avatar.getUuId() + ""));
                                    //同意解散房间人数 设置为0,有人掉线就取消解散房间
//                                    roomLogic.setDissolveCount(1);
//                                    roomLogic.setDissolve(true);
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}
