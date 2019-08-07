package org.firstinspires.ftc.teamcode.autonomous;

import org.firstinspires.ftc.teamcode.common.math.Point;
import org.firstinspires.ftc.teamcode.common.math.Pose;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PurePursuitControllerTest {

    @Test
    void arcCenterFromStartTangentPointAndEndpoint() {
        // Unit circle test - center should be right on origin
        Pose start = new Pose(1, 0, Math.PI/2);
        Point center = new Point(0, 0);
        Point[] ends = {
                new Point(-1, 0),
                new Point(0, 1),
                new Point(Math.sqrt(2)/2, Math.sqrt(2)/2),
                new Point(-Math.sqrt(3)/2, -1.0/2),
        };
        for (Point endpoint : ends) {
            assertEquals(PurePursuitController.
                    arcCenterFromStartTangentPointAndEndpoint(start, endpoint), center);
        }

        // Now a more challenging problem
        Point center2 = new Point(5, -2);
        Pose start2 = new Pose(5 - Math.sqrt(2)/2, Math.sqrt(2)/2 - 2, Math.PI/4);
        Point[] ends2 = {
                new Point(5, -1),
                new Point(6, -2)
        };
        for (Point endpoint2 : ends2) {
            assertEquals(PurePursuitController.
                    arcCenterFromStartTangentPointAndEndpoint(start2, endpoint2), center2);
        }
    }

    @Test
    void pointPoseMinAngle() {
        Pose h1 = new Pose(5, 4, 0);
        Point p1 = new Point(5, 100);
        Point p2 = new Point(5, -100);
        assertEquals(PurePursuitController.pointPoseMinAngle(h1, p1), Math.PI/2);
        assertEquals(PurePursuitController.pointPoseMinAngle(h1, p2), -Math.PI/2);
    }

    @Test
    void goToPosition() {
    }
}