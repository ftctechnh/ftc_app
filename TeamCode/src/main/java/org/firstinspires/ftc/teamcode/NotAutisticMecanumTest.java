package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Rohan Mathur on 9/26/18.
 */
@TeleOp(name="MecanumTest", group="Pushbot")

public class NotAutisticMecanumTest extends LinearOpMode {

	/* Declare OpMode members. */
	HardwarePushbotTest robot   = new HardwarePushbotTest();   // Use a Pushbot's hardware
	private ElapsedTime runtime = new ElapsedTime();
	float moveY;
	float moveX;
	float rotate;
	@Override
	public void runOpMode() {
		robot.init(hardwareMap);
		waitForStart();
		while(opModeIsActive()) {
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

		}


	}

	}
