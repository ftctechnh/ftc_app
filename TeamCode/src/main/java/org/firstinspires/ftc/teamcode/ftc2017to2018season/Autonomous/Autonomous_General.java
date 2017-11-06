package org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcontroller.external.samples.ConceptVuforiaNavigation;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * This OpMode illustrates the basics of using the Vuforia engine to determine
 * the identity of Vuforia VuMarks encountered on the field. The code is structured as
 * a LinearOpMode. It shares much structure with {@link ConceptVuforiaNavigation}; we do not here
 * duplicate the core Vuforia documentation found there, but rather instead focus on the
 * differences between the use of Vuforia for navigation vs VuMark identification.
 *
 * @see ConceptVuforiaNavigation
 * @see VuforiaLocalizer
 * @see VuforiaTrackableDefaultListener
 * see  ftc_app/doc/tutorial/FTC_FieldCoordinateSystemDefinition.pdf
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list.
 *
 * IMPORTANT: In order to use this OpMode, you need to obtain your own Vuforia license key as
 * is explained in {@link ConceptVuforiaNavigation}.
 */

@Autonomous(name="Concept: VuMark Id", group ="Concept")
@Disabled
public class Autonomous_General extends LinearOpMode {

    public static double COUNTS_PER_MOTOR_REV;    // eg: TETRIX Motor Encoder
    public static double DRIVE_GEAR_REDUCTION;     // 56/24
    public static double WHEEL_PERIMETER_CM;     // For figuring circumference
    public static double COUNTS_PER_CM;
    double P_TURN_COEFF = 0.1;
    double TURN_THRESHOLD = 1;
    public DcMotor front_right_motor;
    public DcMotor front_left_motor;
    public DcMotor back_right_motor;
    public DcMotor back_left_motor;
    public ModernRoboticsI2cGyro gyro;
    public ModernRoboticsI2cRangeSensor rangeSensor;
    public ModernRoboticsI2cRangeSensor rangeSensor2;

    public static final String TAG = "Vuforia VuMark Sample";

    OpenGLMatrix lastLocation = null;

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    VuforiaLocalizer vuforia;
    VuforiaTrackables relicTrackables;
    VuforiaTrackable relicTemplate;
    RelicRecoveryVuMark vuMark;

    public void initiate() {
        COUNTS_PER_MOTOR_REV = 1440;
        DRIVE_GEAR_REDUCTION = 1.5;
        WHEEL_PERIMETER_CM = 4*2.54* Math.PI;
        COUNTS_PER_CM = (COUNTS_PER_MOTOR_REV) /
                (WHEEL_PERIMETER_CM * DRIVE_GEAR_REDUCTION);
        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        //robot.init(hardwareMap);
        front_left_motor = hardwareMap.dcMotor.get("leftWheelMotorFront");
        front_right_motor = hardwareMap.dcMotor.get("rightWheelMotorFront");
        back_left_motor = hardwareMap.dcMotor.get("leftWheelMotorBack");
        back_right_motor = hardwareMap.dcMotor.get("rightWheelMotorBack");
        idle();


//        // Connect to motor (Assume standard left wheel)
//        // Change the text in quotes to match any motor name on your robot.
//        shooting_motor = hardwareMap.dcMotor.get("ballShooterMotor");
//        intake_motor = hardwareMap.dcMotor.get("ballCollectorMotor");
//        idle();
//
//        // gyro = hardwareMap.gyroSensor.get("gyro");
//
//        ODSBack = hardwareMap.opticalDistanceSensor.get("ODSBack");
//        idle();
//
//        ODSFront = hardwareMap.opticalDistanceSensor.get("ODSFront");
//        idle();
//        //baseline1 = ODSFront.getRawLightDetected();
//        baseline2 = ODSBack.getRawLightDetected();
//        idle();
//        baseline1 = ODSFront.getRawLightDetected();
//        idle();
//
//        autoBeaconPresser = hardwareMap.servo.get("ServoPress");
//
//
//        //Initiate sensors:
//        if (operation_beacon_press == true) {
//            gyro = hardwareMap.get(ModernRoboticsI2cGyro.class, "gyro");
//
//            rangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "rangeSensor");
//            bColorSensorLeft = hardwareMap.colorSensor.get("bColorSensorLeft");
//            bColorSensorLeft.setI2cAddress(I2cAddr.create8bit(0x3c));
//            bColorSensorLeft.enableLed(false);
//            idle();
//            if (initbColorSensorRight){
//                bColorSensorRight = hardwareMap.colorSensor.get("bColorSensorRight");
//                bColorSensorRight.setI2cAddress(I2cAddr.create8bit(0x70));
//                bColorSensorRight.enableLed(false);
//
//            }
//            idle();
//        }
        gyro = hardwareMap.get(ModernRoboticsI2cGyro.class, "gyro");
        rangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "rangeSensor");
        rangeSensor2 = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "rangeSensor2");
        // Send telemetry message to signify robot waiting;
//        telemetry.addData("Status", "Resetting Encoders");    //
//        telemetry.update();

        idle();

        front_left_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        front_right_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        back_left_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        back_right_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        idle();
        sleep(100);

        front_left_motor.setDirection(DcMotor.Direction.REVERSE);
        back_left_motor.setDirection(DcMotor.Direction.REVERSE);
        idle();
        sleep(100);

        front_left_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        front_right_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        back_left_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        back_right_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        idle();
        sleep(100);
//        shooting_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        intake_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
//        telemetry.addData("Path0", "Starting at %7d :%7d : %7d :%7d",
//                back_left_motor.getCurrentPosition(),
//                back_right_motor.getCurrentPosition(),
//                front_left_motor.getCurrentPosition(),
//                front_right_motor.getCurrentPosition());
//        telemetry.update();

    }

    public void straightDrive(double power) {
        /*front_left_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        back_left_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        front_right_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        back_right_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);*/
        back_left_motor.setPower(power);
        front_left_motor.setPower(power);
        back_right_motor.setPower(power);
        front_right_motor.setPower(power);
    }

    public void strafeRight(double speed){

        front_left_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        back_left_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        front_right_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        back_right_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        front_left_motor.setPower(speed);
        back_left_motor.setPower(-speed);
        front_right_motor.setPower(-speed);
        back_right_motor.setPower(speed);
    }

    public void strafeLeft(double speed){
        front_left_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        back_left_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        front_right_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        back_right_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        front_left_motor.setPower(-speed);
        back_left_motor.setPower(speed);
        front_right_motor.setPower(speed);
        back_right_motor.setPower(-speed);
    }

    public void turnLeft(double speed) {
        front_left_motor.setPower(-speed);
        back_left_motor.setPower(-speed);

        front_right_motor.setPower(speed);
        back_right_motor.setPower(speed);
    }

    public void turnRight(double speed) {
        front_right_motor.setPower(-speed);
        back_right_motor.setPower(-speed);

        front_left_motor.setPower(speed);
        back_left_motor.setPower(speed);
    }

    public void stopMotors() {
        front_right_motor.setPower(0);
        //idle();
        front_left_motor.setPower(0);
        //idle();
        back_right_motor.setPower(0);
        //idle();
        back_left_motor.setPower(0);
        //sleep(100);
        back_left_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //idle();
        front_left_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //idle();
        back_right_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // idle();
        front_right_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        idle();
        sleep(100);
    }

    public void encoderMecanumCrossDrive(double speed,
                                         double leftInches, double rightInches,
                                         double timeoutS, int direction) {
        int newLeftFrontTarget;
        int newRightFrontTarget;
        int newLeftBackTarget;
        int newRightBackTarget;
        double leftSpeed;
        double rightSpeed;

        // Ensure that the opmode is still active
        //  if (opModeIsActive())

        idle();
        sleep(100);

        // Determine new target position, and pass to motor controller
        newLeftFrontTarget = (int)(1.* (front_left_motor.getCurrentPosition() + (int) (leftInches * getCountsPerCm())));
        newRightFrontTarget = (int)(1.*(front_right_motor.getCurrentPosition() + (int) (rightInches * getCountsPerCm())));
        newLeftBackTarget = (int)(1.* (back_left_motor.getCurrentPosition() + (int) (leftInches * getCountsPerCm())));
        newRightBackTarget = (int)(1.*(back_right_motor.getCurrentPosition() + (int) (rightInches * getCountsPerCm())));

        if(direction == 4) {

            front_right_motor.setTargetPosition(newRightFrontTarget);
            //           back_right_motor.setTargetPosition(newRightTarget);
            // idle();
            back_left_motor.setTargetPosition(newLeftBackTarget);
            //          front_left_motor.setTargetPosition(newLeftTarget);
            back_left_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            //idle();
            back_right_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            // idle();
            front_left_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            //idle();
            front_right_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            telemetry.addData("current:" + back_left_motor.getCurrentPosition(),back_left_motor.getTargetPosition());
//            telemetry.addData("current:" + back_right_motor.getCurrentPosition(),back_right_motor.getTargetPosition());
//            telemetry.addData("current:" + front_left_motor.getCurrentPosition(),front_left_motor.getTargetPosition());
//            telemetry.addData("current:" + front_right_motor.getCurrentPosition(),front_right_motor.getTargetPosition());
//            telemetry.update();
            //  idle();
            front_right_motor.setPower(Math.abs(speed));
            //idle();
            back_right_motor.setPower(Math.abs(0));
            //  idle();
            back_left_motor.setPower(Math.abs(speed));
            // idle();
            front_left_motor.setPower(Math.abs(0));
            // idle();
            while (opModeIsActive() &&
                    (front_right_motor.isBusy() && back_left_motor.isBusy())) {
                front_right_motor.setPower(Math.abs(speed));
                //idle();
                back_right_motor.setPower(Math.abs(0));
                //  idle();
                back_left_motor.setPower(Math.abs(speed));
                // idle();
                front_left_motor.setPower(Math.abs(0));
                // idle();
                idle();
            }
        }
        else if (direction == 1){

            //front_right_motor.setTargetPosition(newRightTarget);
            back_right_motor.setTargetPosition(newRightBackTarget);
            //idle();
            //back_left_motor.setTargetPosition(newLeftTarget);
            front_left_motor.setTargetPosition(newLeftFrontTarget);
            // idle();
            back_left_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            //idle();
            back_right_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            // idle();
            front_left_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            //  idle();
            front_right_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            telemetry.addData("current:" + back_left_motor.getCurrentPosition(),back_left_motor.getTargetPosition());
//            telemetry.addData("current:" + back_right_motor.getCurrentPosition(),back_right_motor.getTargetPosition());
//            telemetry.addData("current:" + front_left_motor.getCurrentPosition(),front_left_motor.getTargetPosition());
//            telemetry.addData("current:" + front_right_motor.getCurrentPosition(),front_right_motor.getTargetPosition());
//            telemetry.update();
            front_right_motor.setPower(Math.abs(0));
            //idle();
            back_right_motor.setPower(Math.abs(speed));
            // idle();
            back_left_motor.setPower(Math.abs(0));
            // idle();
            front_left_motor.setPower(Math.abs(speed));
            // idle();
            while (opModeIsActive() &&
                    (front_left_motor.isBusy() && back_right_motor.isBusy())) {
                front_right_motor.setPower(Math.abs(0));
                //idle();
                back_right_motor.setPower(Math.abs(speed));
                // idle();
                back_left_motor.setPower(Math.abs(0));
                // idle();
                front_left_motor.setPower(Math.abs(speed));
                // idle();
                idle();
            }
        }
        else if(direction == 2){

            front_right_motor.setTargetPosition(-newRightFrontTarget);
            //           back_right_motor.setTargetPosition(newRightTarget);
            // idle();
            back_left_motor.setTargetPosition(-newLeftBackTarget);
            //          front_left_motor.setTargetPosition(newLeftTarget);
            // idle();
            back_left_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            // idle();
            back_right_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            //  idle();
            front_left_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            //idle();
            front_right_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            telemetry.addData("current:" + back_left_motor.getCurrentPosition(),back_left_motor.getTargetPosition());
//            telemetry.addData("current:" + back_right_motor.getCurrentPosition(),back_right_motor.getTargetPosition());
//            telemetry.addData("current:" + front_left_motor.getCurrentPosition(),front_left_motor.getTargetPosition());
//            telemetry.addData("current:" + front_right_motor.getCurrentPosition(),front_right_motor.getTargetPosition());
//            telemetry.update();
            front_right_motor.setPower(Math.abs(speed));
            // idle();
            back_right_motor.setPower(Math.abs(0));
            //  idle();
            back_left_motor.setPower(Math.abs(speed));
            //  idle();
            front_left_motor.setPower(Math.abs(0));
            //  idle();
            while (opModeIsActive() &&
                    (back_left_motor.isBusy() && front_right_motor.isBusy())) {
                front_right_motor.setPower(Math.abs(speed));
                // idle();
                back_right_motor.setPower(Math.abs(0));
                //  idle();
                back_left_motor.setPower(Math.abs(speed));
                //  idle();
                front_left_motor.setPower(Math.abs(0));
                //  idle();
                idle();
            }
        }
        else if(direction == 3) {

            //front_right_motor.setTargetPosition(newRightTarget);
            back_right_motor.setTargetPosition(-newRightBackTarget);
            // idle();
            //back_left_motor.setTargetPosition(newLeftTarget);
            front_left_motor.setTargetPosition(-newLeftFrontTarget);
            //  idle();
            back_left_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            // idle();
            back_right_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            // idle();
            front_left_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            // idle();
            front_right_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            telemetry.addData("current:" + back_left_motor.getCurrentPosition(),back_left_motor.getTargetPosition());
//            telemetry.addData("current:" + back_right_motor.getCurrentPosition(),back_right_motor.getTargetPosition());
//            telemetry.addData("current:" + front_left_motor.getCurrentPosition(),front_left_motor.getTargetPosition());
//            telemetry.addData("current:" + front_right_motor.getCurrentPosition(),front_right_motor.getTargetPosition());
//            telemetry.update();
            front_right_motor.setPower(Math.abs(0));
            //  idle();
            back_right_motor.setPower(Math.abs(speed));
            //  idle();
            back_left_motor.setPower(Math.abs(0));
            //  idle();
            front_left_motor.setPower(Math.abs(speed));
            //  idle();
            while (opModeIsActive() &&
                    (front_left_motor.isBusy() && back_right_motor.isBusy())) {
                front_right_motor.setPower(Math.abs(0));
                //  idle();
                back_right_motor.setPower(Math.abs(speed));
                //  idle();
                back_left_motor.setPower(Math.abs(0));
                //  idle();
                front_left_motor.setPower(Math.abs(speed));
                //  idle();
                idle();
            }
        }

        idle();
        //if(leftInches != -rightInches)
                /*front_left_motor.setPower(Math.abs(leftSpeed)*Math.sin(degrees*Math.PI/180 + (Math.PI/4)));
                front_right_motor.setPower(Math.abs(rightSpeed)*Math.cos(degrees*Math.PI/180 + (Math.PI/4)));
                back_left_motor.setPower(Math.abs(leftSpeed)*Math.cos(degrees*Math.PI/180 + (Math.PI/4)));
                back_right_motor.setPower(Math.abs(rightSpeed)*Math.sin(degrees*Math.PI/180 + (Math.PI/4)));*/

        // keep looping while we are still active, and there is time left, and both motors are running.


        // Stop all motion;
        stopMotors();

    }
    public void encoderMecanumDrive(double speed,
                                    double rightInches, double leftInches,
                                    double timeoutS, int direction) {
        int newLeftFrontTarget;
        int newRightFrontTarget;
        int newLeftBackTarget;
        int newRightBackTarget;
        double leftSpeed;
        double rightSpeed;

        // Ensure that the opmode is still active
        //  if (opModeIsActive())

            /*back_left_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            back_right_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            front_left_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            front_right_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);*/


        sleep(100);

        // Determine new target position, and pass to motor controller
        newLeftFrontTarget = front_left_motor.getCurrentPosition() + (int) (leftInches * getCountsPerCm());
        newRightFrontTarget = front_right_motor.getCurrentPosition() + (int) (rightInches * getCountsPerCm());
        newLeftBackTarget = back_left_motor.getCurrentPosition() + (int) (leftInches * getCountsPerCm());
        newRightBackTarget = back_right_motor.getCurrentPosition() + (int) (rightInches * getCountsPerCm());

        if(direction == 0) {
            back_left_motor.setTargetPosition(newLeftBackTarget);
            //idle();
            back_right_motor.setTargetPosition(newRightBackTarget);
            //idle();
            front_left_motor.setTargetPosition(newLeftFrontTarget);
            //idle();
            front_right_motor.setTargetPosition(newRightFrontTarget);
            //idle();

        }
        else if (direction == 1){
            back_left_motor.setTargetPosition(-newLeftBackTarget);
            //idle();
            back_right_motor.setTargetPosition(newRightBackTarget);
            //idle();
            front_left_motor.setTargetPosition(newLeftFrontTarget);
            //idle();
            front_right_motor.setTargetPosition(-newRightFrontTarget);
            //idle();

        }
        else if(direction == -1){
            back_left_motor.setTargetPosition(newLeftBackTarget);
            //idle();
            back_right_motor.setTargetPosition(-newRightBackTarget);
            //idle();
            front_left_motor.setTargetPosition(-newLeftFrontTarget);
            //idle();
            front_right_motor.setTargetPosition(newRightFrontTarget);
            //idle();

        }
        idle();

        // reset the timeout time and start motion.
        if (Math.abs(leftInches) > Math.abs(rightInches)) {
            leftSpeed = speed;
            rightSpeed = (speed * rightInches) / leftInches;
        } else {
            rightSpeed = speed;
            leftSpeed = (speed * leftInches) / rightInches;
        }

        back_left_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //idle();
        back_right_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //idle();
        front_left_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //idle();
        front_right_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //idle();
//        telemetry.addData("current:" + back_left_motor.getCurrentPosition(),back_left_motor.getTargetPosition());
//        telemetry.addData("current:" + back_right_motor.getCurrentPosition(),back_right_motor.getTargetPosition());
//        telemetry.addData("current:" + front_left_motor.getCurrentPosition(),front_left_motor.getTargetPosition());
//        telemetry.addData("current:" + front_right_motor.getCurrentPosition(),front_right_motor.getTargetPosition());
//        telemetry.update();
        //  runtime.reset();
        //if(leftInches != -rightInches)
                /*front_left_motor.setPower(Math.abs(leftSpeed)*Math.sin(degrees*Math.PI/180 + (Math.PI/4)));
                front_right_motor.setPower(Math.abs(rightSpeed)*Math.cos(degrees*Math.PI/180 + (Math.PI/4)));
                back_left_motor.setPower(Math.abs(leftSpeed)*Math.cos(degrees*Math.PI/180 + (Math.PI/4)));
                back_right_motor.setPower(Math.abs(rightSpeed)*Math.sin(degrees*Math.PI/180 + (Math.PI/4)));*/
        back_left_motor.setPower(Math.abs(leftSpeed));
        //idle();
        back_right_motor.setPower(Math.abs(rightSpeed));
        //idle();
        front_left_motor.setPower(Math.abs(leftSpeed));
        //idle();
        front_right_motor.setPower(Math.abs(rightSpeed));
        //idle();









        // keep looping while we are still active, and there is time left, and both motors are running.
        while (opModeIsActive() &&
                (back_left_motor.isBusy() && back_right_motor.isBusy()&&
                        front_left_motor.isBusy() && front_right_motor.isBusy())) {
            back_left_motor.setPower(Math.abs(leftSpeed));
            //idle();
            back_right_motor.setPower(Math.abs(rightSpeed));
            //idle();
            front_left_motor.setPower(Math.abs(leftSpeed));
            //idle();
            front_right_motor.setPower(Math.abs(rightSpeed));
            //idle();
            idle();
        }
        idle();
        // Stop all motion;
        stopMotors();

    }
    public static double getCountsPerCm() {
        return COUNTS_PER_CM;
    }

    public void gyroTurn(double speed, double angle){

        telemetry.addData("starting gyro turn","-----");
        telemetry.update();

        while(opModeIsActive() && !onTargetAngle(speed, angle, P_TURN_COEFF)){
            telemetry.update();
            idle();
            telemetry.addData("-->","inside while loop :-(");
            telemetry.update();
        }
        stopMotors();
        telemetry.addData("done with gyro turn","-----");
        telemetry.update();
    }

    boolean onTargetAngle(double speed, double angle, double PCoeff){
        double error;
        double steer;
        boolean onTarget = false;
        double leftSpeed;
        double rightSpeed;

        //determine turm power based on error
        error = getError(angle);

        if (Math.abs(error) <= TURN_THRESHOLD){

            steer = 0.0;
            leftSpeed = 0.0;
            rightSpeed = 0.0;
            onTarget = true;
        }
        else{

            steer = getSteer(error, PCoeff);
            rightSpeed = speed * steer;
            leftSpeed = -rightSpeed;
            //leftSpeed = -5;
    }

        front_left_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        back_left_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        front_right_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        back_right_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        front_left_motor.setPower(leftSpeed);
        front_right_motor.setPower(rightSpeed);
        back_left_motor.setPower(leftSpeed);
        back_right_motor.setPower(rightSpeed);

        telemetry.addData("Target angle","%5.2f",angle);
        telemetry.addData("Error/Steer", "%5.2f/%5.2f", error, steer);
        telemetry.addData("speed", "%5.2f/%5.2f", leftSpeed, rightSpeed);

        return onTarget;
    }
    public double getError(double targetAngle){

        double robotError;

        robotError = targetAngle - gyro.getIntegratedZValue();
        //telemetry.addData("Zvalue","%5.2f",gyro.getIntegratedZValue());
        //telemetry.update();

        while(robotError > 180) robotError -= 360;

        while(robotError <= -180) robotError += 360;

        telemetry.addData("Robot Error","%5.2f",robotError);
        telemetry.update();

        return robotError;

    }

    public double getSteer(double error , double PCoeff){
        return Range.clip(error * PCoeff, -1 , 1);
    }
    public void vuforiaInit(boolean cameraView, boolean rearCamera){

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        VuforiaLocalizer.Parameters parameters;
        if (cameraView) {
            parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        }
        else
        {
            parameters = new VuforiaLocalizer.Parameters();
        }

        parameters.vuforiaLicenseKey = "AffveYv/////AAAAGQ5VbB9zQUgjlHWrneVac2MnNgfMDlq6EwI3tyURgRK6CHargOTFidfzKod6GLQwGD4m9MPLkR+0NfUrnY8+o8FqAKc" +
                "QbrAsjk8ONdkWYTPZDfoBRgDLNWRuB7LU1MOp9KqAWpXBJjvH5JCKF/Hxz+beHfVqdWQ0BVZdgGMXG4yEzLN5AI+4NIkQeLvI7Cwz5pIlksoH+rb/e6+YExoWZbQWhDTiR" +
                "iemlWjvDM1z2a0kteGDz0wTyHz48IkV4M0YsSQIFKwu3YB2a1vkB9FiRfMrBI+CyInjgNoO8V0EEOtRc6Vqsf3XbF3fGXricZUhl7RIl5M/IkFOgeAZ4ML+JcrjTqfZb2Yh3JNx1me524cK";

        if (rearCamera) {
            parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        }
        else {
            parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
        }

        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        setTargets();
    }

    public void setTargets(){
        relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary
    }

    //should go after play
    public void startTracking(){
        relicTrackables.activate();

    }

    //initializes vumark and returns whether its unknown or not
    public boolean vuMarkFound(){
        vuMark = RelicRecoveryVuMark.from(relicTemplate);
        if (vuMark != RelicRecoveryVuMark.UNKNOWN) {

                /* Found an instance of the template. In the actual game, you will probably
                 * loop until this condition occurs, then move on to act accordingly depending
                 * on which VuMark was visible. */
           // telemetry.addData("VuMark", "%s visible", vuMark);
           // telemetry.update();

            return true;
        }
        return false;
    }

    public double[] imageAngles(){
        if (!vuMarkFound()){
            return null;
        }
        OpenGLMatrix pose = ((VuforiaTrackableDefaultListener)relicTemplate.getListener()).getPose();
        telemetry.addData("Pose", format(pose));

                /* We further illustrate how to decompose the pose into useful rotational and
                 * translational components */
        double[] angles;
        double rX = 0,rY = 0,rZ = 0;
        if (pose != null) {
            VectorF trans = pose.getTranslation();
            Orientation rot = Orientation.getOrientation(pose, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
            // Extract the rotational components of the target relative to the robot
            rX = rot.firstAngle;
            rY = rot.secondAngle;
            rZ = rot.thirdAngle;
            angles = new double[]{rX,rY,rZ};
            return angles;
        }
        else{
            return null;
        }
    }

    public double[] imageTranslation(){
        if (!vuMarkFound()){
            return null;
        }
        OpenGLMatrix pose = ((VuforiaTrackableDefaultListener)relicTemplate.getListener()).getPose();
        telemetry.addData("Pose", format(pose));

                /* We further illustrate how to decompose the pose into useful rotational and
                 * translational components */
        double translation[];
        double tX = 0, tY = 0, tZ = 0;
        if (pose != null) {
            VectorF trans = pose.getTranslation();
            Orientation rot = Orientation.getOrientation(pose, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);

            // Extract the X, Y, and Z components of the offset of the target relative to the robot
            tX = trans.get(0);
            tY = trans.get(1);
            tZ = trans.get(2);
            translation = new double[]{tX, tY, tZ };

            return translation;
        }
        return null;
    }

    public void strafeRangeDistance(double distInCM, double speed, double rsBufffer, boolean dorangeSensor2, boolean strafe) {
    if(strafe) {
        if (!dorangeSensor2) {
            while ((rangeSensor.getDistance(DistanceUnit.CM)) > (distInCM - rsBufffer)) {
                strafeLeft(speed);
                telemetry.addData("Distance from Wall", rangeSensor.getDistance(DistanceUnit.CM));
                telemetry.addData("Target Distance", distInCM - rsBufffer);
                telemetry.update();
            }
            stopMotors();
            sleep(400);
            while ((rangeSensor.getDistance(DistanceUnit.CM) - rsBufffer) < (distInCM - rsBufffer)) {
                strafeRight(speed);
                telemetry.addData("Distance from Wall", rangeSensor.getDistance(DistanceUnit.CM));
                telemetry.addData("Target Distance", distInCM - rsBufffer);
                telemetry.update();
            }
            stopMotors();
            telemetry.addData("Final Distance from Wall", rangeSensor.getDistance(DistanceUnit.CM));
            telemetry.update();
        } else if (dorangeSensor2) {
            while (rangeSensor2.getDistance(DistanceUnit.CM) > (distInCM - rsBufffer)) {
                strafeRight(speed);
                telemetry.addData("Distance from Wall", rangeSensor.getDistance(DistanceUnit.CM));
                telemetry.addData("Target Distance", distInCM - rsBufffer);
                telemetry.update();
            }
            stopMotors();
            sleep(400);
            while ((rangeSensor2.getDistance(DistanceUnit.CM) - rsBufffer) < (distInCM - rsBufffer)) {
                strafeLeft(speed);
                telemetry.addData("Distance from Wall", rangeSensor.getDistance(DistanceUnit.CM));
                telemetry.addData("Target Distance", distInCM - rsBufffer);
                telemetry.update();
            }
            stopMotors();
            telemetry.addData("Final Distance from Wall", rangeSensor.getDistance(DistanceUnit.CM));
            telemetry.update();
        }
    }
    else if(!strafe){
            while ((rangeSensor.getDistance(DistanceUnit.CM)) > (distInCM - rsBufffer)) {
                straightDrive(-speed);
                telemetry.addData("Distance from Wall", rangeSensor.getDistance(DistanceUnit.CM));
                telemetry.addData("Target Distance", distInCM - rsBufffer);
                telemetry.update();
            }
            stopMotors();
            sleep(400);
            while ((rangeSensor.getDistance(DistanceUnit.CM) - rsBufffer) < (distInCM - rsBufffer)) {
                straightDrive(speed);
                telemetry.addData("Distance from Wall", rangeSensor.getDistance(DistanceUnit.CM));
                telemetry.addData("Target Distance", distInCM - rsBufffer);
                telemetry.update();
            }
            stopMotors();
            telemetry.addData("Final Distance from Wall", rangeSensor.getDistance(DistanceUnit.CM));
            telemetry.update();

        }
    }

    public Enum<RelicRecoveryVuMark> returnImage() {
        return vuMark;
    }


    @Override public void runOpMode() {}

    String format(OpenGLMatrix transformationMatrix) {
        return (transformationMatrix != null) ? transformationMatrix.formatAsTransform() : "null";
    }
}
