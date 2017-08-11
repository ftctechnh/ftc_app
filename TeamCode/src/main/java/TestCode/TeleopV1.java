package TestCode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by jakemueller on 8/10/17.
 */
@TeleOp(name = "teleOp One", group = "TestCode")
public class TeleopV1 extends LinearOpMode
{
    private DcMotor motorLeft;
    private DcMotor motorRight;


    public void runOpMode() throws InterruptedException
    {
        motorLeft = hardwareMap.dcMotor.get("LeftDrive");
        motorRight = hardwareMap.dcMotor.get("RightDrive");

        motorLeft.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();

        while(opModeIsActive())
        {
            motorLeft.setPower(-gamepad1.left_stick_y);
            motorRight.setPower(-gamepad1.right_stick_y);





            idle();

        }
    }
}
