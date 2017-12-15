package org.firstinspires.ftc.teamcode;

import android.content.res.Resources;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by BeehiveRobotics-3648 on 11/28/2017.
 */
@Autonomous(name = "RAutonomousRC", group = "Autonomous")
public class RedRecoveryClasses extends LinearOpMode {
    AutoDrive drive;
    JewelArm jewelArm;
    ForkLift ForkLift;
    RelicClaw RelicClaw;
    String color;

    public void runOpMode() throws InterruptedException {
        drive = new AutoDrive(hardwareMap.dcMotor.get("m1"), hardwareMap.dcMotor.get("m2"), hardwareMap.dcMotor.get("m3"), hardwareMap.dcMotor.get("m4"), hardwareMap.gyroSensor.get("g1"), telemetry);
        drive.init(); //Calibrates gyro
        jewelArm = new JewelArm(hardwareMap.servo.get("s4"), hardwareMap.colorSensor.get("cs1"), telemetry);
        ForkLift = new ForkLift(hardwareMap.servo.get("s5"), hardwareMap.servo.get("s6"), hardwareMap.dcMotor.get("m6"), hardwareMap.digitalChannel.get("b0"), hardwareMap.digitalChannel.get("b1"), telemetry);
        RelicClaw = new RelicClaw(hardwareMap.servo.get("s1"), hardwareMap.servo.get("s2"), hardwareMap.dcMotor.get("m5"), telemetry);
        telemetry.addData("ready to start", null);
        telemetry.update();
        waitForStart();
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //ForkLift.closeClaw();
        //RelicClaw.init();
        jewelArm.init();
        jewelArm.down();
        ForkLift.closeClaw();
        color = jewelArm.findJewel();
        if (color == "Red") { //if the arm sees red
            drive.driveTranslateRotate(0, .125, 0, 2);
            drive.driveTranslateRotate(0, -.125, 0, 2);
        } else if (color == "Blue") { //if the arm sees blue
            drive.driveTranslateRotate(0, -.125, 0, 2);
            drive.driveTranslateRotate(0, .125, 0, 2);
        } else {}
        jewelArm.up();
        Thread.sleep(500);
        drive.driveTranslateRotate(0, -.5, 0, 32);
        drive.leftGyro(0, 0, -0.05, 90);
        drive.driveTranslateRotate(0,0.5,0, 5);
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }
}