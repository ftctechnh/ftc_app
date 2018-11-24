package teamcode.kkl2;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "KKL2TeleOp", group = "Linear OpMode")
public class KKL2TeleOp extends LinearOpMode {

    private static final double INTAKE_BASE_SERVO_SPEED = 0.5;
    private static final double INTAKE_SWALLOW_POWER = 0.5;

    private static final double LIFT_BASE_MOTOR_SPEED = 1.0;

    @Override
    public void runOpMode() {
        KKL2HardwareManager.initialize(this);
        waitForStart();
        while (opModeIsActive()) {
            driveUpdate();
            // intakeUpdate();
            liftUpdate();
        }
    }

    private void driveUpdate() {
        float drive = gamepad1.left_stick_y;
        float steer = gamepad1.left_stick_x;
        double powerL = drive + steer;
        double powerR = drive - steer;
        KKL2HardwareManager.driveLMotor.setPower(powerL);
        KKL2HardwareManager.driveRMotor.setPower(powerR);
    }

    private double intakeWristServoPos = 0.0;

    private void intakeUpdate() {
        double intakeBaseServoPower;
        if (gamepad1.left_trigger > 0) {
            intakeBaseServoPower = -INTAKE_BASE_SERVO_SPEED;
        } else if (gamepad2.right_trigger > 0) {
            intakeBaseServoPower = INTAKE_BASE_SERVO_SPEED;
        } else {
            intakeBaseServoPower = 0.0;
        }
        KKL2HardwareManager.intakeBaseServo.setPower(intakeBaseServoPower);

        intakeWristServoPos += gamepad1.right_stick_y;
        intakeWristServoPos = clamp(intakeWristServoPos, -1.0, 1.0);
        KKL2HardwareManager.intakeWristServo.setPosition(intakeWristServoPos);

        double intakeSwallowPower;
        if (gamepad1.left_bumper) {
            intakeSwallowPower = -INTAKE_SWALLOW_POWER;
        } else if (gamepad1.right_bumper) {
            intakeSwallowPower = INTAKE_SWALLOW_POWER;
        } else {
            intakeSwallowPower = 0.0;
        }
        KKL2HardwareManager.intakeSwallow.setPower(intakeSwallowPower);
    }

    private void liftUpdate() {
        double liftBaseMotorPower;
        if (gamepad1.a) {
            liftBaseMotorPower = -LIFT_BASE_MOTOR_SPEED;
        } else if (gamepad1.y) {
            liftBaseMotorPower = LIFT_BASE_MOTOR_SPEED;
        } else {
            liftBaseMotorPower = 0.0;
        }
        KKL2HardwareManager.liftBaseMotor.setPower(liftBaseMotorPower);

        if (gamepad1.x) {
            KKL2HardwareManager.liftLatchServo.setPosition(1.0);
        }
        if (gamepad1.b) {
            KKL2HardwareManager.liftLatchServo.setPosition(-1.0);
        }
    }

    private double clamp(double toClamp, double min, double max) {
        if (toClamp < min) {
            return min;
        }
        if (toClamp > max) {
            return max;
        }
        return toClamp;
    }

}
