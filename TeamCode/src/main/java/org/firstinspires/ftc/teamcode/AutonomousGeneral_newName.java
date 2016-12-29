package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;

/**
 * Created by inspirationteam on 12/18/2016.
 */
@Disabled
public class AutonomousGeneral_newName extends LinearOpMode{
    private static double     COUNTS_PER_MOTOR_REV;    // eg: TETRIX Motor Encoder
    private static double     DRIVE_GEAR_REDUCTION;     // 56/24
    private static double     WHEEL_PERIMETER_CM;     // For figuring circumference
    private  static double     COUNTS_PER_CM;
    protected DcMotor rightWheelMotorFront;
    protected DcMotor rightWheelMotorBack;
    protected DcMotor leftWheelMotorFront;
    protected DcMotor leftWheelMotorBack;
    protected GyroSensor gyro;                      //turning clockwise = +degrees, turning counterclockwise = -degrees
    protected static final double     DRIVE_SPEED             = 1.0;
    protected static final double     TURN_SPEED              = 0.5;
    // motor definition to shoot the small ball
     protected DcMotor ballShooterMotor;

    // motor definition to intake the small ball
    protected DcMotor ballCollectorMotor;
    protected static ElapsedTime runtime = new ElapsedTime();
    protected ColorSensor colorSensor;
    String currentColor = "blank";

    protected Servo beaconPress;
    protected ModernRoboticsI2cRangeSensor frontUltra;
    protected ModernRoboticsI2cRangeSensor rearUltra;


    public void initiate(){
        COUNTS_PER_MOTOR_REV = 1440;
        DRIVE_GEAR_REDUCTION = 2.333;
        WHEEL_PERIMETER_CM = 29;
        COUNTS_PER_CM         = (COUNTS_PER_MOTOR_REV ) /
                (WHEEL_PERIMETER_CM*DRIVE_GEAR_REDUCTION);
        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        //robot.init(hardwareMap);
        leftWheelMotorFront = hardwareMap.dcMotor.get("leftWheelMotorFront");
        rightWheelMotorFront = hardwareMap.dcMotor.get("rightWheelMotorFront");
        leftWheelMotorBack = hardwareMap.dcMotor.get("leftWheelMotorBack");
        rightWheelMotorBack = hardwareMap.dcMotor.get("rightWheelMotorBack");

        // Connect to motor (Assume standard left wheel)
        // Change the text in quotes to match any motor name on your robot.
        ballShooterMotor = hardwareMap.dcMotor.get("ballShooterMotor");
        ballCollectorMotor = hardwareMap.dcMotor.get("ballCollectorMotor");

        //Initiate sensors:
        gyro = hardwareMap.gyroSensor.get("gyro");

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        leftWheelMotorFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightWheelMotorFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftWheelMotorBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightWheelMotorBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        rightWheelMotorFront.setDirection(DcMotor.Direction.REVERSE);
        rightWheelMotorBack.setDirection(DcMotor.Direction.REVERSE);
        idle();

        leftWheelMotorFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightWheelMotorFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftWheelMotorBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightWheelMotorBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0",  "Starting at %7d :%7d : %7d :%7d",
                leftWheelMotorBack.getCurrentPosition(),
                rightWheelMotorBack.getCurrentPosition(),
                leftWheelMotorFront.getCurrentPosition(),
                rightWheelMotorFront.getCurrentPosition());
        telemetry.update();


        //calibrate gyro
        gyro.calibrate();
        while(gyro.isCalibrating()){

        }

        colorSensor = hardwareMap.colorSensor.get("colorSensor");

        beaconPress = hardwareMap.servo.get("beaconPress");
        beaconPress.setPosition(0.5);

        rearUltra = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "rearUltra");
        frontUltra = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "frontUltra");
    }


    @Override
    public void runOpMode(){

    }

    public static double getCountsPerMotorRev() {;
       return COUNTS_PER_MOTOR_REV;
    }

    public static double getDriveGearReduction(){
        return DRIVE_GEAR_REDUCTION;
    }

    public static double getWheelPerimeterCm(){
        return WHEEL_PERIMETER_CM;
    }

    public static double getCountsPerCm(){
        return COUNTS_PER_CM;
    }

    /**
 *  Method to perfmorm a relative move, based on encoder counts.
 *  Encoders are not reset as the move is based on the current position.
 *  Move will stop if any of three conditions occur:
 *  1) Move gets to the desired position
 *  2) Move runs out of time
 *  3) Driver stops the opmode running.
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
            newLeftTarget = leftWheelMotorFront.getCurrentPosition() + (int)(leftInches * getCountsPerCm());
            newRightTarget = rightWheelMotorBack.getCurrentPosition() + (int)(rightInches * getCountsPerCm());
            leftWheelMotorBack.setTargetPosition(newLeftTarget);
            rightWheelMotorBack.setTargetPosition(newRightTarget);
            leftWheelMotorFront.setTargetPosition(newLeftTarget);
            rightWheelMotorFront.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            leftWheelMotorBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightWheelMotorBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftWheelMotorFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightWheelMotorFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            if (Math.abs(leftInches) > Math.abs(rightInches))
            {
                leftSpeed = speed;
                rightSpeed = (speed*rightInches)/leftInches;
            }
            else
            {
                rightSpeed = speed;
                leftSpeed = (speed*leftInches)/rightInches;
            }
            runtime.reset();
            leftWheelMotorBack.setPower(Math.abs(leftSpeed));
            rightWheelMotorBack.setPower(Math.abs(rightSpeed));
            leftWheelMotorFront.setPower(Math.abs(leftSpeed));
            rightWheelMotorFront.setPower(Math.abs(rightSpeed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (leftWheelMotorBack.isBusy() && rightWheelMotorBack.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                        leftWheelMotorBack.getCurrentPosition(),
                        rightWheelMotorBack.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            leftWheelMotorBack.setPower(0);
            rightWheelMotorBack.setPower(0);
            leftWheelMotorFront.setPower(0);
            rightWheelMotorFront.setPower(0);


            // Turn off RUN_TO_POSITION
            leftWheelMotorBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightWheelMotorBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftWheelMotorFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightWheelMotorFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        }

    }

    public void turnLeft(double speed){
        leftWheelMotorFront.setPower(-speed);
        leftWheelMotorBack.setPower(-speed);

        rightWheelMotorFront.setPower(speed);
        rightWheelMotorBack.setPower(speed);
    }

    public void turnRight(double speed){
        rightWheelMotorFront.setPower(-speed);
        rightWheelMotorBack.setPower(-speed);

        leftWheelMotorFront.setPower(speed);
        leftWheelMotorBack.setPower(speed);
    }
    public void stopMotors(){

        rightWheelMotorFront.setPower(0);
        leftWheelMotorFront.setPower(0);
        rightWheelMotorBack.setPower(0);
        leftWheelMotorBack.setPower(0);
    }

    // drive shooting motor for the given time in msec
    public void intakeDrive(double speed,
                            int timeoutS)
    {
        ballCollectorMotor.setPower(speed);
        // Display the current value
        telemetry.addData("Motor Power", "%5.2f", speed);
        telemetry.addData(">", "Press Stop to end test.");
        telemetry.update();
        sleep(timeoutS);

        // Set the motor to the new power and pause;
        ballCollectorMotor.setPower(0);
        telemetry.addData(">", "Done");
        telemetry.update();
    }


    // drive shooting motor for the given time in msec
    public void shootingDrive(double speed,
                              int timeoutS)
    {
        ballShooterMotor.setPower(speed);
        // Display the current value
        telemetry.addData("Motor Power", "%5.2f", speed);
        telemetry.addData(">", "Press Stop to end test.");
        telemetry.update();
        sleep(timeoutS);

        // Set the motor to the new power and pause;
        ballShooterMotor.setPower(0);
        //sleep(500);
        //resetShooter(0.7);
        telemetry.addData(">", "Done");
        telemetry.update();
    }

    public void encoderShoot(double speed){
        ballShooterMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        ballShooterMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        ballShooterMotor.setTargetPosition((int)(ballShooterMotor.getCurrentPosition() + (1478* getDriveGearReduction())));
        ballShooterMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        ballShooterMotor.setPower(1);
        while (ballShooterMotor.isBusy()){
            telemetry.addData("", "Shooting...");
            telemetry.update();
        }
        telemetry.addData("", "Done Shooting");
        telemetry.update();
        ballShooterMotor.setPower(0);
    }

    public void straightDrive(double power){
        leftWheelMotorBack.setPower(power);
        leftWheelMotorFront.setPower(power);
        rightWheelMotorBack.setPower(power);
        rightWheelMotorFront.setPower(power);
    }

    public void newBeacon(String team){


        readColor();

        while(!currentColor.equals(team)) {
            straightDrive(0.2);
            readColor();
        }

        if(currentColor.equals(team)) {
            //encoderDrive(0.4, -15, -15, 10);
            sleep(2000);
            beaconPress.setPosition(0.2);
            sleep(1000);
            beaconPress.setPosition(0.7);
            sleep(1000);
        }

        readColor();

        telemetry.addData("pressed!", currentColor);
        telemetry.update();

        if(currentColor.equals(team)) {
            encoderDrive(0.5, 30, 30, 10);
        }
    }

    public void readColor() {

        // hsvValues is an array that will hold the hue, saturation, and value information.
        float hsvValues[] = {0F,0F,0F};

        // values is a reference to the hsvValues array.
        final float values[] = hsvValues;


        //convert the RGB values to HSV values
        Color.RGBToHSV(colorSensor.red() * 8, colorSensor.green() * 8, colorSensor.blue() * 8, hsvValues);

        int red = colorSensor.red();
        int blue = colorSensor.blue();
        int green = colorSensor.green();
//declares the colors that it sees by default, in a different name!

        colorSensor.enableLed(true);

        if(red > blue && red > green) {
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
        telemetry.addData("current beacon color",currentColor);
        telemetry.addData("Hue", hsvValues[0]);
        telemetry.addData("Saturation", hsvValues[1]);
        telemetry.addData("Value", hsvValues[2]);

        telemetry.update();
    }

    public void wallDrive(double distFromWall) {
        turnLeft();
    }
}
