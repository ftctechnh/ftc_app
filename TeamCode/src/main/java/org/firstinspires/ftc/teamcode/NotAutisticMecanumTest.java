package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;

/**
 * Created by Rohan Mathur on 9/26/18.
 */
@TeleOp(name="MecanumTest", group="Pushbot")

public class NotAutisticMecanumTest extends LinearOpMode {

	/* Declare OpMode members. */
	AvesAblazeHardwarePushbot robot   = new AvesAblazeHardwarePushbot();   // Use a Pushbot's hardware
	private ElapsedTime runtime = new ElapsedTime();
	float moveY;
	float moveX;
	float rotate;
	@Override
	public void runOpMode() {
		robot.init(hardwareMap);
		waitForStart();
		while(opModeIsActive()) {
            telemetry.update();
            //Display coordinates and trackable
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

            //Move Robot
			moveY = (float) Range.clip(-gamepad1.left_stick_y, -1, 1);
			moveX = (float) Range.clip(-gamepad1.left_stick_x, -1, 1);
			rotate = (float) Range.clip(-gamepad1.right_stick_x, -1, 1);
			if ((Math.abs(moveY) > 0.25)) {
				robot.moveUpDown(-moveY);

			}
			else if (Math.abs(moveX) > 0.25)
				robot.moveLeftRight(-moveX);
			else if (Math.abs(rotate) > 0.25) {
				robot.rotate(-rotate);
			} else {
				robot.moveUpDown(0);
				robot.moveLeftRight(0);
				robot.rotate(0);
			}

			//Move team marker mechanism
			if(gamepad1.left_bumper){
				robot.door.setPosition(1);
			}
			if(gamepad1.right_bumper){
				robot.door.setPosition(0);
			}

			if(gamepad1.right_trigger>0.1){
				robot.marker.setPower(0.5-gamepad1.right_trigger/2);
			}
			else if(gamepad1.left_trigger>0.1){
				robot.marker.setPower(0.5+(gamepad1.left_trigger/2));
			}
			else{
				//robot.marker.setPower(0.6);
			}

		}


	}

	}
