package org.firstinspires.ftc.teamcode.Qualifier;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import java.util.Locale;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.atan;
import static java.lang.Math.signum;


public class DriveTrain {


    public enum SpeedSetting {FAST, MID, SLOW}
    private SpeedSetting speedMode;

    //public enum DirectionSetting {FRONT, BACK}
    //public  DirectionSetting forwardDirection;
    public boolean frontIsForward = true;

    public final double WHEEL_DIAMETER = 4.0;
    public final double GEAR_RATIO = 32/24;
    public final double TICKS_REV = 537.6;
    public final double COUNTS_PER_INCH = (TICKS_REV * GEAR_RATIO) / (WHEEL_DIAMETER * 3.1415);
    public static final double turn_THRESHOLD = 2.0;
    public static final double drive_THRESHOLD = 1.0;
    public static final double turn_MIN_SPEED = 0.15;
    public static final double turn_COEF = 1.0;
    public static final double drive_COEF = 1.0; //Maximum additional speed to add to a motor during a gyro drive


    //Iniatalize motors
    public DcMotor left_front = null;
    public DcMotor right_front = null;
    public DcMotor right_rear = null;
    public DcMotor left_rear = null;

//    public AnalogInput maxbotixSensor;

    //MOVE TO GLYPHTRAIN
    public AnalogInput sharpIRSensor;
    public AnalogInput leftSharpSensor;
    public AnalogInput rightSharpSensor;
    public AnalogInput BackMaxbotixSensor;
    public AnalogInput SideMaxbotixSensor;

    //?????????//Middle sensor Color hopefully
   // public DistanceSensor sensorDistance;


    // The IMU sensor object
    BNO055IMU imu;

    // State used for updating telemetry
    Orientation angles;
    Acceleration gravity;


    public void init(HardwareMap hardwareMap) {
        left_front = hardwareMap.get(DcMotor.class, "left_front");
        right_front = hardwareMap.get(DcMotor.class, "right_front");
        right_rear = hardwareMap.get(DcMotor.class, "right_rear");
        left_rear = hardwareMap.get(DcMotor.class, "left_rear");


        /**
         * IMU SETUP
         */
        // Set up the parameters with which we will use our IMU. Note that integration
        // algorithm here just reports accelerations to the logcat log; it doesn't actually
        // provide positional information.
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // Retrieve and initialize the IMU. port 0 on rev hub
        // and named "imu".
        imu = hardwareMap.get(BNO055IMU.class, "imu");
       imu.initialize(parameters);

//      Neverest Motors
        left_front.setDirection(DcMotor.Direction.FORWARD);
        left_rear.setDirection(DcMotor.Direction.FORWARD);
        right_front.setDirection(DcMotor.Direction.REVERSE);
        right_rear.setDirection(DcMotor.Direction.REVERSE);
//      Tetrix Motors
//        left_front.setDirection(DcMotor.Direction.REVERSE);
//        left_rear.setDirection(DcMotor.Direction.REVERSE);
//        right_front.setDirection(DcMotor.Direction.FORWARD);
//        right_rear.setDirection(DcMotor.Direction.FORWARD);

        // Set all motors to zero power
        stopMotors();

        runWithoutEncoders();
        //runUsingEncoders();

        left_front.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        left_rear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        right_front.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        right_rear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        speedMode = SpeedSetting.FAST;
//        forwardDirection = DirectionSetting.FRONT;

        //Maxbotix Sensor
        BackMaxbotixSensor = hardwareMap.analogInput.get("maxbotixsensorback");
        SideMaxbotixSensor = hardwareMap.analogInput.get("maxbotixsensorside");
        //Front IR Sensor
        leftSharpSensor = hardwareMap.analogInput.get("leftsharpsensor");
        rightSharpSensor = hardwareMap.analogInput.get("rightsharpsensor");
        //Sharp IR Sensors
        sharpIRSensor = hardwareMap.analogInput.get("sharpirsensor");
        //sensorDistance = hardwareMap.get(DistanceSensor.class, "sensor_color");

    }

    public void runWithoutEncoders() {
        left_front.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        right_front.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        left_rear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        right_rear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void runUsingEncoders() {
        left_front.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right_front.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        left_rear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right_rear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void drive(double x, double y, double turn) {
        //speed change code
        //   double drive_direction = atan(y/x);
        double speedMultiplier;
        switch (speedMode) {
            // lookup parameter for "fast mode"
            case FAST:
                speedMultiplier = 1.0;
                break;
            case MID:
                speedMultiplier = 0.5;
                break;
            case SLOW:
                // lookup slow speed parameter
                speedMultiplier = 0.25;
                break;
           default:
                speedMultiplier = 0.5;
        }

        // Forward or Reverse Drive (note: The joystick goes negative when pushed forwards, so negate it)
        double forward;
        double strafe;
        double rotate;

        if (frontIsForward) {             // driving with the front facing forward
            forward = Math.pow(y, 3);
            strafe = Math.pow(-x, 3);
            rotate = turn;
        } else {                            // driving with the rear facing forward
            forward = -Math.pow(y, 3);
            strafe = -Math.pow(-x, 3);
            rotate = turn;
        }

        double lfpower;
        double lrpower;
        double rfpower;
        double rrpower;

// temp
        lfpower =  ( forward/1.0 - strafe/1.0 + rotate/1.0 );
        lrpower =  ( forward/1.0 + strafe/1.0 + rotate/1.0  );
        rfpower = ( forward/1.0 + strafe/1.0 - rotate/1.0 );
        rrpower = ( forward/1.0 - strafe/1.0 - rotate/1.0 );
//        lfpower = signum(y)*Math.cos(Math.toRadians(drive_direction + 45));
//        lrpower = signum(y)*Math.sin(Math.toRadians(drive_direction + 45));
//        rfpower = signum(y)*Math.sin(Math.toRadians(drive_direction + 45));
//        rrpower = signum(y)*Math.cos(Math.toRadians(drive_direction + 45));
//



        //Determine largest power being applied in either direction
            double max = abs(lfpower);
            if (abs(lrpower) > max) max = abs(lrpower);
            if (abs(rfpower) > max) max = abs(rfpower);
            if (abs(rrpower) > max) max = abs(rrpower);

//            double multiplier = speedMultiplier / max; //multiplier to adjust speeds of each wheel so you can have a max power of 1 on atleast 1 wheel
        double multiplier = speedMultiplier;
        
            lfpower *= multiplier;
            lrpower *= multiplier;
            rfpower *= multiplier;
            rrpower *= multiplier;

            left_front.setPower(lfpower);
            left_rear.setPower(lrpower);
            right_front.setPower(rfpower);
            right_rear.setPower(rrpower);
    }

    public void drivesmart(double x, double y, double turn) {
        //speed change code
        //   double drive_direction = atan(y/x);
        double speedMultiplier;
        switch (speedMode) {
            // lookup parameter for "fast mode"
            case FAST:
                speedMultiplier = 1.0;
                break;
            case MID:
                speedMultiplier = 0.5;
                break;
            case SLOW:
                // lookup slow speed parameter
                speedMultiplier = 0.25;
                break;
            default:
                speedMultiplier = 0.5;
        }

        // Forward or Reverse Drive (note: The joystick goes negative when pushed forwards, so negate it)
//        double forward;
//        double strafe;
//        double rotate;
//
//        if (frontIsForward) {             // driving with the front facing forward
//            forward = Math.pow(y, 3);
//            strafe = Math.pow(-x, 3);
//            rotate = turn;
//        } else {                            // driving with the rear facing forward
//            forward = -Math.pow(y, 3);
//            strafe = -Math.pow(-x, 3);
//            rotate = turn;
//        }

        double lfpower;
        double lrpower;
        double rfpower;
        double rrpower;

        double rotation = turn*0.75;
// temp

        if (frontIsForward) {             // driving with the front facing forward

        } else {                            // driving with the rear facing forward
            y = -y;
            x = -x;
        }


//        lfpower = signum(y)*Math.cos(Math.toRadians(drive_direction + 45));
//        lrpower = signum(y)*Math.sin(Math.toRadians(drive_direction + 45));
//        rfpower = signum(y)*Math.sin(Math.toRadians(drive_direction + 45));
//        rrpower = signum(y)*Math.cos(Math.toRadians(drive_direction + 45));
//

        //Determine largest power being applied in either direction

        lfpower = ( y - x + rotation);
        lrpower = ( y + x + rotation);
        rfpower = ( y + x - rotation);
        rrpower = ( y - x - rotation);

//        //Determine largest power being applied in either direction
        double max = abs(lfpower);
        if (abs(lrpower) > max) max = abs(lrpower);
        if (abs(rfpower) > max) max = abs(rfpower);
        if (abs(rrpower) > max) max = abs(rrpower);

//            double multiplier = speedMultiplier / max; //multiplier to adjust speeds of each wheel so you can have a max power of 1 on atleast 1 wheel
        double multiplier = (speedMultiplier / max) + 0.2*Math.abs(x);  // try to boost up the strafing power

//        double multiplier = speedMultiplier / max;

        lfpower *= multiplier;
        lrpower *= multiplier;
        rfpower *= multiplier;
        rrpower *= multiplier;

        left_front.setPower(lfpower);
        left_rear.setPower(lrpower);
        right_front.setPower(rfpower);
        right_rear.setPower(rrpower);
    }


    public void drivevector(double x, double y, double turn) {
        //speed change code
        //   double drive_direction = atan(y/x);
        double speedMultiplier;
        switch (speedMode) {
            // lookup parameter for "fast mode"
            case FAST:
                speedMultiplier = 1.0;
                break;
            case MID:
                speedMultiplier = 0.5;
                break;
            case SLOW:
                // lookup slow speed parameter
                speedMultiplier = 0.25;
                break;
            default:
                speedMultiplier = 0.5;
        }
        double lfpower;
        double lrpower;
        double rfpower;
        double rrpower;

        double rotation = turn*0.75;

        if (frontIsForward) {             // driving with the front facing forward
            y = Math.pow(y, 3);
            x = Math.pow(-x, 3);
        } else {                            // driving with the rear facing forward
            y = -Math.pow(y, 3);
            x = -Math.pow(-x, 3);
        }

        double drive_direction = atan(y/x);
        lfpower = signum(y)*Math.cos(drive_direction-PI/4);
        lrpower = signum(y)*Math.sin(drive_direction-PI/4);
        rfpower = signum(y)*Math.sin(drive_direction-PI/4);
        rrpower = signum(y)*Math.cos(drive_direction-PI/4);

//        lfbase = signum(distance)*Math.cos(Math.toRadians(drive_direction + 45));
//        lrbase = signum(distance)*Math.sin(Math.toRadians(drive_direction + 45));
//        rfbase = signum(distance)*Math.sin(Math.toRadians(drive_direction + 45));
//        rrbase = signum(distance)*Math.cos(Math.toRadians(drive_direction + 45));


        //Determine largest power being applied in either direction
        double max = abs(lfpower);
        if (abs(lrpower) > max) max = abs(lrpower);
        if (abs(rfpower) > max) max = abs(rfpower);
        if (abs(rrpower) > max) max = abs(rrpower);

//            double multiplier = speedMultiplier / max; //multiplier to adjust speeds of each wheel so you can have a max power of 1 on atleast 1 wheel
        double multiplier = speedMultiplier;

        lfpower *= multiplier;
        lrpower *= multiplier;
        rfpower *= multiplier;
        rrpower *= multiplier;

        left_front.setPower(lfpower);
        left_rear.setPower(lrpower);
        right_front.setPower(rfpower);
        right_rear.setPower(rrpower);
    }

    public void stopMotors() {
        left_front.setPower(0.0);
        right_front.setPower(0.0);
        right_rear.setPower(0.0);
        left_rear.setPower(0.0);
    }

    //changes the speed of the robot


    public void setSpeedMode(SpeedSetting speed) {
        speedMode = speed;
    }

    public float getheading() {
        // Acquiring the angles is relatively expensive; we don't want
        // to do that in each of the three items that need that info, as that's
        // three times the necessary expense.
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        return angles.firstAngle; //For a -180 to 180 range
        //return (angles.firstAngle + 180 + 180)%360; // for a zero to 360 range
    }
    //----------------------------------------------------------------------------------------------
    // Formatting
    //----------------------------------------------------------------------------------------------

    String formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }

    String formatDegrees(double degrees) {
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }


    public void mecanumTurn(double speed, double target_heading) {
        if (speed > 1) speed = 1.0;
        //else if(speed <= 0) speed = 0.1;

        double correction = target_heading - getheading();
        if (correction <= -180) {
            correction += 360;   // correction should be +/- 180 (to the left negative, right positive)
        } else if (correction >= 180){
            correction -= 360;
        }

        while (abs(correction) >= turn_THRESHOLD) { //opmode active?{
            correction = target_heading - getheading();
            if (abs(correction) <= turn_THRESHOLD) break;

            if (correction <= -180)
                correction += 360;   // correction should be +/- 180 (to the left negative, right positive)
            if (correction >= 180) correction -= 360;
            /**^^^^^^^^^^^MAYBE WE ONLY NEED TO DO THIS ONCE?????*/

            double adjustment = Range.clip((Math.signum(correction) * turn_MIN_SPEED + turn_COEF * correction / 180), -1, 1);  // adjustment is motor power: sign of correction *0.07 (base power)  + a proportional bit

            left_front.setPower(-adjustment * speed);
            left_rear.setPower(-adjustment * speed);
            right_front.setPower((adjustment * speed));
            right_rear.setPower((adjustment * speed));
        }
        stopMotors();
    }


    public void mecanumDrive(double speed, double distance, double robot_orientation, double drive_direction) { //Orientation is to the field //Drive direction is from the robot
        double max;
        double multiplier;
        int right_start;
        int left_start;
        int moveCounts;
        //int drive_direction = -90;
        moveCounts = (int) (distance * COUNTS_PER_INCH);
        right_start = right_rear.getCurrentPosition();
        left_start = left_rear.getCurrentPosition();
        double lfpower;
        double lrpower;
        double rfpower;
        double rrpower;

        double lfbase;
        double lrbase;
        double rfbase;
        double rrbase;
        lfbase = signum(distance)*Math.cos(Math.toRadians(drive_direction + 45));
        lrbase = signum(distance)*Math.sin(Math.toRadians(drive_direction + 45));
        rfbase = signum(distance)*Math.sin(Math.toRadians(drive_direction + 45));
        rrbase = signum(distance)*Math.cos(Math.toRadians(drive_direction + 45));
        while ((abs(right_rear.getCurrentPosition() - right_start) + abs(left_rear.getCurrentPosition() - left_start)) / 2 < abs(moveCounts) /* ENCODERS*/) {//Should we average all four motors?
            //Determine correction
            double correction = robot_orientation - getheading();
            if (correction <= -180){
                correction += 360; }
            else if (correction >= 180) {                      // correction should be +/- 180 (to the left negative, right positive)
                correction -= 360;
            }
            lrpower = lrbase; //MIGHT BE MORE EFFECIENT TO COMBINE THESE WITHT HE ADJUSTMENT PART AND SET ADJUSTMENT TO ZERO IF NOT NEEDED
            lfpower = lfbase;
            rrpower = rrbase;
            rfpower = rfbase;
            if (abs(correction) > drive_THRESHOLD) {//If you are off
                //Apply power to one side of the robot to turn the robot back to the right heading
                double right_adjustment = Range.clip((drive_COEF * correction / 45), -1, 1);
                lrpower -= right_adjustment;
                lfpower -= right_adjustment;
                rrpower = rrbase + right_adjustment;
                rfpower = rfbase + right_adjustment;

            }//Otherwise you Are at the right orientation

            //Determine largest power being applied in either direction
            max = abs(lfpower);
            if (abs(lrpower) > max) max = abs(lrpower);
            if (abs(rfpower) > max) max = abs(rfpower);
            if (abs(rrpower) > max) max = abs(rrpower);

            multiplier = speed / max; //multiplier to adjust speeds of each wheel so you can have a max power of 1 on atleast 1 wheel

            lfpower *= multiplier;
            lrpower *= multiplier;
            rfpower *= multiplier;
            rrpower *= multiplier;

            left_front.setPower(lfpower);
            left_rear.setPower(lrpower);
            right_front.setPower(rfpower);
            right_rear.setPower(rrpower);

//            RobotLog.ii("[GromitIR] ", Double.toString(18.7754*Math.pow(sharpIRSensor.getVoltage(),-1.51)), Integer.toString(left_front.getCurrentPosition()));

        }
        stopMotors();
    }
    /*public void mecanumDriveBlock(double speed, double distance, double robot_orientation, double drive_direction) { //Orientation is to the field //Drive direction is from the robot
        double max;
        double multiplier;
        int right_start;
        int left_start;
        int moveCounts;
        boolean glyphSensed = false;
        //int drive_direction = -90;
        moveCounts = (int) (distance * COUNTS_PER_INCH);
        right_start = right_rear.getCurrentPosition();
        left_start = left_rear.getCurrentPosition();
        double lfpower;
        double lrpower;
        double rfpower;
        double rrpower;

        double lfbase;
        double lrbase;
        double rfbase;
        double rrbase;
        lfbase = signum(distance)*Math.cos(Math.toRadians(drive_direction + 45));
        lrbase = signum(distance)*Math.sin(Math.toRadians(drive_direction + 45));
        rfbase = signum(distance)*Math.sin(Math.toRadians(drive_direction + 45));
        rrbase = signum(distance)*Math.cos(Math.toRadians(drive_direction + 45));
        while ((abs(right_rear.getCurrentPosition() - right_start) + abs(left_rear.getCurrentPosition() - left_start)) / 2 < abs(moveCounts) ) {//Should we average all four motors?
            //Determine correction
            double correction = robot_orientation - getheading();
            if (correction <= -180){
                correction += 360; }
            else if (correction >= 180) {                      // correction should be +/- 180 (to the left negative, right positive)
                correction -= 360;
            }
            lrpower = lrbase; //MIGHT BE MORE EFFECIENT TO COMBINE THESE WITHT HE ADJUSTMENT PART AND SET ADJUSTMENT TO ZERO IF NOT NEEDED
            lfpower = lfbase;
            rrpower = rrbase;
            rfpower = rfbase;
            if (abs(correction) > drive_THRESHOLD) {//If you are off
                //Apply power to one side of the robot to turn the robot back to the right heading
                double right_adjustment = Range.clip((drive_COEF * correction / 45), -1, 1);
                lrpower -= right_adjustment;
                lfpower -= right_adjustment;
                rrpower = rrbase + right_adjustment;
                rfpower = rfbase + right_adjustment;

            }//Otherwise you Are at the right orientation

            //Determine largest power being applied in either direction
            max = abs(lfpower);
            if (abs(lrpower) > max) max = abs(lrpower);
            if (abs(rfpower) > max) max = abs(rfpower);
            if (abs(rrpower) > max) max = abs(rrpower);

            multiplier = speed / max; //multiplier to adjust speeds of each wheel so you can have a max power of 1 on atleast 1 wheel

            lfpower *= multiplier;
            lrpower *= multiplier;
            rfpower *= multiplier;
            rrpower *= multiplier;

            left_front.setPower(lfpower);
            left_rear.setPower(lrpower);
            right_front.setPower(rfpower);
            right_rear.setPower(rrpower);

            if (sensorDistance.getDistance(DistanceUnit.CM) < 12 && !glyphSensed) {     // if block is sensed set boolean
                glyphSensed = true;
            } else if (glyphSensed && sensorDistance.getDistance(DistanceUnit.CM) > 12) {     // if block was already sensed (sense the back end)
                glyphSensed = false;
                sleep(200);
                startclosetime = runtime.milliseconds();    // start timer  set boolean
                //delayLift = true;                            // check for time to lift
                delayClamp = true;
                glyphSensedDelay = 300;
                // set target  as 1 (assume we're at zero for now
                liftTarget = gromit.glyphTrain.liftPosition[1];  // set the new Target
                glyphLiftismoving = true;     // turn on manual override

            }
//            RobotLog.ii("[GromitIR] ", Double.toString(18.7754*Math.pow(sharpIRSensor.getVoltage(),-1.51)), Integer.toString(left_front.getCurrentPosition()));

        }
        stopMotors();
    }*/
    void resetencoders() {
        left_rear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left_front.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right_rear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right_front.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public static void sleep(long sleepTime) {
        long wakeupTime = System.currentTimeMillis() + sleepTime;

        while (sleepTime > 0) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
            }
            sleepTime = wakeupTime - System.currentTimeMillis();
        }
    }   //sleep
}
