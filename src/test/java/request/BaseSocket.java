package request;

import com.chess.context.ConnectAPI;
import com.chess.mahjong.gameserver.pojo.LoginVO;
import com.chess.persist.util.JsonUtilTool;
import net.sf.json.JSONObject;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Author: zrb
 * Date: 2018/9/4
 * Time: 15:36
 * Description:
 */
public class BaseSocket {

    private static final byte FLAG = 1;

    private Socket socket = null;

    private static final String HOST = "";
    private static final Integer PORT = 10122;


    private DataInputStream dataIn = null;
    private DataOutputStream dataOut = null;
    private InputStream in = null;
    private OutputStream outputStream = null;

    static {

    }

    public void init() {
        try {
            socket = new Socket(HOST, PORT);
        } catch (IOException e) {
            throw new RuntimeException("建立socket失败");
        }
    }

    public void login(String openId) throws IOException {
        init();
        LoginVO loginVO = new LoginVO();
        loginVO.setOpenId(openId);
        loginVO.setHeadIcon("无");
        loginVO.setNickName("测试");
        loginVO.setProvince("浙江省");
        loginVO.setCity("杭州市");
        loginVO.setSex(0);
        loginVO.setUnionId("11020310");
        String message = JsonUtilTool.toJson(loginVO);
        System.out.println(message);
        String result = sendMessageCustom(message, ConnectAPI.LOGIN_REQUEST);
    }

    public void joinRoom(String roomId, String openId) throws IOException, InterruptedException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("roomId", roomId);
        login(openId);
        Thread.sleep(10000);
        String message = JsonUtilTool.toJson(jsonObject);
        System.out.println(message);
        String result = sendMessageCustom(message, ConnectAPI.JOIN_ROOM_REQUEST);
    }

    public String invokeSendMessage(String message, int msgCode) throws IOException {
        //init();
        String result = sendMessage(message, msgCode);
        //close();
        return result;
    }

    private void close() throws IOException {
        if (socket != null) {
            socket.close();
        }
    }

    private String sendMessage(String message, int msgCode) throws IOException {
        byte[] data = getData(message, msgCode);
        outputStream = socket.getOutputStream();
        dataOut = new DataOutputStream(outputStream);
        dataOut.write(data);

        InputStream inputStream = socket.getInputStream();
        dataIn = new DataInputStream(inputStream);
        while (true) {
            byte b1 = dataIn.readByte();
            int len = dataIn.readInt();
            int optMsg = dataIn.readInt();
            int status = dataIn.readInt();
            System.out.println("收到服务端返回数据-->" + dataIn.readUTF());
        }
    }

    public String sendMessageCustom(String message, int msgCode) throws IOException {
        byte[] data = getData(message, msgCode);
        outputStream = socket.getOutputStream();
        dataOut = new DataOutputStream(outputStream);
        dataOut.write(data);
        return null;
    }


    private static byte[] getData(String message, int msgCode) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dataIn = new DataOutputStream(outputStream);
        short messageLen = (short) message.getBytes().length;
        int len;
        if (messageLen > 0) {
            // 操作码 4个字节 int
            // 数据长度 2个字节 short
            len = 6 + messageLen;
        } else {
            // 操作码 int
            len = 4 + messageLen;
        }
        dataIn.write(FLAG);  // 1
        dataIn.writeInt(len); // 4
        dataIn.writeInt(msgCode); // 4
        dataIn.writeShort(messageLen); // 2
        dataIn.write(message.getBytes()); // 12
        return outputStream.toByteArray();
    }

}
