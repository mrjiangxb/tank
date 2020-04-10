package com.jiangxb.tank.cor;

import com.jiangxb.tank.GameObject;

public interface Collider {
    boolean collide(GameObject o1, GameObject o2);
}
