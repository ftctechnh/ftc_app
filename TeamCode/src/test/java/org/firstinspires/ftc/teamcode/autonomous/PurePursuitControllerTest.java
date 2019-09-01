package org.firstinspires.ftc.teamcode.autonomous;

import org.firstinspires.ftc.teamcode.common.math.MathUtil;
import org.firstinspires.ftc.teamcode.common.math.Point;
import org.firstinspires.ftc.teamcode.common.math.Pose;
import org.firstinspires.ftc.teamcode.robot.sixwheel.SixWheelHardware;
import org.firstinspires.ftc.teamcode.robot.sixwheel.SixWheelPowers;
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
        // Robot shouldn't move if it's already at its target position
        SixWheelPowers atTarget = PurePursuitController.goToPosition(new Pose(0, 0, 0), new Point(0, 0));
        //assertEquals(atTarget.left, 0);
        //assertEquals(atTarget.right, 0);
        //assertEquals(atTarget, new SixWheelPowers(0, 0));


        // Backing up and going forward
        SixWheelPowers backup = PurePursuitController.goToPosition(new Pose(10, 0, 0), new Point(0, 0));
        assertEquals(backup.left, backup.right, MathUtil.EPSILON);
        assertTrue(backup.left < 0);

        SixWheelPowers forward = PurePursuitController.goToPosition(new Pose(-10, 0, 0), new Point(0, 0));
        assertEquals(forward.left, forward.right, MathUtil.EPSILON);
        assertTrue(forward.left > 0);
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

        // Counter-clockwise and clockwise in all quadrants
        double[][] cases = {
              // x,  y,  h, x dir, turn dir (1 = counterclockwise)
                {20, 20, 0, -1, 1},
                {20, 20, Math.PI/2, -1, -1},
                {20, 20, Math.PI, 1, 1},
                {20, 20, Math.PI * 1.5, 1, -1},

                {-20, 20, 0, 1, -1},
                {-20, 20, Math.PI/2, -1, 1},
                {-20, 20, Math.PI, -1, -1},
                {-20, 20, Math.PI * 1.5, 1, 1},

                {-20, -20, 0, 1, 1},
                {-20, -20, Math.PI/2, 1, -1},
                {-20, -20, Math.PI, -1, 1},
                {-20, -20, Math.PI * 1.5, -1, -1},

                {20, -20, 0, -1, -1},
                {20, -20, Math.PI/2, 1, 1},
                {20, -20, Math.PI, 1, -1},
                {20, -20, Math.PI * 1.5, -1, 1},
        };

        for (int i = 0; i < 16; i++) {
            double[] p = cases[i];
            //double d = PurePursuitController.goToPositionDebug(new Pose(p[0], p[1], p[2]), new Point(0, 0));
            System.out.println("Corrcheck: " + " | " + p[3] + " | " + p[4]);

            //System.out.println("Checking case " + (i + 1));
            SixWheelPowers powers = PurePursuitController.goToPosition(new Pose(p[0], p[1], p[2]), new Point(0, 0));

            // Assert we're traveling forward/backward correctly
            assertTrue(powers.left * p[3] > 0);
            assertTrue(powers.right * p[3] > 0);

            // Assert that we're turning correctly
            if (p[4] == 1) {
                // Assert counterclockwise,
                assertTrue(powers.right > powers.left);
            } else {
                // Assert clockwise
                assertTrue(powers.left > powers.right);
            }
        }
    }
}