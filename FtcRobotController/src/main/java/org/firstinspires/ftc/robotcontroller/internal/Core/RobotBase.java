package org.firstinspires.ftc.robotcontroller.internal.Core;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.hardware.HardwareMap;
/**
 *
 * Team 4964 Rolla Patriots
 *
 * Authors: Joel Schott and now Pranal Madria ;)
 * Created on 8/8/2018.
 */

public abstract class RobotBase {

    public RobotBase (final HardwareMap hwm, final LinearOpMode op){
        hardwareMap = hwm;
        opMode = op;
    }

    private HardwareMap hardwareMap;
    private LinearOpMode opMode;

    public HardwareMap getRobotHardwareMap(){
        return hardwareMap;
    }
    public LinearOpMode getRobotOpMode(){
        return opMode;
    }

    public abstract void stop();

}
