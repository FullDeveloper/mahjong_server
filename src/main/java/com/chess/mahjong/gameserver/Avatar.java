package com.chess.mahjong.gameserver;

import com.chess.mahjong.gameserver.commons.session.GameSession;
import com.chess.mahjong.gameserver.pojo.AvatarVO;
import com.chess.mahjong.gameserver.pojo.RoomVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * 当自己摸牌时检测自己是否有杠的牌。把杠的牌放入到整个list里面，然后在转入给前端
     */
    public List<Integer> gangIndex = new ArrayList<>();

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
     * 0: 天胡
     * 1：明杠 	 1*3番
     * 2：暗杠	 2*3番
     * 3：放杠/点杠 	   1*3番（谁放牌谁出番）
     * 4：自摸 	6番（其他三家每家出2番）
     * 5：普通点炮	3番（谁放炮谁出番）
     * 6：七对点炮	3*3番（谁放炮谁出番）
     * 7：七对自摸	18番（其他三家每家出6番）
     * 8：杠开/杠上花	18番（其他三家每家出6番）
     * 9：抢杠	3*3番（谁要杠牌谁出番）
     * 10：一炮双响	根据胡牌者的牌型来计算放炮者出的番数（胡牌两方所胡牌型的番数相加）
     * 11：一炮三响	根据胡牌者的牌型来计算放炮者出的番数（同上）
     * <p>
     * 数组格式   牌索引:类型
     * 放弃的时候需要清空
     * <p>
     * 小胡点炮：3
     * 大胡点炮：3*3
     * <p>
     * 小胡自摸： 2*3   （每家2分）
     * 大胡自摸： 2*3*3  (每家6分)
     */
    public List<String> huAvatarDetailInfo = new ArrayList<>();


    /**
     * list里面字符串规则
     * 杠：uuid(出牌家),介绍(明杠，暗杠)  （123，明杠）
     * 自己摸来杠：介绍(明杠，暗杠)
     * <p>
     * 点炮：uuid(出牌家),介绍(胡的类型) （123，qishouhu）
     * 自摸：介绍(胡的类型)
     * <p>
     * 碰：
     * <p>
     * eg:  碰： key:1     value: 碰的牌的下标
     * 杠：key:2    value: 杠的牌的下标
     * 胡：key:3    value: 胡的牌的下标
     * 吃：key:4    value:  吃的牌的下标(1:2:3)
     * <p>
     * key:1:碰    2:杠    3:胡   4:吃   5:抢胡
     * value:信息，分条信息之间用","隔开
     */
    private Map<Integer, String> resultRelation = new HashMap<>();

    /**
     * 存储杠和胡的信息
     * @param i 操作类型
     * @param str
     */
    public  void putResultRelation(Integer i , String str) {
        synchronized(resultRelation){
            if(resultRelation.get(i) == null){
                resultRelation.put(i, str);
            }
            else{
                resultRelation.put(i, resultRelation.get(i)+","+str);
            }
        }
    }

    public boolean qiangHu = true;

    /**
     * 请求碰
     */
    public boolean pengQuest = false;
    /**
     * 请求杠
     */
    public boolean gangQuest = false;
    /**
     * 请求胡
     */
    public boolean huQuest = false;
    /**
     * 请求吃
     */
    public boolean chiQuest = false;

    /**
     * 当前玩家能否吃
     */
    public boolean canHu = true;

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


    /**
     * 把牌从自己的牌列中去除
     *
     * @param cardIndex
     */
    public void pullCardFormList(int cardIndex) {
        if (avatarVO.getPaiArray()[0][cardIndex] > 0) {
            avatarVO.getPaiArray()[0][cardIndex]--;
        } else {/*
            try {
                session.sendMsg(new ErrorResponse(ErrorCode.Error_000007));
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Error : pullCardFormList --> 牌数组里没有这张牌");
        */
        }
    }

    /**
     * 檢測是否可以杠别人出的牌/此牌对应的下标不为1（碰过了的牌）
     *
     * @param cardIndex
     * @return
     */
    public boolean checkGang(int cardIndex) {
        boolean flag = false;
        // 每次出牌就先清除缓存里面的可以杠的牌下标
        gangIndex.clear();
        // 相同的牌有3张即可杠牌
        if (avatarVO.getPaiArray()[0][cardIndex] == 3 && avatarVO.getPaiArray()[1][cardIndex] == 0) {
            gangIndex.add(cardIndex);
            flag = true;
        }
        return flag;
    }

    /**
     * 檢測是否可以碰 大于等于2张且对应的下标不为1都可以碰
     *
     * @param cardIndex
     * @return
     */
    public boolean checkPeng(int cardIndex) {
        boolean flag = false;
        if (avatarVO.getPaiArray()[0][cardIndex] >= 2) {
            if (resultRelation.get(1) == null) {
                flag = true;
            } else {
                String[] strs = resultRelation.get(1).split(",");
                for (int i = 0; i < strs.length; i++) {
                    if (strs[i].equals(cardIndex + "")) {
                        flag = false;
                        i = strs.length;
                    } else {
                        flag = true;
                    }
                }
            }
        }
        return flag;
    }

    /**
     * @return
     */
    public boolean checkSelfGang() {
        // 先清除缓存里面的可以杠的牌下标
        gangIndex.clear();
        // 剔除掉当前以前吃，碰，杠的牌组 再进行比较
        boolean flag = false;
        if (!roomVO.isAddWordCard()) {
            // 划水麻将没有风牌  就27
            for (int i = 0; i < 27; i++) {
                // 判断手里有4个的牌
                if (avatarVO.getPaiArray()[0][i] == 4 && avatarVO.getPaiArray()[1][i] != 2) {
                    //先判断所有4个的牌组中是否有未杠过的
                    gangIndex.add(i);
                    flag = true;
                    // 多个杠的情况下默认杠下标最小的个牌
                    break;
                }
            }
        } else {
            // TODO 划水麻将有风牌  就 34
        }
        return flag;
    }

    public void setQuestToFalse(){
        huQuest = false;
        gangQuest = false;
        pengQuest = false;
        chiQuest = false;
    }

    /**
     * 得到牌组的一维数组。用来判断是否胡牌和听牌用
     * @return
     */
    public int[][] getPaiArray(){
        return avatarVO.getPaiArray();
    }

    /**
     * 设置牌的状态
     * @param cardIndex
     * @param type;//碰 1  杠2  胡3  吃4
     */
    public void setCardListStatus(int cardIndex,int type){
        avatarVO.getPaiArray()[1][cardIndex] = type;
    }

    public RoomVO getRoomVO() {
        return roomVO;
    }

    public GameSession getSession() {
        return session;
    }

    public Map<Integer, String> getResultRelation() {
        return resultRelation;
    }

    public void setResultRelation(Map<Integer, String> resultRelation) {
        this.resultRelation = resultRelation;
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
