package org.firstinspires.ftc.robotcontroller.external.samples;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by Rohan Mathur on 10/24/18.
 */

@TeleOp(name="MRTest", group="pushbot")
public class MRTest extends OpMode {

	DcMotor motor0;
	DcMotor motor1;

	CRServo servo0;
	CRServo servo1;

	@Override
	public void init() {
		motor0 = hardwareMap.dcMotor.get("motor0");
		motor1 = hardwareMap.dcMotor.get("motor1");
		motor0.setDirection(DcMotorSimple.Direction.FORWARD);
		motor1.setDirection(DcMotorSimple.Direction.FORWARD);
		motor0.setPower(0);
		motor0.setPower(0);

		servo0 = hardwareMap.crservo.get("servo0");
		servo1 = hardwareMap.crservo.get("servo1");
	}

	@Override
	public void loop() {
		if(gamepad1.left_stick_y != 0){
			motor0.setPower(gamepad1.left_stick_y);
		}
		else{
			motor0.setPower(0);
		}

		if(gamepad1.right_stick_y != 0){
			motor1.setPower(gamepad1.right_stick_y);
		}
		else{
			motor1.setPower(0);
		}

		if(gamepad1.left_trigger>0){
			servo0.setPower(1);
		}
		else if(gamepad1.left_bumper){
			servo0.setPower(-1);
		}
		else if(gamepad1.a){
			servo0.setPower(0);
		}

		if(gamepad1.right_trigger>0){
			servo1.setPower(1);
		}
		else if(gamepad1.right_bumper){
			servo1.setPower(-1);
		}
		else if(gamepad1.b){
			servo1.setPower(0);
		}
	}
}
