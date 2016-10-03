package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="Tank Drive")
public class TankDrive extends PakbotsBaseTeleOpMode {

    public void init() {
        motorInit();
    }

    public void loop() {
        //motor
        double ly = -gamepad1.left_stick_y;
        double ry = -gamepad1.right_stick_y;

        mFrontLeft.setPower(ly);
        mRearLeft.setPower(ly);
        mFrontRight.setPower(ry);
        mRearRight.setPower(ry);

    }
}
