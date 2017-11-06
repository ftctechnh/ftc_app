package org.firstinspires.ftc.teamcode.Dragons1;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.vuforia.CameraDevice;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Created by Stephen Ogden on 9/23/17.
 * FTC 6128 | 7935
 * FRC 1595
 */

// 560 ticks per rotation
// 4 in diameter
// 89 ticks per inches

@Autonomous(name = "Auto Concept", group = "Test")
@Disabled

public class AutoConcept extends LinearOpMode {

    public static final String TAG = "Vuforia VuMark Sample";

    float hsvValues[] = {0F,0F,0F};

    final float values[] = hsvValues;


    // StageNumber > -1 means running
    short stageNumber = 0;

    OpenGLMatrix lastLocation = null;

    VuforiaLocalizer vuforia;

    ColorSensor colorSensor;

    Servo servo = null;

    DcMotor leftFront = null;
    DcMotor leftBack = null;
    DcMotor rightFront = null;
    DcMotor rightBack = null;


    @Override
    public void runOpMode() {

        telemetry.addData("Status", "Initalizing...");

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "AUgZTU3/////AAAAGaQ5yTo6EkZqvsH9Iel0EktQjXWAZUz3q3FPq22sUTrmsYCccs/mjYiflQBH2u7lofbTxe4BxTca9o2EOnNwA8dLGa/yL3cUgDGjeRfXuwZUCpIG6OEKhiPU5ntOpT2Nr5uVkT3vs2uRr7J6G7YoaGHLw2i1wGncRaw37rZyO03QRh0ZatdKIiK1ItuvJkP3qfUJwQwcpROwa+ZdDNQDbpU6WTL+kPZpnkgR8oLcu+Na1lWrbJ2ZTYG8eUjoIGowbVVGJgORHJazy6/7MbYH268h9ZC4vZ12ItyDK/GlPRTeQWdcZRlWfzAAFwNrjmdjWv9hMuOMoWxo2Y2Rw1Fwii4ohLyRmcQa/wAWY+AOEL14";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate");

        colorSensor = hardwareMap.colorSensor.get("color");

        servo = hardwareMap.servo.get("servo");

        leftFront = hardwareMap.dcMotor.get("lf");
        leftBack = hardwareMap.dcMotor.get("lb");
        rightFront = hardwareMap.dcMotor.get("rf");
        rightBack = hardwareMap.dcMotor.get("rb");

        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftFront.setDirection(DcMotorSimple.Direction.FORWARD);
        leftBack.setDirection(DcMotorSimple.Direction.FORWARD);
        rightFront.setDirection(DcMotorSimple.Direction.FORWARD);
        rightBack.setDirection(DcMotorSimple.Direction.FORWARD);

        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("Status", "Done! Press play to start" );
        telemetry.update();

        waitForStart();

        relicTrackables.activate();

        while(opModeIsActive()) {

            // 0. Start the servo at mid pos for now, and initalize camera

            if (stageNumber == 0) {
                servo.setPosition(.5);

                colorSensor.enableLed(true);

                CameraDevice.getInstance().setFlashTorchMode(false);
                RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);


                stageNumber++;
            }

            // 1. Drop servo arm for color sensor




            // Last: Update telemetry

            if (stageNumber >= 0) {

                telemetry.addData("Red value", colorSensor.red());
                telemetry.addData("Green value", colorSensor.green());
                telemetry.addData("Blue value", colorSensor.blue());

                telemetry.addData("lefFront pos", leftFront.getCurrentPosition());
                telemetry.addData("leftFront target", leftFront.getTargetPosition());
                telemetry.addData("leftFront ∆", Math.abs(leftFront.getTargetPosition() - leftFront.getCurrentPosition()));

                telemetry.addData("rightFront pos", rightFront.getCurrentPosition());
                telemetry.addData("rightFront target", rightFront.getTargetPosition());
                telemetry.addData("rightFront ∆", Math.abs(rightFront.getTargetPosition() - rightFront.getCurrentPosition()));

                telemetry.addData("lefBack pos", leftBack.getCurrentPosition());
                telemetry.addData("leftBack target", leftBack.getTargetPosition());
                telemetry.addData("leftBack ∆", Math.abs(leftBack.getTargetPosition() - leftBack.getCurrentPosition()));

                telemetry.addData("rightBack pos", rightBack.getCurrentPosition());
                telemetry.addData("rightBack target", rightBack.getTargetPosition());
                telemetry.addData("rightBack ∆", Math.abs(rightBack.getTargetPosition() - rightBack.getCurrentPosition()));

                telemetry.update();
            } else {
                stop();
            }


        }
        telemetry.addData("Status", "Done!");
        telemetry.update();
    }
    String format(OpenGLMatrix transformationMatrix) {
        return (transformationMatrix != null) ? transformationMatrix.formatAsTransform() : "null";
    }
    /*
    public static void driveToPostion(DcMotor motor, int position) {
    // TODO: Finish this function
    }
    */
}
