package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;

/**
 * Created by Rohan Mathur on 9/17/18.
 */
@TeleOp(name="TestVuforia", group="Pushbot")
public class VuforiaTest extends LinearOpMode {
	AvesAblazeHardwarePushbot robot= new AvesAblazeHardwarePushbot();
	public void runOpMode(){
		robot.init(hardwareMap);
		telemetry.addData("does this work?", "yes");
		telemetry.update();
		waitForStart();
		while(opModeIsActive()) {
			telemetry.addData("worko?", "yes");
			telemetry.update();
			if (robot.resetCoordinates()) {
				telemetry.addData("Target", robot.currentTrackable.getName());
				// express position (translation) of robot in inches.
				VectorF translation = robot.lastLocation.getTranslation();
				//ArrayList translation[x, y, z]
				telemetry.addData("x", translation.get(0) / robot.mmPerInch);
				telemetry.addData("y", translation.get(1) / robot.mmPerInch);

				// Map rotation firstAngle: Roll; secondAngle: Pitch; thirdAngle: Heading
				Orientation rotation = Orientation.getOrientation(robot.lastLocation, EXTRINSIC, XYZ, DEGREES);
				telemetry.addData("Heading", rotation.thirdAngle);
			}
			else {
				telemetry.addData("Target", "none");
			}
		}
	}

}
