package org.firstinspires.ftc.teamcode.SubAssembly.Sensors;

/**
 * Created by Nicholas on 9/5/2018.
 */

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.SubAssembly.Sample.SampleTemplate;

/* Sub Assembly Test OpMode
 * This TeleOp OpMode is used to test the functionality of the specific sub assembly
 */
// Assign OpMode type (TeleOp or Autonomous), name, and grouping
@TeleOp(name = "Sensors Test", group = "Test")
public class SensorsTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        telemetry.addLine("Sensors Test OpMode");

        /* initialize sub-assemblies
         */
        SensorsControl Sensors = new SensorsControl(this);

        telemetry.update();

        //waits for that giant PLAY button to be pressed on RC
        waitForStart();

        //telling the code to run until you press that giant STOP button on RC
        while (opModeIsActive()) {

            Sensors.DisplayBattery();
            telemetry.update();

            //let the robot have a little rest, sleep is healthy
            sleep(40);
        }

    }
}