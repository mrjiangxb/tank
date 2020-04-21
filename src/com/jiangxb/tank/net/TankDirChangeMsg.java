package com.jiangxb.tank.net;

import com.jiangxb.tank.Dir;
import com.jiangxb.tank.Tank;
import com.jiangxb.tank.TankFrame;

import java.io.*;
import java.util.UUID;

public class TankDirChangeMsg extends Msg {

    UUID id;
    int x, y;
    Dir dir;


    public TankDirChangeMsg() {
    }

    public TankDirChangeMsg(UUID id, Dir dir, int x, int y) {
        this.id = id;
        this.dir = dir;
        this.x = x;
        this.y = y;
    }

    public TankDirChangeMsg(Tank tank) {
        this.id = tank.getId();
        this.dir = tank.getDir();
        this.x = tank.getX();
        this.y = tank.getY();
    }



    @Override
    public void handle() {
        if (this.id.equals(TankFrame.getInstance().getMainTank().getId())) {
            return;
        }

        Tank tank = TankFrame.getInstance().findTankByUUID(this.id);

        if (tank != null) {
            tank.setMoving(true);
            tank.setX(this.x);
            tank.setY(this.y);
            tank.setDir(this.dir);
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
            dos.writeLong(this.id.getMostSignificantBits());
            dos.writeLong(this.id.getLeastSignificantBits());
            dos.writeInt(this.x);
            dos.writeInt(this.y);
            dos.writeInt(dir.ordinal());
            dos.flush();
            bytes = baos.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
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

            this.id = new UUID(dis.readLong(), dis.readLong());
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
        return MsgType.TankDirChange;
    }

    @Override
    public String toString() {
        return "TankDirChangeMsg{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", dir=" + dir +
                '}';
    }
}
