package org.firstinspires.ftc.teamcode.autonomous;

import org.firstinspires.ftc.teamcode.common.math.Pose;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openftc.revextensions2.RevBulkData;

import static org.junit.jupiter.api.Assertions.*;

class StandardTrackingWheelLocalizerTest {

    private StandardTrackingWheelLocalizer getNewLocalizer() {
        return new StandardTrackingWheelLocalizer(0, 1, 2);
    }

    private RevBulkData genFakeData(int leftEncoder, int rightEncoder, int latEncoder) {
        int[] encoderVals = new int[] {leftEncoder, rightEncoder, latEncoder};
        RevBulkData data = Mockito.mock(RevBulkData.class);
        Mockito.when(data.getMotorCurrentPosition(Mockito.anyInt()))
                .thenAnswer(invocation ->
                        encoderVals[invocation.getArgumentAt(0, Integer.class)]);
        return data;
    }

    double THRESHOLD = 0.1;

    private boolean roughApproxEquals(Pose p1, Pose p2) {
        return Math.abs(p1.x - p2.x) < THRESHOLD &&
                Math.abs(p1.y - p2.y) < THRESHOLD &&
                Math.abs(p1.heading - p2.heading) < THRESHOLD;
    }

    @Test
    void testTrackingWheelUpdate() {
        // Move forward 24 inches
        StandardTrackingWheelLocalizer straightLocalizer = getNewLocalizer();
        for (int i = 1; i <= 10; i++) {
            int distance = StandardTrackingWheelLocalizer.inchesToEncoderTicks(24 * i);
            straightLocalizer.update(genFakeData(distance, distance, 0));
        }
        // Because we're rounding inchesToEncoderTicks to an int, we'll usually be off by ~0.001
        assertTrue(roughApproxEquals(straightLocalizer.pose(), new Pose(24 * 10, 0, 0)));

        // Move left 24 inches
        int distance = StandardTrackingWheelLocalizer.inchesToEncoderTicks(24);
        StandardTrackingWheelLocalizer strafeLocalizer = getNewLocalizer();
        strafeLocalizer.update(genFakeData(0, 0, distance));
        assertTrue(roughApproxEquals(strafeLocalizer.pose(), new Pose(0, 24, 0)));

        // Spin in a circle degrees
        double spinAngle = Math.PI / 10;
        int leftSpinDist = StandardTrackingWheelLocalizer.inchesToEncoderTicks(
                StandardTrackingWheelLocalizer.LEFT_Y_POS * spinAngle);
        int rightSpinDist = StandardTrackingWheelLocalizer.inchesToEncoderTicks(
                StandardTrackingWheelLocalizer.RIGHT_Y_POS * spinAngle);
        int latSpinDist = StandardTrackingWheelLocalizer.inchesToEncoderTicks(
                -StandardTrackingWheelLocalizer.LAT_X_POS * spinAngle);

        StandardTrackingWheelLocalizer spinLocalizer = getNewLocalizer();
        for (int i = 1; i <= 100; i ++) {
            RevBulkData fakeSpinData = genFakeData(leftSpinDist*i, rightSpinDist*i, latSpinDist*i);
            spinLocalizer.update(fakeSpinData);
            assertTrue(roughApproxEquals(spinLocalizer.pose(), new Pose(0, 0, -i * spinAngle)));
        }
    }
}