package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="  encoderFineTuning", group="Testing")
public class encoderFineTuning extends LinearOpMode
{
    Bogg robot;

    @Override
    public void runOpMode()
    {
        robot = new Bogg(hardwareMap, telemetry);
        waitForStart();
        double deltaX = 0;
        double deltaY = 0;
        double deltaS = 0;

        double savedRadius = DriveEngine.robotRadius;
        double savedDiameter = DriveEngine.wheelDiameter;


        while (opModeIsActive())
        {
            double clockTime = robot.averageClockTime;
            deltaX += gamepad1.left_stick_x * clockTime * 6; //6 inches per second
            deltaY -= gamepad1.left_stick_y * clockTime * 6;
            deltaS -= gamepad1.right_stick_x * clockTime /4 * Math.PI;

            telemetry.addData("wheel diameter", DriveEngine.wheelDiameter);
            telemetry.addData("robot radius", DriveEngine.robotRadius);

            telemetry.addData("deltaX", deltaX);
            telemetry.addData("deltaY", deltaY);
            telemetry.addData("deltaS", deltaS);

            telemetry.addData("x inches", robot.driveEngine.xDist());
            telemetry.addData("y inches", robot.driveEngine.yDist());
            telemetry.addData("spin angle", robot.driveEngine.spinAngle());
            telemetry.addData("spin angle divided by pi", robot.driveEngine.spinAngle() / Math.PI);

            robot.driveEngine.moveOnPath(true, new double[]{deltaX, deltaY, deltaS});

            if(gamepad1.left_bumper || gamepad1.right_bumper)
            {
                robot.driveEngine.resetDistances();
                deltaX = deltaY = deltaS = 0;
            }

            if(gamepad1.dpad_up){
                DriveEngine.wheelDiameter += .01;
            }
            else if(gamepad1.dpad_down){
                DriveEngine.wheelDiameter -= .01;
            }
            else if(gamepad1.dpad_right){
                savedDiameter = DriveEngine.wheelDiameter;
            }
            else if(gamepad1.dpad_left){
                DriveEngine.wheelDiameter = savedDiameter;
            }

            if(gamepad1.y){
                DriveEngine.robotRadius += .01;
            }
            else if(gamepad1.a){
                DriveEngine.robotRadius -= .01;
            }
            else if(gamepad1.b){
                savedRadius = DriveEngine.robotRadius;
            }
            else if(gamepad1.x) {
                DriveEngine.robotRadius = savedRadius;
            }

            if(gamepad1.start)
            {
                deltaX = Math.round(deltaX);
                deltaY = Math.round(deltaY);
                deltaS = Math.round(deltaS);
            }

            telemetry.update();
            robot.update();
            idle();
        }
    }

}

