package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
//import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

//This class defines all the specific hardware for a the BACONbot robot.

public class ArmHardwareClass {

    /* Public OpMode members. */
    public DcMotor verticalArmMotor = null;
    public CRServo clawServo = null;


    /* Define values used in commanding the claw */
    static double clawClose = .5;
    static double clawOpen = -.5;
    static double clawStill = 0;

    double VerticalArmPower = 0;

    /* local OpMode members. */
    HardwareMap hwMap = null;
    private ElapsedTime period = new ElapsedTime();

    /* Constructor */
    public ArmHardwareClass() {

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        verticalArmMotor = hwMap.dcMotor.get("VAM");
        clawServo =  hwMap.crservo.get("CS");

        verticalArmMotor.setPower(0);
        clawServo.setPower(0);


    }
}
