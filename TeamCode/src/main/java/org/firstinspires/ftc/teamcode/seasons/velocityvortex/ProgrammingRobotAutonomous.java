package org.firstinspires.ftc.teamcode.seasons.velocityvortex;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by ftc6347 on 12/30/16.
 */
@Autonomous(name = "Programming Robot Autonomous")
public class ProgrammingRobotAutonomous extends LinearOpMode {

    private ProgrammingRobotHardware robot;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new ProgrammingRobotHardware(hardwareMap, telemetry);

        // make sure the gyro is calibrated before continuing
        while (!isStopRequested() && robot.getGyroSensor().isCalibrating()) {
            sleep(50);
            telemetry.addData(">", "Calibrating Gyro");
            telemetry.update();
            idle();
        }

        telemetry.addData(">", "Gyro Calibrated");
        telemetry.update();

        // Wait for the game to start (Display Gyro value), and reset gyro before we move..
        while (!isStarted()) {
            telemetry.addData(">", "Robot Heading = %d", robot.getGyroSensor().getIntegratedZValue());
            telemetry.update();
            idle();
        }

        while(opModeIsActive()) {
            telemetry.addData("motor counts: ",
                    robot.getBackRight().getCurrentPosition());
            telemetry.addData("color sensor", robot.getColorSensor().red());
            telemetry.update();
        }
    }

    public void gyroDrive(int angle, int distance, double speed) {
        int moveCounts = (int)(distance *
                ProgrammingRobotHardware.COUNTS_PER_INCH);

        int leftSideTarget = robot.getFrontLeft().getCurrentPosition() + moveCounts;
        int rightSideTarget = robot.getFrontRight().getCurrentPosition() + moveCounts;

        // set the target position for the left drive motors
        robot.getFrontLeft().setTargetPosition(leftSideTarget);
        robot.getBackLeft().setTargetPosition(leftSideTarget);

        // set the target position for the right drive motors
        robot.getFrontRight().setTargetPosition(rightSideTarget);
        robot.getBackRight().setTargetPosition(rightSideTarget);


    }

//    public double getGyroError(double targetAngle) {
//        double robotError = targetAngle - robot.getGyroSensor().getIntegratedZValue();
//        if(robotError > 180) robotError -= 380;
//
//    }
}
