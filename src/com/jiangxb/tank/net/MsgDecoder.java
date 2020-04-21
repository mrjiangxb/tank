package com.jiangxb.tank.net;

import com.jiangxb.tank.Dir;
import com.jiangxb.tank.Group;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;
import java.util.UUID;

public class MsgDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 8) return; // TCP 拆包 粘包的问题

        in.markReaderIndex();

        MsgType msgType = MsgType.values()[in.readInt()];
        int length = in.readInt();

        if (in.readableBytes() < length) {
            in.resetReaderIndex();
            return;
        }

        byte[] bytes = new byte[length];
        in.readBytes(bytes);

        Msg msg = null;

        // reflection 利用反射创建msg 减少代码量
        msg = (Msg)Class.forName("com.jiangxb.tank.net." + msgType.toString() + "Msg").getDeclaredConstructor().newInstance();

        /*switch (msgType) {
            case TankJoin:
                msg = new TankJoinMsg();
                break;
            case TankStartMoving:
                msg = new TankStartMovingMsg();
                break;
            case TankStop:
                msg = new TankStopMsg();
                break;
            default:
                break;
        }*/
        msg.parse(bytes); // 解析bytes
        out.add(msg);

    }

}
