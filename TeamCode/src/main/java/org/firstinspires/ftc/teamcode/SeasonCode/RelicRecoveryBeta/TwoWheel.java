package org.firstinspires.ftc.teamcode.SeasonCode.RelicRecoveryBeta;

import org.firstinspires.ftc.teamcode.Utilities.Map;
import org.firstinspires.ftc.teamcode.Utilities.SetRobot;

/**
 * Created by spmce on 11/26/2017.
 */

public class TwoWheel extends DriveTrainHardware {

    TwoWheel(Map map, SetRobot setRobot) {
        this.map = map;
        this.setRobot = setRobot;
    }

    @Override
    public void initHardware() {
        mRight = map.motor("r");
        mLeft  = map.revMotor("l");
    }

    @Override
    public void runHardware() {
        setRobot.power(mRight,rightPower,"right motor");
        setRobot.power(mLeft,leftPower,"left motor");
    }
}
