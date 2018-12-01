package org.firstinspires.ftc.teamcode.RoverRuckus;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Mechanisms.SparkyTheRobot;
import org.firstinspires.ftc.teamcode.RoverRuckus.Mappings.ControlMapping;
import org.firstinspires.ftc.teamcode.Utilities.Control.FeedbackController;
import org.firstinspires.ftc.teamcode.Utilities.Control.HoldingPIDMotor;

import java.util.Arrays;

@Config
public abstract class BaseTeleOp extends LinearOpMode {
    public static double HEADING_INTERVAL = Math.PI / 4;
    public static double MAX_EXTEND_POWER = 0.8;
    public static double MAX_FLIP_POWER = 0.6;

    public ControlMapping controller;
    public boolean fieldCentric;

    SparkyTheRobot robot;
    FeedbackController feedback;
    ElapsedTime loopTime;
    HoldingPIDMotor winch;

    @Override
    public void runOpMode() {

        robot = new SparkyTheRobot(this);
        robot.calibrate(fieldCentric);

        loopTime = new ElapsedTime();

        feedback = new FeedbackController(robot.leftHub, robot.rightHub);
        winch = new HoldingPIDMotor(robot.winch, 1);

        // Enable PID control on these motors
        robot.leftFlipper.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightFlipper.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.linearSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.winch.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Keep standard front direction
        for (int i = 0; i < 4; i++) {
            if (i % 2 == 1) {robot.motorArr[i].setDirection(DcMotor.Direction.FORWARD);}
            else {robot.motorArr[i].setDirection(DcMotor.Direction.REVERSE);}
        }

        // Display setup readouts
        telemetry.log().clear();
        telemetry.log().add("Running RR2 TeleOp");
        telemetry.log().add("Control mapping: [[" + controller.getClass().getSimpleName() + "]]");
        telemetry.log().add("Relativity     : [[" + (fieldCentric ? "Field" : "Robot") + " centric]]");
        telemetry.log().add("Stick divisions: [[" + (int) ((2*Math.PI) / HEADING_INTERVAL) + " divisions]]");
        telemetry.update();


        // Intake flipper servos are disabled by default
        waitForStart();
        robot.markerDeployer.setPosition(0.9);
        loopTime.reset();

        while (opModeIsActive()) {
            controller.update();

            robot.leftFlipper.setPower(controller.armSpeed() * MAX_FLIP_POWER);
            robot.rightFlipper.setPower(controller.armSpeed() * MAX_FLIP_POWER);

            robot.intake.setIntakeSpeed(controller.getSpinSpeed());

            if (controller.shakeCamera()) {
                robot.cameraPositioner.flipUp();
            } else {
                robot.cameraPositioner.flipDown();
            }

            // Check to make sure
            int winchPower = controller.getHangDir();
            if (!robot.hangSwitch.getState() && !controller.override()) {
                // If the switch is pressed (if it's not open)
                winchPower = Math.max(winchPower, 0);
            }

            winch.setPower(winchPower);
            robot.linearSlide.setPower(controller.getExtendSpeed() * MAX_EXTEND_POWER);

            if (controller.flipOut()) {robot.intake.collect();}
            else if (controller.flipBack()) {robot.intake.deposit();}

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
            telemetry.addData("Winch pos", robot.winch.getCurrentPosition());
            telemetry.addData("Loop time", loopTime.milliseconds());
            loopTime.reset();
            telemetry.update();
        }
    }

    public double getControllerDir() {
        double controllerAngle = Math.atan2(controller.driveStickY(), controller.driveStickX()) + Math.PI / 2;
        controllerAngle = Math.round(controllerAngle / HEADING_INTERVAL) * HEADING_INTERVAL;

        if (fieldCentric) {
            robot.updateReadings();
            controllerAngle += robot.getGyroHeading();
        }

        return controllerAngle;
    }

    public double getDist() {
        double d = Math.sqrt(Math.pow(controller.driveStickY(), 2) + Math.pow(controller.driveStickX(), 2));
        if (d < 0.2) {return 0;}
        else if (d > 0.8) {return 1;}
        else {return d;}
    }

    public double clamp(double d) {return Math.max(-1, Math.min(1, d));}
}
