package teamcode.talons;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "TTTeleOp", group = "Linear OpMode")
public class TTTeleOp extends LinearOpMode {

    private static final float POWER_MULTIPLIER = 1.0F;

    private static final float MIN_DRIVE_X = 0.25F;
    private static final float MIN_DRIVE_Y = 0.25F;

    private static final double LIFT_SPEED = 1.0;

    private boolean translationalMovementThisUpdate;

    @Override
    public void runOpMode() {
        TTHardwareManager.initialize(this);
        waitForStart();
        while (opModeIsActive()) {
            float driveX = gamepad1.left_stick_x;
            float driveY = gamepad1.left_stick_y;
            drive(driveX * POWER_MULTIPLIER, driveY * POWER_MULTIPLIER);
            if (!translationalMovementThisUpdate) {
                float turn = gamepad1.right_stick_x;
                turn(turn * POWER_MULTIPLIER);
            }
            if (gamepad1.y) {
                // rise
                setMoveMechanismPower(-LIFT_SPEED);
            } else if (gamepad1.x) {
                setMoveMechanismPower(0.5 * LIFT_SPEED);
            } else {
                setMoveMechanismPower(0);
            }
        }
    }

    private void drive(float x, float y) {
        float frontLeftPow;
        float frontRightPow;
        float backLeftPow;
        float backRightPow;
        if (Math.abs(x) > MIN_DRIVE_X) {
            // side to side
            frontLeftPow = -x;
            frontRightPow = x;
            backLeftPow = x;
            backRightPow = -x;
            translationalMovementThisUpdate = true;
        } else if (Math.abs(y) > MIN_DRIVE_Y) {
            // forward backward
            frontLeftPow = y;
            frontRightPow = y;
            backLeftPow = y;
            backRightPow = y;
            translationalMovementThisUpdate = true;
        } else {
            frontLeftPow = 0.0F;
            frontRightPow = 0.0F;
            backLeftPow = 0.0F;
            backRightPow = 0.0F;
            translationalMovementThisUpdate = false;
        }
        TTHardwareManager.frontLeftDrive.setPower(frontLeftPow);
        TTHardwareManager.frontRightDrive.setPower(frontRightPow);
        TTHardwareManager.backLeftDrive.setPower(backLeftPow);
        TTHardwareManager.backRightDrive.setPower(backRightPow);
    }

    private void turn(float turn) {
        TTHardwareManager.frontLeftDrive.setPower(-turn);
        TTHardwareManager.frontRightDrive.setPower(turn);
        TTHardwareManager.backLeftDrive.setPower(-turn);
        TTHardwareManager.backRightDrive.setPower(turn);
    }

    private void setMoveMechanismPower(double power) {
        TTHardwareManager.liftMotorL.setPower(power);
        TTHardwareManager.liftMotorR.setPower(power);
    }

}
