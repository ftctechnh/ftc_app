package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * Created by inspirationteam on 12/18/2016.
 */
//@Disabled
public class AutonomousGeneral_charlie_TestMotors extends LinearOpMode {
    public static double COUNTS_PER_MOTOR_REV;    // eg: TETRIX Motor Encoder
    public static double DRIVE_GEAR_REDUCTION;     // 56/24
    public static double WHEEL_PERIMETER_CM;     // For figuring circumference
    public static double COUNTS_PER_CM;
    public DcMotor front_right_motor;
    public DcMotor front_left_motor;
    public DcMotor back_right_motor;
    public DcMotor back_left_motor;

    public static final double DRIVE_SPEED = .5;
    public static final double TURN_SPEED = 0.5;

    public static ElapsedTime runtime = new ElapsedTime();


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


        idle();

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

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
        telemetry.addData("Path0", "Starting at %7d :%7d : %7d :%7d",
                back_left_motor.getCurrentPosition(),
                back_right_motor.getCurrentPosition(),
                front_left_motor.getCurrentPosition(),
                front_right_motor.getCurrentPosition());
        telemetry.update();

    }


    @Override
    public void runOpMode() {

    }

    public static double getCountsPerMotorRev() {
        ;
        return COUNTS_PER_MOTOR_REV;
    }

    public static double getDriveGearReduction() {
        return DRIVE_GEAR_REDUCTION;
    }

    public static double getWheelPerimeterCm() {
        return WHEEL_PERIMETER_CM;
    }

    public static double getCountsPerCm() {
        return COUNTS_PER_CM;
    }


    public void straightDrive(double power) {
        front_left_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        back_left_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        front_right_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        back_right_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
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
        idle();
        front_left_motor.setPower(0);
        idle();
        back_right_motor.setPower(0);
        idle();
        back_left_motor.setPower(0);
        sleep(100);
        back_left_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        idle();
        front_left_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        idle();
        back_right_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        idle();
        front_right_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        idle();
        sleep(100);
    }

    public void diagonalDrive(double speed, boolean left, boolean forward){
        if (left && forward){
            front_left_motor.setPower(speed);
            back_right_motor.setPower(speed);
        } else if (left && !forward){
            front_left_motor.setPower(-speed);
            back_right_motor.setPower(-speed);
        } else if (!left && forward){
            front_right_motor.setPower(speed);
            back_left_motor.setPower(speed);
        } else if (!left && !forward){
            front_right_motor.setPower(-speed);
            back_left_motor.setPower(-speed);
        } else{
            //do nothing to prevent error!
        }
    }

    public void newTurnLeft(double speed){

        back_left_motor.setPower(-speed);
        front_left_motor.setPower(-speed);
        front_right_motor.setPower(speed);
        back_right_motor.setPower(speed);

    }

    public void newTurnRight(double speed){

        back_left_motor.setPower(speed);
        front_left_motor.setPower(speed);
        front_right_motor.setPower(-speed);
        back_right_motor.setPower(-speed);
    }

    public void setMotorsToEnc(double leftDist, double rightDist, double time){

        double rightTicks = (rightDist*getCountsPerCm())/time;
        double leftTicks = (leftDist*getCountsPerCm())/time;
        back_left_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        front_left_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        back_right_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        front_right_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        back_left_motor.setMaxSpeed((int) leftTicks);
        front_left_motor.setMaxSpeed((int) leftTicks);
        back_right_motor.setMaxSpeed((int) rightTicks);
        front_right_motor.setMaxSpeed((int) rightTicks);
    }

    public void setMotorsToEncCharlie(double leftDist, double rightDist, double time){

        double rightTicks = ((rightDist)*getCountsPerCm())/time;
        double leftTicks = ((leftDist)*getCountsPerCm())/time;
        back_left_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        front_left_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        back_right_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        front_right_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        back_left_motor.setMaxSpeed((int) leftTicks);
        front_left_motor.setMaxSpeed((int) leftTicks);
        back_right_motor.setMaxSpeed((int) rightTicks);
        front_right_motor.setMaxSpeed((int) rightTicks);
    }

    public void setMotorsModeToColorSensing()
    {
        setMotorsToEnc(29, 29, 0.5);
    }
    public void setMotorsModeToRangeSensing()
    {
        setMotorsToEnc(29, 29, 1);
    }
    public void setMotorsModeToGyroSensing()  { setMotorsToEnc(29, 29, 2); }
    public void setMotorsModeToEncDrive()   { setMotorsToEnc(50, 50, 0.5);  }


    public void encoderMecanumDrive(double speed,
                             double leftInches, double rightInches,
                             double timeoutS, int direction) {
        int newLeftFrontTarget;
        int newRightFrontTarget;
        int newLeftBackTarget;
        int newRightBackTarget;
        double leftSpeed = 0;
        double rightSpeed = 0;


        sleep(500);
        // Ensure that the opmode is still active
      //  if (opModeIsActive())

            double time = runtime.seconds()+timeoutS;
            /*back_left_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            back_right_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            front_left_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            front_right_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);*/
        back_left_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        idle();
        back_right_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        idle();
        front_left_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        idle();
        front_right_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        idle();

        sleep(100);

            // Determine new target position, and pass to motor controller
         newLeftFrontTarget = front_left_motor.getCurrentPosition() + (int) (leftInches * getCountsPerCm());
        newRightFrontTarget = front_right_motor.getCurrentPosition() + (int) (rightInches * getCountsPerCm());
        newLeftBackTarget = back_left_motor.getCurrentPosition() + (int) (leftInches * getCountsPerCm());
        newRightBackTarget = back_right_motor.getCurrentPosition() + (int) (rightInches * getCountsPerCm());

        if(direction == 0) {
            back_left_motor.setTargetPosition(newLeftBackTarget);
            idle();
            back_right_motor.setTargetPosition(newRightBackTarget);
            idle();
            front_left_motor.setTargetPosition(newLeftFrontTarget);
            idle();
            front_right_motor.setTargetPosition(newRightFrontTarget);
        }
        else if (direction == 1){//right
            back_left_motor.setTargetPosition(-newLeftBackTarget);
            idle();
            back_right_motor.setTargetPosition(newRightBackTarget);
            idle();
            front_left_motor.setTargetPosition(newLeftFrontTarget);
            idle();
            front_right_motor.setTargetPosition(-newRightFrontTarget);
        }
        else if(direction == -1){//left
            back_left_motor.setTargetPosition(newLeftBackTarget);
            idle();
            back_right_motor.setTargetPosition(-newRightBackTarget);
            idle();
            front_left_motor.setTargetPosition(-newLeftFrontTarget);
            idle();
            front_right_motor.setTargetPosition(newRightFrontTarget);
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
          //  runtime.reset();
            //if(leftInches != -rightInches)
                /*front_left_motor.setPower(Math.abs(leftSpeed)*Math.sin(degrees*Math.PI/180 + (Math.PI/4)));
                front_right_motor.setPower(Math.abs(rightSpeed)*Math.cos(degrees*Math.PI/180 + (Math.PI/4)));
                back_left_motor.setPower(Math.abs(leftSpeed)*Math.cos(degrees*Math.PI/180 + (Math.PI/4)));
                back_right_motor.setPower(Math.abs(rightSpeed)*Math.sin(degrees*Math.PI/180 + (Math.PI/4)));*/
        back_left_motor.setPower(Math.abs(leftSpeed));
        idle();
        back_right_motor.setPower(Math.abs(rightSpeed));
        idle();
                front_left_motor.setPower(Math.abs(leftSpeed));
        idle();
                front_right_motor.setPower(Math.abs(rightSpeed));
        idle();



            // keep looping while we are still active, and there is time left, and both motors are running.
            while (opModeIsActive() &&
                    (runtime.seconds() < time) &&
                    (back_left_motor.isBusy() && back_right_motor.isBusy()&&
                            front_left_motor.isBusy() && front_right_motor.isBusy())) {

                back_left_motor.setPower(Math.abs(leftSpeed));
                back_right_motor.setPower(Math.abs(rightSpeed));
                front_left_motor.setPower(Math.abs(leftSpeed));
                front_right_motor.setPower(Math.abs(rightSpeed));

                idle();
            }
idle();
            // Stop all motion;
            stopMotors();

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

        double time = runtime.seconds()+timeoutS;
        back_left_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        idle();
        back_right_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        idle();
        front_left_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        idle();
        front_right_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);


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
            idle();
            back_left_motor.setTargetPosition(newLeftBackTarget);
  //          front_left_motor.setTargetPosition(newLeftTarget);
            idle();
            front_right_motor.setPower(Math.abs(speed));
            idle();
            back_right_motor.setPower(Math.abs(0));
            idle();
            back_left_motor.setPower(Math.abs(speed));
            idle();
            front_left_motor.setPower(Math.abs(0));
            idle();
            while (opModeIsActive() &&
                    (runtime.seconds() < time) &&
                    (front_right_motor.isBusy() && back_left_motor.isBusy())) {
                idle();
            }
        }
        else if (direction == 1){

            //front_right_motor.setTargetPosition(newRightTarget);
                       back_right_motor.setTargetPosition(newRightBackTarget);
            idle();
            //back_left_motor.setTargetPosition(newLeftTarget);
                      front_left_motor.setTargetPosition(newLeftFrontTarget);
            idle();
            front_right_motor.setPower(Math.abs(0));
            idle();
            back_right_motor.setPower(Math.abs(speed));
            idle();
            back_left_motor.setPower(Math.abs(0));
            idle();
            front_left_motor.setPower(Math.abs(speed));
            idle();
            while (opModeIsActive() &&
                    (runtime.seconds() < time) &&
                    (front_left_motor.isBusy() && back_right_motor.isBusy())) { idle();
            }
        }
        else if(direction == 2){

            front_right_motor.setTargetPosition(-newRightFrontTarget);
            //           back_right_motor.setTargetPosition(newRightTarget);
            idle();
            back_left_motor.setTargetPosition(-newLeftBackTarget);
            //          front_left_motor.setTargetPosition(newLeftTarget);
            idle();
            front_right_motor.setPower(Math.abs(speed));
            idle();
            back_right_motor.setPower(Math.abs(0));
            idle();
            back_left_motor.setPower(Math.abs(speed));
            idle();
            front_left_motor.setPower(Math.abs(0));
            idle();
            while (opModeIsActive() &&
                    (runtime.seconds() < time) &&
                    (back_left_motor.isBusy() && front_right_motor.isBusy())) { idle();
            }
        }
        else if(direction == 3) {

            //front_right_motor.setTargetPosition(newRightTarget);
            back_right_motor.setTargetPosition(-newRightBackTarget);
            idle();
            //back_left_motor.setTargetPosition(newLeftTarget);
            front_left_motor.setTargetPosition(-newLeftFrontTarget);
            idle();
            front_right_motor.setPower(Math.abs(0));
            idle();
            back_right_motor.setPower(Math.abs(speed));
            idle();
            back_left_motor.setPower(Math.abs(0));
            idle();
            front_left_motor.setPower(Math.abs(speed));
            idle();
            while (opModeIsActive() &&
                    (runtime.seconds() < time) &&
                    (front_left_motor.isBusy() && back_right_motor.isBusy())) { idle();
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

    public void crossDrive(int direction, double speed){
        if(direction == 4) {

            front_right_motor.setPower(Math.abs(speed));
            back_right_motor.setPower(Math.abs(0));
            back_left_motor.setPower(Math.abs(speed));
            front_left_motor.setPower(Math.abs(0));
        }
        else if (direction == 1){

            front_right_motor.setPower(Math.abs(0));
            back_right_motor.setPower(Math.abs(speed));
            back_left_motor.setPower(Math.abs(0));
            front_left_motor.setPower(Math.abs(speed));
        }
        else if(direction == 2){

            front_right_motor.setPower(-Math.abs(speed));
            back_right_motor.setPower(Math.abs(0));
            back_left_motor.setPower(-Math.abs(speed));
            front_left_motor.setPower(Math.abs(0));
        }
        else if(direction == 3) {

            front_right_motor.setPower(Math.abs(0));
            back_right_motor.setPower(-Math.abs(speed));
            back_left_motor.setPower(Math.abs(0));
            front_left_motor.setPower(-Math.abs(speed));
        }
    }
    public void encoderDrive(double speed,
                                    double leftInches, double rightInches,
                                    double timeoutS) {
        int newLeftTarget;
        int newRightTarget;
        double leftSpeed;
        double rightSpeed;

        // Ensure that the opmode is still active
        //  if (opModeIsActive())

        double time = runtime.seconds()+timeoutS;
        back_left_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        back_right_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        front_left_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        front_right_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Determine new target position, and pass to motor controller
        newLeftTarget = back_left_motor.getCurrentPosition() + (int) (leftInches * getCountsPerCm());
        newRightTarget = back_right_motor.getCurrentPosition() + (int) (rightInches * getCountsPerCm());
        back_left_motor.setTargetPosition(newLeftTarget);
        back_right_motor.setTargetPosition(newRightTarget);
        front_left_motor.setTargetPosition(newLeftTarget);
        front_right_motor.setTargetPosition(newRightTarget);


        // reset the timeout time and start motion.
        if (Math.abs(leftInches) > Math.abs(rightInches)) {
            leftSpeed = speed;
            rightSpeed = (speed * rightInches) / leftInches;
        } else {
            rightSpeed = speed;
            leftSpeed = (speed * leftInches) / rightInches;
        }
        //  runtime.reset();
        //if(leftInches != -rightInches)
            front_left_motor.setPower(Math.abs(leftSpeed));
            front_right_motor.setPower(Math.abs(rightSpeed));
            back_left_motor.setPower(Math.abs(leftSpeed));
            back_right_motor.setPower(Math.abs(rightSpeed));



        // keep looping while we are still active, and there is time left, and both motors are running.
        while (opModeIsActive() &&
                (runtime.seconds() < time) &&
                (back_left_motor.isBusy() && back_right_motor.isBusy())) {
        }

        // Stop all motion;
        back_left_motor.setPower(0);
        back_right_motor.setPower(0);
        front_left_motor.setPower(0);
        front_right_motor.setPower(0);
    }




}
