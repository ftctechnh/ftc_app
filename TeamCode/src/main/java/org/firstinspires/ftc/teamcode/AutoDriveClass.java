package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

//This class defines all the specific hardware for a the BACONbot robot.

public class AutoDriveClass {

    /* local OpMode members. */
    public DcMotor Motor = null;


    /* Give place holder values for the motors and the grabber servo */
    double MotorPower = 0;



    /* local OpMode members. */
    HardwareMap hwMap = null;
    private ElapsedTime period = new ElapsedTime();

    /* Constructor */
    public AutoDriveClass() {

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Hardware

        // Define and Initialize Hardware
        Motor = hwMap.dcMotor.get("FL");

        // Set all hardware to default position
        // Set all hardware to default position
        Motor.setPower(0);


        // Set proper encoder state for all motor
        Motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


    }
}
