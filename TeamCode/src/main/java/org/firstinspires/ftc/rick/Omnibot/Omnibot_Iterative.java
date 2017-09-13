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
package org.firstinspires.ftc.rick.Omnibot;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

/**
 * This file provides basic Telop driving for a Pushbot robot.
 * The code is structured as an Iterative OpMode
 *
 * This OpMode uses the common Pushbot hardware class to define the devices on the robot.
 * All device access is managed through the HardwarePushbot class.
 *
 * This particular OpMode executes a basic Tank Drive Teleop for a PushBot
 * It raises and lowers the claw using the Gampad Y and A buttons respectively.
 * It also opens and closes the claws slowly using the left and right Bumper buttons.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="Omnibot: !!", group="1")
@Disabled
public class Omnibot_Iterative extends OpMode {

    /* Declare OpMode members. */
    HardwareOmnibot robot       = new HardwareOmnibot();
    // use the class created to define a Omnibot's hardware
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
        if(gamepad1.left_trigger>0)
        {
            //Slow speed if left bumper id pressed
            robot.speed = (float) 0.1;

        }
        else if(gamepad1.left_bumper)
        {
            //Set speed to fast
            robot.speed = (float) 0.5;
        }
        else
        {
            robot.speed = (float) .25;
        }

		/*
		 * Gamepad 1
		 *
		 * Gamepad 1 controls the motors via the left stick, and it controls the
		 * wrist/claw via the a,b, x, y buttons
		 */

        // for omni drive,
        //Right stick
        // -y should spin all motors forward,
        // +y should spin all motors reverse

        // +X should spin  rightrear forward, left rear  reverse
        // +X should spin  rightfront Reverse,  Left Front forward
        // -X should spin  rightrear reverse, left rear  forward
        // -X should spin  rightfront forward,  Left Front reverse

        // left Stick
        // +x spins clockwise          LF RR  forward,  RF  LR reverse
        // -x spins counter clockwise  LF,RR  reverse   RF, LR Forward

        // throttle: left_stick_y ranges from -1 to 1, where -1 is full up, and
        // 1 is full down
        // direction: left_stick_x ranges from -1 to 1, where -1 is full left
        // and 1 is full right

        // X & Y on right stick contribute to all motors

        // Not sure about scaling,  proportion between motors is probably key,  so scaling the max to 1 and the others smaller might be good.

        float  RFrontspeed = -gamepad1.right_stick_y - (gamepad1.right_stick_x) ;
        float  LFrontspeed = -gamepad1.right_stick_y + (gamepad1.right_stick_x);

        float  RRearspeed  = -gamepad1.right_stick_y + (gamepad1.right_stick_x) ;
        float  LRearspeed  = -gamepad1.right_stick_y - (gamepad1.right_stick_x);

//		Double maxspeed = Math.max(Double,Math.max(Math.max(RFrontspeed,LFrontspeed),Math.max(RRearspeed,LRearspeed)));

        // x on left stick should control turning -try this after.

        LFrontspeed +=  (gamepad1.left_stick_x);
        RRearspeed  += -(gamepad1.left_stick_x);
        RFrontspeed += -(gamepad1.left_stick_x);
        LRearspeed  +=  (gamepad1.left_stick_x);


        // clip the right/left values so that the values never exceed +/- 1
        RFrontspeed = robot.speed * Range.clip(RFrontspeed, -1, 1);
        LFrontspeed = robot.speed * Range.clip(LFrontspeed, -1, 1);
        RRearspeed  = robot.speed * Range.clip(RRearspeed,  -1, 1);
        LRearspeed  = robot.speed * Range.clip(LRearspeed,  -1, 1);


        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.??? not sure what this does skip it for now
/*
		RFrontspeed = (float)scaleInput(RFrontspeed);
		LFrontspeed = (float)scaleInput(LFrontspeed);
		RRearspeed =  (float)scaleInput(RRearspeed);
		LRearspeed =  (float)scaleInput(LRearspeed);
*/

        // write the values to the motors
        robot.motorRightFront.setPower(RFrontspeed);
        robot.motorLeftFront.setPower(LFrontspeed);

        robot.motorRightRear.setPower(RRearspeed);
        robot.motorLeftRear.setPower(LRearspeed);





		/*
		 * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */


        // Send telemetry message to signify robot running;
        telemetry.addData("Text", "*** Robot Data***");
       // telemetry.addData("claw",  "Offset = %.2f", clawOffset);
        //telemetry.addData("left",  "%.2f", left);
        //telemetry.addData("right", "%.2f", right);
        telemetry.addData("LeftFront  RightFront: ", +LFrontspeed + " " + RFrontspeed);
        telemetry.addData("LeftRear   RightRear : ", +LRearspeed  + " " + RRearspeed);
//		telemetry.addData("Maxspeed             : ", + maxspeed);
        telemetry.addData("x,y", + gamepad1.right_stick_x  + " " + gamepad1.right_stick_y);
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        robot.motorRightFront.setPower(0);
        robot.motorLeftFront.setPower(0);
        robot.motorRightRear.setPower(0);
        robot.motorLeftRear.setPower(0);
    }

}
