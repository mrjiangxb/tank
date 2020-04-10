package com.jiangxb.tank;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        TankFrame tankFrame = new TankFrame();

        // 播放bgm
        // new Thread( () -> new Audio("audio/war1.wav").loop() ).start();

        while (true){
            Thread.sleep(25);
            tankFrame.repaint();
        }

    }

}
