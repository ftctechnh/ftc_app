import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.Mocks.MockDcMotor;
import org.firstinspires.ftc.teamcode.NullbotHardware;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by guberti on 1/9/2018.
 */

public class HardwareUtilityFunctionsTest {
    NullbotHardware robot = new NullbotHardware();

    @Test
    public void hardwareUtilities_WaitTick_OperatesNormally() {
        final int DELAY_MS = 1000;
        final int ACCURACY_THRESHOLD = 15;

        // Mock
        robot.imu = mock(BNO055IMU.class);
        when(robot.imu.getAngularOrientation(
                AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS)).thenReturn(new Orientation());

        robot.waitForTick(0);
        ElapsedTime e = new ElapsedTime();

        try { // Simulate some work going on here
            Thread.sleep(500);
        } catch (InterruptedException e1) {}

        robot.waitForTick(DELAY_MS);

        assertEquals(e.milliseconds(), DELAY_MS, ACCURACY_THRESHOLD);
        assertTrue(robot.angles != null); // Make sure we've "gotten" angles
    }

    @Test
    public void hardwareUtilities_Clamp() {
        assertEquals(robot.clamp(1.0), 1.0, 0);
        assertEquals(robot.clamp(1.2), 1.0, 0);
        assertEquals(robot.clamp(0.12473), 0.12473, 0);
        assertEquals(robot.clamp(-0.928), -0.928, 0);
        assertEquals(robot.clamp(-1.0), -1.0, 0);
        assertEquals(robot.clamp(-1.23), -1.0, 0);
    }

    private void mockMotors() {
        robot.motorArr = new DcMotor[4];
        for (int i = 0; i < 4; i++) {
            robot.motorArr[i] = new MockDcMotor();
        }
    }

    @Test
    public void hardwareUtilities_SetMotorSpeeds_OperatesNormally() {
        mockMotors();
        robot.setMotorSpeeds(new double[] {-0.93, -1, 0, 1.5});
        double[] EXPECTED_SPEEDS = {-0.93, -1, 0, 1};

        for (int i = 0; i < 4; i++) {
            assertEquals(EXPECTED_SPEEDS[i], robot.motorArr[i].getPower(), 0);
        }
    }

    @Test
    public void hardwareUtilities_SetDriveMode_OperatesNormally() {
        mockMotors();
        DcMotor.RunMode[] modes = new DcMotor.RunMode[]{
                DcMotor.RunMode.RUN_TO_POSITION,
                DcMotor.RunMode.RUN_USING_ENCODER,
                DcMotor.RunMode.RUN_WITHOUT_ENCODER,
                DcMotor.RunMode.STOP_AND_RESET_ENCODER};

        for (DcMotor.RunMode m : modes) {
            robot.setDriveMode(m);
            for (DcMotor motor : robot.motorArr) {
                assertEquals(motor.getMode(), m);
            }
        }
    }

    @Test
    public void hardwareUtilities_NormAngle() {
        final double[] INPUTS = {0, -Math.PI, 3.385, -Math.PI * 2, Math.PI * 1827428.5};
        final double[] EXPECTED_OUTPUTS = {0, Math.PI, 3.385, 0, Math.PI/2};
        final double DELTA = 0.00001;

        for (int i = 0; i < INPUTS.length; i++) {
            assertEquals(robot.normAngle(INPUTS[i]), EXPECTED_OUTPUTS[i], DELTA);
        }
    }

    @Test
    public void hardwareUtilities_AngleDifference() {
        final double[] MINUENDS = {Math.PI, -Math.PI, 3, Math.PI * 1.5};
        final double[] SUBTRAHENDS = {-Math.PI*2, -2, -1, Math.PI * 119748371};
        final double[] DIFFERENCES = {Math.PI, (Math.PI - 2), ((Math.PI * 2) - 4), Math.PI/2};
        final double DELTA = 0.00001;

        for (int i = 0; i < MINUENDS.length; i++) {
            assertEquals(DIFFERENCES[i], robot.getAngleDifference(MINUENDS[i], SUBTRAHENDS[i]), DELTA);
        }
    }

    @Test
    public void hardwareUtilities_GetDrivePowers() {
        final double[] ANGLES = {0, Math.PI/4, Math.PI/2, Math.PI, Math.PI * 1.5, 2.5};
        final double[] DIAGONAL1_EXPECTED = {0.7071, 1, 0.7071, -0.7071, -0.7071, -0.1433};
        final double[] DIAGONAL2_EXPECTED = {0.7071, 0, -0.7071, -0.7071, 0.7071, -0.9897};
        final double DELTA = 0.001;

        for (int i = 0; i < ANGLES.length; i++) {
            double[] powers = robot.getDrivePowersFromAngle(ANGLES[i]);
            assertEquals(DIAGONAL1_EXPECTED[i], powers[0], DELTA); // Both wheels on diagonal should
            assertEquals(DIAGONAL1_EXPECTED[i], powers[3], DELTA); // go the same speed
            assertEquals(DIAGONAL2_EXPECTED[i], powers[1], DELTA);
            assertEquals(DIAGONAL2_EXPECTED[i], powers[2], DELTA);
        }
    }
}
