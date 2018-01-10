package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "TestingAutoWithClass", group = "Autonomous")

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
		JewelArm = new JewelArm(hardwareMap.servo.get("s4"), hardwareMap.colorSensor.get("cs1"), telemetry);
		ForkLift = new ForkLift(hardwareMap.servo.get("s5"), hardwareMap.servo.get("s6"), hardwareMap.dcMotor.get("m6"), hardwareMap.digitalChannel.get("b0"), hardwareMap.digitalChannel.get("b1"), telemetry);
		vuforia = new BeehiveVuforia(hardwareMap, telemetry);
		Systems = new Systems(drive, ForkLift, JewelArm,vuforia);
		telemetry.addLine("NOW YOU CAN PRESS PLAY");
		telemetry.update();
		waitForStart();
		Systems.findJewel(Color.RED);
	}
}