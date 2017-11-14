package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.List;

import static org.firstinspires.ftc.teamcode.Alliance.BLUE;
import static org.firstinspires.ftc.teamcode.Alliance.RED;
import static org.firstinspires.ftc.teamcode.NullbotHardware.getAngleDifference;

/**
 * Created by guberti on 10/17/2017.
 */
@Autonomous(name="Complete FRONT BLUE autonomous", group="Autonomous")
public class CompleteAutonomous extends LinearOpMode {

    NullbotHardware robot = new NullbotHardware();
    VuforiaLocalizer vuforia;
    List<VuforiaTrackable> trackables;

    final double INITIAL_DESIRED_HEADING = Math.PI / 2;
    final double TURN_VOLATILITY = 1.5 * Math.PI;
    final double ACCEPTABLE_HEADING_VARIATION = Math.PI / 90; // 1 degree
    final int DISTANCE_TO_DRIVE = 400;
    final int ROTATION = 1120; // 2880 encoder clicks per wheel rotation

    PixyCam pixyCam;
    PixyCam.Block redBall;
    PixyCam.Block blueBall;
    Alliance rightMostBall; // Which alliance the rightmost ball belongs to, from robot POV

    VuforiaTrackable relicTemplate;

    @Override
    public void runOpMode() {

        robot.init(hardwareMap, this, gamepad1, gamepad2);

        robot.color = Alliance.RED;

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
        telemetry.update();

        initializeVuforia();

        int pictograph = 1;

        ElapsedTime timeUntilGuess = new ElapsedTime();
        while (opModeIsActive() && timeUntilGuess.seconds() < 5) {

            RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);

            if (vuMark != RelicRecoveryVuMark.UNKNOWN) {

                telemetry.addData("VuMark", "%s visible", vuMark);

                switch (vuMark) {
                    case RIGHT:
                        pictograph = 0;
                        break;
                    case CENTER:
                        pictograph = 1;
                        break;
                    case LEFT:
                        pictograph = 2;
                        break;
                }
                telemetry.update();
                break;
            } else {
                telemetry.addData("VuMark", "not visible");
            }
            telemetry.update();
        }

        // Grab and clamp the block in front of us
        robot.crunchBlockClaw();
        robot.setLiftMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.lift.setPower(0.5);
        robot.lift.setTargetPosition(-2500);
        robot.sleep(2000);

        robot.setDriveMode(DcMotor.RunMode.RUN_TO_POSITION);
        for (DcMotor m : robot.motorArr) { m.setPower(0.35); }

        if (ballPositionsKnown()) {
            knockOffBalls();
        }

        // Drive backwards off the pad
        robot.frontLeft.setTargetPosition(robot.frontLeft.getCurrentPosition() + 2000);
        robot.backLeft.setTargetPosition(robot.backLeft.getCurrentPosition() - 2000);
        robot.frontRight.setTargetPosition(robot.frontRight.getCurrentPosition() - 2000);
        robot.backRight.setTargetPosition(robot.backRight.getCurrentPosition() + 2000);

        log("Finished setting motors to drive sideways");
        waitUntilMovementsComplete();

        turnToPos(0);
        for (DcMotor m : robot.motorArr) { m.setPower(0.35); }
        log("Corrected heading");

        robot.frontLeft.setTargetPosition(robot.frontLeft.getCurrentPosition() + 1600);
        robot.backLeft.setTargetPosition(robot.backLeft.getCurrentPosition() + 1600);
        robot.frontRight.setTargetPosition(robot.frontRight.getCurrentPosition() + 1600);
        robot.backRight.setTargetPosition(robot.backRight.getCurrentPosition() + 1600);

        log("Moving forward...");

        waitUntilMovementsComplete();

        robot.frontLeft.setTargetPosition(robot.frontLeft.getCurrentPosition() + (int) (ROTATION * 3.5));
        robot.backRight.setTargetPosition(robot.backRight.getCurrentPosition() + (int) (ROTATION * 3.5));
        log("Ramming wall...");

        waitUntilMovementsComplete();

        turnToPos(0);
        for (DcMotor m : robot.motorArr) { m.setPower(0.35); }

        robot.frontLeft.setTargetPosition(robot.frontLeft.getCurrentPosition() - 800);
        robot.backLeft.setTargetPosition(robot.backLeft.getCurrentPosition() - 800);
        robot.frontRight.setTargetPosition(robot.frontRight.getCurrentPosition() - 800);
        robot.backRight.setTargetPosition(robot.backRight.getCurrentPosition() - 800);

        waitUntilMovementsComplete();
        // Move into corner

        robot.backLeft.setPower(0.6);
        robot.backRight.setPower(0);
        robot.frontLeft.setPower(0);
        robot.frontRight.setPower(0.6);

        // Up and right five wheel rotations
        robot.backLeft.setTargetPosition(robot.backLeft.getCurrentPosition() + (int) (ROTATION * 2.5));
        robot.backRight.setTargetPosition(robot.backRight.getCurrentPosition());
        robot.frontLeft.setTargetPosition(robot.frontLeft.getCurrentPosition());
        robot.frontRight.setTargetPosition(robot.frontRight.getCurrentPosition() + (int) (ROTATION * 2.5));

        log("Ramming corner...");

        waitUntilMovementsComplete();

        turnToPos(0);
        for (DcMotor m : robot.motorArr) { m.setPower(0.35); }

        robot.frontLeft.setTargetPosition(robot.frontLeft.getCurrentPosition() - ROTATION);
        robot.backLeft.setTargetPosition(robot.backLeft.getCurrentPosition() - ROTATION);
        robot.frontRight.setTargetPosition(robot.frontRight.getCurrentPosition() - ROTATION);
        robot.backRight.setTargetPosition(robot.backRight.getCurrentPosition() - ROTATION);
        log("Moving to correct plane...");

        waitUntilMovementsComplete();

        int driveDist = 1000 + pictograph * 630;

        robot.frontLeft.setTargetPosition(robot.frontLeft.getCurrentPosition() - driveDist);
        robot.backLeft.setTargetPosition(robot.backLeft.getCurrentPosition() + driveDist);
        robot.frontRight.setTargetPosition(robot.frontRight.getCurrentPosition() + driveDist);
        robot.backRight.setTargetPosition(robot.backRight.getCurrentPosition() - driveDist);

        waitUntilMovementsComplete();

        robot.frontLeft.setTargetPosition(robot.frontLeft.getCurrentPosition() + 840);
        robot.backLeft.setTargetPosition(robot.backLeft.getCurrentPosition() + 840);
        robot.frontRight.setTargetPosition(robot.frontRight.getCurrentPosition() + 840);
        robot.backRight.setTargetPosition(robot.backRight.getCurrentPosition() + 840);

        waitUntilMovementsComplete();
        robot.lift.setTargetPosition(0);

        robot.sleep(1000);
        robot.openBlockClaw();
        robot.sleep(500);

        robot.frontLeft.setTargetPosition(robot.frontLeft.getCurrentPosition() - ROTATION/2);
        robot.backLeft.setTargetPosition(robot.backLeft.getCurrentPosition() - ROTATION/2);
        robot.frontRight.setTargetPosition(robot.frontRight.getCurrentPosition() - ROTATION/2);
        robot.backRight.setTargetPosition(robot.backRight.getCurrentPosition() - ROTATION/2);

        waitUntilMovementsComplete();
    }

    public void initializeVuforia() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        parameters.vuforiaLicenseKey = "Ac4jpF3/////AAAAGYER4VUDLEYGlD++ha+MStuNhKORp/7DQz1D1+tQwcrsMnbQwLqRgpkFtCOIGrZ942gdL179juAJmdXeeH+Dk0pVgxLFq6O0AzY1MS3wS5JHvSLppO9v8W//finYio3hQk+TFKD+qWq9Q1nAZx0bMWFeF6IuIjUPQLioBzC/lYzI/L7oi/AJAbFlf6wue3gDs0dgwrAgpe+JFHTgM3g2+y4hS6O0mcJjobAWSNeRxq9caOGfl/q6f09Eu2EccSmHLAaqje0i70eAIZ4Tbg5C31sPZxBOPTEGTQ9NvFhP4FNAXlvPCdiBt6XYE8P17UzPN72p7lRKyp4xR1oC8B/4dYbivso+rQUed5/H7AnQYOdA";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;

        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary

        relicTrackables.activate();

    }

    public void updateBlocks() {
        redBall = pixyCam.GetBiggestBlock(1);
        blueBall = pixyCam.GetBiggestBlock(2);
    }

    public boolean ballPositionsKnown() {
        return redBall.isSeen() && blueBall.isSeen();
    }

    public void knockOffBalls() {

        robot.lowerLeftWhipSnake();

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
    public void turnToPos(double pos) {
        robot.setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
        double difference = Double.MAX_VALUE;


        while(Math.abs(difference) > ACCEPTABLE_HEADING_VARIATION && opModeIsActive()) {
            telemetry.addData("Heading difference", difference);
            double heading = robot.getGyroHeading();

            difference = getAngleDifference(pos, heading);
            double turnSpeed = difference;
            turnSpeed = Math.max(-0.5, Math.min(0.5, turnSpeed));

            double[] unscaledMotorPowers = new double[4];
            telemetry.addData("Turnspeed", turnSpeed);

            for (int i = 0; i < unscaledMotorPowers.length; i++) {
                if (i % 2 == 0) {
                    unscaledMotorPowers[i] = turnSpeed;
                } else {
                    unscaledMotorPowers[i] = -turnSpeed;
                }
            }
            telemetry.addData("M1", unscaledMotorPowers[0]);
            telemetry.addData("M2", unscaledMotorPowers[1]);
            telemetry.addData("M3", unscaledMotorPowers[2]);
            telemetry.addData("M4", unscaledMotorPowers[3]);

            telemetry.update();

            robot.setMotorSpeeds(unscaledMotorPowers);
        }
        robot.setDriveMode(DcMotor.RunMode.RUN_TO_POSITION);
        stopMoving();
    }

    public void stopMoving() {
        for (DcMotor m : robot.motorArr) {
            m.setPower(0);
        }
    }
    public void log(String s) {
        telemetry.log().add(s);
    }
    public void waitUntilMovementsComplete() {
        boolean done = false;

        while (!done) {
            done = true;
            for (DcMotor m : robot.motorArr) {
                if (Math.abs(m.getTargetPosition() - m.getCurrentPosition()) > 75) {
                    done = false;
                    break;
                }
            }

        }
        robot.sleep(500);
    }
}