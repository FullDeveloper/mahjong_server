package request;

import com.chess.context.ConnectAPI;
import org.junit.Test;

import java.io.IOException;

/**
 * @author 周润斌
 * Date: 2018/9/5
 * Time: 10:38
 * Description:
 */
public class OpenAppRequest extends BaseSocket {

    @Test
    public void test_OpenAppRequest() throws IOException {
        String message = "1231231";
        int msgCode = ConnectAPI.OPENAPP_REQUEST;
        String result = invokeSendMessage(message, msgCode);
        System.out.println("response message ==>" + result);
    }

}
