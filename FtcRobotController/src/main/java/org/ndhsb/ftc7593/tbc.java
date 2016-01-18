package org.ndhsb.ftc7593;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

/**
 * Created by mhaeberli on 12/3/15.
 * to hold common values, etc.
 * tbc means "TigerBotsCommon"
 */

// Todo list - probably better kept as Git "issues" but no time now
    //-URGENT - motor direction on front right tank tread reversed for some reason
    //-fix in code vs wires
// -make sure both robots drive the same direction forward; this may require re-wiring the motor

// (almost) done pending testing
    // -finish refactoring common auton / teleop code
    // -finish refactoring common init code

    //  control outputs
    // -add turn - by - gyro to auton
    // -encoder setup
    // -add color / light sensor tracking / steering
    // -add selective color / light sensor tracking / steering
    // -add button pusher auton
    // -can we catch "EMERGENCY_STOP"?
    // -add "sniping" / driving mode to give fine-grain half-range control inputs

// Done
// -add "compiler" for autonlist


public class tbc {
    public final static double MTAPE_MIN_RANGE  = 0.0;
    public final static double MTAPE_MAX_RANGE  = 1.0;
    public final static double SNOWPLOW_MIN_RANGE = 0.20;
    public final static double SNOWPLOW_MID_RANGE = 0.40;
    public final static double SNOWPLOW_MAX_RANGE = 0.70;
    public final static double CLIMBER_MIN_RANGE = 0.0;
    public final static double CLIMBER_MAX_RANGE = 1.0;
    public final static double SLIDERL_MIN_RANGE = 0.0;
    public final static double SLIDERL_MAX_RANGE = 1.0; //0.50
    public final static double SLIDERR_MIN_RANGE = 0.0; //0.30
    public final static double SLIDERR_MAX_RANGE = 1.0;

    public static double climberPosition = CLIMBER_MAX_RANGE;
    public static double sliderLPosition = SLIDERL_MIN_RANGE;
    public static double sliderRPosition = SLIDERR_MAX_RANGE;
    public static double snowplowPosition = SNOWPLOW_MIN_RANGE;
    public static double mtapePosition = MTAPE_MIN_RANGE;
    public static double buttonServoSpeed = 0.5;


    public static void initServoValues() {
        climberPosition = CLIMBER_MAX_RANGE;
        sliderLPosition = SLIDERL_MIN_RANGE;
        sliderRPosition = SLIDERR_MAX_RANGE;
        snowplowPosition = SNOWPLOW_MIN_RANGE;
        mtapePosition = MTAPE_MIN_RANGE;
        buttonServoSpeed = 0.5;
    }

    public static ServoController sc = null;
    public static Servo button = null;
    public static Servo climber = null;
    public static Servo snowplow = null;
    public static Servo mtape = null;
    public static Servo sliderL = null;
    public static Servo sliderR = null;

    public static DcMotor motorFRight = null;
    public static DcMotor motorFLeft = null;
    public static DcMotor motorRRight = null;
    public static DcMotor motorRLeft = null;

    public static DcMotor motorHook = null;
    public static DcMotor motorPusher = null;

    public static ColorSensor sensorRGB = null;

    public static GyroSensor sensorGyro = null;

    public static HardwareMap hardwareMap = null;

    public static void setClimberPosition(Double cPos) {
        if (climber != null) {
            climber.setPosition(cPos);
        }
    }

    public static void setSliderLPosition(Double sPos) {
        if (sliderL != null) {
            sliderL.setPosition(sPos);
        }
    }

    public static void setSliderRPosition(Double sPos) {
        if (sliderR != null) {
            sliderR.setPosition(sPos);
        }
    }

    public static void setSnowplowPosition(Double sPos) {
        if (snowplow != null) {
            snowplow.setPosition(sPos);
        }
    }

    public static void setMtapePosition(Double mPos) {
        if (mtape != null) {
            mtape.setPosition(mPos);
        }
    }

    public static void setButtonServoSpeed(Double bPos) {
        if (button != null) {
            button.setPosition(bPos);
        }
    }

    public static void setMotorRRightPower(float power) {
        if (motorRRight != null) {
            motorRRight.setPower(power);
        }
    }

    public static void setMotorRLeftPower(float power) {
        if (motorRLeft != null) {
            motorRLeft.setPower(power);
        }
    }

    public static void setMotorFRightPower(float power) {
        if (motorFRight != null) {
            motorFRight.setPower(power);
        }
    }

    public static void setMotorFLeftPower(float power) {
        if (motorFLeft != null) {
            motorFLeft.setPower(power);
        }
    }

    public static void setMotorHookPower(float power) {
        if (motorHook != null) {
            motorHook.setPower(power);
        }
    }

    public static void setMotorPusherPower(float power) {
        if (motorPusher != null) {
            motorPusher.setPower(power);
        }
    }

    public static void setDriveMode(DcMotorController.RunMode mode) {
        if (motorRLeft != null) {
            if (motorRLeft.getMode() != mode) {
                motorRLeft.setMode(mode);
            }
        }

        if (motorFLeft != null) {
            if (motorFLeft.getMode() != mode) {
                motorFLeft.setMode(mode);
            }
        }

        if (motorRRight != null) {
            if (motorRRight.getMode() != mode) {
                motorRRight.setMode(mode);
            }
        }

        if (motorFRight != null) {
            if (motorFRight.getMode() != mode) {
                motorFRight.setMode(mode);
            }
        }
    }

    public static void initHardwareMap() {
        try {
            sc = hardwareMap.servoController.get("sc");
            // should make the try catch guards below fancier
            climber = hardwareMap.servo.get("climber");
            button = hardwareMap.servo.get("button");
            mtape = hardwareMap.servo.get("mtape");
            snowplow = hardwareMap.servo.get("snowplow");
            sliderL = hardwareMap.servo.get("sliderL");
            sliderR = hardwareMap.servo.get("sliderR");
        } catch (Exception ex) {
        }


        try {
            motorRRight = hardwareMap.dcMotor.get("motor_right_rear"); //RRight
            motorRLeft = hardwareMap.dcMotor.get("motor_left_rear"); //RLeft
            motorRLeft.setDirection(DcMotor.Direction.REVERSE);
        } catch (Exception ex) {
        }

        /*
        * allows robot to run if there are only 2 drive motors
        */
        try {
            motorFRight = hardwareMap.dcMotor.get("motor_right_front"); // FRight
            motorFLeft = hardwareMap.dcMotor.get("motor_left_front"); // FLeft
            motorFLeft.setDirection(DcMotor.Direction.REVERSE);
        } catch (Exception ex) {
        }

        try {
            motorHook = hardwareMap.dcMotor.get("motorHook");
            motorPusher = hardwareMap.dcMotor.get("pusher");
        } catch (Exception ex) {
        }

        try {
            sensorRGB = hardwareMap.colorSensor.get("color");
            // turn off LED of light sensor.
            sensorRGB.enableLed(false);
        } catch (Exception ex) {
        }

        try {
            sensorGyro = hardwareMap.gyroSensor.get("gyro");
            sensorGyro.calibrate();
            while (sensorGyro.isCalibrating())  {
                Thread.sleep(50);
            }
        } catch (Exception ex) {
        }

    }

    public static void destroyHardwareMap() {

        if (sc != null) {
            sc.pwmDisable();
            sc = null;
        }

        button = null;
        climber = null;
        snowplow = null;
        mtape = null;
        sliderL = null;
        sliderR = null;

        motorFRight = null;
        motorFLeft = null;
        motorRRight = null;
        motorRLeft = null;

        motorHook = null;
        motorPusher = null;
        sensorRGB = null;
    }

/*
 * This method scales the joystick input so for low joystick values, the
 * scaled value is less than linear.  This is to make it easier to drive
 * the robot more precisely at slower speeds.
 */
    double scaleInput(double dVal)  {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0) {
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }

        // get value from the array.
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        // return scaled value.
        return dScale;
    }
}
