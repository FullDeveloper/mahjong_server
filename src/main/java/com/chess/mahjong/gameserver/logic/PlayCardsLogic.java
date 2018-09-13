package com.chess.mahjong.gameserver.logic;

import com.chess.context.Rule;
import com.chess.mahjong.gameserver.Avatar;
import com.chess.mahjong.gameserver.commons.code.ErrorCode;
import com.chess.mahjong.gameserver.commons.constant.GameConstants;
import com.chess.mahjong.gameserver.msg.response.ErrorResponse;
import com.chess.mahjong.gameserver.msg.response.chupai.ChuPaiResponse;
import com.chess.mahjong.gameserver.msg.response.common.ReturnInfoResponse;
import com.chess.mahjong.gameserver.msg.response.gang.GangResponse;
import com.chess.mahjong.gameserver.msg.response.gang.OtherGangResponse;
import com.chess.mahjong.gameserver.msg.response.hu.HuPaiResponse;
import com.chess.mahjong.gameserver.msg.response.peng.PengResponse;
import com.chess.mahjong.gameserver.msg.response.pickcard.OtherPickCardResponse;
import com.chess.mahjong.gameserver.msg.response.pickcard.PickCardResponse;
import com.chess.mahjong.gameserver.pojo.*;
import com.chess.mybatis.entity.Account;
import com.chess.persist.util.StringUtil;

import java.io.IOException;
import java.util.*;

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
     * 有人要碰的數組
     */
    private List<Avatar> penAvatar = new ArrayList<>();
    /**
     * 有人要杠的數組
     */
    private List<Avatar> gangAvatar = new ArrayList<>();
    /**
     * 有人要咋吃的數組
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

    //游戏回放，
    PlayRecordGameVO playRecordGame;

    /**
     * 记录本次游戏是否已经胡了，控制摸牌
     */
    private boolean hasHu;

    /**
     * 当前玩家摸的牌的点数
     */
    private int currentCardPoint = -2;

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


        // 游戏回放记录
        playRecordInit();
    }

    private void playRecordInit() {
        playRecordGame = new PlayRecordGameVO();
        RoomVO roomVo = roomVO.clone();
        roomVo.setEndStatistics(new HashMap<String, Map<String, Integer>>());
        roomVo.setPlayerList(new ArrayList<>());
        playRecordGame.roomvo = roomVo;
        PlayRecordItemVO playRecordItemVO;
        Account account;
        StringBuffer sb;
        for (int i = 0; i < playerList.size(); i++) {
            playRecordItemVO = new PlayRecordItemVO();
            account = playerList.get(i).avatarVO.getAccount();
            playRecordItemVO.setAccountIndex(i);
            playRecordItemVO.setAccountName(account.getNickName());
            sb = new StringBuffer();
            int[] str = playerList.get(i).getPaiArray()[0];
            for (int j = 0; j < str.length; j++) {
                sb.append(str[j]).append(",");
            }
            playRecordItemVO.setCardList(sb.substring(0, sb.length() - 1));
            playRecordItemVO.setHeadIcon(account.getHeadIcon());
            playRecordItemVO.setSex(account.getSex());
            playRecordItemVO.setGameRound(roomVO.getCurrentRound());
            playRecordItemVO.setUuid(Integer.parseInt(account.getUuid()));
            playRecordGame.playerItems.add(playRecordItemVO);
        }
    }

    /**
     * 出牌
     *
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
        PlayRecordOperation(curAvatarIndex, cardPoint, GameConstants.CHUPAi_OPTION, -1, null, null);
        // 把出的牌从玩家的牌集合中删除
        avatar.pullCardFormList(putOffCardPoint);

        // 通知其他玩家当前玩家出的牌
        for (int i = 0; i < playerList.size(); i++) {
            //不能返回给自己
            if (i != curAvatarIndex) {
                playerList.get(i).getSession().sendMsg(new ChuPaiResponse(ErrorCode.SUCCESS_CODE, putOffCardPoint, curAvatarIndex));
            } else {
                // 每次出牌就先清除缓存里面的可以杠的牌下标
                playerList.get(i).gangIndex.clear();
            }
        }

        // 房间为可抢杠胡
        if (avatar.getRoomVO().getZiMo() == 0 && !avatar.getRoomVO().getHong()) {
            // TODO
        } else {
            Avatar ava;
            StringBuffer sb;
            for (int i = 0; i < playerList.size(); i++) {
                ava = playerList.get(i);
                if (!ava.getUuId().equals(avatar.getUuId())) {
                    sb = new StringBuffer();
                    // 检查其他玩家是否可以杠牌
                    if (ava.checkGang(putOffCardPoint)) {
                        // 可杠牌的玩家添加
                        gangAvatar.add(ava);
                        //同时传会杠的牌的点数
                        sb.append("gang:").append(putOffCardPoint).append(",");
                    }
                    // 检查是否可以碰牌
                    if (ava.checkPeng(putOffCardPoint)) {
                        penAvatar.add(ava);
                        sb.append("peng:" + curAvatarIndex + ":" + putOffCardPoint + ",");
                    }
                    if (sb.length() > 1) {
                        // 通知该玩家进行杠牌或者碰牌
                        ava.getSession().sendMsg(new ReturnInfoResponse(ErrorCode.SUCCESS_CODE, sb.toString()));
                    }
                }
            }
        }
        //如果没有吃，碰，杠，胡的情况，则下家自动摸牌
        chuPaiCallBack();
    }

    /**
     * 出牌返回出牌点数和下一家玩家信息
     *
     * @param
     */
    private void chuPaiCallBack() {
        //把出牌点数和下面该谁出牌发送会前端  下一家都还没有摸牌就要出牌了??
        if (!hasHu && checkMsgAndSend()) {
            //如果没有吃，碰，杠，胡的情况，则下家自动摸牌
            pickCard();
        }
    }

    /**
     * 摸牌
     */
    private void pickCard() {
        clearAvatar();
        //摸牌 设置摸牌人的索引
        pickAvatarIndex = getNextAvatarIndex();
        //本次摸得牌点数，下一张牌的点数，及本次摸的牌点数
        int tempPoint = getNextCardPoint();

        if (tempPoint != -1) {
            // 记录回放记录
            PlayRecordOperation(pickAvatarIndex, tempPoint, GameConstants.MOPAI_OPTION, -1, null, null);
            // 记录当前玩家摸的牌的点数
            currentCardPoint = tempPoint;

            // 获取当前摸牌玩家的信息
            Avatar avatar = playerList.get(pickAvatarIndex);
            // 修改出牌 摸牌状态
            avatar.avatarVO.setHasMopaiChupai(true);
            avatar.qiangHu = true;
            avatar.canHu = true;
            // 重置划水麻将胡牌格式
            avatar.avatarVO.setHuType(0);
            // 告诉该玩家他摸得牌
            avatar.getSession().sendMsg(new PickCardResponse(ErrorCode.SUCCESS_CODE, tempPoint));

            // 告诉其他玩家该玩家摸牌事件
            for (int i = 0; i < playerList.size(); i++) {
                if (i != pickAvatarIndex) {
                    playerList.get(i).getSession().sendMsg(new OtherPickCardResponse(ErrorCode.SUCCESS_CODE, pickAvatarIndex));
                } else {
                    // 每次摸牌就先清除缓存里面的可以杠的牌下标
                    playerList.get(i).gangIndex.clear();
                }
            }

            // 判断自己摸上来的牌自己是否可以胡
            StringBuffer sb = new StringBuffer();
            // 将摸起来的牌加入到自己的牌中
            avatar.putCardInList(tempPoint);

            // 检测当前是否可以杠牌
            if (avatar.checkSelfGang()) {
                gangAvatar.add(avatar);
                sb.append("gang");
                for (int i : avatar.gangIndex) {
                    sb.append(":" + i);
                }
                sb.append(",");
                //avatar.gangIndex.clear();//9-18出牌了才清楚(在杠时断线重连后需要这里面的数据)
            }
            // 检测当前是否可以胡牌
            if (checkAvatarIsHuPai(avatar, 100, "mo")) {
                huAvatar.add(avatar);
                sb.append("hu,");
            }

        } else {
            // 没牌了 TODO
        }
    }

    /**
     * 吃牌
     *
     * @param avatar
     * @param cardVo
     * @return
     */
    public boolean chiCard(Avatar avatar, CardVO cardVo) {
        //碰，杠都比吃优先
        boolean flag = false;
        if (roomVO.getRoomType().equals(CHANGSHA_MAHJONG)) {
            // 如果没有胡牌 没有碰牌 没有杠牌 并且有吃牌的情况下
            if (huAvatar.size() == 0 && penAvatar.size() == 0 && gangAvatar.size() == 0 && chiAvatar.size() > 0) {
                // 如果吃牌数组里面有当前对象
                if (chiAvatar.contains(avatar)) {
                    avatar.putCardInList(cardVo.getCardPoint());
                    avatar.setCardListStatus(cardVo.getCardPoint(), GameConstants.CHIPAI_OPTION);
                    clearArrayAndSetQuest();
                    flag = true;
                    for (int i = 0; i < playerList.size(); i++) {
                        if (avatar.getUuId().equals(playerList.get(i).getUuId())) {
                            // 吃牌逻辑待处理 TODO
                        }
                    }
                }
            }
        } else {
            // 只有长沙麻将才可以吃牌
        }
        return flag;
    }

    /**
     * 清理胡杠碰吃数组，并把玩家的请求状态全部设置为false;
     */
    public void clearArrayAndSetQuest() {
        while (gangAvatar.size() > 0) {
            gangAvatar.remove(0).setQuestToFalse();
        }
        while (penAvatar.size() > 0) {
            penAvatar.remove(0).setQuestToFalse();
        }
        while (chiAvatar.size() > 0) {
            chiAvatar.remove(0).setQuestToFalse();
        }
    }

    /**
     * 检测玩家是否胡牌了
     *
     * @param avatar
     * @param cardIndex
     * @param type      当type为""
     */
    public boolean checkAvatarIsHuPai(Avatar avatar, int cardIndex, String type) {
        if (cardIndex != 100) {
            //传入的参数牌索引为100时表示天胡/或是摸牌，不需要再在添加到牌组中
            //System.out.println("检测胡牌的时候------添加别人打的牌："+cardIndex);
            avatar.putCardInList(cardIndex);
        }

        // TODO 胡牌规则 需要编写
        if (checkHu(avatar, cardIndex)) {
            //System.out.println("确实胡牌了");
            //System.out.println(avatar.printPaiString() +"  avatar = "+avatar.avatarVO.getAccount().getNickname());
            if (type.equals("chu")) {
                //System.out.println("检测胡牌成功的时候------移除别人打的牌："+cardIndex);
                avatar.pullCardFormList(cardIndex);
            } else if (type.equals("ganghu")) {
                //划水麻将杠上花  ，大胡
                avatar.avatarVO.setHuType(2);
            }
            return true;
        } else {
            //System.out.println("没有胡牌");
            if (type.equals("chu")) {
                //System.out.println("检测胡牌失败的时候------移除别人打的牌："+cardIndex);
                avatar.pullCardFormList(cardIndex);
            }
            return false;
        }
    }

    /**
     * 获取下一张牌的点数,如果返回为-1 ，则没有牌了
     *
     * @return
     */
    public int getNextCardPoint() {
        nextCardIndex++;
        if (nextCardIndex < listCard.size()) {
            return listCard.get(nextCardIndex);
        }
        return -1;
    }

    /**
     * 获取下一位摸牌人的索引
     *
     * @return
     */
    public int getNextAvatarIndex() {
        int nextIndex = curAvatarIndex + 1;
        if (nextIndex >= 4) {
            nextIndex = 0;
        }
        return nextIndex;
    }

    /**
     * 發送吃，碰，杠，胡牌信息
     *
     * @return
     */
    private boolean checkMsgAndSend() {
        if (huAvatar.size() > 0) {
            return false;
        }
        if (gangAvatar.size() > 0) {
            return false;
        }
        if (penAvatar.size() > 0) {
            return false;
        }
        if (chiAvatar.size() > 0) {
            return false;
        }
        return true;
    }

    /**
     * 游戏回放，记录打牌操作信息
     *
     * @param curAvatarIndex 操作玩家索引
     * @param cardIndex      操作相关牌索引
     * @param type           1出牌，2摸牌，3吃，4碰，5杠，6胡(自摸/点炮),7抢胡,8抓码,9:流局.....
     * @param gangType       type不为杠时 传入 -1
     * @param ma             不是抓码操作时 为null
     * @param valideMa
     */
    private void PlayRecordOperation(int curAvatarIndex, int cardIndex, int type, int gangType, String ma, List<Integer> valideMa) {
        PlayBehaviedVO behaviedvo = new PlayBehaviedVO();
        behaviedvo.setAccountIndexId(curAvatarIndex);
        behaviedvo.setCardIndex(cardIndex + "");
        behaviedvo.setRecordIndex(playRecordGame.behavieList.size());
        behaviedvo.setType(type);
        behaviedvo.setGangType(gangType);
        if (StringUtil.isNotEmpty(ma)) {
            behaviedvo.setMa(ma);
            behaviedvo.setValideMa(valideMa);
        }
        playRecordGame.behavieList.add(behaviedvo);
    }

    /**
     * 玩家选择放弃操作
     *
     * @param avatar
     * @param
     */
    public void gaveUpAction(Avatar avatar) {
        if (validateStatus()) {
            // 清空用户的胡牌详情信息
            avatar.huAvatarDetailInfo.clear();
            // 重置划水麻将胡牌格式
            avatar.avatarVO.setHuType(0);
            if (pickAvatarIndex == playerList.indexOf(avatar)) {
                //如果是自己摸的过，则 canHu = true；
                avatar.canHu = true;
                clearAvatar();
            } else {
                // 执行过操作
                //如果别人打的牌过，放弃胡，则检测有没人杠
                // 将该用户从胡牌集合中删除
                if (huAvatar.contains(avatar)) {
                    huAvatar.remove(avatar);
                    avatar.canHu = false;
                    avatar.qiangHu = false;
                }
                // 从杠牌集合中删除
                if (gangAvatar.contains(avatar)) {
                    gangAvatar.remove(avatar);
                    avatar.gangIndex.clear();
                }
                // 从碰牌集合中删除
                if (penAvatar.contains(avatar)) {
                    penAvatar.remove(avatar);
                }
                // 从吃牌集合中删除
                if (chiAvatar.contains(avatar)) {
                    chiAvatar.remove(avatar);
                }

                // 如果没人胡牌
                if (huAvatar.size() == 0) {
                    // 检测是否有杠牌的人
                    for (Avatar item : gangAvatar) {
                        if (item.gangQuest) {
                            avatar.qiangHu = false;
                            //进行这个玩家的杠操作，并且把后面的碰，吃数组置为0; TODO
                            gangCard(item, putOffCardPoint, 1);
                            clearArrayAndSetQuest();
                            return;
                        }
                    }
                }

            }
        }
    }

    /**
     * @param avatar    杠牌的人
     * @param cardPoint 上一家出的牌的点数
     * @param gangType  杠的类型
     */
    private boolean gangCard(Avatar avatar, int cardPoint, int gangType) {
        boolean flag = false;
        // 如果是划水麻将 暂时不处理 TODO
        if (roomVO.getRoomType().equals(2)) {

        }
        // 拿到该玩家的索引
        int avatarIndex = playerList.indexOf(avatar);
        // 判断杠牌的集合中是否有人
        if (gangAvatar.size() > 0) {
            // 如果胡牌的是自己，或者没有人胡牌
            if ((huAvatar.contains(avatar) && huAvatar.size() == 1) || huAvatar.size() == 0) {
                // 修改出牌 摸牌状态
                avatar.avatarVO.setHasMopaiChupai(true);
                // 将该玩家从胡牌的集合中删除
                if (huAvatar.contains(avatar)) {
                    huAvatar.remove(avatar);
                }
                // 从碰牌的集合中删除
                if (penAvatar.contains(avatar)) {
                    penAvatar.remove(avatar);
                }
                // 从吃牌的集合中删除
                if (chiAvatar.contains(avatar)) {
                    chiAvatar.remove(avatar);
                }
                // 判断杠牌中是否拥有该玩家
                if (gangAvatar.contains(avatar)) {
                    gangAvatar.remove(avatar);
                    // 判断杠的类型，自杠，还是点杠
                    String str = null;
                    int type = 0;
                    // 分数
                    int score = 0;
                    // 暗杠 4 ， 明杠 5(用于统计不同type下的次数和得分)
                    String recordType = null;
                    // 结束统计类型
                    String endStatisticsType = null;
                    // 游戏回放 记录杠的类型
                    int playRecordType;
                    if (avatar.getUuId().equals(playerList.get(pickAvatarIndex).getUuId())) {
                        // 自杠**********自己摸牌*************自杠
                        // 自杠(明杠或暗杠)，，这里的明杠时需要判断本房间是否是抢杠胡的情况，
                        // 如果是抢杠胡，则其他玩家有胡牌的情况下，可以胡
                        String strs = avatar.getResultRelation().get(1);
                        if (strs != null && strs.contains(cardPoint + "")) {
                            playRecordType = 3;
                            // 明杠（划水麻将里面的过路杠）
                            if (avatar.getRoomVO().getZiMo() == 0 && checkQiangHu(avatar, cardPoint)) {
                                //如果是抢杠胡，则判断其他玩家有胡牌的情况，有则给予提示 //判断其他三家是否能抢杠胡。
                                //如果抢胡了，则更新上家出牌的点数为  杠的牌
                                putOffCardPoint = cardPoint;
                                gangAvatar.add(avatar);
                                avatar.gangQuest = true;
                                //回放记录
                                PlayRecordOperation(avatarIndex, cardPoint, 5, 4, null, null);
                                return false;
                            } else {
                                //存储杠牌的信息，
                                avatar.putResultRelation(GameConstants.GANG_PAI, cardPoint + "");
                                // 设置该牌为杠牌
                                avatar.avatarVO.getPaiArray()[1][cardPoint] = GameConstants.GANG_PAI;
                                avatar.getPaiArray()[1][cardPoint] = GameConstants.GANG_PAI;
                                //杠牌标记2
                                avatar.setCardListStatus(cardPoint, GameConstants.GANG_PAI);

                                // 处理长沙麻将 TODO
                                if (CHANGSHA_MAHJONG.equals(roomVO.getRoomType())) {
                                    str = "0:" + cardPoint + ":" + Rule.GANG_MING;
                                    type = 0;
                                    score = 1;
                                    // 暗杠
                                    recordType = GameConstants.AN_GANG;
                                    endStatisticsType = "minggang";
                                }

                            }
                        } else {
                            playRecordType = 2;
                            //存储杠牌的信息，
                            avatar.putResultRelation(GameConstants.GANG_PAI, cardPoint + "");
                            // 设置该牌为杠牌
                            avatar.avatarVO.getPaiArray()[1][cardPoint] = GameConstants.GANG_PAI;
                            avatar.getPaiArray()[1][cardPoint] = GameConstants.GANG_PAI;
                            //杠牌标记2
                            avatar.setCardListStatus(cardPoint, GameConstants.GANG_PAI);
                            // 处理长沙麻将 TODO
                            if (CHANGSHA_MAHJONG.equals(roomVO.getRoomType())) {
                                //长沙麻将
                                str = "0:" + cardPoint + ":" + Rule.GANG_AN;
                                type = 1;
                                score = 2;
                                // 明杠
                                recordType = GameConstants.MING_GANG;
                                endStatisticsType = "angang";
                            }
                        }
                        for (Avatar ava : playerList) {
                            if (ava.getUuId().equals(avatar.getUuId())) {
                                //修改玩家整个游戏总分和杠的总分
                                avatar.avatarVO.getHuReturnObjectVO().updateGangAndHuInfos(recordType, score * 3);
                                //整个房间统计每一局游戏 杠，胡的总次数
                                roomVO.updateEndStatistics(ava.getUuId(), endStatisticsType, 1);
                            } else {
                                //修改其他三家的分数
                                ava.avatarVO.getHuReturnObjectVO().updateGangAndHuInfos(recordType, -1 * score);
                            }
                        }
                        flag = true;
                    } else {
                        //存储杠牌的信息，
                        playRecordType = 1;
                        avatar.putResultRelation(GameConstants.GANG_PAI, cardPoint + "");
                        // 设置该牌为杠牌
                        avatar.avatarVO.getPaiArray()[1][cardPoint] = GameConstants.GANG_PAI;
                        avatar.getPaiArray()[1][cardPoint] = GameConstants.GANG_PAI;
                        //杠牌标记2
                        avatar.setCardListStatus(cardPoint, GameConstants.GANG_PAI);
                        // 点杠(分在明杠里面)（划水麻将里面的放杠）
                        // 把出的牌从出牌玩家的chupais中移除掉
                        playerList.get(curAvatarIndex).avatarVO.removeLastChuPais();
                        // 更新牌组(点杠时才需要更新)   自摸时不需要更新
                        flag = avatar.putCardInList(cardPoint);

                        if (roomVO.getRoomType().equals(CHANGSHA_MAHJONG)) {
                            //长沙麻将
                            score = 3;
                            recordType = "5";
                            str = playerList.get(curAvatarIndex).getUuId() + ":" + cardPoint + ":" + Rule.GANG_DIAN;
                            type = 0;
                            endStatisticsType = "minggang";
                        }

                        //减点杠玩家的分数
                        playerList.get(curAvatarIndex).avatarVO.getHuReturnObjectVO().updateGangAndHuInfos(recordType, -1 * score);
                        //增加杠家的分数
                        avatar.avatarVO.getHuReturnObjectVO().updateGangAndHuInfos(recordType, score);
                        //整个房间统计每一局游戏 杠，胡的总次数
                        roomVO.updateEndStatistics(avatar.getUuId() + "", endStatisticsType, 1);
                    }
                    avatar.avatarVO.getHuReturnObjectVO().updateTotalInfo("gang", str);
                    //回放记录
                    PlayRecordOperation(avatarIndex, cardPoint, 5, playRecordType, null, null);

                    clearArrayAndSetQuest();
                    if (gangType == 0) {
                        //可以换牌的情况只补一张牌
                        //摸牌并判断自己摸上来的牌自己是否可以胡/可以杠****
                        for (int i = 0; i < playerList.size(); i++) {
                            if (!avatar.getUuId().equals(playerList.get(i).getUuId())) {
                                //杠牌返回给其他人只返回杠的类型和杠牌的玩家位置
                                playerList.get(i).getSession().sendMsg(new OtherGangResponse(ErrorCode.SUCCESS_CODE, cardPoint, avatarIndex, type));
//    							 responseMsg = new OtherGangResponse(1,cardPoint,avatarIndex,type);
//    							 lastAvtar = playerList.get(i);
                            } else {
                                // 杠牌返回给其他人只返回杠的类型和杠牌的玩家位置
                                playerList.get(i).getSession().sendMsg(new GangResponse(ErrorCode.SUCCESS_CODE, 1, 1, type));
                            }
                        }
                        // 杠了别人(type)/自己摸杠了之后摸牌起来 然后再检测是否可以胡  可以杠等情况 TODO
                        //pickCardAfterGang(avatar);
                    }
                }
            } else {
                if (gangAvatar.size() > 0) {
                    for (Avatar ava : gangAvatar) {
                        ava.gangQuest = true;
                    }
                }
            }
        } else {
            if (gangAvatar.size() > 0) {
                for (Avatar ava : gangAvatar) {
                    ava.gangQuest = true;
                }
            }
            try {
                playerList.get(avatarIndex).getSession().sendMsg(new ErrorResponse(ErrorCode.Error_000016));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }


    /**
     * 碰牌
     *
     * @param avatar
     * @return
     */
    public boolean pengCard(Avatar avatar, int cardIndex) {
        boolean flag = false;
        //这里可能是自己能胡能碰能杠 但是选择碰
        if (cardIndex != putOffCardPoint) {
            System.out.println("传入错误的牌:传入的牌" + cardIndex + "---上一把出牌：" + putOffCardPoint);
        }

        if (cardIndex < 0) {
            try {
                avatar.getSession().sendMsg(new ErrorResponse(ErrorCode.Error_000019));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        // 只有这种情况下才可以进行操作
        if ((penAvatar.size() >= 1 && huAvatar.size() == 0) || (huAvatar.contains(avatar) && huAvatar.size() == 1 && penAvatar.size() == 1)) {

            // 修改出牌 摸牌状态
            avatar.avatarVO.setHasMopaiChupai(true);

            // 选择碰牌的话 如果胡牌里面该玩家可以胡 则将该玩家从胡牌集合中剔除
            if (huAvatar.contains(avatar)) {
                huAvatar.remove(avatar);
            }

            // 从杠牌中剔除
            if (gangAvatar.contains(avatar)) {
                gangAvatar.remove(avatar);
            }

            if (penAvatar.contains(avatar)) {
                // 记录回放记录
                PlayRecordOperation(playerList.indexOf(avatar), cardIndex, GameConstants.RECORD_TYPE_PENG, -1, null, null);
                // 把出的牌从出牌玩家的chupais中移除掉
                playerList.get(curAvatarIndex).avatarVO.removeLastChuPais();

                penAvatar.remove(avatar);

                // 更新牌组
                flag = avatar.putCardInList(cardIndex);
                // 更新该玩家该牌的状态为碰牌
                avatar.setCardListStatus(cardIndex, GameConstants.PENG_PAI);
                //把各个玩家碰的牌记录到缓存中去,牌的index
                avatar.avatarVO.getHuReturnObjectVO().updateTotalInfo("peng", cardIndex + "");

                clearArrayAndSetQuest();
                for (int i = 0; i < playerList.size(); i++) {
                    if (playerList.get(i).getUuId().equals(avatar.getUuId())) {
                        //碰了的牌放入到avatar的resultRelation  Map中
                        playerList.get(i).putResultRelation(GameConstants.PENG_PAI, cardIndex + "");
                        playerList.get(i).avatarVO.getPaiArray()[1][cardIndex] = GameConstants.PENG_PAI;
                        avatar.getPaiArray()[1][cardIndex] = GameConstants.PENG_PAI;
                    }
                    playerList.get(i).getSession().sendMsg(new PengResponse(ErrorCode.SUCCESS_CODE, cardIndex, playerList.indexOf(avatar)));
                }
                //更新摸牌人信息 2016-8-3
                pickAvatarIndex = playerList.indexOf(avatar);
                curAvatarIndex = playerList.indexOf(avatar);
                //断线重连判断该自己出牌
                currentCardPoint = -2;
            }

        } else {
            if(penAvatar.size() > 0) {
                for (Avatar ava : penAvatar) {
                    ava.pengQuest = true;
                }
            }
        }

        return flag;
    }

    /**
     * 在可以抢杠胡的情况下，判断其他人有没胡的情况
     *
     * @return boolean
     */
    private boolean checkQiangHu(Avatar avatar, int cardPoint) {
        return true;
    }

    /**
     * 检测当，缓存数组里全部为空时，放弃操作，则不起作用
     */
    public boolean validateStatus() {
        if (huAvatar.size() > 0 || penAvatar.size() > 0 || gangAvatar.size() > 0 || chiAvatar.size() > 0 || qishouHuAvatar.size() > 0) {
            return true;
        } else {
            return false;
        }
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
     *
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
