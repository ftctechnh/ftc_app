package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

public class DriveMecanum {
    private DcMotor FrontLeft;
    private DcMotor FrontRight;
    private DcMotor RearLeft;
    private DcMotor RearRight;
    private double speed = 1;

    public DriveMecanum(DcMotor FrontLeft, DcMotor FrontRight, DcMotor RearLeft, DcMotor RearRight, double speed) {
        this.FrontLeft = FrontLeft;
        this.FrontRight = FrontRight;
        this.FrontRight.setDirection(DcMotor.Direction.REVERSE);
        this.RearLeft = RearLeft;
        this.RearRight = RearRight;
        this.RearRight.setDirection(DcMotor.Direction.REVERSE);
        this.speed = speed;
    }
    public void driveTranslateRotate(double x, double y, double z) {
        //Trust me, if you edit this next line you don't know what you're doing. Sorry but it's true. If you feel the need to edit this, please talk to me (Kaden) so I explain to you why it needs to not be edited. Thanks.
        //x = forward, y = side, z = spin
        driveSpeeds(
                -y + x - z,
                -y + x + z,
                y + x - z,
                y + x + z);
    }
    public void driveLeftRight(double xLeft, double xRight, double yLeft, double yRight) {
        //Trust me, if you edit this next line you don't know what you're doing. Sorry but it's true. If you feel the need to edit this, please talk to me (Kaden) so I explain to you why it needs to not be edited. Thanks.
        driveSpeeds(xLeft - yLeft, xRight - yRight, yLeft + xLeft, xRight + yRight);
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
    public double clip(double value) {
        return Range.clip(value, -1, 1);
    }
}
