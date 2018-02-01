package org.firstinspires.ftc.teamcode.seasons.velocityvortex;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by ftc6347 on 4/17/17.
 */
@Disabled
@Autonomous(name = "Simple 3 BLUE", group = "5 simple")
public class AutonomousSimpleBlue3 extends LinearOpModeBase {
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
        encoderDrive(0.25, 3 , RobotDirection.BACKWARD);

        // open intake door
        getDoor3().setPosition(0.25);

        autoLaunchParticle();

        // pivot to drive onto center vortex base
        gyroPivot(0.5, -90, true);

        encoderDriveDiagonal(0.5, 30, RobotDirection.NORTH_WEST);
        encoderDrive(0.5, 12, RobotDirection.LEFT);
    }
}
