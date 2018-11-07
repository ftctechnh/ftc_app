package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.SubAssembly.Claimer.ClaimerControl;
import org.firstinspires.ftc.teamcode.SubAssembly.DriveTrain.DriveControl;
import org.firstinspires.ftc.teamcode.SubAssembly.Lift.LiftControl;
import org.firstinspires.ftc.teamcode.Utilities.GamepadWrapper;

@TeleOp(name = "TeleOp", group = "Drive")
    public class teleop extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        //variables
        double speed = 1.0;

        //initializing subassemblies
        DriveControl Drive = new DriveControl();
        ClaimerControl Claimer = new ClaimerControl();
        LiftControl Lift = new LiftControl();

        //setting up gamepads
        GamepadWrapper egamepad1 = new GamepadWrapper(gamepad1);
        GamepadWrapper egamepad2 = new GamepadWrapper(gamepad2);

        //waits for start
        waitForStart();

        //good ole "while opmode is active" loop so stuff doesn't kill itself
        while (opModeIsActive()) {

            //update gamepads
            egamepad1.updateEdge();
            egamepad2.updateEdge();

            //displays speed for drivers
            telemetry.addLine("Speed: " +speed);
            telemetry.update();

            //speed control
            if (egamepad1.right_bumper.pressed) {
                speed += 0.25;
                if (speed > 3) speed = 3;
            }
            if (egamepad1.left_bumper.pressed) {
                speed -= 0.25;
                if (speed < 0) speed = 0;
            }

            /*drive controls! this tells the robot which inputs control
             which outputs when moving the robot from here to there*/
            if (egamepad1.dpad_up.state) {
                Drive.moveForward(speed);
            } else if (egamepad1.dpad_down.state) {
                Drive.moveBackward(speed);
            } else if (egamepad1.dpad_left.state) {
                Drive.turnLeft(speed);
            } else if (egamepad1.dpad_right.state) {
                Drive.turnRight(speed);
            } else {
                Drive.tankLeft(-gamepad1.right_stick_y);
                Drive.tankRight(-gamepad1.right_stick_y);
            }

            telemetry.update();

            //"Sleeeeeeeep" -Green Goblin, played by Willem Dafoe, Spider-Man, 2002, directed by Sam Raimi
            sleep(40);
        }
    }
}

