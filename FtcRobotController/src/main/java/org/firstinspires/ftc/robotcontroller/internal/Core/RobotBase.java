package org.firstinspires.ftc.robotcontroller.internal.Core;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 *
 * Team 4964 Rolla Patriots
 *
 * Authors: Joel Schott
 * Created on 8/8/2018.
 */

public abstract class RobotBase {

    public RobotBase (final HardWareMap hwm, final LinearOpMode op){
        hardWareMap = hwm;
        opmode = op;
    }

    private HardWareMap hardWareMap;
    private LinearOpMode opmode;
    private Telemetry tele;

    public abstract void stop();

}
