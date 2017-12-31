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
@Autonomous(name = "Auto B1", group = "Blue")
public class Auto_B1 extends OpMode {

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

    private String stageCheck;

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


        //Starts the timer WORKING
        time.reset();

    }
        @Override
        public void loop(){
            currentSeconds = time.seconds();
            switch(state){
                case TIME:
                    //Starts the timer
                    state = States.GRAB;
                    robot.blockLift.clamp(false,true, true, false);
                    stageCheck += "Time - ";
                    break;
                case GRAB:
                    robot.blockLift.clamp(false,false, false, true);
                    state = States.LIFT;

                    goalSeconds = currentSeconds + 0.5;
                    stageCheck += "Grab - ";
                    break;
                case LIFT:
                    if (currentSeconds >= goalSeconds) {
                        robot.blockLift.setLift(500);
                        state = States.ARMDOWN;
                        goalSeconds = currentSeconds += 1.0;
                        stageCheck += "Lift - ";
                    }
                    break;
                case ARMDOWN:
                    //Lowers left arm
                    leftArm.setPosition(goalPosition);
                    if(currentSeconds >= goalSeconds){
                        state = States.READ; //READ
                        stageCheck += "Arm Down - ";
                    }
                    break;
                case READ:
                    //Reads the color/distance sensor to determine which ball to knock off
                    if(colorSensorLeft.blue() > colorSensorLeft.red()){
                        telemetry.addData("Blue:", colorSensorLeft.blue());
                        telemetry.addData("Red:", colorSensorLeft.red());
                        telemetry.addData("The ball is:", "blue");
                        telemetry.update();

                        state = States.LEFTKNOCK;
                        stageCheck += "Read - ";
                    } else if(colorSensorLeft.red() > colorSensorLeft.blue()){
                        telemetry.addData("Blue:", colorSensorLeft.blue());
                        telemetry.addData("Red:", colorSensorLeft.red());
                        telemetry.addData("The ball is:", "red");
                        telemetry.update();

                        state = States.RIGHTKNOCK;
                    }
                    break;
                case LEFTKNOCK:
                    //Knocks the left ball off of the pedestal
                    if(!isFinished){
                        isFinished = robot.driveTrain.encoderDrive(DriveTrain.Direction.S, 0.25, 1);
                    } else{
                        isFinished = false;
                        state = States.LEFTARMUP;
                        time.reset();
                        stageCheck += "Left Knock - ";
                    }
                    break;
                case RIGHTKNOCK:
                    //Knocks the right ball off of the pedestal
                    if(!isFinished){
                        isFinished = robot.driveTrain.encoderDrive(DriveTrain.Direction.N, 0.25, 1);
                    } else{
                        isFinished = false;
                        state = States.RIGHTARMUP;
                        time.reset();
                        stageCheck += "Right Knock - ";
                    }
                    break;
                case LEFTARMUP:
                    //Lifts arm up after knocking left ball
                    leftArm.setPosition(0.85);
                    if(time.seconds() >= 1){
                        state = States.LEFTZONE;
                        stageCheck += "Left Arm Up - ";
                    }
                    break;
                case RIGHTARMUP:
                    //Lifts arm up after knocking right ball
                    leftArm.setPosition(0.85);
                    if(time.seconds() >= 1){
                        state = States.RIGHTZONE;
                        stageCheck += "Right Arm Up - ";
                    }
                    break;
                case LEFTZONE:
                    //Returns to original position from knocking left ball
                    if(!isFinished){
                        isFinished = robot.driveTrain.encoderDrive(DriveTrain.Direction.N, 0.4, 10 );
                    } else{
                        isFinished = false;
                        state = States.TURNBOX;
                        time.reset();
                        stageCheck += "Left Zone - ";
                    }
                    break;
                case RIGHTZONE:
                    //Returns to original position from knocking right ball
                    if(!isFinished){
                        isFinished = robot.driveTrain.encoderDrive(DriveTrain.Direction.N, 0.4, 5);
                    } else{
                        isFinished = false;
                        state = States.TURNBOX;
                        time.reset();
                        stageCheck += "Right Zone - ";
                    }
                    break;
                case TURNBOX:
                    //Turns left to face CryptoBox. UNTESTED
                    if(!isFinished){
                        isFinished = robot.driveTrain.gyroTurn(DriveTrain.Direction.TURNLEFT, 0.25, 90);
                    } else{
                        isFinished = false;
                        state = States.DRIVEBOX;
                        stageCheck += "Turn Box - ";
                    }
                    break;
                case DRIVEBOX:
                    if(!isFinished){
                        isFinished = robot.driveTrain.encoderDrive(DriveTrain.Direction.N, 0.25, 3);
                    } else{
                        isFinished = false;
                        state = States.DROP;
                        stageCheck += "Drive Box - ";
                    }
                    break;
                case DROP:
                    robot.blockLift.clamp(false, false,true, false);
                    state = States.DRIVEBACK;
                    break;
                case DRIVEBACK:
                    if(!isFinished){
                        isFinished = robot.driveTrain.encoderDrive(DriveTrain.Direction.S, 0.3, 1.5);
                    } else{
                        isFinished = false;
                        state = States.CLOSE;
                        stageCheck += "DriveBack - ";
                        stageCheck += "End";
                        goalSeconds = currentSeconds += 0.4;
                    }
                    break;
                case CLOSE:
                    robot.blockLift.clamp(true, false, false, true);
                    if (currentSeconds >= goalSeconds) {
                        state = States.DRIVEFORWARD;
                    }
                case DRIVEFORWARD:
                    if (!isFinished) {
                        isFinished = robot.driveTrain.encoderDrive(DriveTrain.Direction.N, 0.3, 1.5);
                    } else {
                        isFinished = false;
                        state = States.OPEN;
                        goalSeconds = currentSeconds += 0.4;
                    }
                case OPEN:
                    robot.blockLift.clamp(false, true, true, false);
                    if (currentSeconds >= goalSeconds) {
                        state = States.BACKUPFROMBLOCK;
                    }
                case BACKUPFROMBLOCK:
                    if(!isFinished){
                        isFinished = robot.driveTrain.encoderDrive(DriveTrain.Direction.S, 0.3, 1.5);
                    } else{
                        isFinished = false;
                        state = States.ROTATE;
                    }
                case ROTATE:
                    if (!isFinished) {
                        isFinished = robot.driveTrain.gyroTurn(DriveTrain.Direction.TURNRIGHT, 0.3, 180);
                    } else {
                        isFinished = false;
                        state = States.END;
                    }
                case END:
                    robot.driveTrain.stop();
                    break;
            }
            telemetry.addData("State Check: ", stageCheck);
        }
}
