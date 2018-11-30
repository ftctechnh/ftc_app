package teamcode.kkl2;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "KKL2LiftSupportServoWinder", group = "Linear OpMode")
public class KKL2LiftSupportServoWinder extends LinearOpMode {

    private static final int WIND_SPEED = 10;

    private int liftSupportServoPos;

    @Override
    public void runOpMode() {
        KKL2HardwareManager.initialize(this);
        waitForStart();
        while (opModeIsActive()) {
            float lt = gamepad1.left_trigger;
            float rt = gamepad1.right_trigger;
            if (lt > 0) {
                liftSupportServoPos += lt * WIND_SPEED;
            } else if (rt > 0) {
                liftSupportServoPos -= rt * WIND_SPEED;
            }
            KKL2HardwareManager.liftSupportServo.setPosition(liftSupportServoPos);
        }
    }

}
