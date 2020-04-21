package com.jiangxb.tank;

import com.jiangxb.tank.net.Client;
import com.jiangxb.tank.net.TankDieMsg;

import java.awt.*;
import java.util.UUID;

public class Bullet {

    private static final int SPEED = 10;
    public static final int WIDTH = ResourceMgr.bulletD.getWidth();
    public static final int HEIGHT = ResourceMgr.bulletD.getHeight();

    private UUID id = UUID.randomUUID();
    private UUID playerId;

    private int x, y;
    private Dir dir;
    private boolean living = true;
    private Group group = Group.BAD;

    Rectangle rectangle = new Rectangle();

    public Bullet(UUID playerId, int x, int y, Dir dir, Group group) {
        this.playerId = playerId;
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;

        rectangle.x = this.x;
        rectangle.y = this.y;
        rectangle.width = WIDTH;
        rectangle.height = HEIGHT;
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

    public Dir getDir() {
        return dir;
    }

    public void setDir(Dir dir) {
        this.dir = dir;
    }

    public boolean isLive() {
        return living;
    }

    public void setLive(boolean living) {
        this.living = living;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public UUID getId() {
        return id;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public void paint(Graphics g){

        if (!living){
            TankFrame.getInstance().bullets.remove(this);
        }

        switch (dir) {
            case LEFT:
                g.drawImage(ResourceMgr.bulletL, x, y, null);
                break;
            case UP:
                g.drawImage(ResourceMgr.bulletU, x, y, null);
                break;
            case RIGHT:
                g.drawImage(ResourceMgr.bulletR, x, y, null);
                break;
            case DOWN:
                g.drawImage(ResourceMgr.bulletD, x, y, null);
                break;
            default:
                break;
        }
        move();
    }

    private void move() {
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

        // 更新rectangle
        rectangle.x = this.x;
        rectangle.y = this.y;

        if (x < 0 || y < 0 || x > TankFrame.GAME_WIDTH || y > TankFrame.GAME_HEIGHT) this.living = false;

    }

    public void die(){
        this.living = false;
    }

    public void collideWith(Tank tank) {

        System.out.println("bullet-->" + this.playerId);
        System.out.println("tank id-->" + tank.getId());
        if (this.playerId.equals(tank.getId())){
            return;
        }
        System.out.println("||||||||||||||||||||||||||||||||||||");
        if (this.living && tank.isLiving() && this.rectangle.intersects(tank.rectangle)) {
            tank.die();
            this.die();
            System.out.println("send die message !!!!!!");
            Client.INSTANCE.send(new TankDieMsg(this.id, tank.getId()));
            // TankFrame.getInstance().addExplode(tank.getX(), tank.getY());
        }

    }

    public void setId(UUID id) {
        this.id = id;
    }
}
