package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.Timer;
import java.util.TimerTask;

@TeleOp(name = "ConnorRobot", group = "Linear OpMode")
public class ConnorRobot extends LinearOpMode {

    public static final long MILIS_PER_TICK = 10L;

    public static ConnorRobot instance; // a static variable holding a reference to the instance in use

    @Override
    public void runOpMode() {
        waitForStart();
        instance = this; // assigns this instance to a static variable
        HardwareManager.init();
        GamePadInput.init();
        telemetry.addData("Status", "Online");
        telemetry.update();

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                GamePadInput.update(); // performs update operations based on game pad input
            }

        };
        timer.scheduleAtFixedRate(task, 0, MILIS_PER_TICK);
        while (opModeIsActive()) {
            // keeps the program from terminating
        }
        task.cancel();
    }

}
