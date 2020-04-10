package com.jiangxb.tank;

import com.jiangxb.tank.cor.BulletTankCollider;
import com.jiangxb.tank.cor.Collider;
import com.jiangxb.tank.cor.TankTankCollider;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 游戏模型
 * 单例
 */
public class GameModel {

    private static final GameModel INSTANCE = new GameModel();

    Tank myTank = new Tank(200,500, Dir.UP, Group.GOOD, this);

//    Collider collider = new BulletTankCollider();
//    Collider collider2 = new TankTankCollider();

    ColliderChain colliderChain = new ColliderChain();

    private List<GameObject> objects = new ArrayList<>();

    public static GameModel getInstance() {
        return INSTANCE;
    }


    private GameModel() {
        int initTankCount = Integer.parseInt((String) PropertyMgr.get("initTankCount")) ;

        // 初始化敌方坦克
        for (int i = 0; i < initTankCount; i++) {
            this.add(new Tank(50+i*80, 200, Dir.DOWN, Group.BAD, this));
        }
    }

    public void add(GameObject gameObject){
        this.objects.add(gameObject);
    }
    public void remove(GameObject gameObject){
        this.objects.remove(gameObject);
    }

    public void paint(Graphics g) {

//        Color color = g.getColor();
//        g.setColor(Color.WHITE);
//        g.drawString("子弹数"+bullets.size(), 10, 60);
//        g.drawString("敌人数"+tanks.size(), 10, 80);
//        g.drawString("爆炸数"+explodes.size(), 10, 100);
//        g.setColor(color);

        myTank.paint(g);
        // 若用迭代器 删除时会报错
        for (int i = 0; i < objects.size(); i++) {
            objects.get(i).paint(g);
        }

        // 互相碰撞
        for (int i=0; i < objects.size(); i++) {
            for (int j=i+1; j < objects.size(); j++) {
                GameObject o1 = objects.get(i);
                GameObject o2 = objects.get(j);
//                collider.collide(o1, o2);
//                collider2.collide(o1, o2);
                colliderChain.collide(o1, o2);
            }
        }

        // 碰撞检测
//        for (int i = 0; i < bullets.size(); i++) {
//            for (int j = 0; j < tanks.size(); j++) {
//                bullets.get(i).collideWith(tanks.get(j));
//            }
//        }

    }

    public Tank getMainTank() {
        return myTank;
    }
}
