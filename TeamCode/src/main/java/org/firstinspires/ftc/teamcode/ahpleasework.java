package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

    /**
     * Created by Libby on 12/14/17.
     */

    @TeleOp(name="Om Nom Base", group="Linear Opmode")

    public class ahpleasework extends LinearOpMode {

        // Declare OpMode members.
        private ElapsedTime runtime = new ElapsedTime();
        private DcMotor FrontLeftDrive = null;
        private DcMotor FrontRightDrive = null;
        private DcMotor BackLeftDrive = null;
        private DcMotor BackRightDrive = null;
        private DcMotor lift = null;
        private DcMotor NomNomNom = null;
        private DcMotor relicArm = null;
        private Servo rightBoxServo = null;
        private Servo leftBoxServo = null;
        private CRServo pushBackServoLeft = null;
        private CRServo pushBackServoRight = null;
        private Servo wallServo = null;
        private Servo liftIn = null;
        private Servo elbowServo = null;
        private Servo handServo = null;
        public Servo jewelServo = null;






        //static final double INCREMENT   = 0.01;     // amount to slew servo each CYCLE_MS cycle
//    static final int    CYCLE_MS    =   75;
        static final double NOM_FORWARD_POWER = 1;
        static final double NOM_BACKWARD_POWER = -1;
        static final double BOX_RIGHT_DOWN = .34;
        static final double BOX_LEFT_DOWN = .61;
        static final double BOX_RIGHT_UP = .84;
        static final double BOX_LEFT_UP = .1;
        static final double Push_Back_Power = 1;
        static final double JEWEL_UP_POS = 1;
        static final double ELBOW_UP = .366;
        static final double ELBOW_DOWN = .78;
        static final double RELIC_HAND_CLOSE = .45;
        static final double RELIC_HAND_OPEN = .75;
        static double turnConstant = 1;

        // Define class members
        double strafepower = 1;
        static final double NOM_POWER = 1;
        boolean wallout = false;
        boolean relicGang = false;

        controllerPos previousDrive = controllerPos.ZERO;

        float x;
        float y;
        //boolean box position

        @Override
        public void runOpMode() {
            telemetry.addData("Status", "Initialized");
            telemetry.update();

            FrontLeftDrive = hardwareMap.get(DcMotor.class, "fl");
            FrontRightDrive = hardwareMap.get(DcMotor.class, "fr");
            BackLeftDrive = hardwareMap.get(DcMotor.class, "bl");
            BackRightDrive = hardwareMap.get(DcMotor.class, "br");
//            lift = hardwareMap.get(DcMotor.class, "lift");
//            NomNomNom = hardwareMap.get(DcMotor.class, "nom");
//            relicArm = hardwareMap.get(DcMotor.class, "relic_arm");



            FrontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
            BackLeftDrive.setDirection(DcMotor.Direction.REVERSE);
            BackRightDrive.setDirection(DcMotor.Direction.FORWARD);
            FrontRightDrive.setDirection(DcMotor.Direction.FORWARD);
//            lift.setDirection(DcMotor.Direction.FORWARD);
//            NomNomNom.setDirection(DcMotor.Direction.FORWARD);
//            relicArm.setDirection(DcMotor.Direction.FORWARD);
            FrontLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            BackLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            BackRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            FrontRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//            lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//            NomNomNom.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//            relicArm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

//
//            telemetry.addData("right up", leftBoxServo.getPosition());
//            telemetry.addData("left up", rightBoxServo.getPosition());
//            telemetry.update();

            // Wait for the game to start (driver presses PLAY)
            waitForStart();
//            runtime.reset();
//            liftIn.setPosition(.9);             //Relic Blocker
//            sleep(1000);                         //brief pause so that wall servo does not interfere with relic arm release
//            wallServo.setPosition(.4);          //Wall servo out
//            liftIn.setPosition(.9);             //Relic Blocker

            while (opModeIsActive()) {
                telemetry.addData("x stick", gamepad1.left_stick_x);
                telemetry.addData("y stick", gamepad1.left_stick_y);

                move();
                telemetry.update();
                idle();
            }
        }
        public enum controllerPos {
            STRAFE_RIGHT, STRAFE_LEFT, DRIVE_FOWARD, DRIVE_BACK, TURN_RIGHT, TURN_LEFT, ZERO;
        }


        //DRIVER CONTROL
        //MOTORS


        public void moveRobot() {
            double drive = -gamepad1.left_stick_y;
            double diagonalDrive = -gamepad1.left_stick_x;
            double turn = (-gamepad1.right_stick_x/1.2)/turnConstant;

            if(drive > 0.1 && (previousDrive == controllerPos.DRIVE_FOWARD || previousDrive == controllerPos.ZERO)) {
                previousDrive = controllerPos.DRIVE_FOWARD;
                Drive(drive, diagonalDrive);
            } else if(drive < -0.1 && (previousDrive == controllerPos.DRIVE_BACK || previousDrive == controllerPos.ZERO)) {
                previousDrive = controllerPos.DRIVE_BACK;
                Drive(drive, diagonalDrive);
            } else if((diagonalDrive<-.4 || gamepad1.dpad_right) && (previousDrive == controllerPos.STRAFE_RIGHT || previousDrive == controllerPos.ZERO)) {
                previousDrive = controllerPos.STRAFE_RIGHT;
                Strafe(-1);
            } else if((diagonalDrive>.4 || gamepad1.dpad_left) && (previousDrive == controllerPos.STRAFE_LEFT || previousDrive == controllerPos.ZERO)) {
                previousDrive = controllerPos.STRAFE_LEFT;
                Strafe(1);
            }  else if(turn > 0.25 &&(previousDrive == controllerPos.TURN_RIGHT || previousDrive == controllerPos.ZERO)){
                previousDrive = controllerPos.TURN_RIGHT;
                turn(turn);
            } else if(turn < -0.25 &&(previousDrive == controllerPos.TURN_LEFT || previousDrive == controllerPos.ZERO)){
                previousDrive = controllerPos.TURN_LEFT;
                turn(turn);
            }
            else {
                previousDrive = controllerPos.ZERO;
                FrontLeftDrive.setPower(0);
                BackLeftDrive.setPower(0);
                FrontRightDrive.setPower(0);
                BackRightDrive.setPower(0);
            }

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

        public void Drive2(double y, double x){
            x = -x;
            double FL = 0;
            double FR = 0;
            double BL = 0;
            double BR = 0;

            if(y>0) {
                FL = 1;
                FR = 1;
                BL = 1;
                BR = 1;
            }
            else if(y<0){
                FL = -1;
                FR = -1;
                BL = -1;
                BR = -1;
            }

            if(y>0&&x>0){
                FR-=(2*x);
                BL-=(2*x);
            }
            else if (y>0&&x<0) {
                FL+=(2*x);
                BR+=(2*x);
            }
            else if (y<0&&x>0){
                FL+=(2*x);
                BR+=(2*x);
            }
            else if (y<0&&x<0){
                FR-=(2*x);
                BL-=(2*x);
            }

            if(y==0){
                FrontLeftDrive.setPower(x);
                FrontRightDrive.setPower(-x);
                BackLeftDrive.setPower(-x);
                BackRightDrive.setPower(x);
            }
            else {
                FrontLeftDrive.setPower(FL);
                FrontRightDrive.setPower(FR);
                BackLeftDrive.setPower(BL);
                BackRightDrive.setPower(BR);
            }
        }

        public void Drive(double drivePower, double diagonalPower) {
            drivePower = readjustMotorPower(drivePower);
            drivePower = Range.clip(drivePower, -1.0, 1.0);
            diagonalPower = Range.clip(diagonalPower, -1.0, 1.0);


            // -1 -.9 -.15 0 .15 .9 1

            if (diagonalPower >= .15 && diagonalPower <= .9) {
                FrontLeftDrive.setPower(drivePower - diagonalPower);
                BackLeftDrive.setPower(drivePower);
                FrontRightDrive.setPower(drivePower);
                BackRightDrive.setPower(drivePower - diagonalPower);
            } else if (diagonalPower <= -.15 && diagonalPower >= -.9) {
                FrontLeftDrive.setPower(drivePower);
                BackLeftDrive.setPower(drivePower - diagonalPower);
                FrontRightDrive.setPower(drivePower - diagonalPower);
                BackRightDrive.setPower(drivePower);
            } else if (diagonalPower < -.9) {
                Strafe(-1);
            } else if (diagonalPower > .9) {
                Strafe(1);
            } else if (diagonalPower < .15 && diagonalPower > -.15) {
                FrontLeftDrive.setPower(drivePower);
                BackLeftDrive.setPower(drivePower);
                FrontRightDrive.setPower(drivePower);
                BackRightDrive.setPower(drivePower); //can make this an else
            }
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

        public void move() {
            x = gamepad1.left_stick_x;
            y = gamepad1.left_stick_y;


            if(Math.abs(x) > .3) {
                FrontLeftDrive.setPower(x);
                FrontRightDrive.setPower(x);
                BackLeftDrive.setPower(x);
                BackRightDrive.setPower(x);
            }
            telemetry.addData("x", x);

        }


    }

