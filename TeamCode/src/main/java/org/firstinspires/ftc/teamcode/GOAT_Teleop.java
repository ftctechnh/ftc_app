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
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
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

@TeleOp(name="GOAT: TeleOp", group="GOAT")
//@Disabled
public class GOAT_Teleop extends LinearOpMode {

    /* Declare OpMode members. */
    HardwareGOAT robot = new HardwareGOAT();

    double driverShifterPosition;  //variable for position of driver shifter servo
    double passShifterPosition;    //variable for position of passenger shifter servo
    double TailRotateSpeed;        //variable for speed of servos that pivot tail
    double DrivePower;             //variable for power level max of drive motors
    //double  Arm_dsPosition;         //variable for position of driver side arm flapper
    //double  Arm_psPosition;         //variable for position of passenger side arm flapper
    double  SweeperSpeed;           //variable for speed of servo for sweeper
    double  DumpPosition;           //variable for position of box dump servo
    double  dBrakePosition;         //variable for position of the driver and passenger front brakes
    double  pBrakePosition;
    double  ArmShifterPosition;
    double  ClimberArmPosition;
    double  KickerSpeed;

    float tail;     //variable for tail wheel speed
    float Arm;      //variable for driver and passenger arms extension speed
    float ArmPivot; //variable for speed of rotation pivot for arms

    DcMotor motorDriver1;   //Driver Side Motor 1
    DcMotor motorDriver2;   //Driver Side Motor 2
    DcMotor motorPass1;     //Passenger Side Motor 1
    DcMotor motorPass2;     //Passenger Side Motor 2
    DcMotor motorTail;      //Tail Drive Motor
    DcMotor motorArmPivot;  //Arms lead screw Pivot Motor
    DcMotor motorArm1;      //Arm 1 Extension Motor
    DcMotor motorArm2;      //Arm 2 Extension Motor
    Servo DriverShifter;//driver gearbox shifter    1
    Servo PassShifter;  //passenger gearbox shifter 2
    Servo TailRotate1;  //tail pivot servo 1        3
    Servo TailRotate2;  //tail pivot servo 2        4
    //Servo arm_ds;       //driver side flapper       5
    //Servo arm_ps;       //passenger side flapper    6
    Servo Sweeper;      //sweeper servo             7
    Servo Dump;         //box dump/rotate servo     8
    Servo dBrake;       //driver side brake         9
    Servo pBrake;       //passenger side brake      10
    Servo ArmShifter;   //arm gearbox shifter       11
    Servo ClimberArm;   //climber arm               12
    Servo Kicker;

    double scaleInput(double dVal)  {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0) {
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }

        // get value from the array.
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        // return scaled value.
        return dScale;
    }

    @Override
    public void runOpMode() {
        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        //assign the starting position of the servos and variables
        //all the servos variables are set to their start position here so that they are ready for the match to start
        driverShifterPosition = 0.6;    //the driver shifter is set to .6 so that it starts in high gear
        passShifterPosition = 0.6;      //the passenger shifter is set to .6 so that it starts in high gear
        TailRotateSpeed = .5;           //the tail rotate speed is set to .5 (neural) so that it will not go up or down
        DrivePower = 1;                 //drive power is set to 1 so that we start off the match at full speed
        ArmPivot = 0;                   //the speed of the arm pivot is set to 0 to keep it from moving at the beginning of the match
        Arm = 0;                        //the speed of the arms extension is set to 0 at the beginning of the match
        tail = 0;                       //the speed of the tail driver power variable is set to 0
        //Arm_dsPosition = .45;           //sets driver side flapper to be in  at the start of the match
        //Arm_psPosition = .55;           //sets passenger side flapper to be in at the start of the match
        SweeperSpeed = .5;              //sets the sweeper speed to be neutral at the start of the match
        DumpPosition = .5;              //sets the dump servo to the center position to keep the bucket vertical
        dBrakePosition = .92;             //sets the brakes/guards to be down to keep debris out from under the treads
        pBrakePosition = .92;
        ClimberArmPosition = 1;
        KickerSpeed = .5;

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Hello Driver");    //
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            //map left and right y joysticks on controller 1 to adjust the left and right variables for driving the robot
            float left = -gamepad1.left_stick_y;
            float right = -gamepad1.right_stick_y;

            //map controller 1 left and right triggers to fold the side flappers in and out
            if (gamepad1.left_trigger > .01) {
                dBrakePosition = .1;     //set driver flapper to be out
                pBrakePosition = .65;
            }
            if (gamepad1.right_trigger > .01) {
                pBrakePosition = .1;     //set passenger flapper to be up
                dBrakePosition=.65;
            }
            if (gamepad1.dpad_right) {
                dBrakePosition = .87;   //set both flappers to be in
                pBrakePosition = .87;
            }


            //arms are controlled by joysticks and tail is controlled by triggers and bumpers on controller 2
            Arm = gamepad2.left_stick_y;            //left y stick controls arm in and out
            ArmPivot = -gamepad2.right_stick_y;     //right y stick controls arm pivot

            //controls tail power with left trigger and left bumper
            if (gamepad2.right_trigger > .25) {
                tail = 1;   //set tail drive power to 1 for forward
            } else if (gamepad2.right_bumper) {
                tail = -1;  //set tail drive power to -1 for reverse
            } else {
                tail = 0;   //if neither button is pressed, set tail drive power to 0 for neutral
            }

            //control tail rotate with right trigger and right bumper
            if (gamepad2.left_trigger > .5) {
                TailRotateSpeed = 0;    //set tail pivot servos to 0 to rotate tail down
            } else if (gamepad2.left_bumper) {
                TailRotateSpeed = 1;    //set tail pivot servos to 1 to rotate tail up
            } else {
                TailRotateSpeed = .5;   //if neither button is bressed, set tail pivot servos to .5 to be in neutral
            }


            //controller 1 buttons a, b, x, and y control the max drive power to help with prescission driving when needed
            if (gamepad1.a) {
                DrivePower = .25;   //set max drive power to 25%
            }
            if (gamepad1.b) {
                DrivePower = .5;    //set max drive power to 50%
            }
            if (gamepad1.x) {
                DrivePower = .75;   //set max drive power to 75%
            }
            if (gamepad1.y) {
                DrivePower = 1;     //set max drive power to 100%
            }


            //clip the right/left values so that the values never exceed +/- 1
            right = Range.clip(right, -1, 1);   //clip right driver power input to maximum of 1 and minimum of -1
            left = Range.clip(left, -1, 1);     //clip left driver power input to maximum of 1 and minimum of -1
            tail = Range.clip(tail, -1, 1);     //clip tail drive power input to maximum of 1 and minimum of -1

            //scale the joystick value to make it easier to control
            //the robot moves more precisely at slower speeds.
            right = (float)scaleInput(right);   //scale right drive power
            left =  (float)scaleInput(left);    //scale left drive power
            tail = (float)scaleInput(tail);     //scale tail drive power

            //write the values to the motors
            robot.motorDriver1.setPower(left*DrivePower);     //write power to driver motor 1 (throttle times the max drive power mode)
            robot.motorDriver2.setPower(left*DrivePower);     //write power to driver motor 2 (throttle times the max drive power mode)
            robot.motorPass1.setPower(right*DrivePower);      //write power to passenger motor 1 (throttle times the max drive power mode)
            robot.motorPass2.setPower(right*DrivePower);      //write power to passenger motor 2 (throttle times the max drive power mode)
            robot.motorTail.setPower(tail);                   //write power to tail drive motor from tail variable
            robot.motorArmPivot.setPower(ArmPivot);           //write power to arm pivot motor from ArmPivot variable
            robot.motorArm1.setPower(Arm);                    //write power to arm motor 2 from Arm variable
            robot.motorArm2.setPower(Arm);                    //write power to arm motor 1 from Arm variable

            //set position to the shifter servos
            if (gamepad1.left_bumper) {
                driverShifterPosition = .6;     //controller 1 left bumper sets driver shifter to .6 for high gear
                passShifterPosition = .6;       //controller 1 left bumper sets passenger shifter to .6 for high gear
            }
            if (gamepad1.right_bumper) {
                driverShifterPosition = .2;     //controller 1 right bumper sets driver shifter to .2 for low gear
                passShifterPosition = .2;       //controller 1 right bumper sets passenger shifter to .2 for low gear
            }

            //controller 2 buttons a and b control sweeper speed
            if (gamepad2.a) {
                SweeperSpeed = 1;       //controller 2 button a spins the sweeper servo at full speed forwards
            } else if (gamepad2.b) {
                SweeperSpeed = 0;       //controller 2 button b spins the sweeper servo at full speed in reverse
            } else {
                SweeperSpeed = .5;      //if neither button is pressed, the sweeper servo is set to .5 for neutral
            }

            if (gamepad2.x) {
                ArmShifterPosition = .2;
            } else if (gamepad2.y)  {
                ArmShifterPosition = .6;
            }


            //controller 2 dpad controls dump box position and brake position servos
            //controls dump bucket
            if (gamepad2.dpad_right) {
                DumpPosition = 0;         //controller 2 right dpad sets dump bucket to right position
            } else {
                DumpPosition= .57;        //if neither left or right dpad button is pressed, set dump bucket to center position
            }

            //controls brakes with controller 2
            if (gamepad2.dpad_up) {
                KickerSpeed = 1;   //controller 2 up dpad sets left flap to be in down position
            } else {
                KickerSpeed = .55;
            }

            //controls brakes with controller 1
            if (gamepad1.dpad_down) {
                dBrakePosition = .87;     //controller 1 down dpad sets brakes to be in down position
                pBrakePosition = .87;
            }
            if (gamepad1.dpad_up) {
                dBrakePosition = .45;   //controller 1 up dpad sets brakes to be in up position
                pBrakePosition = .45;
            }

            if (gamepad2.a&&gamepad2.b) {
                ClimberArmPosition = 0;
            } else {
                ClimberArmPosition = 1;
            }


            //write position values to the servos from variables
            robot.DriverShifter.setPosition(driverShifterPosition);       //sets position of DriverShifter servo to value of driverShifterPosition variable
            robot.PassShifter.setPosition(passShifterPosition);           //sets position of PassShifter servo to value of passShifterPosition variable
            robot.TailRotate1.setPosition(TailRotateSpeed);               //sets position of TailRotate1 servo to value of TailRotateSpeed variable
            robot.TailRotate2.setPosition(TailRotateSpeed);               //sets position of TailRotate2 servo to value of TailRotateSpeed variable
            //robot.arm_ds.setPosition(Arm_dsPosition);                     //sets position of arm_ds flapper servo to value of Arm_dsPosition variable
            //robot.arm_ps.setPosition(Arm_psPosition);                     //sets position of arm_ps flapper servo to value of Arm_psPosition variable
            robot.Sweeper.setPosition(SweeperSpeed);                      //sets Sweeper servo to value of SweeperSpeed variable
            robot.Dump.setPosition(DumpPosition);                         //sets position of Dump servo to value of DumpPosition variable
            robot.dBrake.setPosition(1.02-dBrakePosition);                //sets position of driver brake servo to value of inverse of BrakesPosition variable with a slight offset for clocked position of servo
            robot.pBrake.setPosition(pBrakePosition);                     //sets position of passenger brake servo to BrakesPosition variable
            robot.ArmShifter.setPosition(ArmShifterPosition);             //sets position of arm gearbox to ArmShifterPosition Variable
            robot.ClimberArm.setPosition(ClimberArmPosition);
            robot.Kicker.setPosition(KickerSpeed);

            //Send telemetry data back to driver station
            telemetry.addData("Text", "*** Robot Data***"); //sends error codes and other data back to driver station
            telemetry.addData("D Brake: ",String.format("%.2f", dBrakePosition));
            telemetry.addData("P Brake: ",String.format("%.2f", pBrakePosition));  //sends value of BrakesPosition back to driver station
            telemetry.addData("Drive Power level: ", "" + String.format("%.2f", DrivePower));  //sends value of Max Driver power variable back to driver station
            telemetry.addData("Shifter", " Position: " + String.format("%.2f", driverShifterPosition));  //sends the position of the driver shifter back to the driver station (this is the same as passenger)
            telemetry.addData("Driver Driver Power",  ": " + String.format("%.2f", left));  //sends the value of the driver throttle power back to the driver station
            telemetry.addData("Passenger Drive Power", ": " + String.format("%.2f", right));    //sends the value of the passenger throttle power back to the driver station
            telemetry.addData("Tail Rotate Speed",  ": " + String.format("%.2f", TailRotateSpeed)); //sends the value of the tail rotate speed back to the driver station
            telemetry.addData("Tail Power", ": " + String.format("%.2f", tail));    //sends the value of the tail power back to the driver station
            telemetry.addData("Arm Power", ": " + String.format("%.2f", Arm));  //sends the value of the arm power back to the driver station
            telemetry.addData("Arm Pivot Power", ": " + String.format("%.2f", ArmPivot));   //sends the value of the arm pivot motor power back to the driver station
            telemetry.addData("Dump Position", ": " + String.format("%.2f", DumpPosition));   //sends the position of the dump bucket back to the driver station
            telemetry.addData("Sweeper", " Speed: " + String.format("%.2f", SweeperSpeed)); //sends the value of the sweeper speed back to the driver station
            telemetry.addData("Arm", " Shifter: " + String.format("%.2f", ArmShifterPosition));
            telemetry.update();

            // Pause for metronome tick.  40 mS each cycle = update 25 times a second.
            robot.waitForTick(40);
        }
    }
}
