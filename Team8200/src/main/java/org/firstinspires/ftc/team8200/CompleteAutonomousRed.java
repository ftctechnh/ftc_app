package org.firstinspires.ftc.team8200;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;



/**
 * Created by student on 1/12/17.
 *
 * The goal of this opmode is to shoot two particles in the center vortex, drive to
 * Red Alliance Beacon, press the correct beacon color, turn, go to the next beacon, turn
 * again, and press that beacon. Finally, the robot will turn toward the center vortex and park on the center vortex,
 * knocking away the capball in the process.
 */

@Autonomous(name="8200: Complete Autonomous Red", group="K9bot")

public class CompleteAutonomousRed extends LinearOpMode {


    HardwareK9bot robot = new HardwareK9bot(); // Hardware Device Object
    private ElapsedTime runtime = new ElapsedTime();

    //static variables (to avoid Magic Number issues)
    static final double WHITE_THRESHOLD = 0.4;
    static final double DRIVE_SPEED = 0.5;
    static final double DRIVE_SLOW_SPEED = 0.1;
    static final double TURN_SPEED = 0.25;
    static final int LED_CHANNEL = 5;
    static final String allianceColor = "red"; // takes either value "red" or "blue"

    //static variables for encoders
    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);

    double voltage, maxVoltage, voltsPerInch, voltageInInches;
    boolean bLedOn = false;


    @Override

    public void runOpMode() {

        bLedOn = true;

        // Set the LED state in the beginning.
        robot.lightSensor.enableLed(bLedOn);

        // turn the LED on in the beginning, just so user will know that the sensor is active.
        robot.cdim.setDigitalChannelState(LED_CHANNEL, bLedOn);


        waitForStart(); //prewritten function, waits for opmode to start
        moveForwardForShot(); //robot moves forward into range of basket
        shoot(); //robot shoots two balls
        moveToBeacon(); //robot turns and moves toward the beacons, using line follower code and sensors to bring it to beacon

        while (!pressButton()) {

            // send the info back to driver station using telemetry function.
            updateTelemetry();
        }

        moveToBeacon2(); //robot turns to its left, goes toward next beacon, stops a little after reaching white line, and turns toward beacon

        while (!pressButton()) {

            // send the info back to driver station using telemetry function.
            updateTelemetry();

        }
        pressButton(); //robot again presses button depending on color of alliance
        moveToCenterVortex(); //robot turns around and heads to park on center vortex, knocking cap ball on the way
    }

    /* moveForwardForShot() moves the robot in position for shot, using encoders to travel the correct distance */

    public void moveForwardForShot()
    {


    }

    /* shoot() runs the wheeled shooters briefly before powers up the elevator to launch two balls at the target */

    public void shoot()
    {


    }

    /* moveToBeacon() moves the robot from its shooting position to the first beacon
     * it uses a light sensor to detect when the robot has reached the white line
      * then it follows the white line to the beacon
      * the ODS recognizes when you have gotten close enough, and the robot comes to a stop
     */
    public void moveToBeacon() {
        // Move to white line
        while (robot.lightSensor.getLightDetected() < WHITE_THRESHOLD) {
            robot.leftMotor.setPower(DRIVE_SPEED);
            robot.rightMotor.setPower(DRIVE_SPEED);
            updateTelemetry();
        }
        while (opModeIsActive()) {
            //while the touch sensor is not touching the wall (or proximity sensor is not touching wall)
            // step 3 turning for ___ seconds
            runtime.reset();
            while (opModeIsActive() && (runtime.seconds() < 0.01)) {
                robot.leftMotor.setPower(0);
                robot.rightMotor.setPower(TURN_SPEED);
                telemetry.addData("Raw", robot.lightSensor.getRawLightDetected());
                telemetry.addData("Say", "Motors turning.");
                telemetry.update();
            }

            //step 4 follow the line
            while (robot.lightSensor.getLightDetected() >= WHITE_THRESHOLD) {
                //follows white light is above threshold AND touch sensor is not touching
                robot.leftMotor.setPower(TURN_SPEED);
                robot.rightMotor.setPower(TURN_SPEED);
                telemetry.addData("Say", "Motors following line.");
                telemetry.update();
            }
        }
    }

    /* pressButton() assumes the colorSensor is looking at the left beacon light. If it senses the color indicated in
    *   allianceColor, then it will move the leftArm, else it will move the right arm.
     */
    public boolean pressButton()
    {

        if (allianceColor == "red")
        {
            if(robot.colorSensor.red() > 180 && robot.colorSensor.green() < 100 && robot.colorSensor.blue() < 100)
            {
                robot.leftArm.setPosition(0.8);
                robot.rightArm.setPosition(0);
                return true;
            }

            else if (robot.colorSensor.red() < 100 && robot.colorSensor.green() < 100 && robot.colorSensor.blue() > 180)

            {
                robot.leftArm.setPosition(0);
                robot.rightArm.setPosition(0.8);
                return true;
            }

            else
            {
                return false;

            }
        }

        else if (allianceColor == "blue")
        {

            if(robot.colorSensor.red() < 100 && robot.colorSensor.green() < 100 && robot.colorSensor.blue() > 180)
            {

                robot.leftArm.setPosition(0.8);
                robot.rightArm.setPosition(0);
                return true;
            }

            else if (robot.colorSensor.red() > 180 && robot.colorSensor.green() < 100 && robot.colorSensor.blue() < 100)
            {
                robot.leftArm.setPosition(0);
                robot.rightArm.setPosition(0.8);
            }
            else
            {
                return false;
            }

        }

        return false;


    }

    /*  moveToBeacon2() moves the robot toward the second beacon by turning the robot 90 degrees, driving toward the second
        tape line, detecting that tape line, and then turning 90 degrees to the right to face the beacon head on
        This method may require some testing to get the exact distance after passing the tape line correct
     */
    public void moveToBeacon2() {


    }


    /* moveToCenterVortex() allows the robot to turn around and move toward the center vortex, knocking the cap ball out of
    the way and parking on the center vortex
     */
    public void moveToCenterVortex() {


    }

    /* updateTelemetry method outputs telemetry data to the driver station when called */
    public void updateTelemetry() {
        telemetry.addData("Raw", robot.lightSensor.getRawLightDetected());
        telemetry.addData("Normal", robot.lightSensor.getLightDetected());
        telemetry.addData("Clear", robot.colorSensor.alpha());
        telemetry.addData("Red  ", robot.colorSensor.red());
        telemetry.addData("Green", robot.colorSensor.green());
        telemetry.addData("Blue ", robot.colorSensor.blue());
        telemetry.update();
    }

    /* turnRobot takes an integer value for degrees, from -180 to 180, and uses the encoders and a formula to
        turn the robot accurately that many degrees
     */
    public void turnRobot(int degrees) {


    }

    /* stopResetEncoders() is a utility function that kills all motor activity and resets the encoder position to zero.

     */

    public void stopResetEncoders() {
        robot.leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //might need to implement a small wait here

        robot.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

}