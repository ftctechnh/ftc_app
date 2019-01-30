package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.vision.MasterVision;
import org.firstinspires.ftc.teamcode.vision.SampleRandomizedPositions;

@Autonomous (name="VuDepotSide", group="Competition Autonomous")
public class VuDepotSide extends LinearOpMode{
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
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        rearLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        rearRight.setDirection(DcMotor.Direction.FORWARD);
        liftMotor.setDirection(DcMotor.Direction.REVERSE);
        armServo.setDirection(Servo.Direction.FORWARD);

        mrGyro = (ModernRoboticsI2cGyro) sensorGyro;


        double turnSpeed = 0.2;
        int zAccumulated;
        int target = 0;

        telemetry.addData("Mode", "waiting");
        telemetry.update();


        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;// recommended camera direction
        parameters.vuforiaLicenseKey = "AbVGrK7/////AAABmV5qNYRo8EpalbdT9iVSnmNR6wynVnTYxdfuU0jrIQJY3/bNzMRAOB9ew/OVmuVwRluGP3sUUHaNIgpXOii6OX5JQHTGyOeDMkVtqPdvynUdw7hRhLL2a8L8nQzJdH4jrKTCB6hAykKflqR4dykoml54fOnuTuXzGgwydwHCkcwt3UnDy/kCMrmSSx/9hBW21N4m6vhqzM9cdhUAGvvQAJPEE7WjrfT14Z4onzZXM185HCLKIEXcaJx10MaGO/xHchVtbvMGB2zDzFJ57uG2+AJopJtI+Qh1anzqoPnolZMUwJHRBhQnxis+QGpoL1RiJ6HqTRQr5mAEuP3q4wX5I1WXydNah5JoLgekylpWKANr\n";

        vision = new MasterVision(parameters, hardwareMap, false, MasterVision.TFLiteAlgorithm.INFER_LEFT);
        vision.init();// enables the camera overlay. this will take a couple of seconds
        vision.enable();// enables the tracking algorithms. this might also take a little time

        waitForStart();

        vision.disable();// disables tracking algorithms. this will free up your phone's processing power for other jobs.

        goldPosition = vision.getTfLite().getLastKnownSampleOrder();

        while(opModeIsActive()){
            telemetry.addData("goldPosition was", goldPosition);// giving feedback

            switch (goldPosition) { // using for things in the autonomous program




                case LEFT:
                {telemetry.addLine("going to the left");


                    vision.shutdown();





                    //-------------------Place Marker Start--------------------------------------------------
                    armServo.setPosition(0.0);
                    sleep(400); //wait


                    //-------------------Block End--------------------------------------------------

                    //-----------------------------Lift Up(lower robot) Start-------------------------------------------
                    operateLift(1, 1, 5);
                    //Wait 1 second
                    sleep(500);
//-----------------------------Lift Up(lower robot) End---------------------------------------------


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
                    rearLeft.setTargetPosition(500);
                    rearLeft.setPower(.5);
                    rearRight.setTargetPosition(-500);
                    rearRight.setPower(.5);
                    frontLeft.setTargetPosition(-500);
                    frontLeft.setPower(.5);
                    frontRight.setTargetPosition(500);
                    frontRight.setPower(.5);
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
                    rearLeft.setTargetPosition(500);
                    rearLeft.setPower(.9);
                    rearRight.setTargetPosition(500);
                    rearRight.setPower(.9);
                    frontLeft.setTargetPosition(500);
                    frontLeft.setPower(.9);
                    frontRight.setTargetPosition(500);
                    frontRight.setPower(.9);
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
                    rearLeft.setTargetPosition(-2300);
                    rearLeft.setPower(.9);
                    rearRight.setTargetPosition(2300);
                    rearRight.setPower(.9);
                    frontLeft.setTargetPosition(-2300);
                    frontLeft.setPower(.9);
                    frontRight.setTargetPosition(2300);
                    frontRight.setPower(.9);
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
//-----------------------------Straight End----------------------------------------------

//-----------------------------Lift Down Start------------------------------------------------------
                    operateLift(0, 1, 5);
                    //Wait 1 second
                    sleep(500);
//-----------------------------Lift Down End--------------------------------------------------------



                    sleep(25000);



                   //----------------------------------------------------------------
                }
                break;































                case CENTER: {
                    telemetry.addLine("going straight");


                    vision.shutdown();


                    //-------------------Place Marker Start--------------------------------------------------
                    armServo.setPosition(0.0);
                    sleep(400); //wait


                    //-------------------Block End--------------------------------------------------

                    //-----------------------------Lift Up(lower robot) Start-------------------------------------------
                    operateLift(1, 1, 5);
                    //Wait 1 second
                    sleep(500);
//-----------------------------Lift Up(lower robot) End----------------------------------------------


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
                    frontLeft.setPower(.5);
                    frontRight.setTargetPosition(-800);
                    frontRight.setPower(.5);
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
                    rearLeft.setTargetPosition(800);
                    rearLeft.setPower(.5);
                    rearRight.setTargetPosition(-800);
                    rearRight.setPower(.5);
                    frontLeft.setTargetPosition(-800);
                    frontLeft.setPower(.5);
                    frontRight.setTargetPosition(800);
                    frontRight.setPower(.5);
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
                    rearLeft.setTargetPosition(3640);
                    rearLeft.setPower(.9);
                    rearRight.setTargetPosition(3640);
                    rearRight.setPower(.9);
                    frontLeft.setTargetPosition(3640);
                    frontLeft.setPower(.9);
                    frontRight.setTargetPosition(3640);
                    frontRight.setPower(.9);
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
//-----------------------------Straight to Depot End------------------------------------------------


//-----------------------------Place Marker Start---------------------------------------------------
                    armServo.setPosition(0.7);
                    sleep(600); //wait

//-----------------------------Place Marker End-----------------------------------------------------


//-----------------------------Turn 45 Degrees Counterclockwise Start--------------------------------------
                    rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    rearLeft.setTargetPosition(-640);
                    rearLeft.setPower(1);
                    rearRight.setTargetPosition(640);
                    rearRight.setPower(1);
                    frontLeft.setTargetPosition(-640);
                    frontLeft.setPower(1);
                    frontRight.setTargetPosition(640);
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
//-----------------------------Turn 45 Degrees Counterclockwise End---------------------------------

//-----------------------------Strafe Towards Wall Start--------------------------------------------
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
                    frontLeft.setPower(.5);
                    frontRight.setTargetPosition(-2000);
                    frontRight.setPower(.5);
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
//-----------------------------Strafe Towards Wall End----------------------------------------------

//-----------------------------Backwards Towards Crater Start-----------------------------------------
                    rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    rearLeft.setTargetPosition(-6050);
                    rearLeft.setPower(1);
                    rearRight.setTargetPosition(-6050);
                    rearRight.setPower(1);
                    frontLeft.setTargetPosition(-6050);
                    frontLeft.setPower(1);
                    frontRight.setTargetPosition(-6050);
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
//-----------------------------Backwards Towards Crater End-------------------------------------------

//-----------------------------Lift Down Start------------------------------------------------------
                    operateLift(0, 1, 5);
                    //Wait 1 second
                    sleep(500);
//-----------------------------Lift Down End--------------------------------------------------------


                    sleep(25000);


                }

                break;


















                case RIGHT: {
                    telemetry.addLine("going to the right");


                    vision.shutdown();



                    //-------------------Place Marker Start--------------------------------------------------
                    armServo.setPosition(0.0);
                    sleep(400); //wait


                    //-------------------Block End--------------------------------------------------

                    //-----------------------------Lift Up(lower robot) Start-------------------------------------------
                    operateLift(1, 1, 5);
                    //Wait 1 second
                    sleep(500);
//-----------------------------Lift Up(lower robot) End---------------------------------------------

//----------------------Strafe off Lander Start-------------------------------------
                    rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    rearLeft.setTargetPosition(-900);
                    rearLeft.setPower(0.5);
                    rearRight.setTargetPosition(900);
                    rearRight.setPower(0.5);
                    frontLeft.setTargetPosition(900);
                    frontLeft.setPower(0.6);
                    frontRight.setTargetPosition(-900);
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
                    rearLeft.setTargetPosition(800);
                    rearLeft.setPower(0.5);
                    rearRight.setTargetPosition(800);
                    rearRight.setPower(0.5);
                    frontLeft.setTargetPosition(800);
                    frontLeft.setPower(0.5);
                    frontRight.setTargetPosition(800);
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

//----------------------Strafe Start-------------------------------------
                    rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    rearLeft.setTargetPosition(-1000);
                    rearLeft.setPower(0.5);
                    rearRight.setTargetPosition(1000);
                    rearRight.setPower(0.5);
                    frontLeft.setTargetPosition(1000);
                    frontLeft.setPower(0.5);
                    frontRight.setTargetPosition(-1000);
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

                    //----------------------Straight Start-------------------------------------
                    rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    rearLeft.setTargetPosition(1800);
                    rearLeft.setPower(0.8);
                    rearRight.setTargetPosition(1800);
                    rearRight.setPower(0.8);
                    frontLeft.setTargetPosition(1800);
                    frontLeft.setPower(0.8);
                    frontRight.setTargetPosition(1800);
                    frontRight.setPower(0.8);
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


//-------------------Turn 45 degrees counterclockwise start-------------------------------
                    rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    rearLeft.setTargetPosition(-700);
                    rearLeft.setPower(.9);
                    rearRight.setTargetPosition(700);
                    rearRight.setPower(.9);
                    frontLeft.setTargetPosition(-700);
                    frontLeft.setPower(.9);
                    frontRight.setTargetPosition(700);
                    frontRight.setPower(.9);
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
//------------------------Turn 45 degrees counterclockwise end-------------------------------


//-----------------------------Strafe Right Start----------------------------------------------
                    rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    rearLeft.setTargetPosition(-2200);
                    rearLeft.setPower(.5);
                    rearRight.setTargetPosition(2200);
                    rearRight.setPower(.5);
                    frontLeft.setTargetPosition(2200);
                    frontLeft.setPower(.6);
                    frontRight.setTargetPosition(-2200);
                    frontRight.setPower(.5);
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
//-----------------------------Strafe Right End------------------------------------------------

//-----------------------------Place Marker Start---------------------------------------------------
                    armServo.setPosition(0.7);
                    sleep(500); //wait
//-----------------------------Place Marker End-----------------------------------------------------



//-----------------------------Straight to Crater Start----------------------------------------------
                    rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    rearLeft.setTargetPosition(-5500);
                    rearLeft.setPower(.9);
                    rearRight.setTargetPosition(-5500);
                    rearRight.setPower(.9);
                    frontLeft.setTargetPosition(-5500);
                    frontLeft.setPower(.9);
                    frontRight.setTargetPosition(-5500);
                    frontRight.setPower(.9);
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
//-----------------------------Straight to Crater End------------------------------------------------
                    //-----------------------------Lift Down Start------------------------------------------------------
                    operateLift(0, 1, 5);
                    //Wait 1 second
                    sleep(500);
//-----------------------------Lift Down End--------------------------------------------------------

                    sleep(25000);
                }

                break;




























                case UNKNOWN: {
                    telemetry.addLine("staying put");

                    //-------------------Place Marker Start--------------------------------------------------
                    armServo.setPosition(0.0);
                    sleep(400); //wait


                    //-------------------Block End--------------------------------------------------

                    //-----------------------------Lift Up(lower robot) Start-------------------------------------------
                    operateLift(1, 1, 5);
                    //Wait 1 second
                    sleep(500);
//-----------------------------Lift Up(lower robot) End---------------------------------------------


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
                    frontLeft.setPower(.5);
                    frontRight.setTargetPosition(-800);
                    frontRight.setPower(.5);
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
                    rearLeft.setTargetPosition(800);
                    rearLeft.setPower(.5);
                    rearRight.setTargetPosition(-800);
                    rearRight.setPower(.5);
                    frontLeft.setTargetPosition(-800);
                    frontLeft.setPower(.5);
                    frontRight.setTargetPosition(800);
                    frontRight.setPower(.5);
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
                    rearLeft.setTargetPosition(3640);
                    rearLeft.setPower(.9);
                    rearRight.setTargetPosition(3640);
                    rearRight.setPower(.9);
                    frontLeft.setTargetPosition(3640);
                    frontLeft.setPower(.9);
                    frontRight.setTargetPosition(3640);
                    frontRight.setPower(.9);
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
//-----------------------------Straight to Depot End------------------------------------------------


//-----------------------------Place Marker Start---------------------------------------------------
                    armServo.setPosition(0.7);
                    sleep(600); //wait

//-----------------------------Place Marker End-----------------------------------------------------


//-----------------------------Turn 45 Degrees Counterclockwise Start--------------------------------------
                    rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    rearLeft.setTargetPosition(-640);
                    rearLeft.setPower(1);
                    rearRight.setTargetPosition(640);
                    rearRight.setPower(1);
                    frontLeft.setTargetPosition(-640);
                    frontLeft.setPower(1);
                    frontRight.setTargetPosition(640);
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
//-----------------------------Turn 45 Degrees Counterclockwise End---------------------------------

//-----------------------------Strafe Towards Wall Start--------------------------------------------
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
                    frontLeft.setPower(.5);
                    frontRight.setTargetPosition(-2000);
                    frontRight.setPower(.5);
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
//-----------------------------Strafe Towards Wall End----------------------------------------------

//-----------------------------Backwards Towards Crater Start-----------------------------------------
                    rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    rearLeft.setTargetPosition(-6050);
                    rearLeft.setPower(1);
                    rearRight.setTargetPosition(-6050);
                    rearRight.setPower(1);
                    frontLeft.setTargetPosition(-6050);
                    frontLeft.setPower(1);
                    frontRight.setTargetPosition(-6050);
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
//-----------------------------Backwards Towards Crater End-------------------------------------------

//-----------------------------Lift Down Start------------------------------------------------------
                    operateLift(0, 1, 5);
                    //Wait 1 second
                    sleep(500);
//-----------------------------Lift Down End--------------------------------------------------------

                    sleep(25000);

                }
                break;

            }

            telemetry.update();
        }

        vision.shutdown();
    }






















    public void turn(int target) throws InterruptedException {
        turnAbsolute(target + mrGyro.getIntegratedZValue());
    }


    public void turnAbsolute(int target) throws InterruptedException {
        int zAccumulated = mrGyro.getIntegratedZValue();
        double turnSpeed = .18;

        while (Math.abs(zAccumulated - target) > 3) {
            if (zAccumulated > target) {
                rearLeft.setPower(turnSpeed);
                frontLeft.setPower(turnSpeed);
                rearRight.setPower(-turnSpeed);
                frontRight.setPower(-turnSpeed);
            }
            if (zAccumulated < target) {
                rearLeft.setPower(-turnSpeed);
                frontLeft.setPower(-turnSpeed);
                rearRight.setPower(turnSpeed);
                frontRight.setPower(turnSpeed);
            }

            waitOneFullHardwareCycle();

            zAccumulated = mrGyro.getIntegratedZValue();
            telemetry.addData("1. accu", String.format("%03d", zAccumulated));
        }
        rearLeft.setPower(0);
        rearRight.setPower(0);
        frontLeft.setPower(0);
        frontRight.setPower(0);

        telemetry.addData("1. accu", String.format("%03d", zAccumulated));

        waitOneFullHardwareCycle();

    }


    public void operateLift(int position, double speed, int timeoutS){
        if (opModeIsActive()) {
            if (position == 0) {
                //Change This Number to Determine the Upper Position of the Lift
                liftMotor.setTargetPosition(liftMotor.getCurrentPosition() - 8200
                );
                liftMotor.setPower(-speed);
            } else {
                //Change This Number to Determine the Lower Position of the Lift
                liftMotor.setTargetPosition(liftMotor.getCurrentPosition() + 8200
                );
                liftMotor.setPower(-speed);
            }
            liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            runtime.reset();

            while (liftMotor.isBusy() && opModeIsActive() &&
                    (runtime.seconds() < timeoutS)) {
                // Display it for the driver.
                telemetry.addData("Lift", "Running to %7d : %7d", liftMotor.getCurrentPosition(), liftMotor.getTargetPosition());
//                telemetry.addData("Path2",  "Running at %7d :%7d",
//                                            robot.leftDrive.getCurrentPosition(),
//                                            robot.rightDrive.getCurrentPosition());
                telemetry.update();
            }
            liftMotor.setPower(0);
            liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }}}