package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import static org.firstinspires.ftc.teamcode.Alliance.BLUE;
import static org.firstinspires.ftc.teamcode.Alliance.RED;
import static org.firstinspires.ftc.teamcode.NullbotHardware.getAngleDifference;

/**
 * Created by guberti on 10/17/2017.
 */
public class CompleteOldAutonomous extends NullbotGemOnlyAutonomous {

    VuforiaLocalizer vuforia;

    final double ACCEPTABLE_HEADING_VARIATION = Math.PI / 90; // 1 degree
    final int DISTANCE_TO_DRIVE = 400;
    final int ROTATION = 1120; // 2880 encoder clicks per wheel rotation

    VuforiaTrackable relicTemplate;

    @Override
    public void runOpMode() {

        robot.init(hardwareMap, this, gamepad1, gamepad2);
        robot.raiseWhipSnake();

        for (DcMotor m : robot.motorArr) {
            m.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        telemetry.clearAll();

        telemetry.log().add("Complete autonomous mode");
        telemetry.log().add("Be prepared for robot to MOVE");
        telemetry.log().add("Robot's current alliance is " + robot.color);
        telemetry.log().add("--------------------------");

        while (!isStarted()) {
            telemetry.update();
        }

        telemetry.log().add("Robot started");

        robot.crunchBlockClaw();
        // Higher x-values are on the right

        robot.almostLowerWhipSnake();
        robot.sleep(500);
        Alliance rightMostBall = (robot.colorSensor.red() > robot.colorSensor.blue()) ? RED : BLUE;

        telemetry.addData("Rightmost ball:", rightMostBall);
        telemetry.update();

        initializeVuforia();

        int pictograph = 1;

        ElapsedTime timeUntilGuess = new ElapsedTime();
        while (opModeIsActive() && timeUntilGuess.seconds() < 4) {

            RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);

            if (vuMark != RelicRecoveryVuMark.UNKNOWN) {

                telemetry.addData("VuMark", "%s visible", vuMark);

                switch (vuMark) {
                    case RIGHT:
                        pictograph = 2;
                        break;
                    case CENTER:
                        pictograph = 1;
                        break;
                    case LEFT:
                        pictograph = 0;
                        break;
                }
                if (robot.color == Alliance.RED) {
                    pictograph = 2 - pictograph;
                }
                telemetry.update();
                break;
            } else {
                telemetry.addData("VuMark", "not visible");
            }
            telemetry.update();
        }

        if (rightMostBall != Alliance.UNKNOWN) {
            knockOffBalls(rightMostBall);
        }

        log("Lifting lift");
        robot.setLiftMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.lift.setTargetPosition(-500);
        robot.lift.setPower(0.5);
        robot.sleep(1000);

        log("Driving off pad");
        robot.setDriveMode(DcMotor.RunMode.RUN_TO_POSITION);
        for (DcMotor m : robot.motorArr) {
            m.setPower(0.4);

            int driveTicks;
            if (robot.startingPad == StartingPosition.FRONT) {
                driveTicks = 2400;
            } else {
                driveTicks = 2000;
            }

            m.setTargetPosition(m.getCurrentPosition() + (driveTicks * robot.color.getColorCode()));

        }

        // Code to raise the whipsnake early when on the back
        if (robot.startingPad == StartingPosition.BACK) {
            robot.sleep(1000);
            robot.raiseLeftWhipSnake();
        }

        waitUntilMovementsComplete();
        robot.raiseLeftWhipSnake();
        robot.sleep(400);

        if (robot.startingPad == StartingPosition.BACK) {
            turnToPos(Math.PI / 2);
        } else {
            if (robot.color == Alliance.RED) {
                turnToPos(Math.PI);
            } else {

                turnToPos(0);
            }
        }

        log("Driving sideways");

        for (DcMotor m : robot.motorArr) {
            m.setPower(0.4);
        }

        int driveTicks = (-150 + 750 * pictograph) * robot.color.getColorCode();

        if (pictograph == 0 && robot.color == Alliance.RED) {
            driveTicks += 100;
            if (robot.startingPad == StartingPosition.BACK) {
                driveTicks -= 1200;
            }
        } else if (robot.color == Alliance.RED && pictograph == 2) {
            driveTicks += 150;
        } else if (robot.color == Alliance.RED && pictograph == 1 && robot.startingPad == StartingPosition.BACK) {
            driveTicks += 150;
        } else if (robot.color == Alliance.BLUE && pictograph == 0 && robot.startingPad == StartingPosition.BACK) {
            telemetry.log().add("Got here");
            driveTicks += 1675;
        } else { // For blue
            driveTicks -= 75;
        }

        if (robot.color == Alliance.BLUE && robot.startingPad == StartingPosition.BACK) {
            if (pictograph == 0) {
                driveTicks -= 75;
            } else if (pictograph == 1) {
                driveTicks -= 150;
            }

            driveTicks -= 150;
        }

        if (robot.color == Alliance.RED && robot.startingPad == StartingPosition.BACK) {
            driveTicks += 100;
            if (pictograph == 0) { // Right
                driveTicks -= 500; // We turn the other way
            }
            if (pictograph == 1) {
                driveTicks -= 200;
            }
        }

        robot.frontLeft.setTargetPosition(robot.frontLeft.getCurrentPosition() + driveTicks);
        robot.backLeft.setTargetPosition(robot.backLeft.getCurrentPosition() - driveTicks);
        robot.frontRight.setTargetPosition(robot.frontRight.getCurrentPosition() - driveTicks);
        robot.backRight.setTargetPosition(robot.backRight.getCurrentPosition() + driveTicks);

        waitUntilMovementsComplete();

        log("Turning to position");

        double desiredPosition;
        if (robot.color == Alliance.BLUE) {
            desiredPosition = -Math.PI/6;
        } else {
            desiredPosition = Math.PI + Math.PI/6;
        }



        if (robot.startingPad == StartingPosition.BACK) {
            desiredPosition += (Math.PI/2) * robot.color.getColorCode();
            if (robot.color == Alliance.RED && pictograph == 0) {
                desiredPosition -= Math.PI / 3;
            } else if (robot.color == Alliance.BLUE && pictograph == 0) {
                desiredPosition += Math.PI / 3;
            }
        }

        robot.sleep(400); // Give some time for motors to stop moving
        turnToPos(desiredPosition);

        robot.sleep(1000);

        log("Driving at our correct angle");
        robot.setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.setMotorSpeeds(new double[]{0.4, 0.4, 0.4, 0.4});
        robot.sleep(1000);

        robot.setDriveMode(DcMotor.RunMode.RUN_TO_POSITION);
        for (DcMotor m : robot.motorArr) {
            m.setPower(0.3);
            m.setTargetPosition(m.getCurrentPosition() - 50);
        }

        waitUntilMovementsComplete();
        stopMoving();

        robot.lift.setTargetPosition(0);
        robot.sleep(1000);
        robot.openBlockClaw();
        robot.sleep(1000);

        for (DcMotor m : robot.motorArr) {
            m.setPower(0.3);
            m.setTargetPosition(m.getCurrentPosition() - 500);
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

    public void knockOffBalls(Alliance rightMostBall) {

        robot.lowerLeftWhipSnake();
        robot.sleep(500);

        if (rightMostBall == Alliance.RED) {
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

    public void turnToPos(double pos) {
        robot.setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
        double difference = Double.MAX_VALUE;
        ElapsedTime timeHeadingAcceptable = new ElapsedTime();
        double turnSign = 0;

        while(Math.abs(difference) > ACCEPTABLE_HEADING_VARIATION && timeHeadingAcceptable.milliseconds() < 500 && opModeIsActive()) {
            robot.updateReadings();
            telemetry.addData("Heading difference", difference);
            double heading = robot.getGyroHeading();

            difference = getAngleDifference(pos, heading);

            if (turnSign == 0) {
                turnSign = Math.signum(difference);
            }

            double turnSpeed = Math.copySign(difference, turnSign);

            turnSpeed = Math.max(-0.75, Math.min(0.75, turnSpeed));

            // Don't go below 0.2

            if (Math.abs(turnSpeed) < 0.2) {
                turnSpeed = 0.2 * Math.signum(turnSpeed);
            }

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

            if (Math.abs(difference) > ACCEPTABLE_HEADING_VARIATION) {
                timeHeadingAcceptable.reset();
            }
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