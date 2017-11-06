package org.firstinspires.ftc.teamcode.ftc2017to2018season;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by inspirationteam on 12/18/2016.
 */
@Autonomous(name = "Charlie Wheel Test")
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

        front_right_motor.setDirection(DcMotor.Direction.REVERSE);
        back_right_motor.setDirection(DcMotor.Direction.REVERSE);
        idle();
        sleep(100);

//        front_left_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        front_right_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        back_left_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        back_right_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        idle();
//        sleep(100);
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
        initiate();

        waitForStart();

        front_left_motor.setPower(1);
        sleep(3000);
        front_left_motor.setPower(0);
        sleep(3000);
        front_right_motor.setPower(1);
        sleep(3000);
        front_right_motor.setPower(0);
        sleep(3000);
        back_left_motor.setPower(1);
        sleep(3000);
        back_left_motor.setPower(0);
        sleep(3000);
        back_right_motor.setPower(1);
        sleep(3000);
        back_right_motor.setPower(0);
    }




}
