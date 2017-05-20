package org.firstinspires.ftc.teamcode.oldbot;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannelController;

import org.firstinspires.ftc.teamcode.drivetrain.ROUSAutoHardware_WithServos;
import org.firstinspires.ftc.teamcode.sensors.EvaluateColorSensor;
import org.firstinspires.ftc.teamcode.sensors.eColorState;


@Autonomous(name="AutoRed", group="Pushbot")
@Disabled
public class ROUSAutoDriveByEncoder_Red extends LinearOpMode {
    ColorSensor sensorRGB;
    DeviceInterfaceModule cdim;


    // we assume that the LED pin of the RGB sensor is connected to
    // digital port 5 (zero indexed).
    static final int LED_CHANNEL = 5;
    /* Declare OpMode members. */
    ROUSAutoHardware_WithServos robot   = new ROUSAutoHardware_WithServos();   // Use ROUS robot hardware
    private ElapsedTime     runtime = new ElapsedTime();

    static final double     COUNTS_PER_MOTOR_REV    = 1120 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = .625 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     Pi                      = 3.141592653f;
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION)/
            (WHEEL_DIAMETER_INCHES * Pi);
    static final double     DRIVE_SPEED             = .3;
    static final double     TURN_SPEED              = 0.07;
    static final double     SCAN_SPEED              =.08;


    @Override
    public void runOpMode() throws InterruptedException {
        // hsvValues is an array that will hold the hue, saturation, and value information.
        float hsvValues[] = {0F,0F,0F};

        // values is a reference to the hsvValues array.
        final float values[] = hsvValues;

        // get a reference to the RelativeLayout so we can change the background
        // color of the Robot Controller app to match the hue detected by the RGB sensor.
        final View relativeLayout = ((Activity) hardwareMap.appContext).findViewById(R.id.RelativeLayout);

        // bPrevState and bCurrState represent the previous and current state of the button.
        boolean bPrevState = false;
        boolean bCurrState = false;

        // bLedOn represents the state of the LED.
        boolean bLedOn = false;

        // get a reference to our DeviceInterfaceModule object.
        cdim = hardwareMap.deviceInterfaceModule.get("dim");

        // set the digital channel to output mode.
        // remember, the Adafruit sensor is actually two devices.
        // It's an I2C sensor and it's also an LED that can be turned on or off.
        cdim.setDigitalChannelMode(LED_CHANNEL, DigitalChannelController.Mode.OUTPUT);

        // get a reference to our ColorSensor object.
        sensorRGB = hardwareMap.colorSensor.get("color");

        // turn the LED on in the beginning, just so user will know that the sensor is active.
        cdim.setDigitalChannelState(LED_CHANNEL, bLedOn);

        // wait for the start button to be pressed.
        waitForStart();

        // loop and read the RGB data.
        // Note we use opModeIsActive() as our loop condition because it is an interruptible method.
        while (opModeIsActive()) {

            // check the status of the x button on gamepad.

            // check for button-press state transitions.
            if (bCurrState == true){

                // button is transitioning to a pressed state. Toggle the LED.
                bLedOn = !bLedOn;
                cdim.setDigitalChannelState(LED_CHANNEL, bLedOn);
            } else {

                bLedOn = false;
            }

            // update previous state variable.
            // convert the RGB values to HSV values.
            Color.RGBToHSV((sensorRGB.red() * 255) / 800, (sensorRGB.green() * 255) / 800, (sensorRGB.blue() * 255) / 800, hsvValues);

            // send the info back to driver station using telemetry function.
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
            idle(); // Always call idle() at the bottom of your while(opModeIsActive()) loop
        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
            robot.init(hardwareMap);

            // Send telemetry message to signify robot waiting;
            //telemetry.addData("Status", "Resetting Encoders");    //
            //telemetry.update();

            robot.leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            idle();

            robot.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            // Send telemetry message to indicate successful Encoder reset
            telemetry.addData("Path0", "Starting at %7d :%7d",
                robot.leftMotor.getCurrentPosition(),
                robot.rightMotor.getCurrentPosition());
            telemetry.update();
            // Wait for the game to start (driver presses PLAY)
            waitForStart();
            //int difference = sensorRGB.red()- sensorRGB.blue();
            // Step through each leg of the path,
            // Note: Reverse movement is obtained by setting a negative distance (not speed)
            encoderDrive(DRIVE_SPEED, 8, 8, 5);
            sleep(250);
            encoderDrive(TURN_SPEED, 4.348721293, -4.348721293, 5);
            sleep(250);
            encoderDrive(DRIVE_SPEED, 55, 55, 5);
            sleep(250);
            encoderDrive(TURN_SPEED,4.505131132, -4.505131132, 5);
            sleep(250);
            encoderDrive(SCAN_SPEED, 24 , 24, 10);
            sleep(250);
            encoderDrive(DRIVE_SPEED, -4, -4, 10 );
            sleep(250);




            /*encoderDrive(DRIVE_SPEED, 24, 24, 20);
            sleep(250);                                                // S1: Forward 45 Inches with 20 Sec timeout
            encoderDrive(TURN_SPEED, 10.99557429, -10.99557429, 20);
            sleep(250);
            encoderDrive(DRIVE_SPEED, 24, 24, 20);
            sleep(250);
            encoderDrive(TURN_SPEED, 10.99557429, -10.99557429,20 );
            sleep(250);
            encoderDrive(DRIVE_SPEED, 24, 24, 20);
            sleep(250);
            encoderDrive(TURN_SPEED, 10.99557429, -10.99557429, 20);
            sleep(250);
            encoderDrive(DRIVE_SPEED, 12, 12, 10);
            sleep(250);
            encoderDrive(TURN_SPEED, 10.99557429, -10.99557429, 20);
            sleep(250);
            encoderDrive(DRIVE_SPEED, 12, 12, 10);
            sleep(250);
            encoderDrive(TURN_SPEED, 10.99557429, -10.99557429, 20);
            sleep(250);
            //encoderDrive(TURN_SPEED, 11.38827337, -11.38827337, 20);
            //sleep(250);                                                            //S3: Turn 90deg to the left to return to a strait orientation// S5: Turn 90deg to the right
            //encoderDrive(DRIVE_SPEED, 42, 42, 10);
            //sleep(250);
*/
            while (opModeIsActive()) {
                Boolean Blue = EvaluateColorSensor.EvaluateColor(sensorRGB, eColorState.blue);
                Boolean Red  = EvaluateColorSensor.EvaluateColor(sensorRGB,eColorState.red);
                if (Blue) {                //red is more than blue
                    encoderDrive(DRIVE_SPEED, -2, -2, 10);
                    telemetry.addData("Color", "BLUE");
                    telemetry.update();
                    sleep(500);
                    telemetry.addData("Color", "BLUE");
                    telemetry.update();
                    sleep(125);
                    //robot.PressR.setPosition(.79);
                    //robot.PressL.setPosition(.95);
                    sleep(250);
                    encoderDrive(SCAN_SPEED, 12, 12, 10);
                    sleep(125);
                    stop();

                } else if (Red) {
                    encoderDrive(DRIVE_SPEED, -2, -2, 10);
                    telemetry.addData("Color", "RED");
                    telemetry.update();
                    sleep(500);
                    telemetry.addData("Color", "RED");
                    telemetry.update();
                    sleep(125);
                    //robot.PressL.setPosition(.76);
                    //robot.PressR.setPosition(.95);
                    sleep(250);
                    encoderDrive(SCAN_SPEED, 12, 12, 10);
                    sleep(125);
                    stop();
                } else {
                    encoderDrive(SCAN_SPEED, .7, .5, 10);

                }

                telemetry.addData("Blue", Blue);
                telemetry.addData("Red", Red);
                telemetry.update();
                // if (sensorRGB.red()- sensorRGB.blue()){
                //   encoderDrive();
                //}

            }
        }
            telemetry.addData("Path", "Complete");
            telemetry.update();
        }


    /*
     *  Method to perform a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */
    public void encoderDrive(double speed,
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

              //sleep(250);   // optional pause after each move
        }
    }
}
