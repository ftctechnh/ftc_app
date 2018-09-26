package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by Rohan Mathur on 9/17/18.
 */
@TeleOp(name="TestVuforia", group="Pushbot")
public class VuforiaTest extends LinearOpMode {
	AvesAblazeHardwarePushbot robot= new AvesAblazeHardwarePushbot();
	public void runOpMode(){
		robot.init(hardwareMap);
		while(opModeIsActive()) {
			if (robot.resetCoordinates())
				telemetry.addData("Target", robot.currentTrackable.toString());
			else {
				telemetry.addData("Target", "none");
			}
		}
	}

}
