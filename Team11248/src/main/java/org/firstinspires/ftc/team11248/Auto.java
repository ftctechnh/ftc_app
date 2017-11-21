package org.firstinspires.ftc.team11248;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.chathamrobotics.common.robot.Robot;

/**
 * Created by Tony_Air on 11/18/17.
 */

@Autonomous(name = "drive")
public class Auto extends LinearOpMode{
    @Override
    public void runOpMode() throws InterruptedException {

        Robot11248 robot = new Robot11248(hardwareMap, telemetry);
        robot.init();
        robot.activateColorSensors();

        waitForStart();

        robot.drive(0,-1,0);
        sleep(1100);
        robot.drive(0,0,0);


//        robot.lowerJewelArm();

//        boolean isBlue = robot.isJewelBlue();
//
//        robot.drive(0,0, isBlue?-1:1);
//        sleep(1000);
//        robot.drive(0,0, isBlue?1:-1);
//        sleep(1000);
//        robot.drive(0,0, 0);

//        robot.raiseJewelArm();





    }
}
