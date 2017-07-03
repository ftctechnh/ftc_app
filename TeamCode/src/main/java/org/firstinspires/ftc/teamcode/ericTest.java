package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by Eric D'Urso on 7/3/2017.
 */
@TeleOp (name = "Eric's Test Of Awesomeness")
public class ericTest extends LinearOpMode
{
    private DcMotor motorLeft;
    private DcMotor motorRight;

    @Override
    public void runOpMode () throws InterruptedException
    {
        motorLeft = hardwareMap.dcMotor.get ("Left Motor");
        motorRight = hardwareMap.dcMotor.get ("Right Motor");

        motorLeft.setDirection(DcMotor.Direction.REVERSE);

        waitForStart ();

        while (opModeIsActive())
        {

            motorLeft.setPower(-gamepad1.left_stick_y);
            motorRight.setPower(-gamepad1.right_stick_y);

            idle();
        }
    }
}
