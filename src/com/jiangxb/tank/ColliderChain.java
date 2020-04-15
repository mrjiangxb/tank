package com.jiangxb.tank;

import com.jiangxb.tank.collider.Collider;


import java.util.LinkedList;
import java.util.List;

public class ColliderChain implements Collider  {

    private List<Collider> colliders = new LinkedList<>();

    public ColliderChain(){
//        add(new BulletTankCollider());
//        add(new TankTankCollider());

        // 从配置文件config读取责任链
        try {
            String str = (String) PropertyMgr.get("colliders");
            String[] strings = str.split(",");
            for (int i=0; i < strings.length; i++) {
                Collider collider = (Collider) Class.forName(strings[i]).newInstance();
                this.add(collider);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
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
