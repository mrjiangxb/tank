package com.jiangxb.tank;

import com.jiangxb.tank.decorator.RectDecorator;
import com.jiangxb.tank.decorator.TailDecorator;
import sun.plugin.dom.css.Rect;

public class DefaultFireStrategy implements FireStrategy {
    @Override
    public void fire(Tank tank) {
        int bX = tank.getX() + Tank.WIDTH/2 - Bullet.WIDTH/2;
        int bY = tank.getY() + Tank.HEIGHT/2 - Bullet.HEIGHT/2;

        GameModel.getInstance().add(
                new TailDecorator(
                        new RectDecorator(
                                new Bullet(bX, bY, tank.getDir(), tank.getGroup())
                        )
                )
        );

        if (tank.getGroup() == Group.GOOD) {
            new Thread( () -> new Audio("audio/tank_fire.wav").play() ).start();
        }
    }
}
