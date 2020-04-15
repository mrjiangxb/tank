package com.jiangxb.tank;

import java.awt.*;
import java.io.Serializable;

public abstract class GameObject implements Serializable {

    public int x, y;

    public abstract void paint(Graphics g);

    public abstract int getWIDTH();

    public abstract int getHEIGHT();


}
