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


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

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

public class WLP_RR_Grabber {


    // Global variables to be initialized in the constructor
    private Telemetry telemetry = null;
    private HardwareMap hardwareMap = null;
    private Gamepad gamepad1 = null;
    private Gamepad gamepad2 = null;


    // Declare Grabber members.
    private DcMotor leftGrabberMotor = null;
    private DcMotor rightGrabberMotor = null;



    // Constructors

    // Don't allow default constructors
    private WLP_RR_Grabber() {
        // Throw an error in the private constructor avoids call it within the class.
        throw new AssertionError();
    }

    // This is the only valid consturctor
    public WLP_RR_Grabber(Telemetry telemetry, HardwareMap hardwareMap,
                          Gamepad gamepad1, Gamepad gamepad2){
        this.telemetry = telemetry;
        this.hardwareMap = hardwareMap;
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
    }


    /*
     * Code to run ONCE when the driver hits INIT
     */


    public void init() {

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).

        leftGrabberMotor  = hardwareMap.get(DcMotor.class, "leftgrabbermotor");
        rightGrabberMotor = hardwareMap.get(DcMotor.class, "rightgrabbermotor");


        // Send telemetry message to signify that Grabber initialization complete
        telemetry.addData("Grabber", "Init Complete");    //
    }


    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    public void init_loop() {
    }


    /*
     * Code to run ONCE when the driver hits PLAY
     */

    public void start() {

    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */

    public void loop() {

        // Calculate power as the average value of left and right triggers. This would
        // correctly reflect driver's motivation. The reasoning behind average power
        // would be to apply the same power on the both sides of the spinner.

        double power = (gamepad1.left_trigger + gamepad1.right_trigger)/2.0;

        //
        power    = Range.clip(power, -1.0, 1.0) ;


        // Send calculated power to the wheels
        leftGrabberMotor.setPower(power);
        rightGrabberMotor.setPower(-power);

        // Show the elapsed game time and wheel power.
        telemetry.addData("Motors Power", "left (%.2f), right (%.2f)",
                leftGrabberMotor.getPower(), rightGrabberMotor.getPower());
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */

    public void stop() {
    }
}
