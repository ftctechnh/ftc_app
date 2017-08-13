package TestCode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import Library.Chassis_motors;

/**
 * Created by Uz as an example of how to use the Chassis_motors class.
 */
@TeleOp(name = "teleOp with class", group = "TestCode")
public class TeleopV1_with_chassis_motors_class extends LinearOpMode
{

    private Chassis_motors chassis_Object = new Chassis_motors(hardwareMap);

    public void runOpMode() throws InterruptedException
    {

        chassis_Object.set_Direction_Forward();

        waitForStart();

        while(opModeIsActive())
        {

            chassis_Object.run_Motors_no_encoder(-gamepad1.left_stick_y, -gamepad1.right_stick_y);

            idle();

        }
    }
}
