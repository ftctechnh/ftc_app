package org.firstinspires.ftc.teamcode.SubAssembly.Lift;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Utilities.GamepadWrapper;

/* Sub Assembly Test OpMode
 * This TeleOp OpMode is used to test the functionality of the specific sub assembly
 */
// Assign OpMode type (TeleOp or Autonomous), name, and grouping
@TeleOp(name = "Lift Test", group = "Test")
public class LiftTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        telemetry.addLine("Lift Test: ");
        /* initialize sub-assemblies
         */
        LiftControl Lift = new LiftControl();

        GamepadWrapper egamepad1 = new GamepadWrapper(gamepad1);
        GamepadWrapper egamepad2 = new GamepadWrapper(gamepad2);

        Lift.init(this);
        telemetry.update();

        //waits for that giant PLAY button to be pressed on RC
        waitForStart();

        //telling the code to run until you press that giant STOP button on RC
        while (opModeIsActive()) {

            egamepad1.updateEdge();
            egamepad2.updateEdge();

            if ((egamepad2.dpad_up.state) && (!Lift.LifterButtonT.isPressed())) {
                Lift.Extend();
            } else if ((egamepad2.dpad_down.state) && (!Lift.LifterButtonB.isPressed())) {
                Lift.Retract();
            } else {
                Lift.Stop();
            }

            if (egamepad2.x.released) {
                Lift.Lock();
            }
            else if (egamepad2.b.released) {
                Lift.Unlock();
            }
            
            telemetry.update();

            //let the robot have a little rest, sleep is healthy
            sleep(40);
        }
    }
}
