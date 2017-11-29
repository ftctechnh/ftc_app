package org.firstinspires.ftc.team9853.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.chathamrobotics.common.opmode.exceptions.StoppedException;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.team9853.opmodes.Autonomous9853;

/**
 * Created by carsonstorm on 11/28/2017.
 */

@Autonomous
public class GyroTest extends Autonomous9853 {
    public GyroTest() {super(false);}

    @Override
    public void run() throws InterruptedException, StoppedException {
        robot.rotateSync(180, AngleUnit.DEGREES);

        Thread.sleep(1000);

        robot.rotateSync(90, AngleUnit.DEGREES);

        Thread.sleep(1000);

        robot.rotateSync(270, AngleUnit.DEGREES);
    }
}
