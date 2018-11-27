package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name="autonomousDrive_DropMoveToWall", group = "Testing")
public class autonomousDrive_DropMoveToWall extends LinearOpMode
{
    Bogg robot;
    Auto auto;
    Mode action;

    private enum Mode
    {
        Stop,
        Drop,
        Slide,
        Spin,
        MoveToWall,
        MoveToDepot
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
        auto = new Auto(robot);

        waitForStart();
        action = Mode.Drop;

        while (opModeIsActive())
        {
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
                case MoveToWall:
                    if(auto.isDoneMovingToWall())
                        action = Mode.Stop;
                    else
                        auto.moveToWall();

                default:
                    auto.stop();

            }

            // Display the current values
            telemetry.addData("time: ", auto.getTime());
            telemetry.addData("back encoder inches", robot.driveEngine.back.getCurrentPosition() * DriveEngine.inPerTicks);
            telemetry.addData("brake position: ", robot.brake.getPosition());
            telemetry.addData("target seen", (robot.camera.targetVisible() == null) ? "N/A" : robot.camera.targetVisible().getName());
            telemetry.addData("touch ", robot.sensors.touchBottom.isPressed());
            telemetry.addData("mode", action);
            telemetry.update();
            idle();
        }
        auto.stop();
    }
}

