package org.steelhead.ftc;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by Alec Matthews on 9/18/16.
 * Added this class so we don't have to
 * edit a sample one.
 *
 */
public class HardwareSteelheadTestBot {
    public DcMotor leftMotor      = null;
    public DcMotor rightMotor     = null;

    private String leftMotorName  = "leftMotor";
    private String rightMotorName = "rightMotor";

    public void init(HardwareMap aHwMap) {

        leftMotor = aHwMap.dcMotor.get(leftMotorName);
        rightMotor = aHwMap.dcMotor.get(rightMotorName);

        leftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        rightMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        leftMotor.setPower(0);
        rightMotor.setPower(0);

        leftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void setLeftMotorName(String newName) {
        leftMotorName = newName;
    }

    public void setRightMotorName(String newName) {
        rightMotorName = newName;
    }
}
