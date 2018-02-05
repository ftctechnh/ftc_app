package org.firstinspires.ftc.teamcode;

/**
 * Created by Kaden on 11/21/2017.
 */

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class RelicClaw {
    private Servo claw;
    private Servo arm;
    private DcMotor motor;
    private Telemetry telemetry;
    private DigitalChannel outButton;
    private DigitalChannel inButton;
    private final double CLOSE_POSITION = 0.0;
    private final double OPEN_POSITION = 1.0;
    private final double STORAGE_POSITION = 1.0;
    private final double PICKUP_POSITION = 0.67;
    private final double DRIVING_POSITION = 0.6;
    private final double UP_POSITION = 0.0;

    public RelicClaw(HardwareMap hardwareMap, Telemetry telemetry) {
        this.claw = hardwareMap.servo.get("s1");
        this.arm = hardwareMap.servo.get("s2");
        this.motor = hardwareMap.dcMotor.get("m5");
        this.outButton = hardwareMap.digitalChannel.get("b2");
        this.inButton = hardwareMap.digitalChannel.get("b3");
        this.telemetry = telemetry;
    }

    public void init() {
        down();
        openClaw();
    }

    public void closeClaw() {
        claw.setPosition(CLOSE_POSITION);
    }

    public void openClaw() {
        claw.setPosition(OPEN_POSITION);
    }

    public void up() {
        setArmPosition(UP_POSITION);
    }

    public void down() {
        setArmPosition(STORAGE_POSITION);
    }

    public void driving() {
        setArmPosition(DRIVING_POSITION);
    }

    public void pickup() {
        setArmPosition(PICKUP_POSITION);
        closeClaw();
    }

    public void setArmPosition(double position) {
        arm.setPosition(position);
    }
    public double getArmPosition() {return arm.getPosition();}

    public void moveMotor(double speed) {
        motor.setPower(speed); //ayy if you want those limits, comment this and uncomment the other one
        if (speed < 0 && !inButton.getState()) {
            speed = 0;

        }
        if (speed > 0 && !outButton.getState()) {
            speed = 0;
        }
        //motor.setPower(speed); //this is the other one
    }

    public void moveMotor(double speed, long time) {
        moveMotor(speed);
        sleep(time);
        stop();
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }

    public void stop() {
        moveMotor(0);
    }
}
