package org.firstinspires.ftc.teamcode.GMR.Robot.SubSystems;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;

/**
 * Created by pston on 1/14/2018
 */

public class ColunmDetect {

    private ModernRoboticsI2cRangeSensor rangeSensor;

    public ColunmDetect (ModernRoboticsI2cRangeSensor rangeSensor) {
        this.rangeSensor = rangeSensor;
    }

    public boolean colunmMove () {
        return false;
    }

}
