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

import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.android.AndroidTextToSpeech;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/**
 * This is NOT an opmode.
 * <p>
 * This class can be used to define all the specific hardware for a single robot.
 * In this case that robot is a Pushbot.
 * See PushbotTeleopTank_Iterative and others classes starting with "Pushbot" for usage examples.
 * <p>
 * This hardware class assumes the following device names have been configured on the robot:
 * Note:  All names are lower case and some have single spaces between words.
 * <p>
 * Motor channel:  Left  drive motor:        "left_drive"
 * Motor channel:  Right drive motor:        "right_drive"
 * Motor channel:  Manipulator drive motor:  "left_arm"
 * Servo channel:  Servo to open left claw:  "left_hand"
 * Servo channel:  Servo to open right claw: "right_hand"
 */
class Hardware15091 {

    static final double ARM_POWER = 1d;
    private static final double ARM_MIN = 0.709d, ARM_MAX = 2.4d;
    private static final double ARM_ANGLE_ENCODER_RATIO = 15161.0738d;
    private static final double P_ARM_COEFF = 0.0004d;     // Larger is more responsive, but also less stable
    /* Public OpMode members. */
    DcMotor leftDrive = null;
    DcMotor rightDrive = null;
    DcMotor armDrive = null;
    Servo armServo = null;
    Servo handServo = null;
    Servo pickupServo = null;
    Servo markerServo = null;
    AnalogInput armAngle = null;
    BNO055IMU imu;
    ColorSensor sensorColor = null;
    DistanceSensor sensorDistance = null;
    private AndroidTextToSpeech tts = null;
    private int beepSoundID;
    /* local OpMode members. */
    private HardwareMap hwMap = null;

    /* Constructor */
    Hardware15091() {

    }

    void speak(String stuff) {
        tts.speak(stuff);
    }

    ArmInfo setArmTarget(double targetToSet) {
        armDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        double voltageDelta = armAngle.getVoltage() - targetToSet;
        int targetDelta = (int) Math.round(voltageDelta * ARM_ANGLE_ENCODER_RATIO);
        int newTarget = armDrive.getCurrentPosition() + targetDelta;
        armDrive.setTargetPosition(newTarget);

        int turnsLeft = Math.abs(armDrive.getCurrentPosition() - newTarget);
        double error = Range.clip(turnsLeft * P_ARM_COEFF, 0, 1);
        return new ArmInfo(turnsLeft <= 100, ARM_POWER * error);
    }

    void setArmPower(double powerToSet) {
        if (armAngle.getVoltage() > ARM_MIN && powerToSet > 0d) {
            armDrive.setPower(powerToSet);
        } else if (armAngle.getVoltage() < ARM_MAX && powerToSet < 0d) {
            armDrive.setPower(powerToSet);
        } else {
            armDrive.setPower(0d);
        }
    }

    double getHeading() {
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        return (double) AngleUnit.DEGREES.fromUnit(angles.angleUnit, angles.firstAngle);
    }

    /* Initialize standard Hardware interfaces */
    void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        leftDrive = hwMap.dcMotor.get("motor_0");
        rightDrive = hwMap.dcMotor.get("motor_1");
        armDrive = hwMap.dcMotor.get("motor_2");
        armAngle = hwMap.analogInput.get("arm_angle");
        armServo = hwMap.servo.get("servo_3");
        pickupServo = hwMap.servo.get("servo_5");
        handServo = hwMap.servo.get("servo_4");
        markerServo = hwMap.servo.get("servo_0");
        sensorColor = hwMap.get(ColorSensor.class, "sensor_color_distance");
        sensorDistance = hwMap.get(DistanceSensor.class, "sensor_color_distance");

        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftDrive.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);
        armDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        tts = new AndroidTextToSpeech();
        tts.initialize();

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        leftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        armDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Set all motors to zero power
        leftDrive.setPower(0);
        rightDrive.setPower(0);
        armDrive.setPower(0);

        // Set up the parameters with which we will use our IMU. Note that integration
        // algorithm here just reports accelerations to the logcat log; it doesn't actually
        // provide positional information.
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = false;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = hwMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        beepSoundID = hwMap.appContext.getResources().getIdentifier("beep", "raw", hwMap.appContext.getPackageName());
    }

    final void beep() {
        new Thread() {
            public void run() {
                SoundPlayer.getInstance().startPlaying(hwMap.appContext, beepSoundID);
            }
        }.start();
    }

    class ArmInfo {
        boolean Done;
        double PowerToSet;

        ArmInfo(boolean done, double powerToSet) {
            Done = done;
            PowerToSet = powerToSet;
        }
    }
}

