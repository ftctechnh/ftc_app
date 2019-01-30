package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.vision.MasterVision;
import org.firstinspires.ftc.teamcode.vision.SampleRandomizedPositions;

@Autonomous (name="TestCrater", group="Competition Autonomous")
public class TestCrater extends LinearOpMode {
    MasterVision vision;
    SampleRandomizedPositions goldPosition;
    DcMotor liftMotor;
    DcMotor frontLeft;
    DcMotor rearLeft;
    DcMotor frontRight;
    DcMotor rearRight;
    Servo armServo;
    GyroSensor sensorGyro;
    ModernRoboticsI2cGyro mrGyro;

    private ElapsedTime runtime = new ElapsedTime();


    @Override
    public void runOpMode() throws InterruptedException {

        //declare motors
        liftMotor = hardwareMap.dcMotor.get("liftMotor");
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        rearLeft = hardwareMap.dcMotor.get("rearLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        rearRight = hardwareMap.dcMotor.get("rearRight");
        sensorGyro = hardwareMap.gyroSensor.get("gyro");
        armServo = hardwareMap.servo.get("armServo");

        //declare motor directions
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        rearLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        rearRight.setDirection(DcMotor.Direction.REVERSE);
        liftMotor.setDirection(DcMotor.Direction.REVERSE);
        armServo.setDirection(Servo.Direction.FORWARD);

        mrGyro = (ModernRoboticsI2cGyro) sensorGyro;


        double turnSpeed = 0.2;
        int zAccumulated;
        int target = 0;

        telemetry.addData("Mode", "waiting");
        telemetry.update();


        waitForStart();

        //-------------------Place Marker Start--------------------------------------------------
        armServo.setPosition(0.0);
        sleep(400); //wait


        //-------------------Block End--------------------------------------------------


        //----------------------Strafe off Lander Start-------------------------------------
        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rearLeft.setTargetPosition(-800);
        rearLeft.setPower(0.5);
        rearRight.setTargetPosition(800);
        rearRight.setPower(0.5);
        frontLeft.setTargetPosition(800);
        frontLeft.setPower(0.6);
        frontRight.setTargetPosition(-800);
        frontRight.setPower(0.5);
        while (rearLeft.isBusy() && opModeIsActive()) {
        }
        while (rearRight.isBusy() && opModeIsActive()) {
        }
        while (frontLeft.isBusy() && opModeIsActive()) {
        }
        while (frontRight.isBusy() && opModeIsActive()) {
        }
        rearLeft.setPower(0);
        rearRight.setPower(0);
        frontLeft.setPower(0);
        frontRight.setPower(0);
        //-------------------Block End--------------------------------------------------

        //----------------------Straight off Lander Start-------------------------------------
        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rearLeft.setTargetPosition(600);
        rearLeft.setPower(0.5);
        rearRight.setTargetPosition(600);
        rearRight.setPower(0.5);
        frontLeft.setTargetPosition(600);
        frontLeft.setPower(0.5);
        frontRight.setTargetPosition(600);
        frontRight.setPower(0.5);
        while (rearLeft.isBusy() && opModeIsActive()) {
        }
        while (rearRight.isBusy() && opModeIsActive()) {
        }
        while (frontLeft.isBusy() && opModeIsActive()) {
        }
        while (frontRight.isBusy() && opModeIsActive()) {
        }
        rearLeft.setPower(0);
        rearRight.setPower(0);
        frontLeft.setPower(0);
        frontRight.setPower(0);
        //-------------------Block End--------------------------------------------------

        //----------------------Get in Position for Mineral Start-------------------------------------
        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rearLeft.setTargetPosition(2000);
        rearLeft.setPower(0.5);
        rearRight.setTargetPosition(-2000);
        rearRight.setPower(0.5);
        frontLeft.setTargetPosition(-2000);
        frontLeft.setPower(0.5);
        frontRight.setTargetPosition(2000);
        frontRight.setPower(0.5);
        while (rearLeft.isBusy() && opModeIsActive()) {
        }
        while (rearRight.isBusy() && opModeIsActive()) {
        }
        while (frontLeft.isBusy() && opModeIsActive()) {
        }
        while (frontRight.isBusy() && opModeIsActive()) {
        }
        rearLeft.setPower(0);
        rearRight.setPower(0);
        frontLeft.setPower(0);
        frontRight.setPower(0);
        //-------------------Block End--------------------------------------------------

        //----------------------Straight to Knock Mineral Start-------------------------------------
        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rearLeft.setTargetPosition(2500);
        rearLeft.setPower(1);
        rearRight.setTargetPosition(2500);
        rearRight.setPower(1);
        frontLeft.setTargetPosition(2500);
        frontLeft.setPower(1);
        frontRight.setTargetPosition(2500);
        frontRight.setPower(1);
        while (rearLeft.isBusy() && opModeIsActive()) {
        }
        while (rearRight.isBusy() && opModeIsActive()) {
        }
        while (frontLeft.isBusy() && opModeIsActive()) {
        }
        while (frontRight.isBusy() && opModeIsActive()) {
        }
        rearLeft.setPower(0);
        rearRight.setPower(0);
        frontLeft.setPower(0);
        frontRight.setPower(0);
        //-------------------Block End--------------------------------------------------

        //-------------------Place Marker Start--------------------------------------------------
        armServo.setPosition(0.7);
        sleep(400); //wait


        //-------------------Block End--------------------------------------------------


        //----------------------WAIT-------------------------------------
        sleep(25000);
        //----------------------------------------------------------------
    }
}
