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

/**

                What the WHOLE teleop driver mode does:


 - Driver 1 actually moves the robot using the left and right sticks for the
   left and right motors, respectively.
 - Driver 1 can use up on the dpad to move both wheels on the robot forward,
   and down for backwards.
 
 - Driver 2 controls the rest of the robot                                    
 - Driver 2 toggles the harvester and wheel shooters on and off using the "A"
   and "Y" buttons, respectively.
 - Driver 2 controls the trigger (servo) using up and down of the dpad.
 
 */

@TeleOp(name="Golden Eagles: TeleOp", group="K9bot")
//@Disabled
public class TankDrive1 extends LinearOpMode {

    // Use robot hardware specified in the file "HardwareK9bot.java".
    HardwareK9bot robot = new HardwareK9bot();

    @Override
    public void runOpMode() {
        
        
        // Some variables to initialize
        double left, right;
        
        // The position of the "trigger" servo
        double triggerPos = 1.0;
        
        // The on / off status of the harvester and wheel shooters
        boolean harvesterOn = false;
        boolean shootersOn = false;
        
        // The previous state of gamepad2's "A" and "Y" buttons
        boolean GP2a = false;
        boolean GP2y = false;
        

        // Initialize the hardware variables.
        robot.init(hardwareMap);


        // Send telemetry message to signify robot ready.
        telemetry.addData("Say", "Hello Driver");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();


        // Run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            
            /*
            If up is pressed on gamepad1's DPAD, move forward.
            If down is pressed on the DPAD, move backward.
            If neither, move using gamepad1's left and right sticks.
            */
            if (gamepad1.dpad_up) {
                left = 1;
                right = 1;
            }
            else if (gamepad1.dpad_down) {
                left = -1;
                right = -1;
            }
            else {
                left = gamepad1.left_stick_y;
                right = gamepad1.right_stick_y;
            }
            
            robot.leftMotor.setPower(left);
            robot.rightMotor.setPower(right);


            /*
            If a button on gamepad2 wasn't pressed during the last loop,
            but is being pressed in this loop, toggle the state of the motors.
            */
            if (!GP2a && gamepad2.a) {
                harvesterOn = !harvesterOn;
            }
            if (!GP2y && gamepad2.y) {
                shootersOn = !shootersOn;
            }
            
            
            // Turn the motors on or off based on their state.
            if (harvesterOn) {
                robot.harvester.setMotorPower(1, -1);
            }
            else {
                robot.harvester.setMotorPower(1, 0);
            }
            if (shootersOn) {
               robot.leftShooter.setPower(-1);
               robot.rightShooter.setPower(-1);
            }
            else {
               robot.leftShooter.setPower(0);
               robot.rightShooter.setPower(0);
            }

            
            /*
            Increase "trigger"'s' servo position when up is pressed on the DPAD.
            Decrease the position when down is pressed on the DPAD.
            */
            if (gamepad2.dpad_up) {
                triggerPos += 0.05;
            }
            else if (gamepad2.dpad_down) {
                triggerPos -= 0.05;
            }
            
            
            // If it exceeds below 0 or above 1, set it to 0 or 1, respectively.
            if (triggerPos < 0) {
                triggerPos = 0;
            }
            else if (triggerPos > 1) {
                triggerPos = 1;
            }


            // Set position of the "trigger" servo
            robot.trigger.setPosition(triggerPos);

            /*
            Make the "now" into the "before" for the next loop.
            If that makes sense.
            */
            GP2a = gamepad2.a;
            GP2y = gamepad2.y;
            

            // Send telemetry message of robot data.
            telemetry.addData("Left wheel power",  "%.2f", left);
            telemetry.addData("Right wheel power", "%.2f", right);
            telemetry.addData("Harvester", (harvesterOn) ? "On" : "Off");
            telemetry.addData("Wheel shooters", (shootersOn) ? "On" : "Off");
            telemetry.addData("Trigger position", "%.2f", triggerPos);
            telemetry.update();
            

            // Pause for metronome tick.  40 mS each cycle = update 25 times a second.
            robot.waitForTick(40);
            
            
        }
        
        
    }
    
}
