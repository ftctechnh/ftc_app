package org.firstinspires.ftc.team11248.Autonomous.Jewel;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.team11248.Robot11248;

/**
 * Created by Tony_Air on 11/28/17.
 */


@Autonomous(name = "Blue Jewel")
public class Auto_Jewel_Blue extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        Robot11248 robot = new Robot11248(hardwareMap, telemetry);
        robot.init();
        robot.activateColorSensors();
        robot.calibrateGyro();

        waitForStart();

        robot.lowerJewelArm();
        sleep(1000);

        boolean isLeftJewelRed = robot.jewelColor.isRed();
        robot.drive(0,0,-.5 * (isLeftJewelRed?1:-1) ); //negative rotation rotates toward jewel color sensor is sensing
        sleep(500);

        robot.stop();
        sleep(500);

        robot.drive(0,0,.5 * (isLeftJewelRed?1:-1) ); //negative rotation rotates toward jewel color sensor is sensing
        sleep(500);
        robot.stop();



    }
}
