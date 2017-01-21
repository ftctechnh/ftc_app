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
package org.firstinspires.ftc.teamcode.Matthew;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.InvadersVelocityVortexBot;


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

@TeleOp(name="Platinum  Wumpus", group="Pushbot")
//@Disabled
public class PlatinumWumpus extends OpMode {

    /* Declare OpMode members. */
    InvadersVelocityVortexBot robot = new InvadersVelocityVortexBot(); // Invaders Robot HW
    // could also use HardwarePushbotMatrix class.
    double clawOffset = 0.0;                  // Servo mid position
    final double CLAW_SPEED = 0.02;                 // sets rate to move servo
    //TouchSensor limitSwitch;                         // Will be connected to PushBot's Limit Switch
    int robotState = -1;
    double DriveSpeedReduction = 1;
    double LiftSpeedReduction = 1;


    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(this);

        // Send telemetry message to signify robot waiting;
        updateTelemetry(telemetry);
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
        telemetry.addData("HEADING", "%d", robot.gyro.getIntegratedZValue());
        telemetry.addData("BEACON SENSOR", "R: = %d, L: %d", robot.beaconSensorRight.alpha(), robot.beaconSensorLeft.alpha());
//        telemetry.addData(">", "doIseeBlueLeft = %s [%d]",   robot.soIseeBlueLeft() ? "YES" : "NO", robot.beaconSensorLeft.blue());
//        telemetry.addData(">", "doIseeRedLeft = %s [%d]",   robot.doIseeRedLeft() ? "YES" : "NO", robot.beaconSensorLeft.red());
        telemetry.addData("RANGE: ","R: %.02f, L: %.02f", robot.UDSRight.getDistance(DistanceUnit.CM), robot.UDSLeft.getDistance(DistanceUnit.CM));
        telemetry.update();
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
        //This makes the slow mode possible by reducing the speed of all of the motors.
        double left;
        double right;
        //Gamepad 1 handles EVERYTHING


        // Speed Mode
        if (gamepad1.x == true) {
            DriveSpeedReduction = 1;
            LiftSpeedReduction = 1;
        }
        else if (gamepad1.b == true) {
            //DO NOT SET THIS TO 0! 1 = ZERO REDUCTION IN SPEED.
            DriveSpeedReduction = 9;
            LiftSpeedReduction = 5;
        }
        else{
            //Nothing is being pressed, don't change the value.
        }

        // Shooter Motor - Enable/Disable
        if(gamepad1.start) {
            robot.setLauncherState(InvadersVelocityVortexBot.LauncherState.ON);
        } else if(gamepad1.back) {
            robot.setLauncherState(InvadersVelocityVortexBot.LauncherState.OFF);
        }

        // Sweeper Controls
        if(gamepad1.right_bumper == true) {
            robot.sweeper.setPower(-1);   // Sweep Balls In
        } else {
            robot.sweeper.setPower(gamepad1.right_trigger); // Push Balls Out (Speed proportional to trigger)
        }

        // Ball Elevator Controls

        robot.ballElevator.setPower(gamepad1.right_stick_y); //Elevator Down (Speed proportional to trigger)



        // Use the left joystick to move the robot forwards/backwards and turn left/right
        //// TODO: 1/12/2017 Fix this so that we drive with two sticks instead of one. Finally.
        double x = -gamepad1.left_stick_x; // Note: The joystick goes negative when pushed forwards, so negate it
        double y = -gamepad1.left_stick_y; // Note: The joystick goes negative when pushed right, so negate it

        // Algorithm for setting power to left/right motors based on joystick x/y values
        // note: The Range.clip function just ensures we stay between Â±100%
        left = Range.clip(y - x, -1, +1);
        right = Range.clip(y + x, -1, +1);

        //Control system for the beacon pushers both tied to left trigger
        robot.beaconRight.setPosition(gamepad1.left_trigger);
        robot.beaconLeft.setPosition(gamepad1.left_trigger);

        if(gamepad1.y == true) {
            robot.setCapBallMotorPower(1 / LiftSpeedReduction, InvadersVelocityVortexBot.CapBallState.UP);
        }
        else if (gamepad1.right_stick_button == true && gamepad1.left_stick_button == true) {
            robot.setCapBallMotorPower(-0.2, InvadersVelocityVortexBot.CapBallState.DOWN);
        }
        else {
            robot.setCapBallMotorPower(0, InvadersVelocityVortexBot.CapBallState.OFF);
        }

        // Call the setPower functions with our calculated values to activate the motors
        left = left / DriveSpeedReduction;
        right = right / DriveSpeedReduction;
        robot.setDriveTrainPower(left,right);

        if(gamepad1.left_bumper == true){
            robot.AlignToWall(3, DistanceUnit.INCH);
        }
    }

    @Override
    public void stop() {
        robot.stop();
    }
}
