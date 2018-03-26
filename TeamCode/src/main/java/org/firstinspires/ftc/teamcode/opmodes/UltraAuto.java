package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.libraries.VuforiaBallLib;

/**
 * Created by Noah on 3/25/2018.
 * Attempt at moving the cryptopox position sensor to ultrasonic for speed
 * This opmode will work as fast as possible to get more points with multiple glyphs
 */

public class UltraAuto extends VuforiaBallLib {
    private static final float PILLAR_SPACING_INCH = 7.63f;
    private static final float ENCODE_PER_CM = 17.3f;

    private enum AutoPath {
        RED_FRONT_RIGHT(true, false, RelicRecoveryVuMark.RIGHT, (float)DistanceUnit.INCH.toCm(3), 45),
        RED_FRONT_CENTER(true, false, RelicRecoveryVuMark.CENTER, (float)DistanceUnit.INCH.toCm(3 - PILLAR_SPACING_INCH), 45),
        RED_FRONT_LEFT(true, false, RelicRecoveryVuMark.LEFT, (float)DistanceUnit.INCH.toCm(5), 135);

        public final RelicRecoveryVuMark vuMark;
        public final boolean red;
        public final boolean rear;
        public final float distanceCM;
        public final float turnAmount;
        AutoPath(boolean red, boolean rear, RelicRecoveryVuMark mark, float travelCM, float turnAmount) {
            this.red = red;
            this.rear = rear;
            this.vuMark = mark;
            this.distanceCM = travelCM;
            this.turnAmount = turnAmount;
        }
    }
}
