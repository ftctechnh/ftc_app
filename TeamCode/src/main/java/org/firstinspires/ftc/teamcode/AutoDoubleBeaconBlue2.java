package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Kota Baer on 10/11/2016.
 */

@Autonomous(name = "AutoDoubleBeaconBlue2", group = "Concept")
public class AutoDoubleBeaconBlue2 extends LinearOpMode {
    double OneFoot = 12; //in inches

    @Override
    public void runOpMode() throws InterruptedException {
        Hardware5035 robot = new Hardware5035();
        robot.init(hardwareMap);
        waitForStart();
        //robot.turnDegrees(45);
        //robot.turnDegrees(-45);
        robot.ballBooster1.setPower(1);
        robot.ballBooster2.setPower(1);
        robot.driveForward(OneFoot * 2);
        sleep(2000);
        robot.triggered();
        sleep(500);
        robot.detriggered();
        sleep(2000);
        robot.triggered();
        sleep(500);
        robot.detriggered();
        robot.ballBooster1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        robot.ballBooster1.setPower(0);
        robot.ballBooster2.setPower(0);
        robot.driveForward(OneFoot * 1);
        robot.driveForward(OneFoot * 4);
        robot.turnDegrees(90);
        while(robot.leftLightSensor.getLightDetected() < .25 || robot.rightLightSensor.getLightDetected() < .25){
            robot.setDrivePower(.25);
        }
        robot.setDrivePower(0);
        robot.turnDegrees(-90);
        robot.driveForward(.5);
        telemetry.addData("value of driveReverse", OneFoot + " inches");
        telemetry.update();

    }
}

