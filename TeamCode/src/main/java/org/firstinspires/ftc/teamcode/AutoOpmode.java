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

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.TouchSensor;

/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a PushBot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="AutoOpmode", group="Testing")  // @Autonomous(...) is the other common choice
//@Disabled
public class AutoOpmode extends LinearOpMode {

    /* Declare OpMode members. */
    
    
    private ElapsedTime runtime = new ElapsedTime();
    private ElapsedTime keepPushingtime = new ElapsedTime();
    DcMotor leftMotor = null;
    DcMotor rightMotor = null;
    DcMotor ballMotor = null;
    Servo servoSlicer, servoPusher;
    DeviceInterfaceModule dim;                  // Device Object
    DigitalChannel        touchSensor;           // Device Object
    //TouchSensor touchSensor;
    boolean goingForward = true;
    boolean running = true;

    private double TIME_ONE = 2;
    private double TIME_TWO = 10;

    private double TIME_EXTRA = .2;

    private double LOW_POWER = .1;
    private double MID_POWER = .15;
    private double HIGH_POWER = .2;

    @Override
    public void runOpMode() {
        /* eg: Initialize the hardware variables. Note that the strings used here as parameters
         * to 'get' must correspond to the names assigned during the robot configuration
         * step (using the FTC Robot Controller app on the phone).
         */
        dim = hardwareMap.get(DeviceInterfaceModule.class, "dim");   //  Use generic form of device mapping
        touchSensor = hardwareMap.get(DigitalChannel.class, "sensor_touch"); //  Use generic form of device mapping
        leftMotor  = hardwareMap.dcMotor.get("left_drive");
        rightMotor = hardwareMap.dcMotor.get("right_drive");
        leftMotor.setDirection(DcMotor.Direction.REVERSE);
        rightMotor.setDirection(DcMotor.Direction.FORWARD);
        leftMotor.setPower(0);
        rightMotor.setPower(0);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive() && running) {

            while(!touchSensor.getState() && goingForward)
            {
                if(runtime.seconds()<TIME_ONE) {
                    leftMotor.setPower(LOW_POWER);
                    rightMotor.setPower(LOW_POWER);
                }
                else if(runtime.seconds() < TIME_TWO){
                    leftMotor.setPower(MID_POWER);
                    rightMotor.setPower(MID_POWER);
                }
                else{
                    leftMotor.setPower(HIGH_POWER);
                    rightMotor.setPower(HIGH_POWER);
                }
                telemetry.update();
            }

            keepPushingtime.reset();
            while(keepPushingtime.seconds()<TIME_EXTRA && goingForward)
            {
                leftMotor.setPower(HIGH_POWER);
                rightMotor.setPower(HIGH_POWER);
            }

            goingForward = false;
            leftMotor.setPower(0);
            rightMotor.setPower(0);
            telemetry.update();
            idle();     // allow something else to run (aka, release the CPU)
        }
    }
}
