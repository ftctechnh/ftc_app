package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;
import java.util.List;

import static org.firstinspires.ftc.teamcode.Alliance.BLUE;
import static org.firstinspires.ftc.teamcode.Alliance.RED;

/**
 * Created by guberti on 10/17/2017.
 */
@Autonomous(name="Full autonomous", group="Autonomous")
public class CompleteAutonomous extends LinearOpMode {

    NullbotHardware robot = new NullbotHardware();
    VuforiaLocalizer vuforia;
    List<VuforiaTrackable> trackables;

    final double INITIAL_DESIRED_HEADING = Math.PI / 2;
    final double TURN_VOLATILITY = 1.5 * Math.PI;
    final double ACCEPTABLE_HEADING_VARIATION = Math.PI / 180; // 1 degree
    final int DISTANCE_TO_DRIVE = 400;
    final int ROTATION = 1120; // 2880 encoder clicks per wheel rotation

    PixyCam pixyCam;
    PixyCam.Block redBall;
    PixyCam.Block blueBall;
    Alliance rightMostBall; // Which alliance the rightmost ball belongs to, from robot POV
    double difference;

    @Override
    public void runOpMode() {

        robot.init(hardwareMap, this, false, gamepad2);

        for (DcMotor m : robot.motorArr) {
            m.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        pixyCam = hardwareMap.get(PixyCam.class, "leftPixyCam");

        telemetry.clearAll();

        telemetry.log().add("Complete autonomous mode");
        telemetry.log().add("Be prepared for robot to MOVE");
        telemetry.log().add("Robot's current alliance is " + robot.color);
        telemetry.log().add("--------------------------");

        while (!isStarted()) {
            updateBlocks();
            telemetry.addData("Red ball:", redBall.toString());
            telemetry.addData("Blue ball:", blueBall.toString());
            telemetry.update();
        }

        telemetry.log().add("Robot started");

        updateBlocks();

        // Higher x-values are on the right

        if (redBall.averageX() > blueBall.averageX()) {
            rightMostBall = RED;
        } else {
            rightMostBall = BLUE;
        }

        telemetry.addData("Rightmost ball:", rightMostBall);

        // Grab and clamp the block in front of us
        robot.closeBlockClaw();
        robot.setLiftMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.lift.setPower(0.5);
        robot.lift.setTargetPosition(-ROTATION / 2);
        robot.sleep(1000);


        // We have now hit the gem

        // We now need to read the pictograph
        initializeVuforia();
        String pictographLocation = null;
        int pictograph = 0;

        while (opModeIsActive() && pictographLocation == null) {

            for (VuforiaTrackable trackable : trackables) {
                boolean seen = ((VuforiaTrackableDefaultListener) trackable.getListener()).isVisible();
                telemetry.addData(trackable.getName(), seen ? "Visible" : "Not Visible");
                if (seen) {
                    pictographLocation = trackable.getName();
                    break;
                }
            }
            telemetry.update();
        }
        switch (pictographLocation) {
            case "leftTarget":
                pictograph = 0;
                break;
            case "centerTarget":
                pictograph = 1;
                break;
            case "rightTarget":
                pictograph = 2;
                break;
        }
        // We now need to move into the corner

        robot.sleep(3000);

        // Up and left three wheel rotations
        robot.frontLeft.setTargetPosition(robot.frontLeft.getTargetPosition() + ROTATION * 3);
        //robot.backLeft.setTargetPosition(robot.backLeft.getTargetPosition() + ROTATION*2);
        //robot.frontRight.setTargetPosition(robot.frontRight.getTargetPosition() + ROTATION*2);
        robot.backRight.setTargetPosition(robot.backRight.getTargetPosition() + ROTATION * 3);

        for (DcMotor m : robot.motorArr) {
            m.setPower(0.35);
        }
        robot.sleep(3000); // Wait for this to be completed

        // Up and right five wheel rotations
        //robot.frontLeft.setTargetPosition(robot.frontLeft.getTargetPosition() + ROTATION*3);
        robot.backLeft.setTargetPosition(robot.backLeft.getTargetPosition() + ROTATION * 3);
        robot.frontRight.setTargetPosition(robot.frontRight.getTargetPosition() + ROTATION * 3);
        //robot.backRight.setTargetPosition(robot.backRight.getTargetPosition() + ROTATION*3);

        robot.sleep(3000); // Wait for this to be completed

        robot.frontLeft.setTargetPosition(robot.frontLeft.getTargetPosition() - ROTATION / 2);
        robot.backLeft.setTargetPosition(robot.backLeft.getTargetPosition() - ROTATION / 2);
        robot.frontRight.setTargetPosition(robot.frontRight.getTargetPosition() - ROTATION / 2);
        robot.backRight.setTargetPosition(robot.backRight.getTargetPosition() - ROTATION / 2);

        robot.sleep(1000); // Wait for this to be completed

        int driveDist = ROTATION + ROTATION / 2 * pictograph;

        robot.frontLeft.setTargetPosition(robot.frontLeft.getTargetPosition() + driveDist);
        robot.backLeft.setTargetPosition(robot.backLeft.getTargetPosition() - driveDist);
        robot.frontRight.setTargetPosition(robot.frontRight.getTargetPosition() - driveDist);
        robot.backRight.setTargetPosition(robot.backRight.getTargetPosition() + driveDist);

        robot.sleep(3000);

        robot.frontLeft.setTargetPosition(robot.frontLeft.getTargetPosition() + ROTATION);
        robot.backLeft.setTargetPosition(robot.backLeft.getTargetPosition() + ROTATION);
        robot.frontRight.setTargetPosition(robot.frontRight.getTargetPosition() + ROTATION);
        robot.backRight.setTargetPosition(robot.backRight.getTargetPosition() + ROTATION);

        robot.sleep(1000);
        robot.lift.setTargetPosition(0);

        robot.sleep(1000);
        robot.openRelicClaw();
        robot.sleep(500);

        robot.frontLeft.setTargetPosition(robot.frontLeft.getTargetPosition() - ROTATION);
        robot.backLeft.setTargetPosition(robot.backLeft.getTargetPosition() - ROTATION);
        robot.frontRight.setTargetPosition(robot.frontRight.getTargetPosition() - ROTATION);
        robot.backRight.setTargetPosition(robot.backRight.getTargetPosition() - ROTATION);
        robot.openRelicClaw();


    }

    public void initializeVuforia() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "Ac4jpF3/////AAAAGYER4VUDLEYGlD++ha+MStuNhKORp/7DQz1D1+tQwcrsMnbQwLqRgpkFtCOIGrZ942gdL179juAJmdXeeH+Dk0pVgxLFq6O0AzY1MS3wS5JHvSLppO9v8W//finYio3hQk+TFKD+qWq9Q1nAZx0bMWFeF6IuIjUPQLioBzC/lYzI/L7oi/AJAbFlf6wue3gDs0dgwrAgpe+JFHTgM3g2+y4hS6O0mcJjobAWSNeRxq9caOGfl/q6f09Eu2EccSmHLAaqje0i70eAIZ4Tbg5C31sPZxBOPTEGTQ9NvFhP4FNAXlvPCdiBt6XYE8P17UzPN72p7lRKyp4xR1oC8B/4dYbivso+rQUed5/H7AnQYOdA";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        VuforiaTrackables pictographAssets = this.vuforia.loadTrackablesFromAsset("FirstRelicRecoveryPictographs");
        VuforiaTrackable blueCryptoBox = pictographAssets.get(0);
        blueCryptoBox.setName("BlueCryptoBox");

        VuforiaTrackable rightPictograph = pictographAssets.get(1);
        rightPictograph.setName("RightTarget");

        VuforiaTrackable centerPictograph = pictographAssets.get(2);
        centerPictograph.setName("CenterTarget");

        VuforiaTrackable leftPictograph = pictographAssets.get(3);
        leftPictograph.setName("LeftTarget");

        trackables = new ArrayList<VuforiaTrackable>();
        trackables.addAll(pictographAssets);

        pictographAssets.activate();
    }

    public void updateBlocks() {
        redBall = pixyCam.GetBiggestBlock(1);
        blueBall = pixyCam.GetBiggestBlock(2);
    }

    public boolean ballPositionsKnown() {
        return redBall.isSeen() && blueBall.isSeen();
    }

    public void knockOffBlocks() {
        if (robot.color == BLUE) {
            robot.lowerLeftWhipSnake();
        } else {
            robot.lowerRightWhipSnake();
        }
        robot.sleep(500);


        int desiredDistance = DISTANCE_TO_DRIVE;

        if (rightMostBall == robot.color) {
            desiredDistance *= -1;
        }

        for (DcMotor m : robot.motorArr) {
            m.setPower(0.2);
        }

        robot.frontLeft.setTargetPosition(desiredDistance);
        robot.backLeft.setTargetPosition(desiredDistance);
        robot.frontRight.setTargetPosition(-desiredDistance);
        robot.backRight.setTargetPosition(-desiredDistance);

        robot.sleep(1000);
        robot.raiseWhipSnake();
        robot.sleep(500);

        for (DcMotor m : robot.motorArr) {
            m.setTargetPosition(0);
        }
    }
}