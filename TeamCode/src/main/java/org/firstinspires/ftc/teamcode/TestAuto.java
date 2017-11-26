package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "TestingAutoWithClass", group = "Autonomous")
public class TestAuto extends LinearOpMode {
	AutoDrive drive;
	public void runOpMode() throws InterruptedException {
		drive = new AutoDrive(
				hardwareMap.dcMotor.get("m1"), //fl motor
				hardwareMap.dcMotor.get("m2"), //fr motor
				hardwareMap.dcMotor.get("m3"), //rl motor
				hardwareMap.dcMotor.get("m4")); //rr motor
		waitForStart();
		drive.driveTranslateRotate(0.5,0,0,1, 1);
		Thread.sleep(2000);
		drive.driveTranslateRotate(-.5,0,0,1,1);
		Thread.sleep(2000);
		drive.driveTranslateRotate(0,0.5,0,1,1);
		Thread.sleep(2000);
		drive.driveTranslateRotate(0,-0.5,0,1,1);
		Thread.sleep(2000);
		drive.driveTranslateRotate(0,0,0.5,1,1);
		Thread.sleep(2000);
		drive.driveTranslateRotate(0,0,-0.5,1,1);
	}

}