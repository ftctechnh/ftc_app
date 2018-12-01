package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Mechanisms.SparkyTheRobot;

@TeleOp
@Config
public class SparkyArmTest extends LinearOpMode {

    SparkyTheRobot robot;

    public static double G_STAT = 0;
    public static double A_STAT = 0;
    public static double CONST_STAT = 10;
    public static double VELO_CAP = 2000;

    public final static int ENCODER_TICKS_PER_REV = 28;
    public final static int GEAR_REDUCTION = 256;
    public final static int TICKS_PER_OUTPUT_REV = ENCODER_TICKS_PER_REV * GEAR_REDUCTION;

    /*public static double COLLECT_ANGLE_DEG = 190;
    public static double DEPOSIT_ANGLE_DEG = 75;

    public static double COLLECT_ANGLE = SparkyTheRobot.normAngle(Math.toRadians(COLLECT_ANGLE_DEG));
    public static double DEPOSIT_ANGLE = SparkyTheRobot.normAngle(Math.toRadians(DEPOSIT_ANGLE_DEG));
    */

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new SparkyTheRobot(this);
        robot.calibrate(false);
        telemetry.log().add("Finished calibrating");
        ElapsedTime cycleTimer = new ElapsedTime();
        robot.leftFlipper.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightFlipper.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.leftFlipper.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        robot.rightFlipper.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        robot.intake.collect();

        robot.leftFlipper.setVelocity(0);
        robot.rightFlipper.setVelocity(0);

        double cumVelo = 0;

        waitForStart();
        telemetry.setMsTransmissionInterval(40);
        cycleTimer.reset();


        while (opModeIsActive()) {
            double t = cycleTimer.milliseconds();
            cycleTimer.reset();

            //double angle = robot.normAngle(robot.armIMU.getAngularOrientation().thirdAngle - Math.PI/2);
            //double currVelo = Math.toDegrees(robot.armIMU.getAngularVelocity().zRotationRate);

            double currVelo = (cumVelo / (double) TICKS_PER_OUTPUT_REV) * 360;
            double angle = (robot.leftFlipper.getCurrentPosition() / (double) TICKS_PER_OUTPUT_REV) * Math.PI * 2;

            double change = CONST_STAT + currVelo * A_STAT - Math.abs(Math.cos(angle)) * G_STAT;
            change *= t;

            int sign;

            if (gamepad1.right_bumper) {
                sign = 1;
            } else if (gamepad1.left_bumper) {
                sign = -1;
            } else if (cumVelo < 0) {
                sign = 1;
            } else {
                sign = -1;
            }

            cumVelo = clamp (cumVelo + sign * change, VELO_CAP);

            if (Math.abs(cumVelo) <= change && !gamepad1.right_bumper && !gamepad1.left_bumper) {
                cumVelo = 0;
            }
            robot.leftFlipper.setVelocity(cumVelo);
            robot.rightFlipper.setVelocity(cumVelo);

            telemetry.addData("Left pos", robot.leftFlipper.getCurrentPosition());
            telemetry.addData("Right pos", robot.rightFlipper.getCurrentPosition());
            telemetry.addData("Angle", angle);
            telemetry.addData("Right pos", currVelo);
            telemetry.addData("Change", change);
            telemetry.addData("Cumulative velocity", cumVelo );
            telemetry.addData("Loop time", t);
            telemetry.update();
            // Power should also be inversely proportional to cos(angle)
        }
    }

    private double clamp (double val, double bound) {
        return Math.max(Math.min(val, bound), -bound);
    }
}
