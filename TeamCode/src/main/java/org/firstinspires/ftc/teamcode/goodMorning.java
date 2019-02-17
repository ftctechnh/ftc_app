package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.SubAssembly.DriveTrain.DriveControl;

import org.firstinspires.ftc.teamcode.SubAssembly.Leds.LedControl;
import org.firstinspires.ftc.teamcode.SubAssembly.Lift.LiftControl;
import org.firstinspires.ftc.teamcode.SubAssembly.Miner.MinerControl;
import org.firstinspires.ftc.teamcode.SubAssembly.Sensors.TofControl;
import org.firstinspires.ftc.teamcode.Utilities.GamepadWrapper;

/* Sub Assembly Test OpMode
 * This TeleOp OpMode is used to test the functionality of the specific sub assembly
 */
// Assign OpMode type (TeleOp or Autonomous), name, and grouping
@TeleOp(name = "Good Morning", group = "Drive")
public class goodMorning extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        telemetry.setAutoClear(false);
        telemetry.addLine("Good Morning");

        double DriveSpeed = 1;
        double TurnSpeed = DriveSpeed / 2;
        int reverse = -1;

        /* initialize sub-assemblies
         */
        LiftControl Lift = new LiftControl();
        TofControl Tof = new TofControl();
        LedControl Led = new LedControl();
        MinerControl Miner = new MinerControl();

        GamepadWrapper egamepad1 = new GamepadWrapper(gamepad1);
        GamepadWrapper egamepad2 = new GamepadWrapper(gamepad2);

        Lift.init(this);
        Tof.init(this);
        Led.init(this);
        Miner.init(this);

        //waits for that giant PLAY button to be pressed on RC
        telemetry.addLine(">> Press PLAY to start");
        telemetry.update();
        telemetry.setAutoClear(true);
        waitForStart();

        //telling the code to run until you press that giant STOP button on RC
        while (opModeIsActive()) {


            egamepad1.updateEdge();
            egamepad2.updateEdge();

            //Ready Player Two

            //lift control
            if ((egamepad2.dpad_up.state) && (!Lift.LifterButtonT.isPressed())) {
                Led.rainbowRainbowPalette();
                Lift.Extend();
            } else if ((egamepad2.dpad_down.state) && (!Lift.LifterButtonB.isPressed())) {
                Led.rainbowPartyPalette();
                Lift.Retract();
            } else {
                Lift.Stop();
            }

            //lift lock controls
            if (egamepad2.a.released) {
                Lift.Lock();
            } else if (egamepad2.b.released) {
                Lift.Unlock();
            }

            //miner controls
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


            telemetry.addLine("DriveSpeed: " + DriveSpeed);
            telemetry.addLine("TurnSpeed: " + TurnSpeed);

            //SubAssembly.test();
            telemetry.update();

            //let the robot have a little rest, sleep is healthy
            sleep(40);
        }
    }
}