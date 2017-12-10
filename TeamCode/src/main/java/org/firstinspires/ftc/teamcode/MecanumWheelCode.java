
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;


@TeleOp(name="Things", group="PushbotPotato")
public class MecanumWheelCode extends LinearOpMode {

    HardwarePushbot robot = new HardwarePushbot();

    double FORWARDNESS_MULTIPLIER   = 0.6;
    double STRAFENESS_MULTIPLIER   = 0.6;
    double TURNYNESS_MULTIPLIER     = 0.8;
    double ARMYNESS_MULTIPLIER      = 0.6;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        waitForStart();
        while (opModeIsActive()){

            double FORWARDNESS = gamepad1.left_stick_y * FORWARDNESS_MULTIPLIER;
            double STRAFENESS  = gamepad1.left_stick_x * STRAFENESS_MULTIPLIER;
            double TURNYNESS   = gamepad1.right_stick_x * TURNYNESS_MULTIPLIER;
            /* The following use of the joystick to control the arm is entirely experimental. */
            double ARMYNESS    = gamepad1.right_stick_y * ARMYNESS_MULTIPLIER;

            robot.leftRearDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot.rightRearDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot.leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot.rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot.armDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            robot.leftRearDrive.setPower((FORWARDNESS + STRAFENESS + TURNYNESS) / 3);
            robot.rightRearDrive.setPower((FORWARDNESS + STRAFENESS - TURNYNESS) / 3);
            robot.leftDrive.setPower((FORWARDNESS - STRAFENESS + TURNYNESS) / 3);
            robot.rightDrive.setPower((FORWARDNESS - STRAFENESS - TURNYNESS) / 3);
            robot.armDrive.setPower(ARMYNESS);

            sleep(25);
        }
    }
}