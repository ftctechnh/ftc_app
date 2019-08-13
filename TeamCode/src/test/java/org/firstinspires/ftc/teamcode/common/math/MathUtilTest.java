package org.firstinspires.ftc.teamcode.common.math;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

class MathUtilTest {

    @Test
    void angleWrap() {
        // Standard cases
        assertEquals(MathUtil.angleWrap(Math.PI * 4.5), Math.PI / 2, MathUtil.EPSILON);
        assertEquals(MathUtil.angleWrap(Math.PI * 5.5), -Math.PI / 2, MathUtil.EPSILON);

        // Works for large numbers
        assertEquals(MathUtil.angleWrap(Math.PI * 1000000), 0, MathUtil.EPSILON);
    }

    @Test
    void clamp() {
        // Upper and lower bounds work normally
        assertEquals(MathUtil.clamp(1.25), 1.0);
        assertEquals(MathUtil.clamp(-5.5), -1.0);

        // Works for large and infinite numbers
        assertEquals(MathUtil.clamp(Double.MAX_VALUE), 1.0);
        assertEquals(MathUtil.clamp(Double.NEGATIVE_INFINITY), -1.0);

        // Doesn't change values in range
        assertEquals(MathUtil.clamp(0.10118304), 0.10118304);
        assertEquals(MathUtil.clamp(-0.8572), -0.8572);
    }

    @Test
    void deadZone() {
        assertEquals(MathUtil.deadZone(0.02, 0.2), 0);
        assertEquals(MathUtil.deadZone(0.3, 0.2), 0.3);

        assertEquals(MathUtil.deadZone(-0.02, 0.2), 0);
        assertEquals(MathUtil.deadZone(-0.3, 0.2), -0.3);
    }

    @Test
    void relativeOdometryUpdate() {
    }

    @Test
    void approxEquals() {
        assertTrue(MathUtil.approxEquals(0, 1e-8));
        assertFalse(MathUtil.approxEquals(0, 1e-2));
    }

    @Test
    void lineSegmentCircleIntersection() {
        // Simple case with circle at (0, 0) and line segment with slope 1 through origin
        Point[] expected = {
                new Point(Math.sqrt(2), Math.sqrt(2)),
                new Point(-Math.sqrt(2), -Math.sqrt(2))
        };

        assertEquals(
                MathUtil.lineSegmentCircleIntersection(
                        new Point(-5, -5),
                        new Point(5, 5),
                        new Point(0, 0),
                        2),
                new TreeSet<>(Arrays.asList(expected))
        );
    }
}