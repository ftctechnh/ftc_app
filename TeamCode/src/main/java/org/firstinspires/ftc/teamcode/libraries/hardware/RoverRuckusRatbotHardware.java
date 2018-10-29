package org.firstinspires.ftc.teamcode.libraries.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by bremm on 10/26/18.
 */

public class RoverRuckusRatbotHardware {
    public DcMotor flDrive   = null;
    public DcMotor rlDrive   = null;
    public DcMotor frDrive  = null;
    public DcMotor rrDrive   = null;

    HardwareMap hwMap = null;

    public void init (HardwareMap ahwMap) {
        hwMap = ahwMap;

        flDrive = hwMap.get(DcMotor.class, "flDrive");
        rlDrive = hwMap.get(DcMotor.class, "rlDrive");
        frDrive = hwMap.get(DcMotor.class, "frDrive");
        rrDrive = hwMap.get(DcMotor.class, "rrDrive");

        flDrive.setDirection(DcMotor.Direction.REVERSE);
        rlDrive.setDirection(DcMotor.Direction.REVERSE);;

        frDrive.setDirection(DcMotor.Direction.FORWARD);;
        rrDrive.setDirection(DcMotor.Direction.FORWARD);;

        flDrive.setPower(0);
        rlDrive.setPower(0);
        frDrive.setPower(0);
        rrDrive.setPower(0);

        flDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rlDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rrDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
}
