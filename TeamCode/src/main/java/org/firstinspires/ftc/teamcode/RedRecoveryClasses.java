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
    public void runOpMode() throws InterruptedException {
        drive = new AutoDrive(
                hardwareMap.dcMotor.get("m1"), //fl motor
                hardwareMap.dcMotor.get("m2"), //fr motor
                hardwareMap.dcMotor.get("m3"), //rl motor
                hardwareMap.dcMotor.get("m4"),
                hardwareMap.gyroSensor.get("g1"));
        jewelArm = new JewelArm(
                hardwareMap.servo.get("s4"), //Servo
                hardwareMap.colorSensor.get("cs1")); //Color Sensor
                waitForStart();

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        drive.init();
        if (jewelArm.findJewel() == JewelArm.Color.Red) { //if the arm sees red
            drive.driveTranslateRotate(0,.125,0,0.25);
        }
        else { //if the arm sees blue
            drive.driveTranslateRotate(0,-.125,0,0.25);
        }
        drive.driveTranslateRotate(0, .5, 0, 32);
        Thread.sleep(500);
        drive.rightGyro(0, 0, .5, 90);
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }
}