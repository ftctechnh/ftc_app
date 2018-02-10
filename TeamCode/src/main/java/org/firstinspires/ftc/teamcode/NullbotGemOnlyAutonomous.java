package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import static org.firstinspires.ftc.teamcode.Alliance.BLUE;
import static org.firstinspires.ftc.teamcode.Alliance.RED;

/**
 * Created by guberti on 10/17/2017.
 */
@Autonomous(name="Knock off gem", group="Autonomous")
@Disabled
public class NullbotGemOnlyAutonomous extends LinearOpMode {

    public NullbotHardware robot   = new NullbotHardware();

    final int DISTANCE_TO_DRIVE = 400;
    final int PIXYCAM_DATA_POINTS = 50;
    final int MS_TO_GATHER_PIXYCAM_DATA = 3000;
    final double CERTAINTY_THRESHOLD = 10;

    public int directionMultiplier;

    @Override
    public void runOpMode() {

        if (!robot.initialized) {
            robot.init(hardwareMap, this, gamepad1, gamepad2);
        }

        for (DcMotor m : robot.motorArr) {
            m.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        telemetry.clearAll();

        telemetry.log().add("Gem only autonomous mode");
        telemetry.log().add("Be prepared for robot to MOVE");
        telemetry.log().add("Robot's current alliance is " + robot.color);
        telemetry.log().add("--------------------------");

        while (!isStarted()) {
            telemetry.addData("Color sensor red:", robot.colorSensor.red());
            telemetry.addData("Color sensor green:", robot.colorSensor.green());
            telemetry.addData("Color sensor blue:", robot.colorSensor.blue());
            telemetry.update();
        }

        telemetry.log().add("Robot started");
        telemetry.log().add("Reading color sensor values");

        robot.almostLowerWhipSnake();
        robot.sleep(500);
        Alliance rightMostBall = (robot.colorSensor.red() > robot.colorSensor.blue()) ? RED : BLUE;

        telemetry.addData("Rightmost ball:", rightMostBall);

        telemetry.update();

        robot.lowerLeftWhipSnake();

        robot.waitForTick(500);


        int desiredDistance = DISTANCE_TO_DRIVE;

        if (rightMostBall == robot.color) {
            desiredDistance *= -1;
        }
        desiredDistance *= directionMultiplier;


        robot.closeBlockClaw();
        robot.setLiftMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.lift.setPower(0.5);
        robot.lift.setTargetPosition(-500);
        robot.sleep(2000);

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
        robot.sleep(1000);

        robot.lift.setTargetPosition(0);
        robot.sleep(2000);
    }
}
