package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

public class ForkLift {
	private Servo rightClaw;
	private Servo leftClaw;
	private DcMotor drawerSlide;
	private TouchSensor topButton;
	private TouchSensor bottomButton;
	private double clawPosition = 0.25;
    private double clawHighEnd = 0.7;
    private double clawLowEnd = 0.35;
    private double up = 0;
    private double down = 0;

	public ForkLift(Servo rightClaw, Servo leftClaw, DcMotor drawerSlide, TouchSensor topButton, TouchSensor bottomButton) {
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
		if (speed < 0) {
			if (bottomButton.isPressed()) {
				speed = 0;
			}
		}
		if (speed > 0) {
			if (topButton.isPressed()) {
				speed = 0;
			}
		}
		drawerSlide.setPower(speed);
	}
	public void setClawPosition(double position) {
		rightClaw.setPosition(position);
		rightClaw.setPosition(position);
		leftClaw.setPosition(position);
		leftClaw.setPosition(position);
	}


}