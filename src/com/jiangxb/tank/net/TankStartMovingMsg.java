package com.jiangxb.tank.net;

import com.jiangxb.tank.Dir;
import com.jiangxb.tank.Tank;
import com.jiangxb.tank.TankFrame;

import java.io.*;
import java.util.UUID;

public class TankStartMovingMsg extends Msg {

    int x, y;
    UUID id;
    Dir dir;

    public TankStartMovingMsg() {
    }

    public TankStartMovingMsg(UUID id, int x, int y, Dir dir) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.dir = dir;
    }

    public TankStartMovingMsg(Tank tank) {
        this.x = tank.getX();
        this.y = tank.getY();
        this.id = tank.getId();
        this.dir = tank.getDir();
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

    public Dir getDir() {
        return dir;
    }


    @Override
    public void handle() {
        System.out.println("handle-->"+this.toString());
        if (this.id.equals(TankFrame.getInstance().getMainTank().getId()) ) {
            return;
        }

        Tank tank = TankFrame.getInstance().findTankByUUID(this.id);

        System.out.println(this.toString());

        if (tank != null) {
            tank.setMoving(true);
            tank.setDir(dir);
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
            dos.writeInt(dir.ordinal());
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
            this.dir = Dir.values()[dis.readInt()];
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
        return MsgType.TankStartMoving;
    }

    @Override
    public String toString() {
        return "TankStartMovingMsg{" +
                "x=" + x +
                ", y=" + y +
                ", id=" + id +
                ", dir=" + dir +
                '}';
    }

}
