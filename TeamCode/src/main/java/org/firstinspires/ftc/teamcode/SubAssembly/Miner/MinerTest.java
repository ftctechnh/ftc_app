package org.firstinspires.ftc.teamcode.SubAssembly.Miner;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.SubAssembly.Leds.LedControl;
import org.firstinspires.ftc.teamcode.SubAssembly.Miner.MinerControl;
import org.firstinspires.ftc.teamcode.Utilities.GamepadWrapper;

/* Sub Assembly Test OpMode
 * This TeleOp OpMode is used to test the functionality of the specific sub assembly
 */
// Assign OpMode type (TeleOp or Autonomous), name, and grouping
@TeleOp(name = "Miner Test", group = "Test")
public class MinerTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        telemetry.addLine("Miner Test: ");
        /* initialize sub-assemblies
         */
        MinerControl Miner = new MinerControl();
        LedControl Led = new LedControl();

        GamepadWrapper egamepad1 = new GamepadWrapper(gamepad1);
        GamepadWrapper egamepad2 = new GamepadWrapper(gamepad2);

        Miner.init(this);
        Led.init(this);
        telemetry.update();

        //waits for that giant PLAY button to be pressed on RC
        waitForStart();

        //telling the code to run until you press that giant STOP button on RC
        while (opModeIsActive()) {

            egamepad1.updateEdge();
            egamepad2.updateEdge();

            if ((gamepad2.left_stick_y < -0.4) && (!Miner.MinerButtonO.isPressed())) {
                Miner.Extend();
            } else if ((gamepad2.left_stick_y > 0.4) && (!Miner.MinerButtonI.isPressed())) {
                Miner.Retract();
            } else {
                Miner.MinerStop();
            }

            if (gamepad2.right_stick_y < -0.4) {
                Miner.Intake();
            } else if (gamepad2.right_stick_y > 0.4) {
                Miner.Untake();
            } else  {
                Miner.Stoptake();
            }

            /*if (egamepad2.left_trigger.released) {
                Miner.Undump();
            } else if (egamepad2.left_bumper.released) {
                Miner.Dump();
            }*/

            if (egamepad2.y.released) {
                Miner.IntakeLower();
                Led.orange();
            } else if (egamepad2.x.released) {
                Miner.IntakeRaise();
                Led.white();
            }

            if (egamepad2.left_trigger.released) {
                Miner.deployDown();
            } else if (egamepad2.left_bumper.released) {
                Miner.deployUp();
            }


            telemetry.update();

            //let the robot have a little rest, sleep is healthy
            sleep(40);
        }
    }
}