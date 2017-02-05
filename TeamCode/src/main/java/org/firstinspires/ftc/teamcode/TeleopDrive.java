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
        double ServoFlipper = 0.8; //equals bottom
        double ServoButtonPusher = 0.0; //equals in position

        // double Aim;


        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

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
            if (gamepad1.right_bumper){
                robot.servo.setPosition(0);
            } else {
                robot.servo.setPosition(.8);
            }

            //Controlling the drive wheels with a range (robot moves when sticks are moved up and down)
            Left  = -gamepad1.left_stick_y;
            Right = -gamepad1.right_stick_y;
            robot.leftMotor.setPower(Left);
            robot.rightMotor.setPower(Right);

            /**
             * Controller 2  mapping starts here
             * */
            //Spin wheels for shooter
            if (gamepad2.right_bumper){
                //Full power when button is pushed
                robot.leftshooter.setPower(-1);
                robot.rightshooter.setPower(1);
            }else {
                //No power when button is not pushed
                robot.leftshooter.setPower(0);
                robot.rightshooter.setPower(0);
            }

            //Move servo(flipper) for shooter
            //Right Trigger
            //Servo range factor is 0.8/1.0
            //Top position = Up = 0.0
            //Bottom position = Down = 0.8
            //Servo range = Range factor * (1.0 - Trigger Value)
            ServoFlipper = 0.8 * (1.0 - gamepad2.right_trigger);
            robot.servo.setPosition(Range.clip(ServoFlipper, 0.0, 0.8));

            //Button pusher servo (2nd) for beacon
            //Left Trigger
            //Servo range factor is 1.0
            //Servo range = Range factor * Trigger Value)
            ServoButtonPusher = 1.0 * gamepad2.left_trigger;
            robot.servo.setPosition(Range.clip(ServoButtonPusher,0.0,1.0));





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