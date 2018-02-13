package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by anshnanda on 30/12/17.
 */

@TeleOp(name = "Teaching", group = "agroup")
@Disabled

public class DRIVINGtestTeleOp extends LinearOpMode
{
    private DcMotor motorFrontLeft;
    private DcMotor motorBackLeft;
    private DcMotor motorFrontRight;
    private DcMotor motorBackRight;


    @Override
    public void runOpMode() throws InterruptedException
    {
        motorFrontLeft = hardwareMap.dcMotor.get("MC1M1");
        motorBackLeft = hardwareMap.dcMotor.get("MC1M2");
        motorFrontRight = hardwareMap.dcMotor.get("MC2M1");
        motorBackRight = hardwareMap.dcMotor.get("MC2M2");

        motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        motorBackLeft.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();

        while(opModeIsActive()){

            if (gamepad1.dpad_up && !gamepad1.dpad_down && !gamepad1.dpad_left && !gamepad1.dpad_right){
                motorFrontLeft.setPower(gamepad1.right_trigger);
                motorBackLeft.setPower(gamepad1.right_trigger);
                motorFrontRight.setPower(gamepad1.right_trigger);
                motorBackRight.setPower(gamepad1.right_trigger);
            }

            if (!gamepad1.dpad_up && gamepad1.dpad_down && !gamepad1.dpad_left && !gamepad1.dpad_right){
                motorFrontLeft.setPower(-gamepad1.right_trigger);
                motorBackLeft.setPower(-gamepad1.right_trigger);
                motorFrontRight.setPower(-gamepad1.right_trigger);
                motorBackRight.setPower(-gamepad1.right_trigger);
            }

            if (!gamepad1.dpad_up && !gamepad1.dpad_down && !gamepad1.dpad_left && !gamepad1.dpad_right){
                motorFrontLeft.setPower(0);
                motorBackLeft.setPower(0);
                motorFrontRight.setPower(0);
                motorBackRight.setPower(0);
            }

            idle();
        }
    }
}
