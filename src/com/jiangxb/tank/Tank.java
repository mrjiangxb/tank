package com.jiangxb.tank;

import com.jiangxb.tank.net.BulletNewMsg;
import com.jiangxb.tank.net.Client;
import com.jiangxb.tank.net.TankJoinMsg;

import javax.swing.event.CaretListener;
import java.awt.*;
import java.util.Random;
import java.util.UUID;

public class Tank {

    private static final int SPEED = 3;

    public static final int WIDTH = ResourceMgr.goodTankU.getWidth();
    public static final int HEIGHT = ResourceMgr.goodTankU.getHeight();

    // 坐标
    private int x, y;
    // 方向
    private Dir dir = Dir.DOWN;
    // 是否移动
    private boolean moving = false;
    private TankFrame tankFrame = null;
    // 是否存活
    private boolean living = true;
    // 所属阵营
    private Group group = Group.BAD;

    Rectangle rectangle = new Rectangle();

    UUID id = UUID.randomUUID();

    private Random random = new Random();

    public Tank(int x, int y, Dir dir, Group group, TankFrame tankFrame) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;
        this.tankFrame = tankFrame;

        rectangle.x = this.x;
        rectangle.y = this.y;
        rectangle.width = WIDTH;
        rectangle.height = HEIGHT;
    }

    public Tank(TankJoinMsg msg) {
        this.x = msg.x;
        this.y = msg.y;
        this.group = msg.group;
        this.moving = msg.moving;
        this.dir = msg.dir;
        this.id = msg.id;

        rectangle.x = this.x;
        rectangle.y = this.y;
        rectangle.width = WIDTH;
        rectangle.height = HEIGHT;
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

    public UUID getId() {
        return this.id;
    }

    public void paint(Graphics g){

        // 在坦克头上显示uuid
        Color color = g.getColor();
        g.setColor(Color.YELLOW);
        g.drawString(id.toString(), this.x, this.y - 10);
        g.setColor(color);

        if (!living) { // 坦克已死 在死亡地标记
            moving = false;
            Color cc = g.getColor();
            g.setColor(Color.WHITE);
            g.drawRect(x, y, WIDTH, HEIGHT);
            g.setColor(cc);
            return;
        }

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

        /*if (this.group == Group.BAD && random.nextInt(100) > 95) {
            this.fire();
        }
        if (this.group == Group.BAD && random.nextInt(100) > 95) {
            randomDir();
        }*/

        boundsCheck();

        // 更新rectangle
        rectangle.x = this.x;
        rectangle.y = this.y;

    }

    private void boundsCheck() {
        if (this.x < 2) x = 2;
        if (this.y < 28) y = 28;
        if (this.x > TankFrame.GAME_WIDTH- Tank.WIDTH -2) x = TankFrame.GAME_WIDTH - Tank.WIDTH -2;
        if (this.y > TankFrame.GAME_HEIGHT - Tank.HEIGHT -2 ) y = TankFrame.GAME_HEIGHT -Tank.HEIGHT -2;
    }

    private void randomDir() {
        this.dir = Dir.values()[random.nextInt(4)];
    }

    public void fire() {
        int bX = this.x + Tank.WIDTH/2 - Bullet.WIDTH/2;
        int bY = this.y + Tank.HEIGHT/2 - Bullet.HEIGHT/2;
        Bullet bullet = new Bullet(this.id, bX, bY, this.dir, this.group);
        TankFrame.getInstance().bullets.add(bullet);
        Client.INSTANCE.send(new BulletNewMsg(bullet));

        if(this.group == Group.GOOD) {
            new Thread( () -> new Audio("audio/tank_fire.wav").play() ).start();
        }

    }

    public void die(){
        this.living = false;
    }

    public boolean isLiving() {
        return living;
    }
}
