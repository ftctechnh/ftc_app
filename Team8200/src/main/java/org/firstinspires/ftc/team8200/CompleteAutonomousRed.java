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

import org.firstinspires.ftc.robotcore.external.Telemetry;


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
    static final double MAX_WHEEL_SHOOTER_SPEED = 1;
    static final int LED_CHANNEL = 5;
    static final String allianceColor = "red"; // takes either value "red" or "blue"

    //static variables for encoders
    static final double COUNTS_PER_MOTOR_REV = 280;    // according to NeveRest 40 spec sheet
    static final double DRIVE_GEAR_REDUCTION = 1.0;     // This is < 1.0 if geared UP, pretty sure direct drive is 1.0?
    static final double WHEEL_DIAMETER_INCHES = 4.0;     // For figuring circumference, needs to be updated
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);

    double voltage, maxVoltage, voltsPerInch, voltageInInches;
    boolean bLedOn = false;
    boolean autoTurnRight = true;


    @Override

    public void runOpMode() {
        if (allianceColor == "red") {
            autoTurnRight = false;
        }
        else if (allianceColor == "blue") {
            autoTurnRight = true;
        }
        robot.init(hardwareMap); // Do not erase to avoid NullPointerException. This MUST be first in runOpMode()

        bLedOn = true;

        // Set the LED state for the Lego Light Sensor
        robot.lightSensor.enableLed(bLedOn);

        // turn the LED on in the beginning, just so user will know that the sensor is active.
        robot.dim.setDigitalChannelState(LED_CHANNEL, bLedOn);

        waitForStart(); //pre-written function, waits for opmode to start
//        moveForwardForShot(); //robot moves forward into range of basket
//        shoot(); //robot shoots two balls
        moveToBeacon(); //robot turns and moves toward the beacons, using line follower code and sensors to bring it to beacon
//
//        //continues until color is sensed (might want to have a failsafe in here in case color isn't being sensed...)
//        while (opModeIsActive() && !pressButton()) {
//
//            // send the info back to driver station using telemetry function.
//            updateTelemetry();
//        }
//
//        moveToBeacon2(); //robot turns to its left, goes toward next beacon, stops a little after reaching white line, and turns toward beacon
//
//        //continues until color is sensed (might want to have a failsafe in here in case color isn't being sensed...)
//        while (opModeIsActive() && !pressButton()) {
//
//            // send the info back to driver station using telemetry function.
//            updateTelemetry();
//
//        }
//        moveToCenterVortex(); //robot turns around and heads to park on center vortex, knocking cap ball on the way
    }

    // moveForwardForShot() moves the robot in position for shot, using encoders to travel the correct distance
    public void moveForwardForShot() {
        runtime.reset();
        while (opModeIsActive() && runtime.seconds() <= 1) {
            robot.leftMotor.setPower(-DRIVE_SPEED);
            robot.rightMotor.setPower(-DRIVE_SPEED);
        }
        robot.leftMotor.setPower(0);
        robot.rightMotor.setPower(0);
    }
    // shoot() runs the wheeled shooters briefly before powers up the elevator to launch two balls at the target
    public void shoot() {
        runtime.reset();
        while (opModeIsActive() && runtime.seconds() < 2.0) {
            robot.leftWheelShooter.setPower(1);
            robot.rightWheelShooter.setPower(1);
        }
        while (opModeIsActive() && runtime.seconds() >= 2.0 && runtime.seconds() < 5.0) {
            robot.legacyController.setMotorPower(1, -0.5);// Run elevator
            robot.leftWheelShooter.setPower(1);
            robot.rightWheelShooter.setPower(1);
        }
        // Kill wheelShooter and elevator
        robot.legacyController.setMotorPower(1, 0);
        robot.leftWheelShooter.setPower(0);
        robot.rightWheelShooter.setPower(0);
    }
    /* moveToBeacon() moves the robot from its shooting position to the first beacon
     * it uses a light sensor to detect when the robot has reached the white line
     * then it follows the white line to the beacon
     * the ODS recognizes when you have gotten close enough, and the robot comes to a stop
     */
    public void moveToBeacon() {
        //Align to go for line
//        runtime.reset();
//        while (opModeIsActive() && runtime.seconds() < 1.25) {
//            robot.leftMotor.setPower(-TURN_SPEED);
//            robot.rightMotor.setPower(0);
//        }
        // Move to white line
        while (opModeIsActive() && robot.lightSensor.getLightDetected() < WHITE_THRESHOLD) {
            robot.leftMotor.setPower(-DRIVE_SPEED);
            robot.rightMotor.setPower(-DRIVE_SPEED);
            updateTelemetry();
            telemetry.addData("Say", "Initial approach.");
        }
        while (opModeIsActive()) {
            //while the touch sensor is not touching the wall (or proximity sensor is not touching wall)
            // step 3 turning for ___ seconds
            runtime.reset();
            while (opModeIsActive() && (runtime.seconds() < 0.05)) {
                robot.rightMotor.setPower(-TURN_SPEED);
                robot.leftMotor.setPower(TURN_SPEED);
                telemetry.addData("Light", robot.lightSensor.getLightDetected());
                telemetry.addData("Say", "Motors turning.");
                telemetry.update();
            }

            //step 4 follow the line
            while (opModeIsActive() && robot.lightSensor.getLightDetected() >= WHITE_THRESHOLD) {
                //follows white light is above threshold AND touch sensor is not touching
                /*robot.leftMotor.setPower(0);
                robot.rightMotor.setPower(0);
                sleep(500);
                */
                if (autoTurnRight == false) {
                    robot.leftMotor.setPower(-TURN_SPEED);
                    robot.rightMotor.setPower(0);
                    telemetry.addData("Say", "turn left");
                    telemetry.update();
                }
                else if (autoTurnRight == true) {
                    robot.leftMotor.setPower(0);
                    robot.rightMotor.setPower(-TURN_SPEED);
                    autoTurnRight = false;
                    telemetry.addData("Say", "turn right");
                    telemetry.update();
                }
                telemetry.addData("Say", "Motors following line.");
                telemetry.update();
            }
        }
        robot.leftMotor.setPower(0);
        robot.rightMotor.setPower(0);
        robot.leftWheelShooter.setPower(0);
        robot.rightWheelShooter.setPower(0);
        robot.legacyController.setMotorPower(1, 0);
        robot.legacyController.setMotorPower(2, 0);
    }

    /* pressButton() assumes the colorSensor is looking at the left beacon light. If it senses the color indicated in
    *   allianceColor, then it will move the leftArm, else it will move the right arm.
     */
   /* public boolean pressButton()
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
        //  telemetry.addData("Raw", robot.lightSensor.getRawLightDetected());
        //telemetry.addData("Normal", robot.lightSensor.getLightDetected());
        telemetry.addData("Clear", robot.colorSensor.alpha());
        telemetry.addData("Red  ", robot.colorSensor.red());
        telemetry.addData("Green", robot.colorSensor.green());
        telemetry.addData("Blue ", robot.colorSensor.blue());
        telemetry.update();
    }

    /* turnRobot takes an integer value for degrees, from -180 to 180, and uses the encoders and a formula to
        turn the robot accurately that many degrees
        turns clockwise (to the right) for degrees > 0
        turns counterclockwise (to the left) for degrees < 0
     */
    public void turnRobot(int degrees) {

        stopResetEncoders();
        double motorInches = degrees * .1;

        if (degrees > 0) //clockwise turn to the right
        {
            encoderDrive(TURN_SPEED, motorInches, -motorInches, 5000);
        } else if (degrees < 0) {
            encoderDrive(TURN_SPEED, -motorInches, motorInches, 5000);
        }
    }

    /* stopResetEncoders() is a utility function that kills all motor activity and resets the encoder position to zero.

     */

    public void stopResetEncoders() {
        robot.leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        sleep(50); //small wait here to make sure it truly resets

        robot.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    /* encoderDrive() is a function that allows the robot to move according to the number of encoder ticks
        the speed should be one of the static variables defined above
        leftInches and rightInches is the distance in actual inches; however, the wheel diameter and gearing
        ratio must be accurate in order to ensure this works properly
        timeoutS is the failsafe for if the encoders are not working properly; they should not be used as a
        replacement for our typical motor timing algorithm which uses the ElapsedTime class)

      */
    public void encoderDrive(double speed,
                             double leftInches, double rightInches,
                             double timeoutS) {
        int newLeftTarget;
        int newRightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = robot.leftMotor.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
            newRightTarget = robot.rightMotor.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);
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
                telemetry.update();
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
}

