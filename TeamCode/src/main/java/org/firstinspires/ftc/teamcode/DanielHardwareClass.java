package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

//import com.qualcomm.robotcore.hardware.Servo;

//This class defines all the specific hardware for a the BACONbot robot.

public class DanielHardwareClass {
    /* Public OpMode members. */
    public DcMotor frontRightMotor = null;
    public DcMotor backLeftMotor = null;
    public DcMotor backRightMotor = null;
    public DcMotor verticalArmMotor = null;
    public CRServo clawServo = null;
    public Servo clawServo2 = null;

    /* Define values used in code*/
    static final double OPEN     =  -.5;
    static final double CLOSE     =  0;
    static final double STOP     =  .5;

    /* Give place holder values for the motors and the grabber servo */
    double FrontRightPower = 0;
    double BackRightPower = 0;
    double BackLeftPower = 0;
    double VerticalArmPower = 0;

    /* local OpMode members. */
    HardwareMap hwMap = null;
    private ElapsedTime period = new ElapsedTime();

    /* Constructor */
    public DanielHardwareClass() {

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;


        // Define and Initialize Hardware;
        frontRightMotor = hwMap.dcMotor.get("FR");
        backLeftMotor = hwMap.dcMotor.get("BL");
        backRightMotor = hwMap.dcMotor.get("BR");
        verticalArmMotor = hwMap.dcMotor.get("VAM");
        clawServo =  hwMap.crservo.get("CS");
        clawServo2 = hwMap.servo.get("S");
        // Set all hardware to default position
        // Set all hardware to default position
        frontRightMotor.setPower(0);
        backLeftMotor.setPower(0);
        backRightMotor.setPower(0);
        verticalArmMotor.setPower(0);
        clawServo.setPower(0);
        clawServo2.setPosition(0);


    }
}
