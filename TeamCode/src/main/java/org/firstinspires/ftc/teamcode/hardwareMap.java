package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class hardwareMap
{
    //Initializes Motor variables in hardware class
    public DcMotor leftFront = null;
    public DcMotor rightFront = null;
    public DcMotor leftBack = null;
    public DcMotor rightBack = null;

    //Initializes RunMode variable for Dc motors so that they can be changed in OpMode class
    private  DcMotor.RunMode initialMode = null;

    //Initializes hardware map variable
    HardwareMap map = null;

    public hardwareMap(DcMotor.RunMode enteredMode)
    {
        initialMode = enteredMode;
    }

    public void init(HardwareMap aMap)
    {
        map = aMap;

        //Maps DC motor variables to configuration
        leftFront  = map.dcMotor.get("leftFront");
        rightFront = map.dcMotor.get("rightFront");
        leftBack  = map.dcMotor.get("leftBack");
        rightBack = map.dcMotor.get("rightBack");

        //Encoder mode set
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //Sets DC mode to what is referenced in OpMode Code if you want to not use encoders, etc.
        leftFront.setMode(initialMode);
        rightFront.setMode(initialMode);
        leftBack.setMode(initialMode);
        rightBack.setMode(initialMode);

        //Sets DC directions
        leftFront.setDirection(DcMotorSimple.Direction.FORWARD);
        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.FORWARD);
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);

        //Makes Sure Robot is not moving at all when class called
        stopRobot();

    }
    public void stopRobot()
    {
        //Sets all DC power to 0
        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);
    }
}
