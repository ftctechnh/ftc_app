package org.firstinspires.ftc.teamcode.SubAssembly.DriveTrain;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Utilities.GamepadWrapper;

/* Sub Assembly Test OpMode
 * This TeleOp OpMode is used to test the functionality of the specific sub assembly
 */
// Assign OpMode type (TeleOp or Autonomous), name, and grouping
@TeleOp(name = "Turn Test", group = "Test")
public class TurnTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        telemetry.addLine("Turn Test: ");
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
                Drive.turn2Angle(speed, -90);
            } else if (egamepad1.dpad_right.state) {
                Drive.turn2Angle(speed, 90);
            } else if (egamepad1.dpad_up.state) {
                Drive.turn2Angle(speed, 0);
            } else if (egamepad1.dpad_down.state) {
                Drive.turn2Angle(speed, 180);
            } else if (egamepad1.a.state) {
                Drive.turnAngle(speed, 30);
            } else if (egamepad1.b.state) {
                Drive.turnAngle(speed, -30);
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
