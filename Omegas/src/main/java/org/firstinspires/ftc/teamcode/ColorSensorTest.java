package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import android.graphics.Color;

/**
 * Created by ethertyper on 10/17/16.
 */

@TeleOp(name="Omegas: Color Sensor Test", group="Tests")
public class ColorSensorTest extends LinearOpMode {

    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();
    HardwareOmegas      Ω       = new HardwareOmegas();

    // IPS Units
    static final double     FORWARD_SPEED = 0.6;
    static final double     TURN_SPEED    = 0.5;

    @Override
    public void runOpMode() {
        Ω.init(hardwareMap);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            telemetry.addData("Color",String.format("Red: %s\nGreen:%s\nBlue:%s\n",
                    Color.red(Ω.leftColorSensor.argb()),
                    Color.green(Ω.leftColorSensor.argb()),
                    Color.blue(Ω.leftColorSensor.argb())));
            telemetry.update();
        }
    }
}
