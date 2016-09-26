package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by ftc6347 on 9/22/16.
 */
@TeleOp(name = "Holonomic Drive", group = "Tests")
public class HolonomicDriveOp extends LinearOpMode {

    private HardwareHolonomic robot = new HardwareHolonomic();

    private float frontLeftPower;
    private float frontRightPower;
    private float backLeftPower;
    private float backRightPower;

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);
        gamepad1.setJoystickDeadzone(0.2f);

        waitForStart();

        while(opModeIsActive()) {

            // Reset variables
            frontLeftPower = 0;
            frontRightPower = 0;
            backLeftPower = 0;
            backRightPower = 0;

            handleStrafe();
            handlePivot();

            if(gamepad1.right_trigger > 0) {
                frontLeftPower /= 4;
                frontRightPower /= 4;
                backLeftPower /= 4;
                backRightPower /= 4;
            }

            // Set the actual motor powers
            robot.getFrontLeft().setPower(frontLeftPower);
            robot.getFrontRight().setPower(frontRightPower);
            robot.getBackLeft().setPower(backLeftPower);
            robot.getBackRight().setPower(backRightPower);

            idle();
        }
    }

    private void handlePivot() {
        frontLeftPower += gamepad1.left_stick_x;
        frontRightPower += gamepad1.left_stick_x;
        backLeftPower += gamepad1.left_stick_x;
        backRightPower += gamepad1.left_stick_x;
    }

    private void handleStrafe() {
        frontLeftPower += -gamepad1.right_stick_y + gamepad1.right_stick_x;
        frontRightPower += gamepad1.right_stick_y + gamepad1.right_stick_x;
        backLeftPower += -gamepad1.right_stick_y - gamepad1.right_stick_x;
        backRightPower += gamepad1.right_stick_y - gamepad1.right_stick_x;
    }
}
