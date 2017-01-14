package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Noah Pietrafesa on 12/3/16.
 */
@TeleOp

public class NoahLukeOpMode extends OpMode {

    DcMotor left1;
    DcMotor left2;
    DcMotor right1;
    DcMotor right2;
//FOR GREEN MECANUM WHEELS
    @Override
    public void init() {
        right2 = hardwareMap.dcMotor.get("right2");
        left2 = hardwareMap.dcMotor.get("left2");
        right1 = hardwareMap.dcMotor.get("right1");
        left1 = hardwareMap.dcMotor.get("left1");

        left1.setDirection(DcMotor.Direction.REVERSE);
        left2.setDirection(DcMotor.Direction.REVERSE);
    }

    //Noah was here
    //Now Luke is here
    //Noah is here once more

    public float linInterp(float fac, float a, float b)
    {
        return a + (b - a) * fac;
    }

    @Override
    public void loop() {

        float frontPower = linInterp(Math.abs(gamepad1.left_stick_y), -1.0f * gamepad1.left_stick_x,
                gamepad1.left_stick_y);

        float backPower = linInterp(Math.abs(gamepad1.left_stick_y), gamepad1.left_stick_x,
                gamepad1.left_stick_y);

        left2.setPower(frontPower);
        right2.setPower(frontPower);

        left1.setPower(backPower);
        right1.setPower(backPower);

        telemetry.addData("front power", frontPower);
        telemetry.addData("back power", backPower);
        telemetry.addData("left stick y", gamepad1.left_stick_y);
        telemetry.addData("left stick x", gamepad1.left_stick_x);
        telemetry.addData("left trigger", gamepad1.left_trigger);
        telemetry.addData("right trigger", gamepad1.right_trigger);
        telemetry.addData("turning power", gamepad1.left_trigger - gamepad1.right_trigger);


        if (gamepad1.right_bumper) {
            //horizontal
            left2.setPower(-1.0f * gamepad1.left_stick_x);
            left1.setPower(gamepad1.left_stick_x);
            right2.setPower(-1.0f * gamepad1.left_stick_x);
            right1.setPower(gamepad1.left_stick_x);

        }
        //turn
        else if (gamepad1.left_bumper) {
            left2.setPower(gamepad1.left_trigger - gamepad1.right_trigger);
            left1.setPower(gamepad1.left_trigger - gamepad1.right_trigger);
            right2.setPower(gamepad1.left_trigger - gamepad1.right_trigger);
            right1.setPower(gamepad1.left_trigger - gamepad1.right_trigger);

        } else {
            //vertical
            //mFrontLeft.setPower(gamepad1.right_stick_y);
            //mBackLeft.setPower(gamepad1.right_stick_y);
            //mFrontRight.setPower(-1 * (gamepad1.right_stick_y));
            //mBackRight.setPower(-1 * (gamepad1.right_stick_y));
        }
        //Diagonals
//       else if(gamepad1.left_trigger ==0) {
//            left2.setPower(gamepad1.left_stick_y);
//            right1.setPower(-1 * (gamepad1.left_stick_y));
//        }
//        else {
//            left1.setPower(gamepad1.left_stick_x);
//            right2.setPower(-1 * (gamepad1.left_stick_x));
//        }

        //non mecanum wheels config
//        left1.setPower(gamepad1.left_stick_y);
//        left2.setPower(gamepad1.left_stick_y);
//        right2.setPower(-1*(gamepad1.right_stick_y));
//        right1.setPower(-1*(gamepad1.right_stick_y));
    }
}
