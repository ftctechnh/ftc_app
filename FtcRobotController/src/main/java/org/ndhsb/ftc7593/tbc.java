package org.ndhsb.ftc7593;

import com.qualcomm.ftcrobotcontroller.opmodes.ColorSensorDriver;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.LightSensor;

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
    public final static double SNOWPLOW_MIN_RANGE = 0.10;
    public final static double SNOWPLOW_MID_RANGE = 0.40;
    public final static double SNOWPLOW_MAX_RANGE = 1.0;
    public final static double CLIMBER_MIN_RANGE = 0.0;
    public final static double CLIMBER_MAX_RANGE = 1.0;
    public final static double SLIDERL_MIN_RANGE = 0.0;
    public final static double SLIDERL_MAX_RANGE = 0.50; //0.50
    public final static double SLIDERR_MIN_RANGE = 0.50; //0.30
    public final static double SLIDERR_MAX_RANGE = 1.0;

    public static double climberPosition = CLIMBER_MAX_RANGE;
    public static double sliderLPosition = SLIDERL_MIN_RANGE;
    public static double sliderRPosition = SLIDERR_MAX_RANGE;
    public static double snowplowPosition = SNOWPLOW_MIN_RANGE;
    public static double mtapePosition = MTAPE_MIN_RANGE;
    public static double buttonServoSpeed = 0.5;
    public static double slideServoSpeed = 0.5;
    public static double dumperServoSpeed = 0.5;

    public ColorSensorDriver.ColorSensorDevice device = ColorSensorDriver.ColorSensorDevice.MODERN_ROBOTICS_I2C;

    public static void initServoValues() {
        climberPosition = CLIMBER_MAX_RANGE;
        sliderLPosition = SLIDERL_MIN_RANGE;
        sliderRPosition = SLIDERR_MAX_RANGE;
        snowplowPosition = SNOWPLOW_MIN_RANGE;
        mtapePosition = MTAPE_MIN_RANGE;
        buttonServoSpeed = 0.5;
        slideServoSpeed = 0.5;
        dumperServoSpeed = 0.5;
    }

    public static ServoController sc = null;
    public static Servo button = null;
    public static Servo climber = null;
    public static Servo snowplow = null;
    public static Servo mtape = null;
    public static Servo sliderL = null;
    public static Servo sliderR = null;
    public static Servo slide1 = null;
    public static Servo slide2 = null;
    public static Servo slide3 = null;
    public static Servo box = null;

    public static DcMotor motorFRight = null;
    public static DcMotor motorFLeft = null;
    public static DcMotor motorRRight = null;
    public static DcMotor motorRLeft = null;

    public static DcMotor motorHook = null;
    public static DcMotor motorPusher = null;
    public static DcMotor motorIntake = null;

    public static ColorSensor light2 = null;
    public static ColorSensor light1 = null;

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

    public static void setSlide1ServoSpeed(Double sPos) {
        if (slide1 != null) {
            slide1.setPosition(sPos);
        }
    }

    public static void setSlide2ServoSpeed(Double sPos) {
        if (slide2 != null) {
            slide2.setPosition(sPos);
        }
    }

    public static void setSlide3ServoSpeed(Double sPos) {
        if (slide3 != null) {
            slide3.setPosition(sPos);
        }
    }

    public static void setBoxServoSpeed(Double sPos) {
        if (box != null) {
            box.setPosition(sPos);
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

    public static void setMotorIntakePower(float power) {
        if (motorIntake != null) {
            motorIntake.setPower(power);
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
            slide1 = hardwareMap.servo.get("slide1");
            slide2 = hardwareMap.servo.get("slide2");
            slide3 = hardwareMap.servo.get("slide3");
            box = hardwareMap.servo.get("box");
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
            motorIntake = hardwareMap.dcMotor.get("intake");
        } catch (Exception ex) {
        }

        try {
            light2 = hardwareMap.colorSensor.get("light2");
            light2.setI2cAddress(0x40);
            light2.enableLed(true);
        } catch (Exception ex) {
        }

        try {
            light1 = hardwareMap.colorSensor.get("light1");
            light1.setI2cAddress(0x3e);
            light1.enableLed(true);
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
        slide1 = null;
        slide2 = null;
        slide3 = null;
        box = null;

        motorFRight = null;
        motorFLeft = null;
        motorRRight = null;
        motorRLeft = null;

        motorHook = null;
        motorPusher = null;
        motorIntake = null;
        light2 = null;
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
