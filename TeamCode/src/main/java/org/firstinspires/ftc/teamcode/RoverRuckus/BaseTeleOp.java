package org.firstinspires.ftc.teamcode.RoverRuckus;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Mechanisms.Intake;
import org.firstinspires.ftc.teamcode.Mechanisms.SparkyTheRobot;
import org.firstinspires.ftc.teamcode.RoverRuckus.Mappings.SoloMapping;
import org.firstinspires.ftc.teamcode.RoverRuckus.Mappings.ControlMapping;
import org.firstinspires.ftc.teamcode.Utilities.Control.FeedbackController;
import org.firstinspires.ftc.teamcode.Utilities.Control.HoldingPIDMotor;

import java.util.Arrays;

@Config
public abstract class BaseTeleOp extends LinearOpMode {
    public static double HEADING_INTERVAL = Math.PI / 4;
    public static double MAX_EXTEND_POWER = 0.8;

    public ControlMapping controller;

    SparkyTheRobot robot;
    FeedbackController feedback;
    Intake intake;
    ElapsedTime loopTime;
    HoldingPIDMotor winch;

    @Override
    public void runOpMode() {

        robot = new SparkyTheRobot(this);
        robot.init(false);
        loopTime = new ElapsedTime();

        feedback = new FeedbackController(robot.leftHub, robot.rightHub);
        intake = new Intake(robot.leftIntakeFlipper, robot.rightIntakeFlipper,
                robot.leftIntakeRoller, robot.rightIntakeRoller);
        winch = new HoldingPIDMotor(robot.winch, 1);

        // Enable PID control on these motors
        // TODO maybe tune these?
        robot.leftFlipper.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightFlipper.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.linearSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.winch.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Keep standard front direction
        for (int i = 0; i < 4; i++) {
            if (i % 2 == 1) {robot.motorArr[i].setDirection(DcMotor.Direction.FORWARD);}
            else {robot.motorArr[i].setDirection(DcMotor.Direction.REVERSE);}
        }

        // Intake flipper servos are disabled by default
        waitForStart();
        loopTime.reset();

        while (opModeIsActive()) {
            //feedback.update();
            controller.update();

            robot.leftFlipper.setPower(controller.armSpeed());
            robot.rightFlipper.setPower(controller.armSpeed());

            intake.setIntakeSpeed(controller.getSpinSpeed());

            winch.setPower(controller.getHangDir());
            robot.linearSlide.setPower(controller.getExtendSpeed() * MAX_EXTEND_POWER);

            if (controller.flipOut()) {intake.collect();}
            else if (controller.flipBack()) {intake.deposit();}

            // Get base mecanum values
            double turnSpeed = -controller.turnSpeed(); // Negated because of heading
            double[] unscaledPowers = robot.getDrivePowersFromAngle(getControllerDir());
            if (getDist() == 0) {unscaledPowers = new double[4];}

            for (int i = 0; i < unscaledPowers.length; i++) {
                if (i % 2 == 0) {
                    unscaledPowers[i] += turnSpeed;
                } else {
                    unscaledPowers[i] -= turnSpeed;
                }
            }
            telemetry.addData("Turn speed", turnSpeed);
            telemetry.addData("Motor powers pre-scaled", Arrays.toString(unscaledPowers));
            // Scale them appropriately
            double greatest = 0;
            for (double d : unscaledPowers) {
                greatest = Math.max(greatest, Math.abs(d));
            }
            double factor = (controller.moveSpeedScale() * Math.max(getDist() + controller.turnSpeed(), 1)) / greatest;
            for (int i = 0; i < 4; i++) {
                robot.motorArr[i].setPower(clamp(unscaledPowers[i] * factor));
            }


            // Telemetry
            //feedback.updateTelemetry(telemetry);
            int pos = (robot.leftFlipper.getCurrentPosition() + robot.rightFlipper.getCurrentPosition()) / 2;
            telemetry.addData("Arm position", pos);
            telemetry.addData("Left flipper velo", robot.leftFlipper.getVelocity());
            telemetry.addData("Right flipper velo", robot.rightFlipper.getVelocity());
            telemetry.addData("Loop time", loopTime.milliseconds());
            loopTime.reset();
            telemetry.update();
        }
    }

    public double getControllerDir() {
        double controllerAngle = Math.atan2(controller.driveStickY(), controller.driveStickX()) + Math.PI / 2;
        return Math.round(controllerAngle / HEADING_INTERVAL) * HEADING_INTERVAL;
    }

    public double getDist() {
        double d = Math.sqrt(Math.pow(controller.driveStickY(), 2) + Math.pow(controller.driveStickX(), 2));
        if (d < 0.2) {return 0;}
        else if (d > 0.8) {return 1;}
        else {return d;}
    }

    public double clamp(double d) {return Math.max(-1, Math.min(1, d));}
}
