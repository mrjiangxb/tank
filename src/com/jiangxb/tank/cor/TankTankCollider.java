package com.jiangxb.tank.cor;

import com.jiangxb.tank.*;

public class TankTankCollider implements Collider {
    @Override
    public boolean collide(GameObject o1, GameObject o2) {
        if (o1 instanceof Tank && o2 instanceof Tank) {
            Tank tank1 = (Tank) o1;
            Tank tank2 = (Tank) o2;

            if (tank1.getRectangle().intersects(tank2.getRectangle())) {
                tank1.turnDir();
                tank2.turnDir();
            }
        }
        return true;
    }
}
