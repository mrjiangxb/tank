package com.jiangxb.tank.observer;

import com.jiangxb.tank.Tank;

public class TankFireEvent {
    Tank tank;

    public TankFireEvent(Tank tank) {
        this.tank = tank;
    }

    public Tank getSource(){
        return tank;
    }

}
