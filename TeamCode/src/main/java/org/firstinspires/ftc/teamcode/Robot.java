package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class Robot {

    private MecanumDrivetrain drivetrain = new MecanumDrivetrain();

    private HardwareMap hwMap =  null;

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        drivetrain.init((ahwMap));
    }

    // Moves the drive train using the given x, y, and rotational velocities
    public void drive(double xVelocity, double yVelocity, double wVelocity){
        drivetrain.drive(xVelocity, yVelocity, wVelocity);
    }


}
