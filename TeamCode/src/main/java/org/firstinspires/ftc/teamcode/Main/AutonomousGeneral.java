package org.firstinspires.ftc.teamcode.Main;

import android.graphics.Color;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * Created by inspirationteam on 12/18/2016.
 */
@Disabled
public class AutonomousGeneral extends LinearOpMode {
    private static double COUNTS_PER_MOTOR_REV;    // eg: TETRIX Motor Encoder
    private static double DRIVE_GEAR_REDUCTION;     // 56/24
    private static double WHEEL_PERIMETER_CM;     // For figuring circumference
    private static double COUNTS_PER_CM;
    protected DcMotor front_right_motor;
    protected DcMotor front_left_motor;
    protected DcMotor back_right_motor;
    protected DcMotor back_left_motor;
    protected GyroSensor gyro;                      //turning clockwise = +degrees, turning counterclockwise = -degrees
    protected ModernRoboticsI2cRangeSensor rangeSensor;
    protected ColorSensor colorSensor;
    String currentColor = "other";
    protected static final double DRIVE_SPEED = .5;
    protected static final double TURN_SPEED = 0.5;
    // motor definition to shoot the small ball
    protected DcMotor shooting_motor;
    protected Servo beaconPress;


    // motor definition to intake the small ball
    protected DcMotor intake_motor;
    protected static ElapsedTime runtime = new ElapsedTime();
    public boolean operation_beacon_press = false;

    public void initiate() {
        COUNTS_PER_MOTOR_REV = 1440;
        DRIVE_GEAR_REDUCTION = 2.333;
        WHEEL_PERIMETER_CM = 29;
        COUNTS_PER_CM = (COUNTS_PER_MOTOR_REV) /
                (WHEEL_PERIMETER_CM * DRIVE_GEAR_REDUCTION);
        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        //robot.init(hardwareMap);
        front_left_motor = hardwareMap.dcMotor.get("leftWheelMotorFront");
        front_right_motor = hardwareMap.dcMotor.get("rightWheelMotorFront");
        back_left_motor = hardwareMap.dcMotor.get("leftWheelMotorBack");
        back_right_motor = hardwareMap.dcMotor.get("rightWheelMotorBack");

        // Connect to motor (Assume standard left wheel)
        // Change the text in quotes to match any motor name on your robot.
        shooting_motor = hardwareMap.dcMotor.get("ballShooterMotor");
        intake_motor = hardwareMap.dcMotor.get("ballCollectorMotor");

        //Initiate sensors:
        if (operation_beacon_press == true) {
            gyro = hardwareMap.gyroSensor.get("gyro");
            rangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "rangeSensor");
            colorSensor = hardwareMap.colorSensor.get("colorSensor");
            beaconPress = hardwareMap.servo.get("beaconPress");
            //calibrate gyro
            gyro.calibrate();
            while (gyro.isCalibrating()) {

            }
        }
        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        front_left_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        front_right_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        back_left_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        back_right_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        front_right_motor.setDirection(DcMotor.Direction.REVERSE);
        back_right_motor.setDirection(DcMotor.Direction.REVERSE);
        idle();

        front_left_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        front_right_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        back_left_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        back_right_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);




        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0", "Starting at %7d :%7d : %7d :%7d",
                back_left_motor.getCurrentPosition(),
                back_right_motor.getCurrentPosition(),
                front_left_motor.getCurrentPosition(),
                front_right_motor.getCurrentPosition());
        telemetry.update();

    }


    @Override
    public void runOpMode() {

    }

    public static double getCountsPerMotorRev() {
        ;
        return COUNTS_PER_MOTOR_REV;
    }

    public static double getDriveGearReduction() {
        return DRIVE_GEAR_REDUCTION;
    }

    public static double getWheelPerimeterCm() {
        return WHEEL_PERIMETER_CM;
    }

    public static double getCountsPerCm() {
        return COUNTS_PER_CM;
    }

    /**
     * Method to perfmorm a relative move, based on encoder counts.
     * Encoders are not reset as the move is based on the current position.
     * Move will stop if any of three conditions occur:
     * 1) Move gets to the desired position
     * 2) Move runs out of time
     * 3) Driver stops the opmode running.
     **/
    public void encoderDrive(double speed,
                             double leftInches, double rightInches,
                             double timeoutS) {
        int newLeftTarget;
        int newRightTarget;
        double leftSpeed;
        double rightSpeed;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = back_left_motor.getCurrentPosition() + (int) (leftInches * getCountsPerCm());
            newRightTarget = back_right_motor.getCurrentPosition() + (int) (rightInches * getCountsPerCm());
            back_left_motor.setTargetPosition(newLeftTarget);
            back_right_motor.setTargetPosition(newRightTarget);
            front_left_motor.setTargetPosition(newLeftTarget);
            front_right_motor.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            back_left_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            back_right_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            front_left_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            front_right_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            if (Math.abs(leftInches) > Math.abs(rightInches)) {
                leftSpeed = speed;
                rightSpeed = (speed * rightInches) / leftInches;
            } else {
                rightSpeed = speed;
                leftSpeed = (speed * leftInches) / rightInches;
            }
            runtime.reset();
            back_left_motor.setPower(Math.abs(leftSpeed));
            back_right_motor.setPower(Math.abs(rightSpeed));
            front_left_motor.setPower(Math.abs(leftSpeed));
            front_right_motor.setPower(Math.abs(rightSpeed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (back_left_motor.isBusy() && back_right_motor.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1", "Running to %7d :%7d", newLeftTarget, newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        back_left_motor.getCurrentPosition(),
                        back_right_motor.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            back_left_motor.setPower(0);
            back_right_motor.setPower(0);
            front_left_motor.setPower(0);
            front_right_motor.setPower(0);


            // Turn off RUN_TO_POSITION
            back_left_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            back_right_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            front_left_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            front_right_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        }

    }

    public void straightDrive(double power) {

        back_left_motor.setPower(power);
        front_left_motor.setPower(power);
        back_right_motor.setPower(power);
        front_right_motor.setPower(power);
    }

    public void turnLeft(double speed) {
        front_left_motor.setPower(-speed);
        back_left_motor.setPower(-speed);

        front_right_motor.setPower(speed);
        back_right_motor.setPower(speed);
    }

    public void turnRight(double speed) {
        front_right_motor.setPower(-speed);
        back_right_motor.setPower(-speed);

        front_left_motor.setPower(speed);
        back_left_motor.setPower(speed);
    }

    public void stopMotors() {

        front_right_motor.setPower(0);
        front_left_motor.setPower(0);
        back_right_motor.setPower(0);
        back_left_motor.setPower(0);
    }

    // drive shooting motor for the given time in msec
    public void intakeDrive(double speed,
                            int timeoutS) {
        intake_motor.setPower(speed);
        // Display the current value
        telemetry.addData("Motor Power", "%5.2f", speed);
        telemetry.addData(">", "Press Stop to end test.");
        telemetry.update();
        sleep(timeoutS);

        // Set the motor to the new power and pause;
        intake_motor.setPower(0);
        telemetry.addData(">", "Done");
        telemetry.update();
    }


    // drive shooting motor for the given time in msec
    public void shootingDrive(double speed,
                              int timeoutS) {
        shooting_motor.setPower(speed);
        // Display the current value
        telemetry.addData("Motor Power", "%5.2f", speed);
        telemetry.addData(">", "Press Stop to end test.");
        telemetry.update();
        sleep(timeoutS);

        // Set the motor to the new power and pause;
        shooting_motor.setPower(0);
        //sleep(500);
        //resetShooter(0.7);
        telemetry.addData(">", "Done");
        telemetry.update();
    }

    public void encoderShoot(double speed) {
        shooting_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooting_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooting_motor.setTargetPosition((int) (shooting_motor.getCurrentPosition() + (1478 * getDriveGearReduction())));
        shooting_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        shooting_motor.setPower(1);
        while (shooting_motor.isBusy()) {
            telemetry.addData("", "Shooting...");
            telemetry.update();
        }
        telemetry.addData("", "Done Shooting");
        telemetry.update();
        shooting_motor.setPower(0);
    }

    public boolean isWhite() {
        colorSensor.enableLed(true);

        /* hsvValues is an array that will hold the hue, saturation, and value information */
        //float hsvValues[] = {0F,0F,0F};
        /* convert the RGB values to HSV values*/
        //Color.RGBToHSV(colorSensor.red() * 8, colorSensor.green() * 8, colorSensor.blue() * 8, hsvValues);

        if (colorSensor.red() == colorSensor.blue() && colorSensor.blue() == colorSensor.green()) {
            return true;
        } else {
            return false;
        }

    }

<<<<<<< HEAD:TeamCode/src/main/java/org/firstinspires/ftc/teamcode/AutonomousGeneral.java
    public void readColor() {

        // hsvValues is an array that will hold the hue, saturation, and value information.
        float hsvValues[] = {0F, 0F, 0F};

        // values is a reference to the hsvValues array.
        final float values[] = hsvValues;


        //convert the RGB values to HSV values
        Color.RGBToHSV(colorSensor.red() * 8, colorSensor.green() * 8, colorSensor.blue() * 8, hsvValues);

        int red = colorSensor.red();
        int blue = colorSensor.blue();
        int green = colorSensor.green();
//declares the colors that it sees by default, in a different name!

        colorSensor.enableLed(true);

        if (red > blue && red > green) {
            currentColor = "red";
        } else if (blue > red && blue > green) {
            currentColor = "blue";
        } else if (green > red && green > blue) {
            currentColor = "green";
        } else {
            currentColor = "other";
        }
        //checks which color the side currently is

        telemetry.addData("r value", colorSensor.red());
        telemetry.addData("g value", colorSensor.green());
        telemetry.addData("b value", colorSensor.blue());
        telemetry.addData("current beacon color", currentColor);
        telemetry.addData("Hue", hsvValues[0]);
        telemetry.addData("Saturation", hsvValues[1]);
        telemetry.addData("Value", hsvValues[2]);

        telemetry.update();
    }

=======
>>>>>>> dfbedde30115dd7e00d6e2695e8f485c459c3180:TeamCode/src/main/java/org/firstinspires/ftc/teamcode/Main/AutonomousGeneral.java
    public void gyro_leftTurn(int degrees, double speed){
        gyro.calibrate();
        while(gyro.isCalibrating()){

        }
        turnLeft(speed);
        while((gyro.getHeading() > degrees) ||(gyro.getHeading() < 10)){ //turn left until the angle becomes as small as you want it
            //gyro.getHeading() returns values from 0 to 359
            telemetry.addData("current gyro pos", gyro.getHeading());
            telemetry.update();
        }
        stopMotors();
    }

    public void gyro_rightTurn(int degrees, double speed){
        gyro.calibrate();
        while(gyro.isCalibrating()){

        }
        turnRight(speed);
        while((gyro.getHeading() < degrees) ||(gyro.getHeading() > 350)){ //turn left until the angle becomes as small as you want it
            //gyro.getHeading() returns values from 0 to 359
            telemetry.addData("current gyro pos", gyro.getHeading());
            telemetry.update();
        }
        stopMotors();
    }


<<<<<<< HEAD:TeamCode/src/main/java/org/firstinspires/ftc/teamcode/AutonomousGeneral.java
    public void newBeacon(String team, double wallTarget) {

        double wallDistRead = 0.0;
        int driveDegree = 0;
        double delta_power = 0;
        double straight_drive_power = -.15;
        int num_beacon_press = 0;
        wallDistRead = rangeSensor.getDistance(DistanceUnit.CM);
        telemetry.addData("wall Dist read", wallDistRead);
        telemetry.update();

        if(wallDistRead > wallTarget){
            gyro_leftTurn(350,0.05);

            while(wallDistRead > wallTarget){

                straightDrive(-0.2);
                wallDistRead = rangeSensor.getDistance(DistanceUnit.CM);
                telemetry.addData("wall Dist read", wallDistRead);
                telemetry.update();
            }

            gyro_rightTurn(10,0.05);
            telemetry.addData("wall Dist read", wallDistRead);
            telemetry.update();
        } else {

            gyro_rightTurn(10,0.05);

            while(wallDistRead < wallTarget){

                straightDrive(-0.2);
                wallDistRead = rangeSensor.getDistance(DistanceUnit.CM);
                telemetry.addData("wall Dist read", wallDistRead);
                telemetry.update();
            }

            gyro_leftTurn(350,0.05);
            telemetry.addData("wall Dist read", wallDistRead);
            telemetry.update();
        }

        readColor();

        gyro.calibrate();
        while(gyro.isCalibrating()){

        }
        while ((num_beacon_press <3) /*||(range sensor front < 20 cm) */) {
            driveDegree = gyro.getHeading();
            telemetry.addData("current gyro pos", gyro.getHeading());
            telemetry.update();

            if (driveDegree <180)
            {
                delta_power = .1*straight_drive_power;
            }
            else
            {
                delta_power = -.1*straight_drive_power;
            }


            back_left_motor.setPower(straight_drive_power +delta_power);
            front_left_motor.setPower(straight_drive_power+delta_power);
            back_right_motor.setPower(straight_drive_power-delta_power);
            front_right_motor.setPower(straight_drive_power-delta_power);
          //  straightDrive(-0.25);
            readColor();


        if (currentColor.equals(team)) {
           // encoderDrive(0.4, 3, 3, 5);
            sleep(750);
            beaconPress.setPosition(0.2);
            sleep(3000);
            beaconPress.setPosition(0.7);
            num_beacon_press++;
        }
        }
        readColor();

        telemetry.addData("pressed!", currentColor);
        telemetry.update();
    }
=======
>>>>>>> dfbedde30115dd7e00d6e2695e8f485c459c3180:TeamCode/src/main/java/org/firstinspires/ftc/teamcode/Main/AutonomousGeneral.java
}
