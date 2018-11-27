package teamcode.ttl2;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "TTL2TeleOp", group = "Linear OpMode")
public class TTL2TeleOp extends LinearOpMode {

    /**
     * The minimum magnitude of game pad input on the x-axis of the left stick that must be reached for lateral translational movement on a single update.
     */
    private static final float MIN_DRIVE_X_INPUT = 0.25F;
    /**
     * The minimum magnitude of game pad input on the y-axis of the left stick that must be reached for vertical translational movement on a single update.
     */
    private static final float MIN_DRIVE_Y_INPUT = 0.25F;
    /**
     * Value representing the speed at which the lift mechanism operates.
     */
    private static final double LIFT_SPEED = 1.0;

    /**
     * Used to make sure that the robot does not translate and rotate on the same update.
     */
    private boolean translationalMovementThisUpdate;

    @Override
    public void runOpMode() {
        // initializes hardware components
        TTL2HardwareManager.initialize(this);
        waitForStart();
        while (opModeIsActive()) {
            float driveX = gamepad1.left_stick_x;
            float driveY = gamepad1.left_stick_y;
            drive(driveX, driveY);
            // do not rotate robot if the robot translated this update
            if (!translationalMovementThisUpdate) {
                float turn = gamepad1.right_stick_x;
                turn(turn);
            }
            if (gamepad1.y) {
                // raise lift mechanism
                setMoveMechanismPower(-LIFT_SPEED);
            } else if (gamepad1.a) {
                // lower lift mechanism
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
        if (Math.abs(x) > MIN_DRIVE_X_INPUT) {
            // side to side
            frontLeftPow = -x;
            frontRightPow = x;
            backLeftPow = x;
            backRightPow = -x;
            translationalMovementThisUpdate = true;
        } else if (Math.abs(y) > MIN_DRIVE_Y_INPUT) {
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
        TTL2HardwareManager.frontLeftDrive.setPower(frontLeftPow);
        TTL2HardwareManager.frontRightDrive.setPower(frontRightPow);
        TTL2HardwareManager.backLeftDrive.setPower(backLeftPow);
        TTL2HardwareManager.backRightDrive.setPower(backRightPow);
    }

    private void turn(float turn) {
        TTL2HardwareManager.frontLeftDrive.setPower(-turn);
        TTL2HardwareManager.frontRightDrive.setPower(turn);
        TTL2HardwareManager.backLeftDrive.setPower(-turn);
        TTL2HardwareManager.backRightDrive.setPower(turn);
    }

    private void setMoveMechanismPower(double power) {
        TTL2HardwareManager.liftMotorL.setPower(power);
        TTL2HardwareManager.liftMotorR.setPower(power);
    }

}
