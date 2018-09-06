package com.chess.mahjong.gameserver.msg.processor;

import com.chess.context.ConnectAPI;
import com.chess.mahjong.gameserver.msg.processor.createroom.CreateRoomMsgProcssor;
import com.chess.mahjong.gameserver.msg.processor.joinroom.JoinRoomMsgProcessor;
import com.chess.mahjong.gameserver.msg.processor.login.LoginMsgProcessor;
import com.chess.mahjong.gameserver.msg.processor.login.OpenAppMsgProcessor;
import com.chess.mahjong.gameserver.msg.processor.startgame.PrepareGameMSGProcessor;

/**
 * Date: 2018/9/5
 * Time: 10:22
 * Description: 注册所有的处理器
 *
 * @author zrb
 */
public enum MsgProcessorRegister {
    /**
     * 打开APP时用户请求服务端处理器
     */
    openApp(ConnectAPI.OPENAPP_REQUEST, new OpenAppMsgProcessor()),
    /**
     * 登陆处理器 包括断线重连
     */
    login(ConnectAPI.LOGIN_REQUEST, new LoginMsgProcessor()),
    /**
     * 创建房间
     */
    createRoom(ConnectAPI.CREATEROOM_REQUEST, new CreateRoomMsgProcssor()),
    /**
     * 用户加入房间
     */
    joinRoom(ConnectAPI.JOIN_ROOM_REQUEST, new JoinRoomMsgProcessor()),
    /**
     * 游戏开始前准备
     */
    prepareGame(ConnectAPI.PrepareGame_MSG_REQUEST, new PrepareGameMSGProcessor());

    private int msgCode;
    private MsgProcessor processor;

    /**
     * 不允许外部创建
     *
     * @param msgCode
     * @param processor
     */
    private MsgProcessorRegister(int msgCode, MsgProcessor processor) {
        this.msgCode = msgCode;
        this.processor = processor;
    }

    /**
     * 获取协议号
     *
     * @return
     */
    public int getMsgCode() {
        return this.msgCode;
    }

    /**
     * 获取对应的协议解晰类对象
     *
     * @return
     */
    public MsgProcessor getMsgProcessor() {
        return this.processor;
    }

}
