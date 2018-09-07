package com.chess.mahjong.gameserver.logic;

import com.chess.mahjong.gameserver.Avatar;
import com.chess.mahjong.gameserver.commons.code.ErrorCode;
import com.chess.mahjong.gameserver.commons.constant.GameConstants;
import com.chess.mahjong.gameserver.msg.response.chupai.ChuPaiResponse;
import com.chess.mahjong.gameserver.msg.response.common.ReturnInfoResponse;
import com.chess.mahjong.gameserver.msg.response.hu.HuPaiResponse;
import com.chess.mahjong.gameserver.msg.response.pickcard.OtherPickCardResponse;
import com.chess.mahjong.gameserver.msg.response.pickcard.PickCardResponse;
import com.chess.mahjong.gameserver.pojo.PlayBehaviedVO;
import com.chess.mahjong.gameserver.pojo.PlayRecordGameVO;
import com.chess.mahjong.gameserver.pojo.PlayRecordItemVO;
import com.chess.mahjong.gameserver.pojo.RoomVO;
import com.chess.mybatis.entity.Account;
import com.chess.persist.util.StringUtil;

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
