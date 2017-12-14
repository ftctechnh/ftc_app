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

package org.firstinspires.ftc.teamcode.MinerClue;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
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

@TeleOp(name="Red Recognition", group="Autonomous")
@Disabled
public class AutonomousRed extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
//    private Gyroscope imu;
    private DcMotor rightMotorTest;
    private DcMotor leftMotorTest;
    private DcMotor elevatorMotorTest;
    private DcMotor beltMotorTest;
    //    private DigitalChannel digitalTouch;
    //private DistanceSensor sensorColorRange;

    private Color currentColor;
    private ColorSensor sensorColorRecognition;
    private float [] HSB = new float [3];
    private Servo servoTest;
    double elevatorPower = 1;
    boolean elevatorFlag = false;
    boolean precision = false;
    boolean detectBlue = false;
    boolean canSwitchPrecision = true;





    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        rightMotorTest = hardwareMap.get(DcMotor.class, "Rmotor");
        rightMotorTest.setDirection(DcMotorSimple.Direction.REVERSE);
        leftMotorTest = hardwareMap.get(DcMotor.class, "Lmotor");
        //digitalTouch = hardwareMap.get(DigitalChannel.class, "digitalTouch");
        //sensorColorRange = hardwareMap.get(DistanceSensor.class, "sensorColorRange");
        sensorColorRecognition = hardwareMap.get (ColorSensor.class, "sensorColorRange");
        servoTest = hardwareMap.get(Servo.class, "colorServo");
        telemetry.addData("Blue", sensorColorRecognition.blue());

        servoTest.setPosition (180);


        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {


            Color.RGBToHSV(sensorColorRecognition.red(), sensorColorRecognition.green(), sensorColorRecognition.blue(), HSB);
            telemetry.addData( "Saturation", HSB [1]);
            telemetry.addData ("Hue", HSB [0]);
            if (HSB [0] > 200 && HSB [0] < 275 && HSB [1] >= 0.6) {
                detectBlue = true;
                telemetry.addData("Color", "BLUE!!!");

//               rightMotorTest.setPower(1);
            } else {
                detectBlue = false;
                telemetry.addData("Color", "Not Blue :(");
//                rightMotorTest.setPower(0);
            }

//            telemetry.addData("Distance", sensorColorRange.getDistance(DistanceUnit.CM));
//            if (sensorColorRange.getDistance(DistanceUnit.CM) < 2.54)
//                telemetry.addData ("Close", "Yes");
//            else
//                telemetry.addData ("Close", "No");

//            // Setup a variable for each drive wheel to save power level for telemetry
//            double leftPower;
//            double rightPower;
//
//            // Choose to drive using either Tank Mode, or POV Mode
//            // Comment out the method that's not used.  The default below is POV.
//
//            // POV Mode uses left stick to go forward, and right stick to turn.
//            // - This uses basic math to combine motions and is easier to drive straight.
//            double drive = -gamepad1.left_stick_y;
//            double turn  =  gamepad1.right_stick_x;
//            leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
//            rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;
//
//            // Tank Mode uses one stick to control each wheel.
//            // - This requires no math, but it is hard to drive forward slowly and keep straight.
//            // leftPower  = -gamepad1.left_stick_y ;
//            // rightPower = -gamepad1.right_stick_y ;
//
//            // Send calculated power to wheels
//            leftDrive.setPower(leftPower);
//            rightDrive.setPower(rightPower);
//
//            // Show the elapsed game time and wheel power.
//            telemetry.addData("Status", "Run Time: " + runtime.toString());
//            telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
//            telemetry.update();
        }
    }
}
