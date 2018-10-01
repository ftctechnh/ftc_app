package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This is NOT an opmode.
 *
 * Configuration for the beta/robot with working name "REVTrix"
 *
 * REV Core Hex Motors Specifications
 *  http://www.revrobotics.com/rev-41-1300/
 *
 *
 */




public class REVTrix {

    // REVTrix specific measurements
    static final double     COUNTS_PER_MOTOR_REV    = 4 ;
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;

    // REVTrix specific drive train members.
    static final double     WHEEL_DIAMETER_INCHES   = 3.5 ; //estimate
    static final double     DRIVE_WHEEL_SEPARATION  = 28.0 ; //estimate
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);

    // REVTrix specific motor and actuator members.
    public DcMotor FrontLeftDrive   = null;
    public DcMotor FrontRightDrive  = null;
    public DcMotor RearLeftDrive    = null;
    public DcMotor RearRightDrive   = null;

    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    public REVTrix() {

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Tetrix motors
        FrontLeftDrive = hwMap.get(DcMotor.class, "motor0");
        FrontRightDrive = hwMap.get(DcMotor.class, "motor1");
        RearLeftDrive = hwMap.get(DcMotor.class, "motor2");
        RearRightDrive = hwMap.get(DcMotor.class, "motor3");

        FrontLeftDrive.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if
        // using AndyMark motors
        FrontRightDrive.setDirection(DcMotor.Direction.FORWARD);// Set to FORWARD if
        // using AndyMark motors
        RearLeftDrive.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if
        // using AndyMark motors
        RearRightDrive.setDirection(DcMotor.Direction.FORWARD);// Set to FORWARD if
        // using AndyMark motors

        // Set all motors to zero power
        FrontLeftDrive.setPower(0);
        FrontRightDrive.setPower(0);
        RearLeftDrive.setPower(0);
        RearRightDrive.setPower(0);

        // Set both motors to run with encoders.
        FrontLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FrontRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        RearLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        RearRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }






}



