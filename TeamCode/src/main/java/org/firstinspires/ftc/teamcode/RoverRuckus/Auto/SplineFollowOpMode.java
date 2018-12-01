package org.firstinspires.ftc.teamcode.RoverRuckus.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.DriveSystems.Mecanum.RoadRunner.SampleMecanumDriveBase;
import org.firstinspires.ftc.teamcode.DriveSystems.Mecanum.RoadRunner.SampleMecanumDriveREV;
import org.firstinspires.ftc.teamcode.Mechanisms.SparkyTheRobot;

@Autonomous
public class SplineFollowOpMode extends LinearOpMode {
    SparkyTheRobot robot;

    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDriveBase drive = new SampleMecanumDriveREV(hardwareMap);
        robot = new SparkyTheRobot(this);
        //robot.calibrate();
        robot.winch.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.winch.setPower(0.5);
        robot.winch.setTargetPosition(0);

        waitForStart();

        robot.winch.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.winch.setPower(-0.5);
        while (opModeIsActive()) {
            robot.updateReadings();
            if (robot.primaryIMU.getGravity().zAccel >= 9.7) {
                break;
            }
        }
        robot.winch.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.winch.setPower(0);
        drive.followTrajectory(Paths.UNHOOK);
        while (opModeIsActive() && drive.isFollowingTrajectory()) {
            drive.update();
        }

        // Now, lower the hang arm
        robot.linearSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.linearSlide.setPower(0.4);
        robot.linearSlide.setTargetPosition(500);
        robot.intake.deposit();

        robot.winch.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.winch.setPower(1);
        robot.winch.setTargetPosition(1500);

        int encoder = robot.winch.getCurrentPosition();
        while (opModeIsActive() && Math.abs(encoder - 1500) > 20) {
            encoder = robot.winch.getCurrentPosition();
            telemetry.addData("Current position", encoder);
            telemetry.update();
        }

        robot.winch.setMotorDisable();
        robot.linearSlide.setTargetPosition(0);
        robot.intake.goToMin();
        sleep(1500);

        drive.followTrajectory(Paths.UNDO_UNHOOK);
        while (opModeIsActive() && drive.isFollowingTrajectory()) {
            drive.update();
        }
    }
}