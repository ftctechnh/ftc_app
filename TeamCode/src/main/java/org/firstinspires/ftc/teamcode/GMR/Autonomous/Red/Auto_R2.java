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

import org.firstinspires.ftc.teamcode.GMR.Autonomous.States;
import org.firstinspires.ftc.teamcode.GMR.Robot.Robot;
import org.firstinspires.ftc.teamcode.GMR.Robot.SubSystems.AllianceColor;
import org.firstinspires.ftc.teamcode.GMR.Robot.SubSystems.DriveTrain;

import static org.firstinspires.ftc.teamcode.GMR.Autonomous.States.END;

/**
 * Created by FTC 4316 on 11/11/2017
 */
@Autonomous(name = "Auto R2", group = "Red")
public class Auto_R2 extends OpMode {

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

    private boolean isFinished;

    States state;

    private double position;
    private double goalPosition;

    private ElapsedTime time = new ElapsedTime();

    private double currentSeconds;
    private double goalSeconds;

    private int turnRadius = 20;
    private double turnPower = 0.15;

    private float endUltrasonic;

    private float gyroBeforeGlyphPit;

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

        time.reset();

        robot.setRelicTilt();

    }
        @Override
        public void loop(){
        currentSeconds = time.seconds();
            telemetry.addData("State:", state);
            telemetry.update();
            switch(state){
                case TIME:
                    state = States.SCAN;
                    goalSeconds = currentSeconds += 5.5;
                    break;
                case SCAN:
                    keyColumn = robot.vision.keyColumnDetect(AllianceColor.RED);
                    if(keyColumn != 0 || currentSeconds >= goalSeconds){
                        state = States.ARMDOWN;
                        goalSeconds = currentSeconds += 0.5;
                    }
                    break;
                case ARMDOWN:
                    //Lowers right arm WORKING
                    rightArm.setPosition(goalPosition);
                    if(currentSeconds >= goalSeconds){
                        state = States.READ; //READ
                    } break;

                case READ:
                    //Reads the color/distance sensor to determine which ball to knock off WORKING
                    if(colorSensorRight.blue() > colorSensorRight.red()){
                        telemetry.addData("Blue:", colorSensorRight.blue());
                        telemetry.addData("Red:", colorSensorRight.red());
                        telemetry.addData("The ball is:", "blue");
                        telemetry.update();

                        state = States.LEFTKNOCK;
                    } else if(colorSensorRight.red() > colorSensorRight.blue()){
                        telemetry.addData("Blue:", colorSensorRight.blue());
                        telemetry.addData("Red:", colorSensorRight.red());
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
                        goalSeconds = currentSeconds += 1.0;
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
                        state = States.LEFTZONE;
                    } break;

                case RIGHTARMUP:
                    //Lifts arm up after knocking right ball WORKING
                    rightArm.setPosition(position);
                    if(currentSeconds >= goalSeconds){
                        state = States.RIGHTZONE;
                    } break;

                case LEFTZONE:
                    //Returns to original position from knocking left ball WORKING
                    if(robot.driveTrain.gyroTurn(DriveTrain.Direction.TURNRIGHT, turnPower, turnRadius)){
                        isFinished = false;
                        state = States.OFFSTONE;
                        time.reset();
                    } break;
                case RIGHTZONE:
                    //Returns to original position from knocking right ball WORKING

                    if(robot.driveTrain.gyroTurn(DriveTrain.Direction.TURNLEFT, turnPower, turnRadius)){
                        isFinished = false;
                        state = States.OFFSTONE;
                        time.reset();
                    } break;

                case OFFSTONE:
                    if (robot.driveTrain.encoderDrive(DriveTrain.Direction.N, 0.2, 6.5)) {
                        state = States.STRAFE;
                        isFinished = false;
                    }
                    break;
                case STRAFE:
                    //Strafes left to face CryptoBox. UNTESTED/DEACTIVATED
                    if(keyColumn == 1){
                        columnDist = 1.0;
                    } else if (keyColumn == 2){
                        columnDist = 3.5;
                    } else if (keyColumn == 3){
                        columnDist = 6.0;
                    } else if (keyColumn == 0){
                        columnDist = 4.0;
                    }

                    if(!isFinished){
                        isFinished = robot.driveTrain.encoderDrive(DriveTrain.Direction.W, 0.3, columnDist);
                    } else{
                        isFinished = false;
                        state = States.DRIVEBOX;
                    } break;
                case DRIVEBOX:
                    if(!isFinished){
                        isFinished = robot.driveTrain.encoderDrive(DriveTrain.Direction.N, 0.3, 1);
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
                        isFinished = robot.driveTrain.encoderDrive(DriveTrain.Direction.S, 0.3, 1);
                    } else{
                        isFinished = false;
                        state = States.CENTER;
                    } break;
                case CENTER:
                    if(keyColumn == 1){
                        if(!isFinished){
                            isFinished = robot.driveTrain.encoderDrive(DriveTrain.Direction.W, 0.3, 2.5);
                        } else{
                            isFinished = false;
                            state = States.GLYPHPITTURN;
                        }
                    } else if(keyColumn == 3){
                        if(!isFinished){
                            isFinished = robot.driveTrain.encoderDrive(DriveTrain.Direction.E, 0.3, 2.5);
                        } else{
                            isFinished = false;
                            state = States.GLYPHPITTURN;
                        }
                    } else{
                        state = States.GLYPHPITTURN;
                    }
                    break;
                case GLYPHPITTURN:
                    if (!isFinished) {
                        isFinished = robot.driveTrain.gyroTurn(DriveTrain.Direction.TURNLEFT, 0.3, 180);
                    } else{
                        isFinished = false;
                        state = States.GRAB;
                    } break;
                case GRAB:
                    robot.blockLift.grab(false, 1);
                    gyroBeforeGlyphPit = robot.driveTrain.getYaw();
                    state = States.GLYPHPITDRIVE;
                    break;
                case GLYPHPITDRIVE:
                    if(!isFinished){
                        isFinished = robot.driveTrain.encoderDrive(DriveTrain.Direction.N, 0.4, 6.5);
                    } else{
                        isFinished = false;
                        state = States.HOLD;
                        goalSeconds = currentSeconds += 2.5;
                    }break;
                case HOLD:
                    if(currentSeconds >= goalSeconds) {
                        robot.blockLift.grab(false, 0);
                        state = States.STRAIGHTEN;
                    }break;
                case STRAIGHTEN:
                    if (!isFinished) {
                        isFinished = robot.driveTrain.straighten(gyroBeforeGlyphPit);
                    } else {
                        isFinished = false;
                        state = States.CRYPTODRIVE;
                    }break;
                case CRYPTODRIVE:
                    if (!isFinished){
                        isFinished = robot.driveTrain.encoderDrive(DriveTrain.Direction.S, 0.4, 5.5);
                    } else{
                        isFinished = false;
                        state = States.CRYPTOTURN;
                    }break;
                case CRYPTOTURN:
                    if (!isFinished){
                        isFinished = robot.driveTrain.gyroTurn(DriveTrain.Direction.TURNRIGHT, 0.3, 180);
                    } else{
                        isFinished = false;
                        state = States.ALTCOLUMN;
                    }break;
                case ALTCOLUMN:
                    if(keyColumn == 2 || keyColumn == 0){
                        if(!isFinished){
                            isFinished = robot.driveTrain.encoderDrive(DriveTrain.Direction.W, 0.3, 2.5);
                        } else{
                            isFinished = false;
                            state = States.DRIVEBOX2;
                        }
                    } else {
                        isFinished = false;
                        state = States.DRIVEBOX2;
                    }
                    break;
                case DRIVEBOX2:
                    if(!isFinished){
                        isFinished = robot.driveTrain.encoderDrive(DriveTrain.Direction.N, 0.25, 2.0);
                    } else{
                        isFinished = false;
                        state = States.DROP2;
                        goalSeconds = currentSeconds += 1.0;
                    } break;
                case DROP2:
                    robot.blockLift.grab(true, 0);
                    if (currentSeconds >= goalSeconds) {
                        state = States.DRIVEBACK2;
                        robot.blockLift.grab(false, 0);
                    } break;
                case DRIVEBACK2:
                    if(!isFinished){
                        isFinished = robot.driveTrain.encoderDrive(DriveTrain.Direction.S, 0.3, 2.0);
                    } else{
                        isFinished = false;
                        state = States.GLYPHPITTURN2;
                    } break;
                case GLYPHPITTURN2:
                    if (!isFinished) {
                        isFinished = robot.driveTrain.gyroTurn(DriveTrain.Direction.TURNLEFT, 0.3, 180);
                    } else{
                        isFinished = false;
                        state = States.END;
                    } break;
                case END:
                    robot.driveTrain.stop();
                    break;
            }

            telemetry.addData("End Ultrasonic", endUltrasonic);

        }

}
