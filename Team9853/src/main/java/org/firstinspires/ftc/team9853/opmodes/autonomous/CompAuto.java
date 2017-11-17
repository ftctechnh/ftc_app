package org.firstinspires.ftc.team9853.opmodes.autonomous;

import org.chathamrobotics.common.opmode.AutonomousRnB;
import org.chathamrobotics.common.opmode.AutonomousTemplate;
import org.chathamrobotics.common.opmode.exceptions.StoppedException;
import org.chathamrobotics.common.robot.RobotFace;
import org.chathamrobotics.common.utils.RGBAColor;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
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
        String result = "";

        robot.start();
        robot.driver.setFront(RobotFace.BACK);
        robot.jewelDisplacer.drop();
        debug();

        Thread.sleep(1000);

        RGBAColor color = robot.jewelDisplacer.getColor();
        robot.log.debug("Red", color.red());
        robot.log.debug("Blue", color.blue());
        robot.log.debug("Green", color.green());

        if (color.red() > color.blue()) result = "Red Left, Blue Right";
        else if (color.blue() > color.red()) result = "Blue Left, Red Right";
        else result = "Juul location unknown";

//        if (color.red() > color.blue() && isRedTeam()) {
//            robot.driver.setDrivePower(0, 0, -1);
//        } else {
//            robot.driver.setDrivePower(0, 0, 1);
//        }

        if ((color.red() > color.blue() && isRedTeam()) || (color.blue() > color.red() && ! isRedTeam()))
            robot.driver.setDrivePower(0, 0, -1);
        else
            robot.driver.setDrivePower(0, 0, 1);
        debug();

        Thread.sleep(1000/4);
        robot.driver.stop();

        robot.jewelDisplacer.raise();
        robot.gyroManager.rotate(90, robot.driver);
        robot.driver.setDrivePower(0 , AngleUnit.DEGREES, 1, 0);

        Thread.sleep(1000);
        robot.driver.stop();
    }
}
