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
public class HardwareSteelheadMainBot {
    public DcMotor leftMotor_1      = null;
    public DcMotor leftMotor_2      = null;
    public DcMotor rightMotor_1     = null;
    public DcMotor rightMotor_2     = null;

    private String leftMotorName_1  = "leftMotor1";
    private String leftMotorName_2 = "leftMotor2";
    private String rightMotorName_1 = "rightMotor1";
    private String rightMotorName_2 = "rightMotor2";

    public void init(HardwareMap aHwMap) {

        leftMotor_1 = aHwMap.dcMotor.get(leftMotorName_1);
        leftMotor_2 = aHwMap.dcMotor.get(leftMotorName_2);

        rightMotor_1 = aHwMap.dcMotor.get(rightMotorName_1);
        rightMotor_2 = aHwMap.dcMotor.get(rightMotorName_2);

        leftMotor_1.setDirection(DcMotorSimple.Direction.FORWARD);
        leftMotor_2.setDirection(DcMotorSimple.Direction.FORWARD);

        rightMotor_1.setDirection(DcMotorSimple.Direction.FORWARD);
        rightMotor_2.setDirection(DcMotorSimple.Direction.FORWARD);

        hardwareLeftPower(0);
        hardwareRightPower(0);

        leftMotor_1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftMotor_2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightMotor_1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightMotor_2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void setLeftMotorName(String newName) {

        leftMotorName_1 = newName + "1";
        leftMotorName_2 = newName + "2";
    }

    public void setRightMotorName(String newName) {

        rightMotorName_1 = newName + "1";
        rightMotorName_2 = newName + "2";
    }

    public void hardwareLeftPower(double power) {
        leftMotor_1.setPower(power);
        leftMotor_2.setPower(power);
    }

    public void hardwareRightPower(double power) {
        rightMotor_1.setPower(power);
        rightMotor_2.setPower(power);
    }
}
