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
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This is NOT an opmode.
 *
 * This class can be used to define all the specific hardware for a single robot.
 * In this case that robot is a Pushbot.
 * See PushbotTeleopTank_Iterative and others classes starting with "Pushbot" for usage examples.
 *
 * This hardware class assumes the following device names have been configured on the robot:
 * Note:  All names are lower case and some have single spaces between words.
 *
 * Motor channel:  Left  drive motor:        "left_drive"
 * Motor channel:  Right drive motor:        "right_drive"
 * Motor channel:  Manipulator drive motor:  "left_arm"
 * Servo channel:  Servo to open left claw:  "left_hand"
 * Servo channel:  Servo to open right claw: "right_hand"
 */
public class RelicDrive
{
    /* Public OpMode members. */
    public DcMotor  leftMid     = null;
    public DcMotor  rightMid    = null;
    public DcMotor  leftBack    = null;
    public DcMotor  rightBack   = null;

    static final double     COUNTS_PER_MOTOR_REV    = 560 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.7;
    static final double     TURN_SPEED              = 0.65;

    //These are guesses
    public static final double MID_RAD = 7.8125;
    public static final double OUT_RAD = 9.85;

    //public static final double ARM_UP_POWER    =  0.45 ;
    //public static final double ARM_DOWN_POWER  = -0.45 ;

    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public RelicDrive(){

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        leftMid  = hwMap.get(DcMotor.class, "left_mid");
        rightMid = hwMap.get(DcMotor.class, "right_mid");
        leftBack    = hwMap.get(DcMotor.class, "left_back");
        rightBack = hwMap.get(DcMotor.class, "right_back");
        //We have AndyMark motors, but our direction of drive is opposite of what is suggested in the below comments
        leftMid.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        rightMid.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors
        leftBack.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        rightBack.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors

        // Set all motors to zero power
        leftMid.setPower(0);
        rightMid.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        leftMid.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMid.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        leftMid.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMid.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

    }
    //encoder drive method
    void encoderDrive(double speed,
                             double lMDis, double rMDis, double lBDis, double rBDis) {
        int newLMTarget;
        int newRMTarget;
        int newLBTarget;
        int newRBTarget;

        // Ensure that the opmode is still active
        //if (opModeIsActive()) {
        /*telemetry.addData("Path", "inside opmode: %7d :%7d",robot.leftDrive.getCurrentPosition(),robot.rightDrive.getCurrentPosition());
        telemetry.update();*/

        // Determine new target position, and pass to motor controller
        newLMTarget = leftMid.getCurrentPosition() + (int)(lMDis * COUNTS_PER_INCH);
        newRMTarget = rightMid.getCurrentPosition() + (int)(rMDis * COUNTS_PER_INCH);
        newLBTarget = leftBack.getCurrentPosition() + (int)(lBDis * COUNTS_PER_INCH);
        newRBTarget = rightBack.getCurrentPosition() + (int)(rBDis * COUNTS_PER_INCH);
        leftMid.setTargetPosition(newLMTarget);
        leftBack.setTargetPosition(newLBTarget);
        rightMid.setTargetPosition(newRMTarget);
        rightBack.setTargetPosition(newRBTarget);

        /*telemetry.addData("after new target set",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
        telemetry.update();
        sleep(500);*/

        // Turn On RUN_TO_POSITION
        leftMid.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightMid.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // reset the timeout time and start motion.
        leftMid.setPower(Math.abs(speed));
        rightMid.setPower(Math.abs(speed));
        leftBack.setPower(Math.abs(speed));
        rightBack.setPower(Math.abs(speed));

        while (leftMid.isBusy() && rightMid.isBusy() && leftBack.isBusy() && rightBack.isBusy());

        // keep looping while we are still active, and there is time left, and both motors are running.
        /* while (opModeIsActive() &&
        (runtime.seconds() < timeoutS) &&
                (leftMid.isBusy() && rightMid.isBusy() && leftBack.isBusy() && rightBack.isBusy())) {

            // Display it for the driver.
            telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
            telemetry.addData("Path2",  "Running at %7d :%7d",
                    robot.leftDrive.getCurrentPosition(),
                    robot.rightDrive.getCurrentPosition());
            telemetry.update();
        }*/

        //Stop all motion;
        leftMid.setPower(0);
        rightMid.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);

        /*telemetry.addData("PathJ", "inside opmode: %7d :%7d",robot.leftDrive.getCurrentPosition(),robot.rightDrive.getCurrentPosition());
        telemetry.update();
        sleep(500);*/

        // Turn off RUN_TO_POSITION
        leftMid.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMid.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //  sleep(250);   // all comments were from when this method was in an OpMode. These commands don't work in non OpModes.
    } //end of encoder drive method
    public void linearDrive (double speed, double distance){
        encoderDrive(speed, distance, distance, distance,distance);
    }
    public void statTurn(double speed, double degrees){
        double midArc = 2 * 3.14 * MID_RAD * (degrees/360);
        double outArc = 2 * 3.14 * OUT_RAD * (degrees/360);
        encoderDrive(speed, midArc, -midArc, outArc, -outArc);
    }
    public void pivotTurn(double speed, double degrees, String direction){
        if (direction == "right"){
            double midArc = 2 * 3.14 * MID_RAD * (degrees/360);
            double outArc = 2 * 3.14 * OUT_RAD * (degrees/360);
            encoderDrive(speed, midArc, 0, outArc, 0);
        }else if (direction == "left"){
            double midArc = 2 * 3.14 * MID_RAD * (degrees/360);
            double outArc = 2 * 3.14 * OUT_RAD * (degrees/360);
            encoderDrive(speed, 0, midArc, 0, outArc);
        }
    }
    public void controlDrive(double left, double right){
        leftMid.setPower(left);
        leftBack.setPower(left);
        rightMid.setPower(right);
        rightBack.setPower(right);
    }
    public int getLMencoder(){
        return leftMid.getCurrentPosition();
    }
    public int getRMencoder(){
        return rightMid.getCurrentPosition();
    }
    public int getLBencoder(){
        return leftBack.getCurrentPosition();
    }
    public int getRBencoder(){
        return rightBack.getCurrentPosition();
    }
 }

