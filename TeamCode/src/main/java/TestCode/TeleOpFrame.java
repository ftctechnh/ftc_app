package TestCode;

/**
 * Created by Jake Mueller on 8/10/2017.
 */

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class TeleOpFrame extends LinearOpMode {

    private DcMotor motorLeft;  //States what motors are called
    private DcMotor motorRight;

    public void runOpMode() throws InterruptedException
    {
        motorLeft = hardwareMap.dcMotor.get("leftDrive");  //tells software what the hardware calls the motors
        motorRight = hardwareMap.dcMotor.get("rightDrive");  // if add more motors insert here

        motorLeft.setDirection(DcMotor.Direction.REVERSE);  // sets the left drive motor to drive in reverse so the rbt drives forwards

        waitForStart();

        while(opModeIsActive())
        {
            // Put anything you want to happen in OpMode here. make sure to copy and paste to a new code.
        }
    }
}