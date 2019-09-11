package org.firstinspires.ftc.simulator;


import org.firstinspires.ftc.teamcode.autonomous.PurePursuitController;
import org.firstinspires.ftc.teamcode.common.math.Point;
import org.firstinspires.ftc.teamcode.common.math.Pose;
import org.firstinspires.ftc.teamcode.robot.sixwheel.SixWheelPowers;
import org.firstinspires.ftc.teamcode.robot.sixwheel.VirtualSixWheelHardware;

import java.io.IOException;

public class Main {
    double FRAMERATE = 20;

    public static void main(String[] args) throws IOException {
        new Main().run();
    }

    public void run() throws IOException {
        VirtualSixWheelHardware robot = new VirtualSixWheelHardware(new Pose(20, 20, 0));
        TXHandler udpServer = new TXHandler(FRAMERATE);

        while(true) {
            SixWheelPowers p = PurePursuitController.goToPosition(robot.pose(), new Point(0, 0));
            robot.setWheelPowers(new SixWheelPowers(-p.left, -p.right));
            robot.elapse(1000 / FRAMERATE);
            udpServer.sendMessage(robot.pose());

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
