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

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.unknownelementsamples.HardwarePushbot_demo;

@TeleOp(name="Template: IO 08", group="Iterative Opmode")  // @Autonomous(...) is the other common choice
// @Disabled
public class TeleOp_TT_2 extends OpMode {
    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();
    HardwarePushbot_TT         robot   = new HardwarePushbot_TT();   // Use a Pushbot's hardware

    static final double     COUNTS_PER_MOTOR_REV    = 1120 ;    // eg: ANDY Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
//    static final double     DRIVE_SPEED             = 0.6;
//    static final double     TURN_SPEED              = 0.6;
//    static final double     PUSH_SPEED             = 0.1;

    private double          leftMotorSpeed = 0.0;               // remember what was requested based on joystick position
    private double          rightMotorSpeed = 0.0;               // remember what was requested based on joystick position

    private double          minimumDeadZone = 0.05;             // adjust this value to increase or descrease the deadzone
    private double          maxMotorSpeed = 0.95;             // adjust this value to set the maximum motor speed, depends on motor type

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        /* eg: Initialize the hardware variables.          */
        // @todo add all additional initalization for hardware here
        robot.init(hardwareMap); // function for init drivetrain/servos **does not handle any sensors!!**

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
        runtime.reset();

        // @todo add all one time 'get ready to run' here

    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {

        telemetry.addData("Status", "Running: " + runtime.toString());

        handleControls();    // function to read all input controls and set globals here
        handleDrivetrain();    //  function to handle drivetrain changes here
        handleFeatures();    //  function to handle auxillary hardware features here
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {

        // @todo add function to clean up status of robot

    }


    private void handleControls() { // @todo add code to read joysticks
        // Run wheels in tank mode (note: The joystick goes negative when pushed forwards, so negate it)
        double left = 0.0;
        double right = 0.0;

        left = -gamepad1.left_stick_y;   // (note: The joystick goes negative when pushed forwards, so negate it)
        right = -gamepad1.right_stick_y;

        left = enforceDeadZone(left);   // don't move unless far enought from zero
        right = enforceDeadZone(right);    // because physical 'dead stick' mayn not be seen as zero


        leftMotorSpeed = left;
        rightMotorSpeed = right;

    }


    private void handleDrivetrain() { // @todo add code to update drivetrain state

        robot.backLeftMotor.setPower(leftMotorSpeed);
        robot.frontLeftMotor.setPower(leftMotorSpeed);
        robot.backRightMotor.setPower(rightMotorSpeed);
        robot.frontRightMotor.setPower(rightMotorSpeed);

    }


    private void handleFeatures() {  // @todo add code to update aux features state


    }

    private double enforceDeadZone(double joystickPos) {

        //  physical 'dead stick' mayn not be seen as zero, so need to eliminate anything below a threshhold

        double minimumDeadZone = 0.05;      // adjust this value to increase or descrease the deadzone

        if (joystickPos >= 0) {      // handle positive and negative separately

            if (joystickPos < minimumDeadZone) {
                joystickPos = 0;        // less than minimum from zero, so set to zero
            }

        }   else    {               // not positive, so must be negative

            if (joystickPos > -1 * minimumDeadZone) {
                joystickPos = 0;        // more than minimum from zero, so set to zero
            }
        }

        return (joystickPos);
    }

    private double scaleMotorPower(double motorpower) {

        //  need to compensate for deadzone
        // and use an acceleration curve

        if (motorpower >= 0) {      // handle positive and negative separately

            motorpower -= minimumDeadZone;  // remove deadzone offzet, otherwise can't represent a power less than deadzone
            motorpower = motorpower * motorpower;    // square motorpower to generate the acceleration curve

        }   else    {               // not positive, so must be negative

            motorpower += minimumDeadZone;              // remove deadzone offzet, otherwise can't represent a power less than deadzone
            motorpower = motorpower * motorpower;       // square motorpower to generate the acceleration curve
            motorpower = -1 * motorpower;               // put back the sign lostg when squaring the value
        }

        return (motorpower);
    }
}

