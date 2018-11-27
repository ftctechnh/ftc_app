package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name="autonomousDrive_DropPlacePark", group = "Testing")
public class autonomousDrive_DropPlacePark extends LinearOpMode
{
    Bogg robot;
    Auto auto;
    Mode action;
    double x;

    private enum Mode
    {
        Stop,
        Drop,
        Slide,
        Spin,
        MoveToWall,
        MoveToDepot,
        DropMarker,
        MoveToCrater
    }

    private enum StartPosition
    {
        FrontBlue,
        BackBlue,
        FrontRed,
        BackRed
    }
    public StartPosition startPosition;


    @Override
    public void runOpMode()
    {
        robot = new Bogg(hardwareMap, gamepad1, telemetry);
        robot.driveEngine.driveAtAngle(Math.PI);
        action = Mode.Stop;

        waitForStart();
        action = Mode.Drop;

        while (opModeIsActive())
        {
            double mobileDistance = robot.sensors.dMobile.getDistance(DistanceUnit.INCH);
            double fixedDistance = robot.sensors.dFixed.getDistance(DistanceUnit.INCH);
            switch(action)
            {
                case Drop:
                    if(auto.isDoneDropping())
                        action = Mode.Slide;
                    else
                        auto.drop();
                    break;
                case Slide:
                    if(auto.isDoneSliding())
                        action = Mode.Spin;
                    else
                        auto.slide();
                    break;
                case Spin:
                    if(auto.isDoneSpinning())
                        action = Mode.MoveToWall;
                    else
                        auto.spin();
                    break;
                case MoveToWall:
                    if(auto.isDoneMovingToWall())
                        action = Mode.MoveToDepot;
                    else
                        auto.moveToWall();
                    break;
                case MoveToDepot:
                    if(auto.isDoneMovingToDepot())
                        action = Mode.DropMarker;
                    else
                        auto.moveToDepot();
                    break;
                case MoveToCrater:
                    if(auto.isDoneMovingToCrater())
                        action = Mode.Stop;
                    else
                        auto.moveToCrater();
                default:
                    auto.stop();

            }


            // Display the current values
            telemetry.addData("time: ", auto.getTime());
            telemetry.addData("mobile distance: ", mobileDistance);
            telemetry.addData("fixed distance", fixedDistance);
            telemetry.addData("target seen", (robot.camera.targetVisible() == null) ? "N/A" : robot.camera.targetVisible().getName());
            telemetry.addData("brake position", robot.brake.getPosition());
            telemetry.addData("touch ", robot.sensors.touchBottom.isPressed());
            telemetry.addData("mode", action);
            telemetry.update();
            idle();
        }
        auto.stop();
    }
}

