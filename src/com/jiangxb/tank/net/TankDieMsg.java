package com.jiangxb.tank.net;

import com.jiangxb.tank.Bullet;
import com.jiangxb.tank.Tank;
import com.jiangxb.tank.TankFrame;

import java.io.*;
import java.util.UUID;

public class TankDieMsg extends Msg {


    UUID bulletId; //who killed me
    UUID id;  // 死亡坦克id
    public TankDieMsg(UUID bulletId, UUID tankId) {
        this.bulletId = bulletId;
        this.id = tankId;
    }

    public TankDieMsg() {}

    @Override
    public byte[] toBytes() {
        ByteArrayOutputStream baos = null;
        DataOutputStream dos = null;
        byte[] bytes = null;
        try {
            baos = new ByteArrayOutputStream();
            dos = new DataOutputStream(baos);
            dos.writeLong(bulletId.getMostSignificantBits());
            dos.writeLong(bulletId.getLeastSignificantBits());
            dos.writeLong(id.getMostSignificantBits());
            dos.writeLong(id.getLeastSignificantBits());
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
            this.bulletId = new UUID(dis.readLong(), dis.readLong());
            this.id = new UUID(dis.readLong(), dis.readLong());
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
        Tank myTank = TankFrame.getInstance().getMainTank();
        System.out.println("we got a tank die:" + id);
        System.out.println("and my tank is:" + myTank.getId());
        Tank tt = TankFrame.getInstance().findTankByUUID(id);
        System.out.println("i found a tank with this id:" + tt);

        Bullet b = TankFrame.getInstance().findBulletByUUID(bulletId);
        if(b != null) {
            b.die();
        }

        if(this.id.equals(TankFrame.getInstance().getMainTank().getId())) {
            TankFrame.getInstance().getMainTank().die();
            TankFrame.getInstance().addExplode(myTank.getX(), myTank.getY());
        } else {

            Tank t = TankFrame.getInstance().findTankByUUID(id);
            if(t != null) {
                t.die();
                TankFrame.getInstance().addExplode(t.getX(), t.getY());
            }
        }
    }


    @Override
    public MsgType getMsgType() {
        return MsgType.TankDie;
    }

}
