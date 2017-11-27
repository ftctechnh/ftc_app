package org.firstinspires.ftc.teamcode.Components.DriveTrain;

import org.firstinspires.ftc.teamcode.Utilities.Map;
import org.firstinspires.ftc.teamcode.Utilities.SetRobot;

/**
 * Created by spmce on 11/26/2017.
 *
 * This drive train is used for robots with a two motor drive train
 */
public class TwoMotor extends DriveTrainHardware {
    // ------------------------ Constructor -------------------------
    /**
     * Constructs a two motor drive train
     *
     * @param map object that is used to map the drive train
     * @param setRobot object that is used to set the power to the drive train
     */
    public TwoMotor(Map map, SetRobot setRobot) {
        this.map = map;
        this.setRobot = setRobot;
    }
    // -------------------------- Mapping ---------------------------
    /**
     * Maps left and right motor of drive train
     */
    @Override
    public void initHardware() {
        mLeft  = map.revMotor("l");
        mRight = map.motor("r");
    }
    // --------------------- Set Hardware Power ---------------------
    /**
     * Sets power to right and left motor of drive train
     */
    @Override
    public void runHardware() {
        setRobot.power(mLeft,leftPower,"left motor");
        setRobot.power(mRight,rightPower,"right motor");
    }
}
