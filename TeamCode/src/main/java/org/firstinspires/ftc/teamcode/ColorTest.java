package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Rohan Mathur on 10/5/18.
 */
@TeleOp(name="ColorTest", group="Pushbot")

public class ColorTest extends LinearOpMode{
	ColorHardware robot=new ColorHardware();
	@Override
	public void runOpMode() {
		robot.init(hardwareMap);

		waitForStart();
		while(opModeIsActive()) {
			telemetry.addData("red", robot.color.red());
			telemetry.addData("green", robot.color.green());
		}
	}
}
