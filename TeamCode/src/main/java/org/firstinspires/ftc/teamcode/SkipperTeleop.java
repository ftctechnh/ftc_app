package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="SkipperTeleop", group="Linear Opmode")
public class SkipperTeleop extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor FrontLeftDrive = null;
    private DcMotor FrontRightDrive = null;
    private DcMotor BackLeftDrive = null;
    private DcMotor BackRightDrive = null;
    private DcMotor LiftDrive = null;



    Servo rightTop;
    Servo leftTop;
    Servo jewelservo;
    Servo rightBottom;
    Servo leftBottom;

    //static final double INCREMENT   = 0.01;     // amount to slew servo each CYCLE_MS cycle
    static final int    CYCLE_MS    =   75;     // period of each cycle
    static final double DOWN_POS_SHOULDER     =  0.4;     // Minimum rotational position
    static final double UP_POS_SHOULDER     =  0.7;

    static final double OPEN_POS_ARM =  0.80;
    static final double CLOSE_POS_ARM =  0.40 ;

    static final double OPEN_BOTTOM_CLAW = 0.0;
    static final double CLOSE_BOTTOM_CLAW = 0.53;


    // Define class members
    double strafepower = 1;
    boolean upShoulder;
    boolean upArm;

    //Starting claw positions
//    double  topServoPosition = (UP_POS_SHOULDER);
//    double  tServoPosition = (OPEN_POS_ARM);
    controllerPos previousDrive = controllerPos.ZERO;

    @Override
    public void runOpMode() {
        rightTop = hardwareMap.get(Servo.class, "right arm servo");
        leftTop = hardwareMap.get(Servo.class, "left arm servo");

        jewelservo = hardwareMap.get(Servo.class, "lowering servo");

        leftBottom = hardwareMap.get(Servo.class, "left bottom claw");
        rightBottom = hardwareMap.get(Servo.class, "right bottom claw");

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        FrontLeftDrive = hardwareMap.get(DcMotor.class, "front_left");
        FrontRightDrive = hardwareMap.get(DcMotor.class, "front_right");
        BackLeftDrive = hardwareMap.get(DcMotor.class, "back_left");
        BackRightDrive = hardwareMap.get(DcMotor.class, "back_right");
        LiftDrive = hardwareMap.get(DcMotor.class, "lift");

        FrontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        BackLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        BackRightDrive.setDirection(DcMotor.Direction.REVERSE);
        FrontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        LiftDrive.setDirection(DcMotor.Direction.FORWARD);

        FrontLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BackLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BackRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FrontRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LiftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();
        // run until the end of the match (driver presses STOP)
        boolean abuttonchanged = true;
        boolean bbuttonchanged = true;
        double sservopos = UP_POS_SHOULDER;
        double aservopos = CLOSE_POS_ARM;

        boolean rightBump = false;
        boolean leftBump = false;


        while (opModeIsActive()) {

            telemetry.addData("Right Top: ", rightTop.getPosition());
            telemetry.addData("Left Top: ", leftTop.getPosition());
            telemetry.addData("Right Bottom: ", rightBottom.getPosition());
            telemetry.addData("Left Bottom: ", leftBottom.getPosition());

            if(gamepad2.right_bumper) {
                if (!rightBump) {
                    rightBump = true;
                } else {
                    rightBump = false;
                }
            }

            if (gamepad2.left_bumper) {
                if (!leftBump) {
                    leftBump = true;
                } else {
                    leftBump = false;
                }
            }

            if (rightBump) {
                rightTop.setPosition(OPEN_POS_ARM);
                leftTop.setPosition(OPEN_POS_ARM);

                telemetry.addLine("Openning Top");
            } else {
                rightTop.setPosition(CLOSE_POS_ARM);
                leftTop.setPosition(CLOSE_POS_ARM);

                telemetry.addLine("Closing Top");
            }

            if (leftBump) {
                rightBottom.setPosition(OPEN_BOTTOM_CLAW);
                leftBottom.setPosition(OPEN_BOTTOM_CLAW);

                telemetry.addLine("Openning Bottom");
            } else {
                rightBottom.setPosition(CLOSE_BOTTOM_CLAW);
                leftBottom.setPosition(CLOSE_BOTTOM_CLAW);

                telemetry.addLine("Closing Bottom");
            }

            telemetry.update();

            moveRobot();
            moveLift();

            sleep(CYCLE_MS);
            idle();
        }
    }
    public enum controllerPos {
        STRAFE_RIGHT, STRAFE_LEFT, DRIVE_FOWARD, DRIVE_BACK, TURN_RIGHT, TURN_LEFT, ZERO;
    }
    //DRIVING CONTROL
    public void moveRobot() {
        double drive = gamepad1.left_stick_y;
        double turn = gamepad1.right_stick_x;
        if(drive > 0.3 && (previousDrive == controllerPos.DRIVE_FOWARD || previousDrive == controllerPos.ZERO)) {
            previousDrive = controllerPos.DRIVE_FOWARD;
            Drive(drive);
        } else if(drive < -0.3 && (previousDrive == controllerPos.DRIVE_BACK || previousDrive == controllerPos.ZERO)) {
            previousDrive = controllerPos.DRIVE_BACK;
            Drive(drive);
        } else if(gamepad1.right_bumper && (previousDrive == controllerPos.STRAFE_RIGHT || previousDrive == controllerPos.ZERO)) {
            previousDrive = controllerPos.STRAFE_RIGHT;
            Strafe(1);
        } else if(gamepad1.left_bumper && (previousDrive == controllerPos.STRAFE_LEFT || previousDrive == controllerPos.ZERO)) {
            previousDrive = controllerPos.STRAFE_LEFT;
            Strafe(-1);
        }  else if(turn > 0.3 &&(previousDrive == controllerPos.TURN_RIGHT || previousDrive == controllerPos.ZERO)){
            previousDrive = controllerPos.TURN_RIGHT;
            turn(turn);
        } else if(turn < -0.3 &&(previousDrive == controllerPos.TURN_LEFT || previousDrive == controllerPos.ZERO)){
            previousDrive = controllerPos.TURN_LEFT;
            turn(turn);
        } else {
            previousDrive = controllerPos.ZERO;
            FrontLeftDrive.setPower(0);
            BackLeftDrive.setPower(0);
            FrontRightDrive.setPower(0);
            BackRightDrive.setPower(0);
        }
    }

    public enum clawPos {
        OPEN, CLOSE;
    }
    //LIFT MOTOR CONTROL
    public void moveLift() {
        double LiftPower;
        if(gamepad2.left_stick_y > .3) {
            LiftPower = 0.5;
        } else if (gamepad2.left_stick_y < -.3) {
            LiftPower = -0.5;
        } else {
            LiftPower = 0;
        }
        LiftDrive.setPower(LiftPower);
    }

    //STRAFING CONTROL
    public void Strafe(int strafedirection) {

        double FRpower = strafedirection * strafepower;
        double BLpower = strafedirection * strafepower;
        double BRpower = -1 * strafedirection * strafepower;
        double FLpower = -1 * strafedirection * strafepower ;

        FLpower = Range.clip(FLpower, -1.0, 1.0) ;
        BRpower = Range.clip(BRpower, -1.0, 1.0) ;
        BLpower = Range.clip(BLpower, -1.0, 1.0) ;
        FRpower = Range.clip(FRpower, -1.0, 1.0) ;

        readjustMotorPower(FRpower);
        readjustMotorPower(BRpower);
        readjustMotorPower(FLpower);
        readjustMotorPower(BLpower);

        FrontLeftDrive.setPower(FLpower);
        BackLeftDrive.setPower(BLpower);
        FrontRightDrive.setPower(FRpower);
        BackRightDrive.setPower(BRpower);

    }

    //DRIVING FOWARADS/BACKWARDS/TURNING
    public void Drive(double drive) {
        double drivePower = drive;

        drivePower = readjustMotorPower(drivePower);
        drivePower = Range.clip(drivePower, -1.0, 1.0);

        FrontLeftDrive.setPower(drivePower);
        BackLeftDrive.setPower(drivePower);
        FrontRightDrive.setPower(drivePower);
        BackRightDrive.setPower(drivePower);

        telemetry.addData("Motors", "drive power (%.2f)", drivePower);
        telemetry.update();
    }
    public void turn(double turn){
        double Rpower = turn;
        double Lpower = -turn;

        Rpower = readjustMotorPower(Rpower);
        Lpower = readjustMotorPower(Lpower);

        Rpower = Range.clip(Rpower, -1.0, 1.0);
        Lpower = Range.clip(Lpower, -1.0, 1.0);

        FrontLeftDrive.setPower(Lpower);
        BackLeftDrive.setPower(Lpower);
        FrontRightDrive.setPower(Rpower);
        BackRightDrive.setPower(Rpower);
    }

    public void grabBlock() {
        rightTop.setPosition(DOWN_POS_SHOULDER);
        leftTop.setPosition(OPEN_POS_ARM);
    }

    public void releaseBlock() {
        rightTop.setPosition(UP_POS_SHOULDER);
        leftTop.setPosition(CLOSE_POS_ARM);
    }

    //KEEPS MOTORS FROM STALLING
    public double readjustMotorPower(double motorPower) {
        if (Math.abs(motorPower) >= 0.3) {
            return motorPower;
        } else {
            return 0;
        }
    }

}