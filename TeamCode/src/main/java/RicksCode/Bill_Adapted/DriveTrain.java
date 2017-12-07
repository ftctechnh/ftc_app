package RicksCode.Bill_Adapted;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.internal.opmode.OpModeManagerImpl;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

import java.util.Locale;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;
import static java.lang.Math.abs;
import static java.lang.Math.signum;


public class DriveTrain {


    public enum SpeedSetting {FAST, SLOW}
    private SpeedSetting speedMode;
    public enum BrakeSetting {Float, Brake}
    public final double WHEEL_DIAMETER = 4.0;
    public final double GEAR_RATIO = 1.0;
    public final double TICKS_REV = 1120;
    public final double COUNTS_PER_INCH = (TICKS_REV * GEAR_RATIO) / (WHEEL_DIAMETER * 3.1415);
    public static final double turn_THRESHOLD = 1.0;
    public static final double drive_THRESHOLD = 1.0;
    public static final double turn_MIN_SPEED = 0.3;
    public static final double turn_COEF = 0.90;
    public static final double drive_COEF = 1.0; //Maximum additional speed to add to a motor during a gyro drive


    //Iniatalize motors
    public DcMotor left_front = null;
    public DcMotor right_front = null;
    public DcMotor right_rear = null;
    public DcMotor left_rear = null;

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

        speedMode = SpeedSetting.FAST;
    }

    public void runWithoutEncoders() {
        left_front.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        right_front.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        right_rear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        left_rear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void runUsingEncoders() {
        left_front.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right_front.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right_rear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        left_rear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void drive(double x, double y, double turn) {
        //speed change code
        double speedMultiplier = 1.0;
        switch (speedMode) {
            // lookup parameter for "fast mode"
            case FAST:
                speedMultiplier = 1.0;
                break;
            case SLOW:
                // lookup slow speed parameter
                speedMultiplier = 0.3;
                break;
            default:
                speedMultiplier = 1.0;
        }


        //DRIVE CODE APPLYING MOTOR POWERS
        left_front.setPower(Range.clip(speedMultiplier * (y + x + turn), -1, 1));
        left_rear.setPower(Range.clip(speedMultiplier * (y - x + turn), -1, 1));

        right_front.setPower(Range.clip(speedMultiplier * (y - x + turn), -1, 1));
        right_rear.setPower(Range.clip(speedMultiplier * (y + x + turn), -1, 1));

    }

    public void stopMotors() {
        left_front.setPower(0.0);
        left_rear.setPower(0.0);
        right_front.setPower(0.0);
        right_rear.setPower(0.0);

    }
    void setDriveBreak(BrakeSetting brakeSetting) {
        if (brakeSetting == BrakeSetting.Brake) {
            left_front.setZeroPowerBehavior(BRAKE);
            left_rear.setZeroPowerBehavior(BRAKE);
            right_front.setZeroPowerBehavior(BRAKE);
            right_rear.setZeroPowerBehavior(BRAKE);
        }
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
        if (correction <= -180)
            correction += 360;   // correction should be +/- 180 (to the left negative, right positive)
        if (correction >= 180) correction -= 360;

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

    public void mecanumDrive(double speed, double distance, double robot_orientation, double drive_direction) {
        double max;
        double multiplier;
        int right_start;
        int left_start;
        int moveCounts;
        double right_adjustment;
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
            lrpower = lrbase; //MIGHT BE MORE EFFECIENT TO COMBINE THESE WITHT HE ADJUSTMENT PART AND SET ADJUSTMENT TO ZERO IF NOT NEEDED
            lfpower = lfbase;
            rrpower = rrbase;
            rfpower = rfbase;
            if (abs(correction) > drive_THRESHOLD) {//If you are off
                //Apply power to one side of the robot to turn the robot back to the right heading
                right_adjustment = Range.clip((drive_COEF * correction / 45), -1, 1);

            }//Otherwise you Are at the right orientation
            else{
                right_adjustment = 0;
            }
            lrpower = lrbase - right_adjustment;
            lfpower = lfbase - right_adjustment;
            rrpower = rrbase + right_adjustment;
            rfpower = rfbase + right_adjustment;
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
        }
        stopMotors();
    }
    void resetencoders() {
        left_rear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left_front.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right_rear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right_front.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
}
