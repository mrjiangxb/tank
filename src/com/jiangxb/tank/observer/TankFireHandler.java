package com.jiangxb.tank.observer;

import com.jiangxb.tank.Tank;

public class TankFireHandler implements TankFireObserver {

    @Override
    public void actionOnFire(TankFireEvent event) {
        Tank tank = event.getSource();
        tank.fire();
    }
}
