package com.jiangxb.tank.collider;

import com.jiangxb.tank.Bullet;
import com.jiangxb.tank.GameObject;
import com.jiangxb.tank.Tank;
import com.jiangxb.tank.Wall;

public class TankWallCollider implements Collider {
    @Override
    public boolean collide(GameObject o1, GameObject o2) {
        if (o1 instanceof Tank && o2 instanceof Wall) {
            Tank tank = (Tank) o1;
            Wall wall = (Wall) o2;

            if (tank.getRectangle().intersects(wall.rectangle)) {
                tank.back();
            }
        } else if(o1 instanceof Wall && o2 instanceof Tank) {
            return collide(o2, o1);
        }
        return true;
    }
}
