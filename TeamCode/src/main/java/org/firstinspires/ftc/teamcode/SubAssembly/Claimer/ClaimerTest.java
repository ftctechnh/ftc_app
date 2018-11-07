package org.firstinspires.ftc.teamcode.SubAssembly.Claimer;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Utilities.GamepadWrapper;

/* Sub Assembly Test OpMode
 * This TeleOp OpMode is used to test the functionality of the specific sub assembly
 */
// Assign OpMode type (TeleOp or Autonomous), name, and grouping
@TeleOp(name = "Claimer Test", group = "Test")
public class ClaimerTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        telemetry.addLine("Claimer Test: ");
        double speed = 1.0;

        /* initialize sub-assemblies
         */
        ClaimerControl Claimer = new ClaimerControl();

        GamepadWrapper egamepad1 = new GamepadWrapper(gamepad1);
        GamepadWrapper egamepad2 = new GamepadWrapper(gamepad2);

        telemetry.update();

        //waits for that giant PLAY button to be pressed on RC
        waitForStart();

        //telling the code to run until you press that giant STOP button on RC
        while (opModeIsActive()) {



            egamepad1.updateEdge();
            egamepad2.updateEdge();



            if (egamepad1.a.state) {
                Claimer.drop();
            }




                //SubAssembly.test();
                telemetry.update();

                //let the robot have a little rest, sleep is healthy
                sleep(40);
            }

        }
    }
