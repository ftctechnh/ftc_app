package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.TouchSensor;

public class RelicClaw {
	private Servo rightClaw;
	private Servo leftClaw;
	private DcMotor drawerSlide;
	private TouchSensor topButton;
	private TouchSensor bottomButton;
	private double clawPosition = 0.0;
    private double clawHighEnd = 0.45;
    private double clawLowEnd = 0.1;
    private double up = 0;
    private double down = 0;

	public RelicClaw(Servo rightClaw, Servo leftClaw, DcMotor drawerSlide, TouchSensor topButton, TouchSensor bottomButton) {
		this.rightClaw = rightClaw;
		this.leftClaw = leftClaw;
		this.drawerSlide = drawerSlide;
		this.topButton = topButton;
		this.bottomButton = bottomButton;
		this.rightClaw.setDirection(Servo.Direction.REVERSE);
	}


	public void initClaw() {setClawPosition(clawPosition);}

	public void closeClaw(){setClawPosition(clawHighEnd);}

	public void openClaw() {setClawPosition(clawLowEnd);}

	public void moveUpDown(double speed) {
		if (!(topButton.isPressed()) && speed > 0) {
			drawerSlide.setPower(speed);
		}
		else if (!(bottomButton.isPressed()) && speed < 0) {
			drawerSlide.setPower(speed);
		}
		else {
			drawerSlide.setPower(0.0);
		}
	}
	public void setClawPosition(double position) {
		rightClaw.setPosition(position);
		rightClaw.setPosition(position);
		leftClaw.setPosition(position);
		leftClaw.setPosition(position);
	}


}