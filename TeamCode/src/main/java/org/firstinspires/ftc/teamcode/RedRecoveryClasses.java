package org.firstinspires.ftc.teamcode;

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

        drive = new AutoDrive(
                hardwareMap.dcMotor.get("m1"), //fl motor
                hardwareMap.dcMotor.get("m2"), //fr motor
                hardwareMap.dcMotor.get("m3"), //rl motor
                hardwareMap.dcMotor.get("m4"),
                hardwareMap.gyroSensor.get("g1"));
        drive.init(); //Calibrates gyro
        jewelArm = new JewelArm(
                hardwareMap.servo.get("s4"), //Servo
                hardwareMap.colorSensor.get("cs1"), // Color sensor
                telemetry); //telemetry for debugging
        ForkLift = new ForkLift(
                hardwareMap.servo.get("s5"), //rightClaw
                hardwareMap.servo.get("s6"), //leftClaw
                hardwareMap.dcMotor.get("m6"), //updown
                hardwareMap.touchSensor.get("b0"), //top button
                hardwareMap.touchSensor.get("b1")); //bottom button
        RelicClaw = new RelicClaw(
                hardwareMap.servo.get("s1"), //claw
                hardwareMap.servo.get("s2"), //arm
                hardwareMap.dcMotor.get("m5")); //motor

        waitForStart();
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //ForkLift.closeClaw();
        //RelicClaw.init();
        jewelArm.down();
        color = jewelArm.findJewel();
        if (color == "Red") { //if the arm sees red
            drive.driveTranslateRotate(0, .125, 0, 2);
            drive.driveTranslateRotate(0, -.125, 0, 2);
        } else if (color == "Blue") { //if the arm sees blue
            drive.driveTranslateRotate(0, -.125, 0, 2);
            drive.driveTranslateRotate(0, .125, 0, 2);
        } else {
        }
        Thread.sleep(500);
        jewelArm.up();
        Thread.sleep(500);
        drive.driveTranslateRotate(0, -.5, 0, 32);
        Thread.sleep(500);
        drive.rightGyro(0, 0, .5, 90);
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }
}
