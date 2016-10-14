package edu.usrobotics.opmode.protobot;

import com.qualcomm.robotcore.hardware.DcMotor;

import edu.usrobotics.opmode.BaseHardware;

/**
 * Created by dsiegler19 on 10/13/16.
 */
public class ProtobotHardware extends BaseHardware {

    public DcMotor frontRight;
    public DcMotor frontLeft;
    public DcMotor backRight;
    public DcMotor backLeft;

    @Override
    public void getDevices() {
        frontRight = hardwareMap.dcMotor.get ("fr");
        frontLeft = hardwareMap.dcMotor.get ("fl");
        backRight = hardwareMap.dcMotor.get ("br");
        backLeft = hardwareMap.dcMotor.get ("bl");
    }
}
