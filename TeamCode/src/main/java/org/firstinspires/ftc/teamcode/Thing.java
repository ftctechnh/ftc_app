package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

@Autonomous(name="FJSKLADFSDKLJFKLSDGHZSDJKLG bgk;hasd;j", group="Linear Auto")

public class Thing extends LinearOpMode {
    /*VuforiaLocalizer vuforia;
    RelicRecoveryVuMark vuMark;
    OpenGLMatrix pose;*/
    VuforiaPlagarism vu = new VuforiaPlagarism();

    public enum MoveType {
        STRAIGHT, LATERALLY, ROT
    }

    final static double PULSES_PER_INCH = (1120 / (4 * Math.PI));
    final static double ADJ_CONST = PULSES_PER_INCH * 2.2;
    private ElapsedTime runtime = new ElapsedTime();
    Hardware750 robot = new Hardware750();

    @Override
    public void runOpMode() {

        robot.init(hardwareMap);
        waitForStart();
        telemetry.addData("skatin fast,", "eatin' ass");
        initVuforia();
        while (true) {
            if (vu.getVuf() == VuforiaPlagarism.type.CENTER) {
                encode(10, 0.5, MoveType.STRAIGHT);
            }
        }
    }

    //Negative speed means:
    //Counterclockwise for MoveType.ROT
    //Left for MoveType.LATERALLY
    //Backwards for MoveType.STRAIGHT
    public void encode(double distance, double speed, MoveType move) {
        int multFL = 1;
        int multFR = 1;
        int multRL = 1;
        int multRR = 1;

        int targetFL;
        int targetFR;
        int targetRL;
        int targetRR;

        if (move == MoveType.ROT) {
            if (speed > 0) {
                multFR *= -1;
                multRR *= -1;
            } else {
                multFL *= -1;
                multRL *= -1;
            }
        } else if (move == MoveType.LATERALLY) {
            if (speed > 0) {
                multFR *= -1;
                multRL *= -1;
            } else {
                multFL *= -1;
                multRR *= -1;
            }
        } else if (move == MoveType.STRAIGHT) {
            if (speed < 0) {
                multFL *= -1;
                multFR *= -1;
                multRL *= -1;
                multRR *= -1;

            }
        }

        robot.rlDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rrDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.flDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        if(opModeIsActive()) {
            robot.flDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.frDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rlDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rrDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            targetFL = multFL * (robot.flDrive.getCurrentPosition() + (int) (distance * PULSES_PER_INCH));
            targetFR = multFR * (robot.frDrive.getCurrentPosition() + (int) (distance * PULSES_PER_INCH));
            targetRL = multRL * (robot.rlDrive.getCurrentPosition() + (int) (distance * PULSES_PER_INCH));
            targetRR = multRR * (robot.rrDrive.getCurrentPosition() + (int) (distance * PULSES_PER_INCH));

            robot.flDrive.setTargetPosition(targetFL);
            robot.frDrive.setTargetPosition(targetFR);
            robot.rlDrive.setTargetPosition(targetRL);
            robot.rrDrive.setTargetPosition(targetRR);

            runtime.reset();
            robot.flDrive.setPower(speed * multFL);
            robot.frDrive.setPower(speed * multFR);
            robot.rlDrive.setPower(speed * multRL);
            robot.rrDrive.setPower(speed * multRR);

            while (opModeIsActive() && (robot.flDrive.isBusy() && robot.frDrive.isBusy() && robot.rlDrive.isBusy() && robot.rrDrive.isBusy())) {
                int i = 0;
                telemetry.addData("Current fl: ", robot.flDrive.getCurrentPosition());
                telemetry.addData("Current fr: ", robot.frDrive.getCurrentPosition());
                telemetry.addData("Current rl: ", robot.rlDrive.getCurrentPosition());
                telemetry.addData("Current rr: ", robot.rrDrive.getCurrentPosition());
                telemetry.addData("fl: ", targetFL);
                telemetry.addData("fr: ", targetFR);
                telemetry.addData("rl: ", targetRL);
                telemetry.addData("rr: ", targetRR);
                telemetry.addData("cool number: ", i);
                i++;
                telemetry.update();
            }
            robot.setAllDriveMotors(0);
            robot.flDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.frDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rlDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rrDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }
    public void wait(int t) {
        try {
            Thread.sleep(t);
        } catch (Exception e) {

        }
    }
    public void initVuforia() {
        VuforiaLocalizer vuforia;
        RelicRecoveryVuMark vuMark;
        OpenGLMatrix pose;
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        parameters.vuforiaLicenseKey = "AbQfkoj/////AAAAGURTD1LwoUjKk6qgxygb/6QTHah6F5/HMfF99SDO7C7wnhjBctp6i+bm/mX4El1OTHR8wW0gGjoM4qNsfM3cgFiMDHE4/IBhgpc2siB6nwrgEVZbo3PwJ0xImdXvTSEfWn8Fc6g+svSUFb97VAyjVAEsOvMC+sSqpjIKEQLoCdbCpLRmnX+9socxkX5qix9OVb0xREGbTtddp2fwtLleMXMHxUwhsTc3q7vqD5LDK7Q8GxOaV9jyB6/3Y3T65qaWOGjlGo39Ts394+WTp4hqwqvuu0Gkztlk2e6IeJbN9sN1+8xb2XQllnrHeBhIXxaoES1MRkyjMHliwQxbRJv8kwPeY9q/AsOA/dUy1x87iZLp";

        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        vuforia = ClassFactory.createVuforiaLocalizer(parameters);


        VuforiaTrackables relicTrackables = vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate");
        vuMark = RelicRecoveryVuMark.from(relicTemplate);
        pose = ((VuforiaTrackableDefaultListener)relicTemplate.getListener()).getPose();

        while (opModeIsActive()) {
            //telemetry.addData("sadf", vuMark);
            //telemetry.update();
            if (vuMark == vuMark.CENTER) {
                encode(10, 0.5, MoveType.STRAIGHT);
                break;
            }

            //telemetry.addData("slkdjf", "asldkfjsld");
                VectorF trans = pose.getTranslation();
                Orientation rot = Orientation.getOrientation(pose, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);

                // Extract the X, Y, and Z components of the offset of the target relative to the robot
                double tX = trans.get(0);
                double tY = trans.get(1);
                double tZ = trans.get(2);

                // Extract the rotational components of the target relative to the robot
                double rX = rot.firstAngle;
                double rY = rot.secondAngle;
                double rZ = rot.thirdAngle;

                telemetry.addData("tX", tX);
                telemetry.addData("tY", tY);
                telemetry.addData("tZ", tZ);
                telemetry.addData("rX", rX);
                telemetry.addData("rY", rY);
                telemetry.addData("rZ", rZ);
        }
    }
}