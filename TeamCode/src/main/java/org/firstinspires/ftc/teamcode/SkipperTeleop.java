package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
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
    private DcMotor RelicDrive = null;

    public Servo loweringJewelServo;
    public Servo turningJewelServo;


    Servo leftTop;
    Servo rightTop;
    Servo rightBottom;
    Servo leftBottom;
    Servo arm;
    Servo hand;

    //static final double INCREMENT   = 0.01;     // amount to slew servo each CYCLE_MS cycle
    static final int    CYCLE_MS    =   75;

    static final double OPEN_BOTTOM_RIGHT =  0.65;
    static final double OPEN_BOTTOM_LEFT = 0.2;
    static final double CLOSE_BOTTOM_RIGHT     =  0.55;
    static final double CLOSE_BOTTOM_LEFT     =  0.4;

    static final double CLOSE_TOP_LEFT = 0.28;
    static final double CLOSE_TOP_RIGHT = 0.52;
    static final double OPEN_TOP_LEFT     =  0.55;
    static final double OPEN_TOP_RIGHT     =  0.25;

    static final double SEMI_OPEN_BOTTOM_RIGHT = 0.65;
    static final double SEMI_OPEN_BOTTOM_LEFT = 0.3;
    static final double SEMI_OPEN_TOP_RIGHT = 0.35;
    static final double SEMI_OPEN_TOP_LEFT = 0.4;

    static final double OPEN_ARM = 1;
    static final double CLOSE_ARM = 0.2;
    static final double OPEN_HAND = 1;
    static final double CLOSE_HAND = 0.3;

    static final double TURNING_SERVO_RESET = 0.5;


    // Define class members
    double strafepower = 1;

    //Starting claw positions
    double rightTopPos = OPEN_TOP_RIGHT;
    double leftTopPos =  OPEN_TOP_LEFT;
    double rightBottomPos = OPEN_BOTTOM_RIGHT;
    double leftBottomPos = OPEN_BOTTOM_LEFT;


    controllerPos previousDrive = controllerPos.ZERO;


    @Override
    public void runOpMode() {
        rightTop = hardwareMap.get(Servo.class, "right top claw");
        leftTop = hardwareMap.get(Servo.class, "left top claw");
        leftBottom = hardwareMap.get(Servo.class, "left bottom claw");
        rightBottom = hardwareMap.get(Servo.class, "right bottom claw");
        arm = hardwareMap.get(Servo.class, "arm servo");
        hand = hardwareMap.get(Servo.class, "hand servo");
        loweringJewelServo = hardwareMap.get(Servo.class, "lowering servo" );
        turningJewelServo = hardwareMap.get(Servo.class, "turning servo");
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        FrontLeftDrive = hardwareMap.get(DcMotor.class, "front_left");
        FrontRightDrive = hardwareMap.get(DcMotor.class, "front_right");
        BackLeftDrive = hardwareMap.get(DcMotor.class, "back_left");
        BackRightDrive = hardwareMap.get(DcMotor.class, "back_right");
        LiftDrive = hardwareMap.get(DcMotor.class, "lift");
        RelicDrive = hardwareMap.get(DcMotor.class, "relic");

        FrontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        BackLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        BackRightDrive.setDirection(DcMotor.Direction.REVERSE);
        FrontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        LiftDrive.setDirection(DcMotor.Direction.FORWARD);
        RelicDrive.setDirection(DcMotor.Direction.FORWARD);

        FrontLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BackLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BackRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FrontRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LiftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RelicDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {

            moveClaws();

            moveJewelArm();

            moveRelic();

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

    public void moveJewelArm() {
        double triggerJewelServo = gamepad2.left_stick_y;

        if(triggerJewelServo > -0.2 && triggerJewelServo < 0.2) { //change to right joystick
            loweringJewelServo.setPosition(0);
            turningJewelServo.setPosition(TURNING_SERVO_RESET);
        }
    }

    public void moveClaws() {
        if(gamepad2.a) {
            //Open top claws
            rightTop.setPosition(OPEN_TOP_RIGHT);
            leftTop.setPosition(OPEN_TOP_LEFT);
        } else if(gamepad2.y) {
            //Close top claws
            rightTop.setPosition(CLOSE_TOP_RIGHT);
            leftTop.setPosition(CLOSE_TOP_LEFT);
        } else if(gamepad2.x) {
            //open top left
            leftTop.setPosition(OPEN_TOP_LEFT);
            rightTop.setPosition(SEMI_OPEN_TOP_RIGHT);
        } else if(gamepad2.b) {
            //open top right
            rightTop.setPosition(OPEN_TOP_RIGHT);
            leftTop.setPosition(SEMI_OPEN_TOP_LEFT);
        } else if (gamepad2.dpad_down) {
            //open bottom claw2
            rightBottom.setPosition(OPEN_BOTTOM_RIGHT);
            leftBottom.setPosition(OPEN_BOTTOM_LEFT);
        } else if (gamepad2.dpad_up) {
            //close bottom claws
            rightBottom.setPosition(CLOSE_BOTTOM_RIGHT);
            leftBottom.setPosition(CLOSE_BOTTOM_LEFT);
        } else if(gamepad2.dpad_left) {
            //open bottom left
            leftBottom.setPosition(OPEN_BOTTOM_LEFT);
            rightBottom.setPosition(SEMI_OPEN_BOTTOM_RIGHT);
        } else if(gamepad2.dpad_right) {
            //open bottom right
            rightBottom.setPosition(OPEN_BOTTOM_RIGHT);
            leftBottom.setPosition(SEMI_OPEN_BOTTOM_LEFT);
        }
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
        } else if(gamepad1.dpad_right && (previousDrive == controllerPos.STRAFE_RIGHT || previousDrive == controllerPos.ZERO)) {
            previousDrive = controllerPos.STRAFE_RIGHT;
            Strafe(1);
        } else if(gamepad1.dpad_left && (previousDrive == controllerPos.STRAFE_LEFT || previousDrive == controllerPos.ZERO)) {
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
        if(gamepad2.left_bumper) {
            LiftPower = 0.5;
        } else if (gamepad2.right_bumper) {
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


    //KEEPS MOTORS FROM STALLING
    public double readjustMotorPower(double motorPower) {
        if (Math.abs(motorPower) >= 0.3) {
            return motorPower;
        } else {
            return 0;
        }
    }

    public void moveRelic() {
        double drivePower = 0.6;

        if(gamepad2.left_trigger > .1) {
            arm.setPosition(CLOSE_ARM);
        } else {
            arm.setPosition(OPEN_ARM);
        }

        if(gamepad2.right_trigger > .1) {
            hand.setPosition(CLOSE_HAND);
        } else {
            hand.setPosition(OPEN_HAND);
        }

        double relicDrive = gamepad2.right_stick_y;

        if(relicDrive > 0.2) {
            RelicDrive.setPower(-drivePower);
        } else if(relicDrive < -0.2) {
            RelicDrive.setPower(drivePower);
        } else {
            RelicDrive.setPower(0);
        }

    }
}






