package org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.Utilities.UseVuforia;

/**
 * Created by Shane on 7/19/2017.
 */

@Autonomous(name = "Relic Recovery Autonomous", group = "Autonomous")
public class RelicRecoveryAutonomous extends RelicRecoveryHardware {

    /**
     * Encoders:
     * 60:1 1680
     * 40:1 1120
     * 20:1 560
     */
    UseVuforia v;

    @Override
    public void init() {
        super.init();
        v = new UseVuforia(hardwareMap,telemetry);
        v.init();
    }

    public void start() {
        v.start();
    }

    @Override
    public void loop() {
        super.loop();
        ssBallPusher.setPosition(BALL_PUSHER_DOWN);
        v.run();
        telemetry.addData("blue",color.blue());
        telemetry.addData("red",color.red());
    }
}
