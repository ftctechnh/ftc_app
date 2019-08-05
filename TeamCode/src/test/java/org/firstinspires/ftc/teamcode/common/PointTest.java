package org.firstinspires.ftc.teamcode.common;

import org.firstinspires.ftc.teamcode.common.math.Point;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PointTest {

    // We test the simple aspects of Point in PoseTest, among others

    @Test
    void rotated() {
        // 0, 0 cannot be rotated
        Point p1 = new Point(0, 0);
        assertEquals(p1, p1.rotated(100));

        // Rotation by 0 radians does not change point
        Point p2 = new Point(5, 3);
        assertEquals(p2, p2.rotated(0));

        // Rotating by round angles gives nice values
        assertEquals(p2.rotated(Math.PI), new Point(-5, -3));

        // Test common unit circle rotation
        Point p3 = new Point(-2, 0);
        assertEquals(p3.rotated(Math.PI / 4), new Point(-Math.sqrt(2), -Math.sqrt(2)));
    }
}