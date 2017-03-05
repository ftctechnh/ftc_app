package org.firstinspires.ftc.teamcode.prototype;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.drivetrain.DriveCommands;
import org.firstinspires.ftc.teamcode.drivetrain.ROUSAutoHardware_WithServos;

/**
 * Created by Connor on 2/9/2017.
 */
@Autonomous(name="AutoShoot2Blue", group="Pushbot")
//@Disabled
public class BlueShoot extends LinearOpMode {
    /* Declare OpMode members. */
    ROUSAutoHardware_WithServos robot = new ROUSAutoHardware_WithServos();   // Use a Pushbot's hardware
    ModernRoboticsI2cGyro gyro = null;                    // Additional Gyro device
    ColorSensor sensorRGB;
    DeviceInterfaceModule cdim;
    private ElapsedTime runtime = new ElapsedTime();
    static final int LED_CHANNEL = 5;
    static final double COUNTS_PER_MOTOR_REV = 1680;    // eg: TETRIX Motor Encoder
    static final double DRIVE_GEAR_REDUCTION = .625;     // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES = 4.0;     // For figuring circumference
    static final double Pi = 3.141592653f;
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
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

    static final double UP = .3;
    static final double DOWN = .9;


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
        telemetry.addData(">", "Initializing Color Sensor");
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
        // send the info back to driver station using telemetry function.
        telemetry.addData(">", "Color Sensor is Active");
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
        sleep(2000);


        waitForStart();
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
            DriveCommands Command = new DriveCommands();
            Command.initializeForOpMode( this, hardwareMap );
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
            robot.button.setPosition(.65);
            robot.servo.setPosition(DOWN);
            telemetry.update();
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
            Command.gyroTurn(this,TURN_SPEED, 0);
            Command.Drive(this,DRIVE_SPEED2, 10, 10, 10);
            Command.gyroTurn(this, TURN_SPEED, -12);
            sleep(1000);
            robot.servo.setPosition(UP);
            telemetry.addData(">", "Servo is in Up Position");
            telemetry.update();
            sleep(1000);
            robot.servo.setPosition(DOWN);
            telemetry.addData(">", "Servo is in Down Position");
            telemetry.update();
            robot.Intake.setPower(-1);
            sleep(5000);
            robot.Intake.setPower(0);
            sleep(2000);
            robot.servo.setPosition(UP);
            telemetry.addData(">", "Servo is in Up Position");
            telemetry.update();
            sleep(1000);
            robot.servo.setPosition(DOWN);
            telemetry.addData(">", "Servo is in Down Position");
            telemetry.update();
            sleep(2000);
            robot.leftshooter.setPower(0);
            robot.rightshooter.setPower(0);
            //TurnLeft(TURN_SPEED, 12 , 10);
            Command.Drive(this,DRIVE_SPEED, 34, 34, 10);
            Command.TurnRight(this,DRIVE_SPEED, 90, 10);
            Command. TurnLeft(this,DRIVE_SPEED, 90, 10);
            Command.gyroTurn(this,TURN_SPEED, 0);
            Command. Drive(this,DRIVE_SPEED, 16, 16, 10);
            stop();
        }
    }

}
