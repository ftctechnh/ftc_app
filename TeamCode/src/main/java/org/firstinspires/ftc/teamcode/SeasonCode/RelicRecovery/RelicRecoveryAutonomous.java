package org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.Utilities.UseVuforia;

/**
 * Created by Shane on 7/19/2017.
 */

@Autonomous(name = "Relic Recovery Autonomous", group = "Autonomous")
public class RelicRecoveryAutonomous extends RelicRecoveryHardware {

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
    }
}
