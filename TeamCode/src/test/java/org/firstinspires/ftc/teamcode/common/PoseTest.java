package org.firstinspires.ftc.teamcode.common;

import org.firstinspires.ftc.teamcode.common.math.Pose;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PoseTest {

    @Test
    void add() {
        Pose p1 = new Pose(5, 3, -2);
        // Heading is large and negative to ensure wrapping does not occur
        Pose p2 = new Pose(5, 3, -3000);
        Pose p3 = new Pose(5, 2, -2);

        // Unequal poses should be unequal regardless of which param is different
        assertNotEquals(p1, p2);
        assertNotEquals(p1, p3);

        // Addition should work and be commutative
        Pose sum = p1.add(p2);
        Pose sum2 = p2.add(p1);

        assertEquals(sum, sum2);
        assertEquals(sum, new Pose(10, 6, -3002));
    }
}