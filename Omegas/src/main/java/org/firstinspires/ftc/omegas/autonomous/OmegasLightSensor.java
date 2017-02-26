package org.firstinspires.ftc.omegas.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.omegas.HardwareOmegas;

/**
 * Created by ethertyper on 10/17/16.
 */

@SuppressWarnings("unused")
@Autonomous(name = "Omegas: Light Sensor Test", group = "Tests")
public class OmegasLightSensor extends LinearOpMode {

    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();
    HardwareOmegas Ω = null;

    @Override
    public void runOpMode() {
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        Ω = new HardwareOmegas() {
            @Override
            public void init() {
                initAppContext(hardwareMap);
                initLightSensor(hardwareMap);
                initDriveMotors(hardwareMap);
                initBeaconators(hardwareMap);
                initTelemetry(telemetry);
                initAudio();

                getLightSensor().enableLed(true);
                sayMessage();
            }
        };

        double light;

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            light = Ω.getLightSensor().getLightDetected();
            telemetry.addData("Data", "Light amount: " + light);
            telemetry.update();

            if (light < 0.4) {
                Ω.driveForward(0.25, 100.0);
            } else {
                try {
                    Thread.sleep(200);
                } catch (Exception e) {
                    System.err.print("You can't even sleep right...");
                }
                Ω.rotate(Math.PI * 4 / 9, true);
                Ω.rightBeaconatorSequence(Ω.getRightBeaconator());

                break;
            }
        }

        Ω.getLightSensor().enableLed(false);
    }
}
