package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.HashMap;
import java.util.Map;

import static org.firstinspires.ftc.teamcode.Alliance.BLUE;
import static org.firstinspires.ftc.teamcode.Alliance.RED;
import static org.firstinspires.ftc.teamcode.NullbotHardware.clamp;

/**
 * Created by guberti on 10/17/2017
 */
/*@Autonomous(name="Test Ultrasonic Nav", group="Demo")*/
public class NavigatorAutonomous extends NullbotGemOnlyAutonomous {

    final double ACCEPTABLE_HEADING_VARIATION = Math.PI / 45; // 1 degree
    final int GUESS_TIME = 2000; // Milliseconds

    final int TICKS_PER_INCH = 100; // For sideways movement
    final double CM_TO_INCHES = 1/2.54;

    Map<RelicRecoveryVuMark, Double[]> COLUMN_DISTANCES = new HashMap<>();

    VuforiaLocalizer vuforia;
    VuforiaTrackable relicTemplate;

    @Override
    public void runOpMode() {

        // Initialize variables
        COLUMN_DISTANCES.put(RelicRecoveryVuMark.LEFT, new Double[] {47.5, 33.0});
        COLUMN_DISTANCES.put(RelicRecoveryVuMark.CENTER, new Double[] {47.5 + 7.625, 39.0});
        COLUMN_DISTANCES.put(RelicRecoveryVuMark.RIGHT, new Double[] {47.5 + 2 * 7.625, 45.0});

        robot.init(hardwareMap, this, gamepad1, gamepad2);
        for (DcMotor m : robot.motorArr) {
            m.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            if (m instanceof DcMotorEx) {
                // Set the PID coefficients
                DcMotorEx mX = (DcMotorEx) m;

                // Quadruple the integral component
                mX.getPIDCoefficients(DcMotor.RunMode.RUN_USING_ENCODER).i *= 4;
                mX.getPIDCoefficients(DcMotor.RunMode.RUN_TO_POSITION).i *= 4;
                telemetry.log().add("Updated PID coefficients for a motor");
            }
        }

        initializeVuforia();
        RelicRecoveryVuMark vuMark;

        do { // Ensure this code runs at least once
            vuMark = RelicRecoveryVuMark.from(relicTemplate);
            telemetry.addData("Front raw cm: ", voltageToInches(robot.frontUltrasonic));
            telemetry.addData("Left raw cm: ", voltageToInches(robot.leftUltrasonic));
            telemetry.addData("Right raw cm: ", voltageToInches(robot.rightUltrasonic));
            telemetry.addData("VuMark: ", vuMark.toString());
            telemetry.update();
        } while (!isStarted());

        // Initialize various robot mechanisms
        robot.closeBlockClaw();
        robot.raiseIntake();
        robot.setLiftMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.lift.setTargetPosition(200);
        robot.lift.setPower(1.0);
        robot.almostLowerWhipSnake();

        // If we've already got the pictograph, we'll just skip this
        ElapsedTime timeUntilGuess = new ElapsedTime();
        while (vuMark == RelicRecoveryVuMark.UNKNOWN && opModeIsActive() &&
                timeUntilGuess.milliseconds() < GUESS_TIME) {
            vuMark = RelicRecoveryVuMark.from(relicTemplate);
        }

        if (vuMark == RelicRecoveryVuMark.UNKNOWN) { // We have to make a guess
            vuMark = RelicRecoveryVuMark.CENTER;
        }

        // Any time that we already spent looking for the pictograph can be discounted here
        // We have to sleep to have time to raise the lift, lower the whipsnake, etc.
        robot.sleep((long) (Math.max(0, 500 - timeUntilGuess.milliseconds())));

        Alliance rightBall = (robot.colorSensor.red() > robot.colorSensor.blue()) ? RED : BLUE;
        knockOffBalls(rightBall);

        // Drive forward to approximately correct position
        robot.setDriveMode(DcMotor.RunMode.RUN_TO_POSITION);
        for (DcMotor m : robot.motorArr) {
            m.setPower(0.75);
            m.setTargetPosition((int) Math.round(COLUMN_DISTANCES.get(vuMark)[1]*TICKS_PER_INCH) *
            robot.color.getColorCode()); // Inverts distance if on red
        }

        robot.sleep(500);
        robot.raiseWhipSnake();
        waitUntilMovementsComplete();

        turnToPos(Math.PI/2);
        stopMoving();

        while (opModeIsActive()) {
            robot.sleep(700);
            double currentDist;

            if (robot.color == Alliance.BLUE) {
                currentDist = voltageToInches(robot.leftUltrasonic);
            } else {
                currentDist = voltageToInches(robot.rightUltrasonic);
            }

            telemetry.log().add("Current left distance: " + currentDist);
            telemetry.log().add("Targeting a distance of " +  COLUMN_DISTANCES.get(vuMark)[0]);
            double difference = COLUMN_DISTANCES.get(vuMark)[0] - currentDist;
            telemetry.log().add("Off by " + difference + " inches");

            if (Math.abs(difference) < CM_TO_INCHES) {
                break;
            } else {
                driveSidewaysInches(difference);
            }
            stopMoving();
            turnToPos(Math.PI/2);
        }

        driveStraight(Math.PI/2, 0.6, 2000);
        robot.openBlockClaw();
        robot.sleep(400); // Give time for claw to open
        robot.lift.setTargetPosition(0); // Bring lift back to ground

        // Ram the blocks in somewhere
        driveStraight(Math.PI/2, -0.6, 500);
        driveStraight(Math.PI/2, 0.6, 1000);

        // Turn around to get more blocks
        driveStraight(Math.PI/2, -0.6, 1000); // Back up
        turnToPos(Math.PI*1.5);

        stopMoving();


    }

    public void driveSidewaysInches(double inches) { // How much to the right?
        robot.setDriveMode(DcMotor.RunMode.RUN_TO_POSITION);
        for (DcMotor m : robot.motorArr) {
            m.setPower(0.5);
        }
        int ticks = (int) Math.round(inches*TICKS_PER_INCH);
        telemetry.log().add("Driving sideways " + ticks + " ticks");

        robot.frontLeft.setTargetPosition(robot.frontLeft.getCurrentPosition() + ticks);
        robot.backLeft.setTargetPosition(robot.backLeft.getCurrentPosition() - ticks);
        robot.frontRight.setTargetPosition(robot.frontRight.getCurrentPosition() - ticks);
        robot.backRight.setTargetPosition(robot.backRight.getCurrentPosition() + ticks);

        waitUntilMovementsComplete();
    }
    public void driveStraight(double lockedHeading, double speed, double milliseconds) {

        // Schedule voltage updates

        ElapsedTime runTime = new ElapsedTime();
        while (runTime.milliseconds() < milliseconds) {
            robot.updateReadings();
            double difference = robot.getSignedAngleDifference(lockedHeading, robot.getGyroHeading());
            double turnSpeed = difference / (Math.PI);
            turnSpeed = clamp(turnSpeed);

            telemetry.addData("Turnspeed: ", turnSpeed);

            double[] powers = new double[4];

            for (int i = 0; i < powers.length; i++) {
                powers[i] = speed;

                if (i % 2 == 0) {
                    powers[i] += turnSpeed;
                } else {
                    powers[i] -= turnSpeed;
                }
            }

            robot.setMotorSpeeds(powers);
        }
    }

    public double voltageToInches(AnalogInput sensor) {
        return (546.45 * sensor.getVoltage() + 3.9075) * CM_TO_INCHES;
        /*double cm = sensor.getVoltage() * 1024.0 / sensor.getMaxVoltage();
        return Math.round(cm) * CM_TO_INCHES; // Convert to inches*/
    }

    public void turnToPos(double pos) {
        double difference = Double.MAX_VALUE;
        robot.setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);

        while(Math.abs(difference) > ACCEPTABLE_HEADING_VARIATION && opModeIsActive()) {
            robot.updateReadings();

            difference = robot.getSignedAngleDifference(robot.normAngle(pos), robot.getGyroHeading());
            double turnSpeed = Math.max(-0.6, Math.min(0.6, difference));

            double[] unscaledMotorPowers = new double[4];

            for (int i = 0; i < unscaledMotorPowers.length; i++) {
                if (i % 2 == 0) {
                    unscaledMotorPowers[i] = turnSpeed;
                } else {
                    unscaledMotorPowers[i] = -turnSpeed;
                }
            }
            telemetry.update();

            robot.setMotorSpeeds(unscaledMotorPowers);
        }
        stopMoving();
    }

    public void stopMoving() {
        for (DcMotor m : robot.motorArr) {
            m.setPower(0);
        }
    }

    public void waitUntilMovementsComplete() {
        boolean done = false;

        while (!done) {
            done = true;
            for (DcMotor m : robot.motorArr) {
                if (Math.abs(m.getTargetPosition() - m.getCurrentPosition()) > 5) {
                    done = false;
                    break;
                }
            }
        }
    }

    public void knockOffBalls(Alliance rightMostBall) {

        robot.lowerLeftWhipSnake();
        robot.sleep(200);

        if (rightMostBall == RED) {
            return;
        }

        robot.sleep(500);

        robot.setDriveMode(DcMotor.RunMode.RUN_TO_POSITION);

        for (DcMotor m : robot.motorArr) {
            m.setPower(0.2);
        }

        int driveNum = DISTANCE_TO_DRIVE * robot.color.getColorCode();
        robot.frontLeft.setTargetPosition(-driveNum);
        robot.backLeft.setTargetPosition(-driveNum);
        robot.frontRight.setTargetPosition(driveNum);
        robot.backRight.setTargetPosition(driveNum);

        waitUntilMovementsComplete();
        robot.raiseWhipSnake();
        robot.sleep(500);

        for (DcMotor m : robot.motorArr) {
            m.setTargetPosition(0);
        }
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
}