package com.jiangxb.tank;

import com.jiangxb.tank.cor.BulletTankCollider;
import com.jiangxb.tank.cor.Collider;
import com.jiangxb.tank.cor.TankTankCollider;

import java.util.LinkedList;
import java.util.List;

public class ColliderChain implements Collider {

    private List<Collider> colliders = new LinkedList<>();

    public ColliderChain(){
        add(new BulletTankCollider());
        add(new TankTankCollider());
    }

    public void add(Collider collider){
        this.colliders.add(collider);
    }

    public boolean collide(GameObject o1, GameObject o2){
        for (int i=0; i<colliders.size(); i++) {
            if ( !colliders.get(i).collide(o1, o2) ) {
                return false;
            }
        }
        return true;
    }

}
