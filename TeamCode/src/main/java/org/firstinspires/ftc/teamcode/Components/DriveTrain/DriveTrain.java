package org.firstinspires.ftc.teamcode.Components.DriveTrain;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by spmce on 11/26/2017.
 *
 * Interface to map and set power to drive train
 */
public interface DriveTrain {

    /**
     * Maps DriveTrain to Phones
     */
    void initHardware();

    /**
     * Set Power to DriveTrain
     */
    void runHardware();
}
