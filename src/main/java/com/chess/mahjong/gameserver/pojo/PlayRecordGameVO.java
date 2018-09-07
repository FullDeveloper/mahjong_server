package com.chess.mahjong.gameserver.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 周润斌
 * Date: 2018/9/7
 * Time: 13:46
 * Description:
 */
public class PlayRecordGameVO {

    public List<PlayBehaviedVO> behavieList = new ArrayList<PlayBehaviedVO>();
    public List<PlayRecordItemVO> playerItems = new ArrayList<PlayRecordItemVO>();
    public RoomVO roomvo;

}
