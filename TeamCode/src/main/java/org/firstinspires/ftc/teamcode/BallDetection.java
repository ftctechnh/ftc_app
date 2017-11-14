package org.firstinspires.ftc.teamcode;

/**
 * Created by DanielLuo on 11/12/17.
 */

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

public class BallDetection extends LinearOpMode {
    HardwareDRive robot = new HardwareDRive();

    @Override public void runOpMode() {
        robot.init(hardwareMap);

        while (opModeIsActive()) {

        }
    }
}
