package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
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
        double IntakeOut; // intake motor spinning out --->
        double IntakeIn; // intake motor spinning in  <---
        // double Aim;


        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Run wheels in tank mode (note: The joystick goes negative when pushed forwards, so negate it)

            IntakeOut = gamepad1.left_trigger;         //Intake direction ---> (no negative before gampad2
            //because we want the motor to spin in the opposite direction
            // of IntakeI)
            IntakeIn = -gamepad1.right_trigger;       //Intake direction <---
            //Aim = -gamepad2.right_stick_y / 2;

            Left  = -gamepad1.left_stick_y;
            Right = -gamepad1.right_stick_y;

               if (gamepad1.left_bumper){
                   robot.leftshooter.setPower(-1);
                   robot.rightshooter.setPower(1);
               }else {
                   robot.leftshooter.setPower(0);
                   robot.rightshooter.setPower(0);
               }

                robot.leftMotor.setPower(Left);
                robot.rightMotor.setPower(Right);

            if (gamepad1.right_trigger >= .1) {           //The if/else statement is a safety mechanism so that the
                //program doesn't crash in the event that both triggers
                //are pressed simultaneously
                robot.Intake.setPower(IntakeIn);

            } else {
                robot.Intake.setPower(IntakeOut);

            }
            if (gamepad1.right_bumper){
                robot.servo.setPosition(0);


            } else {
                robot.servo.setPosition(.8);
            }


            //robot.AimingMotor.setPower(Aim);

            //if (gamepad2.right_bumper) {
            //robot.shootMotorL.setPower(1);
            //robot.shootMotorR.setPower(1);

            //}

            //if (gamepad2.dpad_left) {
            //      robot.Press_left.setPosition(press_l);

            // }

            //if (gamepad2.dpad_right) {
            // robot.Press_right.setPosition(press_r);

            // }
        }
    }
}