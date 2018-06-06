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
import org.firstinspires.ftc.teamcode.GMR.Robot.SubSystems.AllianceColor;
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

    private String completedStates = "";

    private int turnRadius = 20;
    private double turnPower = 0.15;

    private int keyColumn;
    private double columnDist;

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


        robot = new Robot(hardwareMap, telemetry, true);

        goalPosition = 0.25;
        position = 0.85;
        leftArm.setPosition(position); //vertical start
        rightArm.setPosition(0);
        // position

        state = States.TIME;
        isFinished = false;

        keyColumn = 0;
        columnDist = 0;

        time.reset();

        robot.setRelicTilt();
    }
        @Override
        public void loop(){
            currentSeconds = time.seconds();
            switch(state){
                case TIME:
                    state = States.SCAN;
                    goalSeconds = currentSeconds += 5.5;
                    break;
                case SCAN:
                    keyColumn = robot.vision.keyColumnDetect(AllianceColor.BLUE);
                    if(keyColumn != 0 || currentSeconds >= goalSeconds){
                        state = States.ARMDOWN;
                        goalSeconds = currentSeconds += 0.5;
                    }
                    break;
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
                    }
                    break;

                case LEFTKNOCK:
                    //Knocks the left ball off of the pedestal WORKING
                    if(robot.driveTrain.gyroTurn(DriveTrain.Direction.TURNLEFT, turnPower, turnRadius)){
                        isFinished = false;
                        state = States.LEFTARMUP;
                    } break;

                case RIGHTKNOCK:
                    //Knocks the right ball off of the pedestal WORKING
                    if(robot.driveTrain.gyroTurn(DriveTrain.Direction.TURNRIGHT, turnPower, turnRadius)){
                        isFinished = false;
                        state = States.RIGHTARMUP;
                        goalSeconds = currentSeconds += 1.0;
                    } break;

                case LEFTARMUP:
                    //Lifts arm up after knocking left ball WORKING
                    leftArm.setPosition(0.85);
                    state = States.LEFTZONE;
                    break;

                case RIGHTARMUP:
                    //Lifts arm up after knocking right ball WORKING
                    leftArm.setPosition(0.85);
                    if(currentSeconds >= goalSeconds){
                        state = States.RIGHTZONE;
                    } break;

                case LEFTZONE:
                    //Returns to original position from knocking left ball WORKING
                    if(robot.driveTrain.gyroTurn(DriveTrain.Direction.TURNRIGHT, turnPower, turnRadius)){
                        isFinished = false;
                        state = States.OFFSTONE;
                    } break;

                case RIGHTZONE:
                    //Returns to original position from knocking right ball WORKING
                    if(robot.driveTrain.gyroTurn(DriveTrain.Direction.TURNLEFT, turnPower, turnRadius)){
                        isFinished = false;
                        state = States.OFFSTONE;
                    } break;

                case OFFSTONE:
                    if(robot.driveTrain.encoderDrive(DriveTrain.Direction.N, 0.2, 6.0 )) {
                        state = States.STRAFE;
                        completedStates += "OFFSTONE - ";
                    }
                    break;
                case STRAFE:
                    //Turns left to face CryptoBox. WORKING
                    if (keyColumn == 1){
                        columnDist = 2.0;
                    } else if (keyColumn == 2){
                        columnDist = 4.0;
                    } else if (keyColumn == 3){
                        columnDist = 6.0;
                    } else if (keyColumn == 0){
                        columnDist = 4.0;
                    }

                    if(!isFinished){
                        isFinished = robot.driveTrain.encoderDrive(DriveTrain.Direction.E, 0.3, columnDist);
                    } else{
                        isFinished = false;
                        state = States.DRIVEBOX;
                        completedStates += "STRAFE - ";
                    }
                    break;

                case DRIVEBOX:
                    //Drives into CryptoBox
                    if(!isFinished){
                        isFinished = robot.driveTrain.encoderDrive(DriveTrain.Direction.N, 0.3, 1.5);
                    } else{
                        isFinished = false;
                        state = States.DROP;
                        completedStates += "DRIVEBOX";
                        goalSeconds = currentSeconds += 1.0;
                    }

                    break;
                case DROP:
                    robot.blockLift.grab(true, 0);
                    if (currentSeconds >= goalSeconds) {
                        state = States.DRIVEBACK;
                        robot.blockLift.grab(false, 0);
                    }
                    break;
                case DRIVEBACK:
                    if(!isFinished){
                        isFinished = robot.driveTrain.encoderDrive(DriveTrain.Direction.S, 0.3, 1.5);
                    } else{
                        isFinished = false;
                        state = States.GLYPHPITTURN;
                    } break;
                case GLYPHPITTURN:
                    if (!isFinished) {
                        isFinished = robot.driveTrain.gyroTurn(DriveTrain.Direction.TURNRIGHT, 0.3, 135);
                    } else {
                        isFinished = false;
                        state = States.END;
                    }break;
                case END:
                    robot.driveTrain.stop();
                    break;
            }
            telemetry.addData("Current State: ", state);
            telemetry.addData("Completed States: ", completedStates);
        }

}