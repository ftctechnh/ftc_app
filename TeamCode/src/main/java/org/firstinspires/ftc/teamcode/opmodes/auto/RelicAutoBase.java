package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.general.action.RobotAction;
import org.firstinspires.ftc.teamcode.opmodes.base.RelicBase;

import java.util.LinkedList;
import java.util.Queue;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;

/**
 * Created by Derek on 2/13/2018.
 * Basis for all Relic Recovery Autonomous OpModes
 */

@SuppressWarnings("WeakerAccess")
@Autonomous(name = "RelicAutoBase",group = "Relic")
public class RelicAutoBase extends RelicBase {
    protected Queue<RobotAction> queue = new LinkedList<>();

    protected void addActions() {

    }

    @Override
    public void init() {
        super.init();
        drivetrain.setRunMode(STOP_AND_RESET_ENCODER);

        addActions();

        while (drivetrain.isBusy()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
                // :(
            }
        }


        telemetry.addLine("Ready");
    }

    @Override
    public void start() {
        telemetry.clearAll();
    }

    @Override
    public void loop() {
        super.loop();
        try {
            if (queue.peek().run()) queue.poll();
        } catch (NullPointerException n) {
            queue.poll();
            super.stop();
            n.printStackTrace();
        }
    }
}
