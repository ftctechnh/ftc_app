package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by BeehiveRobotics-3648 on 11/28/2017.
 */
@Autonomous(name = "RAutonomousRC", group = "Autonomous")
public class RedRecoveryClasses extends LinearOpMode {
    AutoDrive drive;
    public void runOpMode() throws InterruptedException {
        drive = new AutoDrive(
                hardwareMap.dcMotor.get("m1"), //fl motor
                hardwareMap.dcMotor.get("m2"), //fr motor
                hardwareMap.dcMotor.get("m3"), //rl motor
                hardwareMap.dcMotor.get("m4"),
                hardwareMap.gyroSensor.get("g1"));
                waitForStart();
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        
        drive.driveTranslateRotate(0, .5, 0, 32);
        Thread.sleep(500);
        drive.rightGyro(0, 0, .5, 90);
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }
}