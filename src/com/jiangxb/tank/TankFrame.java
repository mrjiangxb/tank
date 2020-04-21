package com.jiangxb.tank;

import com.jiangxb.tank.net.Client;
import com.jiangxb.tank.net.TankDirChangeMsg;
import com.jiangxb.tank.net.TankStartMovingMsg;
import com.jiangxb.tank.net.TankStopMsg;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;

public class TankFrame extends Frame {

    private static final TankFrame INSTANCE = new TankFrame();

    public static final int GAME_WIDTH = 900, GAME_HEIGHT = 600;

    Random r = new Random();

    Tank myTank = new Tank(r.nextInt(GAME_WIDTH),r.nextInt(GAME_HEIGHT), Dir.UP, Group.GOOD, this);
    List<Bullet> bullets = new ArrayList<>();
    Map<UUID, Tank> tanks = new HashMap<>();
    // Map<UUID, Bullet> bullets = new HashMap<>();
    List<Explode> explodes = new ArrayList<>();



    public static TankFrame getInstance() {
        return INSTANCE;
    }

    public void addTank(Tank tank){
        tanks.put(tank.getId(), tank);
    }

    public Tank getMainTank(){
        return this.myTank;
    }

    // 主窗口设置
    private TankFrame (){
        this.setSize(GAME_WIDTH, GAME_HEIGHT);
        this.setResizable(false);
        this.setTitle("tank war");
        this.setVisible(true);

        this.addKeyListener(new MyKeyListener());

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    // 双缓冲解决屏幕闪烁
    Image offScreenImage = null;
    @Override
    public void update(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = this.createImage(GAME_WIDTH,GAME_HEIGHT);
        }
        Graphics gOffScreen = offScreenImage.getGraphics();
        Color color = gOffScreen.getColor();
        gOffScreen.setColor(Color.BLACK);
        gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        gOffScreen.setColor(color);
        paint(gOffScreen);
        g.drawImage(offScreenImage, 0, 0, null);
    }

    // 画出画面
    @Override
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

        // java8 stream api
        tanks.values().stream().forEach((e) -> e.paint(g));

        for (int i = 0; i < explodes.size(); i++) {
            explodes.get(i).paint(g);
        }

        // 碰撞检测
        Collection<Tank> values = tanks.values();
        for (int i = 0; i < bullets.size(); i++) {
            for (Tank tank : values) {
                bullets.get(i).collideWith(tank);
            }
        }

    }

    public Tank findTankByUUID(UUID id) {
        return tanks.get(id);
    }

    public Bullet findBulletByUUID(UUID id) {
        for(int i=0; i<bullets.size(); i++) {
            if(bullets.get(i).getId().equals(id))
                return bullets.get(i);
        }
        return null;
    }

    public void addBullet(Bullet bullet) {
        this.bullets.add(bullet);
    }

    public void addExplode(int x, int y) {
        int eX = x + Tank.WIDTH/2;
        int eY = y + Tank.HEIGHT/2;
        this.explodes.add(new Explode(eX, eY));
    }

    // 键盘监听
    class MyKeyListener extends KeyAdapter {

        boolean bL = false;
        boolean bR = false;
        boolean bU = false;
        boolean bD = false;

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            switch (key) {
                case KeyEvent.VK_LEFT:
                    bL = true;
                    break;
                case KeyEvent.VK_RIGHT:
                    bR = true;
                    break;
                case KeyEvent.VK_UP:
                    bU = true;
                    break;
                case KeyEvent.VK_DOWN:
                    bD = true;
                    break;
                default:
                    break;
            }

            setMainTankDir();

        }

        @Override
        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();
            switch (key) {
                case KeyEvent.VK_LEFT:
                    bL = false;
                    break;
                case KeyEvent.VK_RIGHT:
                    bR = false;
                    break;
                case KeyEvent.VK_UP:
                    bU = false;
                    break;
                case KeyEvent.VK_DOWN:
                    bD = false;
                    break;
                case KeyEvent.VK_CONTROL:
                    if (myTank.isLiving()){
                        myTank.fire();
                    }
                    break;
                default:
                    break;
            }

            setMainTankDir();

        }

        private void setMainTankDir() {

            // 保存方向
            Dir dir = myTank.getDir();

            if (!bL && !bU && !bR && !bD) {
                myTank.setMoving(false);
                if (myTank.isLiving()) {
                    Client.INSTANCE.send(new TankStopMsg(getMainTank()));
                }
            }else {
                myTank.setMoving(true);
                if (bL) myTank.setDir(Dir.LEFT);
                if (bU) myTank.setDir(Dir.UP);
                if (bR) myTank.setDir(Dir.RIGHT);
                if (bD) myTank.setDir(Dir.DOWN);
                // 发出坦克移动的消息
                if (!myTank.isMoving() && myTank.isLiving()) {
                    Client.INSTANCE.send(new TankStartMovingMsg(getMainTank()));
                    myTank.setMoving(true);
                }
                if (dir != myTank.getDir() && myTank.isLiving()) {
                    Client.INSTANCE.send(new TankDirChangeMsg(myTank));
                }
            }
        }

    }


}
