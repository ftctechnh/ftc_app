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

@Autonomous (name="VuLeft", group="Competition Autonomous")
public class VuLeft extends LinearOpMode {


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





                armServo.setPosition(0.0);

                sleep(500); //wait
//-----------------------------Place Marker End----

//-----------------------------Strafe off Lander Start----------------------------------------------
                rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rearLeft.setTargetPosition(-800);
                rearLeft.setPower(.5);
                rearRight.setTargetPosition(800);
                rearRight.setPower(.5);
                frontLeft.setTargetPosition(800);
                frontLeft.setPower(.6);
                frontRight.setTargetPosition(-800);
                frontRight.setPower(.5);
                while (rearLeft.isBusy() &&

                        opModeIsActive())

                {
                }
                while (rearRight.isBusy() &&

                        opModeIsActive())

                {
                }
                while (frontLeft.isBusy() &&

                        opModeIsActive())

                {
                }
                while (frontRight.isBusy() &&

                        opModeIsActive())

                {
                }
                rearLeft.setPower(0);
                rearRight.setPower(0);
                frontLeft.setPower(0);
                frontRight.setPower(0);
//-----------------------------Strafe off Lander End------------------------------------------------

//-----------------------------Straight off Lander Start--------------------------------------------
                rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rearLeft.setTargetPosition(500);
                rearLeft.setPower(.5);
                rearRight.setTargetPosition(500);
                rearRight.setPower(.5);
                frontLeft.setTargetPosition(500);
                frontLeft.setPower(.5);
                frontRight.setTargetPosition(500);
                frontRight.setPower(.5);
                while (rearLeft.isBusy() &&

                        opModeIsActive())

                {
                }
                while (rearRight.isBusy() &&

                        opModeIsActive())

                {
                }
                while (frontLeft.isBusy() &&

                        opModeIsActive())

                {
                }
                while (frontRight.isBusy() &&

                        opModeIsActive())

                {
                }
                rearLeft.setPower(0);
                rearRight.setPower(0);
                frontLeft.setPower(0);
                frontRight.setPower(0);
//-----------------------------Straight off Lander End----------------------------------------------

//-----------------------------Get in Position for Depot Start--------------------------------------
                rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rearLeft.setTargetPosition(1750);
                rearLeft.setPower(.5);
                rearRight.setTargetPosition(-1750);
                rearRight.setPower(.5);
                frontLeft.setTargetPosition(-1750);
                frontLeft.setPower(.5);
                frontRight.setTargetPosition(1750);
                frontRight.setPower(.5);
                while (rearLeft.isBusy() &&

                        opModeIsActive())

                {
                }
                while (rearRight.isBusy() &&

                        opModeIsActive())

                {
                }
                while (frontLeft.isBusy() &&

                        opModeIsActive())

                {
                }
                while (frontRight.isBusy() &&

                        opModeIsActive())

                {
                }
                rearLeft.setPower(0);
                rearRight.setPower(0);
                frontLeft.setPower(0);
                frontRight.setPower(0);
//-----------------------------Get in Position for Depot End----------------------------------------

//-----------------------------Straight to Depot Start----------------------------------------------
                rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rearLeft.setTargetPosition(1300);
                rearLeft.setPower(.9);
                rearRight.setTargetPosition(1300);
                rearRight.setPower(.9);
                frontLeft.setTargetPosition(1300);
                frontLeft.setPower(.9);
                frontRight.setTargetPosition(1300);
                frontRight.setPower(.9);
                while (rearLeft.isBusy() &&

                        opModeIsActive())

                {
                }
                while (rearRight.isBusy() &&

                        opModeIsActive())

                {
                }
                while (frontLeft.isBusy() &&

                        opModeIsActive())

                {
                }
                while (frontRight.isBusy() &&

                        opModeIsActive())

                {
                }
                rearLeft.setPower(0);
                rearRight.setPower(0);
                frontLeft.setPower(0);
                frontRight.setPower(0);
//-----------------------------Straight to Depot End------------------------------------------------


//-----------------------------Get in Position for Gold Start--------------------------------------
                rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rearLeft.setTargetPosition(800);
                rearLeft.setPower(.5);
                rearRight.setTargetPosition(-800);
                rearRight.setPower(.5);
                frontLeft.setTargetPosition(-800);
                frontLeft.setPower(.5);
                frontRight.setTargetPosition(800);
                frontRight.setPower(.5);
                while (rearLeft.isBusy() &&

                        opModeIsActive())

                {
                }
                while (rearRight.isBusy() &&

                        opModeIsActive())

                {
                }
                while (frontLeft.isBusy() &&

                        opModeIsActive())

                {
                }
                while (frontRight.isBusy() &&

                        opModeIsActive())

                {
                }
                rearLeft.setPower(0);
                rearRight.setPower(0);
                frontLeft.setPower(0);
                frontRight.setPower(0);
//-----------------------------Get in Position for Gold End----------------------------------------


//-----------------------------Straight to Depot Start----------------------------------------------
                rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rearLeft.setTargetPosition(2200);
                rearLeft.setPower(.9);
                rearRight.setTargetPosition(2200);
                rearRight.setPower(.9);
                frontLeft.setTargetPosition(2200);
                frontLeft.setPower(.9);
                frontRight.setTargetPosition(2200);
                frontRight.setPower(.9);
                while (rearLeft.isBusy() &&

                        opModeIsActive())

                {
                }
                while (rearRight.isBusy() &&

                        opModeIsActive())

                {
                }
                while (frontLeft.isBusy() &&

                        opModeIsActive())

                {
                }
                while (frontRight.isBusy() &&

                        opModeIsActive())

                {
                }
                rearLeft.setPower(0);
                rearRight.setPower(0);
                frontLeft.setPower(0);
                frontRight.setPower(0);
//-----------------------------Straight to Depot End------------------------------------------------


//-----------------------------Turn Clockwise Start----------------------------------------------
                rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rearLeft.setTargetPosition(500);
                rearLeft.setPower(.9);
                rearRight.setTargetPosition(-500);
                rearRight.setPower(.9);
                frontLeft.setTargetPosition(500);
                frontLeft.setPower(.9);
                frontRight.setTargetPosition(-500);
                frontRight.setPower(.9);
                while (rearLeft.isBusy() &&

                        opModeIsActive())

                {
                }
                while (rearRight.isBusy() &&

                        opModeIsActive())

                {
                }
                while (frontLeft.isBusy() &&

                        opModeIsActive())

                {
                }
                while (frontRight.isBusy() &&

                        opModeIsActive())

                {
                }
                rearLeft.setPower(0);
                rearRight.setPower(0);
                frontLeft.setPower(0);
                frontRight.setPower(0);
//-----------------------------Turn Clockwise End------------------------------------------------

        //-----------------------------Turn Clockwise Start----------------------------------------------
        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rearLeft.setTargetPosition(500);
        rearLeft.setPower(.9);
        rearRight.setTargetPosition(-500);
        rearRight.setPower(.9);
        frontLeft.setTargetPosition(-500);
        frontLeft.setPower(.9);
        frontRight.setTargetPosition(500);
        frontRight.setPower(.9);
        while (rearLeft.isBusy() &&

                opModeIsActive())

        {
        }
        while (rearRight.isBusy() &&

                opModeIsActive())

        {
        }
        while (frontLeft.isBusy() &&

                opModeIsActive())

        {
        }
        while (frontRight.isBusy() &&

                opModeIsActive())

        {
        }
        rearLeft.setPower(0);
        rearRight.setPower(0);
        frontLeft.setPower(0);
        frontRight.setPower(0);
//-----------------------------Turn Clockwise End------------------------------------------------


        //-----------------------------Turn Counterclockwise Start----------------------------------------------
                rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rearLeft.setTargetPosition(1000);
                rearLeft.setPower(.9);
                rearRight.setTargetPosition(1000);
                rearRight.setPower(.9);
                frontLeft.setTargetPosition(1000);
                frontLeft.setPower(.9);
                frontRight.setTargetPosition(1000);
                frontRight.setPower(.9);
                while (rearLeft.isBusy() &&

                        opModeIsActive())

                {
                }
                while (rearRight.isBusy() &&

                        opModeIsActive())

                {
                }
                while (frontLeft.isBusy() &&

                        opModeIsActive())

                {
                }
                while (frontRight.isBusy() &&

                        opModeIsActive())

                {
                }
                rearLeft.setPower(0);
                rearRight.setPower(0);
                frontLeft.setPower(0);
                frontRight.setPower(0);
//-----------------------------Turn Clockwise End------------------------------------------------


//-----------------------------Place Marker Start---------------------------------------------------
                armServo.setPosition(0.7);

                sleep(500); //wait
//-----------------------------Place Marker End-----------------------------------------------------

        //-----------------------------Turn Counterclockwise Start----------------------------------------------
        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rearLeft.setTargetPosition(-500);
        rearLeft.setPower(.9);
        rearRight.setTargetPosition(-500);
        rearRight.setPower(.9);
        frontLeft.setTargetPosition(-500);
        frontLeft.setPower(.9);
        frontRight.setTargetPosition(-500);
        frontRight.setPower(.9);
        while (rearLeft.isBusy() &&

                opModeIsActive())

        {
        }
        while (rearRight.isBusy() &&

                opModeIsActive())

        {
        }
        while (frontLeft.isBusy() &&

                opModeIsActive())

        {
        }
        while (frontRight.isBusy() &&

                opModeIsActive())

        {
        }
        rearLeft.setPower(0);
        rearRight.setPower(0);
        frontLeft.setPower(0);
        frontRight.setPower(0);
//-----------------------------Turn Clockwise End------------------------------------------------



//-----------------------------Turn Counterclockwise Start----------------------------------------------
                rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rearLeft.setTargetPosition(-2300);
                rearLeft.setPower(.9);
                rearRight.setTargetPosition(2300);
                rearRight.setPower(.9);
                frontLeft.setTargetPosition(-2300);
                frontLeft.setPower(.9);
                frontRight.setTargetPosition(2300);
                frontRight.setPower(.9);
                while (rearLeft.isBusy() &&

                        opModeIsActive())

                {
                }
                while (rearRight.isBusy() &&

                        opModeIsActive())

                {
                }
                while (frontLeft.isBusy() &&

                        opModeIsActive())

                {
                }
                while (frontRight.isBusy() &&

                        opModeIsActive())

                {
                }
                rearLeft.setPower(0);
                rearRight.setPower(0);
                frontLeft.setPower(0);
                frontRight.setPower(0);
//-----------------------------Turn Clockwise End------------------------------------------------

//-----------------------------Strafe Right Start----------------------------------------------
                rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rearLeft.setTargetPosition(-2000);
                rearLeft.setPower(.5);
                rearRight.setTargetPosition(2000);
                rearRight.setPower(.5);
                frontLeft.setTargetPosition(2000);
                frontLeft.setPower(.6);
                frontRight.setTargetPosition(-2000);
                frontRight.setPower(.5);
                while (rearLeft.isBusy() &&

                        opModeIsActive())

                {
                }
                while (rearRight.isBusy() &&

                        opModeIsActive())

                {
                }
                while (frontLeft.isBusy() &&

                        opModeIsActive())

                {
                }
                while (frontRight.isBusy() &&

                        opModeIsActive())

                {
                }
                rearLeft.setPower(0);
                rearRight.setPower(0);
                frontLeft.setPower(0);
                frontRight.setPower(0);
//-----------------------------Strafe Right End------------------------------------------------


//-----------------------------Straight Start--------------------------------------------
                rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rearLeft.setTargetPosition(4800);
                rearLeft.setPower(1);
                rearRight.setTargetPosition(4800);
                rearRight.setPower(1);
                frontLeft.setTargetPosition(4800);
                frontLeft.setPower(1);
                frontRight.setTargetPosition(4800);
                frontRight.setPower(1);
                while (rearLeft.isBusy() &&

                        opModeIsActive())

                {
                }
                while (rearRight.isBusy() &&

                        opModeIsActive())

                {
                }
                while (frontLeft.isBusy() &&

                        opModeIsActive())

                {
                }
                while (frontRight.isBusy() &&

                        opModeIsActive())

                {
                }
                rearLeft.setPower(0);
                rearRight.setPower(0);
                frontLeft.setPower(0);
                frontRight.setPower(0);
//-----------------------------Straight End----------------------------------------------


                sleep(25000);


//----------------------------------------------------------------

            }
}
