package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;


/**
 * Container for the motors that control the overall motion of the robot;
 * i.e. the motors on the bottom.
 */
public class DriveBase {

    private final DcMotor left;
    private final DcMotor right;

    public DriveBase(HardwareMap map){
        //Initialize the motors. Robot configuration (through phones) should have "leftMotor"
        //and "rightMotor".
        left = map.get(DcMotor.class, "leftMotor");
        right = map.get(DcMotor.class, "rightMotor");
        //Reverse motors. Comment out if not necessary.
        left.setDirection(DcMotor.Direction.REVERSE);
        right.setDirection(DcMotor.Direction.REVERSE);
    }
    /** Set the power of the left motor. */
    public void setLeft(double power){
        left.setPower(power);
    }
    /** Set the power of the right motor. */
    public void setRight(double power){
        right.setPower(power);
    }
    /** Set the power of both the left and the right motor, each one its own value.*/
    public void set(double leftPower, double rightPower){
        setLeft(leftPower);
        setRight(rightPower);
    }
    /** Set the power of both the left and the right motor, both the same value.*/
    public void set(double power){
        set(power, power);
    }
    /** Set the power of both motors to 0.*/
    public void stop(){
        set(0);
    }

}
