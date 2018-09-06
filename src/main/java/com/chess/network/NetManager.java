package com.chess.network;

import com.chess.network.codec.GameProtocolCodecFactory;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Author: zrb
 * Date: 2018/9/4
 * Time: 14:52
 * Description:
 */
public class NetManager {

    private NioSocketAcceptor acceptor;

    public void startListener(IoHandler ioHandler, int listenerPort) throws IOException {
        acceptor = new NioSocketAcceptor();
        //最大连接数限制
        acceptor.setBacklog(1000);
        //重新使用地址
        acceptor.setReuseAddress(true);
        acceptor.setHandler(ioHandler);
        // 定义一个编码解码过滤构建者
        DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
        IoFilter protocol = new ProtocolCodecFilter(new GameProtocolCodecFactory());
        chain.addLast("codec", protocol);
        chain.addLast("ThreadPool", new ExecutorFilter(Executors.newCachedThreadPool()));

        int receiveSize = 1024*1024*2;
        int sendSize = 1024*1024*2;
        int timeout = 10;

        SocketSessionConfig sessionConfig = acceptor.getSessionConfig();

        // 设置每一个非主监听连接的端口可以重用
        sessionConfig.setReuseAddress(true);
        // 设置输入缓冲区的大小
        sessionConfig.setReceiveBufferSize(receiveSize);
        // 设置输出缓冲区的大小
        sessionConfig.setSendBufferSize(sendSize);
        sessionConfig.setReadBufferSize(receiveSize);
        // flush函数的调用 设置为非延迟发送，为true则不组装成大包发送，收到东西马上发出
        sessionConfig.setTcpNoDelay(true);
        sessionConfig.setSoLinger(0);
        //设置超时
        sessionConfig.setIdleTime(IdleStatus.BOTH_IDLE, timeout);
        acceptor.bind(new InetSocketAddress(listenerPort));
    }


    /**
     * 开始监听端口 后台
     * @param iohandler
     * @param listenPort
     * @throws Exception
     */
    public  void startHostListener(IoHandler iohandler,int listenPort) throws Exception{
        acceptor = new NioSocketAcceptor();
        acceptor.setBacklog(10);
        acceptor.setReuseAddress(true);
        acceptor.setHandler(iohandler);

        DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
        IoFilter protocol = new ProtocolCodecFilter(new GameProtocolCodecFactory());
        chain.addLast("codec", protocol);
        chain.addLast("ThreadPool",new ExecutorFilter(Executors.newCachedThreadPool()));

        int receiveSize = 1024*1024*2;
        int sendSize = 1024*1024*2;
        int timeout = 10;
        SocketSessionConfig sc = acceptor.getSessionConfig();
        // 设置每一个非主监听连接的端口可以重用
        sc.setReuseAddress(true);
        // 设置输入缓冲区的大小
        sc.setReceiveBufferSize(receiveSize);
        // 设置输出缓冲区的大小
        sc.setSendBufferSize(sendSize);
        sc.setReadBufferSize(receiveSize);
        // flush函数的调用 设置为非延迟发送，为true则不组装成大包发送，收到东西马上发出
        sc.setTcpNoDelay(true);
        sc.setSoLinger(0);
        //设置超时
        sc.setIdleTime(IdleStatus.BOTH_IDLE, timeout);
        acceptor.bind(new InetSocketAddress(listenPort));
    }


}
