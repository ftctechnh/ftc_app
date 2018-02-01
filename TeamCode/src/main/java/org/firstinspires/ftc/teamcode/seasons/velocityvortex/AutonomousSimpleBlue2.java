package org.firstinspires.ftc.teamcode.seasons.velocityvortex;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by ftc6347 on 10/30/16.
 */
@Disabled
@Autonomous(name = "Simple 2 BLUE", group = "4 simple")
public class AutonomousSimpleBlue2 extends LinearOpModeBase {

    @Override
    public void runOpMode() throws InterruptedException {
        initializeHardware();
        // reset drive encoders
        setDriveMotorsMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        autonomousInitLoop();

        // drive backward (since the robot is facing backward)
        encoderDrive(0.25, 14, RobotDirection.BACKWARD);

        gyroPivot(0.8, 45, true);

        //drive backward to align before launch
        encoderDrive(0.25, 3, RobotDirection.BACKWARD);

        // open intake door
        getDoor3().setPosition(0.25);

        autoLaunchParticle();

        encoderDriveDiagonal(0.5, 69, RobotDirection.SOUTH_WEST);

        // align before driving up ramp
        gyroPivot(0.5, 45, true);

        // drive up ramp
        encoderDrive(0.5, 20, RobotDirection.LEFT);
    }
}