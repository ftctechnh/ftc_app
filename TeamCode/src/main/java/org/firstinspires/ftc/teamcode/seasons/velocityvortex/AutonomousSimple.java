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
        resetDriveEncoders();

        // use encoders
        getBackLeftDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        getBackRightDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        getFrontLeftDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        getFrontRightDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("color sensor red", getColorSensor().red());
        telemetry.addData("color sensor blue", getColorSensor().blue());
        telemetry.update();

        waitForStart();

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
    }
}