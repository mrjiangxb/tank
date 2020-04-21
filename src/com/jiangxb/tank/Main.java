package com.jiangxb.tank;

import com.jiangxb.tank.net.Client;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        TankFrame tankFrame = TankFrame.getInstance();

        // tankFrame.setVisible(true);

        // 播放bgm
        // new Thread( () -> new Audio("audio/war1.wav").loop() ).start();

        new Thread(() -> {
            while (true){
                try {
                    Thread.sleep(25);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                tankFrame.repaint();
            }
        }).start();

        Client.INSTANCE.connect();

    }

}
