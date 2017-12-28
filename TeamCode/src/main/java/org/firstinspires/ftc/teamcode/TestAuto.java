package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "TestingAutoWithClass", group = "Autonomous")
public class TestAuto extends LinearOpMode {
	AutoDrive drive;
	ForkLift ForkLift;
	public void runOpMode() throws InterruptedException {
		drive = new AutoDrive(hardwareMap, telemetry);
		ForkLift = new ForkLift(hardwareMap.servo.get("s5"), hardwareMap.servo.get("s6"), hardwareMap.dcMotor.get("m6"), hardwareMap.digitalChannel.get("b0"), hardwareMap.digitalChannel.get("b1"), telemetry);
		drive.init();
		ForkLift.moveMotor(0.2, 100);
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