package com.jiangxb.tank.abstractfactory;

import com.jiangxb.tank.*;

import java.awt.*;

public class RectBullet extends BaseBullet {

    private static final int SPEED = 10;
    public static final int WIDTH = ResourceMgr.bulletD.getWidth();
    public static final int HEIGHT = ResourceMgr.bulletD.getHeight();

    private int x, y;
    private Dir dir;
    private boolean living = true;
    private TankFrame tankFrame = null;
    private Group group = Group.BAD;

    Rectangle rectangle = new Rectangle();

    public RectBullet(int x, int y, Dir dir, Group group, TankFrame tankFrame) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;
        this.tankFrame = tankFrame;

        rectangle.x = this.x;
        rectangle.y = this.y;
        rectangle.width = WIDTH;
        rectangle.height = HEIGHT;

        tankFrame.bullets.add(this);
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

    @Override
    public void paint(Graphics g){

        if (!living){
            tankFrame.bullets.remove(this);
        }

        Color color = g.getColor();
        g.setColor(Color.YELLOW);
        g.fillRect(x, y, 10, 10);
        g.setColor(color);

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

    @Override
    public void collideWith(Tank tank) {

        if (this.group == tank.getGroup()) return;

        // TODO: 用一个rectangle来记录子弹的位置
        if (rectangle.intersects(tank.rectangle)) {
            tank.die();
            this.die();

            int eX = tank.getX() + Tank.WIDTH/2 - Explode.WIDTH/2;
            int eY = tank.getY() + Tank.HEIGHT/2 - Explode.HEIGNT/2;
            tankFrame.explodes.add(tankFrame.gameFactory.createExplode(eX, eY, tankFrame));
        }
    }
}
