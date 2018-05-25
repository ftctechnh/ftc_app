package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by anshnanda on 30/12/17.
 */

@Disabled
@TeleOp(name = "Teaching", group = "agroup")

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

            float dz = 0.2f;

            gamepad1.setJoystickDeadzone(dz);

            //GamePad 1
            double r = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
            telemetry.addData("r = ", r);
            double robotAngle = Math.atan2(-gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;
            telemetry.addData("robotAngle = ", robotAngle);
            double rightX = gamepad1.right_stick_x;
            //telemetry.addData("rightX = ", rightX);
            final double v1 = r * Math.cos(robotAngle) + rightX;
            //telemetry.addData("front left power = ", v1);
            final double v2 = r * Math.sin(robotAngle) - rightX;
            //telemetry.addData("front right power = ", v2);
            final double v3 = r * Math.sin(robotAngle) + rightX;
            //telemetry.addData("back left power = ", v3);
            final double v4 = r * Math.cos(robotAngle) - rightX;
            //telemetry.addData("back right power = ", v4);

            motorFrontLeft.setPower(v1);
            motorFrontRight.setPower(v2);
            motorBackLeft.setPower(v3);
            motorBackRight.setPower(v4);

//            if (gamepad1.dpad_up && !gamepad1.dpad_down && !gamepad1.dpad_left && !gamepad1.dpad_right) {
//                motorFrontLeft.setPower(gamepad1.right_trigger);
//                motorFrontRight.setPower(gamepad1.right_trigger);
//                motorBackRight.setPower(gamepad1.right_trigger);
//                motorBackLeft.setPower(gamepad1.right_trigger);
//            }
//
//            if (!gamepad1.dpad_up && gamepad1.dpad_down && !gamepad1.dpad_left && !gamepad1.dpad_right) {
//                motorFrontLeft.setPower(-gamepad1.right_trigger);
//                motorFrontRight.setPower(-gamepad1.right_trigger);
//                motorBackRight.setPower(-gamepad1.right_trigger);
//                motorBackLeft.setPower(-gamepad1.right_trigger);
    //            }
    //
//            if (!gamepad1.dpad_up && !gamepad1.dpad_down && gamepad1.dpad_left && !gamepad1.dpad_right) {
//                motorFrontLeft.setPower(-gamepad1.right_trigger);
//                motorFrontRight.setPower(gamepad1.right_trigger);
//                motorBackRight.setPower(gamepad1.right_trigger);
//                motorBackLeft.setPower(-gamepad1.right_trigger);
//            }
//
//            if (!gamepad1.dpad_up && !gamepad1.dpad_down && !gamepad1.dpad_left && gamepad1.dpad_right) {
//                motorFrontLeft.setPower(gamepad1.right_trigger);
//                motorFrontRight.setPower(-gamepad1.right_trigger);
//                motorBackRight.setPower(-gamepad1.right_trigger);
//                motorBackLeft.setPower(gamepad1.right_trigger);
//            }
//

            idle();
        }
    }
}
