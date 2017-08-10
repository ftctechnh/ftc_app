package org.firstinspires.ftc.teamcode.PreviousSeasonOpModes.VelocityVortexOpModes;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

public class SupersHardwareMap {
    //Setting up empty hardware map to be replaced later on
    HardwareMap hwMap;
    //HOW TO CONFIGURE THE PHONES:
    //Declaring motor variables                                                                 format: "config name" = M(otor)#, C(ontroller)#
    public DcMotor fleft; //Front left drive motor                                                      "fleft" = M1, C1 with encoder
    public DcMotor fright; //Front right drive motor                                                    "fright" = M2, C1 with encoder
    public DcMotor bleft; //Back left drive motor                                                       "bleft" = M1, C2
    public DcMotor bright; //Back right drive motor                                                     "bright" = M2, C2
    public DcMotor flicker; //Flicker motor                                                             "flicker" = M1, C3 with encoder
    public DcMotor intake; //Intake motor                                                               "intake" = M2, C3

    //Declaring sensor variables                                                                format: "config name" = (category), P(ort)#
    public BNO055IMU imu; //Gyro sensor                                                                 "imu" = I2C (Adafruit IMU), 0
    public OpticalDistanceSensor ods; //Optical distance sensor for stopping in front of the beacon     "color" = I2C (Color Sensor), 1
    public OpticalDistanceSensor ods2; //Optical distance sensor for finding line                       "ods2" = Analog Input (Optical Distance Sensor), 1
    public ColorSensor color; //Color sensor for detecting beacon color                                 "ods" = Analog Input (Optical Distance Sensor), 0

    //Declaring public constants(change to user preference/measurements)
    public static final double WHEEL_DIAMETER = 4 + 7/8;//Unit: inches, measure as current number probably isn't accurate
    public static final double AUTONOMOUS_DRIVE_SPEED = 0.25f;
    public static final double TELEOP_DRIVE_SPEED = 1f;
    public static final double INTAKE_SPEED = -1f;
    public static final double FLICKER_SPEED = 0.8f;
    public static final double BEACON_DISTANCE = 0.08;
    public static final double FLOOR_REFLECTIVITY = .17;
    public static final double LINE_REFLECTIVITY = .95;
    public static final double MIDDLE_REFLECTIVITY = .56;

    //Setting up variables used in program, made some public so that they are accessible by other programs
    public float heading;
    public float lastHeading;
    public float startingAngle;
    float rawGyro;
    float gyroAdd = 0;
    public ElapsedTime timer = new ElapsedTime();
    public boolean notreversed;

    //Teleop constructor
    public SupersHardwareMap(boolean norev) {
        notreversed = norev;                 //Determines whether the program is notreversed side or red side, set to notreversed side for teleop, or red side to go backwards
    }

    //Sets up hardware map, reverses motors, etc.
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        //Setting up the hardwaremap for the motor variables
        fleft = hwMap.dcMotor.get("fright");
        fright = hwMap.dcMotor.get("fleft");
        bleft = hwMap.dcMotor.get("bright");
        bright = hwMap.dcMotor.get("bleft");
        flicker = hwMap.dcMotor.get("flicker");
        intake = hwMap.dcMotor.get("intake");

        //Only sets up sensors in autonomous, otherwise they are not needed
        imu = hwMap.get(BNO055IMU.class, "imu");
        ods = hwMap.opticalDistanceSensor.get("ods");
        ods2 = hwMap.opticalDistanceSensor.get("ods2");
        color = hwMap.colorSensor.get("color");
        color.enableLed(false);

        //Reversing right motors so that all wheels go the same way
        fright.setDirection(DcMotor.Direction.REVERSE);
        bright.setDirection(DcMotor.Direction.REVERSE);
        //May have to reverse flicker or intake
        flicker.setDirection(DcMotor.Direction.REVERSE);

        //Sets motors to the mode that runs them at a constant power (not enough drive motor encoders to make them run at a constant speed, but that would be preferable for autonomous)
        fleft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        fright.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bleft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bright.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //Sets the flicker to run at a constant speed
        flicker.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

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
        startingAngle = heading;
    }

    //Moves or brakes the left drive wheels
    public void ldrive(double leftspeed) {
        if (notreversed) {                           //Moves the drive wheels normally if notreversed side
            fleft.setPower(leftspeed);
            bleft.setPower(leftspeed);
        } else {                              //Moves the drive wheels in the opposite direction and switches motors to drive backwards if on red side(or going backward in teleop)
            fright.setPower(-leftspeed);
            bright.setPower(-leftspeed);
        }
    }

    //Moves or brakes the right drive wheels
    public void rdrive(double rightspeed) {
        if (notreversed) {                           //Moves the drive wheels normally if notreversed side
            fright.setPower(rightspeed);
            bright.setPower(rightspeed);
        } else {                              //Moves the drive wheels in the opposite direction and switches motors to drive backwards if on red side(or going backward in teleop)
            fleft.setPower(-rightspeed);
            bleft.setPower(-rightspeed);
        }
    }

    //Updates the gyro sensor and formats the angle so that it is easier to use
    //Only use in autonomous
    public void updateGyro() {
        //Gets the raw value of the gyro sensor
        rawGyro = -imu.getAngularOrientation().toAxesReference(AxesReference.INTRINSIC).toAxesOrder(AxesOrder.ZYX).firstAngle;

        //Detects if the gyro sensor goes from 0-360 or 360-0 and adjusts gyroAdd to compensate
        if(rawGyro == 0.0 && Math.abs(lastHeading) > 10)
            heading = gyroAdd - lastHeading;
        else {
            if (lastHeading < 10 && rawGyro > 350)
                gyroAdd = gyroAdd + 360;
            else if (lastHeading > 350 && rawGyro < 10)
                gyroAdd = gyroAdd - 360;

            //Puts formatted angle in heading variable and sets the current value as last value for the next cycle
            heading = gyroAdd - rawGyro;
            lastHeading = rawGyro;
        }
    }
}