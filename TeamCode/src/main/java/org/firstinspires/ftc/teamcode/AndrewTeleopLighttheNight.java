package org.firstinspires.ftc.teamcode;

/**
 * Created by albusdumbledore on 8/31/17.
 */

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "Obstacle Course Andrew LTN Drive")
//declare variables
public class AndrewTeleopLighttheNight extends LinearOpMode{
    private DcMotor leftMotor;
    private DcMotor rightMotor;
    private DcMotor leftBackMotor;
    private DcMotor rightBackMotor;

//change
    //something

    private int thing = 2;

    @Override
    public void runOpMode() throws InterruptedException {
        hardwareMap.dcMotor.get("LeftFront");
        hardwareMap.dcMotor.get("RightFront");
        hardwareMap.dcMotor.get("LeftBack");
        hardwareMap.dcMotor.get("RightBack");

        rightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBackMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        while(opModeIsActive()) {
            rightMotor.setPower(gamepad2.right_stick_y);
            rightBackMotor.setPower(gamepad2.right_stick_y);
            leftMotor.setPower(gamepad1.left_stick_y);
            leftBackMotor.setPower(gamepad1.left_stick_y);

        }
        idle();
    }

}
