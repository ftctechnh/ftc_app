package org.firstinspires.ftc.teamcode.Utilities.RoadRunner;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.DriveSystems.Mecanum.RoadRunner.RoadRunnerMecanumInterface;
import com.acmerobotics.roadrunner.drive.Drive;

@TeleOp(name="Quad - Track Width Calibration", group="Diagnostics")
public class TrackWidthCalibration extends LinearOpMode {
    private final int totalRevolutions = 8;
    private final double power = 0.7;

    @Override
    public void runOpMode() {

        Drive drive = new RoadRunnerMecanumInterface(hardwareMap);
        telemetry.log().add("Initialized motor");
        telemetry.update();
        sleep(2000);

        BNO055IMU imu = hardwareMap.get(BNO055IMU.class, "primaryIMU");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        imu.initialize(parameters);

        telemetry.log().add("Press play to begin the track width calibration routine");
        telemetry.log().add("Make sure your robot has enough clearance to turn smoothly");
        telemetry.log().add("Additionally, set the drive's track width to 1");
        telemetry.update();

        waitForStart();

        telemetry.log().clear();
        telemetry.log().add("Running...");
        telemetry.update();

        int revolutions = 0;
        boolean startedMoving = false;
        double lastHeading = 0;

        drive.setPoseEstimate(new Pose2d());
        drive.setVelocity(new Pose2d(0.0, 0.0,  power));
        while (opModeIsActive() && (!startedMoving || revolutions <= totalRevolutions)) {
            double heading = imu.getAngularOrientation().firstAngle;
            if (imu.getParameters().angleUnit == BNO055IMU.AngleUnit.DEGREES) {
                heading = Math.toRadians(heading);
            }
            if (heading >= Math.PI / 2.0) {
                startedMoving = true;
            }
            if (startedMoving && (lastHeading < 0.0 && heading >= 0.0)) {
                revolutions++;
            }
            drive.updatePoseEstimate();
            lastHeading = heading;
        }
        drive.setVelocity(new Pose2d(0.0, 0.0, 0.0));
        double effectiveTrackWidth = drive.getPoseEstimate().getHeading() / (4.0 * Math.PI * totalRevolutions);

        telemetry.log().clear();
        telemetry.log().add("Calibration complete");
        telemetry.log().add(String.format("effective track width = %.2f", effectiveTrackWidth));
        telemetry.update();

        while (opModeIsActive()) {
            idle();
        }

    }
}
