package org.firstinspires.ftc.teamcode.PIDTesting;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.NullbotHardware;

@TeleOp(name="Nullbot: SwervePID test", group="Calibration")
public class NullbotPIDTest extends LinearOpMode {

    NullbotHardware robot   = new NullbotHardware();
    double lastReset;
    double targetSpeed;
    boolean wasLeftBumperPressed;
    boolean wasRightBumperPressed;
    boolean wasUpPressed;
    boolean wasDownPressed;
    boolean wasLeftPressed;
    boolean wasRightPressed;

    @Override
    public void runOpMode() {

        robot.init(hardwareMap, this, gamepad1, gamepad2);

        targetSpeed = 0.0;
        lastReset = 0.0;
        wasLeftBumperPressed = false;
        wasRightBumperPressed = false;

        robot.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.left_trigger > 0.5) {
                targetSpeed += 0.01;
            }
            if (gamepad1.right_trigger > 0.5) {
                targetSpeed -= 0.01;
            }
            if (gamepad1.left_bumper && !wasLeftBumperPressed) {
                targetSpeed += 0.01;
            }
            if (gamepad1.right_bumper && !wasRightBumperPressed) {
                targetSpeed -= 0.01;
            }
            if (gamepad1.dpad_up && !wasUpPressed) {
                reset();
            }
            /*if (gamepad1.dpad_down && !wasDownPressed) {
                motor.K *= 0.9;
                motor.recalculateIntegralConstant();
            }
            if (gamepad1.dpad_left && !wasLeftPressed) {
                motor.SECONDS_PER_REPEAT *= 0.9;
                motor.recalculateIntegralConstant();
            }
            if (gamepad1.dpad_right && !wasRightPressed) {
                motor.SECONDS_PER_REPEAT *= 1.1;
                motor.recalculateIntegralConstant();
            }*/

            wasLeftBumperPressed = gamepad1.left_bumper;
            wasRightBumperPressed = gamepad1.right_bumper;
            wasUpPressed = gamepad1.dpad_up;
            wasDownPressed = gamepad1.dpad_down;
            wasLeftPressed = gamepad1.dpad_left;
            wasRightPressed = gamepad1.dpad_right;
            targetSpeed = robot.clamp(targetSpeed);

            robot.frontLeft.setPower(targetSpeed);
            telemetry.addLine().addData("Target speed", targetSpeed);
            telemetry.update();
        }
    }
    public void reset() {
        robot.frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.sleep(1);
        robot.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}
