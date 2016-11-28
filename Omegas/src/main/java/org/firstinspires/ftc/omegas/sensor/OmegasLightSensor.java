package org.firstinspires.ftc.omegas.sensor;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.omegas.HardwareOmegas;

/**
 * Created by ethertyper on 10/17/16.
 */

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
                initLightSensor(hardwareMap);
            }
        };

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            telemetry.addData("Data", "Light amount: " + Ω.getLineSensor().getLightDetected());
            telemetry.update();
        }
    }
}
