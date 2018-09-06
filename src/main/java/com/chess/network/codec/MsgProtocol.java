package com.chess.network.codec;

/**
 * Author: zrb
 * Date: 2018/9/4
 * Time: 16:09
 * Description:
 */
public interface MsgProtocol {

    /**
     * 默认flag值
     */
    byte DEFALUT_FLAG = 1;
    /**
     * 最大长度
     */
    int MAX_PACKAGE_LENGTH = 1024 * 5;
    /**
     * 标识数占得 byte位数
     */
    int FLAG_SIZE = 1;
    /**
     * 协议中 长度部分 占用的byte数 其值表示(协议号 + 内容) 的长度
     */
    int LENGTH_SIZE = 4;
    /**
     * 消息号占用的byte数
     */
    int MSG_CODE_SIZE = 4;

}
