package com.jiangxb.tank.abstractfactory;

import com.jiangxb.tank.*;

import java.awt.*;
import java.awt.image.ColorConvertOp;
import java.util.Random;

public class RectTank extends BaseTank {

    private static final int SPEED = 5;

    public static final int WIDTH = ResourceMgr.goodTankU.getWidth();
    public static final int HEIGHT = ResourceMgr.goodTankU.getHeight();

    // 坐标
    private int x, y;
    // 方向
    private Dir dir = Dir.DOWN;
    // 是否移动
    private boolean moving = true;
    TankFrame tankFrame = null;
    // 是否存活
    private boolean living = true;


    public Rectangle rectangle = new Rectangle();

    private Random random = new Random();

    FireStrategy fireStrategy;

    public RectTank(int x, int y, Dir dir, Group group, TankFrame tankFrame) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;
        this.tankFrame = tankFrame;

        rectangle.x = this.x;
        rectangle.y = this.y;
        rectangle.width = WIDTH;
        rectangle.height = HEIGHT;

        if (group == Group.GOOD) {
            // 从配置文件读取开火策略
            String goodFSName = (String) PropertyMgr.get("goodFS");
            try {
                fireStrategy = (FireStrategy)Class.forName(goodFSName).newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else {
            fireStrategy = new DefaultFireStrategy();
        }

    }

    public Dir getDir() {
        return dir;
    }

    public void setDir(Dir dir) {
        this.dir = dir;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public void paint(Graphics g){

        if (!living) tankFrame.tanks.remove(this);

        Color color = g.getColor();
        g.setColor(group == Group.GOOD ? Color.RED : Color.LIGHT_GRAY);
        g.fillRect(x, y, 40, 40);
        g.setColor(color);

        move();
    }

    private void move() {

        if (!moving) return;

        switch (dir) {
            case LEFT:
                x -= SPEED;
                break;
            case UP:
                y -= SPEED;
                break;
            case RIGHT:
                x += SPEED;
                break;
            case DOWN:
                y += SPEED;
                break;
            default:
                break;
        }

        if (this.group == Group.BAD && random.nextInt(100) > 95) {
            this.fire();
        }
        if (this.group == Group.BAD && random.nextInt(100) > 95) {
            randomDir();
        }
        /*if (this.group == Group.GOOD){
            new Thread( () -> new Audio("audio/tank_move.wav").play() ).start();
        }*/

        boundsCheck();

        // 更新rectangle
        rectangle.x = this.x;
        rectangle.y = this.y;

    }

    private void boundsCheck() {
        if (this.x < 2) x = 2;
        if (this.y < 28) y = 28;
        if (this.x > TankFrame.GAME_WIDTH- RectTank.WIDTH -2) x = TankFrame.GAME_WIDTH - RectTank.WIDTH -2;
        if (this.y > TankFrame.GAME_HEIGHT - RectTank.HEIGHT -2 ) y = TankFrame.GAME_HEIGHT - RectTank.HEIGHT -2;
    }

    private void randomDir() {
        this.dir = Dir.values()[random.nextInt(4)];
    }

    public void fire() {
        // 为了代码简单先不使用策略模式
        // fireStrategy.fire(this);

        int bX = this.getX() + Tank.WIDTH/2 - Bullet.WIDTH/2;
        int bY = this.getY() + Tank.HEIGHT/2 - Bullet.HEIGHT/2;
        tankFrame.gameFactory.createBullet(bX, bY, this.getDir(), this.getGroup(), this.tankFrame);
        if (this.getGroup() == Group.GOOD) {
            new Thread( () -> new Audio("audio/tank_fire.wav").play() ).start();
        }

    }

    public void die(){
        this.living = false;
    }
}