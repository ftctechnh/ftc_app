package org.usfirst.ftc.theintersect.code;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.robocol.Telemetry;

public class FunctionsOld {
	//Conversion factors for AndyMarks motors and Tetrix motors
	public static int DCdegreeEncoderRatio = 1120 / 360;
	public static int TetrixDegreeEncoderRatio = 1200 / 360;
	public static int TetrixRotationEncoderRatio = 1440;
	public static int gyroError = 50;

	//Moves a motor at a certain speed
	public static void moveSpeed(DcMotor motor, double power) {
		motor.setPower(power);
	}

	//Moves a Tetrix motor for a certain amount of rotations

	//Moves an AndyMarks motor for a certain amount of rotations
	public static void moveRotationsDC(DcMotor motor, int rotations,
			double power, Telemetry telemetry) {
		moveDegreesDC(motor, rotations * 360, power, telemetry);
	}

	//Moves a motor for a certain period of time in milliseconds
	public static void moveTime(DcMotor motor, double Power, int mill) {
		long endTime = System.currentTimeMillis() + mill;
		while(System.currentTimeMillis() < endTime) {
			motor.setPower(Power);
		}
		motor.setPower(0);
	}

	//Precisely moves a Textris motor for a certain amount of degrees
	//Slower than less precise counterpart
	//Max power best results is 0.5
	public static void PreciseMoveDegreesTetrix(DcMotor motor, int deg,
			double power, Telemetry telemetry) {
		motor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
		motor.setTargetPosition(deg * TetrixDegreeEncoderRatio);
		motor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
		motor.setPower(power);
		telemetry.addData("Current Position", motor.getCurrentPosition());
	}

	//Precisely moves an AndyMarks motor for a certain amount of degrees
	//Slower than less precise counterpart
	//Max power with best results is 0.5
	public static void PreciseMoveDegreesDC(DcMotor motor, int deg,
			double power, Telemetry telemetry) {
		motor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
		motor.setTargetPosition(deg * DCdegreeEncoderRatio);
		motor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
		motor.setPower(power);
		telemetry.addData("Current Position", motor.getCurrentPosition());
	}

	//Moves a Tetrix motor for a certain amount of degrees
	//Margin of error is 0.17
	public static void moveDegreesTetrix(DcMotor motor, int deg, double power,
			DcMotorController controller, Telemetry telemetry) {
		motor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
		motor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
		deg *= TetrixDegreeEncoderRatio;
		if(power < 0) {
			deg = -Math.abs(deg);
			controller.setMotorControllerDeviceMode(
					DcMotorController.DeviceMode.WRITE_ONLY);
			waitFor(100);
			motor.setPower(power);
			controller.setMotorControllerDeviceMode(
					DcMotorController.DeviceMode.READ_ONLY);
			waitFor(100);
			while(motor.getCurrentPosition() > deg) {
				telemetry.addData("Current Position",
						motor.getCurrentPosition());
			}
			controller.setMotorControllerDeviceMode(
					DcMotorController.DeviceMode.WRITE_ONLY);
			waitFor(100);
			motor.setPower(0);
		} else {
			deg = Math.abs(deg);
			controller.setMotorControllerDeviceMode(
					DcMotorController.DeviceMode.WRITE_ONLY);
			waitFor(100);
			motor.setPower(power);
			controller.setMotorControllerDeviceMode(
					DcMotorController.DeviceMode.READ_ONLY);
			waitFor(100);
			while(motor.getCurrentPosition() < deg) {
				telemetry.addData("Current Position",
						motor.getCurrentPosition());
			}
			controller.setMotorControllerDeviceMode(
					DcMotorController.DeviceMode.WRITE_ONLY);
			waitFor(100);
			motor.setPower(0);
		}
	}

	//Moves the robot(all four motors) a certain amount of rotations
	/*public static void moveRobotRotations (Double rotations, double power, DcMotorController controller1, DcMotorController controller2, DcMotorController controller3, Telemetry telemetry) {
		moveDegreesTetrix(Autonomous.rFmotor, (int) (rotations * 360), -power,
				controller3, telemetry);
        moveDegreesTetrix(Autonomous.lFmotor, (int) (rotations * 360), power,
				controller3, telemetry);
        moveDegreesTetrix(Autonomous.rBmotor, (int) (rotations * 360), -power, controller1, telemetry);
        moveDegreesTetrix(Autonomous.lBmotor, (int) (rotations * 360), power,
				controller2, telemetry);

	}*/

	//Moves an AndyMarks motor a certain amount of degrees
	//Margin of error is 0.17
	public static void moveDegreesDC(DcMotor motor, int deg, double power,
			Telemetry telemetry) {
		motor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
		motor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
		deg *= DCdegreeEncoderRatio;
		if(power < 0) {
			deg = -Math.abs(deg);
			while(motor.getCurrentPosition() > deg) {
				motor.setPower(power);
				telemetry.addData("Current Position",
						motor.getCurrentPosition());
			}
		} else {
			deg = Math.abs(deg);
			while(motor.getCurrentPosition() < deg) {
				motor.setPower(power);
				telemetry.addData("Current Position",
						motor.getCurrentPosition());
			}
		}
		motor.setPower(0);
	}

    /*public static void moveArm(DcMotor arm1, DcMotor arm2, int direction, DcMotorController controller, Telemetry telemetry) {
		moveRotationsTetrix(arm1, 8, 0.5*direction, controller, telemetry);
        moveRotationsTetrix(arm2, 10, -0.5*direction, controller, telemetry);
    }*/

	public static void waitFor(int mill) {
		try {
			Thread.sleep(mill);
		} catch(Exception e) {
			Thread.currentThread().interrupt();
		}
	}

	//Algorithm to convert gamepad value to DCMotor power value
	//Implements intricate slope system to provide high precision control along with high power and speed
	public static double convertGamepad(float y) {
		int m;

		if(y < 0) {
			m = 1;
		} else {
			m = -1;
		}
		return m * (1 - Math.sqrt(1 - (y * y)));
	}
	/*-----------------------------------------------------------------------------------------
    ----------AUTONOMOUS FUNCTIONS ------------------------------------------------------------
    ------------------------------------------------------------------------------------------*/

	//Finds the color from the color sensor transmissions
	public static String getColor(ColorSensor color) {
		if(color.blue() > 1.0) {
			return "Blue";
		} else if(color.blue() <= 1.0 && color.blue() > 0) {
			return "Middle";
		} else if(color.red() >= 1.0) {
			return "Red";
		} else {
			return "None";
		}
	}
	/*public static void moveNinches(double inches, double power,Telemetry telemetry){
		double n = inches*(360/12.56);
        int m = (int)(n);
        moveDegreesTetrix(Autonomous.rBmotor, m, power, Autonomous.controller1, telemetry);
        moveDegreesTetrix(Autonomous.lBmotor, m, power, Autonomous.controller2, telemetry);
	}
    //Moves the robot to get into position near the beacon
    public static void beacon(int sensorOffset, String targetColor, DcMotorController controller1, DcMotorController controller2, DcMotorController controller3, ColorSensor colorSensor, Telemetry telemetry) {
        while(getColor(colorSensor) != targetColor){
            moveNinches(1,0.5,telemetry);
        }
		FunctionsOld.moveNinches(sensorOffset, 0.5, telemetry);
        FunctionsOld.pushButton(Autonomous.buttonPusher, 0.0);
    }*/

	//Turns the robot right with extreme customizability a certain amount of degrees using the MR Gyro Sensor
	/*public static void turnRobotRightDegrees(GyroSensor gyro , int degrees, Double Rpower,Double Lpower, Telemetry telemetry){
		/*while(gyro.getRotation() < degrees){
			moveSpeed(Autonomous.lFmotor, power);
			moveSpeed(Autonomous.rFmotor, -power);
			moveSpeed(Autonomous.lBmotor, power);
			moveSpeed(Autonomous.rBmotor, -power);
		}
        if(!gyro.isCalibrating()) {
            int start = gyro.getHeading();
            int heading = gyro.getHeading();
            while (heading - start < degrees) {
                moveSpeed(Autonomous.lBmotor, Lpower);
                moveSpeed(Autonomous.rBmotor, Rpower);
                telemetry.addData("Gyro Z", gyro.rawZ());
                telemetry.addData("Heading", gyro.getHeading());
                heading = gyro.getHeading();
                if (heading < start) {
                    heading = gyro.getHeading() + 360 ;
                }
            }
            moveSpeed(Autonomous.lBmotor, 0);
            moveSpeed(Autonomous.rBmotor, 0);
        }
	}

	//Turns the robot left with extreme customizability a certain amount of degrees using the MR Gyro Sensor
	public static void turnRobotLeftDegrees(GyroSensor gyro , int degrees, Double Rpower,Double Lpower, Telemetry telemetry){
		/*while(gyro.getRotation() < degrees){
			moveSpeed(Autonomous.lFmotor, -power);
			moveSpeed(Autonomous.rFmotor, power);
			moveSpeed(Autonomous.lBmotor, -power);
			moveSpeed(Autonomous.rBmotor, power);
		}
        if(!gyro.isCalibrating()) {
            int start = gyro.getHeading();
            int heading = gyro.getHeading();
            while (start - heading < degrees) {
                moveSpeed(Autonomous.lBmotor, -Lpower);
                moveSpeed(Autonomous.rBmotor, -Rpower);
                telemetry.addData("Gyro Z", gyro.rawZ());
                telemetry.addData("Heading", gyro.getHeading());
                heading = gyro.getHeading();
                if (heading > start) {
                    heading = gyro.getHeading() - 360;
                }
            }
            moveSpeed(Autonomous.lBmotor, 0);
            moveSpeed(Autonomous.rBmotor, 0);
        }
	}*/

	/*
    public static void pushButton(Servo buttonPusher, Double position) {
		buttonPusher.setPosition(1);
        buttonPusher.setPosition(position);
		waitFor(500);
        buttonPusher.setPosition(1);
    }

	public static void openFlaps(Servo servo){
		servo.setPosition(0);
	}

	public static void closeFlaps(Servo servo){
		servo.setPosition(1);
	}

	public static void dumpClimbers(Servo mountainClimber){
		mountainClimber.setPosition(0.0);
	}*/
}

