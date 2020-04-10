package com.jiangxb.tank;

import java.awt.*;

public class Explode extends GameObject {

    public static final int WIDTH = ResourceMgr.explodes[0].getWidth();
    public static final int HEIGNT = ResourceMgr.explodes[0].getHeight();

    private int x, y;
    // private boolean living = true;
    // private TankFrame tankFrame = null;

    private int step = 0;

    public Explode(int x, int y) {
        this.x = x;
        this.y = y;

        GameModel.getInstance().add(this);
        new Audio("audio/explode.wav").play();
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


    public void paint(Graphics g){
        g.drawImage(ResourceMgr.explodes[step++], x, y, null);
        if (step >= ResourceMgr.explodes.length) {
            GameModel.getInstance().remove(this);
        }
    }

}
