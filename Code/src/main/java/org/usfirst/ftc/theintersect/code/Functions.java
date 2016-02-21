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
	public static double colorError = 2.0;
    public static double backWheelCircumfrence = 4 * Math.PI;
    public static double mountainClimberInitPosition = 0;
    public static double mountainClimberReleaseOpen = 0.0;
	public static double mountainClimberReleaseClose = 1.0;
	public static double bumperInitPosition = 0.5;
	public static double tubeExtenderInitPosition = 0.5;
    public static double tubeTiltInitPosition = 0.5;
    public static double churroHookDownPos = 0.1;
    public static double churroHookUpPos = 0.9;
	public static double straightGyroCorrectionFactor = 20.0;
	public static double adjustedPowerMax = 0.9;
	public static double adjustedPowerMin = 0.25;
	public static double whiteThreshold = 10;
    public static double barHookDownPos = 0.1;
    public static double barHookUpPos = 0.8;
    public static double barHookTeleInitPos = 0.5;
    public static double barHookAutoInitPos = 1.0;
    public static double linearSlidePower = 0.5;
    public static double triggerThreshold = 0.2;
    public static double sweeperPower = 0.65;
    public static double bumperDownPos = 1.0;
    public static double bumperUpPos = 0.0;
    public static double hangStringPower = 0.5;


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