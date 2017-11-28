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
@Autonomous(name = "Auto B2", group = "Blue")
public class Auto_B2 extends OpMode {

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

    private BlueStates state;

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

        state = BlueStates.TIME;
        isFinished = false;

        //relicTrackables.activate();

    }
        @Override
        public void loop(){
            currentSeconds = time.seconds();
            //telemetry.addData("Pitch:", robot.getPitch());
            //telemetry.update();
            switch(state){
                /*case SCAN:
                    //Scans the pictograph to get correct column
                    vuMark = RelicRecoveryVuMark.from(relicTemplate);
                    if(vuMark != RelicRecoveryVuMark.UNKNOWN){
                        telemetry.addData("Column:", vuMark);
                        telemetry.update();
                        state = BlueStates.TIME;
                    } break;*/
                case TIME:
                    state = BlueStates.GRAB;
                    robot.blockLift.clamp(false,true, true, false);
                    break;
                case GRAB:
                    robot.blockLift.clamp(false,false, false, true);
                    state = BlueStates.LIFT;
                    goalSeconds = currentSeconds + 0.4;
                case LIFT:
                    if (currentSeconds >= goalSeconds) {
                        robot.blockLift.setLift(400);
                        state = BlueStates.ARMDOWN;
                        goalSeconds = currentSeconds += 2.0;
                    }
                case ARMDOWN:
                    //Lowers right arm WORKING
                    leftArm.setPosition(goalPosition);
                    if(currentSeconds >= goalSeconds){
                        state = BlueStates.READ; //READ
                    } break;

                case READ:
                    //Reads the color/distance sensor to determine which ball to knock off WORKING
                    if(colorSensorLeft.blue() > colorSensorLeft.red()){
                        telemetry.addData("Blue:", colorSensorLeft.blue());
                        telemetry.addData("Red:", colorSensorLeft.red());
                        telemetry.addData("The ball is:", "blue");
                        telemetry.update();

                        state = BlueStates.LEFTKNOCK;
                    } else if(colorSensorLeft.red() > colorSensorLeft.blue()){
                        telemetry.addData("Blue:", colorSensorLeft.blue());
                        telemetry.addData("Red:", colorSensorLeft.red());
                        telemetry.addData("The ball is:", "red");
                        telemetry.update();

                        state = BlueStates.RIGHTKNOCK;
                    } break;

                case LEFTKNOCK:
                    //Knocks the left ball off of the pedestal WORKING
                    if(!isFinished){
                        isFinished = robot.driveTrain.encoderDrive(DriveTrain.Direction.S, 0.25, 0.5);
                    } else{
                        isFinished = false;
                        state = BlueStates.LEFTARMUP;
                        time.reset();
                    } break;

                case RIGHTKNOCK:
                    //Knocks the right ball off of the pedestal WORKING
                    if(!isFinished){
                        isFinished = robot.driveTrain.encoderDrive(DriveTrain.Direction.N, 0.25, 1);
                    } else{
                        isFinished = false;
                        state = BlueStates.RIGHTARMUP;
                        time.reset();
                    } break;

                case LEFTARMUP:
                    //Lifts arm up after knocking left ball WORKING
                    leftArm.setPosition(0.85);
                    if(time.seconds() >= 1){
                        state = BlueStates.LEFTZONE;
                    } break;

                case RIGHTARMUP:
                    //Lifts arm up after knocking right ball WORKING
                    leftArm.setPosition(0.85);
                    if(time.seconds() >= 1){
                        state = BlueStates.RIGHTZONE;
                    } break;

                case LEFTZONE:
                    //Returns to original position from knocking left ball WORKING
                    if(!isFinished){
                        isFinished = robot.driveTrain.encoderDrive(DriveTrain.Direction.N, 0.3, 8);
                    } else{
                        isFinished = false;
                        state = BlueStates.STRAFE;
                        time.reset();
                    } break;

                case RIGHTZONE:
                    //Returns to original position from knocking right ball WORKING
                    if(!isFinished){
                        isFinished = robot.driveTrain.encoderDrive(DriveTrain.Direction.N, 0.3, 2);
                    } else{
                        isFinished = false;
                        state = BlueStates.STRAFE;
                        time.reset();
                    } break;

                /*case DRIVEOFF:
                    //Drives until robot is off balance stone. UNTESTED
                    robot.robot(DriveTrain.Direction.N, 0.25);
                    if(robot.getPitch() >= -1 && robot.getPitch() <= 1 && time.seconds() >= 0.1){
                        robot.stop();
                        state = States.END; //DRIVEZONE
                    } break;*/

                /*case DRIVEZONE:
                    //Drives into the parking zone. UNTESTED/DEACTIVATED
                    if(!isFinished){
                        isFinished = robot.encoderDrive(DriveTrain.Direction.S, 0.25, 15);
                    } else{
                        isFinished = false;
                        state = States.END; //TURNBOX
                    } break;*/

                case STRAFE:
                    //Turns left to face CryptoBox. WORKING
                    if(!isFinished){
                        isFinished = robot.driveTrain.encoderDrive(DriveTrain.Direction.E, 0.3, 2);
                    } else{
                        isFinished = false;
                        state = BlueStates.DRIVEBOX;
                    } break;

                case DRIVEBOX:
                    //Drives into CryptoBox
                    if(!isFinished){
                        isFinished = robot.driveTrain.encoderDrive(DriveTrain.Direction.N, 0.3, 2);
                    } else{
                        isFinished = false;
                        state = BlueStates.DROP;
                    } break;

                case DROP:
                    robot.blockLift.clamp(false, false,true, false);
                    state = BlueStates.DRIVEBACK;
                    break;
                case DRIVEBACK:
                    if(!isFinished){
                        isFinished = robot.driveTrain.encoderDrive(DriveTrain.Direction.S, 0.3, 1.5);
                    } else{
                        isFinished = false;
                        state = BlueStates.END;
                    } break;
                case END:
                    robot.driveTrain.stop();
                    break;
            }

        }

}

enum BlueStates {
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
    STRAFE,
    DRIVEBOX,
    DRIVEBACK,
    END,

    GRAB,
    LIFT,
    DROP
}