package com.chess.mahjong.gameserver.logic;

import com.chess.mahjong.gameserver.Avatar;
import com.chess.mahjong.gameserver.commons.code.ErrorCode;
import com.chess.mahjong.gameserver.msg.response.hu.HuPaiResponse;
import com.chess.mahjong.gameserver.pojo.RoomVO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author 周润斌
 * Date: 2018/9/6
 * Time: 10:02
 * Description:
 */
public class PlayCardsLogic {

    private static final Integer CHANGSHA_MAHJONG = 3;

    /**
     * 牌的数量
     */
    private int paiCount;

    /**
     * 单局是否结束，判断能否调用准备接口 10-11新增
     */
    boolean singleOver = true;

    /**
     * 当前摸牌人的索引(初始值为庄家索引)
     */
    private int pickAvatarIndex;

    /**
     * 房主ID
     */
    private String theOwner;

    /**
     * 4家玩家信息集合
     */
    private List<Avatar> playerList;
    /**
     * 房间信息
     */
    private RoomVO roomVO;

    /**
     * 有人要胡的數組
     */
    private List<Avatar> huAvatar = new ArrayList<>();
    /**
     *有人要碰的數組
     */
    private List<Avatar> penAvatar = new ArrayList<>();
    /**
     *有人要杠的數組
     */
    private List<Avatar> gangAvatar = new ArrayList<>();
    /**
     *有人要咋吃的數組
     */
    private List<Avatar> chiAvatar = new ArrayList<>();
    /**
     * 起手胡
     */
    private List<Avatar> qishouHuAvatar = new ArrayList<>();

    /**
     * 整张桌子上所有牌的数组
     */
    private List<Integer> listCard = null;

    /**
     * 上一家出的牌的点数
     */
    private int putOffCardPoint;

    /**
     * 当前出牌人的索引
     */
    private int curAvatarIndex;

    /**
     * 下张牌的索引
     */
    private int nextCardIndex = 0;

    /**
     * 庄家
     */
    public Avatar bankerAvatar = null;

    /**
     * 初始化牌
     *
     * @param value 房间对象
     */
    public void initCard(RoomVO value) {
        roomVO = value;

        // 目前只实现长沙麻将 TODO
        if (CHANGSHA_MAHJONG.equals(roomVO.getRoomType())) {
            paiCount = 27;
        }
        listCard = new ArrayList<>();

        // 初始化牌
        for (int i = 0; i < paiCount; i++) {
            for (int k = 0; k < 4; k++) {
                listCard.add(i);
            }
        }

        for (int i = 0; i < playerList.size(); i++) {
            playerList.get(i).avatarVO.setPaiArray(new int[2][paiCount]);
        }

        //洗牌
        shuffleTheCards();
        //发牌
        dealingTheCards();
    }

    /**
     * 手持牌的数量
     */
    private static final Integer HAND_CARD_COUNT = 13;

    private void dealingTheCards() {
        // 下张牌的索引
        nextCardIndex = 0;
        // 庄家
        bankerAvatar = null;

        // 每个人只有13张牌
        for (int i = 0; i < HAND_CARD_COUNT; i++) {
            // 4个玩家
            for (int k = 0; k < playerList.size(); k++) {
                // 找出4个玩家中谁是 庄家
                if (bankerAvatar == null) {
                    if (playerList.get(k).avatarVO.getMain()) {
                        bankerAvatar = playerList.get(k);
                    }
                }
                playerList.get(k).putCardInList(listCard.get(nextCardIndex));
                playerList.get(k).oneSettlementInfo = "";
                playerList.get(k).overOff = false;
                nextCardIndex++;
            }
        }

        assert bankerAvatar != null;
        // 庄家多一张牌
        bankerAvatar.putCardInList(listCard.get(nextCardIndex));
        // 一句结束标记 false
        singleOver = false;

        // 检测庄家有没有天胡 TODO
        if (checkHu(bankerAvatar, listCard.get(nextCardIndex))) {
            //检查有没有天胡/有则把相关联的信息放入缓存中
            huAvatar.add(bankerAvatar);
            ////二期优化注释 pickAvatarIndex = playerList.indexOf(bankerAvatar);//第一个摸牌人就是庄家
            //发送消息
            bankerAvatar.getSession().sendMsg(new HuPaiResponse(ErrorCode.SUCCESS_CODE, "hu,"));
            bankerAvatar.huAvatarDetailInfo.add(listCard.get(nextCardIndex) + ":" + 0);
        }

        // 检测杠牌 TODO

    }
    /**
     * 出牌
     * @param avatar
     * @param cardPoint
     */
    public void putOffCard(Avatar avatar, int cardPoint) {
        // 重置胡牌类型
        avatar.avatarVO.setHuType(0);

        // 出牌信息放入到缓存中，掉线重连的时候，返回房间信息需要
        avatar.avatarVO.updateChuPais(cardPoint);
        // 修改出牌 摸牌状态
        avatar.avatarVO.setHasMopaiChupai(true);
        // 已经出牌就清除所有的吃，碰，杠，胡的数组
        clearAvatar();
        // 设置上一家出牌的点数
        putOffCardPoint = cardPoint;
        // 记录当前出牌人的索引
        curAvatarIndex = playerList.indexOf(avatar);

        // 记录当前的操作

    }

    private void clearAvatar() {
        huAvatar.clear();
        penAvatar.clear();
        gangAvatar.clear();
        chiAvatar.clear();
        qishouHuAvatar.clear();
    }

    private boolean checkHu(Avatar avatar, Integer integer) {

        // 长沙麻将
        if (CHANGSHA_MAHJONG.equals(roomVO.getRoomType())) {
            return checkHuChangSha(avatar);
        }

        return true;
    }
    /**
     * 判断长沙麻将是否胡牌
     * @param avatar
     * @return
     */
    private boolean checkHuChangSha(Avatar avatar) {
        //判读有没有起手胡
        // checkQiShouHu();
        return false;
    }

    private void checkQiShouHu() {
    }

    /**
     * 随机洗牌
     */
    private void shuffleTheCards() {
        Collections.shuffle(listCard);
        Collections.shuffle(listCard);
    }

    public void setCreateRoomRoleId(String value) {
        theOwner = value;
    }

    public List<Avatar> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Avatar> playerList) {
        this.playerList = playerList;
    }

    public int getPickAvatarIndex() {
        return pickAvatarIndex;
    }

    public void setPickAvatarIndex(int pickAvatarIndex) {
        this.pickAvatarIndex = pickAvatarIndex;
    }



}
