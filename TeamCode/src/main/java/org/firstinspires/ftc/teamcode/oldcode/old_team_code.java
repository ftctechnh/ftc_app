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
* NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY&#39;S PATENT RIGHTS ARE
GRANTED BY THIS
* LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
CONTRIBUTORS
* &quot;AS IS&quot; AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
TO,
* THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
PURPOSE
* ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS
BE LIABLE
* FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL
* DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
GOODS OR
* SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
HOWEVER
* CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
LIABILITY,
* OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
THE USE
* OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.List;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import java.util.Locale;
/**
* {@link SensorBNO055IMU} gives a short demo on how to use the BNO055 Inertial Motion
Unit (IMU) from AdaFruit.
*
* Use Android Studio to Copy this Class, and Paste it into your team&#39;s code folder with a new
name.
* Remove or comment out the @Disabled line to add this opmode to the Driver Station
OpMode list
*
* @see &lt;a href=&quot;http://www.adafruit.com/products/2472&quot;&gt;Adafruit IMU&lt;/a&gt;
*/
@TeleOp(name = &quot;MiddleNoCraterIMU&quot;, group = &quot;Sensor&quot;)
public class MiddleNoCraterIMU extends LinearOpMode
{
DcMotor armLift;

DcMotor frontLeftDrive, frontRightDrive, backLeftDrive, backRightDrive;
Servo markerDrop;
boolean left;
boolean right;
boolean middle;
// The IMU sensor object
BNO055IMU imu;
// State used for updating telemetry
Orientation angles;
Acceleration gravity;

//Vuforia below here
private static final String TFOD_MODEL_ASSET = &quot;RoverRuckus.tflite&quot;;
private static final String LABEL_GOLD_MINERAL = &quot;Gold Mineral&quot;;
private static final String LABEL_SILVER_MINERAL = &quot;Silver Mineral&quot;;
private static final String VUFORIA_KEY =
&quot;AdyWFXL/////AAABmabVwW2af0mcoL7Qkzy/QyRW/zL4XEWo6n5WTnfKvZz0u1WmcbupVi9llI
cYkk6bwAlxDmgCwk5v3RIFtvs5lLJVB8S+mAIlcc1psXZ29NY4Ve0E8VrELpDuufEV9sj4GJ9sSr
LcyMmIG5B5UjrphdJ5XRdG4eNPcUe8fyEABeEiKgTgHS+ybe2dQTMaKBl1sDzacK5g9xDBYm
/kFJx6P2PY6Pe1ncsVIVAzT54qqTOMXq2la69ztU/iLs0NQZR/IHJ3zv8HLlCquNULYGww2yWe
UoR7QpzePPqqO7i23LqJa7BL3cqf06zAU7GE3eJNt4PKNxgQlI5wiV93w1klo8zL4GQrZd7EeZ
Rks/D4bv9q&quot;;
/**
* {@link #vuforia} is the variable we will use to store our instance of the Vuforia
* localization engine.
*/
private VuforiaLocalizer vuforia;
/**
* {@link #tfod} is the variable we will use to store our instance of the Tensor Flow Object
* Detection engine.
*/
private TFObjectDetector tfod;
@Override public void runOpMode()
{
initializeIMU();

armInitEnc(&quot;armLift&quot;);
wheelInit(&quot;frontLeft&quot;, &quot;frontRight&quot;, &quot;backLeft&quot;, &quot;backRight&quot;);
boolean look = false;

markerDropInit(&quot;markerDrop&quot;);
// The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create
that
// first.
initVuforia();
if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
initTfod();
} else {
telemetry.addData(&quot;Sorry!&quot;, &quot;This device is not compatible with TFOD&quot;);
}
/** Wait for the game to begin */
telemetry.addData(&quot;&gt;&quot;, &quot;Press Play to start tracking&quot;);
telemetry.update();
// Wait until we&#39;re told to go
waitForStart();
tfod.activate();
// Start the logging of measured acceleration
imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);
if (opModeIsActive()) {
/** Activate Tensor Flow Object Detection. */
if (tfod != null) {
tfod.activate();
}
// Loop and update the dashboard
while (opModeIsActive())
{
final int scanTimeStart = 45000;

int scanTime = scanTimeStart;
double scanIncrement = 0.00001;

armMove(-.25, 3300);
wheelDrive(&quot;RIGHT&quot;, 0.2, 500);
sleep(250);
wheelDrive(&quot;BACKWARD&quot;, 0.2, 700);
wheelDrive(&quot;LEFT&quot;, 0.2, 800);

while (scanTime &gt; 0 &amp;&amp; right == false) // scanning for the cube on right
{
scanTime -= scanIncrement;
telemetry.addData(&quot;Scanning For Right&quot;, &quot; Bro&quot; );
if (tfod != null)
{
// getUpdatedRecognitions() will return null if no new information is available since
// the last time that call was made.
List&lt;Recognition&gt; updatedRecognitions = tfod.getUpdatedRecognitions();
if (updatedRecognitions != null)
{
telemetry.addData(&quot;# Object Detected&quot;, updatedRecognitions.size());
if(updatedRecognitions.size() &gt; 0)
{
}
for (Recognition recognition : updatedRecognitions)
{
if (recognition.getLabel().equals(LABEL_GOLD_MINERAL))
{
telemetry.addData(&quot;YO&quot;, &quot;IT WORKS NOW WE CAN FIND THE PROBLEM&quot; )
;
telemetry.update();
right = true;
}
}
}

}
telemetry.update();
}
scanTime = scanTimeStart;
if (right == true)
{
telemetry.addData(&quot;Right&quot;, &quot;True&quot;);
telemetry.update();
wheelDrive(&quot;BACKWARD&quot;, 0.2, 400);
wheelTurn45(&quot;LEFT&quot;, 0.25);
wheelDrive(&quot;BACKWARD&quot;, 0.2, 400);
wheelTurn45(&quot;RIGHT&quot;, 0.25);
wheelDrive(&quot;FORWARD&quot;, 0.2, 1200);
}
else
{
wheelDrive(&quot;RIGHT&quot;, 0.2, 1250);
while (scanTime &gt; 0 &amp;&amp; middle == false) // scanning for the cube in middle
{
scanTime -= scanIncrement;
telemetry.addData(&quot;Scanning For Middle&quot;, &quot; Bro&quot; );
if (tfod != null)
{
// getUpdatedRecognitions() will return null if no new information is available since
// the last time that call was made.
List&lt;Recognition&gt; updatedRecognitions = tfod.getUpdatedRecognitions();
if (updatedRecognitions != null)
{
telemetry.addData(&quot;# Object Detected&quot;, updatedRecognitions.size());
if(updatedRecognitions.size() &gt; 0)
{

}
for (Recognition recognition : updatedRecognitions)
{
if (recognition.getLabel().equals(LABEL_GOLD_MINERAL))
{
telemetry.addData(&quot;YO&quot;, &quot;IT WORKS NOW WE CAN FIND THE PROBLEM&quot; )
;
telemetry.update();
middle = true;
}
}
}
}
telemetry.update();
}
scanTime = scanTimeStart;
if (middle == true)
{
telemetry.addData(&quot;Middle&quot;, &quot;True&quot;);
telemetry.update();
wheelDrive(&quot;BACKWARD&quot;, 0.2, 400);
wheelTurn45(&quot;LEFT&quot;, 0.25);
wheelDrive(&quot;BACKWARD&quot;, 0.2, 400);
wheelTurn45(&quot;RIGHT&quot;, 0.25);
wheelDrive(&quot;FORWARD&quot;, 0.2, 1200);
}
else
{
wheelDrive(&quot;RIGHT&quot;, 0.2, 1250);
while (scanTime &gt; 0 &amp;&amp; left == false) // scanning for the cube on left
{
scanTime -= scanIncrement;

telemetry.addData(&quot;Scanning For Left&quot;, &quot; Bro&quot; );
if (tfod != null)
{
// getUpdatedRecognitions() will return null if no new information is available since
// the last time that call was made.
List&lt;Recognition&gt; updatedRecognitions = tfod.getUpdatedRecognitions();
if (updatedRecognitions != null)
{
telemetry.addData(&quot;# Object Detected&quot;, updatedRecognitions.size());
if(updatedRecognitions.size() &gt; 0)
{
}
for (Recognition recognition : updatedRecognitions)
{
if (recognition.getLabel().equals(LABEL_GOLD_MINERAL))
{
telemetry.addData(&quot;YO&quot;, &quot;IT WORKS NOW WE CAN FIND THE PROBLEM&quot; )
;
telemetry.update();
left = true;
}
}
}
}
telemetry.update();
}
if (left == true)
{
wheelDrive(&quot;BACKWARD&quot;, 0.2, 400);
wheelTurn45(&quot;LEFT&quot;, 0.25);
wheelTurn45(&quot;LEFT&quot;, 0.25);
wheelDrive(&quot;BACKWARD&quot;, 0.2, 400);
wheelTurn45(&quot;RIGHT&quot;, 0.25);
wheelDrive(&quot;FORWARD&quot;, 0.2, 1200);
}

else
{
wheelDrive(&quot;BACKWARD&quot;, 0.2, 400);
wheelTurn45(&quot;LEFT&quot;, 0.25);
wheelTurn45(&quot;LEFT&quot;, 0.25);
wheelDrive(&quot;BACKWARD&quot;, 0.2, 400);
wheelTurn45(&quot;RIGHT&quot;, 0.25);
wheelDrive(&quot;FORWARD&quot;, 0.2, 1200);
}
}
}
/*
wheelDrive(&quot;BACKWARD&quot;, 0.2, 1500);
wheelPark(250);
wheelTurn45(&quot;RIGHT&quot;, 0.25);
wheelPark(250);
dropMarkerTest(0.25, 650);
wheelDrive(&quot;FORWARD&quot;, 0.25, 2450);
telemetry.update();
wheelDrive(&quot;BACKWARD&quot;, 0, 0);
*/
stop();

}
if (tfod != null) {

tfod.shutdown();
}
}
}
public void armInitEnc(String name)
{
armLift = hardwareMap.get(DcMotor.class, name);
armLift.setDirection(DcMotor.Direction.FORWARD);
armLift.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);

}
public void armDropEnc(int downPosition, double power)
{
armLift.setMode(DcMotor.RunMode.RESET_ENCODERS);
armLift.setTargetPosition(downPosition);
armLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
armLift.setPower(power);
while(armLift.isBusy()){}
armLift.setPower(0);
armLift.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
}
public void armMove(double power, int sleep)
{
armLift.setPower(power);
sleep(sleep);
armLift.setPower(0);
}

public void markerDropInit(String name)
{
markerDrop = hardwareMap.get(Servo.class, name);
}
public void dropMarkerTest(double position, int Time)
{
markerDrop.setPosition(position);

}
public void wheelInit(String topLeft, String topRight, String bottomLeft, String bottomRight)
{
frontLeftDrive = hardwareMap.get(DcMotor.class, topLeft);
frontRightDrive = hardwareMap.get(DcMotor.class, topRight);
backLeftDrive = hardwareMap.get(DcMotor.class, bottomLeft);
backRightDrive = hardwareMap.get(DcMotor.class, bottomRight);
frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
backLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
backRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
}
public void wheelDrive(String direction,double speed, int milliseconds)
{
if(direction == &quot;FORWARD&quot;)
{
frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
backRightDrive.setDirection(DcMotor.Direction.FORWARD);
backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
}
if(direction == &quot;BACKWARD&quot;)
{
frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
backRightDrive.setDirection(DcMotor.Direction.REVERSE);

backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
}
if(direction == &quot;LEFT&quot;)
{
frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
backRightDrive.setDirection(DcMotor.Direction.REVERSE);
backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
}
if(direction == &quot;RIGHT&quot;)
{
frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
backRightDrive.setDirection(DcMotor.Direction.FORWARD);
backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
}
frontLeftDrive.setPower(speed);
frontRightDrive.setPower(speed);
backRightDrive.setPower(speed);
backLeftDrive.setPower(speed);
if (milliseconds != 0)
{
sleep(milliseconds);
frontLeftDrive.setPower(0);
frontRightDrive.setPower(0);
backLeftDrive.setPower(0);
backRightDrive.setPower(0);
}
}
public void wheelTurn45(String direction,double speed)
{
double targetHeading;
if(direction == &quot;RIGHT&quot;)
{
frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
backRightDrive.setDirection(DcMotor.Direction.REVERSE);
backLeftDrive.setDirection(DcMotor.Direction.REVERSE);

}
if(direction == &quot;LEFT&quot;)
{
frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
backRightDrive.setDirection(DcMotor.Direction.FORWARD);
backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
}

}
public void wheelTurn(String direction,double speed, int milliseconds)
{
if(direction == &quot;RIGHT&quot;)
{
frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
backRightDrive.setDirection(DcMotor.Direction.REVERSE);
backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
}
if(direction == &quot;LEFT&quot;)
{
frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
backRightDrive.setDirection(DcMotor.Direction.FORWARD);
backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
}
frontLeftDrive.setPower(speed);
frontRightDrive.setPower(speed);
backRightDrive.setPower(speed);
backLeftDrive.setPower(speed);
sleep(milliseconds);
frontLeftDrive.setPower(0);
frontRightDrive.setPower(0);
backLeftDrive.setPower(0);
backRightDrive.setPower(0);

}
public void wheelPark(int sleep)
{
frontLeftDrive.setPower(0);
frontRightDrive.setPower(0);
backLeftDrive.setPower(0);
backRightDrive.setPower(0);
sleep(sleep);
}
public void wheelParkDegrease(double rollTarget, double speed)
{
}
//----------------------------------------------------------------------------------------------
// IMU Configuration
//----------------------------------------------------------------------------------------------
void initializeIMU()
{
// Set up the parameters with which we will use our IMU. Note that integration
// algorithm here just reports accelerations to the logcat log; it doesn&#39;t actually
// provide positional information.
BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
parameters.calibrationDataFile = &quot;BNO055IMUCalibration.json&quot;; // see the calibration
sample opmode
parameters.loggingEnabled = true;
parameters.loggingTag = &quot;IMU&quot;;
parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
// Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
// on a Core Device Interface Module, configured to be a sensor of type &quot;AdaFruit IMU&quot;,
// and named &quot;imu&quot;.
imu = hardwareMap.get(BNO055IMU.class, &quot;imu&quot;);
imu.initialize(parameters);
// Set up our telemetry dashboard
composeTelemetry();
}

//----------------------------------------------------------------------------------------------
// Telemetry Configuration
//----------------------------------------------------------------------------------------------
void composeTelemetry() {
// At the beginning of each telemetry update, grab a bunch of data
// from the IMU that we will then display in separate lines.
telemetry.addAction(new Runnable() { @Override public void run()
{
// Acquiring the angles is relatively expensive; we don&#39;t want
// to do that in each of the three items that need that info, as that&#39;s
// three times the necessary expense.
angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX,
AngleUnit.DEGREES);
gravity = imu.getGravity();
}
});
telemetry.addLine()
.addData(&quot;status&quot;, new Func&lt;String&gt;() {
@Override public String value() {
return imu.getSystemStatus().toShortString();
}
})
.addData(&quot;calib&quot;, new Func&lt;String&gt;() {
@Override public String value() {
return imu.getCalibrationStatus().toString();
}
});
telemetry.addLine()
.addData(&quot;heading&quot;, new Func&lt;String&gt;() {
@Override public String value() {
return formatAngle(angles.angleUnit, angles.firstAngle);
}
})
.addData(&quot;roll&quot;, new Func&lt;String&gt;() {
@Override public String value() {
return formatAngle(angles.angleUnit, angles.secondAngle);
}
})

.addData(&quot;pitch&quot;, new Func&lt;String&gt;() {
@Override public String value() {
return formatAngle(angles.angleUnit, angles.thirdAngle);
}
});
telemetry.addLine()
.addData(&quot;grvty&quot;, new Func&lt;String&gt;() {
@Override public String value() {
return gravity.toString();
}
})
.addData(&quot;mag&quot;, new Func&lt;String&gt;() {
@Override public String value() {
return String.format(Locale.getDefault(), &quot;%.3f&quot;,
Math.sqrt(gravity.xAccel*gravity.xAccel
+ gravity.yAccel*gravity.yAccel
+ gravity.zAccel*gravity.zAccel));
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
return String.format(Locale.getDefault(), &quot;%.1f&quot;, AngleUnit.DEGREES.normalize(degrees));
}
/**
* Initialize the Vuforia localization engine.
*/
private void initVuforia() {
/*
* Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
*/
VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
parameters.vuforiaLicenseKey = VUFORIA_KEY;
parameters.cameraDirection = CameraDirection.BACK;

// Instantiate the Vuforia engine
vuforia = ClassFactory.getInstance().createVuforia(parameters);
// Loading trackables is not necessary for the Tensor Flow Object Detection engine.
}
/**
* Initialize the Tensor Flow Object Detection engine.
*/
private void initTfod() {
int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
&quot;tfodMonitorViewId&quot;, &quot;id&quot;, hardwareMap.appContext.getPackageName());
TFObjectDetector.Parameters tfodParameters = new
TFObjectDetector.Parameters(tfodMonitorViewId);
tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL,
LABEL_SILVER_MINERAL);
}
}
