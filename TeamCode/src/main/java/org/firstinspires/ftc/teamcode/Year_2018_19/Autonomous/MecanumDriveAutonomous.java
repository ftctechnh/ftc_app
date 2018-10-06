package org.firstinspires.ftc.teamcode.Year_2018_19.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.Year_2018_19.Robot.MecanumDriveRobot;

@Autonomous(name = "MecanumDriveAutonomous", group = "AutonomousMode")
//@Disabled

public class MecanumDriveAutonomous extends LinearOpMode
{
    private MecanumDriveRobot robot = new MecanumDriveRobot();

    public void runOpMode()
    {
        robot.init(hardwareMap);
        robot.playR2D2Sound(this.hardwareMap.appContext);

        if (gamepad1.b && gamepad1.left_stick_button) {robot.alliance = "Red Left Alliance";} //Red Left Alliance
        else if (gamepad1.b && gamepad1.right_stick_button) {robot.alliance = "Red Right Alliance";} //Red Right Alliance
        else if (gamepad1.x && gamepad1.left_stick_button) {robot.alliance = "Blue Left Alliance";} //Blue Left Alliance
        else if (gamepad1.x && gamepad1.right_stick_button) {robot.alliance = "Blue Right Alliance";} //Blue Right Alliance

        telemetry.addData("Status", "Robot has initialied!");
        telemetry.update();

        waitForStart();
        robot.playBB8Sound(this.hardwareMap.appContext);
        telemetry.addData("Status", "Robot has started!");
        telemetry.update();

        if (robot.alliance == "Red Left Alliance")
        {
            BlueLeftAlliance();
        }
        else if (robot.alliance == "Red Right Alliance")
        {
            BlueRightAlliance();
        }
        else if (robot.alliance == "Blue Left Alliance")
        {
            RedLeftAlliance();
        }
        else if (robot.alliance == "Blue Right Alliance")
        {
            RedRightAlliance();
        }

        sleep (1000);

        robot.playMusic(this.hardwareMap.appContext);

        //TODO: Choose and run the alliance here.

        while (opModeIsActive())
        {
            telemetry.addData("Front Left Drive", robot.frontLeftDrive.getPower());
            telemetry.addData("Front Right Drive", robot.frontRightDrive.getPower());
            telemetry.addData("Back Left Drive", robot.backLeftDrive.getPower());
            telemetry.addData("Back Right Drive", robot.backRightDrive.getPower());

            telemetry.addData("Gyro Sensor X", robot.gyroSensor.rawX());
            telemetry.addData("Gyro Sensor Y", robot.gyroSensor.rawY());
            telemetry.addData("Gyro Sensor Z", robot.gyroSensor.rawZ());

            telemetry.addData("Line Follower Red Color", robot.lineFollower.red());
            telemetry.addData("Line Follower Green Color", robot.lineFollower.green());
            telemetry.addData("Line Follower Blue Color", robot.lineFollower.blue());

            telemetry.update();
        }

        robot.safetyStop();
    }

    private void BlueLeftAlliance()
    {

    }

    private void BlueRightAlliance()
    {

    }

    private void RedLeftAlliance()
    {

    }

    private void RedRightAlliance()
    {

    }
}
