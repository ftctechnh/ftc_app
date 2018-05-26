package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
//import com.qualcomm.robotcore.hardware.Servo;

public class Robot {


    private MecanumDrivetrain drivetrain = new MecanumDrivetrain();

    private HardwareMap hwMap =  null;


    public Robot(){
        // Constructor
    }


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
