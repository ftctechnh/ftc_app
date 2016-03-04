package com.powerstackers.resq.common;

import com.powerstackers.resq.common.enums.PublicEnums.DoorSetting;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import static com.powerstackers.resq.common.RobotConstants.CRS_STOP;
import static com.powerstackers.resq.common.RobotConstants.HOPPER_LEFT_CLOSE;
import static com.powerstackers.resq.common.RobotConstants.HOPPER_RIGHT_CLOSE;
import static com.powerstackers.resq.common.RobotConstants.ZIPLINE_LEFT_CLOSE;
import static com.powerstackers.resq.common.RobotConstants.ZIPLINE_RIGHT_CLOSE;
import static com.powerstackers.resq.common.enums.PublicEnums.MotorSetting;

/**
 * <b>IMPORTANT:</b> This class must be instantiated INSIDE a {@code runOpMode()} method. It can't be done
 * before that, or you'll get a NullPointerException.
 *
 * @author Derek Helm
 */
public class RobotAuto {

//    public static double enRightPosition = 0.0;
//    public static double enLeftPosition = 0.0;
//
//    public static final double EnRightpower = 1;
//    public static final double EnLeftpower = 1;

    // Robot Movements in steps
//    public static final double EnRightS1 = -9000;
//    public static final double EnLeftS1 = 9000;
//    public static final double EnRightS2 = -500;
//    public static final double EnLeftS2 = 500;


//    set range of servo
//    private static final double servoBeacon_MIN_RANGE  = 0.00;
//    private static final double servoBeacon_MAX_RANGE  = 1.00;


//    position of servo <Value of Variable>
//    public static double servoBeaconPosition;

    public OpMode   mode;
    public JonsAlgo algorithm;

    private DcMotor motorLeftA;
    private DcMotor motorLeftB;
    private DcMotor motorRightA;
    private DcMotor motorRightB;
    private DcMotor motorBrush;
//    private DcMotor motorLift;
//    private Servo servoBeacon;
    private Servo servoClimberFlipper;
    private Servo servoChurroLeft;
    private Servo servoChurroRight;
    private Servo servoLiftLeft;
    private Servo servoLiftRight;
    private Servo servoHopperRight;
    private Servo servoHopperLeft;
    private Servo servoHook;
    private Servo servoZippLineLeft;
    private Servo servoZippLineRight;
    private Servo servoHopperSlide;

//    private DeviceInterfaceModule dim;
//    private ColorSensor sensorColor;
    //    private TouchSensor sensorTouch;
    private GyroSensor sensorGyro;
//    private ColorSensor colorSensor;
//    public OpticalDistanceSensor opticalSensor;

    /**
     * Construct a Robot object.
     * @param mode The OpMode in which the robot is being used.
     */
    public RobotAuto(OpMode mode) {

        this.mode = mode;

        motorLeftA  = mode.hardwareMap.dcMotor.get("motorFLeft");
        motorLeftB  = mode.hardwareMap.dcMotor.get("motorBLeft");
        motorRightA = mode.hardwareMap.dcMotor.get("motorFRight");
        motorRightB = mode.hardwareMap.dcMotor.get("motorBRight");
        motorBrush  = mode.hardwareMap.dcMotor.get("motorBrush");
//        motorLift   = mode.hardwareMap.dcMotor.get("motorLift");

//        motorLift.setDirection(DcMotor.Direction.REVERSE);
        motorRightA.setDirection(DcMotor.Direction.REVERSE);
        motorRightB.setDirection(DcMotor.Direction.REVERSE);

//        servoBeacon      = mode.hardwareMap.servo.get("servoBeacon");
        servoClimberFlipper = mode.hardwareMap.servo.get("servoClimbers");
        servoChurroLeft = mode.hardwareMap.servo.get("servoChurroLeft");
        servoChurroRight = mode.hardwareMap.servo.get("servoChurroRight");
        servoHopperRight    = mode.hardwareMap.servo.get("servoHopperRight");
        servoHopperLeft     = mode.hardwareMap.servo.get("servoHopperLeft");
        servoZippLineLeft   = mode.hardwareMap.servo.get("servoZipplineLeft");
        servoZippLineRight  = mode.hardwareMap.servo.get("servoZipplineRight");
        servoHook           = mode.hardwareMap.servo.get("servoHook");
        servoHopperSlide     = mode.hardwareMap.servo.get("servoHopperSlide");
//
        servoClimberFlipper.setPosition(RobotConstants.CLIMBER_EXTEND);
        servoChurroRight.setPosition(RobotConstants.CHURRO_RIGHT_CLOSE);
        servoChurroLeft.setPosition(RobotConstants.CHURRO_LEFT_CLOSE);


        servoHopperLeft.setPosition(HOPPER_LEFT_CLOSE);
        servoHopperRight.setPosition(HOPPER_RIGHT_CLOSE);

        servoLiftLeft       = mode.hardwareMap.servo.get("servoLiftLeft");
        servoLiftRight      = mode.hardwareMap.servo.get("servoLiftRight");

//        dim = mode.hardwareMap.deviceInterfaceModule.get("dim");
//        sensorColor = ClassFactory.createSwerveColorSensor(mode,
//                mode.hardwareMap.colorSensor.get("sensorColor"));
//        sensorColor.enableLed(true);
//        opticalSensor = mode.hardwareMap.opticalDistanceSensor.get("opticalDistance");
        sensorGyro = mode.hardwareMap.gyroSensor.get("sensorGyro");
//        sensorColor = mode.hardwareMap.colorSensor.get("sensorColor");
//
//        colorSensor = ClassFactory.createSwerveColorSensor(mode, mode.hardwareMap.colorSensor.get("colorSensor"));
//        colorSensor.enableLed(true);

        algorithm = new JonsAlgo(this);


        servoLiftLeft.setPosition(CRS_STOP);
        servoLiftRight.setPosition(CRS_STOP);

    }

    /**
     * Initialize the robot's servos and sensors.
     */
    public void initializeRobot() /*throws InterruptedException */{

        servoHopperLeft.setPosition(HOPPER_LEFT_CLOSE);
        servoHopperRight.setPosition(HOPPER_RIGHT_CLOSE);
//        servoBeacon.setPosition(RobotConstants.BEACON_RESTING);
        servoClimberFlipper.setPosition(RobotConstants.CLIMBER_EXTEND);
        servoHook.setPosition(CRS_STOP);
        servoZippLineLeft.setPosition(ZIPLINE_LEFT_CLOSE);
        servoZippLineRight.setPosition(ZIPLINE_RIGHT_CLOSE);
        servoHopperSlide.setPosition(CRS_STOP);
//        servoChurroRight.setPosition(RobotConstants.CHURRO_RIGHT_OPEN);
//        servoChurroLeft.setPosition(RobotConstants.CHURRO_LEFT_OPEN);
        sensorGyro.calibrate();

        servoLiftLeft.setPosition(CRS_STOP);
        servoLiftRight.setPosition(CRS_STOP);
    }

    /**
     * Set the power for the right side drive motors.
     * @param power Double from 0 to 1.
     */
    public void setPowerRight(double power) {
        motorRightA.setPower(power);
        motorRightB.setPower(power);
    }

    /**
     * Set the power for the left side drive motors.
     * @param power Double from 0 to 1.
     */
    public void setPowerLeft(double power) {
        motorLeftA.setPower(power);
        motorLeftB.setPower(power);
    }

    public void setPowerAll(double power) {
        setPowerLeft(power);
        setPowerRight(power);
    }

    /**
     * Set the movement of the brush motor.
     * @param setting MotorSetting indicating the direction.
     */
    public void setBrush(MotorSetting setting) {
        toggleMotor(motorBrush, setting, RobotConstants.BRUSH_SPEED);
    }

    /**
     * Set the direction of the lift: REVERSE, STOP, or FORWARD.
     * @param setting MotorSetting enum indicating the direction.
     */
//    public void setLift(MotorSetting setting) {
//        toggleMotor(motorLift, setting, RobotConstants.LIFT_SPEED);
//    }

    /**
     * Toggles a motor between three settings: FORWARD, STOP, and REVERSE.
     * @param toToggle Motor to change.
     * @param setting MotorSetting indicating the direction.
     * @param power Power value to use.
     */
    private void toggleMotor(DcMotor toToggle, MotorSetting setting, double power) {
        switch (setting) {
            case REVERSE:
                toToggle.setPower(-power);
                break;
            case STOP:
                toToggle.setPower(0);
                break;
            case FORWARD:
                toToggle.setPower(power);
                break;
            default:
                toToggle.setPower(0);
                break;
        }
    }

    /**
     * Toggle a continuous rotation servo in one of three directions: FORWARD, STOP, and REVERSE.
     * @param toToggle Servo to toggle.
     * @param setting MotorSetting indicating the direction.
     */
    private void toggleCRServo(Servo toToggle, MotorSetting setting) {
        switch (setting) {
            case REVERSE:
                toToggle.setPosition(RobotConstants.CRS_REVERSE);
                break;
            case STOP:
                toToggle.setPosition(RobotConstants.CRS_STOP);
                break;
            case FORWARD:
                toToggle.setPosition(RobotConstants.CRS_FORWARD);
                break;
            default:
                toToggle.setPosition(RobotConstants.CRS_STOP);
        }
    }

    /**
     * Trim a servo value between the minimum and maximum ranges.
     * @param servoValue Value to trim.
     * @return A raw double with the trimmed value.
     */
    private double trimServoValue(double servoValue) {
        return Range.clip(servoValue, 0.0, 1.0);
    }

    /**
     * Tap the beacon on the correct side.
     * @param allianceColor The color that we are currently playing as.
     */
//    public void tapBeacon(AllianceColor allianceColor) {
//
//        AllianceColor dominantColor;
//        double positionBeaconServo;
//
//        // Detect the color shown on the beacon's left half, and record it.
//        if (sensorColor.red() > sensorColor.blue()) {
//            dominantColor = AllianceColor.RED;
//        } else {
//            dominantColor = AllianceColor.BLUE;
//        }
//
//        // Tap the correct side based on the dominant color.
//        if (dominantColor == allianceColor) {
//            positionBeaconServo = RobotConstants.BEACON_TAP_LEFT;
//        } else {
//            positionBeaconServo = RobotConstants.BEACON_TAP_RIGHT;
//        }

        // Trim the servo value and set the servo position.
//        positionBeaconServo = trimServoValue(positionBeaconServo);
//        servoBeacon.setPosition(positionBeaconServo);
//    }

    /**
     * Set themode.telemetry.addData("gyro2", robot.getGyroHeading()); right hopper door to open or close.
     * @param doorSetting DoorSetting to set the door to.
     *//*
    public void setHopperRight(DoorSetting doorSetting) {
        if (doorSetting == DoorSetting.OPEN) {
            servoHopperRight.setPosition(RobotCoblic void loop() {

        servoBeaconPosition = Range.clip(servoBeaconPosition, servoBeacon_MIN_RANGE, servoBeacon_MAX_RANGE);

    }nstants.HOPPER_RIGHT_OPEN);
        } else {
            servoHopperRight.setPosition(RobotConstants.HOPPER_RIGHT_CLOSE);
        }
    }*/

    /**
     * Set the climber flipping device to either extended or retracted position.
     * In this case, the OPEN setting indicates extended, and the CLOSED setting is its resting
     * position.
     * @param doorSetting DoorSetting indicating the position.
     */
    public void setClimberFlipper(DoorSetting doorSetting) {
        if (doorSetting == DoorSetting.OPEN) {
            servoClimberFlipper.setPosition(RobotConstants.CLIMBER_RETRACT);
        } else {
            servoClimberFlipper.setPosition(RobotConstants.CLIMBER_EXTEND);
        }
    }

    /**
     * Set the position of the churro grabber servos.
     * In this case, the OPEN position is the retracted, <b>not grabbing</b> position, and close is
     * the opposite.
     * @param doorSetting DoorSetting indicating the position.
     */
    public void setChurroGrabbers(DoorSetting doorSetting) {
        if (doorSetting == DoorSetting.OPEN) {
            servoChurroLeft.setPosition(RobotConstants.CHURRO_LEFT_OPEN);
            servoChurroRight.setPosition(RobotConstants.CHURRO_RIGHT_OPEN);
        } else {
            servoChurroLeft.setPosition(RobotConstants.CHURRO_LEFT_CLOSE);
            servoChurroRight.setPosition(RobotConstants.CHURRO_RIGHT_CLOSE);
        }
    }

    /**
     * Move the robot a specific distance forwards or backwards.
     * To specify the distance, pass a double representing the number of <b>inches</b> that you would
     * like the robot to move. To move backwards, simply pass a negative number.
     * @param distance An integer representing the distance to move.
     */
    public void moveDistance(int distance) {
        motorLeftA.setTargetPosition(motorLeftA.getCurrentPosition() + distance);
        motorRightA.setTargetPosition(motorRightA.getCurrentPosition() + distance);
    }

    public long getLeftEncoder() {
        return motorLeftA.getCurrentPosition();
    }

    public long getRightEncoder() {
        return motorRightA.getCurrentPosition();
    }

    public double getGyroHeading() {
        return  sensorGyro.getHeading();
    }

    public void calibrateGyro() {
        sensorGyro.calibrate();
    }

    public  boolean isGyrocalibrate() {
        return sensorGyro.isCalibrating();
    }

    public double getrawXGyro() {
        return sensorGyro.rawX();
    }

    public double getrawYGyro() {
        return sensorGyro.rawY();
    }

    public double getrawZGyro() {
        return sensorGyro.rawZ();
    }

    /**
     * Turn the robot a specific number of degrees clockwise or counter-clockwise.
     * To specify the number of degrees to turn, pass a double representing the number of
     * <b>degrees</b> to turn. It should be noted that the degrees you turn assume standard position
     * when looking at the robot from above. In other words, passing a negative number will turn
     * clockwise, and a positive number will turn counter-clockwise.
     * @param //degrees A double representing the distance to turn.
     */
//    public void turnDegrees(double degrees, double speed) {
//        // TODO Actually make this method work
//        sensorGyro.calibrate();
//        while (sensorGyro.isCalibrating()){
//
//        }
//
//        if(degrees < 0)
//        {
//            setPowerLeft(-1 * speed);
//            setPowerRight(speed);
//        } else {
//            setPowerLeft(speed);
//            setPowerRight(-1 * speed);
//        }
//
//        while (abs(sensorGyro.getRotation()) < abs(degrees)) {
//
//        }
//        setPowerAll(0);
//    }

//    public void loop() {
//
//        servoBeaconPosition = Range.clip(servoBeaconPosition, servoBeacon_MIN_RANGE, servoBeacon_MAX_RANGE);
//
//    }

    public OpMode getParentOpMode() {
        return mode;
    }

}
