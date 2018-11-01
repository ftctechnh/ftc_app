/*
package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.hardware.lynx.commands.core.LynxDekaInterfaceCommand;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.IntegratingGyroscope;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.sun.tools.javac.util.Position;

import org.firstinspires.ftc.robotcontroller.external.samples.SensorDigitalTouch;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.KNO3.AutoTransitioner;
import org.firstinspires.ftc.teamcode.gyroPID.DriveMethods;
import org.firstinspires.ftc.teamcode.gyroPID.GyroMethods;
import org.firstinspires.ftc.teamcode.multithreading.UpdatingManager;
import org.firstinspires.ftc.teamcode.vuforia.Vision.vuforia.MasterVuforia;

import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

import javax.crypto.spec.OAEPParameterSpec;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;
import static com.qualcomm.robotcore.util.Range.clip;
import static org.firstinspires.ftc.teamcode.RelicRecoveryRobot_7236.CurrentColumn.CENTER_COLUMN;
import static org.firstinspires.ftc.teamcode.RelicRecoveryRobot_7236.FilledState.NONE_EXTRA;
import static org.firstinspires.ftc.teamcode.RelicRecoveryRobot_7236.GlyphState.IN_INTAKE;
import static org.firstinspires.ftc.teamcode.RelicRecoveryRobot_7236.GlyphState.NONE_IN;
import static org.firstinspires.ftc.teamcode.RelicRecoveryRobot_7236.GlyphState.NONE_IN_AFTER_INTAKE;
import static org.firstinspires.ftc.teamcode.RelicRecoveryRobot_7236.GlyphState.ONE_IN;
import static org.firstinspires.ftc.teamcode.RelicRecoveryRobot_7236.GlyphState.STUCK;
import static org.firstinspires.ftc.teamcode.RelicRecoveryRobot_7236.GlyphState.TWO_IN;


public abstract class RelicRecoveryRobot_7236 extends LinearOpMode implements DriveMethods, GyroMethods {

    final String TELEOP_NAME = "Teleop Program";
    //ac1622ffff

    public LateralGlyphMovement lateralGlyphMovement;

    public UpdatingManager updatingManager;

    public ElapsedTime runTime = new ElapsedTime();
    public ElapsedTime timer1 = new ElapsedTime();
    public boolean autonomous;
    public double power_left;
    public double power_right;
    public long flick_time = 400;

    //          SERVO POSITIONS
    public double left_arm_up = 0.45;//0.03
    public double right_arm_up = 0.55;//1.0
    public double left_arm_down = 1.0;//0.68
    public double right_arm_down = 0.0;//0.4
    public double right_arm_slightly_up = 0.42;
    public double left_arm_slightly_up = 0.51;

    public double left_flick_start = 0.35;
    public double left_flick_moved_front = 1.0;
    public double left_flick_moved_back = 0.0;
    public double left_flick_teleop_position = 0.1;
    public double right_flick_start = 0.66;
    public double right_flick_moved_front = 0.0;
    public double right_flick_moved_back = 1.0;
    public double claw_open = 0.62;//non-rev value is 0.7 // .82
    public double claw_closed = 1.0;//non-rev value is 1.0
    public double lateral_movement_right = 0.0;
    public double right_flick_teleop_position = 0.9;

    public double intake_dropped = 0.9;
    public double intake_start = 0.5;
    public double lateral_movement_left = 1.0;
    public double lateral_movement_center = 0.5;

    public double rightLocation = 0.0;
    public double leftLocation = 0.0;
    public double leftCurrentLocation = 0.5;
    public double rightCurrentLocation = 0.5;
    public double miliseconds = 0.0;
    public double miliseconds2 = 0.0;
    public double lastNumb = 0.0;
    public double lastNumb2 = 0.0;
    public boolean arrivedLeft = false;
    public boolean arrivedRight = false;
    public double amount_forward_intaking = 0;
    public int start_encoder_values_right = 0;
    public int start_encoder_values_left = 0;
    public int distance_forward_encoder_right = 0;
    public int distance_forward_encoder_left = 0;
    public ElapsedTime updater = new ElapsedTime();
    public ElapsedTime updater2 = new ElapsedTime();
    public ElapsedTime outakeTimer = new ElapsedTime();

    public ElapsedTime intake_time_1 = new ElapsedTime();
    public ElapsedTime wait_time_1 = new ElapsedTime();
    public ElapsedTime wait_time_2 = new ElapsedTime();
    public ElapsedTime stuck_timer = new ElapsedTime();
    public ElapsedTime intakeTimer = new ElapsedTime();

    public boolean onePastRange = false;
    public boolean twoPastRange = false;


    //          SPEEDS
    public double normal_speed = 0.4;
    public double new_normal_speed = 0.4;
    public double column_speed = 0.3;
    public double turn_speed = 0.2;
    public double turn_speed2 = 0.1;
    public double slow_speed = 0.3;
    public double slightly_faster = 0.4;
    public double jewel_turn_speed = 0.15;//0.15
    public double jewel_hit_speed = 0.35;

    //          DISTANCES TO COLUMNS
    public int red_front_distance_to_right_column = 460; // DONE 620
    public int red_front_distance_to_center_column = 1005;//DONE 1525
    public int red_front_distance_to_left_column = 1845;//DONE 2435  and 1525

    public int red_front_distance_to_right_column2 = 1365; // DONE 1365 // These values from state morning
    public int red_front_distance_to_center_column2 = 1765;//DONE 1765
    public int red_front_distance_to_left_column2 = 2000;//DONE 2000

    public int red_front_distance_to_right_column3 = 1895; // DONE 1795 // morning of state values
    public int red_front_distance_to_center_column3 = 2325;//DONE 2225
    public int red_front_distance_to_left_column3 = 2810;//DONE 2710

    public int red_front_distance_to_right_column4 = 1193; // DONE 1193 712 less
    public int red_front_distance_to_center_column4 = 1623;//DONE 1623
    public int red_front_distance_to_left_column4 = 2098;//DONE 2108

    public int blue_front_distance_to_right_column = 1845;//DONE 2435
    public int blue_front_distance_to_center_column = 1005;//DONE 1525
    public int blue_front_distance_to_left_column = 460; // DONE 620

    public int blue_front_distance_to_right_column2 = 1998;//DONE 1998
    public int blue_front_distance_to_center_column2 = 1705;//DONE 1400 950
    public int blue_front_distance_to_left_column2 = 1295; // DONE 1255

    public int blue_front_distance_to_right_column3 = 2710; // DONE 1395
    public int blue_front_distance_to_center_column3 = 2105;//DONE 2105
    public int blue_front_distance_to_left_column3 = 1795;//DONE 1795

    public int blue_front_distance_to_right_column4 = 2098; // DONE 2108
    public int blue_front_distance_to_center_column4 = 1623;//DONE 1623
    public int blue_front_distance_to_left_column4 = 1193;//DONE 1193

    public int red_back_distance_to_right_column = 915;// DONE 915
    public int red_back_distance_to_center_column = 1380;// DONE 1380
    public int red_back_distance_to_left_column = 1845;

    public int red_back_distance_to_right_column2 = 915;
    public int red_back_distance_to_center_column2 = 1380;
    public int red_back_distance_to_left_column2 = 1845;

    public int red_back_distance_to_right_column3 = 915;
    public int red_back_distance_to_center_column3 = 1380;
    public int red_back_distance_to_left_column3 = 1845;

    public int blue_back_distance_to_right_column = 1845;
    public int blue_back_distance_to_center_column = 1380;
    public int blue_back_distance_to_left_column = 915;

    public int blue_back_distance_to_right_column2 = 1845;
    public int blue_back_distance_to_center_column2 = 1380;
    public int blue_back_distance_to_left_column2 = 915;

    public int blue_back_distance_to_right_column3 = 1845;
    public int blue_back_distance_to_center_column3 = 1380;
    public int blue_back_distance_to_left_column3 = 0;

    public int jewel_knock_counts = 200;
    public long jewel_wait_time = 400;

    //      ROBOT HARDWARE
    public DcMotor motor_drive_left_front;
    public DcMotor motor_drive_left_back;
    public DcMotor motor_drive_right_front;
    public DcMotor motor_drive_right_back;
    public DcMotor encoder_drive_left;
    public DcMotor encoder_drive_right;
    public DcMotor lift1;
    public DcMotor lift2;
    public DcMotor intake1;
    public DcMotor intake2;
    public Servo lateral1;
    public Servo lateral2;
    public Servo arm_right;
    public Servo arm_left;
    public Servo flipper1;
    public Servo flipper2;
    public Servo flick_left;
    public Servo flick_right;
    public Servo release;
    public Servo claw;
    public Servo relicFlipTop;
    public Servo relicRelease;
    public ColorSensor sensorColorLeft;
    public ColorSensor sensorColorRight;
    public SensorDigitalTouch sensorTouch;
    public ModernRoboticsI2cRangeSensor range;
    public BNO055IMU imu;
    public DigitalChannel bottomBeamBreak;
    public DigitalChannel middleBeamBreak;
    public DigitalChannel topBeamBreak;
    public DistanceSensor cryptoDistanceLeft;
    public DistanceSensor cryptoDistanceRight;
    public float imuStartingPosition;

    Orientation lastAngles = new Orientation();
    double globalAngle, power = .30, correction;

    int imuStartInt = (int) imuStartingPosition;

    public enum GlyphState {
        ONE_IN,
        TWO_IN,
        STUCK,
        IN_INTAKE,
        NONE_IN_AFTER_INTAKE,
        NONE_IN
    }

    enum FilledState {
        NONE_EXTRA,
        LEFT_ONE,
        CENTER_ONE,
        RIGHT_ONE,
        LEFT_TWO,
        CENTER_TWO,
        RIGHT_TWO,
        LEFT_THREE,
        CENTER_THREE,
        RIGHT_THREE
    }

    enum CurrentColumn {
        RIGHT_COLUMN,
        CENTER_COLUMN,
        LEFT_COLUMN
    }

    volatile CurrentColumn activeColumn = CENTER_COLUMN;
    volatile public FilledState filled = NONE_EXTRA;
    volatile public GlyphState current = NONE_IN;
    boolean bottomOpen = false;
    boolean middleOpen = false;
    boolean topOpen = false;
    volatile Set<String> telemetrySet = new LinkedHashSet<>();
    volatile ElapsedTime timer = new ElapsedTime();


    public void powerLeft(double power) {
        motor_drive_left_back.setPower(power);
        motor_drive_left_front.setPower(power);
    }

    public void powerRight(double power) {
        motor_drive_right_back.setPower(power);
        motor_drive_right_front.setPower(power);
    }

    public void initiate(boolean isAutonomous) {
        this.autonomous = isAutonomous;
        updatingManager = new UpdatingManager(this);

        telemetry.addData("Status:", "Robot is Initializing. PLEASE DON'T TOUCH YET!");
        telemetry.update();

        if (isAutonomous) {
            //AutoTransitioner.transitionOnStop(this, TELEOP_NAME);

            telemetry.addData("Status: ", "Initiating Vuforia");
            telemetry.update();
            initiate_vuforia();

            telemetry.addData("Status: ", "Initiating IMU");
            telemetry.update();
            initiate_imu();

            telemetry.addData("IMU starting float:", imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES).firstAngle);
            telemetry.update();
        }

        telemetry.addData("Status: ", "Initiating drive");
        telemetry.update();
        initiate_drive();

        telemetry.addData("Status: ", "Initiating lift");
        telemetry.update();
        initiate_lift();

        telemetry.addData("Status: ", "Initiating intake");
        telemetry.update();
        initiate_intake();

        telemetry.addData("Status: ", "Initiating arms");
        telemetry.update();
        initiate_arms();

        telemetry.addData("Status: ", "Initiating lateral servos");
        telemetry.update();
        initiate_lateral_servos();

        telemetry.addData("Status: ", "Initiating flippers");
        telemetry.update();
        initiate_flippers();

        telemetry.addData("Status: ", "Initiating flickers");
        telemetry.update();
        initiate_flickers();

        telemetry.addData("Status: ", "Initiating color sensor");
        telemetry.update();
        initiate_color_distance();

        telemetry.addData("Status: ", "Initiating beam breaks");
        telemetry.update();
        initiate_beam_breaks();

        telemetry.addData("Status: ", "Initiating release servo");
        telemetry.update();
        initiate_release();

        telemetry.addData("Status: ", "Initiating claw servo");
        telemetry.update();
        initiate_claw();

        telemetry.addData("Status: ", "Initiating relic arm servos");
        telemetry.update();
        initiate_relic_flip();

        telemetry.addData("Status: ", "Initiating Range sensors");
        telemetry.update();
        initiate_range();

        telemetry.addData("Status: ", "Initiating Crypto Distance Sensors");
        telemetry.update();
        initiate_cryptoDistance();

        telemetry.addData("Status", "Initiating Relic Release Servo");
        telemetry.update();
        initiate_relic_release();

        lateralGlyphMovement = new LateralGlyphMovement(this);
        lateralGlyphMovement.init(isAutonomous);

        telemetry.addData("Status: ", "Robot is initiated");
        telemetry.update();
    }


    public void initiate_lateral_servos() {

        lateral1 = hardwareMap.servo.get("lateral1");
        lateral2 = hardwareMap.servo.get("lateral2");

    }

    public void initiate_intake() {

        intake1 = hardwareMap.dcMotor.get("intake1");
        intake2 = hardwareMap.dcMotor.get("intake2");

    }

    public void initiate_imu() {

        telemetry.addLine("Initiating IMU");
        telemetry.update();

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        while (opModeIsActive()) {

            imuStartingPosition = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES).firstAngle;

        }
    }

    public void initiate_vuforia() {

        vuforia = new MasterVuforia(this);
        vuforia.init(VuforiaLocalizer.CameraDirection.BACK);//Boring CAMERA!!!
    }

    public void initiate_drive() {
        motor_drive_left_front = hardwareMap.dcMotor.get("motor_drive_left_front");
        motor_drive_left_back = hardwareMap.dcMotor.get("motor_drive_left_back");
        motor_drive_right_front = hardwareMap.dcMotor.get("motor_drive_right_front");
        motor_drive_right_back = hardwareMap.dcMotor.get("motor_drive_right_back");
        motor_drive_right_back.setDirection(DcMotor.Direction.REVERSE);
        motor_drive_right_front.setDirection(DcMotor.Direction.REVERSE);

        idle();

        motor_drive_left_front.setZeroPowerBehavior(BRAKE);
        motor_drive_left_back.setZeroPowerBehavior(BRAKE);
        motor_drive_right_front.setZeroPowerBehavior(BRAKE);
        motor_drive_right_back.setZeroPowerBehavior(BRAKE);

        idle();

        if (autonomous) {
            motor_drive_left_front.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motor_drive_left_back.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motor_drive_right_front.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motor_drive_right_back.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        } else {
            motor_drive_left_front.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motor_drive_left_back.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motor_drive_right_front.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motor_drive_right_back.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        encoder_drive_left = motor_drive_left_back;
        encoder_drive_right = motor_drive_right_back;

        encoder_drive_left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        encoder_drive_right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void initiate_lift() {

        lift1 = hardwareMap.dcMotor.get("lift1");
        lift2 = hardwareMap.dcMotor.get("lift2");

        lift1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lift2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lift1.setDirection(DcMotor.Direction.FORWARD);
        lift2.setDirection(DcMotor.Direction.FORWARD);

        lift1.setZeroPowerBehavior(BRAKE);
        lift2.setZeroPowerBehavior(BRAKE);

    }

    public void initiate_arms() {

        arm_left = hardwareMap.servo.get("arm_left");
        arm_right = hardwareMap.servo.get("arm_right");


        arm_left.setPosition(left_arm_up);
        arm_right.setPosition(right_arm_up);

    }

    public void initiate_flippers() {
        flipper1 = hardwareMap.servo.get("flipper1");
        flipper2 = hardwareMap.servo.get("flipper2");
    }

    public void initiate_color_distance() {

        sensorColorLeft = hardwareMap.get(ColorSensor.class, "sensorColorLeft");
        sensorColorRight = hardwareMap.get(ColorSensor.class, "sensorColorRight");
        //sensorDistance = hardwareMap.get(DistanceSensor.class, "sensor_color_distance");

    }

    public void initiate_flickers() {
        flick_left = hardwareMap.servo.get("flick_left");
        flick_right = hardwareMap.servo.get("flick_right");
    }

    public void initiate_release() {
        release = hardwareMap.servo.get("release");
        release.setPosition(0.65);
    }

    public void initiate_beam_breaks() {
        bottomBeamBreak = hardwareMap.digitalChannel.get("bottomBeamBreak");
        middleBeamBreak = hardwareMap.digitalChannel.get("middleBeamBreak");
        topBeamBreak = hardwareMap.digitalChannel.get("topBeamBreak");
    }

    public void initiate_claw() {
        claw = hardwareMap.servo.get("claw");
        claw.setPosition(claw_closed);
    }

    public void initiate_relic_flip() {

        relicFlipTop = hardwareMap.servo.get("relicFlipTop");
    }

    public void initiate_range() {
        range = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "range");

    }

    public void initiate_cryptoDistance() {
        cryptoDistanceLeft = hardwareMap.get(DistanceSensor.class, "cryptoDistanceLeft");
        cryptoDistanceRight = hardwareMap.get(DistanceSensor.class, "cryptoDistanceRight");
    }

    public void initiate_relic_release() {
        relicRelease = hardwareMap.servo.get("relicRelease");
    }


    public void move_back_bash(int IMUTarget) {
        driveBackwardDistance_gyro(0.6, 200, IMUTarget, 0.02, 1000);
        driveForwardDistance_gyro(0.6, 300, IMUTarget, 0.02);
        driveBackwardDistance_gyro(0.6, 100, IMUTarget, 0.02, 1000);
    }

    public void switch_left(int distanceBack, int IMUTarget) {
        timer1.reset();
        driveBackwardDistance_gyro(0.9, distanceBack, IMUTarget, 0.02, 800);

        timer.reset();
        strafeBackwardRightGyro_dontCorrect((IMUTarget + (-15)), 0.3);

        driveBackwardDistance(0.9, 410);//410

        strafeBackwardLeftGyro_dontCorrect((IMUTarget - 11), 0.3);

        telemetry.addData("Corrected Left", globalAngle);
        telemetry.addData("Total Time:", timer1.milliseconds());
        telemetry.addData("Turn Time: ", timer.milliseconds());
        telemetry.update();
    }

    public void switch_right(int distanceBack, int IMUTarget) {
        driveBackwardDistance_gyro(0.9, distanceBack, IMUTarget, 0.02, 800);

        strafeBackwardLeftGyro_dontCorrect((IMUTarget + 15), 0.3);

        driveBackwardDistance(0.9, 410);


        strafeBackwardRightGyro_dontCorrect((IMUTarget + 11), 0.3);


        telemetry.addData("Corrected Left", globalAngle);
        telemetry.update();
    }

    public void initial_intake_testing(int IMUAngle, int intake_time, double initialDriveSpeed) {
        //Intake as if there were two glyphs in if the middle one is broken any time in the initial intake sequence
        bottomOpen = bottomBeamBreak.getState();
        middleOpen = middleBeamBreak.getState();
        topOpen = topBeamBreak.getState();

        run_intake_in(0.9);
        run_tracks_up(0.9);
        powerDrive(initialDriveSpeed, initialDriveSpeed);
        intake_time_1.reset();
        start_encoder_values_left = encoder_drive_left.getCurrentPosition();
        start_encoder_values_right = encoder_drive_right.getCurrentPosition();
        while (bottomOpen && intake_time_1.milliseconds() < intake_time && opModeIsActive()) {
            bottomOpen = bottomBeamBreak.getState();//                                              Initial intake of glyph. Drive forward
            update_gyro_straight(IMUAngle, 0.02);
            idle();
        }
        sleep(10);
        distance_forward_encoder_left = (encoder_drive_left.getCurrentPosition() - start_encoder_values_left);
        distance_forward_encoder_right = (encoder_drive_right.getCurrentPosition() - start_encoder_values_right);
        powerDrive(0.0, 0.0);

        telemetry.addData("Initial Intake Sequence Done! - bottom beam break", bottomOpen);
        telemetry.update();

        //sleep(3000);
        telemetry.addData("continuing program!", bottomOpen);
        telemetry.update();
        //sleep(300);

        //TRACKS AND INTAKE ARE STILL RUNNING
    }

    // Gets the glyph into the intake or tries a specified number of times and quits to try another intake sequence
    public void world_initial_intake_sequence(double initialDriveSpeed, int intakeTime, int IMUTarget) {
        bottomOpen = bottomBeamBreak.getState();
        middleOpen = middleBeamBreak.getState();
        topOpen = topBeamBreak.getState();

        run_intake_in(1);
        run_tracks_up(1);
        powerDrive(initialDriveSpeed, initialDriveSpeed);
        intakeTimer.reset();
        start_encoder_values_left = encoder_drive_left.getCurrentPosition();
        start_encoder_values_right = encoder_drive_right.getCurrentPosition();
        while (bottomOpen && intakeTimer.milliseconds() < intakeTime && opModeIsActive()) {
            bottomOpen = bottomBeamBreak.getState();
            update_gyro_straight(IMUTarget, 0.02);
            telemetry.addData("Intake running - waiting for beam break || ", intakeTimer.milliseconds());
            telemetry.update();
            idle();
        }
        distance_forward_encoder_left = (encoder_drive_left.getCurrentPosition() - start_encoder_values_left);
        distance_forward_encoder_right = (encoder_drive_right.getCurrentPosition() - start_encoder_values_right);
        powerDrive(0.0, 0.0);
        stop_intake();
        stop_tracks();

        if (!bottomOpen) {
            current = IN_INTAKE;
        } else current = NONE_IN_AFTER_INTAKE;


    }

    public void intake_sequence_testing(int IMUValue, int intake_time, double initialDriveSpeed) {
        initial_intake_testing(IMUValue, intake_time, initialDriveSpeed);

        powerDrive(0.0, 0.0);
        if (!bottomOpen) {//                                                                        Check if glyph is still in intake
            wait_time_2.reset();
            powerDrive2(0.0, 0.0);
            telemetry.addData("1 Glyph is still in intake - bottom open state", bottomOpen);
            telemetry.update();
            while (!bottomOpen && wait_time_2.milliseconds() < 750 && opModeIsActive()) {
                bottomOpen = bottomBeamBreak.getState();
                idle();
            }

            if (!bottomOpen) {

                driveBackwardDistance(0.9, 100);

                run_intake_out(0.5);//ac1622ffff
                telemetry.addData("2 Glyph slightly stuck... fixing it THIS IS THE START OF THE SLIGHTLY STUCK LOOP", bottomOpen);
                telemetry.update();
                current = STUCK;
                wait_time_2.reset();
                while (wait_time_2.milliseconds() < 495 && opModeIsActive()) {//                            STUCK LOOP
                    telemetry.addData("3 IN STUCK LOOP", bottomOpen);
                    telemetry.update();
                    idle();//                                                                             Re-intake Glyph
                }
                wait_time_2.reset();
                run_intake_in(0.9);

                if (!bottomOpen) {

                    while (!bottomOpen && wait_time_2.milliseconds() < 750 && opModeIsActive()) {
                        bottomOpen = bottomBeamBreak.getState();//
                        telemetry.addData("4 !bottomOpen && < 750 loop", bottomOpen);//               Re-intake sequence
                        telemetry.update();
                        idle();
                    }
                    wait_time_2.reset();
                    while (middleOpen && wait_time_2.milliseconds() < 300 && opModeIsActive()) {
                        telemetry.addData("5 While middleOpen && <300", middleOpen);
                        telemetry.update();
                        middleOpen = middleBeamBreak.getState();
                        idle();//                                                    Check middle open state and wait for that and 300 ms
                    }
                    if (!middleOpen) {
                        telemetry.addData("6 !middleOpen - about to position the glyph in the tracks", middleOpen);
                        telemetry.update();
                        run_tracks_down(0.9);
                        sleep(10);//110
                        stop_tracks();
                        current = ONE_IN;
                    } else {
                        current = NONE_IN_AFTER_INTAKE;
                    }
                } else if (!bottomOpen) {

                    driveBackwardDistance(0.9, 100);

                    run_intake_out(0.5);
                    telemetry.addData("7 Glyph slightly stuck... fixing it intake out", bottomOpen);
                    telemetry.update();
                    //sleep(2000);
                    current = STUCK;
                    wait_time_2.reset();
                    while (wait_time_2.milliseconds() < 475 && opModeIsActive()) {
                        telemetry.addData("8 Fixing stuck - wait loop spitting it out", wait_time_2.milliseconds());
                        telemetry.update();
                        idle();//                                                                             Re-intake Glyph
                    }
                    wait_time_2.reset();
                    run_intake_in(0.9);
                    while (!bottomOpen && wait_time_2.milliseconds() < 750 && opModeIsActive()) {
                        bottomOpen = bottomBeamBreak.getState();
                        telemetry.addData("9 re-intaking while loop", bottomOpen);
                        telemetry.update();
                        idle();
                    }
                    wait_time_2.reset();
                    while (middleOpen && wait_time_2.milliseconds() < 300 && opModeIsActive()) {
                        middleOpen = middleBeamBreak.getState();
                        telemetry.addData("10 re-intaking while loop", bottomOpen);
                        telemetry.update();
                        idle();
                    }
                    if (!middleOpen) {
                        telemetry.addData("11 !middleOpen positioning glyph in tracks", bottomOpen);
                        telemetry.update();
                        run_tracks_down(0.9);
                        sleep(10);//110
                        stop_tracks();
                        current = ONE_IN;
                    } else {
                        current = NONE_IN_AFTER_INTAKE;
                    }
                }

            }


        } else if (current == NONE_IN) {
            current = NONE_IN_AFTER_INTAKE;//                                                 No Glyph in at all... re-run sequence
            //driveBackwardDistance_gyro(0.9, distance_forward_encoder_right, 0,0.02,3000);
            rerun_intake_glyphs1();

            telemetry.addData("12 NONE IN!", current);
            telemetry.update();
            sleep(500);

        } else if (!middleOpen || !topOpen) {
            stop_tracks();
            while (!topOpen && opModeIsActive()) {
                run_tracks_down(1.0);
                topOpen = topBeamBreak.getState();
            }
            stop_tracks();
            telemetry.addData("13 Got it! ONE IN AFTER MIDDLE TOP SEQUENCE", topOpen);
            telemetry.update();
            current = ONE_IN;
        }

        stop_tracks();
        stop_intake();


        telemetry.addData("14 END of that one loop - current", current);
        telemetry.update();


        if (current == NONE_IN || current == NONE_IN_AFTER_INTAKE) {
            telemetry.addData("15 if current NONE_IN  - current", current);
            telemetry.update();
            wait_time_2.reset();
            run_tracks_up(0.9);
            while (middleOpen && wait_time_2.milliseconds() < 900 && opModeIsActive()) {
                telemetry.addData("16 while middle open and < 900 - current", current);
                telemetry.update();
                middleOpen = middleBeamBreak.getState();
                idle();
            }
            if (!middleOpen) {
                telemetry.addData("17 !middle open - current", current);
                telemetry.update();
                //sleep(2000);
                stop_intake();
run_tracks_down(0.6);
                sleep(100);
                stop_tracks();
                current = ONE_IN;
            }
        }

        powerDrive(0.0, 0.0);
        stop_tracks();
        stop_intake();

        if (current == ONE_IN) {
            //                                                                                      RUN INTAKE GLYPHS 2
            telemetry.addData("AWESOME SAUCE! WE GUCHI FAM! ", "YEET");
            telemetry.update();
        }

        telemetry.addData("Current state - end of program",current);
        //                                                                          ENDS WITH ONE_IN OR NONE_IN OR NONE_IN_RE-INTAKE
    }

    public void intake_glphs_1Test(int intake_time, double initialDriveSpeed, double secondaryDriveSpeed) {

        initial_intake_glyph(intake_time, initialDriveSpeed);
        //intake and tracks running
        //stop_tracks(); stop_intake();

        powerDrive(0.0, 0.0);
        if (!bottomOpen) {//                                                                        Check if glyph is still in intake
            wait_time_2.reset();
            powerDrive2(0.0, 0.0);
            telemetry.addData("Glyph is still in intake", bottomOpen);
            while (!bottomOpen && wait_time_2.milliseconds() < 850 && opModeIsActive()) {
                bottomOpen = bottomBeamBreak.getState();
                idle();
            }

            if (!bottomOpen) {
                run_intake_out(0.4);
                telemetry.addData("Glyph slightly stuck... fixing it", bottomOpen);
                current = STUCK;
                wait_time_2.reset();
                while (wait_time_2.milliseconds() < 275 && opModeIsActive()) {
                    idle();//                                                                             Re-intake Glyph
                }
                wait_time_2.reset();
                run_intake_in(0.9);

                if (!bottomOpen) {

                    while (!bottomOpen && wait_time_2.milliseconds() < 750 && opModeIsActive()) {
                        bottomOpen = bottomBeamBreak.getState();
                        idle();
                    }
                    wait_time_2.reset();
                    while (middleOpen && wait_time_2.milliseconds() < 300 && opModeIsActive()) {
                        middleOpen = middleBeamBreak.getState();
                        idle();
                    }
                    if (!middleOpen) {
                        run_tracks_down(0.9);
                        sleep(10);//110
                        stop_tracks();
                        current = ONE_IN;
                    } else {
                        current = NONE_IN_AFTER_INTAKE;
                    }
                } else if (!bottomOpen) {
                    run_intake_out(0.4);
                    telemetry.addData("Glyph slightly stuck... fixing it", bottomOpen);
                    current = STUCK;
                    wait_time_2.reset();
                    while (wait_time_2.milliseconds() < 275 && opModeIsActive()) {
                        idle();//                                                                             Re-intake Glyph
                    }
                    wait_time_2.reset();
                    run_intake_in(0.9);
                    while (!bottomOpen && wait_time_2.milliseconds() < 750 && opModeIsActive()) {
                        bottomOpen = bottomBeamBreak.getState();
                        idle();
                    }
                    wait_time_2.reset();
                    while (middleOpen && wait_time_2.milliseconds() < 300 && opModeIsActive()) {
                        middleOpen = middleBeamBreak.getState();
                        idle();
                    }
                    if (!middleOpen) {
                        run_tracks_down(0.9);
                        sleep(10);
                        stop_tracks();
                        current = ONE_IN;
                    } else {
                        current = NONE_IN_AFTER_INTAKE;
                    }
                }

            }


        } else if (current == NONE_IN) {
            current = NONE_IN_AFTER_INTAKE;//                                                 No Glyph in at all... re-run sequence
            //driveBackwardDistance_gyro(0.9, distance_forward_encoder_right, 0,0.02,3000);
            //rerun_intake_glyphs1();

            telemetry.addData("NONE IN!", current);
            telemetry.update();
            sleep(500);

        } else if (!middleOpen || !topOpen) {
            stop_tracks();
            while (!topOpen && opModeIsActive()) {
                run_tracks_down(1.0);
                topOpen = topBeamBreak.getState();
            }
            stop_tracks();
            telemetry.addData("Got it! N00000 2", topOpen);
            telemetry.update();
            sleep(2000);
            current = ONE_IN;
        }

        stop_tracks();
        stop_intake();


        if (current == NONE_IN || current == NONE_IN_AFTER_INTAKE) {

            wait_time_2.reset();
            run_tracks_up(0.9);
            while (middleOpen && wait_time_2.milliseconds() < 900 && opModeIsActive()) {
                middleOpen = middleBeamBreak.getState();
                idle();
            }
            if (!middleOpen) {
                stop_intake();
run_tracks_down(0.6);
                sleep(100);

                stop_tracks();
                current = ONE_IN;
            }
        }

        powerDrive(0.0, 0.0);
        stop_tracks();
        stop_intake();

        if (current == ONE_IN) {
            //                                                                                      RUN INTAKE GLYPHS 2
            telemetry.addData("AWESOME SAUCE! WE GUCHI FAM! ", "YEET");
            telemetry.update();
        }


        //                                                                          ENDS WITH ONE_IN OR NONE_IN OR NONE_IN_RE-INTAKE

    }

    public void intake_glyphs_2Test(int imuValue, int intake_time, double initialDriveSpeed, double secondaryDriveSpeed) {
        initial_intake_glyph_ONE_IN(imuValue, intake_time, initialDriveSpeed);
        //intake running

        powerDrive(0.0, 0.0);
        if (!bottomOpen) {//                                                                        Check if glyph is still in intake
            telemetry.addLine("1");
            telemetry.update();
            wait_time_2.reset();
            powerDrive2(0.0, 0.0);
            run_tracks_up(0.9);
            telemetry.addData("Glyph is still in intake", bottomOpen);
            telemetry.update();
            while (!bottomOpen && wait_time_2.milliseconds() < 800 && topOpen && opModeIsActive()) {
                telemetry.addLine("2");
                telemetry.update();
                topOpen = topBeamBreak.getState();
                bottomOpen = bottomBeamBreak.getState();
                idle();
            }

            whoopsAlmostDumpedTheGlyphOutTheTracks();

            wait_time_2.reset();
            if (!bottomOpen) {
                telemetry.addLine("3");
                telemetry.update();
                run_tracks_up(0.9);
                while (!bottomOpen && wait_time_2.milliseconds() < 450 && topOpen && opModeIsActive()) {
                    telemetry.addLine("4");
                    telemetry.update();
                    topOpen = topBeamBreak.getState();
                    bottomOpen = bottomBeamBreak.getState();
                    idle();
                }
                whoopsAlmostDumpedTheGlyphOutTheTracks();
            }


            if (!bottomOpen) {
                telemetry.addLine("5");
                telemetry.update();
                run_intake_out(0.5);
                telemetry.addData("Glyph slightly stuck... fixing it", bottomOpen);
                telemetry.update();
                current = STUCK;
                wait_time_2.reset();
                while (wait_time_2.milliseconds() < 475 && opModeIsActive()) {
                    idle();//                                                                             Re-intake Glyph
                }
                wait_time_2.reset();
                run_intake_in(0.9);
                run_tracks_up(0.9);
                sleep(100);
                if (!bottomOpen) {
                    telemetry.addLine("6");
                    telemetry.update();
                    while (!bottomOpen && topOpen && wait_time_2.milliseconds() < 750 && opModeIsActive()) {
                        bottomOpen = bottomBeamBreak.getState();
                        topOpen = topBeamBreak.getState();
                        idle();
                    }
                }

                whoopsAlmostDumpedTheGlyphOutTheTracks();

                wait_time_2.reset();
                if (!bottomOpen) {
                    telemetry.addLine("7");
                    telemetry.update();
                    run_tracks_up(0.9);
                    while (!bottomOpen && wait_time_2.milliseconds() < 250 && topOpen && opModeIsActive()) {
                        topOpen = topBeamBreak.getState();
                        bottomOpen = bottomBeamBreak.getState();
                        idle();
                    }

                    whoopsAlmostDumpedTheGlyphOutTheTracks();
                }

  //run_tracks_up(0.8);
                    wait_time_2.reset();
                    while (middleOpen && topOpen && wait_time_2.milliseconds() < 300 && opModeIsActive()) {
                        telemetry.addLine("8");
                        telemetry.update();
                        middleOpen = middleBeamBreak.getState();
                        topOpen = topBeamBreak.getState();
                        idle();
                    }


                if (!bottomOpen) {
                    telemetry.addLine("9");
                    telemetry.update();
                    run_intake_out(0.5);
                    telemetry.addData("Glyph slightly stuck... fixing it", bottomOpen);
                    current = STUCK;
                    wait_time_2.reset();
                    while (wait_time_2.milliseconds() < 475 && opModeIsActive()) {
                        idle();//                                                                             Re-intake Glyph
                    }
                    wait_time_2.reset();
                    run_intake_in(0.9);
                    run_tracks_up(0.9);
                    sleep(100);
                    while (!bottomOpen && topOpen && wait_time_2.milliseconds() < 900 && opModeIsActive()) {
                        topOpen = topBeamBreak.getState();
                        bottomOpen = bottomBeamBreak.getState();
                        idle();
                    }
                    whoopsAlmostDumpedTheGlyphOutTheTracks();
                    wait_time_2.reset();

                    if (bottomOpen) {
                        telemetry.addLine("10");
                        telemetry.update();
                        current = TWO_IN;
                    } else {
                        current = ONE_IN;
                    }

                    if (!bottomOpen) {
                        telemetry.addData("10.5", bottomOpen);
                        telemetry.update();
                        wait_time_2.reset();
                        run_intake_in(1.0);
                        run_tracks_up(0.9);
                        while (!bottomOpen && topOpen && wait_time_2.milliseconds() < 900 && opModeIsActive()) {
                            bottomOpen = bottomBeamBreak.getState();
                            topOpen = topBeamBreak.getState();
                            idle();
                        }
                        whoopsAlmostDumpedTheGlyphOutTheTracks();
                        if (!bottomOpen) {
                            current = ONE_IN;
                        }
                        stop_intake();
                        stop_tracks();

                    }
                }

            }

        } else if (current == NONE_IN || current == NONE_IN_AFTER_INTAKE || current == ONE_IN) {
            telemetry.addLine("11");
            telemetry.update();
            current = ONE_IN;//                                                 No Glyph in at all... re-run sequence
            reRunONE_INSequence(imuValue, 3000, 0);

            telemetry.addData("ONE IN!", current);
            telemetry.update();
            //sleep(500);

        } else
if (!middleOpen || !topOpen)
 {
            telemetry.addLine("12");
            telemetry.update();
            stop_tracks();
            whoopsAlmostDumpedTheGlyphOutTheTracks();
            telemetry.addData("Got it! ", topOpen);
            telemetry.update();
            //sleep(2000);
            current = TWO_IN;
        }

        stop_tracks();
        stop_intake();


        if (current == NONE_IN || current == NONE_IN_AFTER_INTAKE || current == ONE_IN) {
            telemetry.addLine("13");
            telemetry.update();
            wait_time_2.reset();
            run_tracks_up(0.9);
            while (middleOpen && wait_time_2.milliseconds() < 900 && opModeIsActive()) {
                middleOpen = middleBeamBreak.getState();
                idle();
            }
            if (!bottomOpen) {
                stop_intake();
                stop_tracks();
                super_stuck_loop(0, imuValue);

                reRunONE_INSequence(imuValue, 3000, 0);
                stop_tracks();
                current = ONE_IN;
            }
        }

        powerDrive(0.0, 0.0);
        stop_tracks();
        stop_intake();
    }

    public void intake_glyphs_2TestOOF(int imuValue, int intake_time, double initialDriveSpeed, double secondaryDriveSpeed) {
        initial_intake_glyph_ONE_IN(imuValue, intake_time, initialDriveSpeed);
        //intake running

        powerDrive(0.0, 0.0);
        if (!bottomOpen) {//                                                                        Check if glyph is still in intake
            telemetry.addLine("1");
            telemetry.update();
            wait_time_2.reset();
            powerDrive2(0.0, 0.0);
            run_tracks_up(0.9);
            telemetry.addData("Glyph is still in intake", bottomOpen);
            telemetry.update();
            while (!bottomOpen && wait_time_2.milliseconds() < 800 && topOpen && opModeIsActive()) {
                telemetry.addLine("2");
                telemetry.update();
                topOpen = topBeamBreak.getState();
                bottomOpen = bottomBeamBreak.getState();
                idle();
            }

            whoopsAlmostDumpedTheGlyphOutTheTracks();

            wait_time_2.reset();
            if (!bottomOpen) {
                telemetry.addLine("3");
                telemetry.update();
                run_tracks_up(0.9);
                while (!bottomOpen && wait_time_2.milliseconds() < 450 && topOpen && opModeIsActive()) {
                    telemetry.addLine("4");
                    telemetry.update();
                    topOpen = topBeamBreak.getState();
                    bottomOpen = bottomBeamBreak.getState();
                    idle();
                }
                whoopsAlmostDumpedTheGlyphOutTheTracks();
            }


            if (!bottomOpen) {
                telemetry.addLine("5");
                telemetry.update();
                run_intake_out(0.4);
                telemetry.addData("Glyph slightly stuck... fixing it", bottomOpen);
                telemetry.update();
                current = STUCK;
                wait_time_2.reset();
                while (wait_time_2.milliseconds() < 275 && opModeIsActive()) {
                    idle();//                                                                             Re-intake Glyph
                }
                wait_time_2.reset();
                run_intake_in(0.9);
                run_tracks_up(0.9);
                sleep(100);
                if (!bottomOpen) {
                    telemetry.addLine("6");
                    telemetry.update();
                    while (!bottomOpen && topOpen && wait_time_2.milliseconds() < 750 && opModeIsActive()) {
                        bottomOpen = bottomBeamBreak.getState();
                        topOpen = topBeamBreak.getState();
                        idle();
                    }
                }

                whoopsAlmostDumpedTheGlyphOutTheTracks();

                wait_time_2.reset();
                if (!bottomOpen) {
                    telemetry.addLine("7");
                    telemetry.update();
                    run_tracks_up(0.9);
                    while (!bottomOpen && wait_time_2.milliseconds() < 250 && topOpen && opModeIsActive()) {
                        topOpen = topBeamBreak.getState();
                        bottomOpen = bottomBeamBreak.getState();
                        idle();
                    }

                    whoopsAlmostDumpedTheGlyphOutTheTracks();
                }

  //run_tracks_up(0.8);
                    wait_time_2.reset();
                    while (middleOpen && topOpen && wait_time_2.milliseconds() < 300 && opModeIsActive()) {
                        telemetry.addLine("8");
                        telemetry.update();
                        middleOpen = middleBeamBreak.getState();
                        topOpen = topBeamBreak.getState();
                        idle();
                    }


                if (!bottomOpen) {
                    telemetry.addLine("9");
                    telemetry.update();
                    run_intake_out(0.4);
                    telemetry.addData("Glyph slightly stuck... fixing it", bottomOpen);
                    current = STUCK;
                    wait_time_2.reset();
                    while (wait_time_2.milliseconds() < 205 && opModeIsActive()) {
                        idle();//                                                                             Re-intake Glyph
                    }
                    wait_time_2.reset();
                    run_intake_in(0.9);
                    run_tracks_up(0.9);
                    sleep(100);
                    while (!bottomOpen && topOpen && wait_time_2.milliseconds() < 900 && opModeIsActive()) {
                        topOpen = topBeamBreak.getState();
                        bottomOpen = bottomBeamBreak.getState();
                        idle();
                    }
                    whoopsAlmostDumpedTheGlyphOutTheTracks();
                    wait_time_2.reset();

                    if (bottomOpen) {
                        telemetry.addLine("10");
                        telemetry.update();
                        current = ONE_IN;
                    } else {
                        current = NONE_IN;
                    }

                    if (!bottomOpen) {
                        telemetry.addData("10.5", bottomOpen);
                        telemetry.update();
                        wait_time_2.reset();
                        run_intake_in(1.0);
                        run_tracks_up(0.9);
                        while (!bottomOpen && topOpen && wait_time_2.milliseconds() < 900 && opModeIsActive()) {
                            bottomOpen = bottomBeamBreak.getState();
                            topOpen = topBeamBreak.getState();
                            idle();
                        }
                        whoopsAlmostDumpedTheGlyphOutTheTracks();
                        if (!bottomOpen) {
                            current = ONE_IN;
                        }
                        stop_intake();
                        stop_tracks();

                    }
                }

            }


        } else if (current == NONE_IN || current == NONE_IN_AFTER_INTAKE) {
            telemetry.addLine("11");
            telemetry.update();
            current = NONE_IN;//                                                 No Glyph in at all... re-run sequence
            reRunONE_INSequenceOOF(0, 3000, 0);

            telemetry.addData("ONE IN!", current);
            telemetry.update();
            //sleep(500);

        } else
if (!middleOpen || !topOpen)
 {
            telemetry.addLine("12");
            telemetry.update();
            stop_tracks();
            whoopsAlmostDumpedTheGlyphOutTheTracks();
            telemetry.addData("Got it! ", topOpen);
            telemetry.update();
            current = ONE_IN;
        }

        stop_tracks();
        stop_intake();


        if (current == NONE_IN || current == NONE_IN_AFTER_INTAKE) {
            telemetry.addLine("13");
            telemetry.update();
            wait_time_2.reset();
            run_tracks_up(0.9);
            while (middleOpen && wait_time_2.milliseconds() < 900 && opModeIsActive()) {
                middleOpen = middleBeamBreak.getState();
                idle();
            }
            if (!bottomOpen) {
                stop_intake();
                stop_tracks();
                super_stuck_loop(0, 0);

                reRunONE_INSequenceOOF(0, 3000, 0);
                stop_tracks();
                current = NONE_IN;
            }
        }

        powerDrive(0.0, 0.0);
        stop_tracks();
        stop_intake();
    }


    public void reRunONE_INSequenceOOF(int IMUTarget, int intake_time, double initialDriveSpeed) {
        driveBackwardDistance_gyro(0.9, 100, IMUTarget, 0.02, 500);
        initial_intake_glyph_ONE_IN(IMUTarget, (intake_time - 1200), initialDriveSpeed);

        powerDrive(0.0, 0.0);
        if (!bottomOpen) {//                                                                        Check if glyph is still in intake
            telemetry.addLine("1");
            telemetry.update();
            wait_time_2.reset();
            powerDrive2(0.0, 0.0);
            run_tracks_up(0.9);
            telemetry.addData("Glyph is still in intake", bottomOpen);
            telemetry.update();
            while (!bottomOpen && wait_time_2.milliseconds() < 800 && topOpen && opModeIsActive()) {
                telemetry.addLine("2");
                telemetry.update();
                topOpen = topBeamBreak.getState();
                bottomOpen = bottomBeamBreak.getState();
                idle();
            }

            whoopsAlmostDumpedTheGlyphOutTheTracks();

            wait_time_2.reset();
            if (!bottomOpen) {
                telemetry.addLine("3");
                telemetry.update();
                run_tracks_up(0.9);
                while (!bottomOpen && wait_time_2.milliseconds() < 450 && topOpen && opModeIsActive()) {
                    telemetry.addLine("4");
                    telemetry.update();
                    topOpen = topBeamBreak.getState();
                    bottomOpen = bottomBeamBreak.getState();
                    idle();
                }
                whoopsAlmostDumpedTheGlyphOutTheTracks();
            }


            if (!bottomOpen) {
                telemetry.addLine("5");
                telemetry.update();
                run_intake_out(0.5);
                telemetry.addData("Glyph slightly stuck... fixing it", bottomOpen);
                telemetry.update();
                current = STUCK;
                wait_time_2.reset();
                while (wait_time_2.milliseconds() < 475 && opModeIsActive()) {
                    idle();//                                                                             Re-intake Glyph
                }
                wait_time_2.reset();
                run_intake_in(0.9);
                run_tracks_up(0.9);
                sleep(100);
                if (!bottomOpen) {
                    telemetry.addLine("6");
                    telemetry.update();
                    while (!bottomOpen && topOpen && wait_time_2.milliseconds() < 750 && opModeIsActive()) {
                        bottomOpen = bottomBeamBreak.getState();
                        topOpen = topBeamBreak.getState();
                        idle();
                    }
                }

                whoopsAlmostDumpedTheGlyphOutTheTracks();

                wait_time_2.reset();
                if (!bottomOpen) {
                    telemetry.addLine("7");
                    telemetry.update();
                    run_tracks_up(0.9);
                    while (!bottomOpen && wait_time_2.milliseconds() < 250 && topOpen && opModeIsActive()) {
                        topOpen = topBeamBreak.getState();
                        bottomOpen = bottomBeamBreak.getState();
                        idle();
                    }

                    whoopsAlmostDumpedTheGlyphOutTheTracks();
                }


                if (!bottomOpen) {
                    telemetry.addLine("9");
                    telemetry.update();
                    run_intake_out(0.5);
                    telemetry.addData("Glyph slightly stuck... fixing it", bottomOpen);
                    current = STUCK;
                    wait_time_2.reset();
                    while (wait_time_2.milliseconds() < 475 && opModeIsActive()) {
                        idle();//                                                                             Re-intake Glyph
                    }
                    wait_time_2.reset();
                    run_intake_in(0.9);
                    run_tracks_up(0.9);
                    sleep(100);
                    while (!bottomOpen && topOpen && wait_time_2.milliseconds() < 900 && opModeIsActive()) {
                        topOpen = topBeamBreak.getState();
                        bottomOpen = bottomBeamBreak.getState();
                        idle();
                    }
                    whoopsAlmostDumpedTheGlyphOutTheTracks();
                    wait_time_2.reset();

                    if (bottomOpen) {
                        telemetry.addLine("10");
                        telemetry.update();
                        current = ONE_IN;
                    } else {
                        current = NONE_IN;
                    }

                    if (!bottomOpen) {
                        telemetry.addData("10.5", bottomOpen);
                        telemetry.update();
                        wait_time_2.reset();
                        run_intake_in(1.0);
                        run_tracks_up(0.9);
                        while (!bottomOpen && topOpen && wait_time_2.milliseconds() < 900 && opModeIsActive()) {
                            bottomOpen = bottomBeamBreak.getState();
                            topOpen = topBeamBreak.getState();
                            idle();
                        }
                        whoopsAlmostDumpedTheGlyphOutTheTracks();
                        if (!bottomOpen) {
                            current = NONE_IN;
                        }
                        stop_intake();
                        stop_tracks();

                    }
                }

            }


        } else if (current == NONE_IN || current == NONE_IN_AFTER_INTAKE) {
            telemetry.addLine("11");
            telemetry.update();
            current = NONE_IN;//                                                 No Glyph in at all... re-run sequence
            //driveBackwardDistance_gyro(0.9, distance_forward_encoder_right, 0,0.02,3000);
            //rerun_intake_glyphs1();

            telemetry.addData("ONE IN!", current);
            telemetry.update();
            //sleep(500);

        } else
if (!middleOpen || !topOpen)
 {
            telemetry.addLine("12");
            telemetry.update();
            stop_tracks();
            whoopsAlmostDumpedTheGlyphOutTheTracks();
            telemetry.addData("Got it! ", topOpen);
            telemetry.update();
            current = ONE_IN;
        }

        stop_tracks();
        stop_intake();


        if (current == NONE_IN || current == NONE_IN_AFTER_INTAKE) {
            telemetry.addLine("13");
            telemetry.update();
            wait_time_2.reset();
            run_tracks_up(0.9);
            while (middleOpen && wait_time_2.milliseconds() < 900 && opModeIsActive()) {
                middleOpen = middleBeamBreak.getState();
                idle();
            }
            if (!middleOpen) {
                stop_intake();
                stop_tracks();
                current = NONE_IN;
            }
        }

        powerDrive(0.0, 0.0);
        stop_tracks();
        stop_intake();
    }

    public void reRunONE_INSequence(int IMUTarget, int intake_time, double initialDriveSpeed) {
        driveBackwardDistance_gyro(0.9, 100, IMUTarget, 0.02, 500);
        initial_intake_glyph_ONE_IN(IMUTarget, (intake_time - 1200), initialDriveSpeed);

        powerDrive(0.0, 0.0);
        if (!bottomOpen) {//                                                                        Check if glyph is still in intake
            telemetry.addLine("1");
            telemetry.update();
            wait_time_2.reset();
            powerDrive2(0.0, 0.0);
            run_tracks_up(0.9);
            telemetry.addData("Glyph is still in intake", bottomOpen);
            telemetry.update();
            while (!bottomOpen && wait_time_2.milliseconds() < 800 && topOpen && opModeIsActive()) {
                telemetry.addLine("2");
                telemetry.update();
                topOpen = topBeamBreak.getState();
                bottomOpen = bottomBeamBreak.getState();
                idle();
            }

            whoopsAlmostDumpedTheGlyphOutTheTracks();

            wait_time_2.reset();
            if (!bottomOpen) {
                telemetry.addLine("3");
                telemetry.update();
                run_tracks_up(0.9);
                while (!bottomOpen && wait_time_2.milliseconds() < 450 && topOpen && opModeIsActive()) {
                    telemetry.addLine("4");
                    telemetry.update();
                    topOpen = topBeamBreak.getState();
                    bottomOpen = bottomBeamBreak.getState();
                    idle();
                }
                whoopsAlmostDumpedTheGlyphOutTheTracks();
            }


            if (!bottomOpen) {
                telemetry.addLine("5");
                telemetry.update();
                run_intake_out(0.5);
                telemetry.addData("Glyph slightly stuck... fixing it", bottomOpen);
                telemetry.update();
                current = STUCK;
                wait_time_2.reset();
                while (wait_time_2.milliseconds() < 475 && opModeIsActive()) {
                    idle();//                                                                             Re-intake Glyph
                }
                wait_time_2.reset();
                run_intake_in(0.9);
                run_tracks_up(0.9);
                sleep(100);
                if (!bottomOpen) {
                    telemetry.addLine("6");
                    telemetry.update();
                    while (!bottomOpen && topOpen && wait_time_2.milliseconds() < 750 && opModeIsActive()) {
                        bottomOpen = bottomBeamBreak.getState();
                        topOpen = topBeamBreak.getState();
                        idle();
                    }
                }

                whoopsAlmostDumpedTheGlyphOutTheTracks();

                wait_time_2.reset();
                if (!bottomOpen) {
                    telemetry.addLine("7");
                    telemetry.update();
                    run_tracks_up(0.9);
                    while (!bottomOpen && wait_time_2.milliseconds() < 250 && topOpen && opModeIsActive()) {
                        topOpen = topBeamBreak.getState();
                        bottomOpen = bottomBeamBreak.getState();
                        idle();
                    }

                    whoopsAlmostDumpedTheGlyphOutTheTracks();
                }


                if (!bottomOpen) {
                    telemetry.addLine("9");
                    telemetry.update();
                    run_intake_out(0.5);
                    telemetry.addData("Glyph slightly stuck... fixing it", bottomOpen);
                    current = STUCK;
                    wait_time_2.reset();
                    while (wait_time_2.milliseconds() < 475 && opModeIsActive()) {
                        idle();//                                                                             Re-intake Glyph
                    }
                    wait_time_2.reset();
                    run_intake_in(0.9);
                    run_tracks_up(0.9);
                    sleep(100);
                    while (!bottomOpen && topOpen && wait_time_2.milliseconds() < 900 && opModeIsActive()) {
                        topOpen = topBeamBreak.getState();
                        bottomOpen = bottomBeamBreak.getState();
                        idle();
                    }
                    whoopsAlmostDumpedTheGlyphOutTheTracks();
                    wait_time_2.reset();

                    if (bottomOpen) {
                        telemetry.addLine("10");
                        telemetry.update();
                        current = TWO_IN;
                    } else {
                        current = ONE_IN;
                    }

                    if (!bottomOpen) {
                        telemetry.addData("10.5", bottomOpen);
                        telemetry.update();
                        wait_time_2.reset();
                        run_intake_in(1.0);
                        run_tracks_up(0.9);
                        while (!bottomOpen && topOpen && wait_time_2.milliseconds() < 900 && opModeIsActive()) {
                            bottomOpen = bottomBeamBreak.getState();
                            topOpen = topBeamBreak.getState();
                            idle();
                        }
                        whoopsAlmostDumpedTheGlyphOutTheTracks();
                        if (!bottomOpen) {
                            current = ONE_IN;
                        }
                        stop_intake();
                        stop_tracks();

                    }
                }

            }


        } else if (current == NONE_IN || current == NONE_IN_AFTER_INTAKE) {
            telemetry.addLine("11");
            telemetry.update();
            current = ONE_IN;//                                                 No Glyph in at all... re-run sequence
            //driveBackwardDistance_gyro(0.9, distance_forward_encoder_right, 0,0.02,3000);
            //rerun_intake_glyphs1();

            telemetry.addData("ONE IN!", current);
            telemetry.update();
            //sleep(500);

        } else
if (!middleOpen || !topOpen)
 {
            telemetry.addLine("12");
            telemetry.update();
            stop_tracks();
            whoopsAlmostDumpedTheGlyphOutTheTracks();
            telemetry.addData("Got it! ", topOpen);
            telemetry.update();
            current = TWO_IN;
        }

        stop_tracks();
        stop_intake();


        if (current == NONE_IN || current == NONE_IN_AFTER_INTAKE) {
            telemetry.addLine("13");
            telemetry.update();
            wait_time_2.reset();
            run_tracks_up(0.9);
            while (middleOpen && wait_time_2.milliseconds() < 900 && opModeIsActive()) {
                middleOpen = middleBeamBreak.getState();
                idle();
            }
            if (!middleOpen) {
                stop_intake();
                stop_tracks();
                current = ONE_IN;
            }
        }

        powerDrive(0.0, 0.0);
        stop_tracks();
        stop_intake();
    }

    public void whoopsAlmostDumpedTheGlyphOutTheTracks() {
        topOpen = topBeamBreak.getState();
        if (!topOpen) {
            stop_intake();
            run_tracks_down(0.9);
            ElapsedTime stop = new ElapsedTime();
            stop.reset();
            while (stop.milliseconds() < 320 && opModeIsActive()) {
                middleOpen = middleBeamBreak.getState();
                telemetry.addData("Whoops! Almost dumped the glyph out the back!", "Running it down!");
                telemetry.update();
            }
            stop_tracks();
        }
    }

    public void initial_intake_glyph(int intake_time2, double initialDriveSpeed2) {
        bottomOpen = bottomBeamBreak.getState();
        middleOpen = middleBeamBreak.getState();
        topOpen = topBeamBreak.getState();

        run_intake_in(0.9);
        run_tracks_up(0.9);
        powerDrive(initialDriveSpeed2, initialDriveSpeed2);
        intake_time_1.reset();
        start_encoder_values_left = encoder_drive_left.getCurrentPosition();
        start_encoder_values_right = encoder_drive_right.getCurrentPosition();
        while (bottomOpen && intake_time_1.milliseconds() < intake_time2 && opModeIsActive()) {
            bottomOpen = bottomBeamBreak.getState();//                                              Initial intake of glyph. Drive forward
            update_gyro_straight(0, 0.02);
            idle();
        }
        distance_forward_encoder_left = (encoder_drive_left.getCurrentPosition() - start_encoder_values_left);
        distance_forward_encoder_right = (encoder_drive_right.getCurrentPosition() - start_encoder_values_right);
        powerDrive(0.0, 0.0);

        //TRACKS AND INTAKE ARE STILL RUNNING

    }

    public void position_first_glyph_in_tracks() {
        wait_time_2.reset();
        while (middleOpen && wait_time_2.milliseconds() < 400 && opModeIsActive()) {
            middleOpen = middleBeamBreak.getState();
            idle();
        }
        wait_time_2.reset();
        run_tracks_down(0.3);
        while (wait_time_2.milliseconds() < 150 && opModeIsActive()) {
            idle();
        }
    }

    public void move_glyph_to_tracks() {
        powerDrive(0.0, 0.0);
        if (!bottomOpen) {//                                                                        Check if glyph is still in intake
            wait_time_2.reset();
            powerDrive2(0.0, 0.0);
            telemetry.addData("Glyph is still in intake", bottomOpen);
            while (!bottomOpen && wait_time_2.milliseconds() < 350 && opModeIsActive()) {
                bottomOpen = bottomBeamBreak.getState();
                idle();
            }
            if (!bottomOpen) {
                run_intake_out(0.4);
                telemetry.addData("Glyph slightly stuck... fixing it", bottomOpen);
                current = STUCK;
                wait_time_2.reset();
                while (wait_time_2.milliseconds() < 275 && opModeIsActive()) {
                    idle();//                                                                             Re-intake Glyph
                }
                wait_time_2.reset();
                run_intake_in(0.9);
                while (wait_time_2.milliseconds() < 100 && opModeIsActive()) {
                    idle();
                }


                if (!bottomOpen) {//                                                                        Check if glyph is still in intake
                    wait_time_2.reset();
                    powerDrive2(0.0, 0.0);
                    telemetry.addData("Glyph is still in intake", bottomOpen);
                    while (!bottomOpen && wait_time_2.milliseconds() < 350 && opModeIsActive()) {
                        bottomOpen = bottomBeamBreak.getState();
                        idle();
                    }

                }

            } else if (bottomOpen) {
                telemetry.addData("Glyph is in tracks", bottomOpen);//                              If Glyph is in tracks

                position_first_glyph_in_tracks();
                current = ONE_IN;//                                                                    Update current to be ONE_IN

            } else if (current == NONE_IN) {
                current = NONE_IN_AFTER_INTAKE;//                                                 No Glyph in at all... re-run sequence
                driveBackwardDistance_gyro(0.9, distance_forward_encoder_right, 0, 0.02, 3000);
                rerun_intake_glyphs1();

            } else if (!middleOpen) {
                stop_tracks();
                while (!middleOpen && opModeIsActive()) {
                    run_tracks_down(1.0);
                    middleOpen = middleBeamBreak.getState();
                }
                stop_tracks();
                telemetry.addData("Got it!", topOpen);
                telemetry.update();
                current = ONE_IN;
            } else {
                current = NONE_IN_AFTER_INTAKE;
            }

            if (current == NONE_IN_AFTER_INTAKE || current == NONE_IN) {
                rerun_intake_glyphs1();//                                                               Rerun the sequence
            }
            stop_tracks();
            stop_intake();
        }

    }

    public void initial_intake_glyph_ONE_IN(int IMUValue, int intake_time, double initialDriveSpeed) {
        bottomOpen = bottomBeamBreak.getState();
        middleOpen = middleBeamBreak.getState();
        topOpen = topBeamBreak.getState();

        run_intake_in(0.9);
        powerDrive(initialDriveSpeed, initialDriveSpeed);
        intake_time_1.reset();
        start_encoder_values_left = encoder_drive_left.getCurrentPosition();
        start_encoder_values_right = encoder_drive_right.getCurrentPosition();
        while (bottomOpen && intake_time_1.milliseconds() < intake_time && opModeIsActive()) {
            bottomOpen = bottomBeamBreak.getState();//                                           Initial intake of glyph. Drive forward
            update_gyro_straight(IMUValue, 0.02);
            idle();
        }
        sleep(300);
        distance_forward_encoder_left = (encoder_drive_left.getCurrentPosition() - start_encoder_values_left);
        distance_forward_encoder_right = (encoder_drive_right.getCurrentPosition() - start_encoder_values_right);
        powerDrive(0.0, 0.0);

        //INTAKE IS STILL RUNNING
    }

    public void wiggle_left_testing(double speed, double correction_speed, int angle) {
        double final_intake_time = 0.0;

        powerDrive(0.0, speed);
        while (timer1.milliseconds() < 2000 && opModeIsActive()) {
            idle();
        }
        powerDrive(0.0, 0.0);

        strafeBackwardRightGyro_dontCorrect(angle, speed);

        //strafeBackwardRightGyro(angle, speed, correction_speed, 100);

        timer1.reset();
        powerDrive(0.0, speed);


        final_intake_time = timer1.milliseconds();
        powerDrive(0.0, -speed);
        timer1.reset();
        while ((final_intake_time +100) > timer1.milliseconds() && globalAngle < angle && opModeIsActive()) {
            idle();
        }
        powerDrive(0.0,0.0);


    }

    public void wiggle_right_testing(double speed, double correction_speed, int angle) {
        double final_intake_time = 0.0;

        powerDrive(speed, 0.0);
        while (timer1.milliseconds() < 2000 && opModeIsActive()) {
            idle();
        }
        powerDrive(0.0, 0.0);

        strafeBackwardLeftGyro_dontCorrect(angle, speed);

        //strafeBackwardLeftGyro(angle, speed, correction_speed, 100);

        timer1.reset();
        powerDrive(0.0, speed);


        final_intake_time = timer1.milliseconds();
        powerDrive(0.0, -speed);
        timer1.reset();
        while ((final_intake_time +100) > timer1.milliseconds() && globalAngle < angle && opModeIsActive()) {
            idle();
        }
        powerDrive(0.0,0.0);


    }

    public void initial_intake_glyph_turn_left(int intake_time3, double initialDriveSpeed3, int startAngle) {
        bottomOpen = bottomBeamBreak.getState();
        middleOpen = middleBeamBreak.getState();
        topOpen = topBeamBreak.getState();
        double final_intake_time = 0.0;

        run_intake_in(0.9);
        run_tracks_up(0.9);
        powerDrive(0.0, initialDriveSpeed3);
        intake_time_1.reset();
        timer1.reset();
        while (bottomOpen && intake_time_1.milliseconds() < intake_time3 && opModeIsActive()) {
            bottomOpen = bottomBeamBreak.getState();//                                              Initial intake of glyph. Drive forward
            idle();
        }

        final_intake_time = timer1.milliseconds();
        powerDrive(0.0, -initialDriveSpeed3);
        while (startAngle < globalAngle && opModeIsActive()) {
            idle();
        }
        //center_to_IMU_decrease_speed();
        powerDrive(0.0, 0.0);

        //TRACKS AND INTAKE ARE STILL RUNNING
    }

    public void initial_intake_glyph_turn_right(int intake_time3, double initialDriveSpeed3, int startAngle) {
        bottomOpen = bottomBeamBreak.getState();
        middleOpen = middleBeamBreak.getState();
        topOpen = topBeamBreak.getState();
        double final_intake_time = 0.0;

        run_intake_in(0.9);
        run_tracks_up(0.9);
        powerDrive(initialDriveSpeed3, 0.0);
        intake_time_1.reset();
        timer1.reset();
        while (bottomOpen && intake_time_1.milliseconds() < intake_time3 && opModeIsActive()) {
            bottomOpen = bottomBeamBreak.getState();//                                              Initial intake of glyph. Drive forward
            idle();
        }

        final_intake_time = timer1.milliseconds();
        powerDrive(-initialDriveSpeed3, 0.0);
        while (startAngle < globalAngle && opModeIsActive()) {
            idle();
        }
        //center_to_IMU_decrease_speed();
        powerDrive(0.0, 0.0);

        //TRACKS AND INTAKE ARE STILL RUNNING
    }

    public void super_stuck_loop(int outakeTarget, int originalTarget) {
        stop_tracks();
        run_intake_out(0.9);
        timer1.reset();
        while (timer1.milliseconds() < 500 && opModeIsActive()) {
            idle();
        }
        stop_intake();
    }

    public void rerun_intake_glyphs1() {

        initial_intake_glyph(2000, 0.05);
        if (!bottomOpen) {//                                                                        Check if glyph is still in intake
            wait_time_2.reset();
            powerDrive2(0.0, 0.0);
            telemetry.addData("Glyph is still in intake", bottomOpen);
            while (!bottomOpen && wait_time_2.milliseconds() < 350 && opModeIsActive()) {
                bottomOpen = bottomBeamBreak.getState();
                idle();
            }
            run_intake_out(0.4);
            telemetry.addData("Glyph slightly stuck... fixing it", bottomOpen);
            wait_time_2.reset();
            while (wait_time_2.milliseconds() < 250 && opModeIsActive()) {
                idle();//                                                                             Re-intake Glyph
            }


        } else if (bottomOpen) {
            telemetry.addData("Glyph is in tracks", bottomOpen);//                              If Glyph is in tracks

            wait_time_2.reset();
            while (middleOpen && wait_time_2.milliseconds() < 400 && opModeIsActive()) {
                middleOpen = middleBeamBreak.getState();
                idle();
            }
            wait_time_2.reset();
            run_tracks_down(0.3);
            while (wait_time_2.milliseconds() < 150 && opModeIsActive()) {
                idle();
            }
            current = ONE_IN;//                                                                    Update current to be ONE_IN

        }
    }

    public void wiggle_right_left() {
        ElapsedTime wait_time_2 = new ElapsedTime();
        powerDrive2(0.1, -0.1);
        wait_time_2.reset();
        while (wait_time_2.milliseconds() < 300 && opModeIsActive()) {
            idle();
        }

        powerDrive2(-0.1, 0.1);
        while (wait_time_2.milliseconds() < 300 && opModeIsActive()) {
            idle();
        }
    }

    public void center_to_IMU_decrease_speed(double target, double power, double timeout) {
        ElapsedTime timer = new ElapsedTime();
        if (globalAngle < target) {
            powerDrive2(-power, power);
            timer.reset();
            while (globalAngle < target && timer.milliseconds() < timeout && opModeIsActive()) {
                idle();
            }
            powerDrive2(0.0, 0.0);
        }

        if (globalAngle > target) {
            powerDrive2(power, -power);
            timer.reset();
            while (globalAngle > target && timer.milliseconds() < timeout && opModeIsActive()) {
                idle();
            }
            powerDrive2(0.0, 0.0);
        }

        powerDrive2(0.0, 0.0);

        if (globalAngle < target) {
            powerDrive2(-(power - 0.01), (power - 0.01));
            timer.reset();
            while (globalAngle < target && timer.milliseconds() < timeout && opModeIsActive()) {
                idle();
            }
            powerDrive2(0.0, 0.0);
        }

        if (globalAngle > target) {
            powerDrive2((power - 0.01), -(power - 0.01));
            timer.reset();
            while (globalAngle > target && timer.milliseconds() < timeout && opModeIsActive()) {
                idle();
            }
            powerDrive2(0.0, 0.0);
        }
    }

    public void center_to_IMU(double target, double power, double timeout) {
        ElapsedTime timer = new ElapsedTime();
        if (globalAngle < target) {
            powerDrive2(-power, power);
            timer.reset();
            while (globalAngle < target && timer.milliseconds() < timeout && opModeIsActive()) {
                idle();
            }
            powerDrive2(0.0, 0.0);
        }

        if (globalAngle > target) {
            powerDrive2(power, -power);
            timer.reset();
            while (globalAngle > target && timer.milliseconds() < timeout && opModeIsActive()) {
                idle();
            }
            powerDrive2(0.0, 0.0);
        }

        if (globalAngle < target) {
            powerDrive2(-power, power);
            timer.reset();
            while (globalAngle < (target - 0.1) && timer.milliseconds() < timeout && opModeIsActive()) {
                idle();
            }
            powerDrive2(0.0, 0.0);
        }

        if (globalAngle > target) {
            powerDrive2(power, -power);
            timer.reset();
            while (globalAngle > (target + 0.1) && timer.milliseconds() < timeout && opModeIsActive()) {
                idle();
            }
            powerDrive2(0.0, 0.0);
        }
        powerDrive2(0.0, 0.0);



        if(globalAngle < target) {
            powerDrive2(-(power-0.01), (power-0.01));
            timer.reset();
            while(globalAngle < target && timer.milliseconds() < timeout) {
                idle();
            }
            powerDrive2(0.0,0.0);
        }

        if(globalAngle > target) {
            powerDrive2((power-0.01), -(power-0.01));
            timer.reset();
            while(globalAngle > target && timer.milliseconds() < timeout) {
                idle();
            }
            powerDrive2(0.0,0.0);
        }

    }

    public void intake_glphs_1_red(int intake_time) {
        ElapsedTime intake_time_1 = new ElapsedTime();
        ElapsedTime wait_time_1 = new ElapsedTime();
        ElapsedTime wait_time_2 = new ElapsedTime();
        ElapsedTime stuck_timer = new ElapsedTime();

        bottomOpen = bottomBeamBreak.getState();
        middleOpen = middleBeamBreak.getState();
        topOpen = topBeamBreak.getState();

        run_intake_in(0.9);
        run_tracks_up(0.9);
        powerDrive(0.05, 0.05);
        intake_time_1.reset();
        while (bottomOpen && intake_time_1.milliseconds() < intake_time && opModeIsActive()) {
            bottomOpen = bottomBeamBreak.getState();
            idle();
        }
        powerDrive(0.04, 0.04);

        if (!bottomOpen) {
            wait_time_2.reset();
            powerDrive2(0.0, 0.0);
            while (!bottomOpen && wait_time_2.milliseconds() < 650 && opModeIsActive()) {
                bottomOpen = bottomBeamBreak.getState();
                idle();
            }
            wait_time_2.reset();

            intake1.setPower(0.7);
            intake2.setPower(-0.7);
            while (!bottomOpen && wait_time_2.milliseconds() < 300 && opModeIsActive()) {
                bottomOpen = bottomBeamBreak.getState();
                idle();
            }


            powerDrive2(0.4, 0.4);
            if (!bottomOpen) {
                telemetry.addData("Oh it's stuck!", bottomOpen);
                telemetry.update();
                stop_intake();
                run_tracks_down(1.0);
                run_intake_out(1.0);
                stuck_timer.reset();
                while (!bottomOpen && stuck_timer.milliseconds() < 700 && opModeIsActive()) {
                    bottomOpen = bottomBeamBreak.getState();
                    idle();
                }
                sleep(70);
                stop_intake();
                stop_tracks();
                powerDrive2(0.0, 0.0);
                current = STUCK;

            } else if (!middleOpen || !topOpen) {
                stop_tracks();
                while (!topOpen && opModeIsActive()) {
                    run_tracks_down(1.0);
                    topOpen = topBeamBreak.getState();
                }
                stop_tracks();
                telemetry.addData("Got it!", topOpen);
                telemetry.update();
                //current = STUCK;
                current = ONE_IN;
            }
        }

        if (current == NONE_IN) {

            wait_time_2.reset();
            while (middleOpen && wait_time_2.milliseconds() < 900 && opModeIsActive()) {
                middleOpen = middleBeamBreak.getState();
                idle();
            }
            stop_intake();
            run_tracks_down(0.6);
            sleep(100);
            stop_tracks();
            current = ONE_IN;
        }

        powerDrive(0.0, 0.0);


        if (current == STUCK) {

            driveBackwardDistance_gyro(0.5, 150, 90, 0.02, 2000);

            run_intake_in(0.9);
            run_tracks_up(0.9);
            powerDrive(0.08, 0.08);
            intake_time_1.reset();
            while (bottomOpen && intake_time_1.milliseconds() < intake_time && opModeIsActive()) {
                bottomOpen = bottomBeamBreak.getState();
                idle();
            }
            powerDrive(0.04, 0.04);

            if (!bottomOpen) {
                wait_time_2.reset();
                while (!bottomOpen && wait_time_2.milliseconds() < 800 && opModeIsActive()) {
                    bottomOpen = bottomBeamBreak.getState();
                    idle();
                }

                if (!bottomOpen) {
                    telemetry.addData("Oh it's stuck!", bottomOpen);
                    telemetry.update();
                    stop_intake();
                    run_tracks_down(1.0);
                    run_intake_out(1.0);
                    stuck_timer.reset();
                    while (!bottomOpen && stuck_timer.milliseconds() < 700 && opModeIsActive()) {
                        bottomOpen = bottomBeamBreak.getState();
                        idle();
                    }
                    sleep(70);
                    stop_intake();
                    stop_tracks();
                    powerDrive2(0.0, 0.0);
                    current = STUCK;
                } else if (!middleOpen || !topOpen) {
                    stop_tracks();
                    while (!topOpen && opModeIsActive()) {
                        run_tracks_down(1.0);
                        topOpen = topBeamBreak.getState();
                    }
                    stop_tracks();
                    telemetry.addData("Got it!", topOpen);
                    telemetry.update();
                    current = ONE_IN;
                }
            }

            if (current == NONE_IN) {

                wait_time_2.reset();
                while (middleOpen && wait_time_2.milliseconds() < 900 && opModeIsActive()) {
                    middleOpen = middleBeamBreak.getState();
                    idle();
                }
                stop_intake();
                run_tracks_down(0.6);
                sleep(100);
                stop_tracks();
                current = ONE_IN;
            }

            powerDrive(0.0, 0.0);


            if (current == STUCK) {

                driveBackwardDistance_gyro(0.5, 150, 90, 0.02, 2000);

                run_intake_in(0.9);
                run_tracks_up(0.9);
                //powerDrive(0.15, 0.0);
                powerDrive(0.08, 0.08);
                intake_time_1.reset();
                while (bottomOpen && intake_time_1.milliseconds() < intake_time && opModeIsActive()) {
                    bottomOpen = bottomBeamBreak.getState();
                    //update_gyro_straight(0, 0.02);
                    idle();
                }
                powerDrive(0.0, 0.0);

                if (!bottomOpen) {
                    wait_time_2.reset();
                    while (!bottomOpen && wait_time_2.milliseconds() < 800 && opModeIsActive()) {
                        bottomOpen = bottomBeamBreak.getState();
                        idle();
                    }

                    if (!bottomOpen) {
                        telemetry.addData("Oh it's stuck!", bottomOpen);
                        telemetry.update();
                        stop_intake();
                        run_tracks_down(1.0);
                        run_intake_out(1.0);
                        stuck_timer.reset();
                        while (!bottomOpen && stuck_timer.milliseconds() < 700 && opModeIsActive()) {
                            bottomOpen = bottomBeamBreak.getState();
                            idle();
                        }
                        sleep(70);
                        stop_intake();
                        stop_tracks();
                        current = STUCK;
                    } else if (!middleOpen || !topOpen) {
                        stop_tracks();
                        while (!topOpen && opModeIsActive()) {
                            run_tracks_down(1.0);
                            topOpen = topBeamBreak.getState();
                        }
                        stop_tracks();
                        telemetry.addData("Got it!", topOpen);
                        telemetry.update();
                        //current = STUCK;
                        current = ONE_IN;
                    }
                }

                if (current == NONE_IN) {

                    wait_time_2.reset();
                    while (middleOpen && wait_time_2.milliseconds() < 900 && opModeIsActive()) {
                        middleOpen = middleBeamBreak.getState();
                        idle();
                    }
                    stop_intake();
                    run_tracks_down(0.6);
                    sleep(100);
                    stop_tracks();
                    current = ONE_IN;
                }
            }

            powerDrive(0.0, 0.0);

        } else {
            telemetry.addData("Not stuck, we got it!", "Sweet!");
            telemetry.update();
        }

        if (current == ONE_IN) {//  TWO_IN
            telemetry.addData("AWESOME SAUCE! WE GUCHI FAM! ", "yeet");
            telemetry.update();
            driveBackToCryptobox_red(8000);
            sleep(1000);
        }


    }

    public void intake_glphs_1_red_more_intake(int intake_time) {
        ElapsedTime intake_time_1 = new ElapsedTime();
        ElapsedTime wait_time_1 = new ElapsedTime();
        ElapsedTime wait_time_2 = new ElapsedTime();
        ElapsedTime stuck_timer = new ElapsedTime();

        bottomOpen = bottomBeamBreak.getState();
        middleOpen = middleBeamBreak.getState();
        topOpen = topBeamBreak.getState();

        run_intake_in(0.9);
        run_tracks_up(0.9);
        powerDrive(0.05, 0.05);
        intake_time_1.reset();
        while (bottomOpen && intake_time_1.milliseconds() < intake_time && opModeIsActive()) {
            bottomOpen = bottomBeamBreak.getState();
            idle();
        }
        powerDrive(0.04, 0.04);

        if (!bottomOpen) {
            wait_time_2.reset();
            powerDrive2(0.0, 0.0);
            while (!bottomOpen && wait_time_2.milliseconds() < 650 && opModeIsActive()) {
                bottomOpen = bottomBeamBreak.getState();
                idle();
            }
            wait_time_2.reset();

            intake1.setPower(0.7);
            intake2.setPower(-0.7);
            while (!bottomOpen && wait_time_2.milliseconds() < 300 && opModeIsActive()) {
                bottomOpen = bottomBeamBreak.getState();
                idle();
            }


            powerDrive2(0.4, 0.4);
            if (!bottomOpen) {
                telemetry.addData("Oh it's stuck!", bottomOpen);
                telemetry.update();
                stop_intake();
                run_tracks_down(1.0);
                run_intake_out(1.0);
                stuck_timer.reset();
                while (!bottomOpen && stuck_timer.milliseconds() < 700 && opModeIsActive()) {
                    bottomOpen = bottomBeamBreak.getState();
                    idle();
                }
                sleep(70);
                stop_intake();
                stop_tracks();
                powerDrive2(0.0, 0.0);
                current = STUCK;

            } else if (!middleOpen || !topOpen) {
                stop_tracks();
                while (!topOpen && opModeIsActive()) {
                    run_tracks_down(1.0);
                    topOpen = topBeamBreak.getState();
                }
                stop_tracks();
                telemetry.addData("Got it!", topOpen);
                telemetry.update();
                //current = STUCK;
                current = ONE_IN;
            }
        }

        if (current == NONE_IN) {

            wait_time_2.reset();
            while (middleOpen && wait_time_2.milliseconds() < 900 && opModeIsActive()) {
                middleOpen = middleBeamBreak.getState();
                idle();
            }
            stop_intake();
            run_tracks_down(0.6);
            sleep(100);
            stop_tracks();
            current = ONE_IN;
        }

        powerDrive(0.0, 0.0);


        if (current == STUCK) {

            driveBackwardDistance_gyro(0.5, 150, 90, 0.02, 2000);

            run_intake_in(0.9);
            run_tracks_up(0.9);
            powerDrive(0.08, 0.08);
            intake_time_1.reset();
            while (bottomOpen && intake_time_1.milliseconds() < intake_time && opModeIsActive()) {
                bottomOpen = bottomBeamBreak.getState();
                idle();
            }
            powerDrive(0.04, 0.04);

            if (!bottomOpen) {
                wait_time_2.reset();
                while (!bottomOpen && wait_time_2.milliseconds() < 800 && opModeIsActive()) {
                    bottomOpen = bottomBeamBreak.getState();
                    idle();
                }

                if (!bottomOpen) {
                    telemetry.addData("Oh it's stuck!", bottomOpen);
                    telemetry.update();
                    stop_intake();
                    run_tracks_down(1.0);
                    run_intake_out(1.0);
                    stuck_timer.reset();
                    while (!bottomOpen && stuck_timer.milliseconds() < 700 && opModeIsActive()) {
                        bottomOpen = bottomBeamBreak.getState();
                        idle();
                    }
                    sleep(70);
                    stop_intake();
                    stop_tracks();
                    powerDrive2(0.0, 0.0);
                    current = STUCK;
                } else if (!middleOpen || !topOpen) {
                    stop_tracks();
                    while (!topOpen && opModeIsActive()) {
                        run_tracks_down(1.0);
                        topOpen = topBeamBreak.getState();
                    }
                    stop_tracks();
                    telemetry.addData("Got it!", topOpen);
                    telemetry.update();
                    current = ONE_IN;
                }
            }

            if (current == NONE_IN) {

                wait_time_2.reset();
                while (middleOpen && wait_time_2.milliseconds() < 900 && opModeIsActive()) {
                    middleOpen = middleBeamBreak.getState();
                    idle();
                }
                stop_intake();
                run_tracks_down(0.6);
                sleep(100);
                stop_tracks();
                current = ONE_IN;
            }

            powerDrive(0.0, 0.0);


            if (current == STUCK) {

                driveBackwardDistance_gyro(0.5, 150, 90, 0.02, 2000);

                run_intake_in(0.9);
                run_tracks_up(0.9);
                //powerDrive(0.15, 0.0);
                powerDrive(0.08, 0.08);
                intake_time_1.reset();
                while (bottomOpen && intake_time_1.milliseconds() < intake_time && opModeIsActive()) {
                    bottomOpen = bottomBeamBreak.getState();
                    //update_gyro_straight(0, 0.02);
                    idle();
                }
                powerDrive(0.0, 0.0);

                if (!bottomOpen) {
                    wait_time_2.reset();
                    while (!bottomOpen && wait_time_2.milliseconds() < 800 && opModeIsActive()) {
                        bottomOpen = bottomBeamBreak.getState();
                        idle();
                    }

                    if (!bottomOpen) {
                        telemetry.addData("Oh it's stuck!", bottomOpen);
                        telemetry.update();
                        stop_intake();
                        run_tracks_down(1.0);
                        run_intake_out(1.0);
                        stuck_timer.reset();
                        while (!bottomOpen && stuck_timer.milliseconds() < 700 && opModeIsActive()) {
                            bottomOpen = bottomBeamBreak.getState();
                            idle();
                        }
                        sleep(70);
                        stop_intake();
                        stop_tracks();
                        current = STUCK;
                    } else if (!middleOpen || !topOpen) {
                        stop_tracks();
                        while (!topOpen && opModeIsActive()) {
                            run_tracks_down(1.0);
                            topOpen = topBeamBreak.getState();
                        }
                        stop_tracks();
                        telemetry.addData("Got it!", topOpen);
                        telemetry.update();
                        //current = STUCK;
                        current = ONE_IN;
                    }
                }

                if (current == NONE_IN) {

                    wait_time_2.reset();
                    while (middleOpen && wait_time_2.milliseconds() < 900 && opModeIsActive()) {
                        middleOpen = middleBeamBreak.getState();
                        idle();
                    }
                    stop_intake();
                    run_tracks_down(0.6);
                    sleep(100);
                    stop_tracks();
                    current = ONE_IN;
                }
            }

            powerDrive(0.0, 0.0);


            ///////////////////////////////////////////////////////////////////////////////////////

            if (current == STUCK) {

                driveBackwardDistance_gyro(0.5, 150, 90, 0.02, 2000);

                run_intake_in(0.9);
                run_tracks_up(0.9);
                powerDrive(0.08, 0.08);
                intake_time_1.reset();
                while (bottomOpen && intake_time_1.milliseconds() < intake_time && opModeIsActive()) {
                    bottomOpen = bottomBeamBreak.getState();
                    idle();
                }
                powerDrive(0.04, 0.04);

                if (!bottomOpen) {
                    wait_time_2.reset();
                    while (!bottomOpen && wait_time_2.milliseconds() < 800 && opModeIsActive()) {
                        bottomOpen = bottomBeamBreak.getState();
                        idle();
                    }

                    if (!bottomOpen) {
                        telemetry.addData("Oh it's stuck!", bottomOpen);
                        telemetry.update();
                        stop_intake();
                        run_tracks_down(1.0);
                        run_intake_out(1.0);
                        stuck_timer.reset();
                        while (!bottomOpen && stuck_timer.milliseconds() < 700 && opModeIsActive()) {
                            bottomOpen = bottomBeamBreak.getState();
                            idle();
                        }
                        sleep(70);
                        stop_intake();
                        stop_tracks();
                        powerDrive2(0.0, 0.0);
                        current = STUCK;
                    } else if (!middleOpen || !topOpen) {
                        stop_tracks();
                        while (!topOpen && opModeIsActive()) {
                            run_tracks_down(1.0);
                            topOpen = topBeamBreak.getState();
                        }
                        stop_tracks();
                        telemetry.addData("Got it!", topOpen);
                        telemetry.update();
                        current = ONE_IN;
                    }
                }

                if (current == NONE_IN) {

                    wait_time_2.reset();
                    while (middleOpen && wait_time_2.milliseconds() < 900 && opModeIsActive()) {
                        middleOpen = middleBeamBreak.getState();
                        idle();
                    }
                    stop_intake();
                    run_tracks_down(0.6);
                    sleep(100);
                    stop_tracks();
                    current = ONE_IN;
                }

            }
            powerDrive(0.0, 0.0);


        } else {
            telemetry.addData("Not stuck, we got it!", "Sweet!");
            telemetry.update();
        }

        if (current == ONE_IN) {//  TWO_IN
            telemetry.addData("AWESOME SAUCE! WE GUCHI FAM! ", "yeet");
            telemetry.update();
            //driveBackToCryptobox_red(8000);
            sleep(1000);
        }


    }

    public void intake_glphs_2_red(int intake_time) {
        ElapsedTime intake_time_1 = new ElapsedTime();
        ElapsedTime wait_time_1 = new ElapsedTime();
        ElapsedTime wait_time_2 = new ElapsedTime();
        ElapsedTime stuck_timer = new ElapsedTime();

        bottomOpen = bottomBeamBreak.getState();
        middleOpen = middleBeamBreak.getState();
        topOpen = topBeamBreak.getState();

        run_intake_in(0.9);
        powerDrive(0.05, 0.05);
        intake_time_1.reset();
        while (bottomOpen && intake_time_1.milliseconds() < intake_time && topOpen && opModeIsActive()) {
            bottomOpen = bottomBeamBreak.getState();
            topOpen = topBeamBreak.getState();
            idle();
        }

        if (!topOpen) {
            run_tracks_down(0.65);
            while (middleOpen && opModeIsActive()) {
                middleOpen = middleBeamBreak.getState();
                idle();
            }
            sleep(100);
            stop_tracks();
        }

        powerDrive(0.05, 0.05);

        if (!bottomOpen) {
            wait_time_2.reset();
            while (!bottomOpen && wait_time_2.milliseconds() < 800 && topOpen && opModeIsActive()) {
                bottomOpen = bottomBeamBreak.getState();
                topOpen = topBeamBreak.getState();
                idle();
            }
            if (!topOpen) {
                run_tracks_down(0.65);
                while (middleOpen && opModeIsActive()) {
                    middleOpen = middleBeamBreak.getState();
                    idle();
                }
                sleep(100);
                stop_tracks();
            }

            if (!bottomOpen) {
                telemetry.addData("Oh it's stuck!", bottomOpen);
                telemetry.update();
                stop_intake();
                run_tracks_down(1.0);
                run_intake_out(1.0);
                stuck_timer.reset();
                while (!bottomOpen && stuck_timer.milliseconds() < 700 && opModeIsActive()) {
                    bottomOpen = bottomBeamBreak.getState();
                    idle();
                }
                sleep(70);
                stop_intake();
                stop_tracks();
                current = STUCK;

            } else if (!middleOpen || !topOpen) {
                stop_tracks();
                while (!topOpen && opModeIsActive()) {
                    run_tracks_down(1.0);
                    topOpen = topBeamBreak.getState();
                }
                stop_tracks();
                telemetry.addData("Got it!", topOpen);
                telemetry.update();
                //current = STUCK;
                current = TWO_IN;
            }
        }

        if (current == ONE_IN) {

            while (middleOpen && opModeIsActive()) {
                middleOpen = middleBeamBreak.getState();
                idle();
            }
            stop_intake();
            run_tracks_down(0.6);
            sleep(100);
            stop_tracks();
            current = ONE_IN;
        }
        powerDrive(0.0, 0.0);


        if (current == STUCK) {

            run_intake_in(0.9);
            powerDrive(0.05, 0.05);
            intake_time_1.reset();
            while (bottomOpen && intake_time_1.milliseconds() < intake_time && topOpen && opModeIsActive()) {
                bottomOpen = bottomBeamBreak.getState();
                topOpen = topBeamBreak.getState();
                idle();
            }

            if (!topOpen) {
                run_tracks_down(0.65);
                while (middleOpen && opModeIsActive()) {
                    middleOpen = middleBeamBreak.getState();
                    idle();
                }
                sleep(100);
                stop_tracks();
            }

            powerDrive(0.05, 0.05);

            if (!bottomOpen) {
                wait_time_2.reset();
                while (!bottomOpen && wait_time_2.milliseconds() < 800 && topOpen && opModeIsActive()) {
                    bottomOpen = bottomBeamBreak.getState();
                    topOpen = topBeamBreak.getState();
                    idle();
                }
                if (!topOpen) {
                    run_tracks_down(0.65);
                    while (middleOpen && opModeIsActive()) {
                        middleOpen = middleBeamBreak.getState();
                        idle();
                    }
                    sleep(100);
                    stop_tracks();
                }

                if (!bottomOpen) {
                    telemetry.addData("Oh it's stuck!", bottomOpen);
                    telemetry.update();
                    stop_intake();
                    run_tracks_down(1.0);
                    run_intake_out(1.0);
                    stuck_timer.reset();
                    while (!bottomOpen && stuck_timer.milliseconds() < 700 && opModeIsActive()) {
                        bottomOpen = bottomBeamBreak.getState();
                        idle();
                    }
                    sleep(70);
                    stop_intake();
                    stop_tracks();
                    current = STUCK;

                } else if (!middleOpen || !topOpen) {
                    stop_tracks();
                    while (!topOpen && opModeIsActive()) {
                        run_tracks_down(1.0);
                        topOpen = topBeamBreak.getState();
                    }
                    stop_tracks();
                    telemetry.addData("Got it!", topOpen);
                    telemetry.update();
                    //current = STUCK;
                    current = TWO_IN;
                }
            }

            if (current == ONE_IN) {

                while (middleOpen && opModeIsActive()) {
                    middleOpen = middleBeamBreak.getState();
                    idle();
                }
                stop_intake();
                run_tracks_down(0.6);
                sleep(100);
                stop_tracks();
                current = TWO_IN;
            }

            powerDrive(0.0, 0.0);


        }

        if (current == STUCK) {

            run_intake_in(0.9);
            powerDrive(0.05, 0.05);
            intake_time_1.reset();
            while (bottomOpen && intake_time_1.milliseconds() < intake_time && topOpen && opModeIsActive()) {
                bottomOpen = bottomBeamBreak.getState();
                topOpen = topBeamBreak.getState();
                idle();
            }

            if (!topOpen) {
                run_tracks_down(0.65);
                while (middleOpen && opModeIsActive()) {
                    middleOpen = middleBeamBreak.getState();
                    idle();
                }
                sleep(100);
                stop_tracks();
            }

            powerDrive(0.05, 0.05);

            if (!bottomOpen) {
                wait_time_2.reset();
                while (!bottomOpen && wait_time_2.milliseconds() < 800 && topOpen && opModeIsActive()) {
                    bottomOpen = bottomBeamBreak.getState();
                    topOpen = topBeamBreak.getState();
                    idle();
                }
                if (!topOpen) {
                    run_tracks_down(0.65);
                    while (middleOpen && opModeIsActive()) {
                        middleOpen = middleBeamBreak.getState();
                        idle();
                    }
                    sleep(100);
                    stop_tracks();
                }

                if (!bottomOpen) {
                    telemetry.addData("Oh it's stuck!", bottomOpen);
                    telemetry.update();
                    stop_intake();
                    run_tracks_down(1.0);
                    run_intake_out(1.0);
                    stuck_timer.reset();
                    while (!bottomOpen && stuck_timer.milliseconds() < 700 && opModeIsActive()) {
                        bottomOpen = bottomBeamBreak.getState();
                        idle();
                    }
                    sleep(70);
                    stop_intake();
                    stop_tracks();
                    current = STUCK;

                } else if (!middleOpen || !topOpen) {
                    stop_tracks();
                    while (!topOpen && opModeIsActive()) {
                        run_tracks_down(1.0);
                        topOpen = topBeamBreak.getState();
                    }
                    stop_tracks();
                    telemetry.addData("Got it!", topOpen);
                    telemetry.update();
                    current = TWO_IN;
                }
            }

            if (current == ONE_IN) {

                while (middleOpen && opModeIsActive()) {
                    middleOpen = middleBeamBreak.getState();
                    idle();
                }
                stop_intake();
                run_tracks_down(0.6);
                sleep(100);
                stop_tracks();
                current = TWO_IN;
            }

            powerDrive(0.0, 0.0);
        }
        powerDrive(0.0, 0.0);

    }

    public void driveBackToCryptobox(int distance_back, double speed, double secondarySpeed, long optionalTimeout, int imuTarget) {
        ElapsedTime optionalTimeoutClock = new ElapsedTime();
        //driveBackwardDistance_gyro(0.9, distance_back, imuTarget, 0.02, 1300);
        powerDrive(-speed, -speed);

        while (range.cmUltrasonic() > 54
25
 && optionalTimeoutClock.milliseconds() < optionalTimeout && opModeIsActive()) {
            telemetry.addData("timeout Clock", optionalTimeoutClock.milliseconds());
            telemetry.addData("Range CM", range.cmUltrasonic());
            telemetry.update();
            update_gyro_straight(imuTarget, 0.01);//0.02
            idle();
        }

        optionalTimeoutClock.reset();
        powerDrive(-0.09, -0.09);//0.05
        while (range.cmUltrasonic() > 18
19
 && optionalTimeoutClock.milliseconds() < optionalTimeout && opModeIsActive()) {
            telemetry.addData("timeout Clock", optionalTimeoutClock.milliseconds());
            telemetry.addData("Range CM", range.cmUltrasonic());
            telemetry.update();
            update_gyro_straight(imuTarget, 0.01);//0.005//0.02
            idle();
        }
        powerDrive2(0.0, 0.0);

        sleep(200);

        center_lateral_movement();

        powerDrive2(secondarySpeed, secondarySpeed);

        while (range.cmUltrasonic() < 18
20
 && opModeIsActive()) {
            telemetry.addData("Range CM", range.cmUltrasonic());
            telemetry.update();
            idle();
        }
        powerDrive2(0.0, 0.0);
        telemetry.addData("We have arrived at the Cryptobox! Yay!", range.cmUltrasonic());
        telemetry.update();
    }

    public void driveBackToCryptobox_red(long optionalTimeout) {
        ElapsedTime optionalTimeoutClock = new ElapsedTime();
        driveBackwardDistance_gyro(0.9, 500, 90, 0.02, 1300);
        powerDrive(-0.2, -0.2);
        while (range.cmUltrasonic() > 16
25
 && optionalTimeoutClock.milliseconds() < optionalTimeout && opModeIsActive()) {
            update_gyro_straight(90, 0.02);
            idle();
        }

        //Insert lateral movement

        powerDrive2(0.2, 0.2);

        while (range.cmUltrasonic() < 20 && opModeIsActive()) {
            idle();
        }
        powerDrive2(0.0, 0.0);
        telemetry.addData("We have arrived at the Cryptobox! YEET", range.cmUltrasonic());
        telemetry.update();
    }

    public void intake_glphs_1_blue(int intake_time) {
        ElapsedTime intake_time_1 = new ElapsedTime();
        ElapsedTime wait_time_1 = new ElapsedTime();
        ElapsedTime wait_time_2 = new ElapsedTime();
        ElapsedTime stuck_timer = new ElapsedTime();

        bottomOpen = bottomBeamBreak.getState();
        middleOpen = middleBeamBreak.getState();
        topOpen = topBeamBreak.getState();

        run_intake_in(0.9);
        run_tracks_up(0.9);
        powerDrive(0.05, 0.05);
        intake_time_1.reset();
        while (bottomOpen && intake_time_1.milliseconds() < intake_time && opModeIsActive()) {
            bottomOpen = bottomBeamBreak.getState();
            idle();
        }
        powerDrive(0.04, 0.04);

        if (!bottomOpen) {
            wait_time_2.reset();
            powerDrive2(0.0, 0.0);
            while (!bottomOpen && wait_time_2.milliseconds() < 650 && opModeIsActive()) {
                bottomOpen = bottomBeamBreak.getState();
                idle();
            }
            wait_time_2.reset();

            intake1.setPower(0.7);
            intake2.setPower(-0.7);
            while (!bottomOpen && wait_time_2.milliseconds() < 300 && opModeIsActive()) {
                bottomOpen = bottomBeamBreak.getState();
                idle();
            }



            powerDrive2(0.4, 0.4);
            if (!bottomOpen) {
                telemetry.addData("Oh it's stuck!", bottomOpen);
                telemetry.update();
                stop_intake();
                run_tracks_down(1.0);
                run_intake_out(1.0);
                stuck_timer.reset();
                while (!bottomOpen && stuck_timer.milliseconds() < 700 && opModeIsActive()) {
                    bottomOpen = bottomBeamBreak.getState();
                    idle();
                }
                sleep(70);
                stop_intake();
                stop_tracks();
                powerDrive2(0.0, 0.0);
                current = STUCK;

            } else if (!middleOpen || !topOpen) {
                stop_tracks();
                while (!topOpen && opModeIsActive()) {
                    run_tracks_down(1.0);
                    topOpen = topBeamBreak.getState();
                }
                stop_tracks();
                telemetry.addData("Got it!", topOpen);
                telemetry.update();
                //current = STUCK;
                current = ONE_IN;
            }
        }

   if (current == NONE_IN) {

            wait_time_2.reset();
            while (middleOpen && wait_time_2.milliseconds() < 700 && opModeIsActive()) {
                middleOpen = middleBeamBreak.getState();
                idle();
            }
            stop_intake();
            run_tracks_down(0.6);
            sleep(100);
            stop_tracks();
            current = ONE_IN;
        }


        powerDrive(0.0, 0.0);


        if (current == STUCK) {

            driveBackwardDistance_gyro(0.5, 150, -90, 0.02, 2000);

            run_intake_in(0.9);
            run_tracks_up(0.9);
            //powerDrive(0.15, 0.0);
            powerDrive(0.08, 0.08);
            intake_time_1.reset();
            while (bottomOpen && intake_time_1.milliseconds() < intake_time && opModeIsActive()) {
                bottomOpen = bottomBeamBreak.getState();
                //update_gyro_straight(0, 0.02);
                idle();
            }
            powerDrive(0.04, 0.04);

            if (!bottomOpen) {
                wait_time_2.reset();
                while (!bottomOpen && wait_time_2.milliseconds() < 800 && opModeIsActive()) {
                    bottomOpen = bottomBeamBreak.getState();
                    idle();
                }

                if (!bottomOpen) {
                    telemetry.addData("Oh it's stuck!", bottomOpen);
                    telemetry.update();
                    stop_intake();
                    run_tracks_down(1.0);
                    run_intake_out(1.0);
                    stuck_timer.reset();
                    while (!bottomOpen && stuck_timer.milliseconds() < 700 && opModeIsActive()) {
                        bottomOpen = bottomBeamBreak.getState();
                        idle();
                    }
                    sleep(70);
                    stop_intake();
                    stop_tracks();
                    powerDrive2(0.0, 0.0);
                    current = STUCK;
                } else if (!middleOpen || !topOpen) {
                    stop_tracks();
                    while (!topOpen && opModeIsActive()) {
                        run_tracks_down(1.0);
                        topOpen = topBeamBreak.getState();
                    }
                    stop_tracks();
                    telemetry.addData("Got it!", topOpen);
                    telemetry.update();
                    //current = STUCK;
                    current = ONE_IN;
                }
            }

            if (current == NONE_IN) {

                wait_time_2.reset();
                while (middleOpen && wait_time_2.milliseconds() < 900 && opModeIsActive()) {
                    middleOpen = middleBeamBreak.getState();
                    idle();
                }
                stop_intake();
                run_tracks_down(0.6);
                sleep(100);
                stop_tracks();
                current = ONE_IN;
            }


            powerDrive(0.0, 0.0);


            if (current == STUCK) {

                driveBackwardDistance_gyro(0.5, 150, -90, 0.02, 2000);

                run_intake_in(0.9);
                run_tracks_up(0.9);
                //powerDrive(0.15, 0.0);
                powerDrive(0.08, 0.08);
                intake_time_1.reset();
                while (bottomOpen && intake_time_1.milliseconds() < intake_time && opModeIsActive()) {
                    bottomOpen = bottomBeamBreak.getState();
                    //update_gyro_straight(0, 0.02);
                    idle();
                }
                powerDrive(0.0, 0.0);

                if (!bottomOpen) {
                    wait_time_2.reset();
                    while (!bottomOpen && wait_time_2.milliseconds() < 800 && opModeIsActive()) {
                        bottomOpen = bottomBeamBreak.getState();
                        idle();
                    }

                    if (!bottomOpen) {
                        telemetry.addData("Oh it's stuck!", bottomOpen);
                        telemetry.update();
                        stop_intake();
                        run_tracks_down(1.0);
                        run_intake_out(1.0);
                        stuck_timer.reset();
                        while (!bottomOpen && stuck_timer.milliseconds() < 700 && opModeIsActive()) {
                            bottomOpen = bottomBeamBreak.getState();
                            idle();
                        }
                        sleep(70);
                        stop_intake();
                        stop_tracks();
                        //current = ONE_IN;// enum
                        current = STUCK;
                    } else if (!middleOpen || !topOpen) {
                        stop_tracks();
                        while (!topOpen && opModeIsActive()) {
                            run_tracks_down(1.0);
                            topOpen = topBeamBreak.getState();
                        }
                        stop_tracks();
                        telemetry.addData("Got it!", topOpen);
                        telemetry.update();
                        //current = STUCK;
                        current = ONE_IN;
                    }
                }

                if (current == NONE_IN) {

                    wait_time_2.reset();
                    while (middleOpen && wait_time_2.milliseconds() < 900 && opModeIsActive()) {
                        middleOpen = middleBeamBreak.getState();
                        idle();
                    }
                    stop_intake();
                    run_tracks_down(0.6);
                    sleep(100);
                    stop_tracks();
                    current = ONE_IN;
                }
            }

            powerDrive(0.0, 0.0);

        } else {
            telemetry.addData("Not stuck, we got it!", "Sweet!");
            telemetry.update();
        }

        if (current == ONE_IN) {//  TWO_IN
            telemetry.addData("AWESOME SAUCE! WE GUCHI FAM! ", "yeet");
            telemetry.update();
            //driveBackToCryptobox_blue(8000);
            sleep(1000);
        }


    }

    public void intake_glphs_2_blue(int intake_time) {
        ElapsedTime intake_time_1 = new ElapsedTime();
        ElapsedTime wait_time_1 = new ElapsedTime();
        ElapsedTime wait_time_2 = new ElapsedTime();
        ElapsedTime stuck_timer = new ElapsedTime();


        bottomOpen = bottomBeamBreak.getState();
        middleOpen = middleBeamBreak.getState();
        topOpen = topBeamBreak.getState();

        run_intake_in(0.9);
        powerDrive(0.05, 0.05);
        intake_time_1.reset();
        while (bottomOpen && intake_time_1.milliseconds() < intake_time && topOpen && opModeIsActive()) {
            bottomOpen = bottomBeamBreak.getState();
            topOpen = topBeamBreak.getState();
            idle();
        }

        if (!topOpen) {
            run_tracks_down(0.65);
            while (middleOpen && opModeIsActive()) {
                middleOpen = middleBeamBreak.getState();
                idle();
            }
            sleep(100);
            stop_tracks();
        }

        powerDrive(0.05, 0.05);

        if (!bottomOpen) {
            wait_time_2.reset();
            while (!bottomOpen && wait_time_2.milliseconds() < 800 && topOpen && opModeIsActive()) {
                bottomOpen = bottomBeamBreak.getState();
                topOpen = topBeamBreak.getState();
                idle();
            }
            if (!topOpen) {
                run_tracks_down(0.65);
                while (middleOpen && opModeIsActive()) {
                    middleOpen = middleBeamBreak.getState();
                    idle();
                }
                sleep(100);
                stop_tracks();
            }

            if (!bottomOpen) {
                telemetry.addData("Oh it's stuck!", bottomOpen);
                telemetry.update();
                stop_intake();
                run_tracks_down(1.0);
                run_intake_out(1.0);
                stuck_timer.reset();
                while (!bottomOpen && stuck_timer.milliseconds() < 700 && opModeIsActive()) {
                    bottomOpen = bottomBeamBreak.getState();
                    idle();
                }
                sleep(70);
                stop_intake();
                stop_tracks();
                current = STUCK;

            } else if (!middleOpen || !topOpen) {
                stop_tracks();
                while (!topOpen && opModeIsActive()) {
                    run_tracks_down(1.0);
                    topOpen = topBeamBreak.getState();
                }
                stop_tracks();
                telemetry.addData("Got it!", topOpen);
                telemetry.update();
                //current = STUCK;
                current = TWO_IN;
            }
        }

        if (current == ONE_IN) {

            while (middleOpen && opModeIsActive()) {
                middleOpen = middleBeamBreak.getState();
                idle();
            }
            stop_intake();
            run_tracks_down(0.6);
            sleep(100);
            stop_tracks();
            current = ONE_IN;
        }
        powerDrive(0.0, 0.0);


        if (current == STUCK) {

            run_intake_in(0.9);
            powerDrive(0.05, 0.05);
            intake_time_1.reset();
            while (bottomOpen && intake_time_1.milliseconds() < intake_time && topOpen && opModeIsActive()) {
                bottomOpen = bottomBeamBreak.getState();
                topOpen = topBeamBreak.getState();
                idle();
            }

            if (!topOpen) {
                run_tracks_down(0.65);
                while (middleOpen && opModeIsActive()) {
                    middleOpen = middleBeamBreak.getState();
                    idle();
                }
                sleep(100);
                stop_tracks();
            }

            powerDrive(0.05, 0.05);

            if (!bottomOpen) {
                wait_time_2.reset();
                while (!bottomOpen && wait_time_2.milliseconds() < 800 && topOpen && opModeIsActive()) {
                    bottomOpen = bottomBeamBreak.getState();
                    topOpen = topBeamBreak.getState();
                    idle();
                }
                if (!topOpen) {
                    run_tracks_down(0.65);
                    while (middleOpen && opModeIsActive()) {
                        middleOpen = middleBeamBreak.getState();
                        idle();
                    }
                    sleep(100);
                    stop_tracks();
                }

                if (!bottomOpen) {
                    telemetry.addData("Oh it's stuck!", bottomOpen);
                    telemetry.update();
                    stop_intake();
                    run_tracks_down(1.0);
                    run_intake_out(1.0);
                    stuck_timer.reset();
                    while (!bottomOpen && stuck_timer.milliseconds() < 700 && opModeIsActive()) {
                        bottomOpen = bottomBeamBreak.getState();
                        idle();
                    }
                    sleep(70);
                    stop_intake();
                    stop_tracks();
                    current = STUCK;

                } else if (!middleOpen || !topOpen) {
                    stop_tracks();
                    while (!topOpen && opModeIsActive()) {
                        run_tracks_down(1.0);
                        topOpen = topBeamBreak.getState();
                    }
                    stop_tracks();
                    telemetry.addData("Got it!", topOpen);
                    telemetry.update();
                    //current = STUCK;
                    current = TWO_IN;
                }
            }

            if (current == ONE_IN) {

                while (middleOpen && opModeIsActive()) {
                    middleOpen = middleBeamBreak.getState();
                    idle();
                }
                stop_intake();
                run_tracks_down(0.6);
                sleep(100);
                stop_tracks();
                current = TWO_IN;
            }

            powerDrive(0.0, 0.0);


        }

        if (current == STUCK) {

            run_intake_in(0.9);
            powerDrive(0.05, 0.05);
            intake_time_1.reset();
            while (bottomOpen && intake_time_1.milliseconds() < intake_time && topOpen && opModeIsActive()) {
                bottomOpen = bottomBeamBreak.getState();
                topOpen = topBeamBreak.getState();
                idle();
            }

            if (!topOpen) {
                run_tracks_down(0.65);
                while (middleOpen && opModeIsActive()) {
                    middleOpen = middleBeamBreak.getState();
                    idle();
                }
                sleep(100);
                stop_tracks();
            }

            powerDrive(0.05, 0.05);

            if (!bottomOpen) {
                wait_time_2.reset();
                while (!bottomOpen && wait_time_2.milliseconds() < 800 && topOpen && opModeIsActive()) {
                    bottomOpen = bottomBeamBreak.getState();
                    topOpen = topBeamBreak.getState();
                    idle();
                }
                if (!topOpen) {
                    run_tracks_down(0.65);
                    while (middleOpen && opModeIsActive()) {
                        middleOpen = middleBeamBreak.getState();
                        idle();
                    }
                    sleep(100);
                    stop_tracks();
                }

                if (!bottomOpen) {
                    telemetry.addData("Oh it's stuck!", bottomOpen);
                    telemetry.update();
                    stop_intake();
                    run_tracks_down(1.0);
                    run_intake_out(1.0);
                    stuck_timer.reset();
                    while (!bottomOpen && stuck_timer.milliseconds() < 700 && opModeIsActive()) {
                        bottomOpen = bottomBeamBreak.getState();
                        idle();
                    }
                    sleep(70);
                    stop_intake();
                    stop_tracks();
                    current = STUCK;

                } else if (!middleOpen || !topOpen) {
                    stop_tracks();
                    while (!topOpen && opModeIsActive()) {
                        run_tracks_down(1.0);
                        topOpen = topBeamBreak.getState();
                    }
                    stop_tracks();
                    telemetry.addData("Got it!", topOpen);
                    telemetry.update();
                    current = TWO_IN;
                }
            }

            if (current == ONE_IN) {

                while (middleOpen && opModeIsActive()) {
                    middleOpen = middleBeamBreak.getState();
                    idle();
                }
                stop_intake();
                run_tracks_down(0.6);
                sleep(100);
                stop_tracks();
                current = TWO_IN;
            }

            powerDrive(0.0, 0.0);
        }
        powerDrive(0.0, 0.0);

    }

    public void driveBackToCryptobox_blue(long optionalTimeout) {
        ElapsedTime optionalTimeoutClock = new ElapsedTime();
        driveBackwardDistance_gyro(0.9, 500, -90, 0.02, 1300);
        powerDrive(-0.2, -0.2);
        while (range.cmUltrasonic() > 16
25
 && optionalTimeoutClock.milliseconds() < optionalTimeout && opModeIsActive()) {
            update_gyro_straight(-90, 0.02);
            idle();
        }


        //Insert lateral movement
        powerDrive2(0.2, 0.2);


        while (range.cmUltrasonic() < 20 && opModeIsActive()) {
            idle();
        }
        powerDrive2(0.0, 0.0);
        telemetry.addData("We have arrived at the Cryptobox! YEET", range.cmUltrasonic());
        telemetry.update();
    }

    public void correctLateralMovementLeft() {
        while (opModeIsActive() && !arrivedLeft && leftCurrentLocation <= 1) {

            telemetry.addData("Right Current Location: ", rightCurrentLocation);
            telemetry.addData("Left Current Location: ", leftCurrentLocation);
            telemetry.update();

            miliseconds = updater.milliseconds();
            if (miliseconds >= lastNumb + 0.001) {
                leftCurrentLocation += 0.0525;
                lastNumb = miliseconds;
            }

            lateralGlyphMovement.setPos(leftCurrentLocation);
            if (cryptoDistanceLeft.getDistance(DistanceUnit.INCH) >= 0) {
                telemetry.addData("Arrived Left!", cryptoDistanceLeft.getDistance(DistanceUnit.INCH));
                telemetry.update();
                leftLocation = lateral1.getPosition();
                arrivedLeft = true;
            }
        }
    }

    public void correctLateralMovementRight() {
        while (opModeIsActive() && !arrivedRight && rightCurrentLocation >= -1) {

            telemetry.addData("Right Current Location: ", rightCurrentLocation);
            telemetry.addData("Left Current Location: ", leftCurrentLocation);
            telemetry.update();

            miliseconds2 = updater2.milliseconds();
            if (miliseconds2 >= lastNumb2 + 0.001) {
                rightCurrentLocation -= 0.0525;
                lastNumb2 = miliseconds2;
            }

            lateralGlyphMovement.setPos(rightCurrentLocation);
            if (cryptoDistanceRight.getDistance(DistanceUnit.INCH) >= 0) {
                telemetry.addData("Arrived Right!", cryptoDistanceRight.getDistance(DistanceUnit.INCH));
                telemetry.update();
                rightLocation = lateral1.getPosition();
                arrivedRight = true;
            }
        }
    }

    public void center_lateral_movement() {
        correctLateralMovementLeft();
        sleep(360);
        if (cryptoDistanceLeft.getDistance(DistanceUnit.INCH) >= 0) {
            telemetry.addLine("Centered to left!");
            telemetry.update();
        } else {
            correctLateralMovementRight();
            sleep(360);
            telemetry.addLine("Centered to right!");
            telemetry.update();

            if (cryptoDistanceRight.getDistance(DistanceUnit.INCH) >= 0) {
                telemetry.addLine("To the right!");
                telemetry.update();
            } else {
                lateralGlyphMovement.setPos(0.5);
                telemetry.addLine("Not found! Centered");
                telemetry.update();
            }
        }


    }

    public void outakeGlyph1() {
        run_tracks_up(1.0);
        sleep(650);
        //setFlipperPos(0.0);
        sleep(150);
        stop_tracks();
        sleep(600);
        //setFlipperPos(1.0);
        sleep(400);
    }

    public void outtake_glyph_time() {
        ElapsedTime outtakeTimeTime = new ElapsedTime();
        outtakeTimeTime.reset();

    }

    public void outakeGlyph() {
        ElapsedTime outakeTimer = new ElapsedTime();
        topOpen = topBeamBreak.getState();
        if (topOpen) {
            run_tracks_up(1.0);
            run_intake_in(1.0);
            outakeTimer.reset();
            while (topOpen && outakeTimer.milliseconds() < 800 && opModeIsActive()) {
                topOpen = topBeamBreak.getState();
                idle();
            }
        }


        outakeTimer.reset();
        //run_tracks_up(0.7);
        run_tracks_up(1.0);
        run_intake_in(1.0);
        outakeTimer.reset();

        //current = TWO_IN;
        sleep(200);
        run_tracks_up(0.7);
        while (range.cmUltrasonic() > 9
7
 && outakeTimer.milliseconds() < 2800 && opModeIsActive()) { // Does is bruno mars is gay??
            telemetry.addData("> 7 and timer not up", range.cmUltrasonic());
            telemetry.update();
            idle();
        }
        if (outakeTimer.milliseconds() < 1100) {
            onePastRange = true;
        } else if (outakeTimer.milliseconds() > 1100) {
            onePastRange = false;
        }
        outakeTimer.reset();
        while (range.cmUltrasonic() < 9 && outakeTimer.milliseconds() < 1100 && opModeIsActive()) {
            idle();
        }
        if (current == TWO_IN) {
            outakeTimer.reset();
            sleep(150);
            while (range.cmUltrasonic() > 9 && outakeTimer.milliseconds() < 1600 && opModeIsActive()) {
                telemetry.addData("2ND TIME > 7 and timer not up", range.cmUltrasonic());
                telemetry.update();
                idle();
            }
            if (outakeTimer.milliseconds() < 1100) {
                twoPastRange = true;
            } else if (outakeTimer.milliseconds() > 1100) {
                twoPastRange = false;
            }
        }
        stop_tracks();
        stop_intake();
        telemetry.addData("onePastRange", onePastRange);
        telemetry.addData("twoPastRange", twoPastRange);
        telemetry.update();
        driveForwardDistance(0.9, 60);

        powerDrive(-0.6, -0.6);
        outakeTimer.reset();
        while (outakeTimer.milliseconds() < 500 && opModeIsActive()) {
            idle();
        }
        powerDrive(0.0, 0.0);

    }

    public void count_outtaked_glyphs() {

        //if (range.cmUltrasonic() > 5
 && current == TWO_IN
) {
        outakeTimer.reset();
        while (range.cmUltrasonic() > 7 && outakeTimer.milliseconds() < 2000 && opModeIsActive()) {
            idle();
        }
        //}
        if (outakeTimer.milliseconds() < 2000) {
            onePastRange = true;
        }

if (range.cmUltrasonic() < 5) {
            onePastRange = true;
        }


        else if (outakeTimer.milliseconds() > 2000) {
            onePastRange = false;
        }

        telemetry.addData("onePastRange", onePastRange);
        telemetry.update();
        sleep(5000);

    }

    public void world_85_outtakeGlyph() {
        run_tracks_up(1.0);
        sleep(250);
        setFlipperPos(0.0);
        sleep(150);
        stop_tracks();

        sleep(600);
        setFlipperPos(1.0);
    }


    public void resetAngle() {
        lastAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        globalAngle = 0;
    }

    public double getAngle() {
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        double deltaAngle = angles.firstAngle - lastAngles.firstAngle;

        if (deltaAngle < -180)
            deltaAngle += 360;
        else if (deltaAngle > 180)
            deltaAngle -= 360;

        globalAngle += deltaAngle;

        lastAngles = angles;

        return globalAngle;
    }

    public void run_intake_in(double speed) {
        intake1.setPower(-speed);
        intake2.setPower(speed);
    }

    public void run_intake_out(double speed) {
        intake1.setPower(speed);
        intake2.setPower(-speed);
    }

    public void stop_intake() {

        intake1.setPower(0.0);
        intake2.setPower(0.0);

    }

    public void run_tracks_down(double speed) {
        lift1.setPower(-speed);
        lift2.setPower(speed);
    }

    public void run_tracks_up(double speed) {
        lift1.setPower(speed);
        lift2.setPower(-speed);
    }

    public void stop_tracks() {
        lift1.setPower(0.0);
        lift2.setPower(0.0);
    }

    public void drop_intake() {
        release.setPosition(1.0);
        sleep(400);
        release.setPosition(0.8);
    }

    public void jewel_sequence_red() {

        if (sensorColorRight.red() > sensorColorRight.blue()) {

            driveBackwardDistance(0.2, jewel_knock_counts);

            arm_right.setPosition(right_arm_up);

            sleep(jewel_wait_time);

            driveForwardDistance(jewel_hit_speed, jewel_knock_counts);

        } else if (sensorColorRight.red() < sensorColorRight.blue()) {

            driveForwardDistance(0.2, jewel_knock_counts);

            arm_right.setPosition(right_arm_up);

            sleep(jewel_wait_time);

            driveBackwardDistance(jewel_hit_speed, jewel_knock_counts);

        } else {
            telemetry.clear();
            telemetry.addLine("No color Found!");
            telemetry.update();

            arm_right.setPosition(right_arm_slightly_up);

            // CHECK COLOR AGAIN

            sleep(400);

            if (sensorColorRight.red() > sensorColorRight.blue()) {

                arm_right.setPosition(right_arm_down);


                driveBackwardDistance(0.2, jewel_knock_counts);

                arm_right.setPosition(right_arm_up);

                sleep(jewel_wait_time);

                driveForwardDistance(jewel_hit_speed, jewel_knock_counts);

            } else if (sensorColorRight.red() < sensorColorRight.blue()) {

                arm_right.setPosition(right_arm_down);


                driveForwardDistance(0.2, jewel_knock_counts);

                arm_right.setPosition(right_arm_up);

                sleep(jewel_wait_time);

                driveBackwardDistance(jewel_hit_speed, jewel_knock_counts);

            } else {

                telemetry.addLine("Still no color Found!!! We need to keep going.");
                telemetry.update();

                arm_right.setPosition(right_arm_up);

            }
        }

        sleep(1000);

        powerDrive(0.0, 0.0);// Ensure motors are off
    }

    public void test_jewel_red() {
        if (sensorColorRight.red() > sensorColorRight.blue()) {
            telemetry.addData("RED > BlUE", sensorColorRight.red());
            telemetry.update();
        } else if (sensorColorRight.red() < sensorColorRight.blue()) {
            telemetry.addData("BLUE > RED", sensorColorRight.blue());
            telemetry.update();
        }
        telemetry.update();
    }

    public void jewel_sequence_red2() {

        if (sensorColorRight.red() > sensorColorRight.blue()) {

            flick_right.setPosition(right_flick_moved_back);

            sleep(flick_time);

            arm_right.setPosition(right_arm_up);

        } else if (sensorColorRight.red() < sensorColorRight.blue()) {

            flick_right.setPosition(right_flick_moved_front);

            sleep(flick_time);

            arm_right.setPosition(right_arm_up);

        } else {
            telemetry.clear();
            telemetry.addLine("No color Found!");
            telemetry.update();

            arm_right.setPosition(right_arm_slightly_up);

            // CHECK COLOR AGAIN

            sleep(400);

            if (sensorColorRight.red() > sensorColorRight.blue()) {

                arm_right.setPosition(right_arm_down);

                flick_right.setPosition(right_flick_moved_back);

                sleep(flick_time);

                arm_right.setPosition(right_arm_up);

            } else if (sensorColorRight.red() < sensorColorRight.blue()) {

                arm_right.setPosition(right_arm_down);

                flick_right.setPosition(right_flick_moved_front);

                sleep(flick_time);

                arm_right.setPosition(right_arm_up);

            } else {

                telemetry.addLine("Still no color Found!!! We need to keep going.");
                telemetry.update();

                arm_right.setPosition(right_arm_up);


            }
        }

        flick_right.setPosition(right_flick_teleop_position);

        sleep(800);

        powerDrive(0.0, 0.0);// Ensure motors are off
    }

    public void jewel_sequence_red3() {
        flick_right.setPosition(right_flick_start);
        arm_right.setPosition(right_arm_down);

        sleep(500);//600

        if (sensorColorRight.red() > sensorColorRight.blue()) {

            flick_right.setPosition(right_flick_moved_back);

            sleep(flick_time);

            arm_right.setPosition(right_arm_up);

        } else if (sensorColorRight.red() < sensorColorRight.blue()) {

            flick_right.setPosition(right_flick_moved_front);

            sleep(flick_time);

            arm_right.setPosition(right_arm_up);

        } else {
            telemetry.clear();
            telemetry.addLine("No color Found!");
            telemetry.update();

            arm_right.setPosition(right_arm_slightly_up);

            // CHECK COLOR AGAIN

            sleep(400);

            if (sensorColorRight.red() > sensorColorRight.blue()) {

                arm_right.setPosition(right_arm_down);

                flick_right.setPosition(right_flick_moved_back);

                sleep(flick_time);

                arm_right.setPosition(right_arm_up);

            } else if (sensorColorRight.red() < sensorColorRight.blue()) {

                arm_right.setPosition(right_arm_down);

                flick_right.setPosition(right_flick_moved_front);

                sleep(flick_time);

                arm_right.setPosition(right_arm_up);

            } else {

                telemetry.addLine("Still no color Found!!! We need to keep going.");
                telemetry.update();

                arm_right.setPosition(right_arm_up);


            }
        }

        flick_right.setPosition(right_flick_teleop_position);

        sleep(200);
    }

    public void jewel_sequence_blue() {

        if (sensorColorLeft.red() < sensorColorLeft.blue()) {

            driveBackwardDistance(0.2, jewel_knock_counts);

            arm_left.setPosition(left_arm_up);

            sleep(jewel_wait_time);

            driveForwardDistance(jewel_hit_speed, jewel_knock_counts);

        } else if (sensorColorLeft.red() > sensorColorLeft.blue()) {

            driveForwardDistance(0.2, jewel_knock_counts);

            arm_left.setPosition(left_arm_up);

            sleep(jewel_wait_time);

            driveBackwardDistance(jewel_hit_speed, jewel_knock_counts);

        } else {
            telemetry.clear();
            telemetry.addLine("No color Found!");
            telemetry.update();

            arm_left.setPosition(left_arm_slightly_up);

            // CHECK COLOR AGAIN

            sleep(400);

            if (sensorColorLeft.red() < sensorColorLeft.blue()) {

                arm_left.setPosition(left_arm_down);


                driveBackwardDistance(0.2, jewel_knock_counts);

                arm_left.setPosition(left_arm_up);

                sleep(jewel_wait_time);

                driveForwardDistance(jewel_hit_speed, jewel_knock_counts);


            } else if (sensorColorLeft.red() > sensorColorLeft.blue()) {

                arm_left.setPosition(left_arm_down);


                driveForwardDistance(0.2, jewel_knock_counts);

                arm_left.setPosition(left_arm_up);

                sleep(jewel_wait_time);

                driveBackwardDistance(jewel_hit_speed, jewel_knock_counts);


            } else {

                telemetry.addLine("Still no color Found!!! We need to keep going.");
                telemetry.update();

                arm_left.setPosition(left_arm_up);
            }
        }

        sleep(1000);

        powerDrive(0.0, 0.0);// Ensure motors are off
    }

    public void jewel_sequence_blue2() {
        if (sensorColorLeft.red() > sensorColorLeft.blue()) {

            flick_left.setPosition(left_flick_moved_front);

            sleep(flick_time);

            arm_left.setPosition(left_arm_up);

        } else if (sensorColorLeft.red() < sensorColorLeft.blue()) {

            flick_left.setPosition(left_flick_moved_back);

            sleep(flick_time);

            arm_left.setPosition(left_arm_up);

        } else {
            telemetry.clear();
            telemetry.addLine("No color Found!");
            telemetry.update();

            arm_left.setPosition(left_arm_slightly_up);

            // CHECK COLOR AGAIN

            sleep(400);

            if (sensorColorLeft.red() > sensorColorLeft.blue()) {

                arm_left.setPosition(left_arm_down);

                flick_left.setPosition(left_flick_moved_front);

                sleep(flick_time);

                arm_left.setPosition(left_arm_up);

            } else if (sensorColorLeft.red() < sensorColorLeft.blue()) {

                arm_left.setPosition(left_arm_down);

                flick_left.setPosition(left_flick_moved_back);

                sleep(flick_time);

                arm_left.setPosition(left_arm_up);

            } else {

                telemetry.addLine("Still no color Found!!! We need to keep going.");
                telemetry.update();

                arm_left.setPosition(left_arm_up);

            }
        }

        flick_left.setPosition(left_flick_teleop_position);

        sleep(1000);

        powerDrive(0.0, 0.0);// Ensure motors are off
    }

    public void jewel_sequence_blue3() {
        flick_left.setPosition(left_flick_start);
        arm_left.setPosition(left_arm_down);

        sleep(500);

        if (sensorColorLeft.red() > sensorColorLeft.blue()) {

            flick_left.setPosition(left_flick_moved_front);

            sleep(flick_time);

            arm_left.setPosition(left_arm_up);

        } else if (sensorColorLeft.red() < sensorColorLeft.blue()) {

            flick_left.setPosition(left_flick_moved_back);

            sleep(flick_time);

            arm_left.setPosition(left_arm_up);

        } else {
            telemetry.clear();
            telemetry.addLine("No color Found!");
            telemetry.update();

            arm_left.setPosition(left_arm_slightly_up);

            // CHECK COLOR AGAIN

            sleep(400);

            if (sensorColorLeft.red() > sensorColorLeft.blue()) {

                arm_left.setPosition(left_arm_down);

                flick_left.setPosition(left_flick_moved_front);

                sleep(flick_time);

                arm_left.setPosition(left_arm_up);

            } else if (sensorColorLeft.red() < sensorColorLeft.blue()) {

                arm_left.setPosition(left_arm_down);

                flick_left.setPosition(left_flick_moved_back);

                sleep(flick_time);

                arm_left.setPosition(left_arm_up);

            } else {

                telemetry.addLine("Still no color Found!!! We need to keep going.");
                telemetry.update();

                arm_left.setPosition(left_arm_up);

            }
        }

        flick_left.setPosition(left_flick_teleop_position);

        sleep(200);
    }

    public void powerDrive(double left, double right) {
        power_left = left;
        power_right = right;
        powerDrive2(left, right);
    }

    public void powerDrive2(double left, double right) {
        motor_drive_left_front.setPower(left);
        motor_drive_left_back.setPower(left);
        motor_drive_right_front.setPower(right);
        motor_drive_right_back.setPower(right);
    }

    public void driveForwardDistance(double power, int distance) {
        distance += encoder_drive_left.getCurrentPosition() + encoder_drive_right.getCurrentPosition();
        power_left = power;
        power_right = power;
        powerDrive2(power, power);
        while (encoder_drive_left.getCurrentPosition() + encoder_drive_right.getCurrentPosition() < distance && opModeIsActive()) {
            idle();
        }
        powerDrive2(0.0, 0.0);
    }

    public void driveForwardDistance(double power, int distance, int timeout) {
        ElapsedTime timeofout = new ElapsedTime();
        distance += encoder_drive_left.getCurrentPosition() + encoder_drive_right.getCurrentPosition();
        power_left = power;
        power_right = power;
        powerDrive2(power, power);
        while (encoder_drive_left.getCurrentPosition() + encoder_drive_right.getCurrentPosition() < distance && timeofout.milliseconds() < timeout && opModeIsActive()) {
            idle();
        }
        powerDrive2(0.0, 0.0);
    }

    public void driveForwardDistanceDontStop(double power, int distance) {
        distance += encoder_drive_left.getCurrentPosition() + encoder_drive_right.getCurrentPosition();
        power_left = power;
        power_right = power;
        powerDrive2(power, power);
        while (encoder_drive_left.getCurrentPosition() + encoder_drive_right.getCurrentPosition() < distance && opModeIsActive()) {
            idle();
        }
    }

    public void driveForwardDistance_gyro(double power, int distance, int target, double correction_percent) { // NEEDS CHANGED BACK
        distance += encoder_drive_left.getCurrentPosition() + encoder_drive_right.getCurrentPosition();
        power_left = power;
        power_right = power;
        powerDrive(power, power);
        while (encoder_drive_left.getCurrentPosition() + encoder_drive_right.getCurrentPosition() < distance && opModeIsActive()) {
            update_gyro_straight(target, correction_percent);

            telemetry.addData("Left position: ", encoder_drive_left.getCurrentPosition());
            telemetry.addData("Right position: ", encoder_drive_right.getCurrentPosition());
            telemetry.update();
            idle();
        }
        powerDrive(0.0, 0.0);
    }

    public void driveForwardDistance_gyro_DontStop(double power, int distance, int target, double correction_percent) {
        distance += encoder_drive_left.getCurrentPosition() + encoder_drive_right.getCurrentPosition();
        power_left = power;//       Set the left power to the requested
        power_right = power;//      Set the right power to the requested power
        powerDrive(power, power);//        Power the motors through the powerDrive2 method
        while (encoder_drive_left.getCurrentPosition() + encoder_drive_right.getCurrentPosition() < distance && opModeIsActive()) {
            update_gyro_straight(target, correction_percent);
            idle();
        }
    }

    public void driveBackwardDistance(double power, int distance) {
        distance = -distance;
        distance = (distance + encoder_drive_left.getCurrentPosition() + encoder_drive_right.getCurrentPosition());
        power_left = power;
        power_right = power;
        powerDrive(-power, -power);
        while (encoder_drive_left.getCurrentPosition() + encoder_drive_right.getCurrentPosition() > distance && opModeIsActive()) {
            idle();
        }
        powerDrive(0.0, 0.0);
    }

    public void driveBackwardDistance(double power, int distance, int timeout) {
        distance = -distance;
        ElapsedTime timeofout = new ElapsedTime();
        distance = (distance + encoder_drive_left.getCurrentPosition() + encoder_drive_right.getCurrentPosition());
        power_left = power;
        power_right = power;
        powerDrive(-power, -power);
        while (encoder_drive_left.getCurrentPosition() + encoder_drive_right.getCurrentPosition() > distance && timeofout.milliseconds() < timeout && opModeIsActive()) {
            idle();
        }
        powerDrive(0.0, 0.0);
    }

    public void driveBackwardDistanceDontStop(double power, int distance) {
        distance = -distance;
        distance = (distance + encoder_drive_left.getCurrentPosition() + encoder_drive_right.getCurrentPosition());
        power_left = power;
        power_right = power;
        powerDrive(-power, -power);
        while (encoder_drive_left.getCurrentPosition() + encoder_drive_right.getCurrentPosition() > distance && opModeIsActive()) {
            idle();
        }
    }

    public void driveBackwardDistance_gyro(double power, int distance, double target, double correction_percent, int timeout) {
        ElapsedTime timeofout = new ElapsedTime();
        distance = -distance;
        distance = (distance + encoder_drive_left.getCurrentPosition() + encoder_drive_right.getCurrentPosition());
        power_left = -power;
        power_right = -power;
        powerDrive(-power, -power);
        while (encoder_drive_left.getCurrentPosition() + encoder_drive_right.getCurrentPosition() > distance && timeofout.milliseconds() < timeout && opModeIsActive()) {
            update_gyro_straight(target, correction_percent);
            idle();
        }
        powerDrive(0.0, 0.0);
    }

    public void turnRightGyroVelocityVortex(int targetZ, double power, double correctionPower, int timeBeforeCorrection) {
        double IntegratedZValue = getAngle();

        if (IntegratedZValue > targetZ) {
            while (IntegratedZValue > targetZ && opModeIsActive()) {
                powerDrive2(power, -power);
                idle();
            }
            powerDrive2(0.0, 0.0);
        }
        sleep(timeBeforeCorrection);
        if (IntegratedZValue < targetZ) {
            powerDrive2(-correctionPower, correctionPower);
            while (IntegratedZValue < targetZ && opModeIsActive()) {
                idle();
            }
            powerDrive2(0.0, 0.0);
        }
    }

    public void turnRightGyro(int targetZ, double power, double correctionPower, int timeBeforeCorrection) {
        //float Accumulated = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES).firstAngle;
        double Accumulated = getAngle();

        if (Accumulated > targetZ) {
            while (Accumulated > targetZ && opModeIsActive()) {
                powerDrive2(-power, power); // not negative, negative is how it was
                idle();
            }
            powerDrive2(0.0, 0.0);
        }
        sleep(timeBeforeCorrection);
        if (Accumulated < targetZ) {//<
            powerDrive2(-correctionPower, correctionPower);
            while (Accumulated < targetZ && opModeIsActive()) {
                Accumulated = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES).firstAngle;
                idle();
            }
            powerDrive2(0.0, 0.0);
        }
    }

    public void turnRightGyro_dontCorrect(int targetZ, double power) {
        //float Accumulated = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES).firstAngle;
        double Accumulated = getAngle();

        if (Accumulated < targetZ) {
            powerDrive2(power, -power);
            while (Accumulated < targetZ && opModeIsActive()) {
                Accumulated = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES).firstAngle;

                idle();
            }
        }
else if(Accumulated <= targetZ + 2 && Accumulated >= targetZ - 2) {
                powerDrive2(0.0, 0.0);
            }

        powerDrive2(0.0, 0.0);

    }

    public void turnRightGyroNew(int targetZ, double power) {
        //float Accumulated = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES).firstAngle;
        double Accumulated = getAngle();

        if (Accumulated > 0 && Accumulated < targetZ) {
            powerDrive2(power, -power);
            while (Accumulated < targetZ && opModeIsActive()) {
                Accumulated = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES).firstAngle;
                telemetry.addData("IMU data: ", Accumulated);
                telemetry.update();
                idle();
            }
            powerDrive2(0.0, 0.0);
        } else if (Accumulated < 0 && Accumulated > targetZ) {
            powerDrive2(power, -power);
            while (Accumulated > targetZ && opModeIsActive()) {
                Accumulated = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES).firstAngle;
                telemetry.addData("IMU data: ", Accumulated);
                telemetry.update();
                idle();
            }
            powerDrive2(0.0, 0.0);
        }
    }

    public void turnLeftGyro(int targetZ, double power, double correctionPower, int timeBeforeCorrection) {
        //float Accumulated = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES).firstAngle;
        double Accumulated = getAngle();
        if (Accumulated < targetZ) {
            powerDrive2(-power, power);
            while (Accumulated < targetZ && opModeIsActive()) {
                idle();
            }
            powerDrive2(0.0, 0.0);
        }
        sleep(timeBeforeCorrection);
        if (Accumulated > targetZ) {
            powerDrive2(correctionPower, -correctionPower);
            while (Accumulated > targetZ && opModeIsActive()) {
                Accumulated = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES).firstAngle;
                idle();
            }
            powerDrive2(0.0, 0.0);
        }
    }

    public void turnLeftGyro_dontCorrect(int targetZ, double power) {
        //float Accumulated = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES).firstAngle;
        double Accumulated = getAngle();
        if (Accumulated < targetZ) {
            powerDrive2(-power, power);
            while (Accumulated < targetZ && opModeIsActive()) {
                Accumulated = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES).firstAngle;
                idle();
            }
            powerDrive2(0.0, 0.0);
        }
    }

    public void strafeBackwardLeftGyro(int targetZ, double power, double correctionPower, int timeBeforeCorrection) {

        double Accumulated = getAngle();
        if (Accumulated < targetZ) {
            powerDrive2(-power, 0.0);
            while (Accumulated < targetZ && opModeIsActive()) {
                Accumulated = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES).firstAngle;
                idle();
            }
            powerDrive2(0.0, 0.0);
        }
        sleep(timeBeforeCorrection);
        if (Accumulated > targetZ) {
            powerDrive2(correctionPower, 0.0);
            while (Accumulated > targetZ && opModeIsActive()) {
                Accumulated = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES).firstAngle;
                idle();
            }
            powerDrive2(0.0, 0.0);
        }
    }

    public void strafeForwardLeftGyro(int targetZ, double power, double correctionPower, int timeBeforeCorrection) {
        //float Accumulated = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES).firstAngle;
        double Accumulated = getAngle();
        if (Accumulated < targetZ) {

            powerDrive2(0.0, power);
            while (Accumulated < targetZ && opModeIsActive()) {
                idle();
            }
            powerDrive2(0.0, 0.0);
        }
        sleep(timeBeforeCorrection);
        if (Accumulated > targetZ) {
            powerDrive2(0.0, -power);
            while (Accumulated > targetZ && opModeIsActive()) {
                Accumulated = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES).firstAngle;
                idle();
            }
            powerDrive2(0.0, 0.0);
        }
    }

    public void strafeForwardRightGyro(int targetZ, double power, double correctionPower, int timeBeforeCorrection) {
        //float Accumulated = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES).firstAngle;
        double Accumulated = getAngle();
        if (Accumulated > targetZ) {
            powerDrive2(power, 0.0);
            while (Accumulated > targetZ && opModeIsActive()) {
                idle();
            }
            powerDrive2(0.0, 0.0);
        }
        sleep(timeBeforeCorrection);
        if (Accumulated < targetZ) {
            powerDrive2(-power, 0.0);
            while (Accumulated < targetZ && opModeIsActive()) {
                Accumulated = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES).firstAngle;
                idle();
            }
            powerDrive2(0.0, 0.0);
        }
    }

    public void strafeBackwardRightGyro(int targetZ, double power, double correctionPower, int timeBeforeCorrection) {
        //float Accumulated = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES).firstAngle;
        double Accumulated = getAngle();
        if (Accumulated > targetZ) {
            powerDrive2(0.0, -power);
            while (Accumulated > targetZ && opModeIsActive()) {
                idle();
            }
            powerDrive2(0.0, 0.0);
        }
        sleep(timeBeforeCorrection);
        if (Accumulated < targetZ) {
            powerDrive2(0.0, power);
            while (Accumulated < targetZ && opModeIsActive()) {
                Accumulated = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES).firstAngle;
                idle();
            }
            powerDrive2(0.0, 0.0);
        }
    }

    public void strafeBackwardLeftGyro_dontCorrect(int targetZ, double power) {
        //float Accumulated = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES).firstAngle;
        double Accumulated = getAngle();
        if (Accumulated < targetZ) {
            powerDrive2(-power, 0.0);
            while (Accumulated < targetZ && opModeIsActive()) {
                Accumulated = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES).firstAngle;
                idle();
            }
            powerDrive2(0.0, 0.0);
        }
    }

    public void strafeBackwardLeftGyro_dontCorrect_with_timeout(int targetZ, double power, int timeout) {
        //float Accumulated = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES).firstAngle;
        ElapsedTime timer = new ElapsedTime();
        double Accumulated = getAngle();
        if (Accumulated < targetZ) {
            powerDrive2(-power, 0.0);
            timer.reset();
            while (Accumulated < targetZ && timer.milliseconds() < timeout && opModeIsActive()) {
                Accumulated = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES).firstAngle;
                idle();
            }
            powerDrive2(0.0, 0.0);
        }
    }

    public void strafeForwardLeftGyro_dontCorrect(int targetZ, double power) {
        //float Accumulated = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES).firstAngle;
        double Accumulated = getAngle();
        if (Accumulated < targetZ) {

            powerDrive2(0.0, power);
            while (Accumulated < targetZ && opModeIsActive()) {
                Accumulated = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES).firstAngle;
                idle();
            }
            powerDrive2(0.0, 0.0);
        }
    }

    public void strafeForwardRightGyro_dontCorrect(int targetZ, double power) {
        //float Accumulated = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES).firstAngle;
        double Accumulated = getAngle();
        if (Accumulated > targetZ) {
            powerDrive2(power, 0.0);
            while (Accumulated > targetZ && opModeIsActive()) {
                Accumulated = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES).firstAngle;
                idle();
            }
            powerDrive2(0.0, 0.0);
        }
    }

    public void strafeBackwardRightGyro_dontCorrect(int targetZ, double power) {
        //float Accumulated = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES).firstAngle;
        double Accumulated = getAngle();
        if (Accumulated > targetZ) {
            powerDrive2(0.0, -power);
            while (Accumulated > targetZ && opModeIsActive()) {
                Accumulated = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES).firstAngle;
                idle();
            }
            powerDrive2(0.0, 0.0);
        }
    }

    public void strafeBackwardRightGyro_dontCorrect_with_timeout(int targetZ, double power, int timeout) {
        //float Accumulated = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES).firstAngle;
        ElapsedTime timer = new ElapsedTime();
        double Accumulated = getAngle();
        if (Accumulated > targetZ) {
            powerDrive2(0.0, -power);
            while (Accumulated > targetZ && timer.milliseconds() < timeout && opModeIsActive()) {
                Accumulated = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES).firstAngle;
                idle();
            }
            powerDrive2(0.0, 0.0);
        }
    }

    public void update_gyro_straight(double target, double correction_percent) {
        //double floatzAccumulated = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES).firstAngle;

        double zAccumulated = getAngle();

        double powerOffSet = (zAccumulated - target) * correction_percent;

        double left = clip(power_left + powerOffSet, -1, 1);
        double right = clip(power_right - powerOffSet, -1, 1);
        powerDrive2(left, right);

    }

    public void setFlipperPos(double pos) {
        pos = clip(pos, 0, 1);
        flipper1.setPosition(pos);
        flipper2.setPosition(1.0 - pos);
    }


    abstract public void runOpMode() throws InterruptedException;//     Because this is a LinearOpMode runOpMode is required, but is not actually used since this class is not run but extended
}
*/
