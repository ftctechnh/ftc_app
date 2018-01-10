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
}
