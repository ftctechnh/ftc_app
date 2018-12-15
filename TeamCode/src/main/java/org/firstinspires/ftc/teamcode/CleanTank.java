package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.RoboticsUtils.PID;
//import org.firstinspires.ftc.robotcontroller.external.samples.HardwareK9bot;
/**
 * Created by emilyhinds on 12/14/18.
 */
@TeleOp(name="CleanTank", group="CleanTank")
public class CleanTank extends LinearOpMode {

    // Declare OpMode members.
    private DcMotor Left;
    private DcMotor Right;
    private DcMotor Shoulder;
    private DcMotor Elbow;

    @Override
    public void runOpMode() {
        Left = hardwareMap.get(DcMotor.class, "left");
        Right = hardwareMap.get(DcMotor.class, "right");
        Shoulder = hardwareMap.get(DcMotor.class, "shoulder");
        Elbow = hardwareMap.get(DcMotor.class, "elbow");
        waitForStart();

        while (opModeIsActive()) {
            Left.setPower(Range.clip(gamepad1.left_stick_y, -1, 1));
            Right.setPower(Range.clip(-gamepad1.right_stick_y, -1, 1));
            Shoulder.setPower(.5 * Range.clip(gamepad2.left_stick_y, -1, 1));
            Elbow.setPower(.5 * Range.clip(-gamepad2.right_stick_y, -1, 1));

        }
    }


    }

