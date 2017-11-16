package org.firstinspires.ftc.team9853.opmodes.autonomous;

import org.chathamrobotics.common.opmode.AutonomousRnB;
import org.chathamrobotics.common.opmode.AutonomousTemplate;
import org.chathamrobotics.common.opmode.exceptions.StoppedException;
import org.chathamrobotics.common.robot.RobotFace;
import org.chathamrobotics.common.utils.RGBAColor;
import org.firstinspires.ftc.team9853.Robot9853;

/**
 * Created by carsonstorm on 11/16/2017.
 */

public class CompAuto extends AutonomousTemplate<Robot9853> {
    public CompAuto(boolean isRedTeam) {
        super(isRedTeam);
    }

    @Override
    public void initialize() {
        robot = Robot9853.build(this);
        robot.init();
    }

    @Override
    public void run() throws InterruptedException, StoppedException {
        robot.driver.setFront(RobotFace.BACK);
        robot.jewelDisplacer.drop();

        RGBAColor color = robot.jewelDisplacer.getColor();
        robot.log.debug("Red", color.red());
        robot.log.debug("Blue", color.blue());
        robot.log.debug("Green", color.green());

        if (color.red() > color.blue() && isRedTeam()) {
            robot.driver.setDrivePower(0, 0, -1);
        } else {
            robot.driver.setDrivePower(0, 0, 1);
        }

        long endTime = System.currentTimeMillis() + 1000 / 4;
        while (System.currentTimeMillis() < endTime) {
            Thread.sleep(10);
        }
        robot.driver.stop();
    }
}
