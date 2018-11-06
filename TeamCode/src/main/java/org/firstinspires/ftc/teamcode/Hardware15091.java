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

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.android.AndroidAccelerometer;
import org.firstinspires.ftc.robotcore.external.android.AndroidGyroscope;
import org.firstinspires.ftc.robotcore.external.android.AndroidOrientation;
import org.firstinspires.ftc.robotcore.external.android.AndroidSoundPool;
import org.firstinspires.ftc.robotcore.external.android.AndroidTextToSpeech;

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
public class Hardware15091
{
    /* Public OpMode members. */
    public DcMotor  leftDrive   = null;
    public DcMotor  rightDrive  = null;
    public DcMotor  armDrive     = null;
    public Servo    armServo    = null;
    public Servo    handServo   = null;
    public Servo    netServo   = null;
    public AnalogInput armAngle = null;
    public AndroidTextToSpeech tts = null;
    public AndroidGyroscope gyro = null;
    public AndroidOrientation orientation = null;
    public AndroidAccelerometer accelerometer = null;
    public ColorSensor sensorColor = null;
    public DistanceSensor sensorDistance = null;

    public static final double ARM_POWER    =  0.4d;
    public static final double ARM_MIN = 0.35d, ARM_MAX = 2.08d;
    public static final double ARM_SERVO_SPEED = 35d;

    public boolean autoArm = true;
    public void toggleAutoArm() {
        autoArm = !autoArm;
        if (autoArm) {
            tts.speak("Auto Arm enabled.");
        } else {
            tts.speak("Auto Arm disabled.");
    }
    }

    public int armSequence = 0;

    public void initiateHook() {
        if (armSequence == 0) {
            armSequence = 3;
            tts.speak("Initiate hook sequence");
        }
    }

    public  void initateScoop() {
        if (armSequence == 0) {
            armSequence = 6;
        }
    }

    public  void initateShoot() {
        if (armSequence == 0) {
            armSequence = 9;
        }
    }

    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public Hardware15091(){

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        leftDrive  = hwMap.dcMotor.get("motor_0");
        rightDrive = hwMap.dcMotor.get("motor_1");
        armDrive    = hwMap.dcMotor.get("motor_2");
        armAngle = hwMap.analogInput.get("arm_angle");
        armServo = hwMap.servo.get("servo_1");
        netServo = hwMap.servo.get("servo_2");
        handServo = hwMap.servo.get("servo_3");
        sensorColor = hwMap.get(ColorSensor.class, "sensor_color_distance");
        sensorDistance = hwMap.get(DistanceSensor.class, "sensor_color_distance");

        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);

        tts = new AndroidTextToSpeech();
        tts.initialize();
        tts.setLanguage("eng");

        gyro = new AndroidGyroscope();
        accelerometer = new AndroidAccelerometer();
        orientation = new AndroidOrientation();

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        leftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        armDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Define and initialize ALL installed servos.
        InitArm();

        // Set all motors to zero power
        leftDrive.setPower(0);
        rightDrive.setPower(0);
        armDrive.setPower(0);

        tts.speak("Hello aztec, good luck.");
    }

    public void InitArm()
    {
        armServo.setPosition(0d);
        handServo.setPosition(0d);
        period.reset();

        while(armAngle.getVoltage() > ARM_MIN && (period.seconds() < 10d)) {
            armDrive.setPower(0.25d);
        }

        armDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        armDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
 }

