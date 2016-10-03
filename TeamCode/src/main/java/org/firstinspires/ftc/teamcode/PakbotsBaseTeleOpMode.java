package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public abstract class PakbotsBaseTeleOpMode extends OpMode {
    DcMotor mFrontLeft;
    DcMotor mFrontRight;
    DcMotor mRearLeft;
    DcMotor mRearRight;

    void motorInit() {
        mFrontLeft = hardwareMap.dcMotor.get("fl");
        mFrontRight = hardwareMap.dcMotor.get("fr");
        mRearLeft = hardwareMap.dcMotor.get("rl");
        mRearRight = hardwareMap.dcMotor.get("rr");

        mFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        mRearLeft.setDirection(DcMotorSimple.Direction.REVERSE);
    }

}
