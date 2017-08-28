
package org.usfirst.ftc.theintersect.code;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;


@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "MoveFwdR Testing")
public class MoveFrwRightTesting extends LinearOpMode {
    static DcMotor rF, rB, lF, lB, flywheel1, flywheel2, sweeperLow;
    static GyroSensor gyro;
    static ColorSensor beaconColor,floorColor;
    static int conversionFactor = 50;
    static Servo sideWall;
    static CRServo buttonPusher;
    boolean red = false;
    boolean timedOut = false;
    double average;
    DriveTrain driveTrain;
    char alliance;
    @Override
    public void runOpMode() throws InterruptedException {
        double speed = 1.0;
        double rightSpeed = 0.1;
        double distance = 25;
        boolean change = true;
        initHardware();

        waitForStart();
        while (opModeIsActive() ) {
            if (gamepad1.x) {
                speed -= 0.1;
                Functions.waitFor(300);
                change = true;
            }
            if (gamepad1.y) {
                speed += 0.1;
                Functions.waitFor(300);
                change = true;

            }
            if (gamepad1.a) {
                rightSpeed -=0.1;
                Functions.waitFor(300);
                change = true;
            }
            if (gamepad1.b) {
                rightSpeed +=0.1;
                Functions.waitFor(300);
                change = true;

            }
            if (gamepad1.left_bumper) {
                distance -=1;
                Functions.waitFor(300);
                change = true;
            }
            if (gamepad1.right_bumper) {
                distance +=1;
                Functions.waitFor(300);
                change = true;

            }
            if ( change) {
                telemetry.clear();
                telemetry.addData("Speed:", speed);
                telemetry.addData("Right Power", rightSpeed);
                telemetry.addData("Distance", distance);
                telemetry.update();
                change = false;
            }

            if ( gamepad1.start ) {
                driveTrain.moveFwdRight(speed, rightSpeed, distance, 5);
            }
            // waitFor(2000);
        }
    }



    public void initHardware() {
        buttonPusher = hardwareMap.crservo.get("buttonPusher");
        rF = hardwareMap.dcMotor.get("rF");
        rB = hardwareMap.dcMotor.get("rB");
        lF = hardwareMap.dcMotor.get("lF");
        lB = hardwareMap.dcMotor.get("lB");
        lB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheel1 = hardwareMap.dcMotor.get("flywheel1");
        flywheel2 = hardwareMap.dcMotor.get("flywheel2");
        flywheel1.setDirection(DcMotorSimple.Direction.REVERSE);
        //beaconColor = hardwareMap.colorSensor.get("beaconColor");
        floorColor = hardwareMap.colorSensor.get("floorColor");
        beaconColor = hardwareMap.colorSensor.get("beaconColor");
        sweeperLow = hardwareMap.dcMotor.get("sweeperLow");
        sideWall = hardwareMap.servo.get("sideWall");
        sideWall.setPosition(Functions.sideWallUpPos);
        lF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rF.setDirection(DcMotor.Direction.FORWARD);
        rB.setDirection(DcMotor.Direction.FORWARD);
        lB.setDirection(DcMotor.Direction.REVERSE);
        lF.setDirection(DcMotor.Direction.REVERSE);
        beaconColor.setI2cAddress(I2cAddr.create8bit(0x3c));
        gyro = hardwareMap.gyroSensor.get("gyro");
        driveTrain = new DriveTrain(lB, rB, lF, rF, this, gyro, floorColor);
        Servo lLift = hardwareMap.servo.get("lLift");
        Servo rLift = hardwareMap.servo.get("rLift");
        rLift.setDirection(Servo.Direction.REVERSE);
        lLift.setPosition(Functions.liftUpPos);
        rLift.setPosition((Functions.liftUpPos));
        calibrateGyro(telemetry);
    }

    public void calibrateGyro(Telemetry telemetry){
        gyro.calibrate();
        // make sure the gyro is calibrated before continuing
        while (gyro.isCalibrating() && !isStopRequested())  {
            Functions.waitFor(50);
        }
        telemetry.addLine("Robot Ready.");
        telemetry.update();

    }



}

