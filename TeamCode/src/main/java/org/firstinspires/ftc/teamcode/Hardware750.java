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

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;


/**
 *  this is my hardware
 *  owo
 */
public class Hardware750 {
    public enum Direction{
        OUT, IN, STOP
    }
    public enum Side{
        LEFT, RIGHT
    }
    /* Public OpMode members. */
    public ModernRoboticsI2cRangeSensor rangeSensor      = null;
    public Servo                        arm              = null;
    public Servo                        thiccClaw1       = null;
    //public Servo                        thiccClaw2       = null;
    public DcMotor                      flDrive          = null;
    public DcMotor                      frDrive          = null;
    public DcMotor                      rlDrive          = null;
    public DcMotor                      rrDrive          = null;
    public DcMotor                      armExtender      = null;
    public ColorSensor                  color            = null;
    // removed!
    // public DcMotor                      gripper          = null;
    public DcMotor                      lift             = null;
    public DigitalChannel               digitalTouch     = null;
    public DigitalChannel               limitTop         = null;
    public DigitalChannel               limitGripper     = null; // limit open switch for gripper
    public DcMotor                      clawMotor        = null; // rotator motor for relic claw
    //public Servo                        blockEjector     = null; // he eject but he also attac
    public CRServo                      blockServoL1     = null;
    public CRServo                      blockServoL2     = null;
    public CRServo                      blockServoR1     = null;
    public CRServo                      blockServoR2     = null;

    // vuforia license key ;)
    public static final String VUF_LIC = "AbQfkoj/////AAAAGURTD1LwoUjKk6qgxygb/6QTHah6F5/HMfF99SDO7C7wnhjBctp6i+bm/mX4El1OTHR8wW0gGjoM4qNsfM3cgFiMDHE4/IBhgpc2siB6nwrgEVZbo3PwJ0xImdXvTSEfWn8Fc6g+svSUFb97VAyjVAEsOvMC+sSqpjIKEQLoCdbCpLRmnX+9socxkX5qix9OVb0xREGbTtddp2fwtLleMXMHxUwhsTc3q7vqD5LDK7Q8GxOaV9jyB6/3Y3T65qaWOGjlGo39Ts394+WTp4hqwqvuu0Gkztlk2e6IeJbN9sN1+8xb2XQllnrHeBhIXxaoES1MRkyjMHliwQxbRJv8kwPeY9q/AsOA/dUy1x87iZLp";

    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public Hardware750(){

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        digitalTouch = hwMap.get(DigitalChannel.class, "sensor_digital");
        rangeSensor = hwMap.get(ModernRoboticsI2cRangeSensor.class, "rangesensor");
        arm     = hwMap.get(Servo.class, "colorarm");
        color   = hwMap.get(ColorSensor.class, "color");
        lift    = hwMap.get(DcMotor.class, "lift");
        armExtender    = hwMap.get(DcMotor.class, "armExtender");
        limitTop = hwMap.get(DigitalChannel.class, "limitTop");
        limitTop.setMode(DigitalChannel.Mode.INPUT);
        limitGripper = hwMap.get(DigitalChannel.class, "limitGripper");
        limitGripper.setMode(DigitalChannel.Mode.INPUT);
        digitalTouch.setMode(DigitalChannel.Mode.INPUT);
        try {
            clawMotor = hwMap.get(DcMotor.class, "relicClaw");
        } catch (Exception e){
            System.out.println("Relic claw not connected! Handled it...");
        }
        // blockEjector = hwMap.get(Servo.class, "blockEjector");

        try {
            thiccClaw1 = hwMap.get(Servo.class, "thiccClaw1");
        } catch (Exception e) {
            System.out.println("One or both of the relic claw servos isn't connected. " +
                    "Caught in HwMap.");
        }

        blockServoL1 = hwMap.get(CRServo.class, "blockServoL1");
        blockServoL2 = hwMap.get(CRServo.class, "blockServoL2");
        blockServoR1 = hwMap.get(CRServo.class, "blockServoR1");
        blockServoR2 = hwMap.get(CRServo.class, "blockServoR2");


        flDrive = hwMap.get(DcMotor.class, "flDrive");
        frDrive = hwMap.get(DcMotor.class, "frDrive");
        rlDrive = hwMap.get(DcMotor.class, "rlDrive");
        rrDrive = hwMap.get(DcMotor.class, "rrDrive");

        flDrive.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        frDrive.setDirection(DcMotor.Direction.REVERSE);
        rlDrive.setDirection(DcMotor.Direction.FORWARD); // Set to FORWARD if using AndyMark motors
        rrDrive.setDirection(DcMotor.Direction.REVERSE);
        armExtender.setDirection(DcMotor.Direction.REVERSE);
        //gripper.setDirection(DcMotor.Direction.FORWARD); // default this to forward, it might not matter.

        // Set all motors to zero power
        flDrive.setPower(0);
        rlDrive.setPower(0);
        frDrive.setPower(0);
        rrDrive.setPower(0);
        //gripper.setPower(0);
        //lift.setPower(0);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        flDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rlDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rrDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armExtender.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        // rem: this <i>may</i> cause issues with how the motor's speed
        // functions.
        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        try {
            clawMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        } catch (Exception e) {}
        // braking setting
        // FLOAT: do not actively resist external forces
        // BRAKE: actively resist external forces
        // gravity-assisted. we want this to try and hold itself up.
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        try {
            clawMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        } catch (Exception e) {}
    }


    public void setAllDriveMotors(double requestedSpeed) {
        flDrive.setPower(requestedSpeed);
        frDrive.setPower(requestedSpeed);
        rlDrive.setPower(requestedSpeed);
        rrDrive.setPower(requestedSpeed);
    }

    public void rotateBlockGrabber(Direction direction){
        if (direction == Direction.IN){
            blockServoL1.setPower(-1);
            blockServoL2.setPower(-1);
            blockServoR1.setPower(1);
            blockServoR2.setPower(1);
        } else if (direction == Direction.OUT){
            blockServoL1.setPower(1);
            blockServoL2.setPower(1);
            blockServoR1.setPower(-1);
            blockServoR2.setPower(-1);
        } else if (direction == Direction.STOP){
            blockServoL1.setPower(0);
            blockServoL2.setPower(0);
            blockServoR1.setPower(0);
            blockServoR2.setPower(0);
        }
    }

    public void halfSpinGrabber(Direction direction, Side side){
        if(direction == Direction.IN){
            if(side == Side.LEFT) {
                blockServoL1.setPower(-1);
                blockServoL2.setPower(-1);
            } else if(side == Side.RIGHT){
                blockServoR1.setPower(1);
                blockServoR2.setPower(1);
            }
        } else if(direction == Direction.OUT){
            if(side== Side.LEFT){
                blockServoL1.setPower(1);
                blockServoL1.setPower(1);
            } else if (side == side.RIGHT){
                blockServoR1.setPower(-1);
                blockServoR2.setPower(-1);
            }
        }
    }


}

