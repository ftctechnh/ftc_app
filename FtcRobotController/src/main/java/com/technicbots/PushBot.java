package com.technicbots;

import android.hardware.Sensor;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * The Pushbot robot with the API for Autonomous Mode and Teleop Mode
 */
public class PushBot {
 //   public static int DEFAULTLINSLIDE = 0;
    public static double WHEEL_DIAMETER = 4;
    public static int ENCODER_CPR = 1440;
    public static double GEAR_RATIO = 0.5;

    /**
     * The DCMotor for the left wheel
     */
    private static DcMotor leftMotor;
    /**
     * The DCMotor for the right wheel
     */
    private static DcMotor rightMotor;
    /**
     * The DCMotor for the linear slide
     */
    private DcMotor linearSlide;
    /**
     * The servo for the button
     */
    private Servo buttonTouch;
    /**
     * The light sensor(facing downward)
     */
    private Sensor lightSensor;
    /**
     * The color sensor(facing downward)
     */
    private Sensor colorSensor;
    /**
     * The gyro sensor(aligned with robot body)
     */
    private Sensor gyroSensor;

    //private static LinSlideButton lastButton = LinSlideButton.Reset;

    /*public enum LinSlideState {
        State0, State1, State2, State3
    }
    public enum LinSlideButton {
        Reset, LowButton, MediumButton, HighButton
    }*/

    public PushBot(HardwareMap hardwareMap){
        rightMotor = hardwareMap.dcMotor.get("rightMotor");
        leftMotor = hardwareMap.dcMotor.get("leftMotor");
        rightMotor.setDirection(DcMotor.Direction.REVERSE);
    }

    public PushBot(DcMotor left, DcMotor right, DcMotor linearLift, Servo button, Sensor light, Sensor color, Sensor gyro){
        leftMotor = left;
        rightMotor = right;
        linearSlide = linearLift;
        buttonTouch = button;
        lightSensor = light;
        colorSensor = color;
        gyroSensor = gyro;
        rightMotor.setDirection(DcMotor.Direction.REVERSE);
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
     * Precondition: Encoder attached to leftMotor
     * Postcondition: Robot moved to target position
     * @Param distance in cm, + = forward, - = backward
     */
    public static void moveStraight(double distance, double power, boolean reverse) {

        final double CIRCUMFERENCE = Math.PI * WHEEL_DIAMETER;
        final  double ROTATIONS = distance / CIRCUMFERENCE;
        final  double COUNTS = ENCODER_CPR * ROTATIONS * GEAR_RATIO;
        rightMotor.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        rightMotor.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
//        rightMotor.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        leftMotor.setTargetPosition((int) COUNTS);;
        rightMotor.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
      if (reverse) {
          leftMotor.setPower(-1 * power);
          rightMotor.setPower(-1 * power);
      } else{
                leftMotor.setPower(power);
                rightMotor.setPower(power);
            }
        //telemetry.addData("Encoder Value", rightMotor.getCurrentPosition());
        while (rightMotor.getCurrentPosition()<rightMotor.getTargetPosition()) {
        }
        leftMotor.setPower(0);
        rightMotor.setPower(0);

    }

    /**
     * Turn left/right
     * Precondition: Encoder attached to leftMotor
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