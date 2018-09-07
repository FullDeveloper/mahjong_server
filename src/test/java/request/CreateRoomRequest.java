package request;

import com.chess.context.ConnectAPI;
import com.chess.mahjong.gameserver.pojo.CardVO;
import com.chess.mahjong.gameserver.pojo.RoomVO;
import com.chess.persist.util.JsonUtilTool;
import org.junit.Test;

import java.io.IOException;

/**
 * @author 周润斌
 * Date: 2018/9/5
 * Time: 17:47
 * Description:
 */
public class CreateRoomRequest extends BaseSocket {

    @Test
    public void test_CreateRoomRequest() throws IOException, InterruptedException {
        RoomVO roomVO = new RoomVO();
        roomVO.setRoundNumber(8); // 8局
        roomVO.setRoomType(3); // 长沙麻将
        roomVO.setZiMo(1); //只能自摸
        roomVO.setMa(4); // 抓马数量 4个
        login("1111100000");
        Thread.sleep(10000);
        String message = JsonUtilTool.toJson(roomVO);
        System.out.println(message);
        String result = sendMessageCustom(message, ConnectAPI.CREATEROOM_REQUEST);
        Thread.sleep(5000);
        result = sendMessageCustom(message, ConnectAPI.PrepareGame_MSG_REQUEST);

        Thread.sleep(70000);
        CardVO cardVO = new CardVO();
        cardVO.setCardPoint(1);
        result = invokeSendMessage(JsonUtilTool.toJson(cardVO), ConnectAPI.CHUPAI_REQUEST);
    }

}
