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
import org.firstinspires.ftc.teamcode.GMR.Robot.Robot;
import org.firstinspires.ftc.teamcode.GMR.Robot.SubSystems.DriveTrain;

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

    OpenGLMatrix lastLocation = null;
    VuforiaLocalizer vuforia;

    VuforiaTrackable relicTemplate;
    VuforiaTrackables relicTrackables;


    private ElapsedTime time = new ElapsedTime();

    private double currentSeconds;
    private double goalSeconds;

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

        robot = new Robot(hardwareMap, telemetry);

        goalPosition = 0.5;
        position = 0;
        rightArm.setPosition(position); //vertical start
        leftArm.setPosition(0.85);
        // position

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        parameters.vuforiaLicenseKey = "AUkgO0T/////AAAAGaYeMjdF+Us8tdP9fJcRhP9239Bwgzo0STjrR4II0s58wT/ja6GlSAQi/ptpHERhBhdNq8MMmlxC6bjyebsGnr/26IxYKhFFdC67Q7HE0jhDrsrEfxfJMFnsk2zSdt5ofwm2Z1xNhdBg2kfFCzdodI7aHFEdUQ6fddoTioTSPu9zzU9XqBr7Ra+5mTaIwp10heZmlXIjWfu8220ef/tZQ8QSmDX1GSqRLBjUJspesff8Nv9pkQAK3Nvp8YFHKJoFNkSV7QJW7mi/liHYq6DxYqhWk977WYGwzhHA003HNV4OUWhTLJGiPsiFhAlcJVbnVMn6ldnsSauT4unjXA9VBIzaYtSJc29UJYmWyin3MxPz";

        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate");

        state = States.SCAN;
        isFinished = false;

        //Starts the timer WORKING
        time.reset();

        relicTrackables.activate();

    }
        @Override
        public void loop(){
        currentSeconds = time.seconds();
            switch(state){
                case SCAN:
                    //Scans the pictograph to get correct column
                    RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
                    if(vuMark != RelicRecoveryVuMark.UNKNOWN) {
                        telemetry.addData("Column:", vuMark);
                        telemetry.update();
                        state = States.TIME;
                    } break;
                case TIME:
                    state = States.GRAB;
                    robot.blockLift.clamp(false,true, true, false);
                    break;
                case GRAB:
                    robot.blockLift.clamp(false,false, false, true);
                    state = States.LIFT;
                    goalSeconds = currentSeconds + 5;
                    break;
                case LIFT:
                    if (currentSeconds >= goalSeconds) {
                        robot.blockLift.setLift(400);
                        state = States.ARMDOWN;
                        goalSeconds = currentSeconds += 1.0;
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
                    } break;
                case LEFTKNOCK:
                    //Knocks the left ball off of the pedestal WORKING
                    if(!isFinished){
                        isFinished = robot.driveTrain.encoderDrive(DriveTrain.Direction.N, 0.25, 1);
                    } else{
                        isFinished = false;
                        state = States.LEFTARMUP;
                        time.reset();
                    } break;
                case RIGHTKNOCK:
                    //Knocks the right ball off of the pedestal WORKING
                    if(!isFinished){
                        isFinished = robot.driveTrain.encoderDrive(DriveTrain.Direction.S, 0.25, 0.5);
                    } else{
                        isFinished = false;
                        state = States.RIGHTARMUP;
                        time.reset();
                    } break;
                case LEFTARMUP:
                    //Lifts arm up after knocking left ball WORKING
                    rightArm.setPosition(position);
                    if(time.seconds() >= 1){
                        state = States.LEFTZONE;
                    } break;
                case RIGHTARMUP:
                    //Lifts arm up after knocking right ball WORKING
                    rightArm.setPosition(position);
                    if(time.seconds() >= 1){
                        state = States.RIGHTZONE;
                    } break;
                case LEFTZONE:
                    //Returns to original position from knocking left ball WORKING
                    if(!isFinished){
                        isFinished = robot.driveTrain.encoderDrive(DriveTrain.Direction.N, 0.4, 7);
                    } else{
                        isFinished = false;
                        state = States.TURNBOX;
                        time.reset();
                    } break;
                case RIGHTZONE:
                    //Returns to original position from knocking right ball WORKING
                    if(!isFinished){
                        isFinished = robot.driveTrain.encoderDrive(DriveTrain.Direction.N, 0.4, 12);
                    } else{
                        isFinished = false;
                        state = States.TURNBOX;
                        time.reset();
                    } break;
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

enum States {
    SCAN,
    TIME,
    ARMDOWN,
    READ,
    LEFTKNOCK,
    RIGHTKNOCK,
    LEFTARMUP,
    RIGHTARMUP,
    LEFTZONE,
    RIGHTZONE,
    TURNBOX,
    DRIVEBOX,
    DRIVEBACK,
    END,
    GRAB,
    DROP,
    LIFT
}

