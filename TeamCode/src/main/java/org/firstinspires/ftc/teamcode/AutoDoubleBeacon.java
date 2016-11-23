package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Kota Baer on 10/11/2016.
 */

@Autonomous(name = "AutoDoubleBeacon", group = "Concept")
public class AutoDoubleBeacon extends LinearOpMode {
    double OneFoot = 12; //in inches

    @Override
    public void runOpMode() throws InterruptedException {
        Hardware5035 robot = new Hardware5035();
        robot.init(hardwareMap);
        waitForStart();

        robot.turnDegrees(45);
        robot.turnDegrees(-45);
        wait(1000);
        robot.driveForward(12);
        //input Shooting code here!!

        robot.ballBooster1.setPower(1);
        robot.ballBooster2.setPower(1);
        robot.popUp.setPosition(.25);
        wait(250);
        robot.popUp.setPosition(0);
        wait(1000);
        robot.popUp.setPosition(.25);
        wait(250);
        robot.popUp.setPosition(0);
        wait(250);
        robot.ballBooster1.setPower(0);
        robot.ballBooster2.setPower(0);

        robot.turnDegrees(45);
        robot.driveForward(OneFoot * 3);
        robot.turnDegrees(45);
        robot.driveForward(OneFoot * 3);
        //Touch first Button
        robot.driveReverse(OneFoot * 3);
        robot.turnDegrees(-90);
        robot.driveForward(OneFoot * 3.5);
        robot.turnDegrees(90);
        robot.driveForward(OneFoot * 3);
        //touch second button
        robot.driveReverse(OneFoot * 3);


        telemetry.addData("value of driveReverse", OneFoot + " inches");
        telemetry.update();

        /*
        while(true) {
            robot.ballBooster1.setPower(1);
            robot.ballBooster2.setPower(1);
            idle();
        }
        */
    }
}
