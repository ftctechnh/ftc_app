package org.firstinspires.ftc.teamcode.RoverRuckus;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Mechanisms.Arm;
import org.firstinspires.ftc.teamcode.Mechanisms.SparkyTheRobot;
import org.firstinspires.ftc.teamcode.RoverRuckus.Auto.AutoUtils;
import org.firstinspires.ftc.teamcode.RoverRuckus.Mappings.ControlMapping;
import org.firstinspires.ftc.teamcode.Utilities.Control.FeedbackController;
import org.firstinspires.ftc.teamcode.Utilities.Control.HoldingPIDMotor;
import org.firstinspires.ftc.teamcode.Utilities.Control.WheelDriveVector;

import java.util.Arrays;

@Config
public abstract class BaseTeleOp extends LinearOpMode {
    public static double HEADING_INTERVAL = Math.PI / 4;
    public static int MAX_EXTENDER_POS = 950;
    public static int MIN_EXTENDER_POS = 0;
    public static double EXTEND_MAXED_DRIVE_POWER = 0.4;
    public static double TURN_MAX_SPEED = 1.0;
    public static double TURN_SPEED_CUTOFF = 0.03;
    public static double SLEW_TURN_FACTOR = 0.2;
    public static int WINCH_MAX_POS = 6700;

    public ControlMapping controller;
    public boolean fieldCentric;

    private boolean wasTurningTo255;

    SparkyTheRobot robot;
    ElapsedTime loopTime;
    HoldingPIDMotor winch;

    Arm arm;

    @Override
    public void runOpMode() {

        robot = new SparkyTheRobot(this);
        robot.calibrate(true);

        loopTime = new ElapsedTime();
        wasTurningTo255 = false;

        winch = new HoldingPIDMotor(robot.winch, 1);

        arm = new Arm(robot.leftFlipper, robot.rightFlipper, robot.linearSlide);

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
        robot.markerDeployer.setPosition(AutoUtils.MARKER_DEPLOYER_RETRACTED);
        robot.parkingMarker.setPosition(AutoUtils.PARKING_MARKER_RETRACTED);
        loopTime.reset();

        while (opModeIsActive()) {
            controller.update();

            if (controller.collectWithArm()) {
                arm.collect();
            } else if (controller.depositWithArm()) {
                arm.deposit();
            } else {
                arm.setPower(controller.armSpeed());
            }

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
            if (controller.flipOut()) {robot.intake.collect();}
            else if (controller.flipBack()) {robot.intake.deposit();}
            else if (controller.armSpeed() < 0) {robot.intake.collect();}

            WheelDriveVector speeds = new WheelDriveVector(controller.driveStickY(), controller.driveStickX(), controller.turnSpeed());
            speeds.scale(controller.translateSpeedScale(), controller.turnSpeedScale());

            // Control linear slide extend retract and drive robot if necessary
            double slidePower = controller.getExtendSpeed();
            robot.linearSlide.setPower(slidePower);
            int linearSlidePos = robot.linearSlide.getCurrentPosition();
            if (((linearSlidePos < MIN_EXTENDER_POS && slidePower < 0) ||
                    (linearSlidePos > MAX_EXTENDER_POS && slidePower > 0)) &&
                    arm.isCollecting()) { // Don't move robot if we're not collecting
                speeds.forwardSpeed += slidePower * EXTEND_MAXED_DRIVE_POWER;
            }

            // Slew drive mapped to GP2 left/right
            speeds.translateSpeed += controller.getSlewSpeed();
            speeds.turnSpeed += controller.getSlewSpeed() * SLEW_TURN_FACTOR;
            speeds.turnSpeed += controller.getGP2TurnSpeed();

            // Control heading locking
            if (controller.lockTo45() || controller.lockTo225()) {
                // Pressing y overrides lock to 45
                double targetAngle = Math.PI * 1.75;
                if (controller.lockTo225()) {
                    targetAngle = Math.PI * 0.75;
                }

                double difference = robot.getSignedAngleDifference(targetAngle, robot.getHeading());
                double turnSpeed = Math.max(-TURN_MAX_SPEED, Math.min(TURN_MAX_SPEED, difference));
                turnSpeed = Math.copySign(Math.max(TURN_SPEED_CUTOFF, Math.abs(turnSpeed)), turnSpeed);
                speeds.turnSpeed = -turnSpeed;
            }

            if (controller.lockTo225() && !wasTurningTo255) {
                wasTurningTo255 = true;
                winch.setTargetPos(WINCH_MAX_POS);
            } else if (wasTurningTo255 && !controller.lockTo225()) {
                wasTurningTo255 = false;
            }

            robot.setMotorSpeeds(speeds.getDrivePowers());

            // Telemetry
            //feedback.updateTelemetry(telemetry);
            int pos = (robot.leftFlipper.getCurrentPosition() + robot.rightFlipper.getCurrentPosition()) / 2;
            telemetry.addData("Arm position", pos);
            telemetry.addData("Drive stick y", controller.driveStickY());
            telemetry.addData("Drive stick actual y", gamepad1.left_stick_y);
            telemetry.addData("Extender position", robot.linearSlide.getCurrentPosition());

            telemetry.addData("Winch pos", robot.winch.getCurrentPosition());
            telemetry.addData("Loop time", loopTime.milliseconds());
            loopTime.reset();
            telemetry.update();
        }
    }

    public double getControllerDir() {
        double controllerAngle = Math.atan2(controller.driveStickY(), -controller.driveStickX()) + Math.PI / 2;
        controllerAngle = Math.round(controllerAngle / HEADING_INTERVAL) * HEADING_INTERVAL;

        if (fieldCentric) {
            robot.updateReadings();
            controllerAngle += robot.getGyroHeading();
        }

        return controllerAngle;
    }


    public double clamp(double d) {return Math.max(-1, Math.min(1, d));}
}
