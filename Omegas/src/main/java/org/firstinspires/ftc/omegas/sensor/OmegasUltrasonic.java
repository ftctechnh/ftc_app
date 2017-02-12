package org.firstinspires.ftc.omegas.sensor;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.omegas.HardwareOmegas;

/**
 * Created by ethertyper on 2/6/17.
 */
@Autonomous(name = "Omegas: Ultrasonic Test", group = "Tests")
public class OmegasUltrasonic extends LinearOpMode {

    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();
    HardwareOmegas Ω = null;

    @Override
    public void runOpMode() throws InterruptedException {
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        Ω = new HardwareOmegas() {
            @Override
            public void init() {
                initAppContext(hardwareMap);
                initUltrasonicSensor(hardwareMap);
                initTelemetry(telemetry);
                initAudio();

                sayMessage();
            }
        };

        while (opModeIsActive()) {
            telemetry.addData("Ultrasonic levels:", Ω.getUltrasonicSensor().getUltrasonicLevel());
        }
    }
}
