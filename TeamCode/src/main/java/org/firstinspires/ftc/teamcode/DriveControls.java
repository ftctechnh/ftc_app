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

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * This file contains an example of an iterative (Non-Linear) "OpMode".
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all iterative OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="Basic: Iterative OpMode", group="Iterative Opmode")
//@Disabled
public class DriveControls extends OpMode
{
    // Declare OpMode members.

    private DcMotorController dc_motor;
    private DcMotor frontLeftWheel;
    private DcMotor backLeftWheel;
    private DcMotor frontRightWheel;
    private DcMotor backRightWheel;
    private DcMotor frontMotorLeft;
    private DcMotor frontMotorRight;
    private boolean MotorOn;
    private String log = "Fields Initialized";


    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {

        MotorOn = false;

        //maps each dcMotor to the corresponding name listed on the "Configure Robot"
        frontLeftWheel = hardwareMap.dcMotor.get("frontLeft");
        backRightWheel = hardwareMap.dcMotor.get("backRight");
        frontRightWheel = hardwareMap.dcMotor.get("frontRight");
        backLeftWheel =  hardwareMap.dcMotor.get("backLeft");
        frontMotorLeft = hardwareMap.dcMotor.get("frontMotorLeft");
        frontMotorRight = hardwareMap.dcMotor.get("frontMotorRight");


        log = "hardwareMapped";
        telemetry.addData("Out put", log);

    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        // The following allows for controlling mechanm wheels

        //getting horizontal position of left joystick
        float clockwise = gamepad1.right_stick_x;

        //getting vertical position of left joystick
        float forward = gamepad1.left_stick_x;

        //getting horiz position of right joystick
        float right = -gamepad1.left_stick_y;

        //Doing the math to find the power value we need to set
        float temp = (float) (forward* (Math.cos(clockwise)) + right* (Math.sin(clockwise)));
        right = (float) ( -forward* (Math.sin(clockwise)) + right*(Math.cos(clockwise)));
        forward = (float) temp;

        //Finding power value for each wheel
        float front_left = forward + clockwise + right;
        float front_right = forward - clockwise - right;
        float rear_left = forward + clockwise - right;
        float rear_right = forward - clockwise + right;


        //Making sure each wheel has a power value no more than 1 or less than -1
        if(front_right > 1){
            front_right= 1;
        } else if(front_right < -1) {
            front_right = -1;
        }

        if(rear_left > 1){
            rear_left= 1;
        } else if(front_right < -1) {
            rear_left = -1;
        }

        if(rear_right> 1){
            rear_right= 1;
        } else if(rear_right < -1) {
            rear_right = -1;
        }

        //Assigns power value to each wheel
        backLeftWheel.setPower(rear_left);
        frontLeftWheel.setPower(-front_left);
        backRightWheel.setPower(-rear_right);
        frontRightWheel.setPower(front_right);

    }

}
