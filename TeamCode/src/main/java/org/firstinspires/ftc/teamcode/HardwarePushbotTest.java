package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.sun.tools.javac.tree.DCTree;

import static org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot.MID_SERVO;

/**
 * Created by Rohan Mathur on 9/15/18.
 */
public class HardwarePushbotTest
{
	/* Public OpMode members. */
	public Servo threader = null;
	public Servo door =		null;

	public DcMotor motor0 =	null;
	public DcMotor motor1 = null;
	public DcMotor motor2 = null;
	public DcMotor motor3 = null;

	/* local OpMode members. */
	HardwareMap hwMap           =  null;

	/* Constructor */
	public HardwarePushbotTest(){

	}

	/* Initialize standard Hardware interfaces */
	public void init(HardwareMap ahwMap) {
		// Save reference to Hardware map
		hwMap = ahwMap;

		threader = hwMap.get(Servo.class, "threader");
		threader.setPosition(0.5);

		door = hwMap.get(Servo.class, "door");
		threader.setPosition(0.0);

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

