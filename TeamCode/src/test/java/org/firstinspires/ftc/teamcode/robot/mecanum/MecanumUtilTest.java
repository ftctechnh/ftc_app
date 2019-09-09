package org.firstinspires.ftc.teamcode.robot.mecanum;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MecanumUtilTest {

    @Test
    void deadZone() {
        // Ensure we don't cut off things above the deadzone
        assertEquals(MecanumUtil.deadZone(1.0, 0.5), 1.0);
        assertEquals(MecanumUtil.deadZone(0.8, 0.5), 0.8);
        assertEquals(MecanumUtil.deadZone(-0.6, 0.5), -0.6);

        // If it's right on the edge, we should hit the deadzone
        assertEquals(MecanumUtil.deadZone(0.5, 0.5), 0.0);
        assertEquals(MecanumUtil.deadZone(-0.5, 0.5), 0.0);

        // If it's under, we should hit the deadzone as well
        assertEquals(MecanumUtil.deadZone(0.2, 0.5), 0.0);
        assertEquals(MecanumUtil.deadZone(0.0, 0.5), 0.0);
        assertEquals(MecanumUtil.deadZone(-0.3, 0.5), 0.0);
    }

        /*
                 +x and heading=0
                     ^
                     |
            Quad I   |     Quad IV
                     |
                     |
                     |
+y and h pi/2 <--------------->
                     |
            Quad II  |     Quad III
                     |
                     |
                     |
         */


    @Test
    void powersFromAngle() {

        // Counterclockwise turn
        assertEquals(
                MecanumUtil.powersFromAngle(0, 0, 1),
                new MecanumPowers(-1, 1, -1, 1));

        // Clockwise turn
        assertEquals(
                MecanumUtil.powersFromAngle(0, 0, -1),
                new MecanumPowers(1, -1, 1, -1));

        // Forward movement
        assertEquals(
                MecanumUtil.powersFromAngle(0, 1, 0),
                new MecanumPowers(1, 1, 1, 1));

        // Top left (+x +y) diagonal movement
        assertEquals(
                MecanumUtil.powersFromAngle(-Math.PI/4, 1, 0),
                new MecanumPowers(0, 1, 1, 0));

        // Left movement
        assertEquals(
                MecanumUtil.powersFromAngle(-Math.PI/2, 1, 0),
                new MecanumPowers(-1, 1, 1, -1));

        // Bottom left (+x +y) diagonal movement
        assertEquals(
                MecanumUtil.powersFromAngle(-3 * Math.PI/4, 1, 0),
                new MecanumPowers(-1, 0, 0, -1));

        // Backward movement
        assertEquals(
                MecanumUtil.powersFromAngle(-Math.PI, 1, 0),
                new MecanumPowers(-1, -1, -1, -1));

        // Right movement
        assertEquals(
                MecanumUtil.powersFromAngle(-3 * Math.PI/2, 1, 0),
                new MecanumPowers(1, -1, -1, 1));

        // Basic scaling
        // Slow clockwise turn
        assertEquals(
                MecanumUtil.powersFromAngle(0, 0, -0.5),
                new MecanumPowers(0.5, -0.5, 0.5, -0.5));

        // Slow right movement
        assertEquals(
                MecanumUtil.powersFromAngle(-3 * Math.PI/2, 0.5, 0),
                new MecanumPowers(0.5, -0.5, -0.5, 0.5));
    }
}