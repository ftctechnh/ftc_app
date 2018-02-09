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

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.SwitchableLight;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

import java.util.Locale;

/**
 * {@link Red1AutoSensors} gives a short demo on how to use the BNO055 Inertial Motion Unit (IMU) from AdaFruit.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 *
 * @see <a href="http://www.adafruit.com/products/2472">Adafruit IMU</a>
 */
@Autonomous(name = "Red1AutoSensors2000", group = "Preciousss")
//@Disabled                            // Comment this out to add to the opmode list
public class Red1AutoSensors extends LinearOpMode
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------


        //FR = Front Right, FL = Front Left, BR = Back Right, BL = Back Left.
        DcMotor motorFR;
        DcMotor motorFL;
        DcMotor motorBR;
        DcMotor motorBL;
        Servo servoTapper;
        CRServo servoConL;
        CRServo servoConR;
        DcMotor motorConL;
        DcMotor motorConR;

    // The IMU sensor object
        BNO055IMU imu;
        // State used for updating telemetry
        ModernRoboticsI2cRangeSensor rangeSensor;
        NormalizedColorSensor colorSensor;
        NormalizedRGBA colors;
        Orientation angles;
        Acceleration gravity;

        boolean iAmBlue = false;
        boolean iAmRed = true;
        boolean isBoxSide = true;


        private ElapsedTime runtime = new ElapsedTime();


    //----------------------------------------------------------------------------------------------
    // Main logic
    //----------------------------------------------------------------------------------------------

    @Override public void runOpMode() {

        // Set up the parameters with which we will use our IMU. Note that integration
        // algorithm here just reports accelerations to the logcat log; it doesn't actually
        // provide positional information.
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        rangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "rangeSensor");

        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "colorSensor");
        if (colorSensor instanceof SwitchableLight) {
            ((SwitchableLight) colorSensor).enableLight(true);}


        // V u f o r i a  s e t u p



        // H a r d w a r e   M a p p i n g

        motorFR = hardwareMap.dcMotor.get("motorFR");
        motorFR.setDirection(DcMotor.Direction.REVERSE);
        motorFL = hardwareMap.dcMotor.get("motorFL");
        motorFL.setDirection(DcMotor.Direction.FORWARD);
        motorBL = hardwareMap.dcMotor.get("motorBL");
        motorBL.setDirection(DcMotor.Direction.FORWARD);
        motorBR = hardwareMap.dcMotor.get("motorBR");
        motorBR.setDirection(DcMotor.Direction.REVERSE);
        motorConL = hardwareMap.dcMotor.get("motorConL");
        motorConL.setDirection(DcMotor.Direction.FORWARD);
        motorConR = hardwareMap.dcMotor.get("motorConR");
        motorConR.setDirection(DcMotor.Direction.FORWARD);
        servoConL = hardwareMap.crservo.get("servoConL");
        servoConL.setDirection(CRServo.Direction.FORWARD);
        servoConR = hardwareMap.crservo.get("servoConR");
        servoConR.setDirection(CRServo.Direction.REVERSE);
        servoTapper = hardwareMap.servo.get("tapper");


        // Set up our telemetry dashboard
        composeTelemetry();

        // Wait until we're told to go
        waitForStart();

        // J e w e l s
        boolean autoClear = false;
        telemetry.setAutoClear(autoClear);
        telemetry.addLine("starting");
        telemetry.update();

        boolean iSeeBlue = false;
        boolean iSeeRed = false;
        servoTapper.setPosition(0.2d);
        Wait(.2f);
        servoTapper.setPosition(0.675d);
        Wait(2f);

        telemetry.update();

        Wait ( 10f);

        if (colors.red > colors.blue) {
            iSeeRed = true;
            iSeeBlue = false;
        } else {
            iSeeBlue = true;

            iSeeRed = false;
        }


        if ((iSeeRed && iAmRed) || (iSeeBlue && iAmBlue)) {
            telemetry.addData("1", "move right");
            move(0f, .2f, .3f);
            Wait(.2);
            servoTapper.setPosition(0.2d);
            Wait(.2);
            move(0f, -.2f, .3f);
        } else {
            telemetry.addData("1", "move left");
            move(0f, -.2f, .3f);
            Wait(.2);
            servoTapper.setPosition(0.2d);
            Wait(.2);
            move(0f, .2f, .3f);
        }

        // Start the logging of measured acceleration
        imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);

        // Loop and update the dashboard
        while (opModeIsActive()) {
            telemetry.update();
        }
    }

    //----------------------------------------------------------------------------------------------
    // Telemetry Configuration
    //----------------------------------------------------------------------------------------------

    void composeTelemetry() {

        // At the beginning of each telemetry update, grab a bunch of data
        // from the IMU that we will then display in separate lines.
        telemetry.addAction(new Runnable() { @Override public void run()
                {
                // Acquiring the angles is relatively expensive; we don't want
                // to do that in each of the three items that need that info, as that's
                // three times the necessary expense.
                colors = colorSensor.getNormalizedColors();
                angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
                gravity  = imu.getGravity();
                }
            });

        telemetry.addLine()
            .addData("status", new Func<String>() {
                @Override public String value() {
                    return imu.getSystemStatus().toShortString();
                    }
                })
            .addData("calib", new Func<String>() {
                @Override public String value() {
                    return imu.getCalibrationStatus().toString();
                    }
                });

        telemetry.addLine()
            .addData("heading", new Func<String>() {
                @Override public String value() {
                    return formatAngle(angles.angleUnit, angles.firstAngle);
                    }
                })
            .addData("roll", new Func<String>() {
                @Override public String value() {
                    return formatAngle(angles.angleUnit, angles.secondAngle);
                    }
                })
            .addData("pitch", new Func<String>() {
                @Override public String value() {
                    return formatAngle(angles.angleUnit, angles.thirdAngle);
                    }
                });

        telemetry.addLine()
            .addData("grvty", new Func<String>() {
                @Override public String value() {
                    return gravity.toString();
                    }
                })
            .addData("mag", new Func<String>() {
                @Override public String value() {
                    return String.format(Locale.getDefault(), "%.3f",
                            Math.sqrt(gravity.xAccel*gravity.xAccel
                                    + gravity.yAccel*gravity.yAccel
                                    + gravity.zAccel*gravity.zAccel));
                    }
                });

        telemetry.addLine()
                .addData("r", new Func<Float>() {
                    @Override public Float value() {
                        return colors.red;
                    }
                })
                .addData("b", new Func<Float>() {
                    @Override public Float value() {
                        return colors.blue;
                    }
                });
        telemetry.addLine()
                .addData("raw ultrasonic", new Func<String>() {
                    @Override public String value() {
                        return String.format(Locale.getDefault(), "%d",
                                rangeSensor.rawUltrasonic());
                    }
                })
                .addData("raw optical", new Func<String>() {
                @Override public String value() {
                return String.format(Locale.getDefault(), "%d",
                        rangeSensor.rawOptical());
                    }
                })
                .addData("cm optical", new Func<String>() {
                    @Override public String value() {
                        return String.format(Locale.getDefault(), "%G cm",
                                rangeSensor.cmOptical());
                    }
                })
                .addData("cm", new Func<String>() {
                    @Override public String value() {
                        return String.format(Locale.getDefault(), "%.2f cm",
                                rangeSensor.getDistance(DistanceUnit.CM));
                    }
                });
    }

    //----------------------------------------------------------------------------------------------
    // Formatting
    //----------------------------------------------------------------------------------------------

    String formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }

    String formatDegrees(double degrees){
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }


        void Wait(double WaitTime) {
            runtime.reset();
            while (runtime.seconds() < WaitTime) {
                //Comment this out to avoid it overwriting other telemetry
                //telemetry.addData("5", " %2.5f S Elapsed", runtime.seconds());
                //telemetry.update();
            }
        }


        void move(float posx, float posy, float waitTime) {
            float FRBLPower = posy + posx;
            float FLBRPower = posy - posx;
            motorFR.setPower(FRBLPower);
            motorFL.setPower(FLBRPower);
            motorBR.setPower(FLBRPower);
            motorBL.setPower(FRBLPower);
            Wait(waitTime);
            sR();
        }

        void Conveyor (float waitTime)   {
            servoConL.setPower(1);
            servoConR.setPower(1);
            motorConL.setPower(1);
            motorConR.setPower(1);
            Wait(waitTime);
        }


        void sR() {
            float power = 0.f;
            motorFL.setPower(power);
            motorBL.setPower(power);
            motorFR.setPower(power);
            motorBR.setPower(power);
        }

}
