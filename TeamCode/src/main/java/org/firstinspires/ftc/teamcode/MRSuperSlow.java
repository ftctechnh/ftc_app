package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by Rohan Mathur on 10/24/18.
 */

@TeleOp(name="MRSuperSlow", group="pushbot")
public class MRSuperSlow extends OpMode {

	DcMotor motor0;

	@Override
	public void init() {
		motor0 = hardwareMap.dcMotor.get("motor0");
		motor0.setDirection(DcMotorSimple.Direction.FORWARD);
		motor0.setPower(0);
	}

	@Override
	public void loop() {
		if(gamepad1.a){
			motor0.setPower(0.75);
		}
		else if(gamepad1.b){
			motor0.setPower(-0.75);
		}
		else{
			motor0.setPower(0);
		}
	}
}
