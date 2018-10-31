package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Rohan Mathur on 10/30/18.
 */

@TeleOp(name="NewRevTester", group="pushbot")
public class NewRevTester extends OpMode {

	DcMotor motor0;
	Servo servo0;

	@Override
	public void init() {
		motor0 = hardwareMap.dcMotor.get("motor0");
		motor0.setDirection(DcMotorSimple.Direction.FORWARD);
		motor0.setPower(0);

		servo0 = hardwareMap.servo.get("servo0");
		servo0.setPosition(0.5);
	}

	@Override
	public void loop() {
		if(gamepad1.right_bumper){
			motor0.setPower(1);
		}
		else if(gamepad1.left_bumper){
			motor0.setPower(-1);
		}
		else{
			motor0.setPower(0);
		}

		if(gamepad1.a){
			servo0.setPosition(1);
		}
		if(gamepad1.b){
			servo0.setPosition(0);
		}
	}
}
