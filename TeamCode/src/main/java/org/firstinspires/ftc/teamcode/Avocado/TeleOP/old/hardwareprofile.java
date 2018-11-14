package org.firstinspires.ftc.teamcode.Avocado.TeleOP.old;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class hardwareprofile {

    // Lift
    public DcMotor linearLift = null;
    // public DcMotor linearLift2 = null; // <-- uncomment if needed
    public DcMotor claw = null;

    // Drivetrain <-- we have not decided on the Drivetrain design. This assumes we are utilizing four motors for the drivetrain.
    public DcMotor topLeftMotor = null;
    public DcMotor topRightMotor = null;
    public DcMotor bottomLeftMotor = null;
    public DcMotor bottomRightMotor = null;

    // Drivetrain <-- This drivetrain design assumes that we are using two motors for the drivetrain: Chained design
    /*
    public DcMotor LeftMotor = null;
    public DcMotor RightMotor = null;
    */

    HardwareMap hwmap = null;

    public void init(HardwareMap ahwmap) {
        // Save reference to Hardware map
        hwmap = ahwmap;

        // Hardware map

        //Drivetrain
        topLeftMotor = hwmap.get(DcMotor.class, "dttopleft");
        topRightMotor = hwmap.get(DcMotor.class, "dttopright");
        bottomLeftMotor = hwmap.get(DcMotor.class, "dtbottomleft");
        bottomRightMotor = hwmap.get(DcMotor.class, "dtbottoright");

        // Elevator arm
        //linearLift = hwmap.get(DcMotor.class, "linearslide");
        // linearLift2 = hwmap.get(DcMotor.class, "linearslide2");





    }



}

