package request;

import com.chess.context.ConnectAPI;
import com.chess.mahjong.gameserver.pojo.LoginVO;
import com.chess.persist.util.JsonUtilTool;
import org.junit.Test;

import java.io.IOException;

/**
 * @author 周润斌
 * Date: 2018/9/5
 * Time: 13:46
 * Description:
 */
public class LoginRequest extends BaseSocket{

    @Test
    public void test_LoginRequest() throws IOException {

        LoginVO loginVO = new LoginVO();
        loginVO.setOpenId("1111100000");
        loginVO.setHeadIcon("无");
        loginVO.setNickName("测试");
        loginVO.setProvince("浙江省");
        loginVO.setCity("杭州市");
        loginVO.setSex(0);
        loginVO.setUnionId("11020310");
        String message = JsonUtilTool.toJson(loginVO);
        System.out.println(message);

        String result = invokeSendMessage(message, ConnectAPI.LOGIN_REQUEST);
        System.out.println(result);

    }

}
