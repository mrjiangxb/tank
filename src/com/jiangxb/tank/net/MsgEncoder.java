package com.jiangxb.tank.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MsgEncoder extends MessageToByteEncoder<Msg> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Msg msg, ByteBuf out) throws Exception {
        // 消息类型 int类型4字节
        out.writeInt(msg.getMsgType().ordinal());
        // 消息长度
        out.writeInt(msg.toBytes().length);
        // 消息内容
        out.writeBytes(msg.toBytes());
    }

}
