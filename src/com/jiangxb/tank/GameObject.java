package com.jiangxb.tank;

import java.awt.*;

public abstract class GameObject {

    public int x, y;

    public abstract void paint(Graphics g);

    public abstract int getWIDTH();

    public abstract int getHEIGHT();


}
