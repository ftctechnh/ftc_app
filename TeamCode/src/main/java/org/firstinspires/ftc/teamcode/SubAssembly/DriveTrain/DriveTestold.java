package org.firstinspires.ftc.teamcode.SubAssemblyDrive;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Utilities.GamepadWrapper;

//naming the teleop thing
@TeleOp(name = "Drive Test", group = "Test")
public class DriveTest extends LinearOpMode {

    public void displayHelp() {
        telemetry.addLine("Press START for this help");
        telemetry.addLine("Gamepad 1");
        telemetry.addLine("  Reverse (combo only)  a");
        telemetry.addLine("  Change speed (+/-)    right/left bumper");
        telemetry.addLine("  Combination motion");
        telemetry.addLine("    strafe   right joystick x");
        telemetry.addLine("    move     left joystick y ");
        telemetry.addLine("    rotate   left joystick x");
        telemetry.addLine("  Direct motion");
        telemetry.addLine("    move & strafe  dpad");
        telemetry.addLine("    rotate         x, b");
    }

    @Override
    public void runOpMode() throws InterruptedException {
        //declaring all my variables in one place for my sake
        double speed_forward_back, speed_left_right, speed_rotate_left_right;
        double speed = 1.0;
        double reverse = 1.0;

        telemetry.addLine("Drive Test");

        /* initialize sub assemblies
         */
        DriveControl Drive = new DriveControl(this);

        /* Instantiate extended gamepad */
        GamepadWrapper egamepad1 = new GamepadWrapper(gamepad1);
        GamepadWrapper egamepad2 = new GamepadWrapper(gamepad2);

        displayHelp();
        telemetry.update();

        //waits for that giant PLAY button to be pressed on RC
        waitForStart();

        //telling the code to run until you press that giant STOP button on RC
        while (opModeIsActive()) {
            /* Update extended gamepad */
            egamepad1.updateEdge();
            egamepad2.updateEdge();

            //when a button is just released, multiply the speed by -1 so it's reverse
            if (egamepad1.a.released) {
                reverse *= -1;
            }

            //change that speed by those bumpers
            if (egamepad1.right_bumper.pressed) {
                speed += 0.25;
                if (speed > 3) speed = 3;
            }
            if (egamepad1.left_bumper.pressed) {
                speed -= 0.25;
                if (speed < 0) speed = 0;
            }

            // using the right joystick's x axis to rotate left and right
            speed_left_right = -gamepad1.right_stick_x;

            // using the left joystick's y axis to move forward and backwards
            speed_forward_back = -gamepad1.left_stick_y;

            // using the left joystick's x axis to strafe left and right
            speed_rotate_left_right = -gamepad1.left_stick_x;

            //takes all those values, divides
            speed_left_right = speed_left_right * speed * reverse;
            speed_forward_back = speed_forward_back * speed * reverse;
            speed_rotate_left_right = speed_rotate_left_right * speed * reverse;

            /* If any dpad button is pressed, do that motion
             * Only do combination move if no dpad button is pressed
             */
            if (gamepad1.dpad_left) {
                Drive.moveLeft(speed);
            } else if (gamepad1.dpad_right) {
                Drive.moveRight(speed);
            } else if (gamepad1.dpad_up) {
                Drive.moveForward(speed);
            } else if (gamepad1.dpad_down) {
                Drive.moveBackward(speed);
            } else if (gamepad1.x) {
                Drive.rotateLeft(speed);
            } else if (gamepad1.b) {
                Drive.rotateRight(speed);
            } else {
                Drive.moveCombination(speed_forward_back, speed_left_right, speed_rotate_left_right);
            }

            /* display information */
            if (egamepad1.start.state) {
                displayHelp();
            } else {
                telemetry.addData("Speed: ", speed);
                telemetry.addData("Direction: ", reverse);
            }
            telemetry.update();

            //let the robot have a little rest, sleep is healthy
            sleep(40);
        }
    }
}
