package org.firstinspires.ftc.teamcode.autonomous;

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
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.drivetrain.DriveCommands;
import org.firstinspires.ftc.teamcode.drivetrain.ROUSAutoHardware_WithServos;
import org.firstinspires.ftc.teamcode.sensors.EvaluateColorSensor;
import org.firstinspires.ftc.teamcode.sensors.eColorState;

/**
 * Created by Connor on 2/9/2017.
 */
@Autonomous(name="AutoBeaconBlueODS", group="Pushbot")
//@Disabled
public class Blue1Becon extends LinearOpMode {

    /* Declare OpMode members. */

    ROUSAutoHardware_WithServos robot = new ROUSAutoHardware_WithServos();
    ModernRoboticsI2cGyro gyro = null;                                          // Additional Gyro device
    ColorSensor sensorRGB;
    DeviceInterfaceModule cdim;
    OpticalDistanceSensor ODS;

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

    static final double DRIVE_SPEED = .4;   // Nominal speed for better accuracy.
    static final double DRIVE_SPEED2 = .2;
    static final double TURN_SPEED = 0.08;  // Nominal half speed for better accuracy.
    static final double TURN_SPEED2 = 0.05;
    static final double SCAN_SPEED = .03;

    static final double HEADING_THRESHOLD = 10;      // As tight as we can make it with an integer gyro
    static final double P_TURN_COEFF = 0.05;         // Larger is more responsive, but also less stable
    static final double P_DRIVE_COEFF = .075;        // Larger is more responsive, but also less stable
    static final long Pause_Time = 25;
    static final long Pause_Time_int= 250;
    static final long Pause_Time_Telemetry= 2500;

    static final double UP = .3;
    static final double DOWN = .9;
    static final double IN = .65;
    static final double OUT = .9;

    double odsReadingRaw;
    double odsReadingRaw2;

    // odsReadingRaw to the power of (-0.5)

    static double odsReadingLinear;
    static double odsReadingLinear2;


    @Override
    public void runOpMode() throws InterruptedException {

        gyro = (ModernRoboticsI2cGyro) hardwareMap.gyroSensor.get("gyro");

        telemetry.addData(">", "Calibrating Gyro");
        telemetry.update();

        gyro.calibrate();


        // make sure the gyro is calibrated before continuing
        while (!isStopRequested() && gyro.isCalibrating()) {
            sleep(50);
            idle();
        }

        telemetry.addData(">", "Gyro is Calibrated.");    //
        telemetry.update();

        sleep(Pause_Time_Telemetry);

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
        sleep(Pause_Time_int);

        // set the digital channel to output mode.
        // remember, the Adafruit sensor is actually two devices.
        // It's an I2C sensor and it's also an LED that can be turned on or off.

        cdim.setDigitalChannelMode(LED_CHANNEL, DigitalChannelController.Mode.OUTPUT);

        sleep(Pause_Time_int);

        // get a reference to our ColorSensor object.

        sensorRGB = hardwareMap.colorSensor.get("color");

        sleep(Pause_Time_int);

        // turn the LED on in the beginning, just so user will know that the sensor is active.

        cdim.setDigitalChannelState(LED_CHANNEL, bLedOn);

        sleep(Pause_Time_int);

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

        sleep(Pause_Time_Telemetry);


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

            sleep(Pause_Time_int);

            DriveCommands Command = new DriveCommands();
            Command.initializeForOpMode(this, hardwareMap);

            telemetry.addData(">","Activating ODS");
            telemetry.update();

            sleep(Pause_Time_Telemetry);

            ODS = hardwareMap.opticalDistanceSensor.get("ods");

            odsReadingRaw = ODS.getRawLightDetected();                   //update raw value (This function now returns a value between 0 and 5 instead of 0 and 1 as seen in the video)
            odsReadingLinear = Math.pow(odsReadingRaw, -0.5);

            sleep(Pause_Time_int);

            telemetry.addData(">","ODS is Active");
            telemetry.update();

            sleep(Pause_Time_Telemetry);

            // loop and read the RGB data.
            // Note we use opModeIsActive() as our loop condition because it is an interruptible method.

            robot.init(hardwareMap);

            sleep(Pause_Time_int);

            // Ensure the robot it stationary, then reset the encoders and calibrate the gyro.

            robot.leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            sleep(Pause_Time_int);

            // Send telemetry message to alert driver that we are calibrating;

            telemetry.addData(">","Calibrating Encoders");
            telemetry.update();

            sleep(Pause_Time_Telemetry);

            // Always call idle() at the bottom of your while(opModeIsActive()) loop


            robot.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(Pause_Time_int);

            telemetry.addData(">","Encoders are Calibrated");
            telemetry.update();

            sleep(Pause_Time_Telemetry);

            robot.button.setPosition(IN);

            telemetry.addData("Button","Setting position to 'IN'");
            telemetry.update();


            sleep(Pause_Time_Telemetry);

            robot.servo.setPosition(DOWN);
            telemetry.addData("Shooter","Setting position to 'DOWN'");
            telemetry.update();

            sleep(Pause_Time_Telemetry);

            while (!isStarted()) {
                telemetry.addData(">", "Robot Heading = %d", gyro.getIntegratedZValue());
                telemetry.addData("Clear", sensorRGB.alpha());
                telemetry.addData("Red  ", sensorRGB.red());
                telemetry.addData("Blue ", sensorRGB.blue());
                telemetry.addData("0 ODS Raw", odsReadingRaw);
                telemetry.addData("1 ODS linear", odsReadingLinear);
                telemetry.update();
                idle();
            }

            gyro.resetZAxisIntegrator();

            sleep(Pause_Time_int);

            telemetry.addData(">","Robot is ready. Press the Play button");
            telemetry.update();

            sleep(Pause_Time_Telemetry);



            waitForStart();

            robot.leftshooter.setPower(0);
            robot.rightshooter.setPower(0);

            // Step through each leg of the path,
            // Note: Reverse movement is obtained by setting a negative distance (not speed)
            // Put a hold after each turn

            sleep(Pause_Time);

            Command.gyroTurn(this,TURN_SPEED, 0);
            Command.Drive(this, DRIVE_SPEED2, 10, 10, 100);
            Command.gyroTurn(this,TURN_SPEED, -12);
            Command.Shoot(this, UP, DOWN);
            Command.Drive(this, DRIVE_SPEED2, .5,.5,10);
            Command.gyroTurn(this,TURN_SPEED, -60);
            Command.Drive(this, DRIVE_SPEED, 44, 44, 100);
            Command.gyroTurn(this,TURN_SPEED, -65);
            Command.Drive(this, DRIVE_SPEED, 20, 20, 100);
            Command.gyroTurn(this,TURN_SPEED, -75);
            Command.Drive(this, DRIVE_SPEED, 10, 10, 100);
            Command.gyroTurn(this,TURN_SPEED, 0);
            Command.OdsDrive(this, 20, 20);
            Command.Drive(this, DRIVE_SPEED, -25, -25, 10);

            sleep(Pause_Time);

            while (opModeIsActive()) {

            Boolean Blue = EvaluateColorSensor.EvaluateColor(sensorRGB, eColorState.blue);
            Boolean Red = EvaluateColorSensor.EvaluateColor(sensorRGB, eColorState.red);

            sleep(Pause_Time);

            Command.BeaconEvalBlue(this, Blue, Red, IN, OUT);
            }
        }


    }
}


