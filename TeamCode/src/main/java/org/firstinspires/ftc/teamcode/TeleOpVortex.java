package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.modules.GamepadV2;
import org.firstinspires.ftc.teamcode.modules.MecanumDrive;

@TeleOp
public class TeleOpVortex extends OpMode {
    private HardwareVortex robot = new HardwareVortex();

    private GamepadV2 pad2 = new GamepadV2(), pad1 = new GamepadV2();

    private boolean driveMec = true;

    @Override
    public void init() {
        robot.init(hardwareMap);
    }

    @Override public void start() {
        robot.frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void loop() {
        pad2.update(gamepad2);
        pad1.update(gamepad1);

        if(driveMec) {
            MecanumDrive.loop(robot.frontLeft, robot.frontRight, robot.backLeft, robot.backRight, pad2);
        }
        else {
            robot.frontLeft.setPower(pad2.left_stick_y_exponential(1));
            robot.backLeft.setPower(pad2.left_stick_y_exponential(1));
            robot.frontRight.setPower(pad2.right_stick_y_exponential(1));
            robot.backRight.setPower(pad2.right_stick_y_exponential(1));
        }

        if(pad2.a_isReleased(true)) {
            driveMec = !driveMec;
        }

        if(pad2.right_trigger()) {
            robot.flipper.setPower(1);
        }
        else if(pad2.left_trigger()) {
            robot.flipper.setPower(-1);
        }
        else {
            robot.flipper.setPower(0);
        }

        if(pad2.right_bumper) {
            robot.lift.setPower(1);
        }
        else if(pad2.left_bumper) {
            robot.lift.setPower(-1);
        }
        else {
            robot.lift.setPower(0);
        }

        if(pad1.b) {
            robot.shooter.setPower(HardwareVortex.SHOOTER_POWER);
        }
        else if(pad1.x) {
            robot.shooter.setPower(-HardwareVortex.SHOOTER_POWER);
        }
        else {
            robot.shooter.setPower(0);
        }

        if(pad1.right_bumper) {
            robot.intake.setPower(0.75);
        }
        else if(pad1.left_bumper) {
            robot.intake.setPower(-0.50);
        }
        else {
            robot.intake.setPower(0);
        }

        robot.generateTelemetry(telemetry, true);
    }
}
