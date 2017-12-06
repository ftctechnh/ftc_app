/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package uselessCode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="servoTests", group="Linear Opmode")
@Disabled
public class servoTest extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private Servo armServo;
    private Servo leftClampServo;
    private Servo rightClampServo;
    private boolean isClamping = true;
    private double row1Position = 0.2;
    private double row2Position = 0.4;
    private double row3Position = 0.6;



    @Override
    public void runOpMode() {

        telemetry.addData("Status", "Initialized");
        telemetry.update();


        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        armServo = hardwareMap.get(Servo.class, "armServo");
        leftClampServo = hardwareMap.get(Servo.class, "leftClampServo");
        rightClampServo = hardwareMap.get(Servo.class, "rightClampServo");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        armServo.setDirection(Servo.Direction.FORWARD);
        rightClampServo.setDirection(Servo.Direction.REVERSE);
        leftClampServo.setDirection(Servo.Direction.FORWARD);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();
        //boolean goTrue = true;

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Setup a variable for each drive wheel to save power level for telemetry
            double grabberPosition = armServo.getPosition();

            // Choose to drive using either Tank Mode, or POV Mode
            // Comment out the method that's not used.  The default below is POV.

            // POV Mode uses left stick to go forward, and right stick to turn.
            // - This uses basic math to combine motions and is easier to drive straight.
            //autonomous code in teleop to test out how the servo moves, runs once
            /*if(goTrue == true)
            {
                armServo.setPosition(.6);
                sleep(2000);
                armServo.setPosition(.5);
                sleep(2000);
                armServo.setPosition(.2);
                sleep(2000);
                armServo.setPosition(.8);
            }
            goTrue = false;

            if(gamepad1.a)
            {
                telemetry.addLine("yay");
            }*/
            //  if(gamepad1.atRest())  // consider removing
            // {
            //      armServo.setPosition(1);   //^
            // }
            if(gamepad1.a)
            {
                armServo.setPosition(.8);
            }
            else if(gamepad1.b) // middle arm position
            {
                armServo.setPosition(.4);
            }
            else if(gamepad1.y)
            {
                armServo.setPosition(0);
            }
            else if(gamepad1.x)
            {
                // toggles if the arm is clamping every time x is pressed
                isClamping = !isClamping;
                if (isClamping) {
                    rightClampServo.setPosition(0);
                    leftClampServo.setPosition(0);
                }
                else {
                    rightClampServo.setPosition(.5);
                    leftClampServo.setPosition(.5);
                }
            }
            //else if(gamepad1.x)
            //{
            //    rightClampServo.setPosition(0);
            //    leftClampServo.setPosition(0);
            //}

            //if(gamepad1.atRest())
            //{
                //rightClampServo.setPosition(0);
            //}
            //if(gamepad1.left_stick_y != 0)
            //{
            //    rightClampServo.setPosition(-gamepad1.left_stick_y);
            //}
            //Values are just placeholders for now. We will add real ones after we run telemetry

            //if(gamepad1.atRest())
            //{
            //    leftClampServo.setPosition(0);
            //}
            //else if(gamepad1.left_stick_y != 0)
            //{
            //    leftClampServo.setPosition(-gamepad1.left_stick_y);
            //}

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            updateTelemetry();
        }
    }

    public void updateTelemetry(){
        double armServoValue = armServo.getPosition();
        double leftClampValue = leftClampServo.getPosition();
        double rightClampValue = rightClampServo.getPosition();
        boolean aButton = gamepad1.a;
        telemetry.addData("Run Time", runtime.toString());
        telemetry.addData("Arm servo position", armServoValue);
        telemetry.addData("Left clamp position", leftClampValue);
        telemetry.addData("Right clamp position", rightClampValue);
        telemetry.addData("a button", aButton);
        telemetry.update();
    }

    public void gotoRow(int row){
        // Go to Row (Number)
    }

    public void toggleGrabber(){
        //Clamp/Release Grabber
    }
}
