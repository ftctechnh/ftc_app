package org.firstinspires.ftc.teamcode.opmodes.test;

import org.firstinspires.ftc.teamcode.general.action.LinearAction;
import org.firstinspires.ftc.teamcode.general.action.LoopAction;
import org.firstinspires.ftc.teamcode.opmodes.auto.RelicAutoBase;

import java.util.concurrent.Callable;

/**
 * Created by Derek on 2/17/2018.
 */

public class ColorServoTest extends RelicAutoBase {


    @Override
    public void loop() {
        super.loop();
        telemetry.addData("colorSensor","" + colorSensor.red() + ", "+ colorSensor.green() + ", " + colorSensor.blue());
    }

    @Override
    protected void addActions() {
        queue.add(new LinearAction(new Runnable() {
            @Override
            public void run() {
                colorServo.setPosition(0);
            }
        }));

        queue.add(new LinearAction(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }));

        queue.add(new LoopAction(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                colorServo.setPosition(1);
                return false;
            }
        }));

    }
}
