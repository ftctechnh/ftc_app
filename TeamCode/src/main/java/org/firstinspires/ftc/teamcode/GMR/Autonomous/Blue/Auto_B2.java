package org.firstinspires.ftc.teamcode.GMR.Autonomous.Blue;

import com.qualcomm.hardware.kauailabs.NavxMicroNavigationSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.IntegratingGyroscope;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.GMR.Autonomous.States;
import org.firstinspires.ftc.teamcode.GMR.Robot.Robot;
import org.firstinspires.ftc.teamcode.GMR.Robot.SubSystems.DriveTrain;

/**
 * Created by FTC 4316 on 11/11/2017
 */
@Autonomous(name = "Auto B2", group = "Blue")
public class Auto_B2 extends OpMode {

    private Robot robot;

    private DcMotor leftFront;
    private DcMotor rightFront;
    private DcMotor leftRear;
    private DcMotor rightRear;

    private NavxMicroNavigationSensor gyroscope;
    private IntegratingGyroscope gyro;

    private Servo leftArm;
    private Servo rightArm;
    private ColorSensor colorSensorLeft;
    private DistanceSensor distanceSensorLeft;

    private States state;

    private boolean isFinished;

    private double position;
    private double goalPosition;

    private ElapsedTime time = new ElapsedTime();

    private double currentSeconds;
    private double goalSeconds;

    @Override
    public void init() {
        rightFront = hardwareMap.dcMotor.get("rightfront");
        leftFront = hardwareMap.dcMotor.get("leftfront");
        rightRear = hardwareMap.dcMotor.get("rightrear");
        leftRear = hardwareMap.dcMotor.get("leftrear");

        leftArm = hardwareMap.get(Servo.class, "leftArm");
        rightArm = hardwareMap.get(Servo.class, "rightArm");

        colorSensorLeft = hardwareMap.get(ColorSensor.class, "colorDistanceLeft");
        distanceSensorLeft = hardwareMap.get(DistanceSensor.class, "colorDistanceLeft");

        gyroscope = hardwareMap.get(NavxMicroNavigationSensor.class, "navx");


        robot = new Robot(hardwareMap, telemetry);

        goalPosition = 0.25;
        position = 0.85;
        leftArm.setPosition(position); //vertical start
        rightArm.setPosition(0);
        // position

        state = States.TIME;
        isFinished = false;
    }
        @Override
        public void loop(){
            currentSeconds = time.seconds();
            switch(state){
                case TIME:
                    state = States.GRAB;
                    robot.blockLift.clamp(false,true, true, false);
                    break;
                case GRAB:
                    robot.blockLift.clamp(false,false, false, true);
                    state = States.LIFT;
                    goalSeconds = currentSeconds + 0.4;
                case LIFT:
                    if (currentSeconds >= goalSeconds) {
                        robot.blockLift.setLift(400);
                        state = States.ARMDOWN;
                        goalSeconds = currentSeconds += 2.0;
                    }
                case ARMDOWN:
                    //Lowers right arm WORKING
                    leftArm.setPosition(goalPosition);
                    if(currentSeconds >= goalSeconds){
                        state = States.READ; //READ
                    } break;

                case READ:
                    //Reads the color/distance sensor to determine which ball to knock off WORKING
                    if(colorSensorLeft.blue() > colorSensorLeft.red()){
                        telemetry.addData("Blue:", colorSensorLeft.blue());
                        telemetry.addData("Red:", colorSensorLeft.red());
                        telemetry.addData("The ball is:", "blue");
                        telemetry.update();

                        state = States.LEFTKNOCK;
                    } else if(colorSensorLeft.red() > colorSensorLeft.blue()){
                        telemetry.addData("Blue:", colorSensorLeft.blue());
                        telemetry.addData("Red:", colorSensorLeft.red());
                        telemetry.addData("The ball is:", "red");
                        telemetry.update();

                        state = States.RIGHTKNOCK;
                    } break;

                case LEFTKNOCK:
                    //Knocks the left ball off of the pedestal WORKING
                    if(!isFinished){
                        isFinished = robot.driveTrain.encoderDrive(DriveTrain.Direction.S, 0.25, 0.5);
                    } else{
                        isFinished = false;
                        state = States.LEFTARMUP;
                        time.reset();
                    } break;

                case RIGHTKNOCK:
                    //Knocks the right ball off of the pedestal WORKING
                    if(!isFinished){
                        isFinished = robot.driveTrain.encoderDrive(DriveTrain.Direction.N, 0.25, 1);
                    } else{
                        isFinished = false;
                        state = States.RIGHTARMUP;
                        time.reset();
                    } break;

                case LEFTARMUP:
                    //Lifts arm up after knocking left ball WORKING
                    leftArm.setPosition(0.85);
                    if(time.seconds() >= 1){
                        state = States.LEFTZONE;
                    } break;

                case RIGHTARMUP:
                    //Lifts arm up after knocking right ball WORKING
                    leftArm.setPosition(0.85);
                    if(time.seconds() >= 1){
                        state = States.RIGHTZONE;
                    } break;

                case LEFTZONE:
                    //Returns to original position from knocking left ball WORKING
                    if(!isFinished){
                        isFinished = robot.driveTrain.encoderDrive(DriveTrain.Direction.N, 0.4, 9.5);
                    } else{
                        isFinished = false;
                        state = States.STRAFE;
                        time.reset();
                    } break;

                case RIGHTZONE:
                    //Returns to original position from knocking right ball WORKING
                    if(!isFinished){
                        isFinished = robot.driveTrain.encoderDrive(DriveTrain.Direction.N, 0.4, 2.5);
                    } else{
                        isFinished = false;
                        state = States.STRAFE;
                        time.reset();
                    } break;

                case STRAFE:
                    //Turns left to face CryptoBox. WORKING
                    if(!isFinished){
                        isFinished = robot.driveTrain.encoderDrive(DriveTrain.Direction.E, 0.3, 3);
                    } else{
                        isFinished = false;
                        state = States.DRIVEBOX;
                    } break;

                case DRIVEBOX:
                    //Drives into CryptoBox
                    if(!isFinished){
                        isFinished = robot.driveTrain.encoderDrive(DriveTrain.Direction.N, 0.3, 3.5);
                    } else{
                        isFinished = false;
                        state = States.DROP;
                    } break;

                case DROP:
                    robot.blockLift.clamp(false, false,true, false);
                    state = States.DRIVEBACK;
                    break;
                case DRIVEBACK:
                    if(!isFinished){
                        isFinished = robot.driveTrain.encoderDrive(DriveTrain.Direction.S, 0.3, 1.5);
                    } else{
                        isFinished = false;
                        state = States.END;
                    } break;
                case END:
                    robot.driveTrain.stop();
                    break;
            }

        }

}