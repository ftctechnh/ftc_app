package org.firstinspires.ftc.teamcode;

/*
 * Howdy! This class defines all the hardware, constants, and variables
 * that are called in the methodList!
 */

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

public class methodClass {

    /* Define hardward */
    DcMotor frontLeftMotor = null;
    DcMotor frontRightMotor = null;
    DcMotor backLeftMotor = null;
    DcMotor backRightMotor = null;
    DcMotor verticalArmMotor;
    DcMotor trayMotor;
    DcMotor P1Motor = null;
    DcMotor P2Motor = null;
    Servo gemServo;
    BNO055IMU imu;
    DigitalChannel topTouch;
    DigitalChannel bottomTouch;

    /* Give place holder values for the motor powers for the
    * setWheelPower method*/
    double FrontLeftPower = 0;
    double FrontRightPower = 0;
    double BackRightPower = 0;
    double BackLeftPower = 0;

    /*These are used in the setWheelPower method*/
    double frontLeft;
    double frontRight;
    double backLeft;
    double backRight;


}
