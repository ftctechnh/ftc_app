package org.firstinspires.ftc.simulator;


import org.firstinspires.ftc.teamcode.autonomous.PurePursuitController;
import org.firstinspires.ftc.teamcode.common.math.Point;
import org.firstinspires.ftc.teamcode.common.math.Pose;
import org.firstinspires.ftc.teamcode.robot.sixwheel.SixWheelPowers;

import java.io.IOException;

public class Main {


    public static void main(String[] args) throws IOException {
        new Main().run();
    }

    public void run() throws IOException {
        VirtualSixWheelHardware robot = new VirtualSixWheelHardware(this);
        UDPThread udpServer = new UDPThread(20);

        double angle = 0;
        while(true) {
            double y = Math.sin(angle);
            Pose pose = new Pose(y * 20, 0, 0);
            udpServer.sendMessage(pose);

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            angle += 0.1;
        }
    }
}
