package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="OrcaTeleop", group="Linear Opmode")
public class OrcaTeleop extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor FrontLeftDrive = null;
    private DcMotor FrontRightDrive = null;
    private DcMotor BackLeftDrive = null;
    private DcMotor BackRightDrive = null;
    private DcMotor LiftDrive = null;

    Servo rightTop;
    Servo leftTop;
    Servo rightBottom;
    Servo leftBottom;

    //static final double INCREMENT   = 0.01;     // amount to slew servo each CYCLE_MS cycle
    static final int    CYCLE_MS    =   75;     // period of each cycle
    static final double DOWN_POS_SHOULDER     =  0.4;     // Minimum rotational position
    static final double UP_POS_SHOULDER     =  0.7;

    static final double DOWN_POS_ARM     =  0.80;
    static final double UP_POS_ARM     =  0.6 ;

    static final double GRAB_BOTTOM_CLAW = 0.6;
    static final double RELEASE_BOTTOM_CLAW = .76;

    static final double GRAB_TOP_CLAW = 0.22;
    static final double RELEASE_TOP_CLAW = .92;

    // Define class members
    double strafepower = 1;
    boolean upArm;

    //Starting claw positions
    double  sServoPosition = (UP_POS_SHOULDER);
    double  aServoPosition = (UP_POS_ARM);
    controllerPos previousDrive = controllerPos.ZERO;

    DigitalChannel limitSwitch;  // Hardware Device Object

    @Override
    public void runOpMode() {
        leftTop = hardwareMap.get(Servo.class, "left top claw");
        rightTop = hardwareMap.get(Servo.class, "right top claw");
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

        //limit switch
        limitSwitch = hardwareMap.get(DigitalChannel.class, "limit_switch");

        limitSwitch.setMode(DigitalChannel.Mode.INPUT);


        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();
        // run until the end of the match (driver presses STOP)
        boolean abuttonchanged = true;
        boolean bbuttonchanged = true;
        boolean topClaw = false;
        boolean grabbingTR = true;
        boolean grabbingTL = true;
        boolean grabbingBR = true;
        boolean grabbingBL = true;
        double sservopos = UP_POS_SHOULDER;
        double aservopos = UP_POS_ARM;

        rightTop.setPosition(1-RELEASE_TOP_CLAW);
        leftTop.setPosition(RELEASE_TOP_CLAW);
        rightBottom.setPosition(.95 - RELEASE_BOTTOM_CLAW);
        leftBottom.setPosition(RELEASE_BOTTOM_CLAW);

        while (opModeIsActive()) {
            if (limitSwitch.getState() == true) {
                telemetry.addData("limitSwitch", "Is Not Pressed");
            } else {
                telemetry.addData("limitSwitch", "Is Pressed");
            }

            if(gamepad2.dpad_up) {
                topClaw = true;
            }
            else if (gamepad2.dpad_down) {
                topClaw = false;
            }
            telemetry.addData("Top Claw: ", topClaw);
            telemetry.update();
//            if (gamepad2.dpad_right) {
//                telemetry.addData("Claw: ", "Grabbing");
//                if(topClaw) {
//                    rightTop.setPosition(GRAB_TOP_CLAW);
//                    leftTop.setPosition(1 - GRAB_TOP_CLAW);
//                    telemetry.addLine("top grabbing");
//                }
//                else {
//                    rightBottom.setPosition(GRAB_BOTTOM_CLAW);
//                    leftBottom.setPosition(1 - GRAB_BOTTOM_CLAW);
//                    telemetry.addLine("top releasing");
//                }
//            } else if (gamepad2.dpad_left) {
//                telemetry.addData("Claw: ", "Releasing");
//                if(topClaw) {
//                    rightTop.setPosition(RELEASE_TOP_CLAW);
//                    leftTop.setPosition(1 - RELEASE_TOP_CLAW);
//                    telemetry.addLine("bottom grabbing");
//                }
//                else {
//                    rightBottom.setPosition(RELEASE_BOTTOM_CLAW);
//                    leftBottom.setPosition(1 - RELEASE_BOTTOM_CLAW);
//                    telemetry.addLine("bottom releasing");
//                }
//            }
            if(topClaw) {
                if(gamepad2.right_bumper) {
                    rightTop.setPosition(1-RELEASE_TOP_CLAW);
                    leftTop.setPosition(RELEASE_TOP_CLAW);
                    grabbingTR = false;
                    grabbingTL = false;
                    telemetry.addData("Right Top Claw: ", "Grabbing");
                    rightTop.setPosition(1 - GRAB_TOP_CLAW);
                    telemetry.addData("Left Top Claw: ", "Grabbing");
                    leftTop.setPosition(GRAB_TOP_CLAW);
                    telemetry.update();

                }
                else if(gamepad2.left_bumper){
                    grabbingTR = true;
                    grabbingTL = true;
                    telemetry.addData("Right Top Claw: ", "Releasing");
                    rightTop.setPosition(1-RELEASE_TOP_CLAW);
                    telemetry.addData("Left Top Claw: ", "Releasing");
                    leftTop.setPosition(RELEASE_TOP_CLAW);
                    telemetry.update();
                }
                else if (gamepad2.dpad_right) {
                    if (grabbingTR) {
                        telemetry.addData("Right Top Claw: ", "Grabbing");
                        rightTop.setPosition(1-GRAB_TOP_CLAW);
                        telemetry.update();
                    }
                    else {
                        telemetry.addData("Right Top Claw: ", "Releasing");
                        rightTop.setPosition(1-RELEASE_TOP_CLAW);
                        telemetry.update();
                    }
                    grabbingTR = !grabbingTR;
                }
                else if (gamepad2.dpad_left) {
                    if (grabbingTL) {
                        telemetry.addData("Left Top Claw: ", "Grabbing");
                        leftTop.setPosition(GRAB_TOP_CLAW);
                        telemetry.update();
                    }
                    else {
                        telemetry.addData("Left Top Claw: ", "Releasing");
                        leftTop.setPosition(RELEASE_TOP_CLAW);
                        telemetry.update();
                    }
                    grabbingTL = !grabbingTL;
                }
            }
            else {
                if(gamepad2.left_bumper) {
                    rightBottom.setPosition(1-RELEASE_BOTTOM_CLAW);
                    leftBottom.setPosition(RELEASE_BOTTOM_CLAW);
                    grabbingTR = false;
                    grabbingTL = false;
                    telemetry.addData("Right Bottom Claw: ", "Grabbing");
                    rightBottom.setPosition(1 - GRAB_BOTTOM_CLAW);
                    telemetry.addData("Left Bottom Claw: ", "Grabbing");
                    leftBottom.setPosition(GRAB_BOTTOM_CLAW);
                    telemetry.update();

                }
                else if(gamepad2.left_bumper){
                    grabbingTR = true;
                    grabbingTL = true;
                    telemetry.addData("Right Bottom Claw: ", "Releasing");
                    rightBottom.setPosition(.95 - RELEASE_BOTTOM_CLAW);
                    telemetry.addData("Left Bottom Claw: ", "Releasing");
                    leftBottom.setPosition(RELEASE_BOTTOM_CLAW);
                    telemetry.update();
                }
                if (gamepad2.dpad_right) {
                    if (grabbingBR) {
                        telemetry.addData("Right Bottom Claw: ", "Grabbing");
                        rightBottom.setPosition(.95 - GRAB_BOTTOM_CLAW);
                    }
                    else {
                        telemetry.addData("Right Bottom Claw: ", "Releasing");
                        rightBottom.setPosition(.95 - RELEASE_BOTTOM_CLAW);
                    }
                    grabbingBR = !grabbingBR;
                }
                else if (gamepad2.dpad_left) {
                    if (grabbingBL) {
                        telemetry.addData("Left Bottom Claw: ", "Grabbing");
                        leftBottom.setPosition(GRAB_BOTTOM_CLAW);
                    }
                    else {
                        telemetry.addData("Left Bottom Claw: ", "Releasing");
                        leftBottom.setPosition(RELEASE_BOTTOM_CLAW);
                    }
                    grabbingBL = !grabbingBL;
                }
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

    //KEEPS MOTORS FROM STALLING
    public double readjustMotorPower(double motorPower) {
        if (Math.abs(motorPower) >= 0.3) {
            return motorPower;
        } else {
            return 0;
        }
    }

}