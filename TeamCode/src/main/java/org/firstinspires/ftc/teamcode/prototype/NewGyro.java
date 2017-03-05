/*
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
DAMAGES (INCLUDING, BUT NOT LIMITEDMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.firstinspires.ftc.teamcode.prototype;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.drivetrain.ROUSAutoHardware_WithServos;
import org.firstinspires.ftc.teamcode.fieldtracking.TurnCalc;
import org.firstinspires.ftc.teamcode.sensors.EvaluateColorSensor;
import org.firstinspires.ftc.teamcode.sensors.eColorState;

/**
 * This file illustrates the concept of driving a path based on Gyro heading and encoder counts.
 * It uses the common Pushbot hardware class to define the drive on the robot.
 * The code is structured as a LinearOpMode
 *
 * The code REQUIRES that you DO have encoders on the wheels,
 *   otherwise you would use: PushbotAutoDriveByTime;
 *
 *  This code ALSO requires that you have a Modern Robotics I2C gyro with the name "gyro"
 *   otherwise you would use: PushbotAutoDriveByEncoder;
 *
 *  This code requires that the drive Motors have been configured such that a positive
 *  power command moves them forward, and causes the encoders to count UP.
 *
 *  This code uses the RUN_TO_POSITION mode to enable the Motor controllers to generate the run profile
 *
 *  In order to calibrate the Gyro correctly, the robot must remain stationary during calibration.
 *  This is performed when the INIT button is pressed on the Driver Station.
 *  This code assumes that the robot is stationary when the INIT button is pressed.
 *  If this is not the case, then the INIT should be performed again.
 *
 *  Note: in this example, all angles are referenced to the initial coordinate frame set during the
 *  the Gyro Calibration process, or whenever the program issues a resetZAxisIntegrator() call on the Gyro.
 *
 *  The angle of movement/rotation is assumed to be a standardized rotation around the robot Z axis,
 *  which means that a Positive rotation is Counter Clock Wise, looking down on the field.
 *  This is consistent with the FTC field coordinate conventions set out in the document:
 *  ftc_app\doc\tutorial\FTC_FieldCoordinateSystemDefinition.pdf
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="AutoFullBlue", group="Pushbot")
@Disabled
public class NewGyro extends LinearOpMode {

    /* Declare OpMode members. */
    ROUSAutoHardware_WithServos robot = new ROUSAutoHardware_WithServos();   // Use a Pushbot's hardware
    ModernRoboticsI2cGyro gyro = null;                    // Additional Gyro device
    ColorSensor sensorRGB;
    DeviceInterfaceModule cdim;
    private ElapsedTime runtime = new ElapsedTime();
    static final int LED_CHANNEL = 5;
    static final double     COUNTS_PER_MOTOR_REV    = 1680 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = .625 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     Pi                      = 3.141592653f;
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION)/
            (WHEEL_DIAMETER_INCHES * Pi);

    // These constants define the desired driving/control characteristics
    // The can/should be tweaked to suite the specific robot drive train.
    static final double DRIVE_SPEED = .4;// Nominal speed for better accuracy.
    static final double DRIVE_SPEED2 = .2;
    static final double TURN_SPEED = 0.075;     // Nominal half speed for better accuracy.
    static final double SCAN_SPEED = .06;

    static final double HEADING_THRESHOLD = 10;      // As tight as we can make it with an integer gyro
    static final double P_TURN_COEFF = 0.05;     // Larger is more responsive, but also less stable
    static final double P_DRIVE_COEFF = .075;     // Larger is more responsive, but also less stable
    static final float Angle = 87;

    static final double UP = .95;
    static final double DOWN = .75;


    @Override
    public void runOpMode() throws InterruptedException {

        gyro = (ModernRoboticsI2cGyro) hardwareMap.gyroSensor.get("gyro");

        telemetry.addData(">", "Calibrating Gyro");    //
        telemetry.update();

        gyro.calibrate();


        // make sure the gyro is calibrated before continuing
        while (!isStopRequested() && gyro.isCalibrating()) {
            sleep(50);
            idle();
        }

        telemetry.addData(">", "Gyro is Calibrated.");    //
        telemetry.update();
        sleep(3000);
        telemetry.addData(">","Initializing Color Sensor");
        telemetry.update();
// hsvValues is an array that will hold the hue, saturation, and value information.
        float hsvValues[] = {0F, 0F, 0F};

        // values is a reference to the hsvValues array.
        final float values[] = hsvValues;

        // get a reference to the RelativeLayout so we can change the background
        // color of the Robot Controller app to match the hue detected by the RGB sensor.
        final View relativeLayout = ((Activity) hardwareMap.appContext).findViewById(com.qualcomm.ftcrobotcontroller.R.id.RelativeLayout);

        // bPrevState and bCurrState represent the previous and current state of the button.
        boolean bPrevState = false;
        boolean bCurrState = false;

        // bLedOn represents the state of the LED.
        boolean bLedOn = true;

        // get a reference to our DeviceInterfaceModule object.
        cdim = hardwareMap.deviceInterfaceModule.get("dim");
sleep(500);
        // set the digital channel to output mode.
        // remember, the Adafruit sensor is actually two devices.
        // It's an I2C sensor and it's also an LED that can be turned on or off.
        cdim.setDigitalChannelMode(LED_CHANNEL, DigitalChannelController.Mode.OUTPUT);
sleep(500);
        // get a reference to our ColorSensor object.
        sensorRGB = hardwareMap.colorSensor.get("color");
sleep(500);
        // turn the LED on in the beginning, just so user will know that the sensor is active.
        cdim.setDigitalChannelState(LED_CHANNEL, bLedOn);
sleep(500);
        // wait for the start button to be pressed.
        while (opModeIsActive()) {

            // check the status of the x button on gamepad.

            // check for button-press state transitions.
            if (bCurrState == true) {

                // button is transitioning to a pressed state. Toggle the LED.
                bLedOn = !bLedOn;
                cdim.setDigitalChannelState(LED_CHANNEL, bLedOn);
            } else {

                bLedOn = true;
            }

            // update previous state variable.
            // convert the RGB values to HSV values.
            Color.RGBToHSV((sensorRGB.red() * 255) / 800, (sensorRGB.green() * 255) / 800, (sensorRGB.blue() * 255) / 800, hsvValues);
sleep(500);
            // send the info back to driver station using telemetry function.
            telemetry.addData(">","Color Sensor is Active");
            telemetry.addData(">","Please test to make sure it is working");
            telemetry.addData("LED", bLedOn ? "On" : "Off");
            telemetry.addData("Clear", sensorRGB.alpha());
            telemetry.addData("Red  ", sensorRGB.red());
            telemetry.addData("Green", sensorRGB.green());
            telemetry.addData("Blue ", sensorRGB.blue());
            telemetry.addData("Hue", hsvValues[0]);

            // change the background color to match the color detected by the RGB sensor.
            // pass a reference to the hue, saturation, and value array as an argument
            // to the HSVToColor method.
            relativeLayout.post(new Runnable() {
                public void run() {
                    relativeLayout.setBackgroundColor(Color.HSVToColor(0xff, values));
                }
            });

            telemetry.update();
            sleep(10000);
            telemetry.addData(">", "Robot Heading = %d", gyro.getIntegratedZValue());
            telemetry.addData("Clear", sensorRGB.alpha());
            telemetry.addData("Red  ", sensorRGB.red());
            telemetry.addData("Green", sensorRGB.green());
            telemetry.addData("Blue ", sensorRGB.blue());
            telemetry.addData("Hue", hsvValues[0]);
            telemetry.update();
        waitForStart();

        // loop and read the RGB data.
        // Note we use opModeIsActive() as our loop condition because it is an interruptible method.

            robot.init(hardwareMap);
            // Ensure the robot it stationary, then reset the encoders and calibrate the gyro.
            robot.leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            // Send telemetry message to alert driver that we are calibrating;

            // Always call idle() at the bottom of your while(opModeIsActive()) loop


            robot.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.button.setPosition(0);
            robot.servo.setPosition(.8);
            telemetry.update();
            // Wait for the game to start (Display Gyro value), and reset gyro before we move..
            while (!isStarted()) {
                telemetry.addData(">", "Robot Heading = %d", gyro.getIntegratedZValue());
                telemetry.addData("Clear", sensorRGB.alpha());
                telemetry.addData("Red  ", sensorRGB.red());
                telemetry.addData("Green", sensorRGB.green());
                telemetry.addData("Blue ", sensorRGB.blue());
                telemetry.addData("Hue", hsvValues[0]);
                telemetry.update();
                idle();
            }
            gyro.resetZAxisIntegrator();
            robot.leftshooter.setPower(0);
            robot.rightshooter.setPower(0);
            // Step through each leg of the path,
            // Note: Reverse movement is obtained by setting a negative distance (not speed)
            // Put a hold after each turn
            robot.leftshooter.setPower(-1);
            robot.rightshooter.setPower(1);
            sleep(250);
            gyroTurn(TURN_SPEED, 0);
            Drive(DRIVE_SPEED2, 10, 10, 10000);
            robot.servo.setPosition(.2);
            sleep(250);
            robot.servo.setPosition(.8);
            robot.leftshooter.setPower(0);
            robot.rightshooter.setPower(0);
            robot.Intake.setPower(-1);
            TurnLeft(TURN_SPEED, 45, 10000);
            Drive(DRIVE_SPEED, 90, 90, 10000);
            robot.Intake.setPower(0);
            gyroTurn(TURN_SPEED, 0);
            Drive(DRIVE_SPEED2, 14, 14, 10000);
            gyroTurn( TURN_SPEED, 0);

            while (opModeIsActive()) {
                Boolean Blue = EvaluateColorSensor.EvaluateColor(sensorRGB, eColorState.blue);
                Boolean Red = EvaluateColorSensor.EvaluateColor(sensorRGB, eColorState.red);

                if (Blue) {
                    robot.leftshooter.setPower(-1);
                    robot.rightshooter.setPower(1);
                    gyroTurn( TURN_SPEED, 0);
                    Drive(DRIVE_SPEED, 4, 4, 10000);
                    telemetry.addData("Color", "BLUE");
                    telemetry.update();
                    sleep(125);
                    robot.button.setPosition(1);
                    sleep(500);
                    robot.button.setPosition(0);
                    gyroTurn(TURN_SPEED, 0);
                    Drive(DRIVE_SPEED, -46, -46, 10000);
                    gyroTurn(TURN_SPEED, 0);
                    if (Blue){
                        gyroTurn(TURN_SPEED,0);
                        sleep(125);
                        robot.button.setPosition(1);
                        sleep(500);
                        robot.button.setPosition(0);
                        Drive(DRIVE_SPEED, 0,5,10);
                        Drive(DRIVE_SPEED, 5, 5, 10);
                        gyroTurn(TURN_SPEED, 90);
                        robot.servo.setPosition(.2);
                        sleep(250);
                        robot.servo.setPosition(.8);
                        stop();


                    }else if (Red) {
                        gyroTurn(TURN_SPEED,0);
                        Drive(DRIVE_SPEED, -5, -5, 100);
                        sleep(125);
                        robot.button.setPosition(1);
                        sleep(500);
                        robot.button.setPosition(0);
                        Drive(DRIVE_SPEED, 0,5,10);
                        Drive(DRIVE_SPEED, 5, 5, 10);
                        gyroTurn(TURN_SPEED, 90);
                        robot.servo.setPosition(.2);
                        sleep(250);
                        robot.servo.setPosition(.8);
                        stop();
                    } else {
                        robot.leftMotor.setPower(-.06);
                        robot.rightMotor.setPower(-.06);
                        telemetry.addData(">", "Robot Heading = %d", gyro.getIntegratedZValue());
                        telemetry.addData("Clear", sensorRGB.alpha());
                        telemetry.addData("Red  ", sensorRGB.red());
                        telemetry.addData("Green", sensorRGB.green());
                        telemetry.addData("Blue ", sensorRGB.blue());
                        telemetry.update();

                    }





                } else if (Red) {
                    robot.leftshooter.setPower(-1);
                    robot.rightshooter.setPower(1);
                    gyroTurn( TURN_SPEED, 0);
                    Drive(DRIVE_SPEED, 9, 9, 100000);
                    telemetry.addData("Color", "RED");
                    telemetry.update();
                    sleep(125);
                    robot.button.setPosition(1);
                    sleep(500);
                    robot.button.setPosition(0);
                    gyroTurn(TURN_SPEED, 0);
                    Drive(DRIVE_SPEED, -46, -46, 10000);
                    gyroTurn( TURN_SPEED, 0);
                    if (Blue){
                        gyroTurn(TURN_SPEED,0);
                        Drive(DRIVE_SPEED, 4, 4, 100);
                        sleep(125);
                        robot.button.setPosition(1);
                        sleep(500);
                        robot.button.setPosition(0);
                        Drive(DRIVE_SPEED, 0,5,10);
                        Drive(DRIVE_SPEED, 5, 5, 10);
                        gyroTurn(TURN_SPEED, 90);
                        robot.servo.setPosition(.2);
                        sleep(250);
                        robot.servo.setPosition(.8);
                        stop();


                    }else if (Red) {
                        gyroTurn(TURN_SPEED,0);
                        Drive(DRIVE_SPEED, 20, 20, 100);
                        sleep(125);
                        robot.button.setPosition(1);
                        sleep(500);
                        robot.button.setPosition(0);
                        Drive(DRIVE_SPEED, 0,5,10);
                        Drive(DRIVE_SPEED, 5, 5, 10);
                        gyroTurn(TURN_SPEED, 90);
                        robot.servo.setPosition(.2);
                        sleep(250);
                        robot.servo.setPosition(.8);
                        stop();
                    } else {
                        robot.leftMotor.setPower(-.06);
                        robot.rightMotor.setPower(-.06);
                        telemetry.addData(">", "Robot Heading = %d", gyro.getIntegratedZValue());
                        telemetry.addData("Clear", sensorRGB.alpha());
                        telemetry.addData("Red  ", sensorRGB.red());
                        telemetry.addData("Green", sensorRGB.green());
                        telemetry.addData("Blue ", sensorRGB.blue());
                        telemetry.update();

                    }



                } else {
                    robot.leftMotor.setPower(.06);
                    robot.rightMotor.setPower(.06);
                    telemetry.addData(">", "Robot Heading = %d", gyro.getIntegratedZValue());
                    telemetry.addData("Clear", sensorRGB.alpha());
                    telemetry.addData("Red  ", sensorRGB.red());
                    telemetry.addData("Green", sensorRGB.green());
                    telemetry.addData("Blue ", sensorRGB.blue());
                    telemetry.update();




                }




            }

        }
    }
    public void Drive(double speed,
                      double leftInches, double rightInches,
                      double timeoutS) throws InterruptedException {
        int newLeftTarget;
        int newRightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = robot.leftMotor.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newRightTarget = robot.rightMotor.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            robot.leftMotor.setTargetPosition(newLeftTarget);
            robot.rightMotor.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            robot.leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            robot.leftMotor.setPower(Math.abs(speed));
            robot.rightMotor.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (robot.leftMotor.isBusy() && robot.rightMotor.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                        robot.leftMotor.getCurrentPosition(),
                        robot.rightMotor.getCurrentPosition());
                telemetry.addData(">", "Robot Heading = %d", gyro.getIntegratedZValue());
                telemetry.addData("Clear", sensorRGB.alpha());
                telemetry.addData("Red  ", sensorRGB.red());
                telemetry.addData("Green", sensorRGB.green());
                telemetry.addData("Blue ", sensorRGB.blue());
                telemetry.update();



                // Allow time for other processes to run.
                idle();
            }

            // Stop all motion;
            robot.leftMotor.setPower(0);
            robot.rightMotor.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(250);   // optional pause after each move
        }
    }
    public void TurnRight(double speed,
                          float turndeg,
                          double timeoutS) throws InterruptedException {
        int newLeftTarget;
        int newRightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = robot.leftMotor.getCurrentPosition() + (int)(TurnCalc.Turn(turndeg) * COUNTS_PER_INCH );
            newRightTarget = robot.rightMotor.getCurrentPosition() + (int)(TurnCalc.Turn(turndeg) * COUNTS_PER_INCH * -1f);
            robot.leftMotor.setTargetPosition(newLeftTarget);
            robot.rightMotor.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            robot.leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            robot.leftMotor.setPower(Math.abs(speed));
            robot.rightMotor.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (robot.leftMotor.isBusy() && robot.rightMotor.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                        robot.leftMotor.getCurrentPosition(),
                        robot.rightMotor.getCurrentPosition());
                telemetry.addData(">", "Robot Heading = %d", gyro.getIntegratedZValue());
                telemetry.addData("Clear", sensorRGB.alpha());
                telemetry.addData("Red  ", sensorRGB.red());
                telemetry.addData("Green", sensorRGB.green());
                telemetry.addData("Blue ", sensorRGB.blue());
                telemetry.update();



                // Allow time for other processes to run.
                idle();
            }

            // Stop all motion;
            robot.leftMotor.setPower(0);
            robot.rightMotor.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(250);   // optional pause after each move
        }
    }
    public void TurnLeft(double speed,
                         float turndeg,
                         double timeoutS) throws InterruptedException {
        int newLeftTarget;
        int newRightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = robot.leftMotor.getCurrentPosition() + (int) (TurnCalc.Turn(turndeg) * COUNTS_PER_INCH * -1f);
            newRightTarget = robot.rightMotor.getCurrentPosition() + (int) (TurnCalc.Turn(turndeg) * COUNTS_PER_INCH);
            robot.leftMotor.setTargetPosition(newLeftTarget);
            robot.rightMotor.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            robot.leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            robot.leftMotor.setPower(Math.abs(speed));
            robot.rightMotor.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (robot.leftMotor.isBusy() && robot.rightMotor.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1", "Running to %7d :%7d", newLeftTarget, newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.leftMotor.getCurrentPosition(),
                        robot.rightMotor.getCurrentPosition());
                telemetry.addData(">", "Robot Heading = %d", gyro.getIntegratedZValue());
                telemetry.addData("Clear", sensorRGB.alpha());
                telemetry.addData("Red  ", sensorRGB.red());
                telemetry.addData("Green", sensorRGB.green());
                telemetry.addData("Blue ", sensorRGB.blue());
                telemetry.update();


                // Allow time for other processes to run.
                idle();
            }

            // Stop all motion;
            robot.leftMotor.setPower(0);
            robot.rightMotor.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(250);   // optional pause after each move
        }
    }
    /**
     * Method to drive on a fixed compass bearing (angle), based on encoder counts.
     * Move will stop if either of these conditions occur:
     * 1) Move gets to the desired position
     * 2) Driver stops the opmode running.
     *
     * @param speed    Target speed for forward motion.  Should allow for _/- variance for adjusting heading
     * @param distance Distance (in inches) to move from current position.  Negative distance means move backwards.
     * @param angle    Absolute Angle (in Degrees) relative to last gyro reset.
     *                 0 = fwd. +ve is CCW from fwd. -ve is CW from forward.
     *                 If a relative angle is required, add/subtract from current heading.
     */
    public void gyroDrive(double speed,
                          double distance,
                          double angle) {

        int newLeftTarget;
        int newRightTarget;
        int moveCounts;
        double max;
        double error;
        double steer;
        double leftSpeed;
        double rightSpeed;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            moveCounts = (int) (distance * COUNTS_PER_INCH);
            newLeftTarget = robot.leftMotor.getCurrentPosition() + moveCounts;
            newRightTarget = robot.rightMotor.getCurrentPosition() + moveCounts;

            // Set Target and Turn On RUN_TO_POSITION
            robot.leftMotor.setTargetPosition(newLeftTarget);
            robot.rightMotor.setTargetPosition(newRightTarget);

            robot.leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // start motion.
            speed = Range.clip(Math.abs(speed), 0.0, .5);
            robot.leftMotor.setPower(speed);
            robot.rightMotor.setPower(speed);

            // keep looping while we are still active, and BOTH motors are running.
            while (opModeIsActive() &&
                    (robot.leftMotor.isBusy() && robot.rightMotor.isBusy())) {

                // adjust relative speed based on heading error.
                error = getError(angle);
                steer = getSteer(error, P_DRIVE_COEFF);

                // if driving in reverse, the motor correction also needs to be reversed
                if (distance < 0)
                    steer *= -1;

                leftSpeed = speed + steer;
                rightSpeed = speed - steer;

                // Normalize speeds if any one exceeds +/- 1.0;
                max = Math.max(Math.abs(leftSpeed), Math.abs(rightSpeed));
                if (max > 1) {
                    leftSpeed /= max;
                    rightSpeed /= max;
                }

                robot.leftMotor.setPower(leftSpeed);
                robot.rightMotor.setPower(rightSpeed);

                // Display drive status for the driver.
                telemetry.addData("Err/St", "%5.1f/%5.1f", error, steer);
                telemetry.addData("Target", "%7d:%7d", newLeftTarget, newRightTarget);
                telemetry.addData("Actual", "%7d:%7d", robot.leftMotor.getCurrentPosition(),
                        robot.rightMotor.getCurrentPosition());
                telemetry.addData("Speed", "%5.2f:%5.2f", leftSpeed, rightSpeed);
                telemetry.addData(">", "Robot Heading = %d", gyro.getIntegratedZValue());
                telemetry.update();
            }

            // Stop all motion;
            robot.leftMotor.setPower(0);
            robot.rightMotor.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    /**
     * Method to spin on central axis to point in a new direction.
     * Move will stop if either of these conditions occur:
     * 1) Move gets to the heading (angle)
     * 2) Driver stops the opmode running.
     *
     * @param speed Desired speed of turn.
     * @param angle Absolute Angle (in Degrees) relative to last gyro reset.
     *              0 = fwd. +ve is CCW from fwd. -ve is CW from forward.
     *              If a relative angle is required, add/subtract from current heading.
     */
    public void gyroTurn(double speed, double angle) {


        // keep looping while we have time remaining.
        // keep looping while we are still active, and not on heading.
        while (opModeIsActive() && !onHeading(speed, angle, P_TURN_COEFF)) {
            // Update telemetry & Allow time for other processes to run.
            telemetry.addData(">", "Robot Heading = %d", gyro.getIntegratedZValue());
            telemetry.addData("Clear", sensorRGB.alpha());
            telemetry.addData("Red  ", sensorRGB.red());
            telemetry.addData("Green", sensorRGB.green());
            telemetry.addData("Blue ", sensorRGB.blue());
            telemetry.update();
        }
    }


    /**
     * Method to obtain & hold a heading for a finite amount of time
     * Move will stop once the requested time has elapsed
     *
     * @param speed    Desired speed of turn.
     * @param angle    Absolute Angle (in Degrees) relative to last gyro reset.
     *                 0 = fwd. +ve is CCW from fwd. -ve is CW from forward.
     *                 If a relative angle is required, add/subtract from current heading.
     * @param holdTime Length of time (in seconds) to hold the specified heading.
     */
    public void gyroHoldRight(double speed, double angle, double holdTime) {

        ElapsedTime holdTimer = new ElapsedTime();

        // keep looping while we have time remaining.
        holdTimer.reset();
        while (opModeIsActive() && (holdTimer.time() < holdTime)) {
            // Update telemetry & Allow time for other processes to run.
            onHeadingR(speed, angle, P_TURN_COEFF);
            telemetry.addData(">", "Robot Heading = %d", gyro.getIntegratedZValue());
            telemetry.addData("Clear", sensorRGB.alpha());
            telemetry.addData("Red  ", sensorRGB.red());
            telemetry.addData("Green", sensorRGB.green());
            telemetry.addData("Blue ", sensorRGB.blue());
            telemetry.update();
        }

        // Stop all motion;
        robot.leftMotor.setPower(0);
        robot.rightMotor.setPower(0);
    }


    public void gyroHoldLeft(double speed, double angle, double holdTime) {

        ElapsedTime holdTimer = new ElapsedTime();

        // keep looping while we have time remaining.
        holdTimer.reset();
        while (opModeIsActive() && (holdTimer.time() < holdTime)) {
            // Update telemetry & Allow time for other processes to run.
            onHeadingL(speed, angle, P_TURN_COEFF);
            telemetry.addData(">", "Robot Heading = %d", gyro.getIntegratedZValue());
            telemetry.addData("Clear", sensorRGB.alpha());
            telemetry.addData("Red  ", sensorRGB.red());
            telemetry.addData("Green", sensorRGB.green());
            telemetry.addData("Blue ", sensorRGB.blue());
            telemetry.update();
        }

        // Stop all motion;
        robot.leftMotor.setPower(0);
        robot.rightMotor.setPower(0);
    }

    /**
     * Perform one cycle of closed loop heading control.
     *
     * @param speed  Desired speed of turn.
     * @param angle  Absolute Angle (in Degrees) relative to last gyro reset.
     *               0 = fwd. +ve is CCW from fwd. -ve is CW from forward.
     *               If a relative angle is required, add/subtract from current heading.
     * @param PCoeff Proportional Gain coefficient
     * @return
     */

    boolean onHeading(double speed, double angle, double PCoeff) {

        boolean onTarget = false;
        // determine turn power based on +/- error


        if (angle == gyro.getIntegratedZValue()) {
            robot.leftMotor.setPower(0);
            robot.rightMotor.setPower(0);
            sleep(500);
            onTarget = true;
        } else {
            if (gyro.getIntegratedZValue() - angle >= 1) {
                robot.rightMotor.setPower(speed);
                robot.leftMotor.setPower(-speed);


            } else {
                if (gyro.getIntegratedZValue() - angle <= -1) {

                    robot.rightMotor.setPower(-speed);
                    robot.leftMotor.setPower(speed);
                }
            }

        }


        return onTarget;
    }

    boolean onHeadingR(double speed, double angle, double PCoeff) {
        double error;
        double steer;
        boolean onTarget = false;
        double leftSpeed;
        double rightSpeed;

        // determine turn power based on +/- error
        error = getError(angle);

        if (Math.abs(error) <= HEADING_THRESHOLD) {
            steer = 0.0;
            leftSpeed = 0.0;
            rightSpeed = 0.0;
            onTarget = true;
        } else {
            steer = getSteer(error, PCoeff);
            rightSpeed = speed * steer;
            leftSpeed = -rightSpeed;
        }

        // Send desired speeds to motors.
        robot.leftMotor.setPower(leftSpeed);
        robot.rightMotor.setPower(rightSpeed);

        // Display it for the driver.
        telemetry.addData("Target", "%5.2f", angle);
        telemetry.addData("Err/St", "%5.2f/%5.2f", error, steer);
        telemetry.addData("Speed.", "%5.2f:%5.2f", leftSpeed, rightSpeed);

        return onTarget;
    }

    boolean onHeadingL(double speed, double angle, double PCoeff) {
        double error;
        double steer;
        boolean onTarget = false;
        double leftSpeed;
        double rightSpeed;

        // determine turn power based on +/- error
        error = getError(angle);

        if (Math.abs(error) <= HEADING_THRESHOLD) {
            steer = 0.0;
            leftSpeed = 0.0;
            rightSpeed = 0.0;
            onTarget = true;
        } else {
            steer = getSteer(error, PCoeff);
            leftSpeed = speed * steer;
            rightSpeed = -leftSpeed;
        }

        // Send desired speeds to motors.
        robot.leftMotor.setPower(leftSpeed);
        robot.rightMotor.setPower(rightSpeed);

        // Display it for the driver.
        telemetry.addData("Target", "%5.2f", angle);
        telemetry.addData("Err/St", "%5.2f/%5.2f", error, steer);
        telemetry.addData("Speed.", "%5.2f:%5.2f", leftSpeed, rightSpeed);

        return onTarget;
    }

    /**
     * getError determines the error between the target angle and the robot's current heading
     *
     * @param targetAngle Desired angle (relative to global reference established at last Gyro Reset).
     * @return error angle: Degrees in the range +/- 180. Centered on the robot's frame of reference
     * +ve error means the robot should turn LEFT (CCW) to reduce error.
     */
    public double getError(double targetAngle) {

        double robotError;

        // calculate error in -179 to +180 range  (
        robotError = targetAngle - gyro.getIntegratedZValue();
        while (robotError > 180) robotError -= 360;
        while (robotError <= -180) robotError += 360;
        return robotError;
    }

    /**
     * returns desired steering force.  +/- 1 range.  +ve = steer left
     *
     * @param error  Error angle in robot relative degrees
     * @param PCoeff Proportional Gain Coefficient
     * @return
     */
    public double getSteer(double error, double PCoeff) {
        return Range.clip(error * PCoeff, -1, 1);
    }


}
