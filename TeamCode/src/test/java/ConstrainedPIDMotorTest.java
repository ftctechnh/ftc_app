import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.ConstrainedPIDMotor;
import org.firstinspires.ftc.teamcode.Mocks.MockDcMotor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
/**
 * Created by guberti on 1/9/2018.
 */

public class ConstrainedPIDMotorTest {

    @Test
    public void constrainedPIDMotor_OperatesNormally() {
        DcMotorEx mock = new MockDcMotor();
        ConstrainedPIDMotor m = new ConstrainedPIDMotor(mock, 100, 0.6,
        0.4, 0, 5000, mock(Telemetry.class), false);

        // Make sure we're going to maximum
        mock.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); // Make sure this gets changed

        m.setDirection(ConstrainedPIDMotor.Direction.FORWARD);
        assertEquals(5000, mock.getTargetPosition());
        assertEquals(0.6, mock.getPower(), 0.00001);
        assertEquals(DcMotor.RunMode.RUN_TO_POSITION, mock.getMode());

        // Make sure we can release the motor
        m.setDirection(ConstrainedPIDMotor.Direction.COAST);
        assertEquals(mock.getMode(), DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        assertEquals(mock.getPower(), 0, 0.00001);

        // Make sure we can go to the minimum
        m.setDirection(ConstrainedPIDMotor.Direction.BACKWARD);
        assertEquals(0, mock.getTargetPosition());
        assertEquals(0.4, mock.getPower(), 0.00001);
        assertEquals(DcMotor.RunMode.RUN_TO_POSITION, mock.getMode());

        // Try overriding
        m.override = true;
        m.setDirection(ConstrainedPIDMotor.Direction.FORWARD);
        assertEquals(0.3, mock.getPower(), 0.00001);
        assertEquals(DcMotor.RunMode.RUN_USING_ENCODER, mock.getMode());
        m.setDirection(ConstrainedPIDMotor.Direction.BACKWARD);
        assertEquals(-0.2, mock.getPower(), 0.00001);
        assertEquals(DcMotor.RunMode.RUN_USING_ENCODER, mock.getMode());

        m.override = false;

        // Ensure we can escape overriding
        m.setDirection(ConstrainedPIDMotor.Direction.FORWARD);
        assertEquals(5000, mock.getTargetPosition());
        assertEquals(0.6, mock.getPower(), 0.00001);
        assertEquals(DcMotor.RunMode.RUN_TO_POSITION, mock.getMode());
    }

    @Test
    public void constrainedPIDMotor_LocksPosition() throws InterruptedException {
        DcMotorEx mock = new MockDcMotor();
        ConstrainedPIDMotor m = new ConstrainedPIDMotor(mock, 100, 0.6,
                0.4, 0, 5000, mock(Telemetry.class), false);

        m.setDirection(ConstrainedPIDMotor.Direction.FORWARD);
        ((MockDcMotor) mock).setCurrentPosition(500);
        m.setDirection(ConstrainedPIDMotor.Direction.HOLD);
        assertEquals(DcMotor.RunMode.RUN_USING_ENCODER, mock.getMode());
        assertEquals(0, mock.getPower(), 0.0001);
        Thread.sleep(120); // Delay 100 ms
        m.setDirection(ConstrainedPIDMotor.Direction.HOLD);
        assertEquals(DcMotor.RunMode.RUN_TO_POSITION, mock.getMode());
        assertEquals(500, mock.getTargetPosition());
    }
}
