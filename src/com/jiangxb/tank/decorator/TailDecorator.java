package com.jiangxb.tank.decorator;

import com.jiangxb.tank.GameObject;

import java.awt.*;

public class TailDecorator extends GODecorator {

    public TailDecorator(GameObject gameObject) {
        super(gameObject);
    }

    @Override
    public void paint(Graphics g) {
        this.x = gameObject.x;
        this.y = gameObject.y;
        gameObject.paint(g);
        Color color = g.getColor();
        g.setColor(Color.YELLOW);
        g.drawLine(super.gameObject.x, super.gameObject.y, super.gameObject.x+getWIDTH(), super.gameObject.y+getHEIGHT());
        g.setColor(color);
    }

    @Override
    public int getWIDTH() {
        return super.gameObject.getWIDTH();
    }

    @Override
    public int getHEIGHT() {
        return super.gameObject.getHEIGHT();
    }
}
