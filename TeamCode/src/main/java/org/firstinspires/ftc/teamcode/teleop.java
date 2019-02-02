package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.SubAssembly.DriveTrain.DriveControl;

import org.firstinspires.ftc.teamcode.SubAssembly.Leds.LedControl;
import org.firstinspires.ftc.teamcode.SubAssembly.Lift.LiftControl;
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
        int reverse = -1;

        /* initialize sub-assemblies
         */
        DriveControl Drive = new DriveControl();
        LiftControl Lift = new LiftControl();
        TofControl Tof = new TofControl();
        LedControl Led = new LedControl();

        GamepadWrapper egamepad1 = new GamepadWrapper(gamepad1);
        GamepadWrapper egamepad2 = new GamepadWrapper(gamepad2);

        Drive.init(this);
        Lift.init(this);
        Tof.init(this);
        Led.init(this);

        //waits for that giant PLAY button to be pressed on RC
        telemetry.addLine(">> Press PLAY to start");
        telemetry.update();
        telemetry.setAutoClear(true);
        waitForStart();

        //telling the code to run until you press that giant STOP button on RC
        while (opModeIsActive()) {


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
                Led.orange();
            }

            //lift lock controls
            if (egamepad2.a.released) {
                Lift.Lock();
            } else if (egamepad2.b.released) {
                Lift.Unlock();
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