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

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;

/**
 * This file provides basic Telop driving for a iRads robot.
 * Derived from a TeleopTank example for a Pushbot robot.
 * The code is structured as an Iterative OpMode
 *
 * This OpMode uses the iRads hardware class to define the devices on the robot.
 * All device access is managed through the Hardware_iRads class.
 *
 * This particular OpMode executes a basic Tank Drive Teleop for a PushBot
 * It raises and lowers the forklift using the Gampad Y and A buttons respectively.
 * It also swings the button pusher Servo using the left and right Bumper buttons.
 *
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="iRads: Teleop Tank", group="iRads")
//@Disabled
public class iRadsTeleopTank_Iterative extends OpMode{

    /* Declare OpMode members. */
    Hardware_iRads robot       = new Hardware_iRads();   // use the class created to define iRads hardware
                                                         // could also use HardwarePushbotMatrix class.
    double          buttonOffset  = 0.0 ;                // Servo mid position
    final double    BUTTON_SPEED  = 0.04 ;               // sets rate to move servo


    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Hello Driver");    //
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

        // Run wheels in tank mode (note: The joystick goes negative when pushed forwards, so negate it)
        left = -gamepad1.left_stick_y;
        right = -gamepad1.right_stick_y;
        robot.leftDriveMotor.setPower(left);
        robot.rightDriveMotor.setPower(right);

        // Use gamepad left & right Bumpers to swing the buttonPusher right or  left.
        if (gamepad1.right_bumper)
            buttonOffset += BUTTON_SPEED;
        else if (gamepad1.left_bumper)
            buttonOffset -= BUTTON_SPEED;

        // Move servo to new position.
        buttonOffset = Range.clip(buttonOffset, -0.5, 0.5);
        robot.buttonPusher.setPosition(robot.MID_SERVO + buttonOffset);

        // Use gamepad buttons to move the fork lift up (Y) and down (A)
        if (gamepad1.y)
            robot.liftMotor.setPower(1.0);  // Lift up, full speed
        else if (gamepad1.a)
            robot.liftMotor.setPower(-1.0); // Lift down, full speed
        else
            robot.liftMotor.setPower(0.0);

        // Send telemetry message to signify robot running;
        telemetry.addData("button",  "Offset = %.2f", buttonOffset);
        telemetry.addData("left",  "%.2f", left);
        telemetry.addData("right", "%.2f", right);
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

}
