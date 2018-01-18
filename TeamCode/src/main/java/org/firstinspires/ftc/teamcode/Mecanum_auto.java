

package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import android.app.Activity;
import android.graphics.Color;
import android.view.View;


import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.Locale;


import static java.lang.Math.abs;

/**
 * This file illustrates the concept of driving a path based on encoder counts.
 * It uses the common Pushbot hardware class to define the drive on the gromit.
 * The code is structured as a LinearOpMode
 *
 * The code REQUIRES that you DO have encoders on the wheels,
 *   otherwise you would use: PushbotAutoDriveByTime;
 *
 *  This code ALSO requires that the drive Motors have been configured such that a positive
 *  power command moves them forwards, and causes the encoders to count UP.
 *
 *   The desired path in this example is:
 *   - Drive forward for 48 inches
 *   - Spin right for 12 Inches
 *   - Drive Backwards for 24 inches
 *   - Stop and close the claw.
 *
 *  The code is written using a method called: encoderDrive(speed, leftInches, rightInches, timeoutS)
 *  that performs the actual movement.
 *  This methods assumes that each movement is relative to the last stopping place.
 *  There are other ways to perform encoder based moves, but this method is probably the simplest.
 *  This code uses the RUN_TO_POSITION mode to enable the Motor controllers to generate the run profile
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="Nathan", group="Pushbot")
@Disabled

public class Mecanum_auto extends LinearOpMode {

    /* Declare OpMode members. */
    HardwareGromit gromit      = new HardwareGromit(); // use the class for variables and setup
    private ElapsedTime     runtime = new ElapsedTime();

//    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
//    static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
//    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
//    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
//                                                      (WHEEL_DIAMETER_INCHES * 3.1415);
//    static final double     DRIVE_SPEED             = 0.6;
//    static final double     TURN_SPEED              = 0.5;
// The IMU sensor object
    BNO055IMU imu;
    ColorSensor sensorColor;
    DistanceSensor sensorDistance;

    // State used for updating telemetry
    Orientation angles;
    Acceleration gravity;

    @Override
    public void runOpMode() {

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        gromit.init(hardwareMap);
        // Set up the parameters with which we will use our IMU. Note that integration
        // algorithm here just reports accelerations to the logcat log; it doesn't actually
        // provide positional information.
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        // Set up our telemetry dashboard
        composeTelemetry();

        // get a reference to the color sensor.
        sensorColor = hardwareMap.get(ColorSensor.class, "sensor_color_distance");

        // get a reference to the distance sensor that shares the same name.
        sensorDistance = hardwareMap.get(DistanceSensor.class, "sensor_color_distance");

        // hsvValues is an array that will hold the hue, saturation, and value information.
        float hsvValues[] = {0F, 0F, 0F};

        // values is a reference to the hsvValues array.
        final float values[] = hsvValues;

        // sometimes it helps to multiply the raw RGB values with a scale factor
        // to amplify/attentuate the measured values.
        final double SCALE_FACTOR = 255;

        // get a reference to the RelativeLayout so we can change the background
        // color of the Robot Controller app to match the hue detected by the RGB sensor.
        int relativeLayoutId = hardwareMap.appContext.getResources().getIdentifier("RelativeLayout", "id", hardwareMap.appContext.getPackageName());
        final View relativeLayout = ((Activity) hardwareMap.appContext).findViewById(relativeLayoutId);

        // Wait until we're told to go
        //waitForStart();

        // Start the logging of measured acceleration
        imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);


        // Send telemetry message to signify robot waiting;
//        telemetry.addData("Status", "Resetting Encoders");    //
//        telemetry.update();

//        gromit.left_front.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        gromit.left_back.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        gromit.right_front.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        gromit.right_back.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
//        telemetry.addData("Path0",  "Starting at %7d :%7d",
//                          gromit.leftDrive.getCurrentPosition(),
//                          gromit.rightDrive.getCurrentPosition());
//        telemetry.update();

        gromit.jewelsservo.setPosition(0.2);

        /** Wait for the game to start (driver presses PLAY)**/
        waitForStart();
            double error=.3;
            double power=0.5;
        resetStartTime();      // start timer




            gromit.jewelsservo.setPosition(0.85);
        sleep(500);

        // pretend we're red for now...
        if (sensorColor.red() < sensorColor.blue()) {
            gromit.left_front.setPower(power);
            gromit.left_back.setPower(power);
            gromit.right_front.setPower( - power);
            gromit.right_back.setPower( - power);

            sleep(70);
            gromit.left_front.setPower(-power);
            gromit.left_back.setPower (-power);
            gromit.right_front.setPower(power);
            gromit.right_back.setPower (power);
            gromit.jewelsservo.setPosition(0.2);
            sleep(70);

            gromit.left_front.setPower(0);
            gromit.left_back.setPower(0);
            gromit.right_front.setPower(0);
            gromit.right_back.setPower(0);
        }
        else {
            gromit.left_front.setPower(-power);
            gromit.left_back.setPower(-power);
            gromit.right_front.setPower( power);
            gromit.right_back.setPower( power);

            sleep(70);
            gromit.left_front.setPower(power);
            gromit.left_back.setPower(power);
            gromit.right_front.setPower(-power);
            gromit.right_back.setPower(-power);
            gromit.jewelsservo.setPosition(0.2);
            sleep(70);

            gromit.left_front.setPower(0);
            gromit.left_back.setPower(0);
            gromit.right_front.setPower(0);
            gromit.right_back.setPower(0);
        }

       // mecanumDriveGyro(1.0,0.0,0.0,0);
       while (opModeIsActive()) {
           Color.RGBToHSV((int) (sensorColor.red() * SCALE_FACTOR),
                   (int) (sensorColor.green() * SCALE_FACTOR),
                   (int) (sensorColor.blue() * SCALE_FACTOR),
                   hsvValues);

           // send the info back to driver station using telemetry function.
           telemetry.addData("Distance (cm)",
                   String.format(Locale.US, "%.02f", sensorDistance.getDistance(DistanceUnit.CM)));
           telemetry.addData("Alpha", sensorColor.alpha());
           telemetry.addData("Red  ", sensorColor.red());
           telemetry.addData("Green", sensorColor.green());
           telemetry.addData("Blue ", sensorColor.blue());
           telemetry.addData("Hue", hsvValues[0]);
//            telemetry.update();
//            error=(angles.firstAngle-0)/90;
//            gromit.left_front.setPower(power + error/1.0);
//            gromit.left_back.setPower(power + error/1.0);
//            gromit.right_front.setPower(power - error/1.0);
//            gromit.right_back.setPower(power - error/1.0);
//
//
//
           telemetry.update();
       }

//        telemetry.addData("Path", "Complete");
//        telemetry.update();
//
//        mecanumDrive(1,25,34);
//        sleep(2000);
//        stop_drivetrain();
//        mecanumDrive(-1,45,34);
//        sleep(2000);
//        stop_drivetrain();


    }

    /*
     *  Method to perfmorm a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */
    public void mecanumDrive(double speed, double direction,double distance){
        if(abs(speed)> 1) speed = speed/abs(speed);


        double max;
        double multiplier;
        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            double lfpower = Math.cos(Math.toRadians(direction+45));
            double lrpower = Math.sin(Math.toRadians(direction+45));
            double rfpower = Math.sin(Math.toRadians(direction+45));
            double rrpower = Math.cos(Math.toRadians(direction+45));

            //Determine largest power being applied
            max = abs(lfpower);
            if (abs(lrpower) > max) max = abs(lrpower);
            if (abs(rfpower) > max) max = abs(rfpower);
            if (abs(rrpower) > max) max = abs(rrpower);

            multiplier = speed/max; //multiplier to adjust speeds of each wheel so you can have a max power of 1 on atleast 1 wheel

            lfpower = multiplier*lfpower;
            lrpower = multiplier*lrpower;
            rfpower = multiplier*rfpower;
            rrpower = multiplier*rrpower;

            gromit.left_front.setPower(lfpower);
            gromit.left_back.setPower(lrpower);
            gromit.right_front.setPower(rfpower);
            gromit.right_back.setPower(rrpower);

            //  sleep(250);   // optional pause after each move
        }
    }
     /**
     *  Method to Move in a Given direction at a certain Orientation or heading
     *
     */
    public void mecanumDriveGyro(double speed,double direction, double orientation, double distance)
    {
        imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);
        if(abs(speed)> 1) speed = speed/abs(speed);                                                 //Correct Speed if outside of Range

        /**
        * VARIABLES FOR THIS METHOD
        */
        double max;
        double multiplier;
        double turn_correction;
        double correction_speed ;
        double lfpower;
        double lrpower;
        double rfpower;
        double rrpower;

        //Figure out better way to determine which way to turn


        /**
        * Drive Loop of this method
        */
        while(opModeIsActive()) {                                                                     // Ensure that the opmode is still active
            telemetry.update();
            double heading = angles.firstAngle;
            turn_correction = orientation - heading;

            if (turn_correction <= -180) turn_correction +=360;   // correction should be +/- 180 (to the left negative, right positive)
            if (turn_correction >=  180) turn_correction -=360;

            correction_speed = Math.signum(turn_correction) * gromit.drive_COEF * turn_correction / 180;





            lfpower = Math.sin(Math.toRadians(direction+45)) + correction_speed;
            lrpower = Math.cos(Math.toRadians(direction+45)) + correction_speed;
            rfpower = Math.cos(Math.toRadians(direction+45)) - correction_speed;
            rrpower = Math.sin(Math.toRadians(direction+45)) - correction_speed;

            //Determine largest power being applied
            max = lfpower;
            if (lrpower > max) max = lrpower;
            if (rfpower > max) max = rfpower;
            if (rrpower > max) max = rrpower;

            multiplier = speed/max;                                                                 //multiplier to adjust speeds of each wheel so you can have a max power of 1 on atleast 1 wheel

            lfpower = multiplier*lfpower;
            lrpower = multiplier*lrpower;
            rfpower = multiplier*rfpower;
            rrpower = multiplier*rrpower;

            gromit.left_front.setPower(lfpower);
            gromit.left_back.setPower(lrpower);
            gromit.right_front.setPower(rfpower);
            gromit.right_back.setPower(rrpower);

            //  sleep(250);   // optional pause after each move
        }
    }
    public void mecanumTurn( double speed,
                             double target_heading,
                            String type) {
        if(speed > 1) speed = 1.0;
        else if(speed <= 0) speed = 0.1;
        /**READ IMU

         */
        double correction = target_heading /**- IMU reading*/;
        if (correction <= -180) correction +=360;   // correction should be +/- 180 (to the left negative, right positive)
        if (correction >=  180) correction -=360;

        //
            while(abs(correction) >= gromit.turn_THRESHOLD &&  opModeIsActive()) {
                /**READ IMU*/

                correction = target_heading; /** - CURRENT HEADING*/
                if(abs(correction) >= gromit.turn_THRESHOLD) break;

                if (correction <= -180) correction +=360;   // correction should be +/- 180 (to the left negative, right positive)
                if (correction >=  180) correction -=360;
                /**^^^^^^^^^^^MAYBE WE ONLY NEED TO DO THIS ONCE?????*/

                double adjustment = Range.clip((Math.signum(correction) * gromit.turn_MIN_SPEED + gromit.turn_COEF * correction / 180), -1, 1);  // adjustment is motor power: sign of correction *0.07 (base power)  + a proportional bit

                gromit.left_front.setPower(adjustment * speed);
                gromit.left_back.setPower(adjustment * speed);
                gromit.right_front.setPower(-(adjustment * speed));
                gromit.right_back.setPower(-(adjustment * speed));
            }
            stop_drivetrain();
    }

    /**
    * STOP ALL DRIVETRAIN MOTORS
    */
    public void stop_drivetrain(){
        gromit.left_front.setPower(0.0);
        gromit.left_back.setPower(0.0);
        gromit.right_front.setPower(0.0);
        gromit.right_back.setPower(0.0);
    }


    //----------------------------------------------------------------------------------------------
    // Telemetry Configuration
    //----------------------------------------------------------------------------------------------

    void composeTelemetry() {

        // At the beginning of each telemetry update, grab a bunch of data
        // from the IMU that we will then display in separate lines.
        telemetry.addAction(new Runnable() { @Override public void run()
        {
            // Acquiring the angles is relatively expensive; we don't want
            // to do that in each of the three items that need that info, as that's
            // three times the necessary expense.
            angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            gravity  = imu.getGravity();
        }
        });

        telemetry.addLine()
                .addData("status", new Func<String>() {
                    @Override public String value() {
                        return imu.getSystemStatus().toShortString();
                    }
                })
                .addData("calib", new Func<String>() {
                    @Override public String value() {
                        return imu.getCalibrationStatus().toString();
                    }
                });

        telemetry.addLine()
                .addData("heading", new Func<String>() {
                    @Override public String value() {
                        return formatAngle(angles.angleUnit, angles.firstAngle);
                    }
                })
                .addData("roll", new Func<String>() {
                    @Override public String value() {
                        return formatAngle(angles.angleUnit, angles.secondAngle);
                    }
                })
                .addData("pitch", new Func<String>() {
                    @Override public String value() {
                        return formatAngle(angles.angleUnit, angles.thirdAngle);
                    }
                });

        telemetry.addLine()
                .addData("grvty", new Func<String>() {
                    @Override public String value() {
                        return gravity.toString();
                    }
                })
                .addData("mag", new Func<String>() {
                    @Override public String value() {
                        return String.format(Locale.getDefault(), "%.3f",
                                Math.sqrt(gravity.xAccel*gravity.xAccel
                                        + gravity.yAccel*gravity.yAccel
                                        + gravity.zAccel*gravity.zAccel));
                    }
                });
    }

    //----------------------------------------------------------------------------------------------
    // Formatting
    //----------------------------------------------------------------------------------------------

    String formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }

    String formatDegrees(double degrees){
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }
}
