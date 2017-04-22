package org.firstinspires.ftc.teamcode.seasons.velocityvortex;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by ftc6347 on 3/11/17.
 */
@Autonomous(name = "Simple 1 RED", group = "3 simple")
public class AutonomousSimpleRed1 extends LinearOpModeBase {
    @Override
    public void runOpMode() throws InterruptedException {
        initializeHardware();
        // reset drive encoders
        setDriveMotorsMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        autonomousInitLoop();

        // drive backward (since the robot is facing backward)
        encoderDrive(0.25, 14, RobotDirection.BACKWARD);

        gyroPivot(0.8, -40, true);

        //drive backward to align before launch
        encoderDrive(0.25, 3, RobotDirection.BACKWARD);

        // open intake door
        getDoor3().setPosition(0.25);

        autoLaunchParticle();

        encoderDriveDiagonal(0.5, 58, RobotDirection.SOUTH_EAST);

        gyroPivot(0.5, -40, true);

        encoderDriveDiagonal(0.5, 55, RobotDirection.SOUTH_WEST);

        gyroPivot(0.5, -5, true);

        encoderDriveDiagonal(0.5, 24, RobotDirection.NORTH_WEST);

        encoderDrive(0.5, 20, RobotDirection.LEFT);
    }
}
