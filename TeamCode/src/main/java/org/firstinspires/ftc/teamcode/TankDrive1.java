package org.firstinspires.ftc.teamcode;
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

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

//import org.firstinspires.ftc.robotcontroller.external.samples.HardwareK9bot;

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

@TeleOp(name="Golden Eagles: TankDrive1", group="K9bot")
//@Disabled
public class TankDrive1 extends LinearOpMode {

    // Declare OpMode members.
    HardwareK9bot robot = new HardwareK9bot();       // Use a K9'shardware

    @Override
    public void runOpMode() {
        
        // Some variables to initialize
        double left, right, leftShooter, rightShooter, harvester;
        
        // The position of the "trigger" servo
        double triggerPos = 0.2;
        
        // The on / off status of the harvestor and wheel shooters
        boolean harvesterOn = false;
        boolean shootersOn = false;
        
        // The previous state of 2's "A" and "Y" button
        boolean GP2a = false;
        boolean GP2y = false;
        

        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Hello Driver");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Run wheels in tank mode (note: The joystick goes negative when pushed forwards, so negate it)
            left = gamepad1.left_stick_y;
            right = gamepad1.right_stick_y;
            robot.leftMotor.setPower(left);
            robot.rightMotor.setPower(right);


            /*
            If gamepad 2 was false before, but is now true,
            toggle the state of the harvester.
            
            If it was true before, this doesn't do anything.
            */
            if (!GP2a && gamepad2.a) {
                harvesterOn = !harvestorOn;
            }
            
            // Same thing but with the wheel shooters
            if (!GP2y && gamepad2.y) {
                shootersOn = !shootersOn;
            }
            
            
            // Turn harvester on based on harvesterOn variable
            if (harvesterOn) {
                robot.harvester.setMotorPower(1, -1);
            }
            else {
                robot.harvester.setMotorPower(1, 0);
            }
            
            // Turn shooters on based on shootersOn variable
            if (shootersOn) {
               robot.leftShooter.setPower(-1);
               robot.rightShooter.setPower(-1);
            }
            else {
               robot.leftShooter.setPower(0);
               robot.rightShooter.setPower(0);
            }

            
            // Increase "trigger" servo position when the left stick is up
            // Decrease when the stick is down
            if (gamepad2.left_stick_y > 0.2) {
                if (triggerPos != 1.0) {
                    triggerPos += 0.02;
                }
            }
            if (gamepad2.left_stick_y < -0.2) {
                if (triggerPos != -1.0) {
                    triggerPos -= 0.02;
                }
            }

            // Set position of "trigger" servo
            robot.trigger.setPosition(triggerPos);

            // Make the "now" into the "previous" for the next loop.
            GP2a = gamepad2.a;
            GP2y = gamepad2.y;


            // Get the motors' power (for telemetry)
            leftShooter = robot.leftShooter.getPower();
            rightShooter = robot.rightShooter.getPower();

            // Send telemetry message to signify robot running;
            telemetry.addData("left",  "%.2f", left);
            telemetry.addData("right", "%.2f", right);
            telemetry.addData("left shooter", "%.2f", leftShooter);
            telemetry.addData("right shooter", "%.2f", rightShooter);
            telemetry.addData("trigger", "%.2f", triggerPos);

            telemetry.update();

            // Pause for metronome tick.  40 mS each cycle = update 25 times a second.
            robot.waitForTick(40);
        }
    }
}
