package org.firstinspires.ftc.omegas.sensor;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.omegas.HardwareOmegas;

/**
 * Created by ethertyper on 10/17/16.
 */

@Autonomous(name = "Omegas: Light Sensor Test", group = "Tests")
public class OmegasLightSensor extends LinearOpMode {

    /* Declare OpMode members. */
//    private ElapsedTime runtime = new ElapsedTime();
    private HardwareOmegas Ω = new HardwareOmegas();

    @Override
    public void runOpMode() {
        Ω.initLightSensor(hardwareMap);
        Ω.initDriveMotors(hardwareMap);
        Ω.initBeaconators(hardwareMap);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
//        runtime.reset();

        Ω.getLightSensor().enableLed(true);
        double light;

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            light = Ω.getLightSensor().getLightDetected();
            telemetry.addData("Data", "Light amount: " + light);
            telemetry.update();
            if (light<0.4) {
                Ω.driveForward(50.0);
            }
            else {
                Ω.driveForward(100.0);
                Ω.rotate90DegLeft();
                Ω.driveForward(500.0);
                Ω.rightBeaconatorSequence(Ω.getRightBeaconator(), 1500);
                break;
            }
        }
        Ω.getLightSensor().enableLed(false);
    }
}
