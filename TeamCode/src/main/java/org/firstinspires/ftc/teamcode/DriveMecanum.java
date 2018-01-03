package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class DriveMecanum {
    private DcMotor FrontLeft;
    private DcMotor FrontRight;
    private DcMotor RearLeft;
    private DcMotor RearRight;
    private double speed = 1;
    private Telemetry telemetry;

    public DriveMecanum(DcMotor FrontLeft, DcMotor FrontRight, DcMotor RearLeft, DcMotor RearRight, double speed, Telemetry telemetry) {
        this.FrontLeft = FrontLeft;
        this.FrontRight = FrontRight;
        this.FrontRight.setDirection(DcMotor.Direction.REVERSE);
        this.RearLeft = RearLeft;
        this.RearRight = RearRight;
        this.RearRight.setDirection(DcMotor.Direction.REVERSE);
        this.speed = speed;
        this.telemetry = telemetry;
        this.FrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.FrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.RearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.RearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
    public void driveTranslateRotate(double x, double y, double z) {
        //Trust me, if you edit this next line you don't know what you're doing. Sorry but it's true. If you feel the need to edit this, please talk to me (Kaden) so I explain to you why it needs to not be edited. Thanks.
        //for positive values: x - strafe right, y - backward, z - spin right
        //for negative values: x - strafe left, y - forward, z - spin left
        driveSpeeds(
                y + -x - z,
                y + x + z,
                y + x - z,
                y + -x + z);
    }
    public void driveTranslateRotate(double x, double y, double z, long miliseconds) {
        driveTranslateRotate(x,y,z);
        sleep(miliseconds);
        stop();

    }
    public void driveLeftRight(double xLeft, double xRight, double yLeft, double yRight) {
        //Trust me, if you edit this next line you don't know what you're doing. Sorry but it's true. If you feel the need to edit this, please talk to me (Kaden) so I explain to you why it needs to not be edited. Thanks.
        //This one is kinda complicated for using in an autonomous program or any linear set of commands. I'd recommend using driveTranslateRotate for things other than driving. However I will explain this one anyway.
        //There are two forces for each side (left/right) of the robot. y controls forward/backward, x controls side-to-side (strafing). For instance if both ys are positive it will move forward. If both xs are positive it will move right.
        driveSpeeds(yLeft - xLeft, yRight + xRight, yLeft + xLeft, yRight - xRight);
    }

    public void driveLeftRight(double xLeft, double xRight, double yLeft, double yRight, long miliseconds) {
        driveLeftRight(xLeft, xRight, yLeft, yRight);
        sleep(miliseconds);
        stop();
    }
    public void swingRight() {
        driveSpeeds(0, 0, -1, 1);
    }

    public void swingLeft() {
        driveSpeeds(0, 0, 1, -1);
    }

    public void driveSpeeds(double flSpeed, double frSpeed, double rlSpeed, double rrSpeed) {
        FrontLeft.setPower(speed * clip(flSpeed));
        FrontRight.setPower(speed * clip(frSpeed));
        RearLeft.setPower(speed * clip(rlSpeed));
        RearRight.setPower(speed * clip(rrSpeed));
    }
    public void stop() {
        driveSpeeds(0,0,0,0);
    }
    public double clip(double value) {
        return Range.clip(value, -1, 1);
    }
    public void sleep(long miliseconds) {try {Thread.sleep(miliseconds);} catch (InterruptedException e) {}}
}
