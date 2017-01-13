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
package org.firstinspires.ftc.team8200;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;


/*
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

@TeleOp(name="K9bot: Telop Tank", group="K9bot")
//@Disabled
public class K9botTeleopTank_Linear extends LinearOpMode {

    /* Declare OpMode members. */
    HardwareK9bot robot = new HardwareK9bot(); // Use a K9's hardware
    //double armPosition = robot.ARM_HOME; // Servo safe position
    //final double ARM_SPEED = 0.01 ; // sets rate to move servo

    @Override
    public void runOpMode() {
        double leftY;
        //double leftX;
        //double rightX;
        double rightY;
        double leftWS;
        double rightWS;
        //double armPosition = -1;
        //double armHitPosition = 1;

        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Hello BASE Student");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Run wheels in tank mode (note: The joystick goes negative when pushed forwards, so negate it)
            leftY = -gamepad1.left_stick_y;
            rightY = -gamepad1.right_stick_y;

            rightY = Range.clip(rightY, -1, 1);
            leftY = Range.clip(leftY, -1, 1);

            robot.leftMotor.setPower(leftY);
            robot.rightMotor.setPower(rightY);

            leftWS = -gamepad2.left_stick_y;
            rightWS = -gamepad2.right_stick_y;

            rightWS = Range.clip(rightWS, -1, 1);
            leftWS = Range.clip(leftWS, -1, 1);

            robot.leftWheelShooter.setPower(leftWS);
            robot.rightWheelShooter.setPower(rightWS);

            float harvesterPower = gamepad1.right_trigger;
            float harvesterPowerReversed = gamepad1.left_trigger;

            if (harvesterPower > 0.2 && harvesterPowerReversed == 0) {
                robot.harvester.setPower(1);
            }
            else if (harvesterPowerReversed > 0.2 && harvesterPower == 0) {
                robot.harvester.setPower(-1);
            }
            else {
                robot.harvester.setPower(0);
            }


            float elevatorPower = gamepad2.right_trigger;
            float elevatorPowerReversed = gamepad2.left_trigger;

            if(elevatorPower > 0.2 && elevatorPowerReversed == 0)
            {
                robot.elevator.setPower(0.5);
            }
            else if(elevatorPowerReversed > 0.2 && elevatorPower == 0)
            {
                robot.elevator.setPower(-0.5);
            }
            else
            {
                robot.elevator.setPower(0);
            }

            boolean isButtonAPressed = gamepad2.a;
            if(isButtonAPressed)
            {
                robot.leftWheelShooter.setPower(-0.3);
                robot.rightWheelShooter.setPower(0.3);
            }
            else
            {
                robot.leftWheelShooter.setPower(0);
                robot.rightWheelShooter.setPower(0);
            }



            //Arm
            //Press once to extend button pressing arm, press again to retract arm

            boolean isArmExtendedLeft = false;
            boolean isArmExtendedRight = false;


            if (gamepad2.x && !isArmExtendedLeft) {
                robot.armLeft.setPosition(1);
                isArmExtendedLeft = true;
            }
            else if (gamepad2.x && isArmExtendedLeft) {
                robot.armLeft.setPosition(0);
                isArmExtendedLeft = false;
            }
            else {
                robot.armLeft.setPosition(0);
                isArmExtendedLeft = false;
            }

            if (gamepad2.b && !isArmExtendedRight) {
                robot.armRight.setPosition(1);
                isArmExtendedRight = true;
            }
            else if (gamepad2.b && isArmExtendedRight) {
                robot.armRight.setPosition(0);
                isArmExtendedRight = false;
            }
            else {
                robot.armRight.setPosition(0);
                isArmExtendedRight = false;
            }

            // Send telemetry message to signify robot running
            // Telemetry messages go here if we need in the future
            //robot.leftMotor.setPower(leftY);
            //robot.rightMotor.setPower(rightY);


            // Send telemetry message to signify robot running;
            telemetry.addData("leftY",  "%.2f", leftY);
            telemetry.addData("rightY", "%.2f", rightY);
            telemetry.update();

            // Pause for metronome tick.
            // 40 ms each cycle = update 25 times a second.
//            robot.waitForTick(40);
        }
    }
}