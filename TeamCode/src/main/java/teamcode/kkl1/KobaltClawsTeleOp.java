package teamcode.kkl1;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.Timer;
import java.util.TimerTask;

@TeleOp(name = "KobaltClawsTeleOp", group = "Linear OpMode")
public class KobaltClawsTeleOp extends LinearOpMode {

    public static final long MILIS_PER_TICK = 10L;

    public static KobaltClawsTeleOp instance; // a static variable holding a reference to the instance in use

    @Override
    public void runOpMode() {
        waitForStart();
        instance = this; // assigns a static variable to this instance
        KKL1HardwareManager.init(this);
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
