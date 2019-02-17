package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

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
@TeleOp(name = "teleop", group = "Drive")
public class teleop extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        telemetry.setAutoClear(false);
        telemetry.addLine("TeleOp");

        double DriveSpeed = 1;
        double TurnSpeed = DriveSpeed / 2;
        int reverse = 1;

        /* initialize sub-assemblies
         */
        DriveControl Drive = new DriveControl();
        LiftControl Lift = new LiftControl();
        TofControl Tof = new TofControl();
        LedControl Led = new LedControl();
        MinerControl Miner = new MinerControl();

        GamepadWrapper egamepad1 = new GamepadWrapper(gamepad1);
        GamepadWrapper egamepad2 = new GamepadWrapper(gamepad2);

        Drive.init(this);
        Lift.init(this);
        Tof.init(this);
        Led.init(this);
        Miner.init(this, true);

        //time based variables
        ElapsedTime runtime = new ElapsedTime();
        double lastReset = 0;
        double now = 0;
        boolean Silver = false;
        boolean silver = false;

        //waits for that giant PLAY button to be pressed on RC
        telemetry.addLine(">> Press PLAY to start");
        telemetry.update();
        telemetry.setAutoClear(true);
        waitForStart();

        //telling the code to run until you press that giant STOP button on RC
        while (opModeIsActive()) {

            now = runtime.seconds() - lastReset;

            egamepad1.updateEdge();
            egamepad2.updateEdge();

            //Ready Player One

            //reverse control
            if (egamepad1.b.released) {
                reverse = reverse * -1;
            }

            //latch speed setting
            if (egamepad1.a.released) {
                if (DriveSpeed == 2 * TurnSpeed) {
                    DriveSpeed = 0.25;
                    TurnSpeed = 0.25;
                } else {
                    DriveSpeed = 1;
                    TurnSpeed = 0.5;
                }
            }

            //drive speed control
            if (egamepad1.right_bumper.pressed) {
                DriveSpeed += 0.25;
                if (DriveSpeed > 3) DriveSpeed = 3;
            }
            if (egamepad1.right_trigger.pressed) {
                DriveSpeed -= 0.25;
                if (DriveSpeed <= 0) DriveSpeed = 0.25;
            }

            //turning speed control
            if (egamepad1.left_bumper.pressed) {
                TurnSpeed += 0.25;
                if (TurnSpeed > 3) TurnSpeed = 3;
            }
            if (egamepad1.left_trigger.pressed) {
                TurnSpeed -= 0.25;
                if (TurnSpeed <= 0) TurnSpeed = 0.25;
            }

            //drive controls
            if (-gamepad1.left_stick_y < -0.4) {
                Drive.moveBackward(reverse * DriveSpeed);
            } else if (-gamepad1.left_stick_y > 0.4) {
                Drive.moveForward(reverse * DriveSpeed);
            } else if (gamepad1.left_stick_x > 0.4) {
                Drive.turnRight(TurnSpeed);
            } else if (gamepad1.left_stick_x < -0.4) {
                Drive.turnLeft(TurnSpeed);
            } else {
                Drive.stop();
            }

            if (egamepad1.dpad_down.released) {
                /*if (Miner.DeployerServo.getSetpoint() == MinerControl.Setpoints.Undump && Miner.MinerButtonI.isPressed()){
                    Miner.Extend();
                    Drive.TimeDelay(0.2);
                    Miner.MinerStop();
                }*/
                Miner.deployDown();
            } else if (egamepad1.dpad_up.released) {
                /*if (Miner.DeployerServo.getSetpoint() == MinerControl.Setpoints.Undump && Miner.MinerButtonI.isPressed()){
                    Miner.Extend();
                    Drive.TimeDelay(0.2);
                    Miner.MinerStop();
                }*/
                Miner.deployUp();
            }

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
                Silver = false;
            } else if (egamepad2.x.released) {
                Miner.IntakeRaise();
                Led.white();
                Silver = true;
            }

            if ((gamepad2.left_stick_y < -0.4) && (!Miner.MinerButtonO.isPressed())) {
                Miner.Extend();
                //resets the clock
                lastReset = runtime.seconds();
                if (silver) {
                    Miner.IntakeRaise();
                    silver = false;
                }
            } else if ((gamepad2.left_stick_y > 0.4) && (!Miner.MinerButtonI.isPressed())) {
                Miner.Retract();
                if (now > 1 && Silver) {
                    Miner.IntakeLower();
                    silver = true;
                }
            } else {
                Miner.MinerStop();
                //resets the clock
                lastReset = runtime.seconds();
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