package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

//This class defines all the specific hardware for a the BACONbot robot.

public class uhhhHardwareClass {
    /* Public OpMode members. */

    public DcMotor verticalArmMotor = null;
    public CRServo clawServo = null;

    /* Give place holder values for the motors and the grabber servo */
    double VerticalArmPower = 0;

    /* Define values used in commanding the claw */
    static double clawClose = .5;
    static double clawOpen = -.5;
    static double clawStill = 0;

    /* local OpMode members. */
    HardwareMap hwMap = null;
    private ElapsedTime period = new ElapsedTime();

    /* Constructor */
    public uhhhHardwareClass() {

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Hardware

        // Define and Initialize Hardware
        verticalArmMotor = hwMap.dcMotor.get("VAM");
        clawServo =  hwMap.crservo.get("CS");

        // Set all hardware to default position
        // Set all hardware to default position
        verticalArmMotor.setPower(0);
        clawServo.setPower(0);


        // Set proper encoder state for all motor


//        // Set proper encoder state for all motor
//        frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }
}
