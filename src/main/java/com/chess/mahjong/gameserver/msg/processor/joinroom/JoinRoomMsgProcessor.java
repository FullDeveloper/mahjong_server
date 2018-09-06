package com.chess.mahjong.gameserver.msg.processor.joinroom;

import com.chess.mahjong.gameserver.Avatar;
import com.chess.mahjong.gameserver.commons.code.ErrorCode;
import com.chess.mahjong.gameserver.commons.session.GameSession;
import com.chess.mahjong.gameserver.logic.RoomLogic;
import com.chess.mahjong.gameserver.manager.GameSessionManager;
import com.chess.mahjong.gameserver.manager.RoomManager;
import com.chess.mahjong.gameserver.msg.processor.MsgProcessor;
import com.chess.mahjong.gameserver.msg.response.ErrorResponse;
import com.chess.network.codec.message.ClientRequest;
import com.chess.persist.util.GlobalUtil;
import net.sf.json.JSONObject;

/**
 * @author 周润斌
 * Date: 2018/9/6
 * Time: 9:50
 * Description:
 */
public class JoinRoomMsgProcessor extends MsgProcessor {

    @Override
    public void process(GameSession gameSession, ClientRequest request) throws Exception {
        // 检查用户是否登陆
        if (GlobalUtil.checkIsLogin(gameSession)) {
            JSONObject json = JSONObject.fromObject(request.getString());
            // 获取房间号
            int roomId = Integer.parseInt(json.get("roomId").toString());
            // 根据房间号获取该房间的房间逻辑处理对象
            RoomLogic roomLogic = RoomManager.getInstance().getRoom(roomId);
            if (roomLogic != null) {
                // 获取该用户的信息
                Avatar avatar = gameSession.getRole(Avatar.class);

                // 用户存在房间 直接回到房间
                if(avatar.avatarVO.getRoomId() != null){
                    GameSessionManager.getInstance().putGameSessionInHashMap(gameSession,avatar.getUuId());
                    roomLogic.returnBackAction(avatar);
                    return;
                }
                roomLogic.intoRoom(avatar);
            } else {
                // 房间不存在 提示客户端
                gameSession.sendMsg(new ErrorResponse(ErrorCode.Error_000018));
            }
        } else {
            // 销毁对象 TODO
        }
    }

}
