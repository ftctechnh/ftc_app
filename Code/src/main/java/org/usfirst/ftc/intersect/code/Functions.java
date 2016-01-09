package org.usfirst.ftc.intersect.code;

import com.qualcomm.robotcore.hardware.DcMotor;

public class Functions {
	public static Double convertGamepad(float y) {
		int m;

		if(y < 0) {
			m = 1;
		} else {
			m = -1;
		}
		return m * (1 - Math.sqrt(1 - (y * y)));
	}

	public static void moveTwoMotors(DcMotor motor1, DcMotor motor2,
			Double power) {
		motor1.setPower(power);
		motor2.setPower(power);
	}
}
