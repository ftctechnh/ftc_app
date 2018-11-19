package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.SubAssembly.DriveTrain.DriveControl;

import org.firstinspires.ftc.teamcode.SubAssembly.Lift.LiftControl;
import org.firstinspires.ftc.teamcode.Utilities.GamepadWrapper;

/* Sub Assembly Test OpMode
 * This TeleOp OpMode is used to test the functionality of the specific sub assembly
 */
// Assign OpMode type (TeleOp or Autonomous), name, and grouping
@TeleOp(name = "teleop", group = "Drive")
public class teleop extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        telemetry.addLine("Drive Test: ");
        telemetry.addLine("Ready Player One");
        telemetry.addLine("Ready Player Two");

        double speed = 1.0;

        /* initialize sub-assemblies
         */
        DriveControl Drive = new DriveControl();
        LiftControl Lift = new LiftControl();

        GamepadWrapper egamepad1 = new GamepadWrapper(gamepad1);
        GamepadWrapper egamepad2 = new GamepadWrapper(gamepad2);


        Drive.init(hardwareMap);
        Lift.init(hardwareMap);
        telemetry.update();

        //waits for that giant PLAY button to be pressed on RC
        waitForStart();

        //telling the code to run until you press that giant STOP button on RC
        while (opModeIsActive()) {


            egamepad1.updateEdge();
            egamepad2.updateEdge();

            // "Ready Player One" - Halliday

            //speed control
            if (egamepad1.right_bumper.pressed) {
                speed += 0.25;
                if (speed > 3) speed = 3;
            }
            if (egamepad1.left_bumper.pressed) {
                speed -= 0.25;
                if (speed < 0) speed = 0;
            }

            if (egamepad1.dpad_left.state) {
                Drive.tankRightForward(speed);
            } else if (egamepad1.dpad_right.state) {
                Drive.tankLeftForward(speed);
            }

            else if (-gamepad1.left_stick_y < -0.4) {
                Drive.moveBackward(speed);
            } else if (-gamepad1.left_stick_y > 0.4) {
                Drive.moveForward(speed);
            }

            else if (gamepad1.left_stick_x > 0.4) {
                Drive.turnRight(speed/2);
            } else if (gamepad1.left_stick_x < -0.4) {
                Drive.turnLeft(speed/2);
            }
            else { Drive.stop();}


            //ready player two

            if (egamepad2.dpad_up.state) {
                Lift.ManualExtend();
            } else if (egamepad2.dpad_down.state) {
                Lift.ManualRetract();
            } else {
                Lift.ManualStop();
            }

            if (egamepad2.a.released) {
                Lift.Lock();
            }
            else if (egamepad2.b.released) {
                Lift.Unlock();
            }

            telemetry.addLine("Speed: " + speed);


            //SubAssembly.test();
            telemetry.update();

            //let the robot have a little rest, sleep is healthy
            sleep(40);
        }

    }
}
