package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Kota Baer on 10/11/2016.
 */

@Autonomous(name = "AutoTest", group = "Concept")
public class AutoDoubleBeaconBlue2 extends LinearOpMode {
    double OneFoot = 12; //in inches

    @Override
    public void runOpMode() throws InterruptedException {
        Hardware5035 robot = new Hardware5035();
        robot.init(hardwareMap);
        waitForStart();
//
// Turn on ball shooter motors
        robot.ballBooster1.setPower(1);
robot.ballBooster2.setPower(1);

        // Driving the robot in position to shoot the balls
        robot.driveForward((OneFoot * 2.25) - 1);



        //shooting the balls
        sleep(2000);
        robot.triggered();
        sleep(500);
        robot.detriggered();
sleep(2000);
        robot.triggered();
        sleep(500);
robot.detriggered();

        //turns off shooter
        robot.ballBooster1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        robot.ballBooster1.setPower(0);
        robot.ballBooster2.setPower(0);

// Aiming for the far corner
        robot.turnDegrees(-24.5);

        // Drive towards the corner
        robot.driveForward(84);


        //Turn to face the wall
        robot.turnDegrees(-60);

        //Calculate how far the wall is
        double walldistance = robot.frontUltra.getUltrasonicLevel();


    }
}
