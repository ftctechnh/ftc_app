package com.technicbots;

import android.hardware.Sensor;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * The competition robot with the API for Autonomous Mode and Teleop Mode
 */
public class MainRobot {
    public static int DEFAULTLINSLIDE = 0;
    public static double WHEEL_DIAMETER = 4;
    public static int ENCODER_CPR = 1440;
    public static double GEAR_RATIO = 0.5;

    /**
     * The DCMotor for the left wheel
     */
    private static DcMotor leftWheel;
    /**
     * The DCMotor for the right wheel
     */
    private static DcMotor rightWheel;
    /**
     * The DCMotor for the linear slide
     */
    private static DcMotor linearSlide;
    /**
     * The servo for the button
     */
    private static Servo buttonTouch;
    /**
     * The light sensor(facing downward)
     */
    private static Sensor lightSensor;
    /**
     * The color sensor(facing downward)
     */
    private static Sensor colorSensor;
    /**
     * The gyro sensor(aligned with robot body)
     */
    private static Sensor gyroSensor;

    private static OpticalDistanceSensor opticalDistanceSensor;

    //private static LinSlideButton lastButton = LinSlideButton.Reset;

    /*public enum LinSlideState {
        State0, State1, State2, State3
    }
    public enum LinSlideButton {
        Reset, LowButton, MediumButton, HighButton
    }*/



    public MainRobot(HardwareMap hardwareMap){
        rightWheel = hardwareMap.dcMotor.get("rightwheel");
        leftWheel = hardwareMap.dcMotor.get("leftwheel");
        //opticalDistanceSensor = hardwareMap.opticalDistanceSensor.get("sensor_EOPD");
        rightWheel.setDirection(DcMotor.Direction.REVERSE);
    }

    public MainRobot(DcMotor left, DcMotor right, DcMotor linearLift, Servo button, Sensor light, Sensor color, Sensor gyro){
        leftWheel = left;
        rightWheel = right;
        linearSlide = linearLift;
        buttonTouch = button;
        lightSensor = light;
        colorSensor = color;
        gyroSensor = gyro;

    }

    /**
     * Releases the climbers into shelter
     * Precondition: Robot needs to be in front of the beacon with distance of ???? cm
     * Postcondition: Arm is retracted
     *
     * @return
     */
    public void releaseClimbers() {

    }

    /**
     * Presses the button on the rescue beacon based on color sensor reading
     * Precondition: Robot needs to be ???? cm away from the front of the beacon with color sensor facing beacon
     * PostCondition: Mechanism reset to originial state
     */
    public void pressRescueBeacon() {

    }

    /**
     * Move straight
     * Precondition: Encoder attached to leftWheel
     * Postcondition: Robot moved to target position
     * @Param distance in cm, + = forward, - = backward
     */
    public static void moveStraight(double distance, double power, boolean reverse) {

        final double CIRCUMFERENCE = Math.PI * WHEEL_DIAMETER;
        final  double ROTATIONS = distance / CIRCUMFERENCE;
        final  double COUNTS = ENCODER_CPR * ROTATIONS * GEAR_RATIO;
        rightWheel.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        rightWheel.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
//        rightWheel.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        rightWheel.setTargetPosition((int) COUNTS);
        rightWheel.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
        if (reverse) {
            leftWheel.setPower(-1 * power);
            rightWheel.setPower(-1 * power);
        } else{
            leftWheel.setPower(power);
            rightWheel.setPower(power);
        }
        //telemetry.addData("Encoder Value", rightWheel.getCurrentPosition());
        while (rightWheel.getCurrentPosition()<rightWheel.getTargetPosition()) {
        }
        leftWheel.setPower(0);
        rightWheel.setPower(0);

    }

    /**
     * Turn left/right
     * Precondition: Encoder attached to leftWheel
     * Postcondition: Robot turned target degrees
     * @Param degrees in degrees, + = turn right, - = turn left
     */
    public static void turn(float degrees) {

    }

    /**
     * Go straight until targetIntensity greater than _____
     * Precondition: Robot facing line, light sensor facing down
     * Postcondition: Robot stopped at line
     * @Param targetIntensity in ????
     */
    public static void straightTillLine(float targetIntensity) {

    }
    /**
     * Line follower
     * Precondition: Robot on the line, light sensor attached
     * Postcondition: Robot at target distance
     * @Param distance in cm, + = forward, - = backward
     */
    public static void lineFollower(double distance) {

    }

    /**
     * Scoring Low Goal
     * Precondition: Robot next to mountain,
     * Postcondition: Linear Slide retracted to original state
     * @Param linSlideDistance in cm, + = forward, - = backward
     */
    public static void scoreLowGoal(double linSlideDistance) {

    }

    /**
     * Scoring Medium Goal
     * Precondition: Robot on mountain,
     * Postcondition: Linear Slide retracted to original state
     * @Param linSlideDistance in cm, + = forward, - = backward
     */
    public static void scoreMediumGoal(double linSlideDistance) {

    }

    /**
     * Scoring High Goal
     * Precondition: Robot pn mountain,
     * Postcondition: Linear Slide retracted to original state
     * @Param linSlideDistance in cm, + = forward, - = backward
     */
    public static void scoreHighGoal(double linSlideDistance) {

    }

}
