package org.firstinspires.ftc.teamcode.seasons.relicrecovery;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuMarkInstanceId;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.mechanism.impl.VisionHelper;


/**
 * Created by Owner on 12/5/2017.
 */
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "TestAuto", group = "autonomous")
public class Autonomous extends LinearOpMode {

    private RelicRecoveryRobot robot;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new RelicRecoveryRobot(this);

        //detect color of stone
        boolean isStoneRed = true;
        boolean isStoneRight = false;

//        if(robot.balancingStoneSensor.red() > 0){
//            isStoneRed = true;
//            telemetry.addData(">", "Red stone detected.");
//        } else {
//            isStoneRed = false;
//            telemetry.addData(">", "Blue stone detected.");
//        }

        robot.getGlyphLift().initializeGrippers();
        robot.getIntake().raiseIntake();
        robot.getJewelKnocker().retractArm();

        telemetry.addData(">", "Press LB for Left, RB for Right, LT for Red, RT for Blue");
        telemetry.update();

        while(!isStarted() && !opModeIsActive()){
            if(gamepad1.left_trigger > 0){
                isStoneRed = true;
            } else if(gamepad1.right_trigger > 0){
                isStoneRed = false;
            }

            if(gamepad1.right_bumper) {
                isStoneRight = true;
            } else if(gamepad1.left_bumper) {
                isStoneRed = false;
            }

            if(isStoneRed) {
                telemetry.addData("team", "Red Alliance Selected.");
            } else {
                telemetry.addData("team", "Blue Alliance Selected.");
            }

            if(isStoneRight) {
                telemetry.addData("stone", "Right Stone Selected");
            } else {
                telemetry.addData("stone", "Left Stone Selected.");
            }

            telemetry.update();
        }

        waitForStart();

        // decide which program to run
        if(isStoneRed) {
            telemetry.addData(">", "Running Red Alliance Program.");
            telemetry.update();

            robot.getGlyphLift().closeRedGripper();

            sleep(500);

            robot.getGlyphLift().setLiftMotorPower(1);
            sleep(250);
            robot.getGlyphLift().setLiftMotorPower(0);

            // Lower Jewel Mechinism
            robot.getJewelKnocker().extendArm();

            sleep(500);

            if(robot.getJewelKnocker().isJewelRed(robot)) {
                robot.getHDriveTrain().directionalDrive(0, 0.5, 3, false); //drive 4 inches right
                robot.getJewelKnocker().retractArm();

                sleep(500);

                robot.getHDriveTrain().directionalDrive(180, 0.5, 7, false);
            } else if(robot.getJewelKnocker().isJewelBlue(robot)) {
                robot.getHDriveTrain().directionalDrive(180, 0.5, 3, false); // drive 4 inches left
            }

            robot.getJewelKnocker().retractArm();

            robot.getHDriveTrain().directionalDrive(0, 1.0, 36, false);

            // drive forward
            if(isStoneRight) {
                robot.getHDriveTrain().directionalDrive(90, 0.5, 24, false);
            }

            robot.getGlyphLift().setLiftMotorPower(-0.5);
            sleep(250);
            robot.getGlyphLift().setLiftMotorPower(0);

            robot.getGlyphLift().openRedGripper();

        } else {
            telemetry.addData(">", "Running Blue Alliance Program.");
            telemetry.update();

            robot.getGlyphLift().closeRedGripper();

            sleep(500);

            robot.getGlyphLift().setLiftMotorPower(1);
            sleep(250);
            robot.getGlyphLift().setLiftMotorPower(0);

            robot.getJewelKnocker().extendArm();

            sleep(500);

            if(robot.getJewelKnocker().isJewelRed(robot)) {
                robot.getHDriveTrain().directionalDrive(180, 0.5, 3, false); // drive 4 inches left
                robot.getJewelKnocker().retractArm();

                sleep(500);

                robot.getHDriveTrain().directionalDrive(0, 0.5, 7, false);
            } else if(robot.getJewelKnocker().isJewelBlue(robot)) {
                robot.getHDriveTrain().directionalDrive(0, 0.5, 3, false); //drive 4 inches right
            }

            robot.getJewelKnocker().retractArm();

            robot.getHDriveTrain().directionalDrive(180, 1.0, 36, false);

            // drive forward
            if(isStoneRight) {
                robot.getHDriveTrain().directionalDrive(90, 0.5, 24, false);
            }

            robot.getGlyphLift().setLiftMotorPower(-0.5);
            sleep(250);
            robot.getGlyphLift().setLiftMotorPower(0);

            robot.getGlyphLift().openRedGripper();
        }

    }

    String format(OpenGLMatrix transformationMatrix) {
        return (transformationMatrix != null) ? transformationMatrix.formatAsTransform() : "null";
    }
    RelicRecoveryVuMark scanPicto(){
        RelicRecoveryVuMark keyColumn = RelicRecoveryVuMark.UNKNOWN;
        VuforiaLocalizer vuforia = robot.getVisionHelper().getVuforia();
        /**
         * Load the data set containing the VuMarks for Relic Recovery. There's only one trackable
         * in this data set: all three of the VuMarks in the game were created from this one template,
         * but differ in their instance id information.
         * @see VuMarkInstanceId
         */
        VuforiaTrackables relicTrackables = vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary

        telemetry.addData(">", "Press Play to start");
        telemetry.update();
        waitForStart();

        relicTrackables.activate();

        while (opModeIsActive()) {

            /**
             * See if any of the instances of {@link relicTemplate} are currently visible.
             * {@link RelicRecoveryVuMark} is an enum which can have the following values:
             * UNKNOWN, LEFT, CENTER, and RIGHT. When a VuMark is visible, something other than
             * UNKNOWN will be returned by {@link RelicRecoveryVuMark#from(VuforiaTrackable)}.
             */
            RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
            if (vuMark != RelicRecoveryVuMark.UNKNOWN) {
                keyColumn = vuMark;

                /* Found an instance of the template. In the actual game, you will probably
                 * loop until this condition occurs, then move on to act accordingly depending
                 * on which VuMark was visible. */
                telemetry.addData("VuMark", "%s visible", vuMark);

                /* For fun, we also exhibit the navigational pose. In the Relic Recovery game,
                 * it is perhaps unlikely that you will actually need to act on this pose information, but
                 * we illustrate it nevertheless, for completeness. */
                OpenGLMatrix pose = ((VuforiaTrackableDefaultListener)relicTemplate.getListener()).getPose();
                telemetry.addData("Pose", format(pose));


                /* We further illustrate how to decompose the pose into useful rotational and
                 * translational components */
                if (pose != null) {
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
                }
            }
            else {
                telemetry.addData("VuMark", "not visible");
            }

            telemetry.update();
        }
        return keyColumn;
    }
}
