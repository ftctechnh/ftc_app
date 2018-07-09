package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Robot {

    MecanumDrivetrain drivetrain = new MecanumDrivetrain(this);

    private HardwareMap hwMap =  null;
    OpMode opMode;

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap, OpMode op) {
        // Save reference to Hardware map
        hwMap = ahwMap;
        opMode = op;

        drivetrain.init((ahwMap));
    }

    // Moves the drive train using the given x, y, and rotational velocities
    public void drive(double xVelocity, double yVelocity, double wVelocity){
        drivetrain.drive(xVelocity, yVelocity, wVelocity);
    }

    public void encoderDrive(double yDist, double maxSpeed){
        drivetrain.encoderDrive(0, yDist, 0, maxSpeed);
    }


}
