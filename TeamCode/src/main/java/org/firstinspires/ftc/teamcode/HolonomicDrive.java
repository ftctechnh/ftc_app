package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="Holonomic")
public class HolonomicDrive extends PakbotsBaseTeleOpMode {

    public void init() {
        motorInit();
    }

    public void loop() {
        //motor
        double ty = -gamepad1.left_stick_y;
        double tx = gamepad1.left_stick_x;
        double rx = gamepad1.right_stick_x;
        mecanumDrive(tx, ty, rx);
        /*
        double ly = -gamepad1.left_stick_y;
        double lx = gamepad1.left_stick_x;
        double rx = -gamepad1.right_stick_x;
        double ry = gamepad1.right_stick_y;
        mFrontLeft.setPower(ly);
        mFrontRight.setPower(ry);
        mRearLeft.setPower(lx);
        mRearRight.setPower(rx);
        */
    }

    private void mecanumDrive(double x,double y,double r){
        double flValue=y+r+x;
        double rlValue=y+r-x;
        double frValue=y-r-x;
        double rrValue=-r+x+y;

        double maxValue = Math.max(
                Math.max( Math.abs(flValue), Math.abs(rlValue) ),
                Math.max( Math.abs(frValue), Math.abs(rrValue) )
        );
        if(maxValue > 1.0){
            flValue /= maxValue;
            rlValue /= maxValue;
            frValue /= maxValue;
            rrValue /= maxValue;
        }

        mFrontLeft.setPower(flValue);
        mRearLeft.setPower(rlValue);
        mFrontRight.setPower(frValue);
        mRearRight.setPower(rrValue);

    }

}
