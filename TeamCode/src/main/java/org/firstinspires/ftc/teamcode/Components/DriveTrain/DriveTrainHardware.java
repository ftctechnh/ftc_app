package org.firstinspires.ftc.teamcode.Components.DriveTrain;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Utilities.Map;
import org.firstinspires.ftc.teamcode.Utilities.SetRobot;

/**
 * Created by spmce on 11/26/2017.
 *
 * Abstract class for drive train Hardware
 * Contains left and right motors and variables
 */

public abstract class DriveTrainHardware implements DriveTrain {
    // ---------------------- Hardware Objects ----------------------
    Map      map;
    SetRobot setRobot;
    // ---------------------- Hardware Devices ----------------------
    DcMotor  mLeft;
    DcMotor  mRight;
    // --------------------- Hardware Variables ---------------------
    /**
     * this variable is used to set power to left motor
     */
    public double leftPower;
    /**
     * this variable is used to set power to right motor
     */
    public double rightPower;
    // ------------------------ Constructor -------------------------
    DriveTrainHardware() {
        map      = null;
        setRobot = null;
        mLeft    = null;
        mRight   = null;
        leftPower  = 0;
        rightPower = 0;
    }
}
