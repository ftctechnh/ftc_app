package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import static org.firstinspires.ftc.teamcode.Alliance.BLUE;
import static org.firstinspires.ftc.teamcode.Alliance.RED;

/**
 * Created by guberti on 10/17/2017.
 */
@Autonomous(name="Knock off gem", group="Autonomous")
public class NullbotGemOnlyAutonomous extends LinearOpMode {

    NullbotHardware robot   = new NullbotHardware();

    final int DISTANCE_TO_DRIVE = 480;

    PixyCam pixyCam;
    PixyCam.Block redBall;
    PixyCam.Block blueBall;

    @Override
    public void runOpMode() {

        robot.init(hardwareMap, this, false, gamepad2);

        for (DcMotor m : robot.motorArr) {
            m.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        pixyCam = hardwareMap.get(PixyCam.class, "leftPixyCam");

        telemetry.clearAll();

        telemetry.log().add("Gem only autonomous mode");
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

        Alliance rightMostBall; // Which alliance the rightmost ball belongs to, from robot POV

        // Higher x-values are on the right

        if (redBall.averageX() > blueBall.averageX()) {
            rightMostBall = RED;
        } else {
            rightMostBall = BLUE;
        }

        telemetry.addData("Rightmost ball:", rightMostBall);

        robot.lowerWhipSnake();
        robot.waitForTick(500);


        int desiredDistance = DISTANCE_TO_DRIVE;

        if (rightMostBall == robot.color) {
            desiredDistance *= -1;
        }

        for (DcMotor m : robot.motorArr) {
            m.setTargetPosition(desiredDistance);
        }

        while (opModeIsActive()) {
            for (DcMotor m : robot.motorArr) {
                double toGo = Math.abs(m.getTargetPosition() - m.getCurrentPosition());
                m.setPower(robot.clamp(toGo/100.0));
            }
        }
        robot.raiseWhipSnake();
    }

    public void updateBlocks() {
        redBall = pixyCam.GetBiggestBlock(1);
        blueBall = pixyCam.GetBiggestBlock(2);
    }
}
