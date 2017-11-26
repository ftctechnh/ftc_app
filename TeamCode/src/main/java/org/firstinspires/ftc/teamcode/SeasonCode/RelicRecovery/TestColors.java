package org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.Utilities.ReadColor;
import org.firstinspires.ftc.teamcode.Utilities.SetRobot;

/**
 * Created by lsatt on 11/25/2017.
 */

@Autonomous(name = "Relic Recovery Test", group = "Autonomous")
public class TestColors extends RelicRecoveryAutoMeth {
    ReadColor readColor;
    SetRobot setRobot;
    @Override
    public void init() {
        super.init();
        readColor = new ReadColor(sColor);
        setRobot = new SetRobot(telemetry);
    }

    @Override
    public void loop() {
        super.loop();
        ballPusherPosition = BALL_PUSHER_DOWN;
        setRobot.position(ssBallPusher,ballPusherPosition,"ball pusher");
        telemetry.addData("blue",sColor.blue());
        telemetry.addData("red",sColor.red());
    }
}
