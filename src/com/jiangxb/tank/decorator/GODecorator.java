package com.jiangxb.tank.decorator;

import com.jiangxb.tank.GameObject;

import java.awt.*;

public abstract class GODecorator extends GameObject {
    GameObject gameObject;

    public GODecorator(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    public abstract void paint(Graphics g);

}
