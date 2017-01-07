package org.firstinspires.ftc.omegas.sensor;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.omegas.HardwareOmegas;

/**
 * Created by ethertyper on 10/22/16.
 */

@SuppressWarnings("unused")
@Autonomous(name = "Omegas: Rotation Test", group = "Tests")
public class OmegasRotation extends LinearOpMode {

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
                initDriveMotors(hardwareMap);
                initTelemetry(telemetry);

                sayMessage();
            }
        };

        // Rotate for a clean thirty seconds.
        Ω.rotate(30000 - runtime.milliseconds(), true);
    }
}
