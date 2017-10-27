package org.firstinspires.ftc.teamcode.GeneralCode.PreviousSeasonOpModes.VelocityVortexOpModes;

//Importing useful classes for the motors, servos, and sensors
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

public class State_Shoot_Cap_Ball extends LinearOpMode {

    //Declaing motor variables
    DcMotor fleft; //Front left drive motor
    DcMotor fright; //Front right drive motor
    DcMotor bleft; //Back left drive motor
    DcMotor bright; //Back right drive motor
    DcMotor intake; //Intake motor
    DcMotor flicker; //Flicker motor

    //Declaring servo variables
    Servo leftBeacon; //Hits left beacon button
    Servo rightBeacon; //Hits right beacon button

    //Declaring empty sensor variables here to make them global
    TouchSensor touch;
    ColorSensor color;
    LightSensor leftLight;
    LightSensor rightLight;
    UltrasonicSensor ultrasonic;
    BNO055IMU imu;

    //Setting up global variables
    ElapsedTime timer = new ElapsedTime();
    float heading;
    float lastHeading;
    float rawGyro;
    float gyroAdd = 0;

    //Change wheelDiameter depending on the diameter of the drive wheels
    float wheelDiameter = 4;//inches
    //Set driveSpeed depending on speed preference
    float driveSpeed = 0.25f;
    //colorThreshold is 1/2 * (line color + floor color)
    float colorThreshold = .26f;

    //Main function that automatically runs when program is started
    public void runOpMode() {
        initialize();
        shoot();
        positionNextBall();
        shoot();
        driveInches(60);
    }



    //Sets up variables and hardware maps for the start of the program
    public void initialize() {
        //Assigning values to the previously declared motor, beacon, and sensor variables
        fleft = hardwareMap.dcMotor.get("fleft");
        fright = hardwareMap.dcMotor.get("fright");
        bleft = hardwareMap.dcMotor.get("bleft");
        bright = hardwareMap.dcMotor.get("bright");
        flicker = hardwareMap.dcMotor.get("flicker");
        intake = hardwareMap.dcMotor.get("intake");

        //Reversing right motors so that they both go the same way
        fleft.setDirection(DcMotor.Direction.REVERSE);
        bleft.setDirection(DcMotor.Direction.REVERSE);

        //Reversing Flicker motor so that positive powers make it shoot
        flicker.setDirection(DcMotorSimple.Direction.REVERSE);

        //Setting up servo variables(these are continuous rotation servos but are setup as regular ones to simplify it)
        leftBeacon = hardwareMap.servo.get("leftBeacon");
        rightBeacon = hardwareMap.servo.get("rightBeacon");

        //Reverse right beacon pusher so that it goes the right way
        //rightBeacon.setDirection(Servo.Direction.REVERSE);

        //Setting up sensors
        touch = hardwareMap.touchSensor.get("touch");
        color = hardwareMap.colorSensor.get("color");
        leftLight = hardwareMap.lightSensor.get("rightLight");
        rightLight = hardwareMap.lightSensor.get("leftLight");
        ultrasonic = hardwareMap.ultrasonicSensor.get("ultrasonic");
        imu = hardwareMap.get(BNO055IMU.class, "imu"); //Gyro sensor

        //Turning on sensor light to enhance accuracy of readings
        color.enableLed(false);
        leftLight.enableLed(true);
        rightLight.enableLed(true);

        //Setting up data for gyro sensors
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "AdafruitIMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        imu.initialize(parameters);
        updateGyro();

        //Waiting for program to be started before starting
        waitForStart();

    }

    //Updates the gyro sensor and formats the angle so that it is easier to use
    public void updateGyro(){
        //Gets the raw value of the gyro sensor
        rawGyro = -imu.getAngularOrientation().toAxesReference(AxesReference.INTRINSIC).toAxesOrder(AxesOrder.ZYX).firstAngle;

        //Detects if the gyro sensor goes from 0-360 or 360-0 and adjusts gyroAdd to compensate
        if (lastHeading < 60 && rawGyro > 300) {
            gyroAdd = gyroAdd + 360;
        } else if (lastHeading > 300 && rawGyro < 60) {
            gyroAdd = gyroAdd - 360;
        }

        //Puts formatted angle in heading variable and sets the current value as last value for the next cycle
        heading = gyroAdd - rawGyro;
        lastHeading = rawGyro;
    }

    //Turns the robot a specified number of degrees(might not work when wheel direction is reversed) right = negative
    public void gyroTurn(float degrees) {
        //Sets the current heading as the initial heading for reference when turning
        float gyroHeadingInitial = heading;
        //Turns the correct direction until the angle has been reached
        if (degrees <= 0) {
            while (heading > degrees + gyroHeadingInitial && opModeIsActive()) {
                fleft.setPower(driveSpeed);
                bleft.setPower(driveSpeed);
                fright.setPower(-driveSpeed);
                bright.setPower(-driveSpeed);
                updateGyro();
            }
        } else {
            while (heading < degrees + gyroHeadingInitial && opModeIsActive()) {
                fleft.setPower(-driveSpeed);
                bleft.setPower(-driveSpeed);
                fright.setPower(driveSpeed);
                bright.setPower(driveSpeed);
                updateGyro();
            }
        }

        //Stops wheels
        fleft.setPower(0);
        bleft.setPower(0);
        fright.setPower(0);
        bright.setPower(0);
    }

    //Drives forward until it finds the line in front of the beacon and stops
    //Set direction to 1 to go forwards, and -1 to go backwards
    public void lineUp(int direction) {
        fleft.setPower(driveSpeed * .5 * direction);
        bleft.setPower(driveSpeed * .5 * direction);
        fright.setPower(driveSpeed * .5 * direction);
        bright.setPower(driveSpeed * .5 * direction);
        while(leftLight.getLightDetected() < colorThreshold && rightLight.getLightDetected() < colorThreshold && opModeIsActive()){}
        while(leftLight.getLightDetected() < colorThreshold && opModeIsActive()) {
            fright.setPower(0);
            bright.setPower(0);
            fleft.setPower(driveSpeed * .35 * direction);
            bleft.setPower(driveSpeed * .35 * direction);
        }
        while(rightLight.getLightDetected() < colorThreshold && opModeIsActive()) {
            fleft.setPower(0);
            bleft.setPower(0);
            fright.setPower(driveSpeed * .35 * direction);
            bright.setPower(driveSpeed * .35 * direction);
        }
        fleft.setPower(0);
        bleft.setPower(0);
        fright.setPower(0);
        bright.setPower(0);
    }

    //Uses the color sensor to detect which button to press, then presses it(not currently functional)
    public void pressBeacon() {
        //Checks if the color is red and hits it if it is(have to find a better way to detect the colors)
        leftBeacon.setPosition(0.5);
        rightBeacon.setPosition(0.5);

        if(color.red() > color.blue() && opModeIsActive()) {
            telemetry.addData("left", true);
            timer.reset();
            while(timer.seconds() < 2 && opModeIsActive())
                leftBeacon.setPosition(1);
            leftBeacon.setPosition(.5);
        }

        //If it isn't blue it hits the other one
        else {
            telemetry.addData("right", true);
            timer.reset();
            while(timer.seconds() < 2 && opModeIsActive())
                rightBeacon.setPosition(0);
            rightBeacon.setPosition(.5);
        }

        //Runs the pushers backwards to reset their position
        timer.reset();
        while(timer.seconds() < 2 && opModeIsActive()) {
            leftBeacon.setPosition(0);
            rightBeacon.setPosition(1);
        }
        leftBeacon.setPosition(.5);
        rightBeacon.setPosition(.5);
    }

    //Note for using encoders:
    //One rotation of an 40 gear reduction Andymark motor is 1120
    //One rotation of a Tetrix motor is 1440

    //Drives the robot a specified number of inches(negative value to go backwards)
    public void driveInches(float inches){
        //Figures out what value to give the encoder based on the amount of inches to be covered
        int encoderInput = (int) java.lang.Math.floor((inches / (wheelDiameter * 3.1416)) * 1120); //Change 1120 based on motor type
        //Resets encoders and sets the power and position to be used
        fright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fright.setTargetPosition(encoderInput);
        fright.setPower(driveSpeed);
        bright.setPower(driveSpeed);
        fleft.setPower(driveSpeed);
        bleft.setPower(driveSpeed);
        fright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        timer.reset();
        updateGyro();
        double initAngle = heading;
        while(fright.getCurrentPosition() < fright.getTargetPosition() && timer.seconds() < 5 && opModeIsActive()){
            //Waits until the motors have gone the right distance, then stops one side early if it reached the target position first
            updateGyro();
            if (heading > initAngle) {
                fleft.setPower(driveSpeed * 1.5);
                bleft.setPower(driveSpeed * 1.5);
                fright.setPower(driveSpeed * 0.5);
                bright.setPower(driveSpeed * 0.5);
            } else if (heading < initAngle) {
                fleft.setPower(driveSpeed * 0.5);
                bleft.setPower(driveSpeed * 0.5);
                fright.setPower(driveSpeed * 1.5);
                bright.setPower(driveSpeed * 1.5);
            } else {
                fleft.setPower(driveSpeed);
                bleft.setPower(driveSpeed);
                fright.setPower(driveSpeed);
                bright.setPower(driveSpeed);
            }
        }
        fright.setPower(0);
        fright.setPower(0);

        //Stops wheels and sets motors back to their regular mode
        fleft.setPower(0);
        bleft.setPower(0);
        fright.setPower(0);
        bright.setPower(0);
        fleft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        fright.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    //Runs the flicker at a specified power and encoder value, used for positionNextBall and shoot
    public void moveFlicker(int encoderInput, float power){
        //Must be multiplied by 3/2 because it has a gear reduction of 60 instead of the usual 40
        encoderInput = (int) java.lang.Math.floor(encoderInput * 1.5 * 0.9) ;
        flicker.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flicker.setTargetPosition(encoderInput);
        flicker.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flicker.setPower(power);
        timer.reset();
        while(Math.abs(flicker.getCurrentPosition()) < Math.abs(flicker.getTargetPosition()) && timer.seconds() < 5 && opModeIsActive()){
            telemetry.addData("flicker current:", flicker.getCurrentPosition());
            telemetry.addData("flicker target:", flicker.getTargetPosition());
            telemetry.update();
        }
        flicker.setPower(0);
    }

    //Positions the next ball in the flicker
    public void positionNextBall(){
        //Runs the intake motor which should correctly position the ball
        timer.reset();
        while(timer.seconds() < 3 && opModeIsActive()){
            intake.setPower(-1);
        }
        intake.setPower(0);

        //Moves the particle to the shooting location if the intake doesn't correctly deliver it
        //Note: 373 is 1/3 of rotation
        moveFlicker(-373, -.25f);
        moveFlicker(373, .25f);
        timer.reset();
        while (timer.seconds() < 1 && opModeIsActive()){}
    }

    //Shoots the ball with the flicker
    public void shoot(){
        //Moves the particle to the shooting location if the intake doesn't correctly deliver it
        moveFlicker(1120, .8f);
    }

    public void align(){
        driveInches(5);

        float distance = 0;
        while (distance == 0 && opModeIsActive()) {
            timer.reset();
            while(timer.seconds() < 1 && opModeIsActive()){}
            distance = (float) ultrasonic.getUltrasonicLevel();
        }

        fright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fright.setTargetPosition( (int) Math.floor( 105 * (distance / 2.54 - 2.5)));
        fright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fright.setPower(1.5 * driveSpeed);
        bright.setPower(1.5 * driveSpeed);
        while(fright.getCurrentPosition() < fright.getTargetPosition() && opModeIsActive()){}
        fright.setPower(0);
        bright.setPower(0);

        fleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fleft.setTargetPosition( (int) Math.floor(105 * (distance / 2.54 - 2.5)));
        fleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fleft.setPower(1.5 * driveSpeed);
        bleft.setPower(1.5 * driveSpeed);
        while (fleft.getCurrentPosition() < fleft.getTargetPosition() && opModeIsActive()){}
        fleft.setPower(0);
        bleft.setPower(0);
    }
}