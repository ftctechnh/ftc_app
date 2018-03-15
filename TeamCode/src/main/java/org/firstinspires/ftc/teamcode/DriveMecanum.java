package org.firstinspires.ftc.teamcode;

/**
 * Created by Kaden on 11/22/2017.
 */

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class DriveMecanum {
    private DcMotor FrontLeft;
    private DcMotor FrontRight;
    private DcMotor RearLeft;
    private DcMotor RearRight;
    private double speed = 1;
    private Telemetry telemetry;
    private HardwareMap hardwareMap;
    final double D_PAD_SLOW_SPEED = 0.25;
    final double BUMPER_SLOW_SPEED = 0.25;

    public DriveMecanum(OpMode opMode) {
        this.hardwareMap = opMode.hardwareMap;
        this.FrontLeft = hardwareMap.dcMotor.get("m1");
        this.FrontRight = hardwareMap.dcMotor.get("m2");
        this.FrontRight.setDirection(DcMotor.Direction.REVERSE);
        this.RearLeft = hardwareMap.dcMotor.get("m3");
        this.RearRight = hardwareMap.dcMotor.get("m4");
        this.RearRight.setDirection(DcMotor.Direction.REVERSE);
        this.telemetry = telemetry;
        setBRAKE();
    }

    public void driveTranslateRotate(double x, double y, double z) {
        //for positive values: x - strafe right, y - backward, z - spin right
        //for negative values: x - strafe left, y - forward, z - spin left
        driveSpeeds(
                y + -x - z,
                y + x + z,
                y + x - z,
                y + -x + z);
    }
    public void forward(double speed) {
        driveTranslateRotate(0, -Math.abs(speed), 0);
    }

    public void backward(double speed) {
        driveTranslateRotate(0, Math.abs(speed), 0);
    }

    public void strafeRight(double speed) {
        driveTranslateRotate(Math.abs(speed), 0, 0);
    }

    public void strafeLeft(double speed) {
        driveTranslateRotate(-Math.abs(speed), 0, 0);
    }

    public void spinRight(double speed) {
        driveTranslateRotate(0, 0, Math.abs(speed));
    }

    public void spinLeft(double speed) {
        driveTranslateRotate(0, 0, -Math.abs(speed));

    }
    public void driveTranslateRotate(double x, double y, double z, long miliseconds) {
        driveTranslateRotate(x,y,z);
        sleep(miliseconds);
        stop();

    }

    public void stop() {
        driveSpeeds(0,0,0,0);
    }
    public double clip(double value) {
        return Range.clip(value, -1, 1);
    }
    public void sleep(long miliseconds) {try {Thread.sleep(miliseconds);} catch (InterruptedException e) {}}
    private void setBRAKE() {
        this.FrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.FrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.RearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.RearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
}
