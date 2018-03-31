package org.firstinspires.ftc.teamcode.ftc2017to2018season.Testing_and_Calibrations.Extends_Autonomous_General;

import org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous.Autonomous_General_George_;

/**
 * Created by Inspiration Team on 1/21/2018.
 */
//@Autonomous(name = "gyroTurnREV_final")
public class GyroTurnREV extends Autonomous_General_George_ {

    @Override
    public void runOpMode() {
        initiate(false);

        waitForStart();
        gyroTurnREV(0.5,90);
    }

}
