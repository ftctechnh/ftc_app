package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "DropRobotAutonomous")
public class DropRobotAutonomous extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        AnimatornicsRobot robot = new AnimatornicsRobot(hardwareMap, telemetry);

        waitForStart();

        robot.moveLift(this, 2.0, "Down", 0.7);
        //robot.moveLift(this, 2.0, "Down", 1.0);
        robot.moveRobot(this, 3.0, "LateralRight", -1.0, 1.0, -1.0, 1.0);
    }
}
