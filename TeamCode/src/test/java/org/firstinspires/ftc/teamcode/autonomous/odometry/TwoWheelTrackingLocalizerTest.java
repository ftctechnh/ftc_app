package org.firstinspires.ftc.teamcode.autonomous.odometry;

import org.firstinspires.ftc.teamcode.common.math.Pose;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openftc.revextensions2.RevBulkData;

import static org.junit.jupiter.api.Assertions.*;

class TwoWheelTrackingLocalizerTest {

    private TwoWheelTrackingLocalizer getNewLocalizer() {
        return new TwoWheelTrackingLocalizer(0, 1);
    }

    private RevBulkData genFakeData(int parallelEncoder, int latEncoder) {
        int[] encoderVals = new int[] {parallelEncoder, latEncoder};
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
        TwoWheelTrackingLocalizer straightLocalizer = getNewLocalizer();
        for (int i = 1; i <= 10; i++) {
            int distance = StandardTrackingWheelLocalizer.inchesToEncoderTicks(24 * i);
            straightLocalizer.update(genFakeData(distance, 0), 0);
        }
        // Because we're rounding inchesToEncoderTicks to an int, we'll usually be off by ~0.001
        assertTrue(roughApproxEquals(straightLocalizer.pose(), new Pose(24 * 10, 0, 0)));

        // Move left 24 inches
        int distance = StandardTrackingWheelLocalizer.inchesToEncoderTicks(24);
        TwoWheelTrackingLocalizer strafeLocalizer = getNewLocalizer();
        strafeLocalizer.update(genFakeData(0, distance), 0);
        assertTrue(roughApproxEquals(strafeLocalizer.pose(), new Pose(0, 24, 0)));

        // Spin in a circle
        double spinAngle = Math.PI / 2;
        int parallelSpinDist = TwoWheelTrackingLocalizer.inchesToEncoderTicks(
                TwoWheelTrackingLocalizer.PARALLEL_Y_POS * spinAngle);
        int lateralSpinDist = TwoWheelTrackingLocalizer.inchesToEncoderTicks(
                -TwoWheelTrackingLocalizer.LATERAL_X_POS * spinAngle);

        TwoWheelTrackingLocalizer spinLocalizer = getNewLocalizer();
        for (int i = 1; i <= 100; i ++) {
            RevBulkData fakeSpinData = genFakeData(parallelSpinDist*i, lateralSpinDist*i);
            spinLocalizer.update(fakeSpinData, -i * spinAngle);
            assertTrue(roughApproxEquals(spinLocalizer.pose(), new Pose(0, 0, -i * spinAngle)));
        }

        // Track a square
        TwoWheelTrackingLocalizer squareLocalizer = getNewLocalizer();
        totalSquareParallel = 0;
        totalSquareLateral = 0;

        moveOneSquareLeg(squareLocalizer);
        assertTrue(roughApproxEquals(squareLocalizer.pose(), new Pose(24, 0, Math.PI/2)));
        moveOneSquareLeg(squareLocalizer);
        assertTrue(roughApproxEquals(squareLocalizer.pose(), new Pose(24, 24, Math.PI)));
        moveOneSquareLeg(squareLocalizer);
        assertTrue(roughApproxEquals(squareLocalizer.pose(), new Pose(0, 24, 3 * Math.PI / 2)));
        moveOneSquareLeg(squareLocalizer);
        assertTrue(roughApproxEquals(squareLocalizer.pose(), new Pose(0, 0, 2 * Math.PI)));

    }

    int totalSquareParallel;
    int totalSquareLateral;
    double totalHeading;

    private void moveOneSquareLeg(TwoWheelTrackingLocalizer localizer) {
        int distance = StandardTrackingWheelLocalizer.inchesToEncoderTicks(24);
        totalSquareParallel += distance;
        localizer.update(genFakeData(totalSquareParallel, totalSquareLateral), totalHeading);


        totalSquareParallel += TwoWheelTrackingLocalizer.inchesToEncoderTicks(
                -TwoWheelTrackingLocalizer.PARALLEL_Y_POS * Math.PI / 2);
        totalSquareLateral += TwoWheelTrackingLocalizer.inchesToEncoderTicks(
                TwoWheelTrackingLocalizer.LATERAL_X_POS * Math.PI / 2);
        totalHeading += Math.PI / 2;
        localizer.update(genFakeData(totalSquareParallel, totalSquareLateral), totalHeading);
    }
}