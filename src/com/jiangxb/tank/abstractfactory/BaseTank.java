package com.jiangxb.tank.abstractfactory;

import com.jiangxb.tank.Group;

import java.awt.*;

public abstract class BaseTank {
    // 所属阵营
    Group group = Group.BAD;

    public Rectangle rectangle = new Rectangle();

    public abstract void paint(Graphics g);

    public Group getGroup() {
        return this.group;
    }

    public abstract void die();

    public abstract int getX();

    public abstract int getY();
}
