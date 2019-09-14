package org.firstinspires.ftc.teamcode.robot.mecanum;

import org.firstinspires.ftc.teamcode.common.math.MathUtil;
import org.firstinspires.ftc.teamcode.common.math.Pose;
import org.firstinspires.ftc.teamcode.robot.mecanum.MecanumUtil;
import org.firstinspires.ftc.teamcode.robot.mecanum.VirtualMecanumHardware;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VirtualMecanumHardwareTest {

    @Test
    void testVirtualMecanumSquare() {
        VirtualMecanumHardware virtualRobot = new VirtualMecanumHardware(new Pose(0, 0, 0));

        // Drive top left (+x +y)
        virtualRobot.setPowers(MecanumUtil.powersFromAngle(Math.PI/4, 1, 0));
        virtualRobot.elapse(2);
        // Ensure we're in Q1
        assertTrue(virtualRobot.pose().x > 0);
        assertTrue(virtualRobot.pose().y > 0);

        assertTrue(MathUtil.approxEquals(virtualRobot.pose().heading, 0));

        // Drive backwards and ensure we're in Q2
        virtualRobot.setPowers(MecanumUtil.BACKWARD);
        virtualRobot.elapse(2);
        assertTrue(virtualRobot.pose().x < 0);
        assertTrue(virtualRobot.pose().y > 0);
        assertTrue(MathUtil.approxEquals(virtualRobot.pose().heading, 0));

        // Drive right and ensure we're in Q3
        virtualRobot.setPowers(MecanumUtil.RIGHT);
        virtualRobot.elapse(2);
        assertTrue(virtualRobot.pose().x < 0);
        assertTrue(virtualRobot.pose().y < 0);
        assertTrue(MathUtil.approxEquals(virtualRobot.pose().heading, 0));

        // Drive right and ensure we're in Q4
        virtualRobot.setPowers(MecanumUtil.FORWARD);
        virtualRobot.elapse(2);
        assertTrue(virtualRobot.pose().x > 0);
        assertTrue(virtualRobot.pose().y < 0);
        assertTrue(MathUtil.approxEquals(virtualRobot.pose().heading, 0));

        // Drive left and ensure we're in Q1
        virtualRobot.setPowers(MecanumUtil.LEFT);
        virtualRobot.elapse(2);
        assertTrue(virtualRobot.pose().x > 0);
        assertTrue(virtualRobot.pose().y > 0);
        assertTrue(MathUtil.approxEquals(virtualRobot.pose().heading, 0));

        // Drive back right and ensure we're back where we started
        virtualRobot.setPowers(MecanumUtil.powersFromAngle(5*Math.PI/4, 1, 0));
        virtualRobot.elapse(2);
        // Ensure we're back at 0, 0 where we started
        assertTrue(MathUtil.approxEquals(virtualRobot.pose().x, 0));
        assertTrue(MathUtil.approxEquals(virtualRobot.pose().y, 0));
        assertTrue(MathUtil.approxEquals(virtualRobot.pose().heading, 0));
    }
}