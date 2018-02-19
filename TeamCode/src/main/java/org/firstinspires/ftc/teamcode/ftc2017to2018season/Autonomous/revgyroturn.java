package org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Inspiration Team on 2/18/2018.
 */
@Autonomous(name = "revgyroturn")
public class revgyroturn extends Autonomous_General_George {

    @Override
    public void runOpMode() {
        initiate(false);
        waitForStart();
        gyroTurnREV(0.4,0);
        sleep(100);
        telemetry.addData("","gyro turn finished, starting wall align");
        telemetry.update();
        wallAlign(0.5,28, 0);//since the columns of the cryptobox are protruding,
        // the range sensor is actually using the distance from the protruding columns
        //the last value is 0 for the blue auto and 1 for the red auto
        sleep(200);
        telemetry.addData("", "wall align finished, starting gyro turn");
        telemetry.update();
        gyroTurnREV(0.5,-84);
    }
}
