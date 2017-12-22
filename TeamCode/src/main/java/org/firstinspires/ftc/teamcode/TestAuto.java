package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Autonomous(name = "TestingAutoWithClass", group = "Autonomous")
public class TestAuto extends LinearOpMode {
	AutoDrive drive;
	public void runOpMode() throws InterruptedException {
		drive = new AutoDrive(
				hardwareMap.dcMotor.get("m1"), //fl motor
				hardwareMap.dcMotor.get("m2"), //fr motor
				hardwareMap.dcMotor.get("m3"), //rl motor
				hardwareMap.dcMotor.get("m4"), //rr motor
				hardwareMap,
				telemetry); // gyro
		drive.init();
		telemetry.addLine("Ready to start");
		telemetry.update();
		waitForStart();
		drive.driveTranslateRotate(0,0.5,0,10); // Forward
		Thread.sleep(2000);
		drive.driveTranslateRotate(0,-0.5,0,10); // Backward
		Thread.sleep(2000);
		drive.driveTranslateRotate(0.5,0,0,10); // Move right
		Thread.sleep(2000);
		drive.driveTranslateRotate(-0.5,0,0,10); // Move left
		Thread.sleep(2000);
		drive.driveTranslateRotate(0,0,0.5,10); // Spin right
		Thread.sleep(2000);
		drive.driveTranslateRotate(0,0,-0.5,10); // Spin left
		Thread.sleep(2000);
		drive.rightGyro(0,0,0.125,270);
		Thread.sleep(2000);
		drive.leftGyro(0,0,-0.125,90);
		Thread.sleep(2000);
		drive.rightGyro(0,0,0.125,270);
		Thread.sleep(2000);
		drive.leftGyro(0,0,-0.125,350);

	}
}