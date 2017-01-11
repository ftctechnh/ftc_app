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
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;


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



//This opmode lets the driver drive with only one stick. This means that we have a lot of buttons to map to other things.

@TeleOp(name="Bigly", group="Pushbot")
//@Disabled
public class InvadersPushbot_Iterative extends OpMode{

    /* Declare OpMode members. */
    InvadersVelocityVortexBot robot       = new InvadersVelocityVortexBot(); // use the class created to define a Pushbot's hardware
                                                         // could also use HardwarePushbotMatrix class.
    Gamepad lastGamePadState = new Gamepad();




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
        double x = gamepad1.left_stick_x; // Note: The joystick goes negative when pushed forwards, so negate it
        double y = -gamepad1.left_stick_y; // Note: The joystick goes negative when pushed right, so negate it

        // Algorithm for setting power to left/right motors based on joystick x/y values
        // note: The Range.clip function just ensures we stay between Â±100%
        left = Range.clip(y - x, -1, +1);
        right = Range.clip(y + x, -1, +1);

        // Call the setPower functions with our calculated values to activate the motors
        robot.leftMotor.setPower(left);
        robot.rightMotor.setPower(right);

        // Read our limit switch to see if the arm is too high
        boolean limitTriggered = robot.touchSensor.isPressed();

        if (limitTriggered) {
            robot.ballElevator.setPower(0);  //Elevator off
        }

        // Send telemetry message to signify robot running;
        telemetry.addData("left", "%.2f", left);
        telemetry.addData("right", "%.2f", right);
        telemetry.addData("touchSensor:", "%s", limitTriggered ? "Triggered" : "Open");
        telemetry.addData("ballElevator:", robot.ballElevator.getPower());
        telemetry.addData("beacon servos", "L: %.02f, R: %.02f", robot.beaconLeft.getPosition(), robot.beaconRight.getPosition());
        telemetry.addData("Color: ", "A:%03d,R%03d,G%03d,B%03d",
                robot.floorSensor.red(),
                robot.floorSensor.green(),
                robot.floorSensor.blue(),
                robot.floorSensor.alpha());
        telemetry.addData("UDS:", "%.2fcm", robot.UDS.getDistance(DistanceUnit.CM));
        telemetry.addData("ODS:", "%.2fpercent", robot.ODS.getLightDetected());
        updateTelemetry(telemetry);

        //Beacon button and pusher button
        robot.beaconLeft.setPosition(1 - gamepad1.left_trigger);
        robot.beaconRight.setPosition(1 - gamepad1.left_trigger);
        //arobot.sweeper.setPower(1 - gamepad1.right_trigger);

        if (gamepad1.a == true) {
            if (robot.touchSensor.isPressed() == true) {
                robot.setBallElevator(0);
            } else {

                robot.setBallElevator(-1); // Elevator down
            }
        } else if (gamepad1.y == true) {
            //robot.ballElevator.setPower(1);
            robot.setBallElevator(1);
        } else {
            robot.setBallElevator(0);
        }

        if (gamepad1.start == true) {
            //robot.leftBallLauncher.setPower(-1);
            //robot.rightBallLauncher.setPower(-1);
            robot.setLauncherPower(1);
        } else if (gamepad1.back == true) {
            //robot.leftBallLauncher.setPower(0);
            //robot.rightBallLauncher.setPower(0);
            robot.setLauncherPower(0);
            if (!limitTriggered) {
                robot.setBallElevator(-1);  //Elevator down
            }
        }
        //CapBall lifter
        if (gamepad1.dpad_up) {
            robot.setCapBallMotorPower(0.5);
        } else if (gamepad1.dpad_down) {
            robot.setCapBallMotorPower(-0.5);
        } else
        {
            robot.setCapBallMotorPower(0);
        }

        if (gamepad1.dpad_left) {
            robot.setSweeperPower(1);
        }
        else if (gamepad1.dpad_right) {
            robot.setSweeperPower(-1);
        }
        else {
            robot.setSweeperPower(0);
        }
    }




    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        // Reset/Stop the servos
        robot.beaconLeft.setPosition(0.5);
        robot.beaconRight.setPosition(0.5);
        robot.ballElevator.setPower(0);

        // Stop the motors
        robot.sweeper.setPower(0);
        robot.leftMotor.setPower(0.0);
        robot.rightMotor.setPower(0.0);
        robot.leftBallLauncher.setPower(0.0);
        robot.rightBallLauncher.setPower(0.0);
    }
}
