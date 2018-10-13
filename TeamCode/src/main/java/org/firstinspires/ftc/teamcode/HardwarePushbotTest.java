package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
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
	public CRServo threader = null;
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

/*		threader = hwMap.get(CRServo.class, "threader");
		threader.setPower(0);

		door = hwMap.get(Servo.class, "door");
		door.setPosition(0.5);*/

		motor0 = hwMap.get(DcMotor.class, "motor0");
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
		motor3.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
	}
	public void rotate(double power){
		motor0.setPower(power);
		motor1.setPower(power);
		motor2.setPower(power);
		motor3.setPower(power);
	}
	public void moveLeftRight(double power){
		motor0.setPower(power);
		motor1.setPower(power);
		motor2.setPower(-power);
		motor3.setPower(-power);
	}
	public void moveUpDown(double power){
		motor0.setPower(-power);
		motor1.setPower(power);
		motor2.setPower(-power);
		motor3.setPower(power);
	}
	public void moveAll(double power, double theta){
		motor0.setPower(power);
	}
}

