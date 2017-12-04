package org.firstinspires.ftc.team11248.Autonomous.Park;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.chathamrobotics.common.robot.Robot;
import org.firstinspires.ftc.team11248.Robot11248;

/**
 * Created by Tony_Air on 11/18/17.
 */

@Autonomous(name = "Park")
public class Park extends LinearOpMode{
    @Override
    public void runOpMode() throws InterruptedException {

        Robot11248 robot = new Robot11248(hardwareMap, telemetry);
        robot.init();
        robot.activateColorSensors();

        waitForStart();

        robot.drive(0,-1,0);
        sleep(1100);
        robot.drive(0,0,0);



    }
}
