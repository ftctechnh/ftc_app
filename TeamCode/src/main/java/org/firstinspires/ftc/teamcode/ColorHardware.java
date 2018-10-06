package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Rohan Mathur on 9/15/18.
 */
public class ColorHardware
{
	/* Public OpMode members. */

	public DcMotor motor0 =	null;
	public ColorSensor color;

	/* local OpMode members. */
	HardwareMap hwMap =  null;

	/* Constructor */
	public ColorHardware(){

	}

	/* Initialize standard Hardware interfaces */
	public void init(HardwareMap ahwMap) {
		// Save reference to Hardware map
		hwMap = ahwMap;

		color = hwMap.get(ColorSensor.class, "color");

		motor0 = hwMap.get(DcMotor.class, "motor0");
/*		motor0 = hwMap.get(DcMotor.class, "motor0");
		motor0.setDirection(DcMotor.Direction.FORWARD);
		motor0.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

		motor1 = hwMap.get(DcMotor.class, "motor1");
		motor1.setDirection(DcMotor.Direction.FORWARD);
		motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

		motor2 = hwMap.get(DcMotor.class, "motor2");
		motor2.setDirection(DcMotor.Direction.FORWARD);
		motor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

		motor3 = hwMap.get(DcMotor.class, "motor3");
		motor3.setDirection(DcMotor.Direction.FORWARD);
		motor3.setMode(DcMotor.RunMode.RUN_USING_ENCODER);*/
	}
}

