package org.firstinspires.ftc.teamcode.GMR.Autonomous.Red;

import com.qualcomm.hardware.kauailabs.NavxMicroNavigationSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.IntegratingGyroscope;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.GMR.Autonomous.States;
import org.firstinspires.ftc.teamcode.GMR.Robot.Robot;
import org.firstinspires.ftc.teamcode.GMR.Robot.SubSystems.AllianceColor;
import org.firstinspires.ftc.teamcode.GMR.Robot.SubSystems.DriveTrain;

import static org.firstinspires.ftc.teamcode.GMR.Autonomous.States.ARMDOWN;
import static org.firstinspires.ftc.teamcode.GMR.Autonomous.States.READ;

/**
 * Created by FTC 4316 on 11/11/2017
 */
@Autonomous(name = "Auto R1", group = "Red")
public class Auto_R1 extends OpMode {

    private Robot robot;

    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftRear;
    DcMotor rightRear;

    NavxMicroNavigationSensor gyroscope;
    IntegratingGyroscope gyro;

    Servo rightArm;
    Servo leftArm;

    ColorSensor colorSensorRight;
    DistanceSensor distanceSensorRight;


    private States state;

    private boolean isFinished;

    private double position;
    private double goalPosition;

    private int turnRadius = 20;
    private double turnPower = 0.15;

    private ElapsedTime time = new ElapsedTime();

    private double currentSeconds;
    private double goalSeconds;

    private int keyColumn;

    private double columnDist;

    @Override
    public void init() {
        rightFront = hardwareMap.dcMotor.get("rightfront");
        leftFront = hardwareMap.dcMotor.get("leftfront");
        rightRear = hardwareMap.dcMotor.get("rightrear");
        leftRear = hardwareMap.dcMotor.get("leftrear");

        rightArm = hardwareMap.get(Servo.class, "rightArm");
        leftArm = hardwareMap.get(Servo.class, "leftArm");

        colorSensorRight = hardwareMap.get(ColorSensor.class, "colorDistanceRight");
        distanceSensorRight = hardwareMap.get(DistanceSensor.class, "colorDistanceRight");

        gyroscope = hardwareMap.get(NavxMicroNavigationSensor.class, "navx");

        robot = new Robot(hardwareMap, telemetry, true);

        goalPosition = 0.5;
        position = 0;
        rightArm.setPosition(position); //vertical start
        leftArm.setPosition(0.85);
        // position


        state = States.TIME;

        isFinished = false;

        keyColumn = 0;
        columnDist = 0;

        //Starts the timer WORKING
        time.reset();

    }
        @Override
        public void loop(){
        currentSeconds = time.seconds();
        robot.blockLift.brake(true);
        telemetry.addData("State:", state);
            switch(state){
                case TIME:
                    state = ARMDOWN;
                    goalSeconds = currentSeconds += 0.5;
                    break;
                case ARMDOWN:
                    //Lowers right arm WORKING
                    rightArm.setPosition(goalPosition);
                    if(currentSeconds >= goalSeconds){
                        state = READ; //READ
                    } break;
                case READ:
                    //Reads the color/distance sensor to determine which ball to knock off WORKING
                    if(colorSensorRight.blue() > colorSensorRight.red()){
                        /*telemetry.addData("Blue:", colorSensorRight.blue());
                        telemetry.addData("Red:", colorSensorRight.red());
                        telemetry.addData("The ball is:", "blue");
                        telemetry.update();*/

                        state = States.LEFTKNOCK;
                    } else if(colorSensorRight.red() > colorSensorRight.blue()){
                        /*telemetry.addData("Blue:", colorSensorRight.blue());
                        telemetry.addData("Red:", colorSensorRight.red());
                        telemetry.addData("The ball is:", "red");
                        telemetry.update();*/

                        state = States.RIGHTKNOCK;
                    } break;
                case LEFTKNOCK:
                    //Knocks the left ball off of the pedestal WORKING
                    if(robot.driveTrain.encoderDrive(DriveTrain.Direction.N, 0.2, 1)){
                        isFinished = false;
                        state = States.LEFTARMUP;
                    } break;

                case RIGHTKNOCK:
                    //Knocks the right ball off of the pedestal WORKING
                    if(robot.driveTrain.gyroTurn(DriveTrain.Direction.TURNRIGHT, turnPower, turnRadius)){
                        isFinished = false;
                        state = States.RIGHTARMUP;
                    } break;

                case LEFTARMUP:
                    //Lifts arm up after knocking left ball WORKING
                    rightArm.setPosition(position);
                    if(currentSeconds >= goalSeconds){
                        state = States.OFFSTONE;
                    } break;

                case RIGHTARMUP:
                    //Lifts arm up after knocking right ball WORKING
                    rightArm.setPosition(position);
                    if(currentSeconds >= goalSeconds){
                        state = States.RIGHTZONE;
                    } break;

                case LEFTZONE:
                    //Returns to original position from knocking left ball WORKING
                    /*if(robot.driveTrain.gyroTurn(DriveTrain.Direction.TURNRIGHT, turnPower, turnRadius)){
                        isFinished = false;
                        state = States.OFFSTONE;

                    }*/ break;
                case RIGHTZONE:
                    //Returns to original position from knocking right ball WORKING

                    if(robot.driveTrain.gyroTurn(DriveTrain.Direction.TURNLEFT, turnPower, turnRadius)){
                        isFinished = false;
                        columnDist = 2;
                        state = States.OFFSTONE;
                    } break;
                case OFFSTONE:
                    if(keyColumn == 1){
                        columnDist += 5.5;
                    } else if(keyColumn == 2){
                        columnDist += 7;
                    } else if(keyColumn == 3){
                        columnDist += 8.5;
                    } else if(keyColumn == 0){
                        columnDist += 7;
                    }

                    if(robot.driveTrain.encoderDrive(DriveTrain.Direction.N, 0.2, columnDist)) {
                        state = States.TURNBOX;
                    }
                    break;
                case TURNBOX:
                    //Turns left to face CryptoBox. UNTESTED
                    if(!isFinished){
                        isFinished = robot.driveTrain.gyroTurn(DriveTrain.Direction.TURNRIGHT, 0.25, 90);
                    } else{
                        isFinished = false;
                        state = States.DRIVEBOX;
                    } break;
                case DRIVEBOX:
                    //Drives into the CryptoBox
                    if(!isFinished){
                        isFinished = robot.driveTrain.encoderDrive(DriveTrain.Direction.N, 0.25, 3);
                    } else{
                        isFinished = false;
                        state = States.DROP;
                        goalSeconds = currentSeconds += 1.0;
                    } break;
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
                        state = States.END;
                    } break;
                case END:
                    robot.driveTrain.stop();
                    break;
            }

        }
}
