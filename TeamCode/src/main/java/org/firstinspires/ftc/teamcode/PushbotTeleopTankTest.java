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

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

//import org.firstinspires.ftc.teamcode.DriveBaseHardwareMap;
import org.firstinspires.ftc.teamcode.DriveBaseHardwareMap;

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
 *
 * This is a test Team 15403.
 */

@TeleOp(name="Pushbot: Teleop Tank", group="Pushbot")
@Disabled
public class PushbotTeleopTankTest extends OpMode{

    /* Declare OpMode members. */
    DriveBaseHardwareMap robot       = new DriveBaseHardwareMap(); // use the class created to define a Pushbot's hardware
    // could also use HardwarePushbotMatrix class.
    double speed=0;
    double          clawOffset  = 0.0 ;                  // Servo mid position
    final double    CLAW_SPEED  = 0.02 ;                 // sets rate to move servo

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
        //telemetry.addData("Say", "Did you ever hear the tragedy of Darth Plagueis the Wise?\n" +
        //      "I thought not. It's not a story the Jedi would tell you. It's a Sith legend. Darth Plagueis was a Dark Lord of the Sith, so powerful and so wise he could use the Force to influence the midichlorians to create life... He had such a knowledge of the dark side that he could even keep the ones he cared about from dying. The dark side of the Force is a pathway to many abilities some consider to be unnatural. He became so powerful... the only thing he was afraid of was losing his power, which eventually, of course, he did. Unfortunately, he taught his apprentice everything he knew, then his apprentice killed him in his sleep. It's ironic he could save others from death, but not himself.");    //
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop(){
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
        double intleft;


        // Run wheels in tank mode (note: The joystick goes negative when pushed forwards, so negate it)
        left = -(((gamepad1.left_stick_y)/1.3)+speed);
        intleft=gamepad2.left_stick_y;
        //left = 0.0;
        right =((gamepad1.right_stick_y)/1.3)+speed;
        robot.lifter.setPower(intleft);
        robot.top_left.setPower(right);
        robot.bot_left.setPower(right);
        robot.top_right.setPower(left);
        robot.bot_right.setPower(left);

        // Use gamepad left & right Bumpers to open and close the claw

        // Move both servos to new position.  Assume servos are mirror image of each other.

        // Use gamepad buttons to move the arm up (Y) and down (A)

        if (gamepad1.right_bumper) {
            speed = -0.5;
        }
        else if(gamepad1.left_bumper) {
            speed = 0.5;
        }
        else{
            speed = 0;
        }

        if (gamepad2.a){
            robot.sweeper.setPower(0.5);
        }
        else if(gamepad2.y){
            robot.sweeper.setPower(-0.5);
        }
        else{
            robot.sweeper.setPower(0);
        }
        if(gamepad1.dpad_up){
            robot.lifter.setPower(-0.5);
        }
        else if(gamepad1.dpad_down){
            robot.lifter.setPower(0.75);
        }
        else{
            robot.lifter.setPower(0);
        }
        if(gamepad1.b){
            robot.dropper.setPower(0.5);
            telemetry.addData("CRServo",robot.dropper.getPower());
            telemetry.update();
        }
        else if(gamepad1.x){
            robot.dropper.setPower(-0.5);
            telemetry.addData("CRServo",robot.dropper.getPower());
            telemetry.update();
        }
        else{
            robot.dropper.setPower(0);
        }

        // Send telemetry message to signify robot running;
        /*telemetry.addData("claw",  "Offset = %.2f", clawOffset);
        telemetry.addData("left",  "%.2f", left);
        telemetry.addData("right", "%.2f", right);*/
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }
}