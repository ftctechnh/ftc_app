package org.firstinspires.ftc.teamcode.relicrecoveryv2;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IntegratingGyroscope;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.internal.opmode.TelemetryImpl;

import java.util.Random;

/**
 * Created by TPR on 12/14/17.
 */
@TeleOp(name="RelicTelyOp",group="Jeff" )
public class RelicTelyMode  extends MeccyMode{
    //
    //PengwinFin pengwinFin;
    PengwinWing pengwinWing;
    //
    //<editor-fold desc="Startify">
    VuforiaLocalizer vuforia;
    OpenGLMatrix lastLocation = null;
    //
    ElapsedTime time = new ElapsedTime();
    //
    private Random randy = new Random();
    //
    double degreeOfRobotPower = 1;
    DrivingAction drivingAction = DrivingAction.Driving;
    //
    boolean powered = false;
    //
    //<editor-fold desc="Controls"
    //joysticks
    double leftX;//j*
    double leftY;
    double rightX;//*j
    double up;//m
    double extend;//m
    //triggers
    boolean halfPower;//j
    boolean quarterPower;//j
    boolean left;//m*
    boolean right;
    boolean grabberPosition = false;//true is closed
    boolean armPosition = true;//true is up  //*m
    //
    double switchify = 1;
    //</editor-fold>
    //
    //</editor-fold>
    //
    public void runOpMode() {
        //<editor-fold desc="Initialize">
        //<editor-fold desc="Vuforia">
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        //
        parameters.vuforiaLicenseKey = "AbxR5+T/////AAAAGR1YlvU/6EDzrJvG5EfPnXSFutoBr1aCusr0K3pKqPuWTBQsUb0mv5irjoX2Xf/GFvAvHyw8v1GBYgHwE+hNTcNj05kw3juX+Ur4l3HNnp5SfXV/8fave0xB7yVYZ/LBDraNnYXiuT+D/5iGfQ99PVVao3LI4uGUOvL9+3vbPqtTXLowqFJX5uE7R/W4iLmNqHgTCSzWcm/J1CzwWuOPD252FDE9lutdDVRri17DBX0C/D4mt6BdI5CpxhG6ZR0tm6Zh2uvljnCK6N42V5x/kXd+UrBgyP43CBAACQqgP6MEvQylUD58U4PeTUWe9Q4o6Xrx9QEwlr8v+pmi9nevKnmE2CrPPwQePkDUqradHHnU";
        //
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;//set camera (front)
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);
        //
        //Load ciphers:
        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate");
        //</editor-fold>
        //
        //<editor-fold desc="Other">
        //pengwinFin = new PengwinFin(hardwareMap);
        pengwinWing = new PengwinWing(hardwareMap);
        leftBackMotor = hardwareMap.dcMotor.get("lback"); //left back
        rightBackMotor = hardwareMap.dcMotor.get("rback"); //right back
        leftFrontMotor = hardwareMap.dcMotor.get("lfront"); //left front
        rightFrontMotor = hardwareMap.dcMotor.get("rfront"); //right front
        configureMotors();
        //
        BNO055IMU imu;
        Orientation angles;
        Acceleration gravity;
        //</editor-fold>
        //</editor-fold>
        //
        waitForStartify();
        //
        //<editor-fold desc="Reset">
        telemetry.log().clear();
        relicTrackables.activate();
        time.reset();
        //</editor-fold>
        //
        while (opModeIsActive()) {
            //<editor-fold desc="Update">
            //Joey's controller
            leftX = gamepad1.left_stick_x;
            leftY = gamepad1.left_stick_y;
            rightX = gamepad1.right_stick_x;
            halfPower = gamepad1.right_bumper;
            quarterPower = gamepad1.left_bumper;
            //Meg's Controller
            left = !(gamepad2.left_trigger == 0);
            right = !(gamepad2.right_trigger == 0);
            up = gamepad2.left_stick_y;
            extend = gamepad2.right_stick_y;
            //
            RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
            //</editor-fold>
            //
            setDegreePower();
            //
            switchs();
            //
            moveTheRobot();
            //
            telemetryJazz(vuMark);
            //
        }
    }
    //
    //<editor-fold desc="Functions">
    private void setDegreePower() {
        if (halfPower) {
            degreeOfRobotPower = 0.5;
        }
        else if(quarterPower){
            degreeOfRobotPower = 0.25;
        }else{
            degreeOfRobotPower = 1.0;
        }
    }
    //
    private void switchs(){
        if (!(rightX == 0)){
            drivingAction = DrivingAction.Turning;
        }else if(Math.abs(leftX) > .15){
            drivingAction = DrivingAction.Strafing;
        }else{
            drivingAction = DrivingAction.Driving;
        }
        //
        if(gamepad2.dpad_right){
            grabberPosition = true;
        }else{
            grabberPosition = false;
        }
    }
    //
    private void moveTheRobot() {
        //<editor-fold desc="Chassy">
        switch (drivingAction){
            case Driving:
                    drive(-leftY * degreeOfRobotPower);
                break;
            case Turning:
                    turn(rightX * degreeOfRobotPower);
                break;
            case Strafing:
                    strafe(leftX * degreeOfRobotPower, -leftY * degreeOfRobotPower);
                break;
        }
        //</editor-fold>
        //
        //<editor-fold desc="Grabber">
        if (left && right || grabberPosition){
            pengwinWing.setServos(true, true);
            if (left || right){grabberPosition = false;}
        }else if(left){
            pengwinWing.setServos(true, false);
        }else if(right){
            pengwinWing.setServos(false, true);
        }else {
            pengwinWing.setServos(false, false);
        }
        //</editor-fold>
        //
        //<editor-fold desc="Arm">
        if (gamepad2.y){
            pengwinWing.raiseArm();
        }else if(gamepad2.x){
            pengwinWing.lowerArm();
        }
        //
        pengwinWing.extendArm(gamepad2.right_stick_y);
        //
        if (!(gamepad2.left_stick_y == 0) || powered){
            pengwinWing.manualArm(gamepad2.left_stick_y);
            if (gamepad2.left_stick_y == 0){
                powered = false;
            }else {
                powered = true;
            }
        }
        //</editor-fold>
    }
    //
    private void telemetryJazz(RelicRecoveryVuMark vuMark) {
        telemetry.addData("Unicorn Crossing", time.milliseconds());
        telemetry.addData("Motor", leftBackMotor.getCurrentPosition());
        if (vuMark != RelicRecoveryVuMark.UNKNOWN) {
            //
            telemetry.addData("VuMark", vuMark);
            //
        }else {
            telemetry.addData("VuMark", "None");
        }
        telemetry.update();
    }
    //</editor-fold>
}
/*List
1. Gyro Turn
2. Encoder Telemetry
3. Arm Configuration
4. Arm Set Position
5. Joystick Manual Position
6. Limit Switch Allowance
 */