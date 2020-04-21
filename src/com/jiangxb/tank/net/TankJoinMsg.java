package com.jiangxb.tank.net;

import com.jiangxb.tank.Dir;
import com.jiangxb.tank.Group;
import com.jiangxb.tank.Tank;
import com.jiangxb.tank.TankFrame;

import java.io.*;
import java.util.UUID;

public class TankJoinMsg extends Msg {
    public int x, y;
    public Dir dir;
    public boolean moving;
    public Group group;
    public UUID id;

    public TankJoinMsg() {
    }

    public TankJoinMsg(int x, int y, Dir dir, boolean moving, Group group, UUID id) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.moving = moving;
        this.group = group;
        this.id = id;
    }

    public TankJoinMsg(Tank tank) {
        this.x = tank.getX();
        this.y = tank.getY();
        this.dir = tank.getDir();
        this.group = Group.BAD;
        this.moving = tank.isMoving();
        this.id = tank.getId();
    }

    // 读取字节数组，填充msg
    public void parse(byte[] bytes){
        DataInputStream dis = null;
        try {
            dis = new DataInputStream(new ByteArrayInputStream(bytes));
            this.x = dis.readInt();
            this.y = dis.readInt();
            this.dir = Dir.values()[dis.readInt()];
            this.moving = dis.readBoolean();
            this.group = Group.values()[dis.readInt()];
            this.id = new UUID(dis.readLong(), dis.readLong());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (dis != null){
                    dis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public MsgType getMsgType() {
        return MsgType.TankJoin;
    }

    // 将消息转换为字节数组
    @Override
    public byte[] toBytes () {
        ByteArrayOutputStream baos = null;
        DataOutputStream dos = null;
        byte[] bytes = null;

        try {
            baos = new ByteArrayOutputStream();
            dos = new DataOutputStream(baos);
            dos.writeInt(x);
            dos.writeInt(y);
            dos.writeInt(dir.ordinal());
            dos.writeBoolean(moving);
            dos.writeInt(group.ordinal()); // 枚举类下标
            dos.writeLong(id.getMostSignificantBits()); // 高64位  UUID为128位
            dos.writeLong(id.getLeastSignificantBits()); // 低64位
            dos.flush();
            bytes = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bytes;
    }

    @Override
    public String toString() {
        return "TankJoinMsg{" +
                "x=" + x +
                ", y=" + y +
                ", dir=" + dir +
                ", moving=" + moving +
                ", group=" + group +
                ", id=" + id +
                '}';
    }

    @Override
    public void handle() {
        if (this.id.equals( TankFrame.getInstance().getMainTank().getId() ) ||
                TankFrame.getInstance().findTankByUUID(this.id) != null ) {
            return;
        }
        System.out.println("-->"+this.toString());
        Tank tank = new Tank(this);
        TankFrame.getInstance().addTank(tank);

        Client.INSTANCE.send(new TankJoinMsg(TankFrame.getInstance().getMainTank()));
    }
}
