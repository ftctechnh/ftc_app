package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "ConnorRobot", group = "Linear OpMode")
public class ConnorRobot extends LinearOpMode {

    public static final long MILIS_PER_ITERATION = 100L;

    public static ConnorRobot instance; // a static variable holding a reference to the instance in use

    @Override
    public void runOpMode() {
        instance = this; // assigns a static variable to the current instance
        telemetry.addData("Status", "Online");
        telemetry.update();
        waitForStart();
        while (opModeIsActive()) {
            GamePadInput.update(); // performs update operations based on game pad input
            sleep(MILIS_PER_ITERATION);
        }
    }


    private int ticks = 10;
    private double step = 0.1;

    public void armStuff() {
        while (opModeIsActive()) {

        }
    }

}
