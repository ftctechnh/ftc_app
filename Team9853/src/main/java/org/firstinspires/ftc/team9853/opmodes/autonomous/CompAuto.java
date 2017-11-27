package org.firstinspires.ftc.team9853.opmodes.autonomous;

import org.chathamrobotics.common.opmode.AutonomousRnB;
import org.chathamrobotics.common.opmode.AutonomousTemplate;
import org.chathamrobotics.common.opmode.exceptions.StoppedException;
import org.chathamrobotics.common.robot.RobotFace;
import org.chathamrobotics.common.RGBAColor;
import org.firstinspires.ftc.team9853.Robot9853;

/**
 * Created by carsonstorm on 11/16/2017.
 */

@AutonomousRnB
public class CompAuto extends AutonomousTemplate<Robot9853> {
    protected boolean isHit;

    public CompAuto(boolean isRedTeam) {
        super(isRedTeam);
    }

    @Override
    public void setup() {
        robot = Robot9853.build(this);
        robot.init();
    }

    @Override
    public void run() throws InterruptedException, StoppedException {

        robot.start();
        robot.driver.setFront(RobotFace.BACK);
        robot.jewelDisplacer.drop();
        debug();

        Thread.sleep(1000);

        RGBAColor color = robot.jewelDisplacer.getColor();
        robot.log.debug("Red", color.red());
        robot.log.debug("Blue", color.blue());
        robot.log.debug("Green", color.green());

        if (isTeamColor(color))
            hitJewel(-1);
        else if (color.red() == color.blue()) return;
        else
            hitJewel(1);
    }

    private void hitJewel(double power) throws InterruptedException, StoppedException {
        robot.driver.setDrivePower(0, 0, power);
        debug();

        Thread.sleep(1000/4);
        robot.driver.stop();

        robot.jewelDisplacer.raise();
        Thread.sleep(1000);

        robot.driver.setDrivePower(0, 0, -power);
        debug();

        Thread.sleep(1000/4);
        robot.driver.stop();

        isHit = true;
    }

    private boolean isRed(RGBAColor color) {
        return color.red() > color.blue();
    }

    private boolean isBlue(RGBAColor color) {
        return color.blue() > color.red();
    }

    private boolean isTeamColor(RGBAColor color) {
        if (isRedTeam()) return isRed(color);

        return isBlue(color);
    }
}
