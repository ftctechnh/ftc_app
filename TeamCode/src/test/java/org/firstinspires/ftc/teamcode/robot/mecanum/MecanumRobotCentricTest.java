package org.firstinspires.ftc.teamcode.robot.mecanum;

import org.firstinspires.ftc.simulator.SimulatedOpModeFactory;
import org.firstinspires.ftc.teamcode.common.math.MathUtil;
import org.firstinspires.ftc.teamcode.common.math.Pose;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class MecanumRobotCentricTest {

    @Test
    void opModeTest() {
        SimulatedOpModeFactory simOpMode = new SimulatedOpModeFactory(MecanumRobotCentric.class);
        simOpMode.opMode.start();

        // At start, our position should be 0, 0 after waiting a bit
        simOpMode.elapseCycles(10, 100);
        assertEquals(simOpMode.robot.pose(), new Pose(0, 0, 0));
        assertEquals(simOpMode.robot.wheelPowers, new MecanumPowers(0,0, 0, 0));

        // Turning the robot works correctly
        simOpMode.opMode.gamepad1.right_stick_x = 1f; // All the way to the left, so turning counterclockwise
        simOpMode.elapseCycles(1, 100);
        assertTrue(simOpMode.robot.pose().heading > 0);
        simOpMode.opMode.gamepad1.right_stick_x = -1f; // All the way to the right
        simOpMode.elapseCycles(2, 100);
        assertTrue(simOpMode.robot.pose().heading < 0);
        simOpMode.robot.position = new Pose(0, 0, 0); // Move us back to start

        // Driving the robot works correctly
        simOpMode.opMode.gamepad1.right_stick_x = 0;
        simOpMode.opMode.gamepad1.left_stick_y = 1f;
        simOpMode.elapseCycles(1, 100);
        assertTrue(simOpMode.robot.pose().x > 0);
        assertEquals(0, simOpMode.robot.pose().y, MathUtil.EPSILON);
        assertEquals(0, simOpMode.robot.pose().heading, MathUtil.EPSILON);

        // Now go backwards
        simOpMode.opMode.gamepad1.left_stick_y = -1f;
        simOpMode.elapseCycles(2, 100);
        assertTrue(simOpMode.robot.pose().x < 0);
        simOpMode.robot.position = new Pose(0, 0, 0); // Move us back to start

        // Now left
        simOpMode.opMode.gamepad1.left_stick_x = 1f;
        simOpMode.elapseCycles(1, 100);
        assertTrue(simOpMode.robot.pose().y > 0);

        // Now diagonally up and right
        simOpMode.opMode.gamepad1.left_stick_x = -1f;
        simOpMode.opMode.gamepad1.left_stick_y = 1f;
        simOpMode.elapseCycles(2, 100);
        assertTrue(simOpMode.robot.pose().x > 0);
        assertTrue(simOpMode.robot.pose().y < 0);

        // Now ensure we're not in field centric
        simOpMode.robot.position = new Pose(0, 0, Math.PI/2); // Move us back to start
        simOpMode.opMode.gamepad1.left_stick_x = 0;
        simOpMode.opMode.gamepad1.left_stick_y = 1f;
        simOpMode.elapseCycles(1, 100);
        assertEquals(0, simOpMode.robot.pose().x, MathUtil.EPSILON);
        assertTrue(simOpMode.robot.pose().y > 0);
    }
}