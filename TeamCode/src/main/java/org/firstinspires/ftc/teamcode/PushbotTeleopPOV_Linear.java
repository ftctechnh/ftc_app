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
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
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

@TeleOp(name = "Pushbot: Teleop POV TC4", group = "Pushbot")

//@Disabled

public class PushbotTeleopPOV_Linear extends LinearOpMode {



    /* Declare OpMode members. */

    TCHardwarePushbot robot = new TCHardwarePushbot();   // Use a Pushbot's hardware
    private ElapsedTime     runtime = new ElapsedTime();

    static final double     FORWARD_SPEED = 0.6;
    static final double     TURN_SPEED    = 0.5;
    // could also use HardwarePushbotMatrix class.

    //  double          clawOffset      = 0;                       // Servo mid position

    //  final double    CLAW_SPEED      = 0.02 ;                   // sets rate to move servo


    @Override

    public void runOpMode() throws InterruptedException {

        double lDrive;

        double rDrive;

        double cDrive;

        double fLift;
        double arm1;
        double arm2;

        double max;





        /* Initialize the hardware variables.

         * The init() method of the hardware class does all the work here

         */

        robot.init(hardwareMap);


        // Send telemetry message to signify robot waiting;

        //telemetry.addData("Say", "Hello Driver");    //

        // Wait for the game to start (driver presses PLAY)

        waitForStart();


        // run until the end of the match (driver presses STOP)

        while (opModeIsActive()) {
            //sets flift servos to 90 degrees

            robot.jko.setPosition(.5);
            robot.claw.setPosition(0);

            // Run wheels in POV mode (note: The joystick goes negative when pushed forwards, so negate it)

            // In this mode the Left stick moves the robot fwd and back, the Right stick turns left and right.

            lDrive = -gamepad1.left_stick_y + gamepad1.right_stick_x;

            rDrive = -gamepad1.left_stick_y - gamepad1.right_stick_x;

            cDrive = -gamepad1.left_trigger + gamepad1.right_trigger;
            arm1 = -gamepad2.left_stick_y;
            arm2 = -gamepad2.right_stick_y;


            if (gamepad2.a) {
                robot.fs1.setPosition(.15);
                robot.fs2.setPosition(.6);
                robot.fs3.setPosition(.35);
                robot.fs4.setPosition(.7);
            }
            if (gamepad2.b) {
                robot.fs1.setPosition(.5);
                robot.fs3.setPosition(.8);
                robot.fs2.setPosition(.25);
                robot.fs4.setPosition(.35);
            }
            if (gamepad1.a) {
                robot.jko.setPosition(.75);
            }
            if (gamepad1.b) {
                robot.jko.setPosition(.5);
            }
            if (gamepad2.x) {
                robot.claw.setPosition(.75);
            }

            if (gamepad2.dpad_up) {
                robot.fLift.setDirection(DcMotorSimple.Direction.FORWARD);
                robot.fLift.setPower(1);
            }
            else if(!gamepad2.dpad_up) {
                robot.fLift.setPower(0);
            }
            if (gamepad2.dpad_down) {
                robot.fLift.setDirection(DcMotorSimple.Direction.REVERSE);
                robot.fLift.setPower(1);
            }
            else if(!gamepad2.dpad_down) {
                robot.fLift.setPower(0);
            }



            // Normalize the values so neither exceed +/- 1.0

            max = Math.max(Math.abs(lDrive), Math.abs(rDrive));

            if (max > 0.5)

            {

                lDrive /= max;

                rDrive /= max;

                cDrive /= max;


            }


            robot.lDrive.setPower(lDrive);

            robot.rDrive.setPower(rDrive);

            robot.cDrive.setPower(cDrive);
            robot.arm1.setPower(arm1);
            robot.arm2.setPower(arm2);


        }

    }

}