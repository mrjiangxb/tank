package com.jiangxb.tank.net;


import com.jiangxb.tank.TankFrame;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {

    public static final Client INSTANCE = new Client();

    private Channel channel = null;

    private Client () {
    }

    public void connect () {
        // 事件处理的线程池
        EventLoopGroup group = new NioEventLoopGroup(1);
        // 辅助启动类
        Bootstrap bootstrap = new Bootstrap();

        try {
            ChannelFuture future = bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ClientChannelInitializer())
                    // 调用connect后handler才会执行
                    .connect("localhost", 8888);
            // connect 方法是异步的，如果要确认connect连上再往下执行需要用sync方法
            //.sync();

            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (!channelFuture.isSuccess()) {
                        // 连接失败
                        System.out.println("not connected!");
                    } else {
                        System.out.println("connected!");
                        // initialize the channel
                        channel = channelFuture.channel();
                    }
                }
            });

            future.sync();
            // wait until close
            future.channel()
                    // 当close被调用时才会执行
                    .closeFuture()
                    .sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    public void send(String msg){
        ByteBuf buf = Unpooled.copiedBuffer(msg.getBytes());
        channel.writeAndFlush(buf);
    }

    public void send(Msg msg) {
        channel.writeAndFlush(msg);
    }

    public void closeConnect() {
        this.send("_close_");
    }

}

class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                .addLast(new MsgEncoder())
                .addLast(new MsgDecoder())
                .addLast(new ClientHandler());
    }
}

class ClientHandler extends SimpleChannelInboundHandler<Msg> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Msg msg) throws Exception {

        System.out.println(msg.toString());
        msg.handle();

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // netty的ByteBuf 直接访问内存 效率高 Direct Memory
        // 直接访问内存 不参与java的垃圾回收
        // 这个buf直接指向操作系统内存，不是jvm的虚拟内存，需要手动释放
        // ByteBuf buf = Unpooled.copiedBuffer("[system]--->hello".getBytes());

        // 该方法会自动释放内存
        ctx.writeAndFlush(new TankJoinMsg(TankFrame.getInstance().getMainTank())); // test
    }
}
