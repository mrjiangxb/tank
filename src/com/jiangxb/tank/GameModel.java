package com.jiangxb.tank;

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
    List<Bullet> bullets = new ArrayList<>();
    List<Tank> tanks = new ArrayList<>();
    List<Explode> explodes = new ArrayList<>();




    private GameModel() {
        int initTankCount = Integer.parseInt((String) PropertyMgr.get("initTankCount")) ;

        // 初始化敌方坦克
        for (int i = 0; i < initTankCount; i++) {
            tanks.add(new Tank(50+i*80, 200, Dir.DOWN, Group.BAD, this));
        }
    }

    public static GameModel getInstance() {
        return INSTANCE;
    }

    public void paint(Graphics g) {

        Color color = g.getColor();
        g.setColor(Color.WHITE);
        g.drawString("子弹数"+bullets.size(), 10, 60);
        g.drawString("敌人数"+tanks.size(), 10, 80);
        g.drawString("爆炸数"+explodes.size(), 10, 100);
        g.setColor(color);

        myTank.paint(g);
        // 若用迭代器 删除时会报错
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).paint(g);
        }

        for (int i = 0; i < tanks.size(); i++) {
            tanks.get(i).paint(g);
        }

        for (int i = 0; i < explodes.size(); i++) {
            explodes.get(i).paint(g);
        }

        // 碰撞检测
        for (int i = 0; i < bullets.size(); i++) {
            for (int j = 0; j < tanks.size(); j++) {
                bullets.get(i).collideWith(tanks.get(j));
            }
        }

    }

    public Tank getMainTank() {
        return myTank;
    }
}
