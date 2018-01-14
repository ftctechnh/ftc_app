package org.firstinspires.ftc.teamcode.GMR.Robot.SubSystems;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;

/**
 * Created by pston on 1/14/2018
 */

public class ColumnDetect {

    private ModernRoboticsI2cRangeSensor rangeSensor;

    public ColumnDetect(ModernRoboticsI2cRangeSensor rangeSensor) {
        this.rangeSensor = rangeSensor;
    }

    public boolean columnMove () {
        return false;
    }

}
