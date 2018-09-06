package com.chess.context;

/**
 * Author: zrb
 * Date: 2018/9/4
 * Time: 14:27
 * Description:
 */
public class ConnectAPI {


    /**
     * 打开APP时请求操作码和响应操作码
     */
    public static int OPENAPP_REQUEST = 0x000007;
    public static int OPENAPP_RESPONSE = 0x000008;


    /**
     * 用户执行登陆请求和响应操作码
     */
    public static int LOGIN_REQUEST = 0x000001;
    public static int LOGIN_RESPONSE = 0x000002;

    /**
     * 游戏错误码返回
     */
    public static int ERROR_RESPONSE = 0xffff09;

    /**
     * 通知客户端进行断线操作
     */
    public static int BREAK_LINE_RESPONSE = 0x211211;


    /**
     * 后台广告链接通知/后台充卡链接通知  公用request  不公用response
     */
    public static int HOST_SEND_REQUEST = 0x158888;
    public static int HOST_SEND_RESPONSE = 0x157777;

    /**
     * 创建游戏房间
     */
    public static int CREATEROOM_REQUEST = 0x00009;
    public static int CREATEROOM_RESPONSE = 0x00010;

    /**
     * 断线玩家重连之后 其他三家人接收信息
     */
    public static int OTHER_BACK_LOGIN_RESPONSE = 0x001111;

    /**
     * 后台登陆
     */
    public static int BACK_LOGIN_RESPONSE = 0x001002;

    /**
     * 用户加入房间通知
     */
    public static int JOIN_ROOM_NOTICE = 0x10a004;
    /**
     * 用户加入房间响应
     */
    public static int JOIN_ROOM_RESPONSE  = 0x000004;
    /**
     * 用户加入房间请求
      */
    public static int JOIN_ROOM_REQUEST  = 0x000003;

    /**
     * 用户离线通知
     */
    public static int OFF_LINE_RESPONSE  = 0x000015;

    /**
     * 用户请求准备游戏
     */
    public static int  PrepareGame_MSG_REQUEST = 0x333333;
    public static int PrepareGame_MSG_RESPONSE = 0x444444;

    /**
     * 胡牌
     */
    public static int HU_PAI_REQUEST = 0x100009;
    public static int HUPAI_RESPONSE = 0x100010;
    public static int HU_PAI_ALL_RESPONSE = 0x100110;


    /**
     * 开始游戏
     */
    public static int START_GAME_RESPONSE = 0x00012;

}
