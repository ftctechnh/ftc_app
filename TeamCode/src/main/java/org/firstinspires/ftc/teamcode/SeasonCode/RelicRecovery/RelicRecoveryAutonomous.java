package org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery;

import android.support.annotation.RequiresPermission;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Utilities.ReadColor;
import org.firstinspires.ftc.teamcode.Utilities.UseVuforia;

/**
 * Created by Shane on 7/19/2017.
 */

@Autonomous(name = "Relic Recovery Autonomous", group = "Autonomous")
public class RelicRecoveryAutonomous extends RelicRecoveryHardware {

    UseVuforia useVuforia;
    ReadColor readColor;

    private boolean ifBlue;
    private int state = 0;
    private boolean ifDone = false;

    @Override
    public void init() {
        super.init();
        useVuforia = new UseVuforia(hardwareMap,telemetry);
        readColor = new ReadColor(color);
        useVuforia.init();
    }

    public void start() {
        useVuforia.start();
    }


    enum States {
        READING_VALUES ,
        HIT_JEWEL
    }
    enum Color {
        NEITHER,
        RED,
        BLUE
    }

    private States _state = States.READING_VALUES;

    @Override
    public void loop() {
        super.loop();

        switch(_state)
        {
            case READING_VALUES:
                ssBallPusher.setPosition(BALL_PUSHER_DOWN);
                if(useVuforia.run() && readColor.readColor()) {
                    _state = States.HIT_JEWEL;
                }
                break;
            case HIT_JEWEL:
                readColor.getColorDetected();
                break;
            default:
                telemetry.addData("Test", "Cry");
        }


        telemetry.addData("blue",color.blue());
        telemetry.addData("red",color.red());
        if (ifBlue) {
            ballRotatorPosition = BALL_ROTATE_LEFT;

        } else {
            ballRotatorPosition= BALL_ROTATE_RIGHT;
        }

    }

}
