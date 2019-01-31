/**
 * Created by eduardo on 1/24/18.
 */
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "SOLJABOY TOLD YA", group = "Linear OpMode")
public class JahsehDwayneRicardoOnfroy extends LinearOpMode
{

    private DcMotor leftWheel;
    private DcMotor rightWheel;
    private Servo leftClaw;
    private Servo rightClaw;
    private DcMotor claw;

    @Override
    public void runOpMode() throws InterruptedException
    {
        rightWheel = hardwareMap.get(DcMotor.class, "right_drive");
        leftWheel = hardwareMap.get(DcMotor.class, "left_drive");
        claw = hardwareMap.get(DcMotor.class, "claw");
        rightClaw = hardwareMap.get(Servo.class, "right_claw");
        leftClaw = hardwareMap.get(Servo.class, "left_claw");

        leftWheel.setDirection(DcMotor.Direction.FORWARD);
        rightWheel.setDirection(DcMotor.Direction.REVERSE);
        claw.setDirection(DcMotor.Direction.FORWARD);

        leftClaw.setDirection(Servo.Direction.FORWARD);
        rightClaw.setDirection(Servo.Direction.REVERSE);

        waitForStart();

        while (opModeIsActive())
        {
            double leftPower;
            double rightPower;
            double clawMovement;
            boolean openClaw;
            boolean closeClaw;

            leftPower = -gamepad1.right_trigger;
            rightPower = gamepad1.left_trigger;
            openClaw = gamepad1.a;
            closeClaw = gamepad1.b;
            clawMovement = gamepad1.left_stick_y;

            leftPower = Range.clip(leftPower, -1.0, 1.0);
            rightPower = Range.clip(rightPower, -1.0, 1.0);
            clawMovement = Range.clip(clawMovement, -1.0, 1.0);

            // Send calculated power to wheels
            leftWheel.setPower(leftPower);
            rightWheel.setPower(rightPower);
            claw.setPower(clawMovement);
            if (gamepad1.right_bumper)
            {
                leftPower *= -1;
                rightPower *= -1;
            }
            if (openClaw)
            {
                leftClaw.setPosition(1.0);
                rightClaw.setPosition(1.0);
            }
            if (closeClaw)
            {
                leftClaw.setPosition(0.0);
                rightClaw.setPosition(0.0);
            }
        }
    }
}
