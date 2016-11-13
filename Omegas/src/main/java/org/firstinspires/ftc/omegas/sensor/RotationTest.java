package org.firstinspires.ftc.omegas.sensor;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.omegas.HardwareOmegas;

/**
 * Created by ethertyper on 10/22/16.
 */

@Autonomous(name = "Omegas: Rotation Test", group = "Tests")
public class RotationTest extends LinearOpMode {

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
            // eg: Run wheels in tank mode opposite to each other.
            Ω.leftBackMotor.setPower(1.0);
            Ω.leftFrontMotor.setPower(1.0);
            Ω.rightBackMotor.setPower(-1.0);
            Ω.rightFrontMotor.setPower(-1.0);
        }
    }
}
