package org.firstinspires.ftc.teamcode.Matthew;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.InvadersVelocityVortexBot;

/**
 * Created by matth on 11/5/2016.
 */

public class Matthew_TeleOp {
    @TeleOp(name = "Matthew TeleOp", group = "Pushbot")
    @Disabled
    public class PushbotTeleopPOV_Linear extends LinearOpMode {

        /* Declare OpMode members. */
        InvadersVelocityVortexBot robot = new InvadersVelocityVortexBot();   // Invaders Robot HW
        // could also use HardwarePushbotMatrix class.
        double clawOffset = 0;                       // Servo mid position
        final double CLAW_SPEED = 0.02;                   // sets rate to move servo


        @Override
        public void runOpMode() throws InterruptedException {
            double left;
            double right;
            double max;

        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
            robot.init(hardwareMap);

            // Send telemetry message to signify robot waiting;
            telemetry.addData("Say", "Ready player 1");    //
            telemetry.update();

            // Wait for the game to start (driver presses PLAY)
            waitForStart();

            // run until the end of the match (driver presses STOP)

            int robotState = 0;

            /*

            0 = Moving elevator down until imaginary limit switch is pressed.
            1 = Opening pusher servo
            2 = Paused state
            3 = Move elevator up to the top.




             */

            while (opModeIsActive()) {

                // Run wheels in POV mode (note: The joystick goes negative when pushed forwards, so negate it)
                // In this mode the Left stick moves the robot fwd and back, the Right stick turns left and right.
                left = -gamepad1.left_stick_y + gamepad1.right_stick_x;
                right = -gamepad1.left_stick_y - gamepad1.right_stick_x;

                // Normalize the values so neither exceed +/- 1.0
                max = Math.max(Math.abs(left), Math.abs(right));
                if (max > 1.0) {
                    left /= max;
                    right /= max;
                }

                robot.leftMotor.setPower(left);
                robot.rightMotor.setPower(right);

                // Use gamepad left & right Bumpers to open and close the claw
                if (gamepad1.right_bumper)
                    clawOffset += CLAW_SPEED;
                else if (gamepad1.left_bumper)
                    clawOffset -= CLAW_SPEED;

                // Move both servos to new position.  Assume servos are mirror image of each other.
                clawOffset = Range.clip(clawOffset, -0.5, 0.5);
                //robot.leftClaw.setPosition(robot.MID_SERVO + clawOffset);
                //robot.rightClaw.setPosition(robot.MID_SERVO - clawOffset);

                // Use gamepad buttons to move arm up (Y) and down (A)
                //if (gamepad1.y)
                    //robot.//armMotor.setPower(robot.ARM_UP_POWER);
                //else if (gamepad1.a)
                    //robot.armMotor.setPower(robot.ARM_DOWN_POWER);
                //else if (gamepad1.x)
                    //robot.pusher.setPosition(1);
                //else if (gamepad1.b) {
                    //robot.ballElevator.setPosition(1);
                    sleep(3000);
                    //robot.ballElevator.setPosition(0.5);
                    sleep(500);
                    //robot.ballElevator.setPosition(0);
                    sleep(3000);
                }

                //else
                    //robot.armMotor.setPower(0.0);
                    //robot.pusher.setPosition(0);
                    //robot.ballElevator.setPosition(0.5);

                // Send telemetry message to signify robot running;
                telemetry.addData("claw", "Offset = %.2f", clawOffset);
                //telemetry.addData("left", "%.2f", left);
                //telemetry.addData("right", "%.2f", right);
                telemetry.update();

                // Pause for metronome tick.  40 mS each cycle = update 25 times a second.
                robot.waitForTick(40);

                //robot.

                switch (robotState){
                    case 0:
                    {
                        robot.ballElevator.setDirection(DcMotorSimple.Direction.FORWARD);
                    }
                    case 1:
                    {
                        //robot.pusher.setPosition(1);
                    }
                    case 2:
                    {
                        //Pause
                    }
                    case 3:
                    {
                        //robot.pusher.setPosition(0);
                    }



                }
            }
        }
    }

