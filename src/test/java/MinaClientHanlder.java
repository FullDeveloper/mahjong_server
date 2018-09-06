import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Author: zrb
 * Date: 2018/9/4
 * Time: 15:40
 * Description:
 */
public class MinaClientHanlder extends IoHandlerAdapter {
    public void sessionOpened(IoSession session) throws Exception {
        System.out.println("客户端登陆");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dataIn = new DataOutputStream(outputStream);
        String message = "";
        byte flag = 1;
        dataIn.write(flag);
        dataIn.writeInt(message.length());
        dataIn.writeInt(1);
        byte[] bytes = outputStream.toByteArray();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        DataInputStream dataIn2 = new DataInputStream(in);
        byte flag2 = dataIn2.readByte();
        System.out.println(flag2);
        session.write(outputStream.toByteArray());
    }

    public void sessionClosed(IoSession session) {
        System.out.println("client close");
    }

    public void messageReceived(IoSession session, Object message) throws Exception {
        System.out.println("客户端接受到了消息" + message);

//        session.write("Sent by Client1");
    }
}