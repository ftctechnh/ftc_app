package org.firstinspires.ftc.omegas.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.omegas.HardwareOmegas;

/**
 * Created by ethertyper on 10/29/16.
 *
 * Backs up into the cap ball.
 */

@SuppressWarnings("unused")
@Autonomous(name = "Omegas: Cap Ball Test", group = "Tests")
public class OmegasCapBall extends LinearOpMode {

    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();
    HardwareOmegas Ω = null;

    // IPS Units
    static final double FORWARD_SPEED = 0.6;
    static final double TURN_SPEED = 0.5;

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        Ω = new HardwareOmegas() {
            @Override
            public void init() {
                initDriveMotors(hardwareMap);
                initAppContext(hardwareMap);
                initTelemetry(telemetry);
                initAudio();

                sayMessage();
            }
        };

        // run until the end of the match (driver presses STOP)
        Ω.driveForward(-0.25, 3000);

        for (DcMotor motor : Ω.getMotors()) {
            motor.setPower(0.0f);
        }
    }
}
