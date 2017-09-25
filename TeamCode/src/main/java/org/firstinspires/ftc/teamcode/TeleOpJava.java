package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "Java Demo", group = "TeleOp")
public class TeleOpJava extends RobotController {

    // defining all the motors in one line is good practice in Java
    DcMotor lf, lb, rf, rb;

	@Override
	public void init() {
	    // set up our motors
		lf = hardwareMap.dcMotor.get("lf"); // note: java makes you put semicolons everywhere
		lf.setDirection(DcMotorSimple.Direction.FORWARD);
        lb = hardwareMap.dcMotor.get("lb");
        lb.setDirection(DcMotorSimple.Direction.FORWARD);
        rf = hardwareMap.dcMotor.get("rf");
        rf.setDirection(DcMotorSimple.Direction.FORWARD);
        rb = hardwareMap.dcMotor.get("rb");
        rb.setDirection(DcMotorSimple.Direction.FORWARD);
	}

	@Override
	public void loop() {
	    double l_power = curve_input(gamepad1.left_stick_y);
	    double r_power = curve_input(gamepad1.right_stick_y);
        lf.setPower(l_power);
        lb.setPower(l_power);
        rf.setPower(r_power);
        rb.setPower(r_power);
    }
    // note: this method is a variable of type double - it's really just a number
    public double curve_input(double x) {
	    return Math.pow(x, 1.6);
    }
}
