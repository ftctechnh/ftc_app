package org.firstinspires.ftc.teamcode.SubAssembly.DriveTrain;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Utilities.GamepadWrapper;

/* Sub Assembly Test OpMode
 * This TeleOp OpMode is used to test the functionality of the specific sub assembly
 */
// Assign OpMode type (TeleOp or Autonomous), name, and grouping
@TeleOp(name = "Drive Test", group = "Test")
public class DriveTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        telemetry.addLine("Drive Test: ");
        double speed = 1.0;

        /* initialize sub-assemblies
         */
        DriveControl Drive = new DriveControl();

        GamepadWrapper egamepad1 = new GamepadWrapper(gamepad1);
        GamepadWrapper egamepad2 = new GamepadWrapper(gamepad2);

        Drive.init(this);
        telemetry.update();

        //waits for that giant PLAY button to be pressed on RC
        waitForStart();

        //telling the code to run until you press that giant STOP button on RC
        while (opModeIsActive()) {


            egamepad1.updateEdge();
            egamepad2.updateEdge();

            //speed control
            if (egamepad1.left_stick_button.pressed) {
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
            } else if (-gamepad1.left_stick_y < -0.4) {
                Drive.moveBackward(speed);
            } else if (-gamepad1.left_stick_y > 0.4) {
                Drive.moveForward(speed);
            } else if (gamepad1.left_stick_x > 0.4) {
                Drive.turnRight(speed / 2);
            } else if (gamepad1.left_stick_x < -0.4) {
                Drive.turnLeft(speed / 2);
            } else {
                Drive.stop();
            }

            telemetry.addLine("Speed: " + speed);


            //SubAssembly.test();
            telemetry.update();

            //let the robot have a little rest, sleep is healthy
            sleep(40);
        }

    }
}
