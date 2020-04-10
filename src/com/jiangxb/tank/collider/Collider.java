package com.jiangxb.tank.collider;

import com.jiangxb.tank.GameObject;

public interface Collider {
    boolean collide(GameObject o1, GameObject o2);
}
