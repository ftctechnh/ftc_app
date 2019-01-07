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
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This file illustrates the concept of driving a path based on time.
 * It uses the common Pushbot hardware class to define the drive on the robot.
 * The code is structured as a LinearOpMode
 *
 * The code assumes that you do NOT have encoders on the wheels,
 *   otherwise you would use: PushbotAutoDriveByEncoder;
 *
 *   The desired path in this example is:
 *   - Drive forward for 3 seconds
 *   - Spin right for 1.3 seconds
 *   - Drive Backwards for 1 Second
 *   - Stop and close the claw.
 *
 *  The code is written in a simple form with no optimizations.
 *  However, there are several ways that this type of sequence could be streamlined,
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="Auto Guy Test", group="concepts")
//@Disabled
public class ECRguytest_Auto extends LinearOpMode {

    /* Declare OpMode members. */
    HardwareECRguy          robot   = new HardwareECRguy();
    private ElapsedTime     runtime = new ElapsedTime();


    static final double     FORWARD_SPEED = 0.6;
    static final double     TURN_SPEED    = 0.5;

    @Override
    public void runOpMode() {

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);


        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Step through each leg of the path, ensuring that the Auto mode has not been stopped along the way

        //Encoder Tests:
        //Forward_for_Distance(inches, motor power)
        //Backward_for_Distance(inches, motor power)
        //Left_for_Distance(degrees, motor power)
        //Right_for_Distance(degrees, motor power)
        //StopMoving(seconds)
        int gold_qualifier = 60;

        robot.Forward_for_Distance(15, .85);
        robot.StopMoving(3.0,runtime);
        //robot.Backward_for_Distance(48, .5);
        //sense color, if white continue
      /*  if (robot.color.blue() < gold_qualifier){
            telemetry.addLine("GOLDEN");
            telemetry.update();
            sleep(2000);
            robot.Forward_for_Distance(10,.85);
        } // if red
        else {
            telemetry.addLine("WHITE, MOVING ON");
            telemetry.update();
            sleep(2000);
            robot.Left_for_Distance(90, .5);
            robot.StopMoving(3.0,runtime);
            robot.Forward_for_Distance(12.4,.85);
            robot.StopMoving(3.0,runtime);
            robot.Right_for_Distance(90,.5);



        if (robot.color.blue() < gold_qualifier) {
            telemetry.addLine("GOLD FO SHO");
            telemetry.update();
            sleep(2000);
            robot.Forward_for_Distance(10, .85);
        }

        else {
            telemetry.addLine("STILL WHITE");
            telemetry.update();
            sleep(2000);
            robot.Right_for_Distance(90, .5);
            robot.StopMoving(3.0, runtime);
            robot.Forward_for_Distance(30, .85);
            robot.StopMoving(3.0, runtime);
            robot.Left_for_Distance(90, .5);
            robot.Forward_for_Distance(5, 0.85);
        }
        }

/*
        robot.Left_for_Distance(90, .5);
        robot.StopMoving(3.0,runtime);
       robot.Forward_for_Distance(12.4,.85);
        robot.StopMoving(3.0,runtime);
       robot.Right_for_Distance(90,.85);
       robot.StopMoving(3.0,runtime);
       robot.Forward_for_Distance(1.25,.85);
        //sense color, if white continue
        robot.Backward_for_Distance(5,.85);
        robot.StopMoving(3.0,runtime);
        robot.Right_for_Distance(90,.5);
        robot.StopMoving(3.0,runtime);
        robot.Forward_for_Distance(30,.85);
        robot.StopMoving(3.0,runtime);
        robot.Left_for_Distance(90,.5);
        robot.Forward_for_Distance(5,0.85);
*/



        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(1000);
    }
}
