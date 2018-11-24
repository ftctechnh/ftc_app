package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="LinearSlideTest", group="Linear Opmode")
public class LinearSlideTest extends LinearOpMode {
    private DcMotor motor1;

    public void runOpMode() {

        initialize();
        waitForStart();



        //Always True
        while (opModeIsActive() ) {
            float joystickY = gamepad1.right_stick_y;
            motor1.setPower(joystickY);
        }
    }

    public void initialize() {
        motor1 = hardwareMap.get(DcMotor.class, "motor1");
        motor1.setDirection(DcMotor.Direction.FORWARD);
    }
}
