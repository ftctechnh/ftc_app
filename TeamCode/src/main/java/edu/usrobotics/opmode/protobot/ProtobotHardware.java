package edu.usrobotics.opmode.protobot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import edu.usrobotics.opmode.BaseHardware;


/**
 * Created by dsiegler19 on 10/13/16.
 */
public class ProtobotHardware extends BaseHardware {

    public DcMotor frontRight;
    public DcMotor frontLeft;
    public DcMotor backRight;
    public DcMotor backLeft;

    //public DcMotor harvester;

    public Servo gate;

    public float gateClosedPosition = 0.3f;
    public float gateOpenedPosition = 1f;

    @Override
    public void getDevices() {

        frontRight = hardwareMap.dcMotor.get ("fr");
        frontLeft = hardwareMap.dcMotor.get ("fl");
        backRight = hardwareMap.dcMotor.get ("br");
        backLeft = hardwareMap.dcMotor.get ("bl");

        //harvester = hardwareMap.dcMotor.get("harvester");

        //harvester.setDirection(DcMotorSimple.Direction.REVERSE);

        gate = hardwareMap.servo.get("gate");

        gate.setPosition(gateClosedPosition);

    }
}
