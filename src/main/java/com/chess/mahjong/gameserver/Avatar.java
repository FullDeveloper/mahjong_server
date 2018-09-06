package com.chess.mahjong.gameserver;

import com.chess.mahjong.gameserver.commons.session.GameSession;
import com.chess.mahjong.gameserver.pojo.AvatarVO;
import com.chess.mahjong.gameserver.pojo.RoomVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 周润斌
 * Date: 2018/9/5
 * Time: 12:53
 * Description:
 */
public class Avatar {

    public AvatarVO avatarVO;

    /**
     * session
     */
    private GameSession session;

    /**
     * 房间号
     */
    private RoomVO roomVO;

    /**
     * 存储某一句游戏断线时 结算信息
     */
    public String oneSettlementInfo;

    /**
     * 是否是一局结算时掉线
     */
    public boolean overOff = false;


    /**
     * 获取用户uuid
     *
     * @return
     */
    public String getUuId() {
        return avatarVO.getAccount().getUuid();
    }

    /**
     * 检测到有人胡牌时存储胡牌的详细消息(划水麻将和长沙麻将用)
     *      0: 天胡
     *             1：明杠 	 1*3番
     2：暗杠	 2*3番
     3：放杠/点杠 	   1*3番（谁放牌谁出番）
     4：自摸 	6番（其他三家每家出2番）
     5：普通点炮	3番（谁放炮谁出番）
     6：七对点炮	3*3番（谁放炮谁出番）
     7：七对自摸	18番（其他三家每家出6番）
     8：杠开/杠上花	18番（其他三家每家出6番）
     9：抢杠	3*3番（谁要杠牌谁出番）
     10：一炮双响	根据胡牌者的牌型来计算放炮者出的番数（胡牌两方所胡牌型的番数相加）
     11：一炮三响	根据胡牌者的牌型来计算放炮者出的番数（同上）

     数组格式   牌索引:类型
     放弃的时候需要清空
     *
     *小胡点炮：3
     *大胡点炮：3*3
     *
     *小胡自摸： 2*3   （每家2分）
     *大胡自摸： 2*3*3  (每家6分)
     */
    public List<String> huAvatarDetailInfo = new ArrayList<>();

    /**
     * 为自己的牌组里加入新牌
     * /碰 1  杠2  胡3  吃4
     *
     * @param cardIndex
     */
    public boolean putCardInList(Integer cardIndex) {
        if (avatarVO.getPaiArray()[0][cardIndex] < 4) {
            avatarVO.getPaiArray()[0][cardIndex]++;
            return true;
        } else {
            return true;
        }
    }

    public RoomVO getRoomVO() {
        return roomVO;
    }

    public GameSession getSession() {
        return session;
    }

    public void setSession(GameSession session) {
        this.session = session;
    }

    public void setRoomVO(RoomVO roomVo) {
        this.roomVO = roomVo;
        if (avatarVO != null) {
            avatarVO.setRoomId(roomVo.getRoomId());
        }
    }


}
