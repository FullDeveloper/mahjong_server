package com.chess.mahjong;

import com.chess.mahjong.gameserver.context.ExecutorServiceManager;
import com.chess.mybatis.InitServices;
import com.chess.network.MinaHostMsgHandler;
import com.chess.network.MinaMsgHandler;
import com.chess.network.NetManager;
import com.chess.network.codec.message.MsgDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author: zrb
 * Date: 2018/9/4
 * Time: 14:25
 * Description:
 */
public class GameServerBootstrap {

    private static final Logger logger = LoggerFactory.getLogger(GameServerBootstrap.class);

    private static int port = 10122;

    private static int hostPort = 10121;

    private static NetManager netManager;

    private static GameServerBootstrap instance = new GameServerBootstrap();

    public static MsgDispatcher msgDispatcher = new MsgDispatcher();;

    public static GameServerBootstrap getInstance() {
        return instance;
    }

    private GameServerBootstrap() {
        netManager = new NetManager();
    }

    public static void main(String[] args) {
        startUp();
    }

    private static void startUp() {
        try {
            logger.info("开始启动服务器......");
            ExecutorServiceManager.getInstance().initExecutorService();
            logger.info("初始化服务器线程池完成");
            InitServices.getInstance().initServiceFun();
            logger.info("数据库连接初始化完成");
            netManager.startListener(new MinaMsgHandler(), port);
            netManager.startHostListener(new MinaHostMsgHandler(), hostPort);
            logger.info("服务器监听端口:{}完成", port);
            logger.info("game server started...");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("服务器启动失败!");
        }
    }


}
