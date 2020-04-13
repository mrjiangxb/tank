package com.jiangxb.tank;

import java.awt.*;
import java.util.Random;

public class Tank extends GameObject {

    private static final int SPEED = 4;

    public static final int WIDTH = ResourceMgr.goodTankU.getWidth();
    public static final int HEIGHT = ResourceMgr.goodTankU.getHeight();

    // 坐标 父类继承
    // private int x, y;
    // 记录move()前的位置
    int oldX, oldY;
    // 方向
    private Dir dir = Dir.DOWN;
    // 是否移动
    private boolean moving = true;
    // TankFrame tankFrame = null;
    // 是否存活
    private boolean living = true;
    // 所属阵营
    private Group group = Group.BAD;

    private Rectangle rectangle = new Rectangle();

    private Random random = new Random();

    FireStrategy fireStrategy;

    public Tank(int x, int y, Dir dir, Group group) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;

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

        // 加入到 GameModel 的objects中
        GameModel.getInstance().add(this);

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

    public Rectangle getRectangle() {
        return rectangle;
    }

    public int getWIDTH() {
        return WIDTH;
    }

    public int getHEIGHT() {
        return HEIGHT;
    }

    public void paint(Graphics g){

        if (!living) GameModel.getInstance().remove(this);

        switch (dir) {
            case LEFT:
                g.drawImage(this.group == Group.GOOD ? ResourceMgr.goodTankL : ResourceMgr.badTankL, x, y, null);
                break;
            case UP:
                g.drawImage(this.group == Group.GOOD ? ResourceMgr.goodTankU : ResourceMgr.badTankU, x, y, null);
                break;
            case RIGHT:
                g.drawImage(this.group == Group.GOOD ? ResourceMgr.goodTankR : ResourceMgr.badTankR, x, y, null);
                break;
            case DOWN:
                g.drawImage(this.group == Group.GOOD ? ResourceMgr.goodTankD : ResourceMgr.badTankD, x, y, null);
                break;
            default:
                break;
        }
        move();
    }

    // 移动
    private void move() {
        // 记录移动前的坐标
        oldX = x;
        oldY = y;

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

    // 边界检测
    private void boundsCheck() {
        if (this.x < 2) x = 2;
        if (this.y < 28) y = 28;
        if (this.x > TankFrame.GAME_WIDTH- Tank.WIDTH -2) x = TankFrame.GAME_WIDTH - Tank.WIDTH -2;
        if (this.y > TankFrame.GAME_HEIGHT - Tank.HEIGHT -2 ) y = TankFrame.GAME_HEIGHT -Tank.HEIGHT -2;
    }

    // 随机方向
    private void randomDir() {
        this.dir = Dir.values()[random.nextInt(4)];
    }

    // 回退
    public void back(){
        this.x = oldX;
        this.y = oldY;
    }

    // 开火
    public void fire() {
        fireStrategy.fire(this);
    }

    public void die(){
        this.living = false;
    }

}
