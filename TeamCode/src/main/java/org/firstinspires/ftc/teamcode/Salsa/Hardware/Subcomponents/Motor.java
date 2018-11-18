package org.firstinspires.ftc.teamcode.Salsa.Hardware.Subcomponents;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorImpl;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by adityamavalankar on 11/5/18.
 */

public abstract class Motor implements DcMotor {

    /** This is a slightly modified version of the {com.qualcomm.robotcore.hardware.DcMotor} class
     *  It is meant to interface better with the object oriented nature of the Team Salsa code
     *  In this case, all of the functions are inherited from the {com.qualcomm.robotcore.hardware.DcMotor} class
     *  The init() function here is meant to HardwareMap() more effectively, without much extra work
     */

    public HardwareMap hwmap;
    private DcMotor dcm;

    public void init(String hardwareName, HardwareMap inputMap) {

        hwmap = inputMap;

        this.dcm = hwmap.get(DcMotor.class, hardwareName);
    }

}
