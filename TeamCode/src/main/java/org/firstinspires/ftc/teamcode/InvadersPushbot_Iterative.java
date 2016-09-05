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
package org.firstinspires.ftc.teamcode;
import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.robotcore.util.Range;


/**
 * This file provides Telop driving for the IfSpace Invaders 2015/16 Pushbot robot.
 * The code is structured as an Iterative OpMode
 *
 * This OpMode uses the common Pushbot hardware class to define the devices on the robot.
 * Most device access is managed through the HardwarePushbot class.  A touch sensor was added to
 * limit our robot arm range of motion and is manually connected to the robot during init().
 *
 * This particular OpMode provides single-thumb navigation of PushBot using the left-thumbstick.
 * It raises and lowers the claw using the right-thumbstick.
 * It also opens and closes the claws slowly using the left and right Trigger buttons.
 *
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="Invaders: Pushbot Teleop", group="Pushbot")
//@Disabled
public class InvadersPushbot_Iterative extends OpMode{

    /* Declare OpMode members. */
    HardwarePushbot robot       = new HardwarePushbot(); // use the class created to define a Pushbot's hardware
                                                         // could also use HardwarePushbotMatrix class.
    double          clawOffset  = 0.0 ;                  // Servo mid position
    final double    CLAW_SPEED  = 0.02 ;                 // sets rate to move servo
    TouchSensor     limitSwitch;                         // Will be connected to PushBot's Limit Switch

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Connect our limit switch TouchSensor object to the Robot
        limitSwitch = hardwareMap.touchSensor.get("arm limit");
        assert(limitSwitch != null);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Prepare to be Invaded");
        updateTelemetry(telemetry);
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        double left;
        double right;

        // Use the left joystick to move the robot forwards/backwards and turn left/right
        double x = -gamepad1.left_stick_x; // Note: The joystick goes negative when pushed forwards, so negate it
        double y = -gamepad1.left_stick_y; // Note: The joystick goes negative when pushed right, so negate it

        // Algorithm for setting power to left/right motors based on joystick x/y values
        // note: The Range.clip function just ensures we stay between Â±100%
        left = Range.clip(y-x, -1, +1);
        right = Range.clip(y+x, -1, +1);

        // Call the setPower functions with our calculated values to activate the motors
        robot.leftMotor.setPower(left);
        robot.rightMotor.setPower(right);

        // Use gamepad left & right triggers to open and close the claw
        if (gamepad1.right_trigger > 0)
            clawOffset += CLAW_SPEED;
        else if (gamepad1.left_trigger > 0)
            clawOffset -= CLAW_SPEED;

        // Move both servos to new position.  Assume servos are mirror image of each other.
        clawOffset = Range.clip(clawOffset, -0.5, 0.5);
        robot.leftClaw.setPosition(robot.MID_SERVO + clawOffset);
        robot.rightClaw.setPosition(robot.MID_SERVO - clawOffset);

        // Read our limit switch to see if the arm is too high
        boolean limitTriggered = limitSwitch.isPressed();

        // Use right joystick the arm up (as long as our limit switch hasn't been triggered) or down
        if ((gamepad1.right_stick_y > 0) && !limitTriggered)
            robot.armMotor.setPower(robot.ARM_UP_POWER);
        else if (gamepad1.right_stick_y < 0)
            robot.armMotor.setPower(robot.ARM_DOWN_POWER);
        else
            robot.armMotor.setPower(0.0);

        // Send telemetry message to signify robot running;
        telemetry.addData("claw",  "Offset = %.2f", clawOffset);
        telemetry.addData("left",  "%.2f", left);
        telemetry.addData("right", "%.2f", right);
        telemetry.addData("switch", "%s", limitTriggered ? "Triggered" : "Open");
        updateTelemetry(telemetry);
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

}
