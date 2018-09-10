package com.chess.mahjong.gameserver.logic;

import com.chess.mahjong.gameserver.Avatar;
import com.chess.mahjong.gameserver.commons.code.ErrorCode;
import com.chess.mahjong.gameserver.manager.RoomManager;
import com.chess.mahjong.gameserver.msg.response.ErrorResponse;
import com.chess.mahjong.gameserver.msg.response.joinroom.JoinRoomNotice;
import com.chess.mahjong.gameserver.msg.response.joinroom.JoinRoomResponse;
import com.chess.mahjong.gameserver.msg.response.login.BackLoginResponse;
import com.chess.mahjong.gameserver.msg.response.login.OtherBackLoginResponse;
import com.chess.mahjong.gameserver.msg.response.startgame.PrepareGameResponse;
import com.chess.mahjong.gameserver.msg.response.startgame.StartGameResponse;
import com.chess.mahjong.gameserver.pojo.AvatarVO;
import com.chess.mahjong.gameserver.pojo.CardVO;
import com.chess.mahjong.gameserver.pojo.HuReturnObjectVO;
import com.chess.mahjong.gameserver.pojo.RoomVO;
import com.chess.mybatis.entity.Account;
import com.chess.mybatis.service.AccountService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 周润斌
 * Date: 2018/9/5
 * Time: 16:13
 * Description:
 */
public class RoomLogic {

    private RoomVO roomVO;

    /**
     * 房主
     */
    private Avatar createAvator;

    /**
     * 房间可以使用的次数
     */
    private int count = 0;

    /**
     * 玩家列表
     */
    private List<Avatar> playerList;
    /**
     * 玩牌逻辑
     */
    private PlayCardsLogic playCardsLogic;


    /**
     * 同意解散房间的人数
     */
    private int dissolveCount = 1;

    /**
     * 记录是否已经有人申请解散房间
     */
    private boolean dissolve = true;

    private boolean begin = false;

    public RoomLogic(RoomVO roomVO) {
        this.roomVO = roomVO;
        if (roomVO != null) {
            count = roomVO.getRoundNumber();
        }
    }

    public void CreateRoom(Avatar avatar) {
        createAvator = avatar;

        // 设置玩家所属房间
        avatar.setRoomVO(roomVO);
        // 设置为房主
        avatar.avatarVO.setMain(true);

        // 将该玩家加入到对应房间的玩家中
        List<AvatarVO> playerLists = new ArrayList<>();
        playerLists.add(avatar.avatarVO);
        roomVO.setPlayerList(playerLists);

        // 初始化玩家列表
        playerList = new ArrayList<>();
        playerList.add(avatar);

    }

    public List<Avatar> getPlayerList() {
        return playerList;
    }

    /**
     * 断线重连，如果房间还未被解散的时候，则返回整个房间信息
     *
     * @param avatar
     */
    public void returnBackAction(Avatar avatar) {
        if (playCardsLogic == null) {
            //只是在房间，游戏尚未开始,打牌逻辑为空
            for (int i = 0; i < playerList.size(); i++) {
                if (playerList.get(i).getUuId() != avatar.getUuId()) {
                    //给其他三个玩家返回重连用户信息
                    playerList.get(i).getSession().sendMsg(new OtherBackLoginResponse(ErrorCode.SUCCESS_CODE, avatar.getUuId() + ""));
                }
            }
            // 通知客户端直接进入房间
            avatar.getSession().sendMsg(new BackLoginResponse(ErrorCode.SUCCESS_CODE, roomVO));
        } else {
            // 游戏开始之后的断线重连 TODO
            // playCardsLogic.returnBackAction(avatar);
        }
    }

    /**
     * 房间内玩家数量最大值
     */
    private static final Integer MAX_PLAYER_COUNT = 4;

    /**
     * 用户进入房间
     *
     * @param avatar
     */
    public boolean intoRoom(Avatar avatar) {
        synchronized (this) {
            // 房间内玩家达到数量 无法继续加入
            if (playerList.size() == MAX_PLAYER_COUNT) {
                try {
                    avatar.getSession().sendMsg(new ErrorResponse(ErrorCode.Error_000011));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            } else {
                // 并不是房主
                avatar.avatarVO.setMain(false);
                // 设置用户所属房间
                avatar.avatarVO.setRoomId(roomVO.getRoomId());
                // 设置用户所属房间的信息
                avatar.setRoomVO(roomVO);
                // 通知房间内其他玩家该玩家进入房间
                noticeJoinRoomOtherPlayer(avatar);
                // 将该玩家加入到本房间内的玩家集合中
                playerList.add(avatar);
                // 将该玩家的信息加入到房间对象中
                roomVO.getPlayerList().add(avatar.avatarVO);
                // 将玩家的编号和房间号对应起来
                RoomManager.getInstance().addUuidAndRoomId(avatar.avatarVO.getAccount().getUuid(), roomVO.getRoomId());
                avatar.getSession().sendMsg(new JoinRoomResponse(ErrorCode.SUCCESS_CODE, roomVO));
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
    }

    private void noticeJoinRoomOtherPlayer(Avatar avatar) {
        AvatarVO avatarVO = avatar.avatarVO;
        for (int i = 0; i < playerList.size(); i++) {
            // 通知房间内其他玩家该玩家上限 将该玩家的信息告诉他们
            playerList.get(i).getSession().sendMsg(new JoinRoomNotice(ErrorCode.SUCCESS_CODE, avatarVO));
        }
    }

    public void readyGame(Avatar avatar) throws IOException {
        // 只有单局结束之后调用准备接口才有用10-11新增
        // 如果是没开始过一局。   或者是一局已经结束
        if (count == roomVO.getRoundNumber() || (playCardsLogic.singleOver && count != roomVO.getRoundNumber())) {
            // 房间次数已经用完
            if (count <= 0) {
                //房间次数已经为0
                for (Avatar ava : playerList) {
                    ava.getSession().sendMsg(new ErrorResponse(ErrorCode.Error_000010));
                }
            } else {
                // 设置该用户为准备状态
                avatar.avatarVO.setReady(true);
                // 拿到用户的索引 是第几个进入房间的
                int avatarIndex = playerList.indexOf(avatar);
                //成功则返回
                for (Avatar ava : playerList) {
                    ava.getSession().sendMsg(new PrepareGameResponse(ErrorCode.SUCCESS_CODE, avatarIndex));
                }

                // 检查是否可以开始游戏
                checkCanBeStartGame();

            }
        }

    }

    /**
     * 检查是否可以开始新的游戏
     */
    private void checkCanBeStartGame() {
        if (playerList.size() == MAX_PLAYER_COUNT) {
            //房间里面4个人且都准备好了则开始游戏
            boolean flag = true;
            for (Avatar avatar : playerList) {
                if (!avatar.avatarVO.getReady()) {
                    //还有人没有准备
                    flag = false;
                    break;
                }
            }
            // 如果是true则可以开始游戏了。
            if (flag) {
                begin = true;
                // 执行开始游戏操作
                startGameRound();
            }
        }

    }

    /**
     * 开始一回合新的游戏
     */
    private void startGameRound() {

        if (count <= 0) {
            // 房间次数用完了,通知所有玩家
            for (Avatar avatar : playerList) {
                try {
                    avatar.getSession().sendMsg(new ErrorResponse(ErrorCode.Error_000010));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            count--;
            // 设置当前游戏局数
            roomVO.setCurrentRound(roomVO.getCurrentRound() + 1);

            // 判断是否为第一局游戏
            if ((count + 1) != roomVO.getRoundNumber()) {
                // 不是第一局
            } else {
                // 是第一局
                playCardsLogic = new PlayCardsLogic();
                //第一局  摸牌玩家索引初始值为0
                playCardsLogic.setPickAvatarIndex(0);
            }
        }

        // 设置创建房间者编号
        playCardsLogic.setCreateRoomRoleId(createAvator.getUuId());
        // 设置玩家列表
        playCardsLogic.setPlayerList(playerList);
        // 初始化玩家的牌
        playCardsLogic.initCard(roomVO);


        Avatar avatar;
        Account account;
        for (int i = 0; i < playerList.size(); i++) {
            //清除各种数据  1：本局胡牌时返回信息组成对象 ，
            avatar = playerList.get(i);
            //重置是否准备状态 10-11新增
            avatar.avatarVO.setReady(false);
            avatar.avatarVO.setHuReturnObjectVO(new HuReturnObjectVO());
            avatar.getSession().sendMsg(new StartGameResponse(ErrorCode.SUCCESS_CODE, avatar.avatarVO.getPaiArray(), playerList.indexOf(playCardsLogic.bankerAvatar)));
            //修改玩家是否玩一局游戏的状态
            account = AccountService.getInstance().selectByPrimaryKey(avatar.avatarVO.getAccount().getId());
            if ("0".equals(account.getPlayGame())) {
                account.setPlayGame("1");
                AccountService.getInstance().updateByPrimaryKeySelective(account);
                avatar.avatarVO.getAccount().setPlayGame("1");
            }
        }

    }

    /**
     * 出牌
     * @param avatar
     * @param cardPoint
     */
    public void chuCard(Avatar avatar, int cardPoint) {
        playCardsLogic.putOffCard(avatar, cardPoint);
    }

    /**
     * 玩家选择放弃操作
     *
     * @param avatar
     * @param //1-胡，2-杠，3-碰，4-吃
     */
    public void gaveUpAction(Avatar avatar) {
        playCardsLogic.gaveUpAction(avatar);
    }

    /**
     * 吃牌
     *
     * @param avatar
     * @return
     */
    public boolean chiCard(Avatar avatar, CardVO cardVo) {
        return playCardsLogic.chiCard(avatar, cardVo);
    }

    public void setDissolveCount(int dissolveCount) {
        this.dissolveCount = dissolveCount;
    }

    public void setDissolve(boolean dissolve) {
        this.dissolve = dissolve;
    }



}
