/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.firstinspires.ftc.team8200;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;


/**
 * This OpMode uses the common HardwareK9bot class to define the devices on the robot.
 * All device access is managed through the HardwareK9bot class. (See this class for device names)
 * The code is structured as a LinearOpMode
 *
 * This particular OpMode executes a basic Tank Drive Teleop for the K9 bot
 * It raises and lowers the arm using the Gampad Y and A buttons respectively.
 * It also opens and closes the claw slowly using the X and B buttons.
 *
 * Note: the configuration of the servos is such that
 * as the arm servo approaches 0, the arm position moves up (away from the floor).
 * Also, as the claw servo approaches 0, the claw opens up (drops the game element).
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="K9bot: Telop Tank", group="K9bot")
//@Disabled
public class K9botTeleopTank_Linear extends LinearOpMode {

    /* Declare OpMode members. */
    HardwareK9bot   robot           = new HardwareK9bot();              // Use a K9'shardware
    //double          armPosition     = robot.ARM_HOME;                   // Servo safe position
    //double          clawPosition    = robot.CLAW_HOME;                  // Servo safe position
    //final double    CLAW_SPEED      = 0.01 ;                            // sets rate to move servo
    //final double    ARM_SPEED       = 0.01 ;                            // sets rate to move servo

    @Override
    public void runOpMode() {
        double leftY;
        //double leftX;
        //double rightX;
        double rightY;
        double armPosition = -1;
        double armHitPosition = 1;

        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Hello BASE Student");    //
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Run wheels in tank mode (note: The joystick goes negative when pushed forwards, so negate it)
            leftY = -gamepad1.left_stick_y;
            rightY = -gamepad1.right_stick_y;`
            //leftX = Range.clip(leftY, -1, 1);
            rightY = Range.clip(rightY, -1, 1);
            leftY = Range.clip(leftY, -1, 1);
            robot.leftMotor.setPower(leftY);
            robot.rightMotor.setPower(rightY);
            //leftX = gamepad1.left_stick_x;
            //rightX = gamepad1.right_stick_x;
            //rightX = Range.clip(rightX, -1, 1);
            //leftX = Range.clip(leftX, -1, 1);
            float harvesterPower = gamepad1.right_trigger;
            float harvesterPowerReversed = gamepad1.left_trigger;

            if (harvesterPower > 0.2) {
                if (gamepad1.right_bumper) {
                    robot.harvester.setPower(1);
                }
                else {
                    robot.harvester.setPower(0.5);
                }
            }
            else if (harvesterPowerReversed > 0.2) {
                if (gamepad1.left_bumper) {
                    robot.harvester.setPower(-1);
                }
                else {
                    robot.harvester.setPower(-0.5);
                }
            }
            else {

                robot.harvester.setPower(0);

            }

            if (gamepad1.dpad_up) {
                robot.arm.setPosition(armHitPosition);
            }
            else if(gamepad1.dpad_down) {
                robot.arm.setPosition(armPosition);

            }

            //robot.leftMotor.setPower(leftY);
            //robot.rightMotor.setPower(rightY);

            /*
             left = the power x
             right = the powerx
            leftup = the power y
            right up = the power y
            if(left > 0 || right > 0)
            {
            left.motor.setPower(1)
            rightMotor.setPower(1)
            }
            if(left < 0 || right < 0)
            {
            left.motor.setPower(-1)
            rightMotor.setPower(-1)
            }
            if(leftup > 0)
            {
            LEFTmotor.setPower(leftup)
            rightMotorsetpOWER(leftUp*-1)
            }
            f(rightUp > 0)
            {
            rightmotor.setPower(rightUp)
            leftMotorsetpOWER(rightUp*-1)
            }
            if(leftup < 0)
            {
            LEFTmotor.setPower(leftup)
            rightMotorsetpOWER(leftUp*-1)
            }
            f(rightUp < 0)
            {
            rightmotor.setPower(rightUp)
            leftMotorsetpOWER(rightUp*-1)
            }
                         */

            /*if(leftY > 0 && rightY >0)
            {
                robot.leftMotor.setPower(1);
                robot.rightMotor.setPower(1)  ;
            }
            else if(leftY < 0 && rightY < 0)
            {
                robot.leftMotor.setPower(-1);
                robot.rightMotor.setPower(-1)  ;
            }

            else if(rightX > 0)
            {
                robot.leftMotor.setPower(0.5);
                robot.rightMotor.setPower(-0.5)  ;
            }
            else if(rightX < 0)
            {
                robot.leftMotor.setPower(-0.5);
                robot.rightMotor.setPower(0.5)  ;
            }

            else if(leftX > 0)
            {
                robot.leftMotor.setPower(-0.5);
                robot.rightMotor.setPower(0.5)  ;
            }
            else if(leftX < 0)
            {
                robot.leftMotor.setPower(0.5);
                robot.rightMotor.setPower(-0.5)  ;
            }
            else
            {
                robot.leftMotor.setPower(0);
                robot.rightMotor.setPower(0)  ;
            }


            // Use gamepad Y & A raise and lower the arm
           /* if (gamepad1.a)
                armPosition = 0.9;
            else if (gamepad1.y)
                armPosition = 0.1;

            // Use gamepad X & B to open and close the claw
            /*if (gamepad1.x)
                clawPosition += CLAW_SPEED;
            else if (gamepad1.b)
                clawPosition -= CLAW_SPEED;
                */

            // Move both servos to new position.
           // armPosition  = Range.clip(armPosition, robot.ARM_MIN_RANGE, robot.ARM_MAX_RANGE);
            //robot.arm.setPosition(armPosition);
            /*clawPosition = Range.clip(clawPosition, robot.CLAW_MIN_RANGE, robot.CLAW_MAX_RANGE);
            robot.claw.setPosition(clawPosition);

            // Send telemetry message to signify robot running;
            telemetry.addData("arm",   "%.2f", armPosition);
            telemetry.addData("claw",  "%.2f", clawPosition);

            */
           // telemetry.addData("leftX",  "%.2f", leftX);
           // telemetry.addData("righXt", "%.2f", rightX);
            telemetry.addData("leftY",  "%.2f", leftY);
            telemetry.addData("rightY", "%.2f", rightY);
            telemetry.update();

            // Pause for metronome tick.  40 mS each cycle = update 25 times a second.
            robot.waitForTick(40);
        }
    }
}
