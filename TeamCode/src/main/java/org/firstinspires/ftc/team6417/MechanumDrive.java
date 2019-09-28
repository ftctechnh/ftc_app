package org.firstinspires.ftc.team6417;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class MecanumDrive {
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor backLeft;

    void init(HardwareMap hwMap) {
        frontLeft = hwMap.get(DcMotor.class, "LeftFrontDrive");
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight = hwMap.get(DcMotor.class, "RightFrontDrive");
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft = hwMap.get(DcMotor.class, "LeftBackDrive");
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight = hwMap.get(DcMotor.class, "RightBackDrive");
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //backLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
    }

    void setSpeeds(double flSpeed, double frSpeed, double blSpeed, double brSpeed) {
        double largest = 1.0;
        largest = Math.max(largest, Math.abs(flSpeed));
        largest = Math.max(largest, Math.abs(frSpeed));
        largest = Math.max(largest, Math.abs(blSpeed));
        largest = Math.max(largest, Math.abs(brSpeed));

        frontLeft.setPower(flSpeed / largest);
        frontRight.setPower(frSpeed / largest);
        backLeft.setPower(blSpeed / largest);
        backRight.setPower(brSpeed / largest);
    }
    void driveMecanum(double forward, double strafe, double rotate) {
        double frontLeftSpeed = forward + strafe + rotate;
        double frontRightSpeed = forward - strafe - rotate;
        double backLeftSpeed = forward - strafe + rotate;
        double backRightSpeed = forward + strafe - rotate;

        setSpeeds(frontLeftSpeed, frontRightSpeed, backLeftSpeed, backRightSpeed);
    }

    void reportEncoders(Telemetry telemetry) {
        telemetry.addData("Encoders", "%d %d %d %d",
                frontLeft.getCurrentPosition(),
                frontRight.getCurrentPosition(),
                backLeft.getCurrentPosition(),
                backRight.getCurrentPosition());
    }
}