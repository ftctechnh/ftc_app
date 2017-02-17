package org.firstinspires.ftc.team8200;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;


/**
 * Created by student on 1/12/17.
 *
 * The goal of this opmode is to shoot two particles in the center vortex, drive to
 * Red Alliance Beacon, press the correct beacon color, turn, go to the next beacon, turn
 * again, and press that beacon. Finally, the robot will turn toward the center vortex and park on the center vortex,
 * knocking away the capball in the process.
 */

@Autonomous(name="8200: Complete Autonomous Blue 2", group="K9bot")

public class CompleteAutonomousBlue2 extends LinearOpMode {


    HardwareK9bot robot = new HardwareK9bot(); // Hardware Device Object
    private ElapsedTime runtime = new ElapsedTime();

    //static variables (to avoid Magic Number issues)
    static final double WHITE_THRESHOLD = 0.4;
    static final double DRIVE_SPEED = 0.5;
    static final double DRIVE_SLOW_SPEED = 0.1;
    static final double TURN_SPEED = 1;
    static final double MAX_WHEEL_SHOOTER_SPEED = 1;
    static final int LED_CHANNEL = 5;
    static final String allianceColor = "blue"; // takes either value "red" or "blue"

    //static variables for encoders, not using this at the moment
    static final double COUNTS_PER_MOTOR_REV = 280;    // according to NeveRest 40 spec sheet
    static final double DRIVE_GEAR_REDUCTION = 40.0;     // This is < 1.0 if geared UP, pretty sure direct drive is 1.0?
    static final double WHEEL_DIAMETER_INCHES = 2.8;     // For figuring circumference, needs to be updated
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);

    double voltage, maxVoltage, voltsPerInch, voltageInInches;
    boolean bLedOn = false;
    boolean autoTurnRight = true;
    boolean robotOnLine = false;

    double timingConstant = 0.009; //found through a linear regression for turns

    double redVal, blueVal, greenVal;
    @Override

    public void runOpMode() {
        if (allianceColor == "red") {
            autoTurnRight = false;
        } else if (allianceColor == "blue") {
            autoTurnRight = true;
        }
        robot.init(hardwareMap); // Do not erase to avoid NullPointerException. This MUST be first in runOpMode()

        bLedOn = true;

        // Set the LED state for the Lego Light Sensor
        robot.lightSensorLeft.enableLed(bLedOn);

        // turn the LED on in the beginning, just so user will know that the sensor is active.
        robot.dim.setDigitalChannelState(LED_CHANNEL, bLedOn);

        waitForStart(); //pre-written function, waits for opmode to start

        turnRobot(90);
        turnRobot(180);
        turnRobot(360);
        turnRobot(-90);
        turnRobot(-180);
        turnRobot(-360);
        /*
        while (opModeIsActive() && robot.lightSensor.getLightDetected() < WHITE_THRESHOLD) {

            robot.leftMotor.setPower(DRIVE_SLOW_SPEED);
            robot.rightMotor.setPower(DRIVE_SLOW_SPEED);

        }


        while (opModeIsActive() && !pressButton()) {
            updateTelemetry();


        }

        moveToBeacon2();

        while (opModeIsActive() && !pressButton()) {
            updateTelemetry();

        }

        turnRobot(-135.0);

        moveForwardForShot();

        shoot();

        moveToCenterVortex();
        */
    }
    public void moveForwardForShot() {
        runtime.reset();
        while (opModeIsActive() && runtime.seconds() <= 1) {
            robot.leftMotor.setPower(1);
            robot.rightMotor.setPower(1);
        }
        robot.leftMotor.setPower(0);
        robot.rightMotor.setPower(0);
    }
    // shoot() runs the wheel shooters briefly before it powers up the elevator to launch two balls at the target
    public void shoot() {
        runtime.reset();
        while (opModeIsActive() && runtime.seconds() < 2.0) {
            robot.leftWheelShooter.setPower(1);
            robot.rightWheelShooter.setPower(1);
        }
        while (opModeIsActive() && runtime.seconds() >= 2.0 && runtime.seconds() < 5.0) {
            robot.elevator.setPower(-0.5);// Run elevator
            robot.leftWheelShooter.setPower(1);
            robot.rightWheelShooter.setPower(1);
        }
        // Kill wheelShooter and elevator
        robot.elevator.setPower(0);
        robot.leftWheelShooter.setPower(0);
        robot.rightWheelShooter.setPower(0);
    }
    /* What is this? */

   /* public void sideMethod() {
        while (opModeIsActive() && robot.lightSensor.getLightDetected() < WHITE_THRESHOLD && robot.colorSensor.blue() < blueVal && robot.colorSensor.red() < redVal && robot.colorSensor.green() < greenVal) {
            robot.leftMotor.setPower(DRIVE_SPEED);
            robot.rightMotor.setPower(DRIVE_SPEED);
        }
        while (opModeIsActive() && robot.lightSensor.getLightDetected() >= WHITE_THRESHOLD) {
            pressButton();
        }
    }

    /* pressButton() assumes the colorSensor is looking at the left beacon light. If it senses the color indicated in
    *   allianceColor, then it will move the leftArm, else it will move the right arm.
     */
    /*public boolean pressButton()
    {

        if (allianceColor == "red")
        {
            if(robot.colorSensor.red() > 1800 && robot.colorSensor.red() > robot.colorSensor.green() && robot.colorSensor.red() > robot.colorSensor.blue()) {
                robot.arm.setPosition(0.8);
                moveToBeacon2();
                return true;
            }

            else if (robot.colorSensor.blue() > 2350 && robot.colorSensor.blue() > robot.colorSensor.green() && robot.colorSensor.blue() > robot.colorSensor.red()) {
                robot.arm.setPosition(0);
                runtime.reset();
                while (runtime.seconds() > .8) {
                    robot.leftMotor.setPower(DRIVE_SPEED);
                    robot.rightMotor.setPower(DRIVE_SPEED);
                }
                robot.arm.setPosition(.8);
                moveToBeacon2();
                return true;
            }

            else
            {
                return false;

            }
        }
        // we have to finish the statement below
        else if (allianceColor == "blue")
        {

            if(robot.colorSensor.red() < 100 && robot.colorSensor.green() < 100 && robot.colorSensor.blue() > 180)
            {

                robot.arm.setPosition(0.8);
                return true;
            }

            else if (robot.colorSensor.red() > 180 && robot.colorSensor.green() < 100 && robot.colorSensor.blue() < 100)
            {
                robot.arm.setPosition(0.8);
                return true;
            }
            else
            {
                return false;
            }

        }

        return false;


    }


    /*  moveToBeacon2()

     */
    public void moveToBeacon2() {


    }


    /* moveToCenterVortex() allows the robot to turn around and move toward the center vortex, knocking the cap ball out of
    the way and parking on the center vortex
     */
    public void moveToCenterVortex()
    {
        runtime.reset();
        while(opModeIsActive() && runtime.seconds() <= 1.0)
        {
            robot.leftMotor.setPower(0.5);
            robot.rightMotor.setPower(0.5);
        }
        runtime.reset();
        while(opModeIsActive() && runtime.seconds() <= 1.0)
        {
            robot.leftMotor.setPower(0.5);
            robot.rightMotor.setPower(-0.5);
        }
        runtime.reset();
        while(opModeIsActive() && runtime.seconds() <= 3.0)
        {
            robot.leftMotor.setPower(-0.5);
            robot.rightMotor.setPower(-0.5);
        }

    }

    /* updateTelemetry method outputs telemetry data to the driver station when called */
   /* public void updateTelemetry() {

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
    public void turnRobot(double degrees) {

        ElapsedTime turnTime = new ElapsedTime();
        double motorTime = Math.abs(degrees) * timingConstant; //MUST update timingConstant to get this to work!!!
        while (opModeIsActive() && turnTime.seconds() <= motorTime) {
            telemetry.addData("Say", "Turning at ",degrees);
            telemetry.update();
            if (degrees > 0) //clockwise turn
            {
                robot.leftMotor.setPower(TURN_SPEED);
                robot.rightMotor.setPower(-TURN_SPEED);
            } else if (degrees < 0) {
                robot.leftMotor.setPower(-TURN_SPEED);
                robot.rightMotor.setPower(TURN_SPEED);
            }
        }
        robot.leftMotor.setPower(0);
        robot.rightMotor.setPower(0);
        sleep(1000); //remove from final op mode or make smaller
    }


    /* turnRobotEncoder takes an integer value for degrees, from -180 to 180, and uses the encoders and a formula to
        turn the robot accurately that many degrees
        turns clockwise (to the right) for degrees > 0
        turns counterclockwise (to the left) for degrees < 0
     */
    public void turnRobotEncoder(double degrees) {

        stopResetEncoders();
        double motorInches = degrees * .1;

        if (degrees > 0) //clockwise turn to the right
        {
            encoderDrive(TURN_SPEED, motorInches, -motorInches, 5000); //change for no encoders
        }
        else if (degrees < 0) {
            encoderDrive(TURN_SPEED, -motorInches, motorInches, 5000); //change for no encoders
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
