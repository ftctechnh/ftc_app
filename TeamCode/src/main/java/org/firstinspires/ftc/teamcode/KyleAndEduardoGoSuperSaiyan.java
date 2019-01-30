/**
 * Created by eduardo on 1/24/18.
 */
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "SOLJABOY TOLD YA", group = "xd")
public class KyleAndEduardoGoSuperSaiyan extends LinearOpMode {

    private DcMotor leftWheel;
    private DcMotor rightWheel;
    private Servo leftClaw;
    private Servo rightClaw;

    @Override
    public void runOpMode() throws InterruptedException
    {
        rightWheel = hardwareMap.dcMotor.get("rightMotor");
        leftWheel = hardwareMap.dcMotor.get("leftMotor");
        rightClaw = hardwareMap.servo.get("rightClaw");
        leftClaw = hardwareMap.servo.get("leftClaw");

        leftWheel.setDirection(DcMotor.Direction.FORWARD);
        rightWheel.setDirection(DcMotor.Direction.REVERSE);

        leftClaw.setDirection(Servo.Direction.FORWARD);
        rightClaw.setDirection(Servo.Direction.REVERSE);

        waitForStart();

        while (opModeIsActive()) {
            double leftPower;
            double rightPower;

            // Choose to drive using either Tank Mode, or POV Mode
            // Comment out the method that's not used.  The default below is POV.

            // POV Mode uses left stick to go forward, and right stick to turn.
            // - This uses basic math to combine motions and is easier to drive straight.
            /*leftPower    = Range.clip(-gamepad1.left_stick_y + gamepad1.right_stick_x, -1.0, 1.0) ;
            rightPower   = Range.clip(-gamepad1.left_stick_y - gamepad1.right_stick_x, -1.0, 1.0) ;*/

            // Tank Mode uses one stick to control each wheel.
            // - This requires no math, but it is hard to drive forward slowly and keep straight.
            leftPower  = -gamepad1.left_stick_y;
            rightPower = -gamepad1.right_stick_y;

            // Send calculated power to wheels
            leftWheel.setPower(leftPower);
            rightWheel.setPower(rightPower);
        }
    }
}
