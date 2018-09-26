package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import static org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot.MID_SERVO;

/**
 * Created by Rohan Mathur on 9/15/18.
 */
public class HardwarePushbotTest
{
	/* Public OpMode members. */
	public Servo threader = null;

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


	}
}

