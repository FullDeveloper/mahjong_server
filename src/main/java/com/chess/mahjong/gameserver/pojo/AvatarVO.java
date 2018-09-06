package com.chess.mahjong.gameserver.pojo;

import com.chess.mybatis.entity.Account;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 周润斌
 * Date: 2018/9/5
 * Time: 12:54
 * Description:
 */
public class AvatarVO {

    /**
     * 用户基本信息
     */
    private Account account;
    /**
     * 用户的IP地址
     */
    private String ip;

    /**
     * 是否在线
     */
    private Boolean onLine;
    /**
     * 房间号
     */
    private Integer roomId;

    /**
     * 是否是庄家
     */
    private Boolean main = false;

    private Boolean ready;

    /**
     * 牌数组
     * /碰 1  杠2  胡3  吃4
     */
    private int[][] paiArray;

    /**
     * 存储整局牌的 杠，胡以及得分情况的对象，游戏结束时直接返回对象
     */
    private HuReturnObjectVO huReturnObjectVO;

    /**
     * 打了的牌的字符串  1,2,3,4,5,6,1,3,5 格式
     */
    private List<Integer> chuPais = new ArrayList<>();

    /**
     * 划水麻将  胡牌的类型(1:普通小胡(点炮/自摸)    2:大胡(点炮/自摸))
     * 放弃操作，摸牌，出牌，都需要重置
     */
    private int huType = 0;

    /**
     * 摸牌出牌状态
     * 摸了牌/碰/杠 true  出牌了false
     * 为true 表示该出牌了    为false表示不该出牌
     */
    private boolean hasMopaiChupai = false;

    public boolean isHasMopaiChupai() {
        return hasMopaiChupai;
    }

    public void setHasMopaiChupai(boolean hasMopaiChupai) {
        this.hasMopaiChupai = hasMopaiChupai;
    }

    public void updateChuPais(int cardPoint) {
        chuPais.add(cardPoint);
    }

    public int getHuType() {
        return huType;
    }

    public void setHuType(int huType) {
        this.huType = huType;
    }

    public HuReturnObjectVO getHuReturnObjectVO() {
        return huReturnObjectVO;
    }

    public void setHuReturnObjectVO(HuReturnObjectVO huReturnObjectVO) {
        this.huReturnObjectVO = huReturnObjectVO;
    }

    public int[][] getPaiArray() {
        return paiArray;
    }

    public void setPaiArray(int[][] paiArray) {
        this.paiArray = paiArray;
    }

    public Boolean getReady() {
        return ready;
    }

    public void setReady(Boolean ready) {
        this.ready = ready;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Boolean isOnLine() {
        return onLine;
    }

    public void setOnLine(Boolean onLine) {
        this.onLine = onLine;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Boolean getMain() {
        return main;
    }

    public void setMain(Boolean main) {
        this.main = main;
    }


}
