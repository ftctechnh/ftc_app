package org.firstinspires.ftc.teamcode.common.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LineTest {

    @Test
    void constructors() {
        // We correctly assign slope and intercept
        Line base = new Line(-1, 2);
        assertEquals(base.slope, -1);
        assertEquals(base.intercept, 2);
        assertFalse(base.isVertical());

        // We convert from point slope to slope intercept properly
        Point p1 = new Point(-1000, 1002);
        Point p2 = new Point(10000, -9998);
        Line pointSlope1 = new Line(p1, -1);
        Line pointSlope2 = new Line(p2, -1);
        assertEquals(pointSlope1, base);
        assertEquals(pointSlope2, base);
        assertFalse(pointSlope1.isVertical());
        assertFalse(pointSlope2.isVertical());

        // We convert from point-point properly
        Line pointPoint = new Line(p1, p2);
        assertEquals(pointPoint, base);
        assertFalse(pointPoint.isVertical());

        // We cannot create vertical line with slope y-intercept constructor
        assertThrows(IllegalArgumentException.class, () -> {
            new Line(Double.NEGATIVE_INFINITY, 0);
        });

        // Vertical lines with +infinity slope equal those with -infinity slope
        Line posVertical = new Line(new Point(5, 0), Double.NEGATIVE_INFINITY);
        Line negVertical = new Line(new Point(5, 0), Double.NEGATIVE_INFINITY);
        assertEquals(posVertical, negVertical);
        assertEquals(negVertical.slope, Double.NEGATIVE_INFINITY);
        assertTrue(posVertical.isVertical());
        assertTrue(negVertical.isVertical());

        // Vertical line can be created from two points
        Line twoPointVertical = new Line(new Point(-2, -2), new Point(-2, -4));
        assertEquals(twoPointVertical.slope, Double.NEGATIVE_INFINITY);
        assertTrue(twoPointVertical.isVertical());
    }

    @Test
    void perpendicularSlope() {
        Line posSlope = new Line(3, 5);
        Line negSlope = new Line(-3, -10924);
        Line horizontalLine = new Line(0, 2);
        Line verticalLine = new Line(new Point(5, 2), new Point(5, 4));

        assertEquals(posSlope.perpendicularSlope(), -1.0/3, MathUtil.EPSILON);
        assertEquals(negSlope.perpendicularSlope(), 1.0/3, MathUtil.EPSILON);
        assertEquals(horizontalLine.perpendicularSlope(), Double.NEGATIVE_INFINITY);
        assertEquals(verticalLine.perpendicularSlope(), 0);
    }

    @Test
    void evaluate() {
        // Simple case
        Line l1 = new Line(-3, 5);
        assertEquals(l1.evaluate(2), -1);
        assertEquals(l1.evaluate(-2), 11);

        // Vertical line isn't a function, can't be evaluated
        Line vertLine = new Line(new Point(0, 5), Double.NEGATIVE_INFINITY);
        assertThrows(IllegalArgumentException.class, () -> {
            vertLine.evaluate(0);
        });
    }

    @Test
    void intersect() {
        Line l1 = new Line(-3, 5);
        Line l2 = new Line(5, -8);

        // Intersection should be commutative
        assertEquals(l1.intersect(l2), l2.intersect(l1));

        // It should also be correct
        assertEquals(l1.intersect(l2), new Point(1.625, 0.125));

        // Parallel lines never cross
        Line p2 = new Line(-3, 6);
        assertThrows(IllegalArgumentException.class, () -> {
            p2.intersect(l1);
        });

        // Equal lines should always cross
        Line e2 = new Line(-3, 5);
        assertThrows(IllegalArgumentException.class, () -> {
            e2.intersect(l1);
        });

        // Vertical lines should be treated the same way
        Line v1 = new Line(new Point(5, 1), Double.NEGATIVE_INFINITY);
        Line v2 = new Line(new Point(2, 1), Double.POSITIVE_INFINITY);
        assertThrows(IllegalArgumentException.class, () -> {
            v1.intersect(v2);
        });
    }

    @Test
    void midpoint() {
        assertEquals(
                Line.midpoint(new Point(2, 2), new Point(4, 4)),
                new Point(3, 3));
    }

    @Test
    void distance() {
        assertEquals(
                Line.distance(new Point(-0.5, -1.25), new Point(2.5,2.75)),
                5);
    }
}