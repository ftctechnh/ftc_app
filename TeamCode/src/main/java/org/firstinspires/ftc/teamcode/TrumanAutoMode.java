package org.firstinspires.ftc.teamcode;
//
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.hardware.CRServo;
//import com.qualcomm.robotcore.hardware.ColorSensor;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.GyroSensor;
//import com.qualcomm.robotcore.hardware.I2cAddr;
//import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
//import com.qualcomm.robotcore.hardware.Servo;
//
public class TrumanAutoMode{}
//
//    // Constants
//    private static final float MOTOR_POWER = .2f;
//    private static final float SLOW_MOTOR_POWER = .2f;
//    private static final float SLOW_TURN_POWER = .2f;
//
//    private static final float TIME_STOPPED = 1.0f;
//
//    private static final float RAMP_TIME = .6f;
//
//    private static final float FIRST_TURING_TIME = 1.2f;
//
//    private static final int BEACON_DISTANCE_THRESHOLD = 13;
//
//    private static final int COLOR_ON_WHITE_THRESHOLD = 8;
//    private static final int COLOR_OFF_WHITE_THRESHOLD = 3;
//
//    private static final double CLICKER_SLIDE_TIME = .4f;
//    private static final double CLICKING_FORWARD_TIME = 1.0f;
//
//    private static final double BACKING_UP_TIME = 0.5f;
//
//    private static final double TIME_TOWARDS_BALL = 2.75f;
//    private static final double TIME_FROM_BALL = 1.0f;
//
//    private static final int DEFAULT_ANGLE_THRESHOLD = 3;
//
//    public enum Turn {
//        Right, Left
//    }
//
//    public enum Color {
//        Red, Blue
//    }
//
//    public enum State {
//        Calibrating,
//        RampUpShootStageOne,
//        RampUpShootStageTwo,
//        ShootingBalls,
//        RampDownShootStageOne,
//        RampDownShootStageTwo,
//        DrivingTowardsBall,
//        BackingFromBall,
//        Turning,
//        Start,
//        Searching,
//        Straightening,
//        FindTheWhiteLine,
//        ApproachingBeacon,
//        ScanningLeft,
//        CenteringFromLeft,
//        ScanningRight,
//        CenteringFromRight,
//        Picking,
//        GoLeft,
//        GoRight,
//        Clicking,
//        Backing,
//        SlidingToNextBeacon,
//        Stopped,
//        Done
//    }
//
//    // Motors and servos
//    DcMotor mFrontRight;
//    DcMotor mFrontLeft;
//    DcMotor mBackRight;
//    DcMotor mBackLeft;
//
//    DcMotor mRamp;
//    DcMotor mPropLeft;
//    DcMotor mPropRight;
//
//    CRServo sSlide;
//    Servo sBallGuard;
//    Servo sLeft;
//    Servo sRight;
//
//    // Miscellaneous Hardware
//    ColorSensor bottomColor;
//    ColorSensor frontColor;
//
//    RangeSensor range;
//
//    // Init state
//    Turn turning = Turn.Right;
//    Color seekColor = Color.Red;
//    double timeDelay;
//
//    // State
//    State state;
//    double time_at_start;
//    double time_to_move;
//    double time_to_scan;
//    Color leftSideGuess;
//    Color rightSideGuess;
//
//    // Stopped State
//    double time_to_stop;
//    State next_state;
//
//    State after_scan_state;
//
//    GyroSensor gyro;
//
//    public TrumanAutoMode(Turn turn, Color color, double delay) {
//        turning = turn;
//        seekColor = color;
//        timeDelay = delay;
//    }
//
//    @Override
//    public void init() {
//        mFrontRight = hardwareMap.dcMotor.get("front right");
//        mFrontLeft = hardwareMap.dcMotor.get("front left");
//        mBackRight = hardwareMap.dcMotor.get("back right");
//        mBackLeft = hardwareMap.dcMotor.get("back left");
//
//        mBackLeft.setDirection(DcMotor.Direction.REVERSE);
//        mFrontLeft.setDirection(DcMotor.Direction.REVERSE);
//
//        mPropRight = hardwareMap.dcMotor.get("prop right");
//        mPropLeft = hardwareMap.dcMotor.get("prop left");
//
//        sBallGuard = hardwareMap.servo.get("servo guard");
//        sBallGuard.setPosition(0.5f);
//
//        sSlide = hardwareMap.crservo.get("servo slide");
//        sLeft = hardwareMap.servo.get("servo left");
//
//        frontColor = hardwareMap.colorSensor.get("front");
//        frontColor.setI2cAddress(I2cAddr.create8bit(0x2c));
//        bottomColor = hardwareMap.colorSensor.get("bottom");
//        bottomColor.setI2cAddress(I2cAddr.create8bit(0x1c));
//
//        frontColor.enableLed(false);
//        bottomColor.enableLed(false);
//
//        range = new RangeSensor(hardwareMap, "range");
//        gyro = hardwareMap.gyroSensor.get("gyro");
//
//        resetState();
//    }
//
//    @Override
//    public void stop() {
//    }
//
//    @Override
//    public void start() {
//        bottomColor.enableLed(true);
//        resetState();
//    }
//
//    private void changeState(State new_state) {
//        time_at_start = time;
//        state = new_state;
//    }
//
//    private void stopState(double time, State next)
//    {
//        time_to_stop = time;
//        next_state = next;
//        changeState(State.Stopped);
//    }
//
//    private void setMotorsForward() {
//        mFrontRight.setPower(MOTOR_POWER);
//        mBackRight.setPower(MOTOR_POWER);
//        mFrontLeft.setPower(MOTOR_POWER);
//        mBackLeft.setPower(MOTOR_POWER);
//    }
//
//    private void setMotorsBackward() {
//        mFrontRight.setPower(-MOTOR_POWER);
//        mBackRight.setPower(-MOTOR_POWER);
//        mFrontLeft.setPower(-MOTOR_POWER);
//        mBackLeft.setPower(-MOTOR_POWER);
//    }
//    private void setMotorsForwardSlow() {
//        mFrontRight.setPower(SLOW_MOTOR_POWER);
//        mBackRight.setPower(SLOW_MOTOR_POWER);
//        mFrontLeft.setPower(SLOW_MOTOR_POWER);
//        mBackLeft.setPower(SLOW_MOTOR_POWER);
//    }
//
//    private void setMotorsBackwardSlow() {
//        mFrontRight.setPower(-SLOW_MOTOR_POWER);
//        mBackRight.setPower(-SLOW_MOTOR_POWER);
//        mFrontLeft.setPower(-SLOW_MOTOR_POWER);
//        mBackLeft.setPower(-SLOW_MOTOR_POWER);
//    }
//
//    private void setMotorsStopped() {
//        mFrontRight.setPower(0.0f);
//        mBackRight.setPower(0.0f);
//        mFrontLeft.setPower(0.0f);
//        mBackLeft.setPower(0.0f);
//    }
//
//    private void setTurnRight() {
//        mFrontRight.setPower(-MOTOR_POWER);
//        mBackRight.setPower(-MOTOR_POWER);
//        mFrontLeft.setPower(MOTOR_POWER);
//        mBackLeft.setPower(MOTOR_POWER);
//    }
//
//    private void setTurnLeft() {
//        mFrontRight.setPower(MOTOR_POWER);
//        mBackRight.setPower(MOTOR_POWER);
//
//        mFrontLeft.setPower(-MOTOR_POWER);
//        mBackLeft.setPower(-MOTOR_POWER);
//    }
//    private void setTurnRightSlow() {
//        mFrontRight.setPower(-SLOW_MOTOR_POWER);
//        mBackRight.setPower(-SLOW_MOTOR_POWER);
//        mFrontLeft.setPower(SLOW_MOTOR_POWER);
//        mBackLeft.setPower(SLOW_MOTOR_POWER);
//    }
//
//    private void setTurnLeftSlow() {
//        mFrontRight.setPower(SLOW_MOTOR_POWER);
//        mBackRight.setPower(SLOW_MOTOR_POWER);
//
//        mFrontLeft.setPower(-SLOW_MOTOR_POWER);
//        mBackLeft.setPower(-SLOW_MOTOR_POWER);
//    }
//
//
//    private void setMotorsRight() {
//        mFrontRight.setPower(MOTOR_POWER);
//        mBackRight.setPower(-MOTOR_POWER);
//
//        mFrontLeft.setPower(MOTOR_POWER);
//        mBackLeft.setPower(-MOTOR_POWER);
//    }
//    private void setMotorsLeft() {
//        mFrontRight.setPower(-MOTOR_POWER);
//        mBackRight.setPower(MOTOR_POWER);
//
//        mFrontLeft.setPower(-MOTOR_POWER);
//        mBackLeft.setPower(MOTOR_POWER);
//    }
//    private void setMotorsRightSlow() {
//        mFrontRight.setPower(SLOW_MOTOR_POWER);
//        mBackRight.setPower(-SLOW_MOTOR_POWER);
//
//        mFrontLeft.setPower(SLOW_MOTOR_POWER);
//        mBackLeft.setPower(-SLOW_MOTOR_POWER);
//    }
//    private void setMotorsLeftSlow() {
//        mFrontRight.setPower(-SLOW_MOTOR_POWER);
//        mBackRight.setPower(SLOW_MOTOR_POWER);
//
//        mFrontLeft.setPower(-SLOW_MOTOR_POWER);
//        mBackLeft.setPower(SLOW_MOTOR_POWER);
//    }
//
//    public void resetState() {
//        sLeft.setPosition(0.0);
//        sSlide.setPower(0.0);
//
//        time_at_start = 0.0f;
//        time_to_move = 0.0f;
//        time_to_scan = 0.0f;
//        leftSideGuess = null;
//        rightSideGuess = null;
//
//        // These are just sane defaults that probably won't crash the robot, they shouldn't be used
//        // as defaults that can be relied on.
//        time_to_stop = 1.0f;
//        next_state = State.Done;
//
//        // Calibrate the driver state
//        gyro.calibrate();
//
//        changeState(State.Calibrating);
//    }
//
//    @Override
//    public void loop() {
//
//        range.updateCache();
//
//        switch (state) {
//            case Calibrating:
//                telemetry.addData("doing", "calibrating");
//                if(!gyro.isCalibrating()) {
//                    // Don't start searching until the gyro is finished calibrating.
//                    after_scan_state = State.SlidingToNextBeacon;
//                    changeState(State.ScanningLeft);
//                }
//                break;
//            case RampUpShootStageOne:
//                telemetry.addData("doing", "ramp up shoot stage one");
//                if (time - time_at_start < RAMP_TIME) {
//                    mPropRight.setPower(.4f);
//                    mPropLeft.setPower(.4f);
//                } else {
//                    changeState(State.RampUpShootStageTwo);
//                }
//                break;
//            case RampUpShootStageTwo:
//                telemetry.addData("doing", "ramp up shoot stage two");
//                if (time - time_at_start < RAMP_TIME) {
//                    mPropRight.setPower(.7f);
//                    mPropLeft.setPower(.7f);
//                } else {
//                    changeState(State.ShootingBalls);
//                }
//                break;
//            case ShootingBalls:
//                telemetry.addData("doing", "shooting balls");
//                if (time - time_at_start < 2.0f) {
//                    mPropRight.setPower(1.0f);
//                    mPropLeft.setPower(1.0f);
//                    sBallGuard.setPosition(0.0f);
//                } else {
//                    changeState(State.RampDownShootStageOne);
//                }
//                break;
//            case RampDownShootStageOne:
//                telemetry.addData("doing", "ramp down shoot stage one");
//                if (time - time_at_start < RAMP_TIME) {
//                    mPropRight.setPower(.5f);
//                    mPropLeft.setPower(.5f);
//                } else {
//                    changeState(State.RampDownShootStageTwo);
//                }
//                break;
//            case RampDownShootStageTwo:
//                telemetry.addData("doing", "ramp down shoot stage two");
//                if (time - time_at_start < RAMP_TIME) {
//                    mPropRight.setPower(.2f);
//                    mPropLeft.setPower(.2f);
//                } else {
//                    mPropRight.setPower(0.0f);
//                    mPropLeft.setPower(0.0f);
//                    changeState(State.Start);
//                }
//                break;
//            case DrivingTowardsBall:
//                telemetry.addData("doing", "driving towards ball");
//                setMotorsBackward();
//                if (time - time_at_start >= TIME_TOWARDS_BALL) {
//                    setMotorsStopped();
//                    changeState(State.Start);
//                }
//                break;
//            case Start:
//                telemetry.addData("doing", "starting");
//
//                if (time >= timeDelay) {
//                    changeState(State.Turning);
//                }
//                break;
//            case BackingFromBall:
//                telemetry.addData("doing", "backing from ball");
//                setMotorsForward();
//                if (time - time_at_start >= TIME_FROM_BALL)
//                {
//                    setMotorsStopped();
//                    changeState(State.Turning);
//                }
//                break;
//            case Turning:
//                telemetry.addData("doing", "turning");
//                if (turning == Turn.Left) {
//                    setTurnLeft();
//                } else if (turning == Turn.Right) {
//                    setTurnRight();
//                }
//                if (time - time_at_start > FIRST_TURING_TIME) {
//                    changeState(State.Searching);
//                }
//                break;
//            case Searching:
//                telemetry.addData("doing", "searching");
//                setMotorsForwardSlow();
//
//                if (bottomColor.alpha() >= COLOR_ON_WHITE_THRESHOLD) {
//                    stopState(TIME_STOPPED, State.Straightening);
//                }
//                break;
//            case Straightening:
//                telemetry.addData("doing", "straightening");
//
//                // This state only runs the first time, so make sure that after we scan we go back
//                // to finding the white line again.
//                after_scan_state = State.SlidingToNextBeacon;
//
//                if(turning == Turn.Left)
//                {
//                    setTurnRightSlow();
//                    if(robotIsAtAngle(90))
//                    {
//                        changeState(State.FindTheWhiteLine);
//                    }
//                }
//                else if(turning == Turn.Right)
//                {
//                    setTurnLeftSlow();
//                    if(robotIsAtAngle(270))
//                    {
//                        changeState(State.FindTheWhiteLine);
//                    }
//                }
//                break;
//            case FindTheWhiteLine:
//                telemetry.addData("doing", "finding the white line");
//
//                if(turning == Turn.Left)
//                {
//                    setMotorsLeft();
//                }
//                else
//                {
//                    setMotorsRight();
//                }
//
//                if(bottomColor.alpha() >= COLOR_ON_WHITE_THRESHOLD)
//                {
//                    // We found the white line
//                    setMotorsStopped();
//                    changeState(State.ApproachingBeacon);
//                }
//                break;
//            case ApproachingBeacon:
//                // Maybe add a hard limit on the time we can wait for the
//                // distance sensor. Or not, IMO the distance sensor probably
//                // won't fail and more likely it will make the robot drop the
//                // climbers unnecessarily, screwing us over during the driver
//                // period.
//
//                telemetry.addData("doing", "approaching beacon");
//                setMotorsForwardSlow();
//                if (range.ultraSonic() <= BEACON_DISTANCE_THRESHOLD) {
//                    setMotorsStopped();
//                    changeState(State.ScanningLeft);
//                }
//                break;
//            case ScanningLeft:
//                telemetry.addData("doing", "scanning left");
//
//                // Start scanning to the left
//
//                // Try to guess this color!
//                leftSideGuess = guessFrontColor();
//
//                if (time - time_at_start < 0.5f || leftSideGuess == null) {
//                    setMotorsLeftSlow();
//                } else {
//                    // How long did we scan for?
//                    time_to_scan = time - time_at_start;
//                    changeState(State.CenteringFromLeft);
//                }
//                break;
//            case CenteringFromLeft:
//                telemetry.addData("doing", "centering from left");
//                if (time - time_at_start < time_to_scan) {
//                    // Scan right for the same amount of time to center it.
//                    setMotorsRightSlow();
//                } else {
//                    changeState(State.ScanningRight);
//                }
//                break;
//            case ScanningRight:
//                telemetry.addData("doing", "scanning right");
//                rightSideGuess = guessFrontColor();
//
//                if (time - time_at_start < time_to_scan || rightSideGuess == null) {
//                    // Go the same distance to the right
//                    setMotorsRightSlow();
//                } else {
//                    changeState(State.CenteringFromRight);
//                }
//                break;
//            case CenteringFromRight:
//                telemetry.addData("doing", "centering from right");
//
//                if (time - time_at_start < time_to_scan) {
//                    setMotorsLeftSlow();
//                }
//                changeState(State.Picking);
//                break;
//            case Picking:
//                telemetry.addData("doing", "picking");
//
//                // Repeat if we don't know which side is which
//                if (leftSideGuess == null || rightSideGuess == null) {
//                    changeState(State.ScanningLeft);
//                } else if (leftSideGuess == seekColor) {
//                    // Go left!
//                    changeState(State.GoLeft);
//                } else if (rightSideGuess == seekColor) {
//                    changeState(State.GoRight);
//                } else {
//                    // The other robot already took it from us, it's safe to
//                    // pick either one and go for it, I believe.
//                    changeState(State.GoRight);
//                }
//                break;
//            case GoLeft:
//                telemetry.addData("doing", "going left");
//
//                if (time - time_at_start < time_to_scan) {
//                    setMotorsLeftSlow();
//                } else {
//                    changeState(State.Clicking);
//                }
//                break;
//            case GoRight:
//                telemetry.addData("doing", "going right");
//
//                // TODO: Use >= on the change state case just like most other state impls.
//                if (time - time_at_start < time_to_scan) {
//                    setMotorsRightSlow();
//                } else {
//                    changeState(State.Clicking);
//                }
//                break;
//            case Clicking:
//                telemetry.addData("doing", "clicking");
//
//                setMotorsForward();
//
//                if (time - time_at_start >= CLICKING_FORWARD_TIME) {
//                    changeState(State.Backing);
//                }
//                break;
//            case Backing:
//                telemetry.addData("doing", "backing");
//
//                setMotorsBackward();
//
//                if (time - time_at_start >= CLICKING_FORWARD_TIME) {
//                    changeState(after_scan_state);
//                }
//                break;
//            case SlidingToNextBeacon:
//                telemetry.addData("doing", "sliding to next beacon");
//                // This state just sets up find the white line mode to finish by going into the done
//                // state.
//
//                // Well, we have to make sure we get off the white line first
//                if(bottomColor.alpha() <= COLOR_OFF_WHITE_THRESHOLD) {
//                    after_scan_state = State.Done;
//                    changeState(State.FindTheWhiteLine);
//                }
//                break;
//            case Stopped:
//                if (time - time_at_start <= time_to_stop)
//                {
//                    setMotorsStopped();
//                }
//                else
//                {
//                    changeState(next_state);
//                }
//                break;
//            case Done:
//            default:
//                telemetry.addData("doing", "done");
//                setMotorsStopped();
//                break;
//        }
//        telemetry.addData("front color", frontColor.alpha());
//        telemetry.addData("bottom color", bottomColor.alpha());
//        telemetry.addData("optical distance", range.optical());
//        telemetry.addData("range distance", range.ultraSonic());
//        telemetry.addData("time_to_move", time_to_move);
//        telemetry.addData("time_at_start", time_at_start);
//        telemetry.addData("gyro heading", gyro.getHeading());
//        telemetry.addData("time", time);
//        if(leftSideGuess != null) {
//            telemetry.addData("leftSideGuess", leftSideGuess.toString());
//        } else
//        {
//            telemetry.addData("leftSideGuess", "null");
//        }
//        if(rightSideGuess != null) {
//            telemetry.addData("rightSideGuess", rightSideGuess.toString());
//        }
//        else
//        {
//            telemetry.addData("rightSideGuess", "null");
//        }
//
//        Color frontColor = guessFrontColor();
//        if(frontColor != null) {
//            telemetry.addData("curColorGuess", frontColor.toString());
//        } else
//        {
//            telemetry.addData("curColorGuess", "null");
//        }
//    }
//
//    private Color guessFrontColor() {
//        float red_blue_ratio = frontColor.red() / (float) frontColor.blue();
//        if (red_blue_ratio > 4.0f) {
//            return Color.Red;
//        }
//        float blue_red_ratio = frontColor.blue() / (float) frontColor.red();
//        if (blue_red_ratio > 4.0f) {
//            return Color.Blue;
//        }
//        return null;
//    }
//
//    public boolean robotIsAtAngle(int angle)
//    {
//        return robotIsAtAngle(angle, DEFAULT_ANGLE_THRESHOLD);
//    }
//    public boolean robotIsAtAngle(int angle, int threshold) {
//        // Find the upper and the lower bound
//        int upper = angle + Math.abs(threshold);
//        int lower = angle - Math.abs(threshold);
//
//        // Make sure they are in the proper range
//        upper = upper % 360;
//        lower = lower % 360;
//
//        int heading = gyro.getHeading();
//
//        return lower <= heading && heading <= upper;
//    }
//}
