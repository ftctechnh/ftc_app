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

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.GMR.Robot.Robot;
import org.firstinspires.ftc.teamcode.GMR.Robot.SubSystems.DriveTrain;

/**
 * Created by FTC 4316 on 11/11/2017
 */
@Autonomous(name = "Auto B1", group = "Blue")
public class Auto_B1 extends OpMode {

    private Robot robot;

    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftRear;
    DcMotor rightRear;

    NavxMicroNavigationSensor gyroscope;
    IntegratingGyroscope gyro;

    Servo leftArm;
    Servo rightArm;
    ColorSensor colorSensorLeft;
    DistanceSensor distanceSensorLeft;

    //OpenGLMatrix lastLocation = null;
    //VuforiaLocalizer vuforia;

    //VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
    //VuforiaTrackable relicTemplate = relicTrackables.get(0);
    //RelicRecoveryVuMark vuMark;

    private States state;

    private boolean isFinished;

    private double position;
    private double goalPosition;

    private ElapsedTime time = new ElapsedTime();

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

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        parameters.vuforiaLicenseKey = "AUkgO0T/////AAAAGaYeMjdF+Us8tdP9fJcRhP9239Bwgzo0STjrR4II0s58wT/ja6GlSAQi/ptpHERhBhdNq8MMmlxC6bjyebsGnr/26IxYKhFFdC67Q7HE0jhDrsrEfxfJMFnsk2zSdt5ofwm2Z1xNhdBg2kfFCzdodI7aHFEdUQ6fddoTioTSPu9zzU9XqBr7Ra+5mTaIwp10heZmlXIjWfu8220ef/tZQ8QSmDX1GSqRLBjUJspesff8Nv9pkQAK3Nvp8YFHKJoFNkSV7QJW7mi/liHYq6DxYqhWk977WYGwzhHA003HNV4OUWhTLJGiPsiFhAlcJVbnVMn6ldnsSauT4unjXA9VBIzaYtSJc29UJYmWyin3MxPz";

        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
        //this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        //relicTemplate.setName("relicVuMarkTemplate");

        robot = new Robot(hardwareMap, telemetry);

        goalPosition = 0.35;
        position = 0.85;
        leftArm.setPosition(position); //vertical start
        rightArm.setPosition(0);
        // position

        state = States.TIME;
        isFinished = false;

        //relicTrackables.activate();

    }
        @Override
        public void loop(){
            //telemetry.addData("Pitch:", robot.getPitch());
            //telemetry.update();
            switch(state){
                /*case SCAN:
                    //Scans the pictograph to get correct column
                    vuMark = RelicRecoveryVuMark.from(relicTemplate);
                    if(vuMark != RelicRecoveryVuMark.UNKNOWN){
                        telemetry.addData("Column:", vuMark);
                        telemetry.update();
                        state = States.TIME;
                    } break;*/
                case TIME:
                    //Starts the timer
                    time.reset();
                    state = States.ARMDOWN;
                    break;
                case ARMDOWN:
                    //Lowers left arm
                    leftArm.setPosition(goalPosition);
                    if(time.seconds() >= 1.0){
                        state = States.READ;
                    } break;

                case READ:
                    //Reads the color/distance sensor to determine which ball to knock off
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
                    //Knocks the left ball off of the pedestal
                    if(!isFinished){
                        isFinished = robot.driveTrain.encoderDrive(DriveTrain.Direction.S, 0.25, 1);
                    } else{
                        isFinished = false;
                        state = States.LEFTARMUP;
                        time.reset();
                    } break;

                case RIGHTKNOCK:
                    //Knocks the right ball off of the pedestal
                    if(!isFinished){
                        isFinished = robot.driveTrain.encoderDrive(DriveTrain.Direction.N, 0.25, 1);
                    } else{
                        isFinished = false;
                        state = States.RIGHTARMUP;
                        time.reset();
                    } break;

                case LEFTARMUP:
                    //Lifts arm up after knocking left ball
                    leftArm.setPosition(0.85);
                    if(time.seconds() >= 1){
                        state = States.LEFTZONE;
                    } break;

                case RIGHTARMUP:
                    //Lifts arm up after knocking right ball
                    leftArm.setPosition(0.85);
                    if(time.seconds() >= 1){
                        state = States.RIGHTZONE;
                    } break;

                case LEFTZONE:
                    //Returns to original position from knocking left ball
                    if(!isFinished){
                        isFinished = robot.driveTrain.encoderDrive(DriveTrain.Direction.N, 0.25, 10);
                    } else{
                        isFinished = false;
                        state = States.TURNBOX;
                        time.reset();
                    } break;

                case RIGHTZONE:
                    //Returns to original position from knocking right ball
                    if(!isFinished){
                        isFinished = robot.driveTrain.encoderDrive(DriveTrain.Direction.N, 0.25, 3);
                    } else{
                        isFinished = false;
                        state = States.TURNBOX;
                        time.reset();
                    } break;
                case TURNBOX:
                    //Turns left to face CryptoBox. UNTESTED
                    if(!isFinished){
                        isFinished = robot.driveTrain.gyroTurn(DriveTrain.Direction.TURNLEFT, 0.25, 90);
                    } else{
                        isFinished = false;
                        state = States.DRIVEBOX;
                    } break;

                case DRIVEBOX:
                    if(!isFinished){
                        isFinished = robot.driveTrain.encoderDrive(DriveTrain.Direction.N, 0.25, 1);
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
    DRIVEOFF,
    DRIVEZONE,
    TURNBOX,
    DRIVEBOX,
    END
}