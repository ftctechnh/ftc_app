package org.firstinspires.ftc.teamcode.Dragons1;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.vuforia.CameraDevice;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Created by Stephen Ogden on 10/23/17.
 * FTC 6128 | 7935
 * FRC 1595
 */

// 560 ticks per rotation
// 4 in diameter
// 89 ticks per inches

// This is for 6128

// TODO: Finish me!

@Autonomous(name = "Blue Jewel Auto", group = "Test")
@Disabled

public class BlueJewelAuto extends LinearOpMode {

    public static final String TAG = "Vuforia VuMark Sample";
    private final double SERVOUPPOS = .5;
    private final double SERVODOWNPOS = 0;
    private float hsvValues[] = {0F, 0F, 0F};
    final float values[] = hsvValues;
    private String color = null;
    private String imageLocation = null;
    // StageNumber > -1 means running
    private int stageNumber = 0;
    OpenGLMatrix lastLocation = null;
    private VuforiaLocalizer vuforia;

    @Override
    public void runOpMode() {

        //<editor-fold desc="Initialization">
        telemetry.addData("Status", "Initializing...");
        telemetry.update();

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "AUgZTU3/////AAAAGaQ5yTo6EkZqvsH9Iel0EktQjXWAZUz3q3FPq22sUTrmsYCccs/mjYiflQBH2u7lofbTxe4BxTca9o2EOnNwA8dLGa/yL3cUgDGjeRfXuwZUCpIG6OEKhiPU5ntOpT2Nr5uVkT3vs2uRr7J6G7YoaGHLw2i1wGncRaw37rZyO03QRh0ZatdKIiK1ItuvJkP3qfUJwQwcpROwa+ZdDNQDbpU6WTL+kPZpnkgR8oLcu+Na1lWrbJ2ZTYG8eUjoIGowbVVGJgORHJazy6/7MbYH268h9ZC4vZ12ItyDK/GlPRTeQWdcZRlWfzAAFwNrjmdjWv9hMuOMoWxo2Y2Rw1Fwii4ohLyRmcQa/wAWY+AOEL14";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);
        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate");
        RelicRecoveryVuMark vuMark;

        ColorSensor colorSensor = hardwareMap.colorSensor.get("color");
        colorSensor.enableLed(false);

        Servo servo = hardwareMap.servo.get("servo");
        servo.setPosition(SERVOUPPOS);

        DcMotor left = hardwareMap.dcMotor.get("lf");
        left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        DcMotor right = hardwareMap.dcMotor.get("rf");
        right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // TODO: Note: I am temporarily disabling directions for the motors, becasue theyre running with encoders
        //left.setDirection(DcMotorSimple.Direction.FORWARD);
        //right.setDirection(DcMotorSimple.Direction.REVERSE);

        telemetry.addData("Status", "Done! Press play to start");
        telemetry.update();
        waitForStart();
        relicTrackables.activate();
        //</editor-fold>


        while (opModeIsActive()) {

            //<editor-fold desc="Move down servo">
            if (stageNumber == 0) {
                servo.setPosition(SERVODOWNPOS);
                colorSensor.enableLed(false);
                CameraDevice.getInstance().setFlashTorchMode(false);
                vuMark = RelicRecoveryVuMark.from(relicTemplate);
                stageNumber++;
            }
            if (stageNumber == 1) {
                if (servo.getPosition() == SERVODOWNPOS) {
                    stageNumber++;
                }
            }
            //</editor-fold>

            //<editor-fold desc="Detect Jewel Color">
            if (stageNumber == 2 || stageNumber == 3 || stageNumber == 4) {
                colorSensor.enableLed(true);
                if (colorSensor.red() > colorSensor.blue() && colorSensor.green() < colorSensor.red()) {
                    color = "Red";
                    stageNumber++;
                } else if (colorSensor.red() < colorSensor.blue() && colorSensor.green() < colorSensor.blue()) {
                    color = "Blue";
                    stageNumber++;
                } else {
                    color = "Unknown :(";
                }
            }
            //</editor-fold>

            //<editor-fold desc="Drive based on Jewel Color">
            if (stageNumber == 5) {
                switch (color) {
                    case "Red": {
                        driveToPostion(left, -1, .3);
                        driveToPostion(right, 1, .3);
                        stageNumber++;
                    }
                    case "Blue": {
                        driveToPostion(left, 1, .3);
                        driveToPostion(right, -1, .3);
                        stageNumber++;
                    }
                }
            }

            if (stageNumber == 6) {
                if (isThere(left, 2000) || isThere(right, 2000)) {
                    servo.setPosition(SERVOUPPOS);
                    stageNumber++;
                }

            }
            //</editor-fold>

            //<editor-fold desc="Drive back to position">
            if (stageNumber == 7) {
                switch (color) {
                    case "Red": {
                        driveToPostion(left, 1, .3);
                        driveToPostion(right, -1, .3);
                        stageNumber++;
                    }
                    case "Blue": {
                        driveToPostion(left, -1, .3);
                        driveToPostion(right, 1, .3);
                        stageNumber++;
                    }
                }
            }
            if (stageNumber == 8) {
                if (isThere(left, 2000) || isThere(right, 2000)) {
                    left.setPower(0);
                    right.setPower(0);
                    stageNumber++;
                }
            }
            //</editor-fold>

            // Scan image, if all else fails, go center
            if (stageNumber == 9) {
                vuMark = RelicRecoveryVuMark.from(relicTemplate);
                switch (vuMark) {
                    case LEFT: {
                        imageLocation = "Left";
                        stageNumber++;
                    }
                    case RIGHT: {
                        imageLocation = "Right";
                        stageNumber++;
                    }
                    case CENTER: {
                        imageLocation = "Center";
                        stageNumber++;
                    }
                    case UNKNOWN: {
                        imageLocation = "Center"; // Go to center if unknown
                        stageNumber++;
                    }
                }
            }

            if (stageNumber == 10) {
                // Drive 36 In to get before key thing
                driveToPostion(left, -36, .5);
                driveToPostion(right, 36, .5);
                stageNumber++;
            }
            if (stageNumber == 11) {
                if (isThere(left, 2000) || isThere(right, 2000)) {
                    left.setPower(0);
                    right.setPower(0);
                    stageNumber++;
                }
            }

            if (stageNumber == 12) {
                // Turn 90 degrees clockwise
                driveToPostion(left, -4, .5);
                driveToPostion(right, 4, .5);
                stageNumber++;
            }
            if (stageNumber == 13) {
                if (isThere(left, 2000) || isThere(right, 2000)) {
                    left.setPower(0);
                    right.setPower(0);
                    stageNumber++;
                }
            }

            // Drive depending on image

            // Mark as done
            if (stageNumber == 14) {
                left.setPower(0);
                right.setPower(0);
                stageNumber = -1;
            }

            //<editor-fold desc="Telemetry and Stop">
            if (stageNumber >= 0) {
                telemetry.addData("Stage number", stageNumber)
                        .addData("Color", color)
                        .addData("Image Location", imageLocation)
                        .addData("", "")
                        .addData("Red value", colorSensor.red())
                        .addData("Blue value", colorSensor.blue())
                        .addData("", "").addData("lefFront pos", left.getCurrentPosition())
                        .addData("left target", left.getTargetPosition())
                        .addData("left ∆", Math.abs(left.getTargetPosition() - left.getCurrentPosition()))
                        .addData("", "").addData("right pos", right.getCurrentPosition())
                        .addData("right target", right.getTargetPosition())
                        .addData("right ∆", Math.abs(right.getTargetPosition() - right.getCurrentPosition()));
                telemetry.update();
            } else {
                stop();
            }
            //</editor-fold>

            idle();
        }
        telemetry.addData("Status", "Done!");
        telemetry.update();
    }

    String format(OpenGLMatrix transformationMatrix) {
        return (transformationMatrix != null) ? transformationMatrix.formatAsTransform() : "null";
    }

    private static void driveToPostion(DcMotor motor, int position, double power) {
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setTargetPosition(position * 25810);
        motor.setPower(power);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    private static boolean isThere(DcMotor motor, int discrepancy) {
        int curentPos = motor.getCurrentPosition();
        int targetPos = motor.getTargetPosition();
        return Math.abs((targetPos - curentPos)) <= discrepancy;
    }
}
