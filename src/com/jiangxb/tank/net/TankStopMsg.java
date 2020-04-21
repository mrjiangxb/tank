package com.jiangxb.tank.net;

import com.jiangxb.tank.Dir;
import com.jiangxb.tank.Tank;
import com.jiangxb.tank.TankFrame;

import java.io.*;
import java.util.UUID;

public class TankStopMsg extends Msg {

    int x, y;
    UUID id;

    public TankStopMsg() {
    }

    public TankStopMsg(UUID id, int x, int y) {
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public TankStopMsg(Tank tank) {
        this.x = tank.getX();
        this.y = tank.getY();
        this.id = tank.getId();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public void handle() {
        System.out.println("handle-->"+this.toString());
        // 是自己的话返回
        if (this.id.equals(TankFrame.getInstance().getMainTank().getId()) ) {
            return;
        }

        Tank tank = TankFrame.getInstance().findTankByUUID(this.id);

        System.out.println(this.toString());

        if (tank != null) {
            tank.setMoving(false);
            tank.setX(this.x);
            tank.setY(this.y);
        }
    }

    @Override
    public byte[] toBytes() {
        DataOutputStream dos = null;
        ByteArrayOutputStream baos = null;
        byte[] bytes = null;

        try {
            baos = new ByteArrayOutputStream();
            dos = new DataOutputStream(baos);
            dos.writeLong(id.getMostSignificantBits());
            dos.writeLong(id.getLeastSignificantBits());
            dos.writeInt(this.x);
            dos.writeInt(this.y);
            dos.flush();
            bytes = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
                try {
                    if (baos != null){
                        baos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (dos != null) {
                        dos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        return bytes;
    }

    @Override
    public void parse(byte[] bytes) {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));

        try {
            this.id = new UUID(dis.readLong(),dis.readLong());
            this.x = dis.readInt();
            this.y = dis.readInt();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (dis != null) {
                    dis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public MsgType getMsgType() {
        return MsgType.TankStop;
    }

    @Override
    public String toString() {
        return "TankStopMsg{" +
                "x=" + x +
                ", y=" + y +
                ", id=" + id +
                '}';
    }
}
