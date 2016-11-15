package org.firstinspires.ftc.omegas.sensor;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.omegas.HardwareOmegas;

/**
 * Created by ethertyper on 10/17/16.
 */

@Autonomous(name = "Omegas: Color Sensor Test", group = "Tests")
public class LightSensorTest extends LinearOpMode {

    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();
    HardwareOmegas Ω = new HardwareOmegas();

    @Override
    public void runOpMode() {
        Ω.init(hardwareMap);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            telemetry.addData("Data", "Light amount: ", Ω.lineSensor.getLightDetected());
            telemetry.update();
        }
    }
}
