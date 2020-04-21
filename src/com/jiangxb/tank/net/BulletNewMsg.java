package com.jiangxb.tank.net;

import com.jiangxb.tank.Bullet;
import com.jiangxb.tank.Dir;
import com.jiangxb.tank.Group;
import com.jiangxb.tank.TankFrame;

import java.io.*;
import java.util.UUID;

public class BulletNewMsg extends Msg {

    UUID playerID;
    UUID id;
    int x, y;
    Dir dir;
    Group group;

    public BulletNewMsg(Bullet bullet) {
        this.playerID = bullet.getPlayerId();
        this.id = bullet.getId();
        this.x = bullet.getX();
        this.y = bullet.getY();
        this.dir = bullet.getDir();
        this.group = Group.BAD;
    }

    public BulletNewMsg() {

    }

    @Override
    public byte[] toBytes() {
        ByteArrayOutputStream baos = null;
        DataOutputStream dos = null;
        byte[] bytes = null;
        try {
            baos = new ByteArrayOutputStream();
            dos = new DataOutputStream(baos);
            //先写主战坦克id
            dos.writeLong(this.playerID.getMostSignificantBits());
            dos.writeLong(this.playerID.getLeastSignificantBits());
            //写子弹id
            dos.writeLong(id.getMostSignificantBits());
            dos.writeLong(id.getLeastSignificantBits());
            dos.writeInt(x);
            dos.writeInt(y);
            dos.writeInt(dir.ordinal());
            dos.writeInt(group.ordinal());
            dos.flush();
            bytes = baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(dos != null) {
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
            this.playerID = new UUID(dis.readLong(), dis.readLong());
            this.id = new UUID(dis.readLong(), dis.readLong());
            this.x = dis.readInt();
            this.y = dis.readInt();
            this.dir = Dir.values()[dis.readInt()];
            this.group = Group.values()[dis.readInt()];
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                dis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void handle() {
        if (this.playerID.equals(TankFrame.getInstance().getMainTank().getId()))
            return;

        Bullet bullet = new Bullet(this.playerID, x, y, dir, group);
        bullet.setId(this.id);
        TankFrame.getInstance().addBullet(bullet);
    }

    @Override
    public MsgType getMsgType() {
        return MsgType.BulletNew;
    }

    @Override
    public String toString() {
        return "BulletNewMsg{" +
                "playerID=" + playerID +
                ", id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", dir=" + dir +
                ", group=" + group +
                '}';
    }
}
