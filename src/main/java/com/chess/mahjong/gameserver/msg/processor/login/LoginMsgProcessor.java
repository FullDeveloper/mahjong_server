package com.chess.mahjong.gameserver.msg.processor.login;

import com.chess.mahjong.gameserver.Avatar;
import com.chess.mahjong.gameserver.commons.code.ErrorCode;
import com.chess.mahjong.gameserver.commons.constant.GameConstants;
import com.chess.mahjong.gameserver.commons.initial.Params;
import com.chess.mahjong.gameserver.commons.session.GameSession;
import com.chess.mahjong.gameserver.context.GameServerContext;
import com.chess.mahjong.gameserver.logic.RoomLogic;
import com.chess.mahjong.gameserver.manager.GameSessionManager;
import com.chess.mahjong.gameserver.manager.RoomManager;
import com.chess.mahjong.gameserver.msg.processor.INotAuthProcessor;
import com.chess.mahjong.gameserver.msg.processor.MsgProcessor;
import com.chess.mahjong.gameserver.msg.response.host.HostNoticeResponse;
import com.chess.mahjong.gameserver.msg.response.login.LoginResponse;
import com.chess.mahjong.gameserver.pojo.AvatarVO;
import com.chess.mahjong.gameserver.pojo.LoginVO;
import com.chess.mybatis.entity.Account;
import com.chess.mybatis.entity.NoticeTable;
import com.chess.mybatis.service.AccountService;
import com.chess.mybatis.service.NoticeTableService;
import com.chess.network.codec.message.ClientRequest;
import com.chess.persist.util.JsonUtilTool;

import java.util.Date;

/**
 * @author 周润斌
 * Date: 2018/9/5
 * Time: 11:15
 * Description:
 */
public class LoginMsgProcessor extends MsgProcessor implements INotAuthProcessor {
    @Override
    public void process(GameSession gameSession, ClientRequest request) throws Exception {
        String message = request.getString();
        LoginVO loginVO = JsonUtilTool.fromJson(message, LoginVO.class);
        Account account = AccountService.getInstance().selectAccount(loginVO.getOpenId());
        if (account == null) {
            // 创建新用户并登陆
            account = new Account();
            account.setOpenId(loginVO.getOpenId());
            account.setUuid((AccountService.getInstance().selectMaxId() + 100000) + "");
            account.setRoomCard(Params.initialRoomCard);
            account.setHeadIcon(loginVO.getHeadIcon());
            account.setNickName(loginVO.getNickName());
            account.setCity(loginVO.getCity());
            account.setProvince(loginVO.getProvince());
            account.setSex(loginVO.getSex());
            account.setUnionId(loginVO.getUnionId());
            account.setCreateTime(new Date());
            account.setActualCard(Params.initialRoomCard);
            account.setTotalCard(Params.initialRoomCard);
            account.setStatus("0");
            account.setPlayGame("0");

            if (AccountService.getInstance().createAccount(account) == 0) {
                // 插入不成功 TODO


            } else {
                // 设置当前登陆玩家信息
                Avatar tempAva = new Avatar();
                AvatarVO tempAvaVo = new AvatarVO();
                tempAvaVo.setAccount(account);
                tempAvaVo.setIp(loginVO.getIP());
                tempAva.avatarVO = tempAvaVo;

                // 执行登陆操作
                loginAction(gameSession, tempAva);

                //把session放入到GameSessionManager
                GameSessionManager.getInstance().putGameSessionInHashMap(gameSession, tempAva.getUuId());

                // 发送公告给玩家
                Thread.sleep(3000);
                NoticeTable notice = NoticeTableService.getInstance().selectRecentlyObject();
                String content = notice.getContent();
                gameSession.sendMsg(new HostNoticeResponse(ErrorCode.SUCCESS_CODE, content));
            }
        } else {
            // TODO
            // 如果玩家是掉线的，则直接从缓存(GameServerContext)中取掉线玩家的信息
            // 判断用户是否已经进行断线处理(如果前端断线时间过短，后台则可能还未来得及把用户信息放入到离线map里面，就已经登录了，所以取出来就会是空)
            Thread.sleep(1000);

            // 从在线用户中获取用户
            Avatar avatar = GameServerContext.getAvatarFromOnLine(account.getUuid());

            // 如果没有拿到 就从离线集合中获取
            if (avatar == null) {
                avatar = GameServerContext.getAvatarFromOff(account.getUuid());
            }

            // 在线离线列表中都不存在 则从会话中获取
            if (avatar == null) {
                GameSession gamesession = GameSessionManager.getInstance().getAvatarByUuid(GameConstants.SESSION_PREFIX + account.getUuid());
                if (gamesession != null) {
                    avatar = gamesession.getRole(Avatar.class);
                }
            }

            // 没有进行登陆过
            if (avatar == null) {
                //判断微信昵称是否修改过，若修改过昵称，则更新数据库信息
                if (!loginVO.getNickName().equals(account.getNickName())) {
                    account.setNickName(loginVO.getNickName());
                    AccountService.getInstance().updateByPrimaryKeySelective(account);
                }
                //断线超过时间后，自动退出
                avatar = new Avatar();
                AvatarVO avatarVO = new AvatarVO();
                avatarVO.setAccount(account);
                avatarVO.setIp(loginVO.getIP());
                avatar.avatarVO = avatarVO;

                // 加入到在线列表之中
                loginAction(gameSession, avatar);
                // 把session放入到GameSessionManager
                GameSessionManager.getInstance().putGameSessionInHashMap(gameSession, avatar.getUuId());

                // 发送公告给玩家
                Thread.sleep(3000);
                NoticeTable notice = NoticeTableService.getInstance().selectRecentlyObject();
                String content = notice.getContent();
                gameSession.sendMsg(new HostNoticeResponse(ErrorCode.SUCCESS_CODE, content));
            } else {
                //断线重连
                GameServerContext.add_onLine_Character(avatar);
                GameServerContext.remove_offLine_Character(avatar);

                avatar.avatarVO.setOnLine(true);
                avatar.avatarVO.setAccount(account);
                avatar.avatarVO.setIp(loginVO.getIP());

                // 停止销毁对象 TODO

                // 重新给当前会话建立关联
                avatar.setSession(gameSession);
                gameSession.setLogin(true);
                gameSession.setRole(avatar);


                // 玩家断线重连操作 TODO
                returnBackAction(gameSession, avatar);

                //把session放入到GameSessionManager,并且移除以前的session
                GameSessionManager.getInstance().putGameSessionInHashMap(gameSession, avatar.getUuId());

                //公告发送给玩家
                Thread.sleep(3000);
                NoticeTable notice = NoticeTableService.getInstance().selectRecentlyObject();
                String content = notice.getContent();
                gameSession.sendMsg(new HostNoticeResponse(ErrorCode.SUCCESS_CODE, content));
            }
        }
        System.out.println("玩家" + account.getUuid() + ":登录游戏");
    }

    private void returnBackAction(GameSession gameSession, Avatar avatar) {

        // 判断用户是否在房间中 TODO
        if (avatar.avatarVO.getRoomId() != null) {
            RoomLogic roomLogic = RoomManager.getInstance().getRoom(avatar.avatarVO.getRoomId());
            if (roomLogic != null) {
                //如果用户是在玩游戏/在房间的时候断线，且返回时房间还未被解散，则需要返回游戏房间其他用户信息，牌组信息
                roomLogic.returnBackAction(avatar);
                try {
                    Thread.sleep(1000);
                    // 在某一句结算时断线，重连时返回结算信息 TODO
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else{
                GameSession gamesession = avatar.getSession();
                GameServerContext.add_onLine_Character(avatar);
                gamesession.sendMsg(new LoginResponse(ErrorCode.SUCCESS_CODE, avatar.avatarVO));
            }
        } else {
            //如果不是在游戏时断线，则直接返回个人用户信息avatar
            avatar.getSession().sendMsg(new LoginResponse(ErrorCode.SUCCESS_CODE, avatar.avatarVO));
        }

    }

    /**
     * 执行登陆操作。 设置用户的Login状态为true， 在线状态为 true
     *
     * @param gameSession
     * @param avatar
     */
    private void loginAction(GameSession gameSession, Avatar avatar) {
        gameSession.setRole(avatar);
        gameSession.setLogin(true);
        avatar.setSession(gameSession);
        avatar.avatarVO.setOnLine(true);
        GameServerContext.add_onLine_Character(avatar);
        gameSession.sendMsg(new LoginResponse(ErrorCode.SUCCESS_CODE, avatar.avatarVO));
    }
}
