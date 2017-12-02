package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.ArrayList;
import java.util.List;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class DriveEngine {
    DcMotor front;
    DcMotor back;
    DcMotor left;
    DcMotor right;

//    private static final double ticksPerWheelRev = 1120;
//    private static final double wheelRevPerRobotRev = 5.2705; // (2 * pi * 263.525) / (2 * pi * 50)
//    private static final double ticksPerRobotRev = ticksPerWheelRev * wheelRevPerRobotRev;
//    private static final double radiansPerTick = 2 * Math.PI / ticksPerRobotRev * 360.0 / 406.0;

    double offset = Math.PI / 4;
    double theta = offset;

    double alpha = .2;
    double xAve = 0;
    double yAve = 0;

    public DriveEngine(HardwareMap hardwareMap) {
        front = hardwareMap.dcMotor.get("front");
        back = hardwareMap.dcMotor.get("back");
        left = hardwareMap.dcMotor.get("left");
        right = hardwareMap.dcMotor.get("right");

        front.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        back.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        front.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        back.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        front.setDirection(DcMotor.Direction.REVERSE);
        back.setDirection(DcMotor.Direction.FORWARD);
        left.setDirection(DcMotor.Direction.FORWARD);
        right.setDirection(DcMotor.Direction.REVERSE);
    }

    public double smoothX(double x)
    {
        if(x == 0)
            xAve = 0;
        else
            xAve = alpha * x + (1-alpha) * xAve;
        return xAve;
    }

    public double smoothY(double y)
    {
        if(y == 0)
            yAve = 0;
        else
            yAve = alpha * y + (1-alpha) * yAve;
        return yAve;
    }

    public void drive(double x, double y) {

        x = smoothX(x);
        y = smoothY(y);

        double xprime = x * Math.cos(theta) - y * Math.sin(theta);
        double yprime = x * Math.sin(theta) + y * Math.cos(theta);

        front.setPower(xprime);
        back.setPower(xprime);
        left.setPower(yprime);
        right.setPower(yprime);
    }

    public void rotate(double yIn) {
        double x = yIn / 2;
        double y = -1 * yIn / 2;

        front.setPower(x);
        back.setPower(-1*x);
        left.setPower(y);
        right.setPower(-1*y);

    }


}