package com.jiangxb.tank.collider;

import com.jiangxb.tank.GameObject;

import java.io.Serializable;

public interface Collider extends Serializable {
    boolean collide(GameObject o1, GameObject o2);
}
