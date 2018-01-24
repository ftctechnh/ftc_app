package org.firstinspires.ftc.teamcode;

/**
 * Created by Kaden on 11/26/2017.
 */

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "TestAuto", group = "Autonomous")
public class TestAuto extends LinearOpMode {
	AutoDrive drive;
	ForkLift ForkLift;
	RelicClaw RelicClaw;
	Systems Systems;
	BeehiveVuforia vuforia;
	JewelArm JewelArm;
	public void runOpMode() throws InterruptedException {
		telemetry.addLine("DO NOT PRESS PLAY YET");
		telemetry.update();
		drive = new AutoDrive(hardwareMap, telemetry);
		drive.init(); //Calibrates gyro
		JewelArm = new JewelArm(hardwareMap, telemetry);
		ForkLift = new ForkLift(hardwareMap, telemetry);
		vuforia = new BeehiveVuforia(hardwareMap, telemetry);
		Systems = new Systems(drive, ForkLift, JewelArm, vuforia, telemetry);
		telemetry.addLine("NOW YOU CAN PRESS PLAY");
		telemetry.update();
		waitForStart();
		drive.driveTranslateRotate(0,0.2,0, 20);
	}
}