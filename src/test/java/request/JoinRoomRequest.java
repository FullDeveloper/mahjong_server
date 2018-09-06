package request;

import com.chess.context.ConnectAPI;
import com.chess.persist.util.JsonUtilTool;
import net.sf.json.JSONObject;
import org.junit.Test;

import java.io.IOException;

/**
 * @author 周润斌
 * Date: 2018/9/6
 * Time: 10:39
 * Description:
 */
public class JoinRoomRequest extends BaseSocket {

    @Test
    public void test_JoinRoomRequest() throws IOException, InterruptedException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("roomId", "300900");
        login("1111100032");


        Thread.sleep(6000);

        String message = JsonUtilTool.toJson(jsonObject);
        System.out.println(message);
        String result = sendMessageCustom(message, ConnectAPI.JOIN_ROOM_REQUEST);
        Thread.sleep(10000);
        result = invokeSendMessage("", ConnectAPI.PrepareGame_MSG_REQUEST);
    }

}
