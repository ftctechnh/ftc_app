package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Connor on 1/28/2017.
 */


@TeleOp(name = "Shooter", group = "Pushbot")
//@Disabled
    public class TeleopDrive extends LinearOpMode {
    // Use a Pushbot's hardware


    @Override
    public void runOpMode() throws InterruptedException {
        ROUSAutoHardware_WithServos robot = new ROUSAutoHardware_WithServos();

        double Left;
        double Right;
        double IntakeOut; // intake motor spinning out --->(reverse intake)
        double IntakeIn; // intake motor spinning in  <---(forward intake)
        //double ServoFlipper = 0.8; //equals bottom
        // double ServoButtonPusher = 0.0; //equals in position

        // double Aim;


        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);
        robot.button.setPosition(.56);
        robot.servo.setPosition(.8);
        robot.leftshooter.setPower(-1);
        robot.rightshooter.setPower(1);
        sleep(1000);
        robot.leftshooter.setPower(0);
        robot.rightshooter.setPower(0);
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            /**
             * Controller 1 mapping starts here
             * */
            // Run wheels in tank mode (note: The joystick goes negative when pushed forwards, so negate it)

            IntakeOut = gamepad1.left_trigger;         //Intake direction --->(reverse intake) (no negative before gampad2
            //because we want the motor to spin in the opposite direction
            // of IntakeI)
            IntakeIn = -gamepad1.right_trigger;       //Intake direction <---(forward intake)
            //Aim = -gamepad2.right_stick_y / 2;

            if (gamepad1.right_trigger >= .1) {           //The if/else statement is a safety mechanism so that the
                //program doesn't crash in the event that both triggers
                //are pressed simultaneously
                robot.Intake.setPower(IntakeIn);
            } else {
                robot.Intake.setPower(IntakeOut);
            }


            //Controlling the drive wheels with a range (robot moves when sticks are moved up and down)
            Left = -gamepad1.left_stick_y;
            Right = -gamepad1.right_stick_y;
            if (gamepad1.right_bumper) {

                robot.leftMotor.setPower(Left * .2);
                robot.rightMotor.setPower(Right * .2);

            } else {
                robot.leftMotor.setPower(Left);
                robot.rightMotor.setPower(Right);
            }
            /**
             * Controller 2  mapping starts here
             * */

            //Spin wheels for shooter
            if (gamepad2.right_bumper) {
                //Full power when button is pushed
                robot.leftshooter.setPower(-1);
                robot.rightshooter.setPower(1);
            } else {
                //No power when button is not pushed
                robot.leftshooter.setPower(0);
                robot.rightshooter.setPower(0);
            }

            //Button Press
            if (gamepad2.a) {
                robot.button.setPosition(.76);
            } else {
                robot.button.setPosition(.56);
            }

            // Shooter servo flipper
            if (gamepad2.left_bumper) {
                robot.servo.setPosition(.4);
            } else {
                robot.servo.setPosition(.8);
            }

            telemetry.addData( "Button", "Val: %.04f", robot.button.getPosition());
            telemetry.addData( "Flipper", "Val: %.04f", robot.servo.getPosition());
            telemetry.update();
        }
    }
}
