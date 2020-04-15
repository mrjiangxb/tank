package com.jiangxb.tank;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 游戏模型
 * 单例
 */
public class GameModel implements Serializable {

    private static final GameModel INSTANCE = new GameModel();

    static {
        INSTANCE.init();
    }

    private Tank myTank;

    ColliderChain colliderChain = new ColliderChain();

    private List<GameObject> objects = new ArrayList<>();

    public static GameModel getInstance() {
        return INSTANCE;
    }

    private GameModel() {}

    private void init(){

        myTank = new Tank(200,500, Dir.UP, Group.GOOD);

        int initTankCount = Integer.parseInt((String) PropertyMgr.get("initTankCount")) ;

        // 初始化敌方坦克
        for (int i = 0; i < initTankCount; i++) {
            new Tank(50+i*80, 200, Dir.DOWN, Group.BAD);

        }

        // 初始化墙
        this.add(new Wall(150, 150, 200, 50));
        this.add(new Wall(550, 150, 200, 50));
        this.add(new Wall(300, 300, 50, 200));
        this.add(new Wall(550, 300, 50, 200));

    }

    public void add(GameObject gameObject){
        this.objects.add(gameObject);
    }

    public void remove(GameObject gameObject){
        this.objects.remove(gameObject);
    }

    public void paint(Graphics g) {

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
                // 执行碰撞检测链
                colliderChain.collide(o1, o2);
            }
        }

    }

    public Tank getMainTank() {
        return myTank;
    }

    // 存盘
    public void save(){
        ObjectOutputStream oos = null;
        try {
            File file = new File("d:/tank.data");
            oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(myTank);
            oos.writeObject(objects);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 回档
    public void load() {
        ObjectInputStream ois = null;
        try {
            File file = new File("d:/tank.data");
            ois = new ObjectInputStream(new FileInputStream(file));
            myTank = (Tank) ois.readObject();
            objects = (List<GameObject>) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
