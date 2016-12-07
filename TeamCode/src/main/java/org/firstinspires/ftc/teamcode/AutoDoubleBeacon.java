package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Kota Baer on 10/11/2016.
 */

@Autonomous(name = "AutoDoubleBeacon", group = "Concept")
public class AutoDoubleBeacon extends LinearOpMode {
    double OneFoot = 12; //in inches

    @Override
    public void runOpMode() throws InterruptedException {
        ElapsedTime wait = new ElapsedTime(ElapsedTime.Resolution.SECONDS);
        Hardware5035 robot = new Hardware5035();
        robot.init(hardwareMap);
        waitForStart();

        //robot.turnDegrees(45);
        //robot.turnDegrees(-45);
        wait.reset();
        robot.ballBooster1.setPower(1);
        robot.ballBooster2.setPower(1);
        robot.driveForward(OneFoot * 2);
        //while(robot.ballBooster1.getPower() <= 1 && robot.ballBooster2.getPower() <= 1) {
        //    robot.ballBooster1.setPower(Math.max(robot.ballBooster1.getPower() + .000005, 1));
        //    robot.ballBooster2.setPower(Math.max(robot.ballBooster2.getPower() + .000005, 1));
        //}
        wait.seconds();
        wait.startTime();
        while(wait.time() < 2) {
            robot.triggered();
        }
        wait.reset();
        wait.startTime();
        while(wait.time() < 2) {
            robot.detriggered();
        }
        wait.reset();
        wait.startTime();
        while(wait.time() < 2) {
            robot.triggered();
        }
        wait.reset();
        robot.detriggered();
        robot.ballBooster1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        robot.ballBooster1.setPower(0);
        robot.ballBooster2.setPower(0);
        robot.driveForward(OneFoot * 1);
        robot.turnDegrees(-90);
        /*

        robot.driveForward(12);
        //input Shooting code here!!

        robot.ballBooster1.setPower(1);
        robot.ballBooster2.setPower(1);
        robot.triggered();
        while(wait.time() <= 250){
            telemetry.addData("waiting", "waiting");
            telemetry.update();
        }
        robot.detriggered();
        while(wait.time() <= 1000){
            telemetry.addData("waiting", "waiting");
            telemetry.update();
        }
        robot.triggered();
        while(wait.time() <= 250){
            telemetry.addData("waiting", "waiting");
            telemetry.update();
        }
        robot.detriggered();
        while(wait.time() <= 250){
            telemetry.addData("waiting", "waiting");
            telemetry.update();
        }
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
        */

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
