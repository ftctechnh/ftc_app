package org.firstinspires.ftc.teamcode.SeasonCode.RelicRecoveryBeta;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Utilities.Map;
import org.firstinspires.ftc.teamcode.Utilities.SetRobot;

/**
 * Created by spmce on 11/26/2017.
 */

public abstract class DriveTrainHardware implements DriveTrain {
    Map      map      = null;
    SetRobot setRobot = null;
    DcMotor  mLeft    = null;
    DcMotor  mRight   = null;
    public double leftPower;
    public double rightPower;

    protected DriveTrainHardware() {
        leftPower = 0;
        rightPower = 0;
    }
}
