package com.chess.mahjong.gameserver.pojo;

import com.chess.persist.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 周润斌
 * Date: 2018/9/6
 * Time: 14:56
 * Description:
 */
public class HuReturnObjectVO {

    /**
     * key:type:游戏自摸1，接炮2，点炮3，暗杠4，明杠5 ，胡6记录(key),
     * value:list里面，第一个为点炮/杠/胡次数，第二个元素为点炮/杠/胡分数总和
     */
    private Map<String , ArrayList<Integer>> gangAndHuInfos =  new HashMap<String, ArrayList<Integer>>();

    /**
     * 杠的总分
     */
    private int gangScore = 0;

    /**
     * 总分
     */
    private int totalScore = 0;

    /**
     * 存放吃，碰，杠，胡的信息
     */
    private Map<String , String> totalInfo = new HashMap<String, String>() ;

    /**
     *  游戏自摸1，接炮2，点炮3，暗杠4，明杠5 ，胡6记录(type),加码7
     * @param type 类型
     * @param score 分数（划水麻将则是番数）
     */
    public synchronized void updateGangAndHuInfos(String type , int score) {
        ArrayList<Integer> listNew = new ArrayList<Integer>();
        ArrayList<Integer> list = gangAndHuInfos.get(type);
        if(list == null || list.size() <= 0){
            listNew.add(1);
            listNew.add(score);
        }
        else{
            //在原来的基础上修改信息
            listNew.add(list.get(0)+1);
            listNew.add(list.get(1)+score);
        }
        if(type.equals("4") || type.equals("5")){
            //杠的总分
            updateGangScore(score);
        }
        updateTotalScore(score);
        gangAndHuInfos.put(type, listNew);
    }

    /**
     * 统计吃，碰，杠，胡的详细信息， 比如 谁杠了谁的什么牌，明杠，还是暗杠等
     * @param type 信息类型  "chi","peng","gang","hu",     划水麻将跟庄"gengzhuang"
     * @param str   信息内容
     */
    public synchronized void updateTotalInfo(String type ,String str) {
        if(type.equals("chi")){
            System.out.println("chi");
        }
        if(StringUtil.isNotEmpty(str)){
            if(totalInfo.get(type) == null){
                totalInfo.put(type, str);
            }
            else{
                totalInfo.put(type, totalInfo.get(type)+","+str);
            }
        }
        else{
            System.out.println("HuReturnObjectVO里面的updateTotalInfo--传入的str不呢为空");
        }
    }

    private void updateGangScore(int score) {
        gangScore = gangScore +score;
    }

    //更新总分数
    private void updateTotalScore(int score) {
        totalScore = totalScore +score;
    }

}
