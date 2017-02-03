package org.firstinspires.ftc.teamcode.seasons.velocityvortex;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by ftc6347 on 10/30/16.
 */
@Autonomous(name = "Simple autonomous", group = "autonomous")
public class AutonomousSimple extends LinearOpModeBase {

    @Override
    public void runOpMode() throws InterruptedException {
        initializeHardware();
        // reset drive encoders
        setDriveMotorsMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // use encoders
        getBackLeftDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        getBackRightDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        getFrontLeftDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        getFrontRightDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("color sensor red1", getColorSensor1().red());
        telemetry.addData("color sensor blue1", getColorSensor1().blue());
        telemetry.update();

        waitForStart();

        // ten second wait
        getRobotRuntime().reset();
        while(opModeIsActive() && getRobotRuntime().seconds() < 10) {
            idle();
        }

        // drive backward (since the robot is facing backward)
        encoderDrive(0.5, -12, -12);

        // launch the first (loaded) particle
        launchParticle();

        // open intake door
        getDoor3().setPosition(0.25);

        // run the intake
        getRobotRuntime().reset();
        while(opModeIsActive() && getRobotRuntime().milliseconds() < 500) {
            getIntakeMotor().setPower(-1);
        }
        getIntakeMotor().setPower(0);

        // launch the second particle
        launchParticle();

        // drive back three more feet
        encoderDrive(0.5, -36, -36);
    }
}