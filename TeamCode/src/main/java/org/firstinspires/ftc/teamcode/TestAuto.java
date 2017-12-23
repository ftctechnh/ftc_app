package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Autonomous(name = "TestingAutoWithClass", group = "Autonomous")
public class TestAuto extends LinearOpMode {
	AutoDrive drive;
	public void runOpMode() throws InterruptedException {
		drive = new AutoDrive(hardwareMap.dcMotor.get("m1"), hardwareMap.dcMotor.get("m2"), hardwareMap.dcMotor.get("m3"), hardwareMap.dcMotor.get("m4"), hardwareMap, telemetry);
		drive.init();
		telemetry.addLine("Ready to start");
		telemetry.update();
		waitForStart();
		drive.driveTranslateRotate(0,0.1,0,10); // Forward
		Thread.sleep(2000);
		drive.driveTranslateRotate(0,-0.1,0,10); // Backward
		Thread.sleep(2000);
		drive.driveTranslateRotate(0.1,0,0,10); // Move right
		Thread.sleep(2000);
		drive.driveTranslateRotate(-0.1,0,0,10); // Move left
		Thread.sleep(2000);
		drive.driveTranslateRotate(0,0,0.1,10); // Spin right
		Thread.sleep(2000);
		drive.driveTranslateRotate(0,0,-0.1,10); // Spin left
		Thread.sleep(2000);
		drive.rightGyro(0,0,0.125,-90);
		Thread.sleep(2000);
		drive.leftGyro(0,0,-0.125,90);
		Thread.sleep(2000);
		drive.rightGyro(0,0,0.125,90);
		Thread.sleep(2000);
		drive.leftGyro(0,0,-0.125,-90);
		Thread.sleep(2000);
		drive.rightGyro(0,0,0.125,0);

	}
}