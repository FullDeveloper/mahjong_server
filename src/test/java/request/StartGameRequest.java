package request;

import com.chess.context.ConnectAPI;
import org.junit.Test;

import java.io.IOException;

/**
 * @author 周润斌
 * Date: 2018/9/6
 * Time: 15:03
 * Description:
 */
public class StartGameRequest extends BaseSocket {


    @Test
    public void test_StartGameRequest() throws IOException, InterruptedException {
        login("");
        joinRoom("","");

        String result = invokeSendMessage("", ConnectAPI.JOIN_ROOM_REQUEST);

    }

}
