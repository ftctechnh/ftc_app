package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import static org.firstinspires.ftc.teamcode.R.layout.servo;

/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a PushBot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="Servo Mover", group="Linear Opmode")  // @Autonomous(...) is the other common choice
//@Disabled
public class ServoMover extends LinearOpMode {
    public Servo servo = null;

    @Override
    public void runOpMode() throws InterruptedException {
        servo = hardwareMap.servo.get("servo");

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            float position = 0;

            if (gamepad1.a) {
                position = 1;
            } else if (gamepad1.y) {
                position = 0;
            }

            servo.setPosition(position);

            telemetry.addData("Status", "Running...");
            telemetry.addData("Servo", "%.2f", servo.getPosition());
            telemetry.update();

            idle(); // Always call idle() at the bottom of your while(opModeIsActive()) loop
        }
    }
}