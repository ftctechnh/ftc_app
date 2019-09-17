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
* "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
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
* @see &lt;a href="http://www.adafruit.com/products/2472"&gt;Adafruit IMU&lt;/a&gt;
*/
@TeleOp(name = "MiddleNoCraterIMU", group = "Sensor")
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
private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
private static final String LABEL_SILVER_MINERAL = "Silver Mineral";
private static final String VUFORIA_KEY =
"AdyWFXL/////AAABmabVwW2af0mcoL7Qkzy/QyRW/zL4XEWo6n5WTnfKvZz0u1WmcbupVi9llI
cYkk6bwAlxDmgCwk5v3RIFtvs5lLJVB8S+mAIlcc1psXZ29NY4Ve0E8VrELpDuufEV9sj4GJ9sSr
LcyMmIG5B5UjrphdJ5XRdG4eNPcUe8fyEABeEiKgTgHS+ybe2dQTMaKBl1sDzacK5g9xDBYm
/kFJx6P2PY6Pe1ncsVIVAzT54qqTOMXq2la69ztU/iLs0NQZR/IHJ3zv8HLlCquNULYGww2yWe
UoR7QpzePPqqO7i23LqJa7BL3cqf06zAU7GE3eJNt4PKNxgQlI5wiV93w1klo8zL4GQrZd7EeZ
Rks/D4bv9q";
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

armInitEnc("armLift");
wheelInit("frontLeft", "frontRight", "backLeft", "backRight");
boolean look = false;

markerDropInit("markerDrop");
// The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create
that
// first.
initVuforia();
if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
initTfod();
} else {
telemetry.addData("Sorry!", "This device is not compatible with TFOD");
}
/** Wait for the game to begin */
telemetry.addData("&gt;", "Press Play to start tracking");
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
wheelDrive("RIGHT", 0.2, 500);
sleep(250);
wheelDrive("BACKWARD", 0.2, 700);
wheelDrive("LEFT", 0.2, 800);

while (scanTime &gt; 0 &amp;&amp; right == false) // scanning for the cube on right
{
scanTime -= scanIncrement;
telemetry.addData("Scanning For Right", " Bro" );
if (tfod != null)
{
// getUpdatedRecognitions() will return null if no new information is available since
// the last time that call was made.
List&lt;Recognition&gt; updatedRecognitions = tfod.getUpdatedRecognitions();
if (updatedRecognitions != null)
{
telemetry.addData("# Object Detected", updatedRecognitions.size());
if(updatedRecognitions.size() &gt; 0)
{
}
for (Recognition recognition : updatedRecognitions)
{
if (recognition.getLabel().equals(LABEL_GOLD_MINERAL))
{
telemetry.addData("YO", "IT WORKS NOW WE CAN FIND THE PROBLEM" )
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
telemetry.addData("Right", "True");
telemetry.update();
wheelDrive("BACKWARD", 0.2, 400);
wheelTurn45("LEFT", 0.25);
wheelDrive("BACKWARD", 0.2, 400);
wheelTurn45("RIGHT", 0.25);
wheelDrive("FORWARD", 0.2, 1200);
}
else
{
wheelDrive("RIGHT", 0.2, 1250);
while (scanTime &gt; 0 &amp;&amp; middle == false) // scanning for the cube in middle
{
scanTime -= scanIncrement;
telemetry.addData("Scanning For Middle", " Bro" );
if (tfod != null)
{
// getUpdatedRecognitions() will return null if no new information is available since
// the last time that call was made.
List&lt;Recognition&gt; updatedRecognitions = tfod.getUpdatedRecognitions();
if (updatedRecognitions != null)
{
telemetry.addData("# Object Detected", updatedRecognitions.size());
if(updatedRecognitions.size() &gt; 0)
{

}
for (Recognition recognition : updatedRecognitions)
{
if (recognition.getLabel().equals(LABEL_GOLD_MINERAL))
{
telemetry.addData("YO", "IT WORKS NOW WE CAN FIND THE PROBLEM" )
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
telemetry.addData("Middle", "True");
telemetry.update();
wheelDrive("BACKWARD", 0.2, 400);
wheelTurn45("LEFT", 0.25);
wheelDrive("BACKWARD", 0.2, 400);
wheelTurn45("RIGHT", 0.25);
wheelDrive("FORWARD", 0.2, 1200);
}
else
{
wheelDrive("RIGHT", 0.2, 1250);
while (scanTime &gt; 0 &amp;&amp; left == false) // scanning for the cube on left
{
scanTime -= scanIncrement;

telemetry.addData("Scanning For Left", " Bro" );
if (tfod != null)
{
// getUpdatedRecognitions() will return null if no new information is available since
// the last time that call was made.
List&lt;Recognition&gt; updatedRecognitions = tfod.getUpdatedRecognitions();
if (updatedRecognitions != null)
{
telemetry.addData("# Object Detected", updatedRecognitions.size());
if(updatedRecognitions.size() &gt; 0)
{
}
for (Recognition recognition : updatedRecognitions)
{
if (recognition.getLabel().equals(LABEL_GOLD_MINERAL))
{
telemetry.addData("YO", "IT WORKS NOW WE CAN FIND THE PROBLEM" )
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
wheelDrive("BACKWARD", 0.2, 400);
wheelTurn45("LEFT", 0.25);
wheelTurn45("LEFT", 0.25);
wheelDrive("BACKWARD", 0.2, 400);
wheelTurn45("RIGHT", 0.25);
wheelDrive("FORWARD", 0.2, 1200);
}

else
{
wheelDrive("BACKWARD", 0.2, 400);
wheelTurn45("LEFT", 0.25);
wheelTurn45("LEFT", 0.25);
wheelDrive("BACKWARD", 0.2, 400);
wheelTurn45("RIGHT", 0.25);
wheelDrive("FORWARD", 0.2, 1200);
}
}
}
/*
wheelDrive("BACKWARD", 0.2, 1500);
wheelPark(250);
wheelTurn45("RIGHT", 0.25);
wheelPark(250);
dropMarkerTest(0.25, 650);
wheelDrive("FORWARD", 0.25, 2450);
telemetry.update();
wheelDrive("BACKWARD", 0, 0);
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
if(direction == "FORWARD")
{
frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
backRightDrive.setDirection(DcMotor.Direction.FORWARD);
backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
}
if(direction == "BACKWARD")
{
frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
backRightDrive.setDirection(DcMotor.Direction.REVERSE);

backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
}
if(direction == "LEFT")
{
frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
backRightDrive.setDirection(DcMotor.Direction.REVERSE);
backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
}
if(direction == "RIGHT")
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
if(direction == "RIGHT")
{
frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
backRightDrive.setDirection(DcMotor.Direction.REVERSE);
backLeftDrive.setDirection(DcMotor.Direction.REVERSE);

}
if(direction == "LEFT")
{
frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
backRightDrive.setDirection(DcMotor.Direction.FORWARD);
backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
}

}
public void wheelTurn(String direction,double speed, int milliseconds)
{
if(direction == "RIGHT")
{
frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
backRightDrive.setDirection(DcMotor.Direction.REVERSE);
backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
}
if(direction == "LEFT")
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
parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration
sample opmode
parameters.loggingEnabled = true;
parameters.loggingTag = "IMU";
parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
// Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
// on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
// and named "imu".
imu = hardwareMap.get(BNO055IMU.class, "imu");
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
.addData("status", new Func&lt;String&gt;() {
@Override public String value() {
return imu.getSystemStatus().toShortString();
}
})
.addData("calib", new Func&lt;String&gt;() {
@Override public String value() {
return imu.getCalibrationStatus().toString();
}
});
telemetry.addLine()
.addData("heading", new Func&lt;String&gt;() {
@Override public String value() {
return formatAngle(angles.angleUnit, angles.firstAngle);
}
})
.addData("roll", new Func&lt;String&gt;() {
@Override public String value() {
return formatAngle(angles.angleUnit, angles.secondAngle);
}
})

.addData("pitch", new Func&lt;String&gt;() {
@Override public String value() {
return formatAngle(angles.angleUnit, angles.thirdAngle);
}
});
telemetry.addLine()
.addData("grvty", new Func&lt;String&gt;() {
@Override public String value() {
return gravity.toString();
}
})
.addData("mag", new Func&lt;String&gt;() {
@Override public String value() {
return String.format(Locale.getDefault(), "%.3f",
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
return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
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
"tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
TFObjectDetector.Parameters tfodParameters = new
TFObjectDetector.Parameters(tfodMonitorViewId);
tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL,
LABEL_SILVER_MINERAL);
}
}
