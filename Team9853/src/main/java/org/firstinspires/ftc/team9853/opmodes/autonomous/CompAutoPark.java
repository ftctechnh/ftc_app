package org.firstinspires.ftc.team9853.opmodes.autonomous;

import org.chathamrobotics.common.opmode.AutonomousRnB;
import org.chathamrobotics.common.opmode.exceptions.StoppedException;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

/**
 * Created by carsonstorm on 11/18/2017.
 */

@AutonomousRnB
public class CompAutoPark extends CompAuto {
    public CompAutoPark(boolean isRedTeam) {super(isRedTeam);}

    @Override
    public void run() throws InterruptedException, StoppedException {
        super.run();
        if (! isHit) return;

        robot.driver.setDrivePower(isRedTeam() ? 0 : 180 , AngleUnit.DEGREES, 1, 0);

        Thread.sleep(1000);
        robot.driver.stop();
    }
}
