package org.firstinspires.ftc.teamcode.PIDTesting;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsUsbDcMotorController;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.DifferentialControlLoopCoefficients;

/**
 * Created by guberti on 10/21/2017.
 */
@TeleOp(name="PID Responsiveness Test", group="PID")
@Disabled
public class PIDTestSystem extends LinearOpMode {
    PIDTestInterface p;
    ModernRoboticsUsbDcMotorController motorController;

    boolean wasAPressed;
    boolean wasBPressed;
    boolean wasXPressed;

    @Override
    public void runOpMode() {
        p = new PIDTestMashupPID(hardwareMap.dcMotor.get("motor"), telemetry);
        motorController = hardwareMap.get(ModernRoboticsUsbDcMotorController.class, "ctrl");

        DifferentialControlLoopCoefficients d = new DifferentialControlLoopCoefficients(160, 32, 112);
        motorController.setDifferentialControlLoopCoefficients(1, d);
        motorController.setDifferentialControlLoopCoefficients(2, d);

        wasAPressed = false;
        wasBPressed = false;
        wasXPressed = false;

        waitForStart();

        while(opModeIsActive()) {
            double s = 0;
            if (gamepad1.dpad_left) {
                s = -0.5;
            } else if (gamepad1.dpad_right) {
                s = 0.5;
            }

            if (gamepad1.dpad_up) {
                s *= 2;
            } else if (gamepad1.dpad_down) {
                s *= 0.25;
            }

            if (gamepad1.left_stick_x > 0.15) {
                s = 1;
            } else if (gamepad1.left_stick_x < -0.15) {
                s = -1;
            }

            telemetry.addData("Input", s);
            telemetry.update();

            if (gamepad1.a) {
                p = new PIDTestNoPID(hardwareMap.dcMotor.get("motor"), telemetry);
            }
            if (gamepad1.x) {
                p = new PIDTestDefaultPID(hardwareMap.dcMotor.get("motor"), telemetry);
            }
            if (gamepad1.b) {
                p = new PIDTestMashupPID(hardwareMap.dcMotor.get("motor"), telemetry);
            }
            p.setPower(s);
        }
    }
}
