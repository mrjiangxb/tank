package com.jiangxb.tank.abstractfactory;

import com.jiangxb.tank.Audio;
import com.jiangxb.tank.ResourceMgr;
import com.jiangxb.tank.TankFrame;

import java.awt.*;

public class RectExplode extends BaseExplode {
    public static final int WIDTH = ResourceMgr.explodes[0].getWidth();
    public static final int HEIGNT = ResourceMgr.explodes[0].getHeight();

    private int x, y;
    // private boolean living = true;
    private TankFrame tankFrame = null;

    private int step = 0;

    public RectExplode(int x, int y, TankFrame tankFrame) {
        this.x = x;
        this.y = y;
        this.tankFrame = tankFrame;

        new Thread( ()->new Audio("audio/explode.wav").play()).start();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }


    @Override
    public void paint(Graphics g){
        /*g.drawImage(ResourceMgr.explodes[step++], x, y, null);
        if (step >= ResourceMgr.explodes.length) {
            tankFrame.explodes.remove(this);
        }*/

        Color color = g.getColor();
        g.setColor(Color.RED);
        g.fillRect(x, y, 10*step, 10*step);
        step++;
        if (step >= 10) {
            tankFrame.explodes.remove(this);
        }
        g.setColor(color);

    }
}
