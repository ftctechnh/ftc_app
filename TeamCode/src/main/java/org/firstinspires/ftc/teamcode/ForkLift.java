package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.DigitalChannel;

public class ForkLift {
	private Servo rightClaw;
	private Servo leftClaw;
	private DcMotor drawerSlide;
	private DigitalChannel topButton;
	private DigitalChannel bottomButton;
	private double clawPosition = 0.0;
    private double clawHighEnd = 0.45;
    private double clawLowEnd = 0.1;
    private double up = 0;
    private double down = 0;

	public ForkLift(Servo rightClaw, Servo leftClaw, DcMotor drawerSlide, DigitalChannel topButton, DigitalChannel bottomButton) {
		this.rightClaw = rightClaw;
		this.leftClaw = leftClaw;
		this.drawerSlide = drawerSlide;
		this.topButton = topButton;
		this.bottomButton = bottomButton;
		this.rightClaw.setDirection(Servo.Direction.REVERSE);
	}
	
	
	public void initClaw() {
		setClawPosition(clawPosition);
	}

	public void closeClaw() {
		setClawPosition(clawHighEnd);
	}

	public void openClaw() {
		setClawPosition(clawLowEnd);
	}

	public void moveUpDown(double speed) {
		if (!(topButton.getState()) && speed > 0) {
			drawerSlide.setPower(speed);
		}
		else if (!(bottomButton.getState()) && speed < 0) {
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