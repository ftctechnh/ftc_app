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

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;

/**
 * This OpMode uses the CRServo class whcih makes a continuous rotation servo act like a
 * simple motor.  Power range from -1 to 1 with 0 being stop.
 * <p>
 * This example runs the servo at full speed.  Easy to change that by changing the
 * power values for open and close.
 */
@TeleOp(name = "Sharon's Servo Code", group = "BACONbot")
//@Disabled
public class SharonServoCode extends LinearOpMode {

    //    static final double OPEN     =  1;     // need to test OPEN and CLOSE is right, one is CW and the other CCW
//    static final double CLOSE     =  -1;
        /* This says to use BACONbot hardware */
    ArmHardwareClass robot = new ArmHardwareClass();

    @Override
    public void runOpMode() throws InterruptedException {

        // Connect to servo (Assume PushBot Left Hand)
        // Change the text in quotes to match any servo name on your robot.
//        clamp = hardwareMap.get(CRServo.class, "left_hand");

        // Wait for the start button
        telemetry.addData(">", "Press Start to scan Servo.");
        telemetry.update();
        waitForStart();


        // Scan servo till stop pressed.
        while (opModeIsActive()) {


            // Display the current value
            telemetry.addData("Servo Controls", "Y is OPEN, A is close");
            telemetry.addData(">", "Press Stop to end test.");
            telemetry.update();

            if (gamepad1.a) {
                robot.clawServo.setPosition(100);
                wait(1000);
                telemetry.addLine("HIII");
            }
            if (gamepad1.y) {
                robot.clawServo.setPosition(80);
                wait(1000);
               }
            }
=======
            // hi
          }
        }
>>>>>>> Stashed changes



            // Signal done;
            telemetry.addData(">", "Done");
            telemetry.update();
        }
    }
}
