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

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
/**
 * This OpMode uses the common Pushbot hardware class to define the devices on the robot.
 * <p>
 * All device access is managed through the HardwarePushbot class.
 * <p>
 * The code is structured as a LinearOpMode
 * <p>
 * <p>
 * <p>
 * This particular OpMode executes a POV Game style Teleop for a PushBot
 * <p>
 * In this mode the left stick moves the robot FWD and back, the Right stick turns left and right.
 * <p>
 * It raises and lowers the claw using (change to center motor)the Gampad Y and A buttons respectively.
 * <p>
 * It also opens and closes the claws slowly (change to swiffer) using the left and right Bumper buttons.
 * <p>
 * <p>
 * <p>
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * <p>
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name = "Pushbot: Teleop POV Joystick Test", group = "Pushbot")

//@Disabled

public class PushbotTeleopPOV_Joystick_Test extends LinearOpMode {



    /* Declare OpMode members. */

    TCHardwarePushbot robot = new TCHardwarePushbot();   // Use a Pushbot's hardware


    // could also use HardwarePushbotMatrix class.

    //  double          clawOffset      = 0;                       // Servo mid position

    //  final double    CLAW_SPEED      = 0.02 ;                   // sets rate to move servo


    @Override

    public void runOpMode() throws InterruptedException {

        double lDrive;

        double rDrive;

        double cDrive;





        /* Initialize the hardware variables.

         * The init() method of the hardware class does all the work here

         */

        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;

        //telemetry.addData("Say", "Hello Driver");    //

        //telemetry.update();


        // Wait for the game to start (driver presses PLAY)

        waitForStart();

        while (opModeIsActive()) {

            // run until the end of the match (driver presses STOP)

            // Run wheels in POV mode (note: The joystick goes negative when pushed forwards, so negate it)

            // In this mode the Left stick moves the robot fwd and back, the Right stick turns left and right.

            lDrive = -gamepad1.left_stick_y + gamepad1.right_stick_x;

            rDrive = -gamepad1.left_stick_y - gamepad1.right_stick_x;

            cDrive = -gamepad1.left_trigger + gamepad1.right_trigger;

            double max = Math.max(Math.abs(lDrive), Math.abs(rDrive));

            if (max > 0.5)

            {

                lDrive /= max;

                rDrive /= max;

                cDrive /= max;


            }


            robot.lDrive.setPower(lDrive);

            robot.rDrive.setPower(rDrive);

            robot.cDrive.setPower(cDrive);


        }

    }
}