package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by Nithilan on 10/29/2017.
 * Tank Drive opmode for Golden Gear robot.
 */

public class NS_OpMode_TankDrive extends OpMode {
    NS_Robot_GoldenGears GGRobot = null;
    @Override
    public void init() {
        GGRobot = new NS_Robot_GoldenGears(hardwareMap);

    }

    @Override
    public void loop() {

    }
}
