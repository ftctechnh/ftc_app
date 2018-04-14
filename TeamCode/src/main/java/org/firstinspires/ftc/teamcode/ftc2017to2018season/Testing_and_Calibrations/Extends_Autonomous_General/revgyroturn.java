package org.firstinspires.ftc.teamcode.ftc2017to2018season.Testing_and_Calibrations.Extends_Autonomous_General;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous.Autonomous_General_George_;

/**
 * Created by Inspiration Team on 2/18/2018.
 */
@Autonomous(name = "revgyroturn")
@Disabled
public class revgyroturn extends Autonomous_General_George_ {

    @Override
    public void runOpMode() {
        initiate(false);
        waitForStart();
        gyroTurnREV(0.4,0, 10);
        sleep(100);
        telemetry.addData("","gyro turn finished, starting wall align");
        telemetry.update();
        wallAlign(0.5,28, 0);//since the columns of the cryptobox are protruding,
        // the range sensor is actually using the distance from the protruding columns
        //the last value is 0 for the blue auto and 1 for the red auto
        sleep(200);
        telemetry.addData("", "wall align finished, starting gyro turn");
        telemetry.update();
        gyroTurnREV(0.5,-84, 10);
    }
}
