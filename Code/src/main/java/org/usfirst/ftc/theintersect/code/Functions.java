package org.usfirst.ftc.theintersect.code;

import com.qualcomm.robotcore.hardware.DcMotor;

public class Functions {
	//Important ratios for different types of motors
	public static int neveRestPPR = 1120;
	public static int tetrixPPR = 1440;
	public static int neveRestDegreeRatio = neveRestPPR / 360;
	public static int tetrixDegreeRatio = tetrixPPR / 360;
	public static double neveRestTetrixSpeedRatio = 150 / 160;
	public static double encoderError = 4.0;
	public static double colorError = 4.0;
	public static double backWheelCircumfrence = 3*3*Math.PI;


	public static double convertGamepad(float y) {
		int m;

		if(y < 0) {
			m = 1;
		} else {
			m = -1;
		}
		return m * (1 - Math.sqrt(1 - (y * y)));
	}

	public static void moveTwoMotors(DcMotor motor1, DcMotor motor2,
			double power) {
		motor1.setPower(power);
		motor2.setPower(power);
	}

    public static void waitFor(int mill) {
        try {
            Thread.sleep(mill);
        } catch(Exception e) {
            Thread.currentThread().interrupt();
        }
    }
}