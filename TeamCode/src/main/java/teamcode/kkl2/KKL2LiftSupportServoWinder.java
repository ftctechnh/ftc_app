package teamcode.kkl2;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "KKL2LiftSupportServoWinder", group = "Linear OpMode")
public class KKL2LiftSupportServoWinder extends LinearOpMode {

    private static final double WIND_SPEED = 0.01;

    @Override
    public void runOpMode() {
        KKL2HardwareManager.initialize(this);
        waitForStart();
        while (opModeIsActive()) {
            double power;
            if (gamepad1.right_bumper) {
                power = 1.0;
            } else if (gamepad1.left_bumper) {
                power = 0.0;
            } else {
                power = 0.5;
            }
            KKL2HardwareManager.liftSupportServo.setPosition(power);
        }
    }

}
